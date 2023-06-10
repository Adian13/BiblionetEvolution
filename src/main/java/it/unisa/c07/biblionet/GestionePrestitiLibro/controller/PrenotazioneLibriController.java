package it.unisa.c07.biblionet.GestionePrestitiLibro.controller;

import it.unisa.c07.biblionet.GestionePrestitiLibro.service.PrenotazioneLibriService;
import it.unisa.c07.biblionet.model.dao.customQueriesResults.ILibroIdAndName;
import it.unisa.c07.biblionet.GestioneUtenti.repository.BibliotecaDAO;
import it.unisa.c07.biblionet.GestioneUtenti.repository.LettoreDAO;
import it.unisa.c07.biblionet.entity.Libro;
import it.unisa.c07.biblionet.entity.TicketPrestito;
import it.unisa.c07.biblionet.entity.Biblioteca;
import it.unisa.c07.biblionet.entity.Lettore;
import it.unisa.c07.biblionet.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Implementa il controller per il sottosistema
 * PrenotazioneLibri.
 *
 * @author Viviana Pentangelo, Gianmario Voria
 */
@SessionAttributes("loggedUser")
@Controller
@RequiredArgsConstructor
@RequestMapping("/prenotazione-libri")
public class PrenotazioneLibriController {

    /**
     * Il service per effettuare le operazioni di
     * persistenza.
     */
    private final PrenotazioneLibriService prenotazioneService;
    private final LettoreDAO lettoreDAO;
    private final BibliotecaDAO bibliotecaDAO;

    /**
     * Implementa la funzionalità che permette di
     * visualizzare tutti i libri prenotabili.
     *
     * @return La view per visualizzare i libri
     */
    @GetMapping(value = "")
    @ResponseBody
    @CrossOrigin
    public List<Libro> visualizzaListaLibri() {
        return prenotazioneService.visualizzaListaLibriCompleta();
    }

    /**
     * Implementa la funzionalità che permette di
     * visualizzare tutti i libri filtrati.
     *
     * @param stringa La stringa di ricerca
     * @param filtro  L'informazione su cui filtrare
     * @return La view che visualizza la lista
     */
    @GetMapping(value = "/ricerca")
    @ResponseBody
    @CrossOrigin
    public List<Libro> visualizzaListaFiltrata(
            @RequestParam("stringa") final String stringa,
            @RequestParam("filtro") final String filtro) {

        switch (filtro) {
            case "titolo":
                return prenotazioneService.visualizzaListaLibriPerTitolo(stringa);
            case "genere":
                return prenotazioneService.visualizzaListaLibriPerGenere(stringa);
            case "biblioteca":
                return prenotazioneService.visualizzaListaLibriPerBiblioteca(stringa);
            default:
                return prenotazioneService.visualizzaListaLibriCompleta();
        }
    }

    /**
     * Implementa la funzionalità che permette di
     * visualizzare le biblioteche presso cui è
     * possibile prenotare il libro.
     *
     * @param id    L'ID del libro di cui effettuare la prenotazione
     * @return La view che visualizza la lista delle biblioteche
     */
    @GetMapping(value = "/{id}/visualizza-libro")
    @ResponseBody
    @CrossOrigin
    public List<Biblioteca> prenotaLibro(@PathVariable final int id) {
        return prenotazioneService.getBibliotecheLibro(prenotazioneService.getLibroByID(id));
    }

    @GetMapping(value = "/{id}/ottieni-libro")
    @ResponseBody
    @CrossOrigin
    public Libro ottieniLibro(@PathVariable final int id) {
        return prenotazioneService.getLibroByID(id);
    }


    /**
     * Implementa la funzionalità che permette di
     * richiedere il prestito di un libro.
     *
     * @param idBiblioteca L'ID della biblioteca che possiede il libro
     * @param idLibro      L'ID del libro di cui effettuare la prenotazione
     * @return La view che visualizza la lista dei libri prenotabili
     */
    @PostMapping(value = "/conferma-prenotazione")
    @ResponseBody
    @CrossOrigin
    public ResponseEntity<String> confermaPrenotazione(@RequestParam final String idBiblioteca,
                                       @RequestParam final String idLibro,
                                       @RequestHeader (name="Authorization") final String token) {

        if (!Utils.isUtenteLettore(token)) {
            return new ResponseEntity<>("Non sei autorizzato", HttpStatus.FORBIDDEN);
        }
        Lettore l = (Lettore) lettoreDAO.getOne(Utils.getSubjectFromToken(token));
        TicketPrestito ticketPrestito = prenotazioneService.richiediPrestito(l,idBiblioteca,Integer.parseInt(idLibro));
        if(ticketPrestito != null) return new ResponseEntity<>("OK", HttpStatus.OK);
        return new ResponseEntity<>("Errore", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Implementa la funzionalità che permette di
     * a una biblioteca di visualizzare le richieste di
     * prenotazione ricevute.
     *
     * @return La view che visualizza la lista delle richieste
     */
    @GetMapping(value = "/visualizza-richieste")
    @ResponseBody
    @CrossOrigin
    public List<TicketPrestito> visualizzaRichieste(
            @RequestHeader (name="Authorization") final String token
    ) {
        if (!Utils.isUtenteBiblioteca(token)) {
            return null;
        }
        Biblioteca biblioteca = (Biblioteca) bibliotecaDAO.getOne(Utils.getSubjectFromToken(token));

        List<TicketPrestito> lista = prenotazioneService.getTicketsByBiblioteca(biblioteca);
            /*
            List<TicketPrestito> list1 = new ArrayList<>();
            List<TicketPrestito> list2 = new ArrayList<>();
            List<TicketPrestito> list3 = new ArrayList<>();
            for (TicketPrestito t : lista) {
                if (t.getStato().equals(
                        TicketPrestito.Stati.IN_ATTESA_DI_CONFERMA)) {
                    list1.add(t);
                } else if (t.getStato().equals(
                        TicketPrestito.Stati.IN_ATTESA_DI_RESTITUZIONE)) {
                    list2.add(t);
                } else if (t.getStato().equals(
                        TicketPrestito.Stati.CHIUSO)) {
                    list3.add(t);
                }
            }*/
            //todo trovare una soluzione con Giuseppe
        return lista;

    }

    /**
     * Implementa la funzionalità che permette di
     * richiedere il prestito di un libro.
     *
     * @param id     l'ID del ticket da accettare
     * @param giorni il tempo di concessione del prestito
     * @return La view che visualizza la lista delle prenotazioni
     */
    @PostMapping(value = "/ticket/{id}/accetta")
    @ResponseBody
    @CrossOrigin
    public ResponseEntity<String> accettaPrenotazione(final @PathVariable int id,
                        final @RequestParam(value = "giorni") int giorni) {
        TicketPrestito ticket = prenotazioneService.getTicketByID(id);
        ticket = prenotazioneService.accettaRichiesta(ticket, giorni);
        if(ticket!=null) return new ResponseEntity<>("Richiesta accettata", HttpStatus.OK);
        return new ResponseEntity<>("Errore", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Implementa la funzionalità che permette di
     * richiedere il prestito di un libro.
     *
     * @param id l'ID del ticket da rifiutare
     * @return La view che visualizza la lista delle prenotazioni
     */
    @PostMapping(value = "/ticket/{id}/rifiuta")
    @ResponseBody
    @CrossOrigin
    public ResponseEntity<String> rifiutaPrenotazione(final @PathVariable int id) {
        TicketPrestito ticket = prenotazioneService.getTicketByID(id);
        ticket = prenotazioneService.rifiutaRichiesta(ticket);
        if(ticket!=null) return new ResponseEntity<>("Richiesta rifiutata", HttpStatus.OK);
        return new ResponseEntity<>("Errore", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Implementa la funzionalità che permette di
     * chiudere una prenotazione di un libro quando
     * questo viene riconsegnato.
     *
     * @param id l'ID del ticket da chiudere
     * @return La view che visualizza la lista delle prenotazioni
     */
    @PostMapping(value = "/ticket/{id}/chiudi")
    @ResponseBody
    @CrossOrigin
    public ResponseEntity<String> chiudiPrenotazione(final @PathVariable int id) {
        TicketPrestito ticket = prenotazioneService.getTicketByID(id);
        ticket = prenotazioneService.chiudiTicket(ticket);
        if(ticket!=null) return new ResponseEntity<>("Prenotazione chiusa", HttpStatus.OK);
        return new ResponseEntity<>("Errore", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Implementa la funzionalità che permette di
     * ottenere la lista di ticket di un lettore.
     *
     * @return La view che visualizza la lista delle prenotazioni del lettore
     */
    @GetMapping(value = "/visualizza-prenotazioni")
    @ResponseBody
    @CrossOrigin
    public List<TicketPrestito> visualizzaPrenotazioniLettore(@RequestHeader (name="Authorization") final String token) {

        if (!Utils.isUtenteLettore(token)) {
            return null;
        }
        Lettore lettore = (Lettore) lettoreDAO.getOne(Utils.getSubjectFromToken(token));

        List<TicketPrestito> listaTicket =
                    prenotazioneService.getTicketsLettore(lettore);
        /*
            List<TicketPrestito> list1 = new ArrayList<>();
            List<TicketPrestito> list2 = new ArrayList<>();
            List<TicketPrestito> list3 = new ArrayList<>();
            List<TicketPrestito> list4 = new ArrayList<>();
            for (TicketPrestito t : listaTicket) {
                if (t.getStato().equals(
                        TicketPrestito.Stati.IN_ATTESA_DI_CONFERMA)) {
                    list1.add(t);
                } else if (t.getStato().equals(
                        TicketPrestito.Stati.IN_ATTESA_DI_RESTITUZIONE)) {
                    list2.add(t);
                } else if (t.getStato().equals(
                        TicketPrestito.Stati.CHIUSO)) {
                    list3.add(t);
                } else if (t.getStato().equals(
                        TicketPrestito.Stati.RIFIUTATO)) {
                    list4.add(t);
                }
            }*/ //todo parlarne con Giuseppe
            return listaTicket;
        }



    /**
     * Implementa la funzionalità che permette di
     * ottenere una lista di id e titoli di libri
     * sulla base di un titolo dato
     * ! Controllare prima di consegnare
     *
     * @param titolo il titolo che deve fare match
     * @return la lista di informazioni
     */
    @RequestMapping(value = "/find-libri-by-titolo-contains")
    @CrossOrigin
    public @ResponseBody List<ILibroIdAndName> findLibriByTitoloContains(
            @RequestParam("q") final String titolo
    ) {
        return prenotazioneService.findByTitoloContains(titolo);
    }

}
