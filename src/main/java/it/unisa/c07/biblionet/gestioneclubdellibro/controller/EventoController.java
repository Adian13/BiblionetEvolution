package it.unisa.c07.biblionet.gestioneclubdellibro.controller;

import it.unisa.c07.biblionet.gestioneclubdellibro.*;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Esperto;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Evento;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Lettore;
import it.unisa.c07.biblionet.utils.BiblionetResponse;
import it.unisa.c07.biblionet.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.function.Consumer;





@Controller
@RequiredArgsConstructor
@RequestMapping("/club-del-libro")
public class EventoController {

    private final GestioneEventiService eventiService;
    private final ClubDelLibroService clubService;
    private final LettoreService lettoreService;
    private final EspertoService espertoService;

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
        Esperto esperto = (Esperto) espertoService.findEspertiByNome(Utils.getSubjectFromToken(token));

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
        Lettore l = lettoreService.findLettoreByEmail(Utils.getSubjectFromToken(token));
        if (l == null) return null;
        return eventiService.abbandonaEvento(l.getEmail(), idEvento);
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
        Lettore l = lettoreService.findLettoreByEmail(Utils.getSubjectFromToken(token));
        if (l == null) return null;
        return eventiService.partecipaEvento(l.getEmail(), idEvento);
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
     * di gestire la chiamata POST
     * per creare un evento un club del libro.
     *
     * @param id         l'id dell'evento
     * @param eventoDTO il form dell'evento
     * @return la view della lista degli eventi
     */
    @PostMapping(value = "/eventi/crea")
    @CrossOrigin
    @ResponseBody
    public BiblionetResponse creaEvento(final @RequestParam int id,
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
                                                 final int idClub, final Optional<Integer> idEvento, final Consumer<Evento> operazione) {

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


}
