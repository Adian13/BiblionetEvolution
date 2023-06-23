package it.unisa.c07.biblionet.gestioneclubdellibro.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import it.unisa.c07.biblionet.common.UtenteRegistrato;
import it.unisa.c07.biblionet.common.UtenteRegistratoDTO;
import it.unisa.c07.biblionet.events.CreateLettore;
import it.unisa.c07.biblionet.events.MiddleEsperto;
import it.unisa.c07.biblionet.gestioneclubdellibro.*;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.ClubDelLibro;
import it.unisa.c07.biblionet.utils.BiblionetConstraints;
import it.unisa.c07.biblionet.utils.BiblionetResponse;
import it.unisa.c07.biblionet.utils.Utils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Evento;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Esperto;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Lettore;
import lombok.RequiredArgsConstructor;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

/**
 * Implementa il controller per il sottosistema
 * ClubDelLibro.
 *
 * @author Viviana Pentangelo
 * @author Gianmario Voria
 * @author Nicola Pagliara
 * @author Luca Topo
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/club-del-libro")
public class ClubDelLibroController {

    private final ClubDelLibroService clubService;
    private final GestioneEventiService eventiService;

    /**
     * Implementa la funzionalità di login come utente.
     * @param email
     * @param password
     * @return rimanda alla pagina di home.
     */
    @PostMapping(value = "/login")
    @CrossOrigin
    @ResponseBody
    public BiblionetResponse login(@RequestParam String email,
                                   @RequestParam String password) {

        UtenteRegistrato utente = clubService.loginUtente(email, password);

        if (utente == null) {
            return new BiblionetResponse("Login fallito.", false);
        } else {
            return new BiblionetResponse("", true);
        }
    }

    @PostMapping(value = "/conferma-modifica-lettore")
    @ResponseBody
    @CrossOrigin
    public BiblionetResponse modificaDatiLettore(
            final @RequestHeader(name = "Authorization") String token,
            final @Valid @RequestParam("Lettore") LettoreDTO lettore,
            BindingResult bindingResult,
            final @RequestParam("vecchia_password") String vecchia,
            final @RequestParam("nuova_password") String nuova,
            final @RequestParam("conferma_password") String conferma) {



        if (!Utils.isUtenteLettore(Utils.getSubjectFromToken(token)))
            return new BiblionetResponse(BiblionetResponse.NON_AUTORIZZATO, false);

        if (!Utils.getSubjectFromToken(token).equals(lettore.getEmail()))
            return new BiblionetResponse("Non puoi cambiare email", false); //todo non si può modificare la mail, va fatto anche per lettore e biblioteca sto controllo

        //todo tecnicamente non controllo se è un lettore
        if(clubService.loginUtente(lettore.getEmail(), vecchia) == null) return new BiblionetResponse(BiblionetResponse.NON_AUTORIZZATO, false);
        String password = (BiblionetConstraints.confrontoPassword(nuova, conferma));
        if(password.isEmpty()) return new BiblionetResponse(BiblionetResponse.RICHIESTA_NON_VALIDA, false);
        lettore.setPassword(password);
        String s = controlliPreliminari(bindingResult, vecchia, lettore);
        if (!s.isEmpty()) return new BiblionetResponse(s, false);

        clubService.aggiornaLettoreDaModel(lettore);
        return new BiblionetResponse("Dati aggiornati", true);
    }

    /**
     * Implementa la funzionalità di modifica dati di un esperto.
     *
     * @param esperto         Un esperto da modificare.
     * @param vecchia         La vecchia password dell'account.
     * @param nuova           La nuova password dell'account.
     * @param conferma        La password di conferma password dell'account.
     * @param emailBiblioteca L'email della biblioteca scelta.
     * @return login Se la modifica va a buon fine.
     * modifica_dati_esperto Se la modifica non va a buon fine
     */
    @PostMapping(value = "/conferma-modifica-esperto")
    @ResponseBody
    @CrossOrigin
    public BiblionetResponse modificaDatiEsperto(
            final @RequestHeader(name = "Authorization") String token,
            final @Valid @RequestParam("Esperto") EspertoDTO esperto,
            BindingResult bindingResult,
            final @RequestParam("vecchia_password") String vecchia,
            final @RequestParam("nuova_password") String nuova,
            final @RequestParam("conferma_password") String conferma,
            final @RequestParam("email_biblioteca") String emailBiblioteca) {



        if (!Utils.isUtenteEsperto(Utils.getSubjectFromToken(token)))
            return new BiblionetResponse(BiblionetResponse.NON_AUTORIZZATO, false);

        if (!Utils.getSubjectFromToken(token).equals(esperto.getEmail()))
            return new BiblionetResponse("Non puoi cambiare email", false); //todo non si può modificare la mail, va fatto anche per lettore e biblioteca sto controllo

        //todo tecnicamente non controllo se è un esperto
        if(clubService.loginUtente(esperto.getEmail(), vecchia) == null) return new BiblionetResponse(BiblionetResponse.NON_AUTORIZZATO, false);
        String password = (BiblionetConstraints.confrontoPassword(nuova, conferma));
        if(password.isEmpty()) return new BiblionetResponse(BiblionetResponse.RICHIESTA_NON_VALIDA, false);
        esperto.setPassword(password);
        String s = controlliPreliminari(bindingResult, vecchia, esperto);
        if (!s.isEmpty()) return new BiblionetResponse(s, false);

        clubService.aggiornaEspertoDaModel(esperto, emailBiblioteca); //todo qualche check in più sull'esistenza dell'esperto, anche se se ha il token è autoamticamente registrato

        return new BiblionetResponse("Dati aggiornati", true);
    }

    /**
     * Implementa la funzionalità di registrazione di un esperto.
     */
    @PostMapping(value = "/esperto")
    @ResponseBody
    @CrossOrigin
    public BiblionetResponse registrazioneEsperto(final @Valid @ModelAttribute EspertoDTO esperto,
                                                  BindingResult bindingResult,
                                                  final @RequestParam("conferma_password") String password,
                                                  final @RequestParam("email_biblioteca") String bibliotecaEmail) {

        String s = controlliPreliminari(bindingResult, password, esperto);
        if (!s.isEmpty()) {
            return new BiblionetResponse(s, false);
        }
        clubService.creaEspertoDaModel(esperto, bibliotecaEmail);
        return new BiblionetResponse("Registrazione ok", true);
    }

    /**
     * Implementa la funzionalità di registrazione di
     * un lettore.
     * Gestisce la chiamata POST
     * per creare un nuovo lettore.
     *
     * @param lettore  Il lettore da registrare
     * @param password il campo conferma password del form per controllare
     *                 il corretto inserimento della stessa.
     * @return La view per effettuare il login
     */
    @PostMapping(value = "/lettore")
    @ResponseBody
    @CrossOrigin
    public BiblionetResponse registrazioneLettore(@Valid @ModelAttribute LettoreDTO lettore,
                                                  BindingResult bindingResult,
                                                  final @RequestParam("conferma_password")
                                                  String password
    ) {
        String s = controlliPreliminari(bindingResult, password, lettore);
        if (!s.isEmpty()) return new BiblionetResponse(s, false);

        clubService.creaLettoreDaModel(lettore);
        return new BiblionetResponse("Registrazione effettuata correttamente", true);
    }

    private String controlliPreliminari(BindingResult bindingResult, String password, UtenteRegistratoDTO utenteRegistrato) {
        if (bindingResult.hasErrors()) {
            return "Errore di validazione";
        }
        if (!BiblionetConstraints.passwordRispettaVincoli(utenteRegistrato.getPassword(), password)) {
            return "Password non adeguata";
        }
        return "";
    }

    /**
     * Metodo di utilità che modifica o crea un evento, validando
     * i dati.
     *
     * @param eventoDTO Il form con i dati da modificare
     * @param idClub     L'id del club del libro in cui inserire l'evento.
     * @param idEvento   L'id dell'evento, che può essere vuoto per ottenere
     *                   l'autoassegnazione.
     * @param operazione L'operazione, tra creazione e modifica, che si vuole
     *                   effettuare.
     * @return La view inserita.
     */
    private BiblionetResponse modificaCreaEvento(final EventoDTO eventoDTO, BindingResult bindingResult,
                                                 //@RequestParam final String view,
                                                 final int idClub, final Optional <Integer> idEvento, final Consumer < Evento > operazione) {

        if (bindingResult.hasErrors())
            return new BiblionetResponse("I dati inseriti non rispettano il formato atteso", false);
        var club = this.clubService.getClubByID(idClub);

        if (club == null) {
            return new BiblionetResponse("", false);
        }

        var evento = new Evento();

        if (idEvento.isPresent()) {
            evento.setIdEvento(idEvento.get());
        }

        evento.setClub(club);
        evento.setNomeEvento(eventoDTO.getNome());
        evento.setDescrizione(eventoDTO.getDescrizione());

        var dataOra = LocalDateTime.of(eventoDTO.getData(), eventoDTO.getOra());
        if (dataOra.isBefore(LocalDateTime.now())) {
            return new BiblionetResponse("Data non valida", false);
        }

        evento.setDataOra(dataOra);

        if (eventoDTO.getLibro() != null) {
            var libro = this.eventiService.getLibroById(eventoDTO.getLibro());
            if (libro.isEmpty()) {
                return new BiblionetResponse("Libro inserito non valido", false);
            }
            evento.setLibro(libro.get());
        }

        operazione.accept(evento);
        return new BiblionetResponse("Evento creato/modificato", true);

    }

    /**
     * Implementa la funzionalità che permette
     * di visualizzare i Club del Libro
     * presenti nel Database.
     *
     * @param generi Un Optional che contiene una lista di generi per cui
     *               filtrare
     * @param citta  Un Optional che contiene una lista di possibili città
     * @return La pagina di visualizzazione
     */
    @GetMapping(value = "")
    @ResponseBody
    @CrossOrigin
    public List < Object > visualizzaListaClubs(@RequestParam(value = "generi") final Optional < List < String >> generi,
                                                @RequestParam(value = "citta") final Optional < List < String >> citta) {

        // Molto più pulito della concatenazione con gli stream
        Predicate <ClubDelLibro> filtroGenere = x -> true;

        if (generi.isPresent()) {
            filtroGenere = x -> false;

            var generiDaDB = generi.get();

            for (String genere: generiDaDB) {
                filtroGenere = filtroGenere.or(c -> c.getGeneri().contains(genere));
            }
        }

        Predicate < ClubDelLibro > filtroCitta = x -> true;

        if (citta.isPresent()) {
            filtroCitta = x -> false;
            for (String cittaSingola: citta.get()) {
                filtroCitta = filtroCitta.or(c -> clubService.getCittaFromClubDelLibro(c).equals(cittaSingola));
            }
        }

        List < ClubDelLibro > listaClubs = clubService.visualizzaClubsDelLibro(filtroCitta.and(filtroGenere));

        // Necessito di un oggetto anonimo per evitare problemi con JS
        return listaClubs.stream().map(club -> new Object() {
            public final String nome = club.getNome();
            public final String descrizione = club.getDescrizione();
            public final String nomeEsperto = club.getEsperto().getNome() + " " + club.getEsperto().getCognome();
            public final String immagineCopertina = club.getImmagineCopertina();
            public final Set < String > generi = club.getGeneri();
            public final int idClub = club.getIdClub();
            public final int iscritti = club.getLettori().size();
            public final String email = club.getEsperto().getEmail();
        }).collect(Collectors.toList());

        //model.addAttribute("generi", this.clubService.getTuttiGeneri()); todo
        // model.addAttribute("citta", this.clubService.getCitta()); todo

    }

    /**
     * Implementa la funzionalità di visualizzare la pagina di creazione di
     * un club del libro.
     * @param model L'oggetto model usato per inserire gli attributi
     * @param club Il form in cui inserire i dati del club
     * @return La pagina del Club

     @GetMapping(value = "crea")
     public String visualizzaCreaClubDelLibro(final Model model,
     final @ModelAttribute
     ClubForm club) {
     var utente = (UtenteRegistrato) model.getAttribute("loggedUser");
     if (utente == null || !utente.getTipo().equals("Esperto")) {
     throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
     }
     model.addAttribute("generi", this.clubService.getTuttiGeneri());
     model.addAttribute("club", club);

     return "club-del-libro/creazione-club";
     }
     todo non è chiaro se serva
     */

    /**
     * Implementa la funzionalità di creazione di un club del libro.
     *
     * @param clubDTO Il club che si vuole creare
     * @return la pagina del Club
     */
    @PostMapping(value = "/crea")
    @CrossOrigin
    @ResponseBody
    public BiblionetResponse creaClubDelLibro(final @Valid @ModelAttribute ClubDTO clubDTO,
                                              @RequestHeader(name = "Authorization") final String token,
                                              BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return new BiblionetResponse(BiblionetResponse.FORMATO_NON_VALIDO, false);
        }
        if (!Utils.isUtenteEsperto(token)) {
            return new BiblionetResponse(BiblionetResponse.NON_AUTORIZZATO, false);
        }
        Esperto esperto = clubService.findEspertoByEmail(Utils.getSubjectFromToken(token));

        ClubDelLibro cdl = new ClubDelLibro();
        cdl.setNome(clubDTO.getNome());
        cdl.setDescrizione(clubDTO.getDescrizione());
        cdl.setEsperto(esperto);
        String copertina = getBase64Image(clubDTO.getCopertina());
        if (copertina != null) cdl.setImmagineCopertina(copertina);

        cdl.setGeneri(new HashSet<>(clubDTO.getGeneri()));

        ClubDelLibro clubDelLibro =  clubService.creaClubDelLibro(cdl);
        System.err.println(clubDelLibro.getNome());
        if(clubDelLibro == null) return new BiblionetResponse(BiblionetResponse.ERRORE, false);
        return new BiblionetResponse("Club del Libro creato", true);

    }


    /**
     * Implementa la funzionalità di visualizzazione dei clubs
     * a cui il lettore é iscritto.
     *
     * @return La view di visualizzazione dei clubs a cui é iscritto
     */
    @GetMapping(value = "area-utente/visualizza-clubs-personali-lettore")
    @ResponseBody
    @CrossOrigin
    public List<ClubDelLibro> visualizzaClubsLettore(
            final @RequestHeader(name = "Authorization") String token
    ) {
        if (!Utils.isUtenteLettore(Utils.getSubjectFromToken(token))) return new ArrayList<>();
        Lettore lettore =  clubService.getLettoreByEmail(Utils.getSubjectFromToken(token));
        return clubService.findClubsLettore(lettore);
    }

    @GetMapping(value = "/visualizza-esperti-biblioteca")
    @ResponseBody
    @CrossOrigin
    public List<Esperto> visualizzaEspertiBiblioteca(
            @RequestParam final String emailBiblioteca
    ) {
        return clubService.getEspertiByBiblioteca(emailBiblioteca);
    }

    @GetMapping(value = "/visualizza-clubs-biblioteca")
    @ResponseBody
    @CrossOrigin
    public List<ClubDelLibro> visualizzaClubBiblioteca(
            @RequestParam final String emailBiblioteca
    ) {
        Set<ClubDelLibro> clubs= new HashSet<>();
        List<Esperto> esperti = clubService.getEspertiByBiblioteca(emailBiblioteca);
        for(Esperto esperto: esperti){
            clubs.addAll(clubService.getClubsByEsperto(esperto));
        }
        return new ArrayList<>(clubs);
    }




    /**
     * Implementa la funzionalità di visualizzazione dei clubs
     * che l'esperto gestisce.
     *
     * @return La view di visualizzazione dei clubs che gestisce
     */
    @GetMapping(value = "area-utente/visualizza-clubs-personali-esperto")
    @ResponseBody
    @CrossOrigin
    public List<ClubDelLibro> visualizzaClubsEsperto(final @RequestHeader(name = "Authorization") String token) {
        //todo da controllare
        if (!Utils.isUtenteEsperto(Utils.getSubjectFromToken(token))) return new ArrayList<>();
        return clubService.findClubsEsperto(clubService.findEspertoByEmail(Utils.getSubjectFromToken(token)));
    }
    /**
     * Implementa la funzionalità che permette
     * di re-indirizzare alla pagina di modifica
     * dei dati di un Club del Libro.
     *
     * @param id    l'ID del Club da modificare
     * @param club  Il club che si vuole creare
     * @return La view che visualizza il form di modifica dati
     */
    @GetMapping(value = "/{id}/modifica")
    @ResponseBody
    @CrossOrigin
    public BiblionetResponse visualizzaModificaDatiClub(final @PathVariable int id,
                                                        final @ModelAttribute ClubDTO club,
                                                        @RequestHeader(name = "Authorization") final String token
    ) {
        Esperto esperto = clubService.findEspertoByEmail(Utils.getSubjectFromToken(token));
        var cdl = this.clubService.getClubByID(id);
        if (cdl == null || esperto == null) {
            return new BiblionetResponse(BiblionetResponse.OGGETTO_NON_TROVATO, false);
        }
        if (!cdl.getEsperto().getEmail().equals(esperto.getEmail())) {
            return new BiblionetResponse(BiblionetResponse.NON_AUTORIZZATO, false);
        }

        club.setNome(cdl.getNome());
        club.setDescrizione(cdl.getDescrizione());
        club.setGeneri(new ArrayList < > (cdl.getGeneri()));
    /*
            model.addAttribute("club", club);
            model.addAttribute("id", id);
            model.addAttribute("generi", this.clubService.getTuttiGeneri());*/
        return new BiblionetResponse("Da regolare col Front-End", false);
    }

    /**
     * Implementa la funzionalità per la modifica dei dati di un Club.
     *
     * @param id       Lo Id del Club
     * @param clubDTO Il form dove inserire i nuovi dati
     * @return La schermata del club
     */
    @PostMapping(value = "/{id}/modifica")
    @ResponseBody
    @CrossOrigin
    public BiblionetResponse modificaDatiClub(final @PathVariable int id, @RequestHeader(name = "Authorization") final String token, final @Valid @ModelAttribute ClubDTO clubDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new BiblionetResponse(BiblionetResponse.FORMATO_NON_VALIDO, false);
        }
        if (!Utils.isUtenteEsperto(token)) {
            return new BiblionetResponse(BiblionetResponse.NON_AUTORIZZATO, false);
        }

        ClubDelLibro clubPers = this.clubService.getClubByID(id);

        String copertina = getBase64Image(clubDTO.getCopertina());
        if (copertina != null) clubPers.setImmagineCopertina(copertina);
        if (clubDTO.getGeneri() != null) {
            clubPers.setGeneri(new HashSet<>(clubDTO.getGeneri()));
        }
        clubPers.setNome(clubDTO.getNome());
        clubPers.setDescrizione(clubDTO.getDescrizione());
        this.clubService.modificaDatiClub(clubPers);
        return new BiblionetResponse("Modifiche apportate", true);
    }

    /**
     * Implementa la funzionalità che permette
     * l'iscrizione di un lettore a un
     * Club del Libro.
     *
     * @param id l'ID del Club a cui iscriversi
     * @return La view che visualizza la lista dei club
     */
    @PostMapping(value = "/{id}/iscrizione")
    public BiblionetResponse partecipaClub(final @PathVariable int id, @RequestHeader(name = "Authorization") final String token) {

        if (!Utils.isUtenteLettore(token)) return new BiblionetResponse("Non sei autorizzato.", false);
        Lettore lettore = clubService.findLettoreByEmail(Utils.getSubjectFromToken(token)); //todo in questi casi andrebbero fatti i check per null
        ClubDelLibro clubDelLibro = this.clubService.getClubByID(id);
        if (clubDelLibro.getLettori().contains(lettore)) {
            return new BiblionetResponse(BiblionetResponse.ISCRIZIONE_FALLITA, false);
        }
        this.clubService.partecipaClub(clubDelLibro, lettore);
        return new BiblionetResponse(BiblionetResponse.ISCRIZIONE_OK, true);
    }

    /**
     * Implementa la funzionalità che permette
     * la visualizzazione della modifica dei dati di
     * un evento di un Club del Libro.
     * @param idClub l'ID del Club
     * @param idEvento l'ID dell'evento
     * @param evento il form dell'evento
     * @return La view che visualizza la lista dei club
     */
    @GetMapping(value = "/{idClub}/eventi/{idEvento}/modifica")
    @CrossOrigin
    @ResponseBody
    public BiblionetResponse visualizzaModificaEvento(final @PathVariable int idClub,
                                                      final @PathVariable int idEvento,
                                                      final @Valid @ModelAttribute EventoDTO evento,
                                                      BindingResult bindingResult,
                                                      @RequestHeader(name = "Authorization") final String token) {

        var eventoBaseOpt = this.eventiService.getEventoById(idEvento);
        Esperto esperto = (Esperto) clubService.findEspertiByNome(Utils.getSubjectFromToken(token));

        if (eventoBaseOpt.isEmpty()) {
            return new BiblionetResponse(BiblionetResponse.OGGETTO_NON_TROVATO, false);
        }

        if (esperto != null && !eventoBaseOpt.get().getClub().getEsperto().getEmail().equals(esperto.getEmail())) {
            return new BiblionetResponse(BiblionetResponse.NON_AUTORIZZATO, false);
        }

        var eventoBase = eventoBaseOpt.get();

        if (eventoBase.getClub().getIdClub() != idClub) {
            return new BiblionetResponse(BiblionetResponse.RICHIESTA_NON_VALIDA, false);
        }

        evento.setNome(eventoBase.getNomeEvento());
        evento.setData(eventoBase.getDataOra().toLocalDate());
        evento.setOra(eventoBase.getDataOra().toLocalTime());
        evento.setDescrizione(eventoBase.getDescrizione());
        if (eventoBase.getLibro() != null) {
            evento.setLibro(eventoBase.getLibro().getIdLibro());
        }
    /*
         model.addAttribute("evento", evento);
         model.addAttribute("club", eventoBase.getClub());
         model.addAttribute("id", eventoBase.getIdEvento());*/

        return new BiblionetResponse(BiblionetResponse.RICHIESTA_NON_VALIDA, false);
    }

    /**
     * Implementa la funzionalità che permette
     * di gestire la chiamata POST
     * per creare un evento un club del libro.
     *
     * @param id         l'id dell'evento
     * @param eventoDTO il form dell'evento
     * @return la view della lista degli eventi
     */
    @PostMapping(value = "/{id}/eventi/crea")
    @CrossOrigin
    @ResponseBody
    public BiblionetResponse creaEvento(final @PathVariable int id,
                                        final @Valid @ModelAttribute EventoDTO eventoDTO,
                                        BindingResult bindingResult) {
        return this.modificaCreaEvento(
                eventoDTO,
                bindingResult,
                id,
                Optional.empty(),
                this.eventiService::creaEvento
        );
    }

    /**
     * Implementa la funzionalità che permette la modifica di un evento.
     * @param idClub l'ID del club
     * @param idEvento l'ID dell'evento
     * @param eventoDTO il form dell'evento
     * @return la view che visualizza la lista degli eventi
     */
    @PostMapping(value = "/{idClub}/eventi/{idEvento}/modifica")
    @CrossOrigin
    @ResponseBody
    public BiblionetResponse modificaEvento(final @PathVariable int idClub,
                                            final @PathVariable int idEvento,
                                            final @Valid @ModelAttribute EventoDTO eventoDTO, BindingResult bindingResult) {
        return this.modificaCreaEvento(
                eventoDTO,
                bindingResult,
                idClub,
                Optional.of(idEvento),
                evento -> {
                    var statusModifica =
                            this.eventiService.modificaEvento(evento);
                    if (statusModifica.isEmpty()) {
                        throw new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                "L'evento con id " + idEvento + "non è associato al club con id " + idClub + "."
                        );
                    }
                }
        );
    }

    /**
     * Implementa la funzionalità che permette
     * la creazione da parte di un Esperto
     * di un Evento.
     * @param id l'ID dell'evento
     * @param evento il form dell'evento
     * @return La view che visualizza il form di creazione Evento
     */
    @GetMapping(value = "/{id}/eventi/crea")
    @CrossOrigin
    @ResponseBody
    public BiblionetResponse visualizzaCreaEvento(final @PathVariable int id,
                                                  final @Valid @ModelAttribute EventoDTO evento, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return new BiblionetResponse(BiblionetResponse.FORMATO_NON_VALIDO, false);
        }
        var club = this.clubService.getClubByID(id);

        if (club == null) {
            return new BiblionetResponse(BiblionetResponse.OGGETTO_NON_TROVATO, false);
        }
        /*
       model.addAttribute("club", club);
       model.addAttribute("evento", evento);*/

        return new BiblionetResponse(BiblionetResponse.FORMATO_NON_VALIDO, false);

    }

    /**
     * Implementa la funzionalità che permette di gestire
     * la visualizzazione dei dati di un Club del Libro.
     * @param id l'ID del Club di cui visualizzare i dati
     * @return La view che visualizza i dati
*/
     @GetMapping(value = "/{id}")
     @CrossOrigin
     @ResponseBody
     public ClubDelLibro visualizzaDatiClub(final @PathVariable int id) {
        return clubService.getClubByID(id);
     }

    /**
     * Implementa la funzionalità che permette di eliminare
     * un evento.
     *
     * @param club L'identificativo del Club dell'evento
     * @param id   L'identificativo dell'evento da eliminare
     * @return La view della lista degli eventi
     */
    @GetMapping(value = "/{club}/eventi/{id}")
    public BiblionetResponse eliminaEvento(final @PathVariable int club, final @PathVariable int id) {
        Optional < Evento > eventoEliminato = this.eventiService.eliminaEvento(id);

        //System.out.println(eventoEliminato);
        if (eventoEliminato.isEmpty()) {
            return new BiblionetResponse("Evento inesistente", false);
        }

        return new BiblionetResponse("Evento eliminato", true);
    }

    /**
     * Implementa la funzionalità che permette di visualizzare
     * la lista degli iscritti a un club.
     *
     * @param id L'identificativo del club
     * @return La view della lista degli iscritti
     */
    @GetMapping(value = "/{id}/iscritti")
    public ClubDelLibro visualizzaIscrittiClub(final @PathVariable int id) {
        return clubService.getClubByID(id);
    }

    /**
     * Implementa la funzionalità che permette di visualizzare
     * la lista degli eventi di un club.
     *
     * @param id l'ID del club
     * @return la view che visualizza gli eventi
     */
    @GetMapping(value = "/{id}/eventi")
    public BiblionetResponse visualizzaListaEventiClub(final @PathVariable int id, @RequestHeader(name = "Authorization") final String token) {
        if (clubService.getClubByID(id) == null) {
            return new BiblionetResponse("Club inesistente", false);
        }
        if (!Utils.isUtenteLettore(token)) return new BiblionetResponse("Non sei autorizzato", false);
        Lettore l = clubService.findLettoreByEmail(Utils.getSubjectFromToken(token));
        if (l == null) return new BiblionetResponse("Non sei autorizzato", false);

        List < Evento > tutti = clubService.getClubByID(id).getEventi();
        List < Evento > mieiEventi = l.getEventi();
        List < Evento > mieiEventiClub = new ArrayList < > ();
        for (Evento e: mieiEventi) {
            if (e.getClub().getIdClub() == id) {
                mieiEventiClub.add(e);
            }
        }
        for (Evento e: mieiEventiClub) {
            if (tutti.contains(e)) {
                tutti.remove(e);
            }
        }
        //todo trovare una soluzione con Giuseppe
    /*
    model.addAttribute("club", clubService.getClubByID(id));
    model.addAttribute("eventi", tutti);
    model.addAttribute("mieiEventi", mieiEventiClub);*/

        return new BiblionetResponse("", true);
    }

    /**
     * Implementa la funzionalità che permette d'iscriversi
     * a uno degli eventi presenti nella lista relativa a
     * un Club del Libro.
     *
     * @param idEvento l'evento a cui partecipare
     * @param idClub   il club dell'evento
     * @return la view che visualizza la lista degli eventi
     */
    @GetMapping(value = "/{idClub}/eventi/{idEvento}/iscrizione")
    public Lettore partecipaEvento(final @PathVariable int idEvento, final @PathVariable int idClub, @RequestHeader(name = "Authorization") final String token) {
        if (!Utils.isUtenteLettore(token)) return null;
        Lettore l = clubService.findLettoreByEmail(Utils.getSubjectFromToken(token));
        if (l == null) return null;
        return eventiService.partecipaEvento(l.getEmail(), idEvento);
    }

    /**
     * Implementa la funzionalità che permette di disiscriversi
     * a uno degli eventi presenti nella lista relativa a
     * un Club del Libro a cui ci si era precedentemente iscritti.
     *
     * @param idEvento l'evento a cui disiscriversi
     * @param idClub   il club dell'evento
     * @return la view che visualizza la lista degli eventi
     */
    @GetMapping(value = "/{idClub}/eventi/{idEvento}/abbandono")
    @CrossOrigin
    @ResponseBody
    public Lettore abbandonaEvento(final @PathVariable int idEvento, final @PathVariable int idClub, @RequestHeader(name = "Authorization") final String token) {
        if (!Utils.isUtenteLettore(token)) return null;
        Lettore l = clubService.findLettoreByEmail(Utils.getSubjectFromToken(token));
        if (l == null) return null;
        return eventiService.abbandonaEvento(l.getEmail(), idEvento);
    }

    private String getBase64Image(MultipartFile copertina) {
        if (copertina != null && !copertina.isEmpty()) {
            try {
                return Base64.getEncoder().encodeToString(copertina.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}