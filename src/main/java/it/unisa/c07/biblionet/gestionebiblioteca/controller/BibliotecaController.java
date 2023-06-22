package it.unisa.c07.biblionet.gestionebiblioteca.controller;

import it.unisa.c07.biblionet.common.UtenteRegistrato;
import it.unisa.c07.biblionet.common.UtenteRegistratoDTO;
import it.unisa.c07.biblionet.events.CreateBiblioteca;
import it.unisa.c07.biblionet.events.MiddleEsperto;
import it.unisa.c07.biblionet.gestionebiblioteca.BibliotecaDTO;
import it.unisa.c07.biblionet.gestionebiblioteca.LibroBibliotecaDTO;
import it.unisa.c07.biblionet.gestionebiblioteca.PrenotazioneLibriService;
import it.unisa.c07.biblionet.gestionebiblioteca.repository.LibroBiblioteca;
import it.unisa.c07.biblionet.gestionebiblioteca.repository.Biblioteca;
import it.unisa.c07.biblionet.utils.BiblionetConstraints;
import it.unisa.c07.biblionet.utils.BiblionetResponse;
import it.unisa.c07.biblionet.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
@Controller
@RequiredArgsConstructor
@RequestMapping("/biblioteca")
public class BibliotecaController {



    /**
     * Il service per effettuare le operazioni di
     * persistenza.
     */
    private final PrenotazioneLibriService prenotazioneService;
    private final ApplicationEventPublisher events;

    /**
     * Implementa la funzionalità di modifica dati di una biblioteca.
     *
     * @param biblioteca email della biblioteca da modificare.
     * @param vecchia    La vecchia password dell'account.
     * @param nuova      La nuova password dell'account.
     * @param conferma   La password di conferma password dell'account.
     * @return login Se la modifica va a buon fine.
     * modifica_dati_biblioteca Se la modifica non va a buon fine
     */
    @PostMapping(value = "/conferma-modifica-biblioteca")
    @ResponseBody
    @CrossOrigin
    public BiblionetResponse modificaDatiBiblioteca(
            final @RequestHeader(name = "Authorization") String token,
            final @Valid @ModelAttribute("Biblioteca") BibliotecaDTO biblioteca,
            BindingResult bindingResult,
            final @RequestParam("vecchia_password") String vecchia,
            final @RequestParam("nuova_password") String nuova,
            final @RequestParam("conferma_password") String conferma) {

        if(!Utils.isUtenteBiblioteca(token)) return new BiblionetResponse(BiblionetResponse.NON_AUTORIZZATO, false);

        if (!Utils.getSubjectFromToken(token).equals(biblioteca.getEmail()))
            return new BiblionetResponse("Non puoi cambiare email", false); //todo non si può modificare la mail, va fatto anche per lettore e biblioteca sto controllo


        String password = (BiblionetConstraints.confrontoPassword(nuova, conferma));
        if(password.isEmpty()) return new BiblionetResponse(BiblionetResponse.RICHIESTA_NON_VALIDA, false);
            String s = controlliPreliminari(bindingResult, vecchia, biblioteca);
        if (!s.isEmpty()) return new BiblionetResponse(s, false);
        biblioteca.setPassword(password);

        UtenteRegistrato b = prenotazioneService.bibliotecaDaModel(biblioteca);
        if(b==null) return new BiblionetResponse(BiblionetResponse.ERRORE, false);
        return new BiblionetResponse(BiblionetResponse.OPERAZIONE_OK, true);
    }

    /**
     * Implementa la funzionalità che permette di
     * visualizzare tutte le biblioteche iscritte.
     * @return La view per visualizzare le biblioteche
     */
    @GetMapping(value = "/visualizza-biblioteche")
    @ResponseBody
    @CrossOrigin
    public List<Biblioteca> visualizzaListaBiblioteche() {
        return prenotazioneService.findAllBiblioteche();
    }

    /**
     * Implementa la funzionalità di registrazione di una biblioteca.
     *
     * @param biblioteca la biblioteca da registrare
     * @param password   la password di conferma
     * @return la view di login
     */
    @PostMapping(value = "/biblioteca")
    @ResponseBody
    @CrossOrigin
    public BiblionetResponse registrazioneBiblioteca(@Valid @ModelAttribute BibliotecaDTO biblioteca,
                                                     BindingResult bindingResult,
                                                     @RequestParam("conferma_password") String password
    ) {
        String s = controlliPreliminari(bindingResult, password, biblioteca);
        if (!s.isEmpty()) {
            return new BiblionetResponse(s, false);
        }
        prenotazioneService.bibliotecaDaModel(biblioteca);
        return new BiblionetResponse("Registrazione effettuata correttamente", true);
    }

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

        UtenteRegistrato utente = prenotazioneService.loginBiblioteca(email, password);

        if (utente == null) {
            return new BiblionetResponse("Login fallito.", false);
        } else {
            return new BiblionetResponse("", true);
        }
    }

    private String controlliPreliminari(BindingResult bindingResult, String vecchia, UtenteRegistratoDTO utenteRegistrato) {
        if (bindingResult.hasErrors()) {
            return "Errore di validazione";
        }
        if (prenotazioneService.loginBiblioteca(utenteRegistrato.getEmail(), vecchia) == null) { //usata solo per vedere se la password vecchia corrisponde, non effettua davvero il login
            return "Password errata. Non sei autorizzato a modificare la password.";
        }


        return "";

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
            return new BiblionetResponse(BiblionetResponse.NON_AUTORIZZATO, false);
        }
        if (isbn == null) {
            return new BiblionetResponse("L'ISBN inserito non è valido", false);
        }
        Biblioteca b = prenotazioneService.findBibliotecaByEmail(Utils.getSubjectFromToken(token));

        List<String> glist = Arrays.asList(generi.clone());
        LibroBiblioteca l = prenotazioneService.inserimentoPerIsbn(
                isbn, b.getEmail(), numCopie, glist);
        if (l == null) {
            return new BiblionetResponse("Libro non creato", false);
        }
        return new BiblionetResponse("Libro creato con successo", true);

    }

    /**
     * Implementa la funzionalità che permette inserire
     * un libro alla lista dei possessi preso
     * dal db.
     *
     * @param idLibro  l'ID del libro
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
        Biblioteca b =  prenotazioneService.findBibliotecaByEmail(Utils.getSubjectFromToken(token));
        prenotazioneService.inserimentoDalDatabase(idLibro, b.getEmail(), numCopie);
        return new BiblionetResponse("Libro inserito con successo", true);

    }

    /**
     * Implementa la funzionalità che permette inserire
     * un libro manualmente tramite form.
     *
     * @param libro             Il libro da salvare
     * @param numCopie          il numero di copie possedute
     * @param annoPubblicazione l'anno di pubblicazione
     * @return La view per visualizzare il libro
     */
    @PostMapping(value = "/inserimento-manuale")
    @ResponseBody
    @CrossOrigin
    public BiblionetResponse inserisciManualmente(
            @RequestHeader (name="Authorization") final String token,
            @RequestParam final LibroBibliotecaDTO libro,
            @RequestParam final int numCopie,
            @RequestParam final String annoPubblicazione) {

        if (!Utils.isUtenteBiblioteca(token)) {
            return new BiblionetResponse("Non sei autorizzato", false);
        }
        Biblioteca b = prenotazioneService.findBibliotecaByEmail(Utils.getSubjectFromToken(token));
        LibroBiblioteca l = new LibroBiblioteca();
        l.setTitolo(libro.getTitolo());
        l.setIsbn(libro.getIsbn());
        l.setDescrizione(libro.getDescrizione());
        l.setCasaEditrice(libro.getCasaEditrice());
        l.setAutore(libro.getAutore());
        if (libro.getImmagineLibro() != null) {
            byte[] imageBytes = libro.getImmagineLibro().getBytes();
            String base64Image =
                    Base64.getEncoder().encodeToString(imageBytes);
            l.setImmagineLibro(base64Image);
        }
        LocalDateTime anno = LocalDateTime.of(
                Integer.parseInt(annoPubblicazione), 1, 1, 1, 1);
        l.setAnnoDiPubblicazione(anno);
        LibroBiblioteca newLibro = prenotazioneService.inserimentoManuale(l, b.getEmail(), numCopie, new ArrayList<>(libro.getGeneri()));
        if(newLibro == null) return new BiblionetResponse("Errore inserimento libro", false);
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
                return prenotazioneService.findBibliotecaByNome(stringa);
            case "citta":
                return prenotazioneService.findBibliotecaByCitta(stringa);
            default:
                return prenotazioneService.findAllBiblioteche();
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
        return prenotazioneService.findBibliotecaByEmail(email);
    }
}
