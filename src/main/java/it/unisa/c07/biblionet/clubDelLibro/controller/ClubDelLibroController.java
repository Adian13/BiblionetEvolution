package it.unisa.c07.biblionet.clubDelLibro.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import it.unisa.c07.biblionet.model.dao.utente.EspertoDAO;
import it.unisa.c07.biblionet.utils.BiblionetResponse;
import it.unisa.c07.biblionet.utils.Utils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import it.unisa.c07.biblionet.clubDelLibro.service.ClubDelLibroService;
import it.unisa.c07.biblionet.gestioneEventi.service.GestioneEventiService;
import it.unisa.c07.biblionet.model.entity.ClubDelLibro;
import it.unisa.c07.biblionet.model.entity.Evento;
import it.unisa.c07.biblionet.model.entity.Genere;
import it.unisa.c07.biblionet.model.entity.utente.Esperto;
import it.unisa.c07.biblionet.model.entity.utente.Lettore;
import it.unisa.c07.biblionet.model.entity.utente.UtenteRegistrato;
import it.unisa.c07.biblionet.model.form.ClubForm;
import it.unisa.c07.biblionet.model.form.EventoForm;
import lombok.RequiredArgsConstructor;

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
@SessionAttributes("loggedUser")
public class ClubDelLibroController {

    /**
     * Il service per effettuare le operazioni di persistenza.
     */
    private final ClubDelLibroService clubService;

    /**
     * Il service per effettuare le operazioni di persistenza
     * degli eventi.
     */
    private final GestioneEventiService eventiService;
    private final EspertoDAO espertoDAO;


    /**
     * Metodo di utilità che modifica o crea un evento, validando
     * i dati.
     *
     * @param eventoForm Il form con i dati da modificare
     * @param idClub     L'id del club del libro in cui inserire l'evento.
     * @param idEvento   L'id dell'evento, che può essere vuoto per ottenere
     *                   l'autoassegnazione.
     * @param operazione L'operazione, tra creazione e modifica, che si vuole
     *                   effettuare.
     * @return La view inserita.
     */
    private BiblionetResponse modificaCreaEvento(final @Valid @ModelAttribute EventoForm eventoForm, BindingResult bindingResult,
                                                 //@RequestParam final String view,
                                                 @RequestParam final int idClub, @RequestParam final Optional<Integer> idEvento, @RequestParam final Consumer<Evento> operazione) {

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
        evento.setNomeEvento(eventoForm.getNome());
        evento.setDescrizione(eventoForm.getDescrizione());

        var dataOra = LocalDateTime.of(eventoForm.getData(), eventoForm.getOra());
        if (dataOra.isBefore(LocalDateTime.now())) {
            return new BiblionetResponse("Data non valida", false);
        }

        evento.setDataOra(dataOra);

        if (eventoForm.getLibro() != null) {
            var libro = this.eventiService.getLibroById(eventoForm.getLibro());
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
    public List<Object> visualizzaListaClubs(@RequestParam(value = "generi") final Optional<List<String>> generi, @RequestParam(value = "citta") final Optional<List<String>> citta) {

        // Molto più pulito della concatenazione con gli stream
        Predicate<ClubDelLibro> filtroGenere = x -> true;

        if (generi.isPresent()) {
            filtroGenere = x -> false;

            var generiDaDB = clubService.getGeneri(generi.get());

            for (Genere genere : generiDaDB) {
                filtroGenere = filtroGenere.or(c -> c.getGeneri().contains(genere));
            }
        }

        Predicate<ClubDelLibro> filtroCitta = x -> true;

        if (citta.isPresent()) {
            filtroCitta = x -> false;
            for (String cittaSingola : citta.get()) {
                filtroCitta = filtroCitta.or(c -> clubService.getCittaFromClubDelLibro(c).equals(cittaSingola));
            }
        }

        List<ClubDelLibro> listaClubs = clubService.visualizzaClubsDelLibro(filtroCitta.and(filtroGenere));


        // Necessito di un oggetto anonimo per evitare problemi con JS
        return listaClubs.stream().map(club -> new Object() {
            public final String nome = club.getNome();
            public final String descrizione = club.getDescrizione();
            public final String nomeEsperto = club.getEsperto().getNome() + " " + club.getEsperto().getCognome();
            public final String immagineCopertina = club.getImmagineCopertina();
            public final Set<String> generi = club.getGeneri().stream().map(Genere::getNome).collect(Collectors.toSet());
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
     * @param clubForm Il club che si vuole creare
     * @return la pagina del Club
     */
    @RequestMapping(value = "/crea", method = RequestMethod.POST)
    public ResponseEntity<String> creaClubDelLibro(final @Valid @ModelAttribute ClubForm clubForm, @RequestHeader(name = "Authorization") final String token, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>("Formato dati non valido", HttpStatus.BAD_REQUEST);
        }
        if (!Utils.isUtenteEsperto(token)) {
            return new ResponseEntity<>("Non sei autorizzato", HttpStatus.UNAUTHORIZED);
        }
        Esperto esperto = (Esperto) espertoDAO.getOne(Utils.getSubjectFromToken(token));

        ClubDelLibro cdl = new ClubDelLibro();
        cdl.setNome(clubForm.getNome());
        cdl.setDescrizione(clubForm.getDescrizione());
        cdl.setEsperto(esperto);
        String copertina = getBase64Image(clubForm.getCopertina());
        if (copertina != null) cdl.setImmagineCopertina(copertina);


        cdl.setGeneri(Arrays.asList(new Genere[]{}));
        cdl.setGeneri(this.clubService.getGeneri(clubForm.getGeneri()));

        this.clubService.creaClubDelLibro(cdl);
        return new ResponseEntity<>("Club del Libro creato", HttpStatus.UNAUTHORIZED);

    }

    /**
     * Implementa la funzionalità che permette
     * di re-indirizzare alla pagina di modifica
     * dei dati di un Club del Libro.
     * @param id l'ID del Club da modificare
     * @param club Il club che si vuole creare
     * @param model l'oggetto model usato per inserire gli attributi
     * @return La view che visualizza il form di modifica dati

     @RequestMapping(value = "/{id}/modifica", method = RequestMethod.GET)
     public String visualizzaModificaDatiClub(final @PathVariable int id,
     final @ModelAttribute
     ClubForm club,
     final Model model) {
     var esperto = (UtenteRegistrato) model.getAttribute("loggedUser");
     var cdl = this.clubService.getClubByID(id);
     if (cdl == null) {
     throw new ResponseStatusException(HttpStatus.NOT_FOUND);
     }
     if (esperto == null
     || !cdl.getEsperto().getEmail().equals(esperto.getEmail())) {
     throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
     }

     club.setNome(cdl.getNome());
     club.setDescrizione(cdl.getDescrizione());
     club.setGeneri(cdl.getGeneri().stream().map(Genere::getNome).
     collect(Collectors.toList()));

     model.addAttribute("club", club);
     model.addAttribute("id", id);
     model.addAttribute("generi", this.clubService.getTuttiGeneri());
     return "club-del-libro/modifica-club";
     }
     todo non so se serve
     */

    /**
     * Implementa la funzionalità per la modifica dei dati di un Club.
     *
     * @param id       Lo Id del Club
     * @param clubForm Il form dove inserire i nuovi dati
     * @return La schermata del club
     */
    @RequestMapping(value = "/{id}/modifica", method = RequestMethod.POST)
    public ResponseEntity<String> modificaDatiClub(final @PathVariable int id, @RequestHeader(name = "Authorization") final String token, final @Valid @ModelAttribute ClubForm clubForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>("Formato dati non valido", HttpStatus.BAD_REQUEST);
        }
        if (!Utils.isUtenteEsperto(token)) {
            return new ResponseEntity<>("Non sei autorizzato", HttpStatus.UNAUTHORIZED);
        }

        ClubDelLibro clubPers = this.clubService.getClubByID(id);

        String copertina = getBase64Image(clubForm.getCopertina());
        if (copertina != null) clubPers.setImmagineCopertina(copertina);
        if (clubForm.getGeneri() != null) {
            List<Genere> gList = clubService.getGeneri(clubForm.getGeneri());
            clubPers.setGeneri(gList);
        }
        clubPers.setNome(clubForm.getNome());
        clubPers.setDescrizione(clubForm.getDescrizione());
        this.clubService.modificaDatiClub(clubPers);
        return new ResponseEntity<>("Modifiche apportate", HttpStatus.OK);
    }

    /**
     * Implementa la funzionalità che permette
     * l'iscrizione di un lettore a un
     * Club del Libro.
     *
     * @param id    l'ID del Club a cui iscriversi
     * @param model Il model da passare alla view
     * @return La view che visualizza la lista dei club
     */
    @RequestMapping(value = "/{id}/iscrizione", method = RequestMethod.POST)
    public String partecipaClub(final @PathVariable int id, final Model model) {

        UtenteRegistrato lettore = (UtenteRegistrato) model.getAttribute("loggedUser");
        if (lettore == null || !lettore.getTipo().equals("Lettore")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        ClubDelLibro clubDelLibro = this.clubService.getClubByID(id);
        if (clubDelLibro.getLettori().contains(lettore)) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE);
        }
        this.clubService.partecipaClub(clubDelLibro, (Lettore) lettore);
        return "redirect:/club-del-libro/";
    }

    /**
     * Implementa la funzionalità che permette
     * la visualizzazione della modifica dei dati di
     * un evento di un Club del Libro.
     * @param idClub l'ID del Club
     * @param idEvento l'ID dell'evento
     * @param evento il form dell'evento
     * @return La view che visualizza la lista dei club

     @RequestMapping( value = "/{idClub}/eventi/{idEvento}/modifica",
     method = RequestMethod.GET
     )
     public String visualizzaModificaEvento(final @PathVariable int idClub,
     final @PathVariable int idEvento,
     final @Valid @ModelAttribute
     EventoForm evento,
     BindingResult bindingResult) {

     var eventoBaseOpt =
     this.eventiService.getEventoById(idEvento);
     var esperto = (UtenteRegistrato) model.getAttribute("loggedUser");

     if (eventoBaseOpt.isEmpty()) {
     throw new ResponseStatusException(
     HttpStatus.NOT_FOUND,
     "Evento Inesistente"
     );
     }

     if (esperto != null && !eventoBaseOpt.get().getClub().getEsperto()
     .getEmail().equals(esperto.getEmail())) {
     throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
     }

     var eventoBase = eventoBaseOpt.get();

     if (eventoBase.getClub().getIdClub() != idClub) {
     throw new ResponseStatusException(
     HttpStatus.BAD_REQUEST,
     "L'evento con id " + idEvento
     + "non è associato al club con id "
     + idClub + "."
     );
     }

     evento.setNome(eventoBase.getNomeEvento());
     evento.setData(eventoBase.getDataOra().toLocalDate());
     evento.setOra(eventoBase.getDataOra().toLocalTime());
     evento.setDescrizione(eventoBase.getDescrizione());
     if (eventoBase.getLibro() != null) {
     evento.setLibro(eventoBase.getLibro().getIdLibro());
     }

     model.addAttribute("evento", evento);
     model.addAttribute("club", eventoBase.getClub());
     model.addAttribute("id", eventoBase.getIdEvento());

     return "club-del-libro/modifica-evento";
     }
     todo
     */

    /**
     * Implementa la funzionalità che permette
     * di gestire la chiamata POST
     * per creare un evento un club del libro.
     * @param id l'id dell'evento
     * @param eventoForm il form dell'evento
     * @return la view della lista degli eventi

     @RequestMapping(value = "/{id}/eventi/crea", method = RequestMethod.POST)
     public String creaEvento(final @PathVariable int id,
     final @Valid @ModelAttribute EventoForm eventoForm,
     BindingResult bindingResult) {
     return this.modificaCreaEvento(
     eventoForm,
     "redirect:/club-del-libro/" + id,
     id,
     Optional.empty(),
     this.eventiService::creaEvento
     );
     }
     todo ?
     */
    /**
     * Implementa la funzionalità che permette la modifica di un evento.
     * @param idClub l'ID del club
     * @param idEvento l'ID dell'evento
     * @param eventoForm il form dell'evento
     * @return la view che visualizza la lista degli eventi

     @RequestMapping(value = "/{idClub}/eventi/{idEvento}/modifica",
     method = RequestMethod.POST)
     public String modificaEvento(final @PathVariable int idClub,
     final @PathVariable int idEvento,
     final @ModelAttribute EventoForm eventoForm) {
     return this.modificaCreaEvento(
     eventoForm,
     "redirect:/club-del-libro/" + idClub,
     idClub,
     Optional.of(idEvento),
     evento -> {
     var statusModifica =
     this.eventiService.modificaEvento(evento);
     if (statusModifica.isEmpty()) {
     throw new ResponseStatusException(
     HttpStatus.BAD_REQUEST,
     "L'evento con id " + idEvento
     + "non è associato al club con id "
     + idClub + "."
     );
     }
     }
     );
     }
     todo ?
     */

    /**
     * Implementa la funzionalità che permette
     * la creazione da parte di un Esperto
     * di un Evento.
     * @param id l'ID dell'evento
     * @param evento il form dell'evento
     * @param model il model da passare alla view
     * @return La view che visualizza il form di creazione Evento

     @RequestMapping(value = "/{id}/eventi/crea", method = RequestMethod.GET)
     public String visualizzaCreaEvento(final @PathVariable int id,
     final @ModelAttribute EventoForm evento,
     final Model model) {
     var club = this.clubService.getClubByID(id);

     if (club == null) {
     throw new ResponseStatusException(
     HttpStatus.NOT_FOUND,
     "Club del Libro Inesistente"
     );
     }

     model.addAttribute("club", club);
     model.addAttribute("evento", evento);

     return "club-del-libro/aggiungi-evento";
     }
     todo ?
     */
    /**
     * Implementa la funzionalità che permette di gestire
     * la visualizzazione dei dati di un Club del Libro.
     * @param id l'ID del Club di cui visualizzare i dati
     * @param model il model per il passaggio dei dati
     * @return La view che visualizza i dati

     @RequestMapping(value = "/{id}",
     method = RequestMethod.GET)
     public String visualizzaDatiClub(final @PathVariable int id,
     final Model model) {
     model.addAttribute("club", clubService.getClubByID(id));
     return "club-del-libro/visualizza-singolo-club";
     }
     todo?
     */
    /**
     * Implementa la funzionalità che permette di eliminare
     * un evento.
     *
     * @param club L'identificativo del Club dell'evento
     * @param id   L'identificativo dell'evento da eliminare
     * @return La view della lista degli eventi
     */
    @RequestMapping(value = "/{club}/eventi/{id}", method = RequestMethod.GET)
    public String eliminaEvento(final @PathVariable int club, final @PathVariable int id) {
        Optional<Evento> eventoEliminato = this.eventiService.eliminaEvento(id);

        System.out.println(eventoEliminato);


        if (eventoEliminato.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento Inesistente");
        }

        return "redirect:/club-del-libro/" + club;
    }

    /**
     * Implementa la funzionalità che permette di visualizzare
     * la lista degli iscritti a un club.
     *
     * @param id    L'identificativo del club
     * @param model il model la passare alla view
     * @return La view della lista degli iscritti
     */
    @RequestMapping(value = "/{id}/iscritti", method = RequestMethod.GET)
    public String visualizzaIscrittiClub(final @PathVariable int id, final Model model) {
        model.addAttribute("club", clubService.getClubByID(id));
        return "club-del-libro/visualizza-iscritti";
    }

    /**
     * Implementa la funzionalità che permette di visualizzare
     * la lista degli eventi di un club.
     *
     * @param id    l'ID del club
     * @param model il model da passare alla view
     * @return la view che visualizza gli eventi
     */
    @RequestMapping(value = "/{id}/eventi", method = RequestMethod.GET)
    public String visualizzaListaEventiClub(final @PathVariable int id, final Model model) {
        if (clubService.getClubByID(id) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        UtenteRegistrato utente = (UtenteRegistrato) model.getAttribute("loggedUser");
        if (utente == null || !utente.getTipo().equals("Lettore")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        Lettore l = (Lettore) utente;
        List<Evento> tutti = clubService.getClubByID(id).getEventi();
        List<Evento> mieiEventi = l.getEventi();
        List<Evento> mieiEventiClub = new ArrayList<>();
        for (Evento e : mieiEventi) {
            if (e.getClub().getIdClub() == id) {
                mieiEventiClub.add(e);
            }
        }
        for (Evento e : mieiEventiClub) {
            if (tutti.contains(e)) {
                tutti.remove(e);
            }
        }
        model.addAttribute("club", clubService.getClubByID(id));
        model.addAttribute("eventi", tutti);
        model.addAttribute("mieiEventi", mieiEventiClub);

        return "club-del-libro/visualizza-eventi";
    }

    /**
     * Implementa la funzionalità che permette d'iscriversi
     * a uno degli eventi presenti nella lista relativa a
     * un Club del Libro.
     *
     * @param idEvento l'evento a cui partecipare
     * @param idClub   il club dell'evento
     * @param model    l'oggetto Model da cui ottenere il lettore autenticato
     * @return la view che visualizza la lista degli eventi
     */
    @RequestMapping(value = "/{idClub}/eventi/{idEvento}/iscrizione", method = RequestMethod.GET)
    public String partecipaEvento(final @PathVariable int idEvento, final @PathVariable int idClub, final Model model) {
        UtenteRegistrato utente = (UtenteRegistrato) model.getAttribute("loggedUser");
        if (utente == null || !utente.getTipo().equals("Lettore")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        model.addAttribute("loggedUser", eventiService.partecipaEvento(utente.getEmail(), idEvento));
        return "redirect:/club-del-libro/" + idClub + "/eventi";
    }

    /**
     * Implementa la funzionalità che permette di disiscriversi
     * a uno degli eventi presenti nella lista relativa a
     * un Club del Libro a cui ci si era precedentemente iscritti.
     *
     * @param idEvento l'evento a cui disiscriversi
     * @param idClub   il club dell'evento
     * @param model    l'oggetto Model da cui ottenere il lettore autenticato
     * @return la view che visualizza la lista degli eventi
     */
    @RequestMapping(value = "/{idClub}/eventi/{idEvento}/abbandono", method = RequestMethod.GET)
    public String abbandonaEvento(final @PathVariable int idEvento, final @PathVariable int idClub, final Model model) {
        UtenteRegistrato utente = (UtenteRegistrato) model.getAttribute("loggedUser");
        if (utente == null || !utente.getTipo().equals("Lettore")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        model.addAttribute("loggedUser", eventiService.abbandonaEvento(utente.getEmail(), idEvento));
        return "redirect:/club-del-libro/" + idClub + "/eventi";
    }

    private String getBase64Image(MultipartFile copertina) {
        if (copertina != null && !copertina.isEmpty()) {
            try {
                byte[] imageBytes = copertina.getBytes();
                String base64Image = Base64.getEncoder().encodeToString(imageBytes);
                return base64Image;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}