package it.unisa.c07.biblionet.prenotazioneLibri.controller;

import it.unisa.c07.biblionet.model.dao.utente.BibliotecaDAO;
import it.unisa.c07.biblionet.model.entity.Libro;
import it.unisa.c07.biblionet.model.entity.utente.Biblioteca;
import it.unisa.c07.biblionet.model.form.LibroForm;
import it.unisa.c07.biblionet.prenotazioneLibri.service.PrenotazioneLibriService;
import it.unisa.c07.biblionet.utils.BiblionetResponse;
import it.unisa.c07.biblionet.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

/**
 * Implementa il controller per il sottosistema
 * PrenotazioneLibri, in particolare la gestione
 * delle Biblioteche.
 *
 * @author Viviana Pentangelo, Gianmario Voria
 */
@SessionAttributes("loggedUser")
@Controller
@RequiredArgsConstructor
@RequestMapping("/biblioteca")
public class BibliotecaController {

    /**
     * Il service per effettuare le operazioni di
     * persistenza.
     */
    private final PrenotazioneLibriService prenotazioneService;
    private final BibliotecaDAO bibliotecaDAO;

    /**
     * Implementa la funzionalità che permette di
     * visualizzare tutte le biblioteche iscritte.
     * @return La view per visualizzare le biblioteche
     */
    @RequestMapping(value = "/visualizza-biblioteche",
            method = RequestMethod.GET)
    @ResponseBody
    @CrossOrigin
    public List<Biblioteca> visualizzaListaBiblioteche() {
        return prenotazioneService.getAllBiblioteche();
    }

    /**
     * Implementa la funzionalità che permette inserire
     * un libro tramite l'isbn e una Api di Google.
     * @param isbn l'isbn del libro
     * @param generi la lista dei generi del libro
     * @param numCopie il numero di copie possedute
     * @return La view per visualizzare il libro
     */
    @PostMapping(value = "/inserimento-isbn")
    @ResponseBody
    @CrossOrigin
    public BiblionetResponse inserisciPerIsbn(@RequestParam final String isbn,
                                              @RequestHeader (name="Authorization") final String token,
                                              @RequestParam final String[] generi,
                                              @RequestParam final int numCopie) {

        if (!Utils.isUtenteBiblioteca(token)) {
            return new BiblionetResponse("Non sei autorizzato", false);
        }
        //todo controllare scadenza token

        if (isbn == null) {
            return new BiblionetResponse("ISBN inesistente", false);
        }
        Biblioteca b = (Biblioteca) bibliotecaDAO.getOne(Utils.getSubjectFromToken(token));

        List<String> glist = Arrays.asList(generi.clone());
        Libro l = prenotazioneService.inserimentoPerIsbn(
                isbn, b.getEmail(), numCopie, glist);
        if (l == null) {
            return new BiblionetResponse("Libro non crato", false);
        }
        return new BiblionetResponse("Libro creato con successo", true);

    }

    /**
     * Implementa la funzionalità che permette inserire
     * un libro alla lista dei possessi preso
     * dal db.
     * @param idLibro l'ID del libro
     * @param numCopie il numero di copie possedute
     * @return La view per visualizzare il libro
     */
    @PostMapping(value = "/inserimento-archivio")
    @ResponseBody
    @CrossOrigin
    public BiblionetResponse inserisciDaDatabase(@RequestParam final int idLibro,
                                      @RequestHeader (name="Authorization") final String token,
                                   @RequestParam final int numCopie) {

        if (!Utils.isUtenteBiblioteca(token)) {
            return new BiblionetResponse("Non sei autorizzato", false);
        }
        Biblioteca b = (Biblioteca) bibliotecaDAO.getOne(Utils.getSubjectFromToken(token));
        Libro l = prenotazioneService.inserimentoDalDatabase(
                idLibro, b.getEmail(), numCopie);
        return new BiblionetResponse("Libro inserito con successo", true);

    }

    /**
     * Implementa la funzionalità che permette inserire
     * un libro manualmente tramite form.
     * @param libro Il libro da salvare
     * @param numCopie il numero di copie possedute
     * @param annoPubblicazione l'anno di pubblicazione
     * @return La view per visualizzare il libro
     */
    @PostMapping(value = "/inserimento-manuale")
    @ResponseBody
    @CrossOrigin
    public BiblionetResponse inserisciManualmente(
            @RequestHeader (name="Authorization") final String token,
            @RequestParam final LibroForm libro,
            @RequestParam final int numCopie,
            @RequestParam final String annoPubblicazione) {

        if (!Utils.isUtenteBiblioteca(token)) {
            return new BiblionetResponse("Non sei autorizzato", false);
        }
        Biblioteca b = (Biblioteca) bibliotecaDAO.getOne(Utils.getSubjectFromToken(token));
        Libro l = new Libro();
        l.setTitolo(libro.getTitolo());
        if (libro.getIsbn() != null) {
            l.setIsbn(libro.getIsbn());
        }
        if (libro.getDescrizione() != null) {
            l.setDescrizione(libro.getDescrizione());
        }
        l.setCasaEditrice(libro.getCasaEditrice());
        l.setAutore(libro.getAutore());
        if (libro.getImmagineLibro() != null) {
            try {
                byte[] imageBytes = libro.getImmagineLibro().getBytes();
                String base64Image =
                        Base64.getEncoder().encodeToString(imageBytes);
                l.setImmagineLibro(base64Image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        LocalDateTime anno = LocalDateTime.of(
                Integer.parseInt(annoPubblicazione), 1, 1, 1, 1);
        l.setAnnoDiPubblicazione(anno);
        Libro newLibro = prenotazioneService.inserimentoManuale(
                l, b.getEmail(), numCopie, libro.getGeneri());
        return new BiblionetResponse("Libro inserito con successo", true);

    }

    /**
     * Implementa la funzionalità che permette di
     * visualizzare le biblioteche filtrate.
     *
     * @param stringa La stringa di ricerca
     * @param filtro  L'informazione su cui filtrare
     * @return La view che visualizza la lista
     */
    @GetMapping(value = "/ricerca")
    @ResponseBody
    @CrossOrigin
    public List<Biblioteca> visualizzaListaFiltrata(
            @RequestParam("stringa") final String stringa,
            @RequestParam("filtro") final String filtro) {

        switch (filtro) {
            case "nome":
                return prenotazioneService.getBibliotecheByNome(stringa);
            case "citta":
                return prenotazioneService.getBibliotecheByCitta(stringa);
            default:
                return prenotazioneService.getAllBiblioteche();
        }
    }

    /**
     * Implementa la funzionalità di visualizzazione
     * del profilo di una singola biblioteca.
     * @param email della biblioteca
     * @return La view di visualizzazione singola biblioteca
     */
    @GetMapping(value = "/{email}")
    @ResponseBody
    @CrossOrigin
    public Biblioteca visualizzaDatiBiblioteca(final @PathVariable String email) {
        return prenotazioneService.getBibliotecaById(email);
    }
}
