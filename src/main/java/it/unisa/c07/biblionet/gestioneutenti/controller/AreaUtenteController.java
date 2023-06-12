package it.unisa.c07.biblionet.gestioneutenti.controller;

import it.unisa.c07.biblionet.gestioneclubdellibro.ClubDelLibroService;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.ClubDelLibro;
import it.unisa.c07.biblionet.gestioneclubdellibro.EspertoDTO;
import it.unisa.c07.biblionet.DTO.UtenteRegistratoDTO;
import it.unisa.c07.biblionet.gestioneclubdellibro.LettoreDTO;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Esperto;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Lettore;
import it.unisa.c07.biblionet.gestioneprestitilibro.BibliotecaDTO;
import it.unisa.c07.biblionet.gestioneprestitilibro.repository.Biblioteca;
import it.unisa.c07.biblionet.gestioneprestitilibro.PrenotazioneLibriService;
import it.unisa.c07.biblionet.gestioneutenti.AutenticazioneService;
import it.unisa.c07.biblionet.common.*;
import it.unisa.c07.biblionet.common.UtenteRegistratoDAO;
import it.unisa.c07.biblionet.utils.BiblionetResponse;
import it.unisa.c07.biblionet.utils.RispettoVincoli;
import it.unisa.c07.biblionet.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Alessio Casolaro, Antonio Della Porta
 */
@Controller
@RequiredArgsConstructor
public class AreaUtenteController {

    /**
     * Il service per effettuare le operazioni di persistenza.
     */
    private final AutenticazioneService autenticazioneService;
    private final UtenteRegistratoDAO utenteRegistratoDAO; //todo i DAO è meglio gestirli nei service
    private final PrenotazioneLibriService prenotazioneLibriService;

    /**
     * Implementa la funzionalità di smistare l'utente sulla view di
     * modifica dati corretta.
     *
     * @return modifica_dati_biblioteca se l'account
     * da modificare é una biblioteca.
     * <p>
     * modifica_dati_esperto se l'account
     * da modificare é un esperto.
     * <p>
     * modifica_dati_lettore se l'account
     * da modificare é un lettore.
     */
    @GetMapping(value = "/modifica-dati")
    @CrossOrigin
    @ResponseBody
    public UtenteRegistrato modificaDati(@RequestHeader(name = "Authorization") final String token) {
        //todo a me sembra simile alla sottostante, quindi le ho collassate in una sola, l'ideale sarebbe
        //semplificare tutto anche lato client
        return selezionaUtente(token);
    }

    private UtenteRegistrato selezionaUtente(String token){

        return utenteRegistratoDAO.getOne(Utils.getSubjectFromToken(token));
    }
    /**
     * Implementa la funzionalità di visualizzazione area utente
     * in base al tipo.
     *
     * @return La view di visualizzazione area utente
     */
    @GetMapping(value = "/area-utente")
    @ResponseBody
    @CrossOrigin
    public UtenteRegistrato datiAreaUtente(
            @RequestHeader(name = "Authorization") final String token
    ) {
        return selezionaUtente(token);

    }

    private String controlliPreliminari(BindingResult bindingResult, String vecchia, UtenteRegistratoDTO utenteRegistrato) {
        if (bindingResult.hasErrors()) {
            return "Errore di validazione";
        }

        if (autenticazioneService.login(utenteRegistrato.getEmail(), vecchia) == null) { //usata solo per vedere se la password vecchia corrisponde, non effettua davvero il login
            return "Password errata. Non sei autorizzato a modificare la password.";
        }

        return "";

    }

    private String qualePassword(String vecchia, String nuova, String conferma) {
        if (nuova.isEmpty() && conferma.isEmpty()) return vecchia;
        if (RispettoVincoli.confrontoPassword(nuova, conferma)) {
            return conferma;
        }
        return vecchia;
    }

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

        biblioteca.setPassword(qualePassword(vecchia, nuova, conferma).getBytes());
        String s = controlliPreliminari(bindingResult, vecchia, biblioteca);
        if (!s.isEmpty()) return new BiblionetResponse(s, false);


        //prenotazioneLibriService.salvaBiblioteca(biblioteca);

        return new BiblionetResponse(BiblionetResponse.OPERAZIONE_OK, true);
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
            return new BiblionetResponse("Non sei autorizzato", false);

        esperto.setPassword(qualePassword(vecchia, nuova, conferma).getBytes());
        String s = controlliPreliminari(bindingResult, vecchia, esperto);
        if (!s.isEmpty()) return new BiblionetResponse(s, false);


        Biblioteca b = prenotazioneLibriService.findBibliotecaByEmail(emailBiblioteca);
        if (b != null) {
            //esperto.setBiblioteca(b);
        } else {
            //esperto.setBiblioteca(toUpdate.getBiblioteca());
        }

        //autenticazioneService.aggiornaEsperto(esperto);

        return new BiblionetResponse("Dati aggiornati", true);
    }

    /**
     * Implementa la funzionalità di modifica dati di un lettore.
     *
     * @param lettore  Un lettore da modificare.
     * @param vecchia  La vecchia password dell'account.
     * @param nuova    La nuova password dell'account.
     * @param conferma La password di conferma password dell'account.
     * @return login Se la modifica va a buon fine.
     * modifica_dati_lettore Se la modifica non va a buon fine
     */
    @PostMapping(value = "/conferma-modifica-lettore")
    @ResponseBody
    @CrossOrigin
    public BiblionetResponse confermaModificaLettore(final @RequestHeader(name = "Authorization") String token,
                                                     final @Valid @ModelAttribute LettoreDTO lettore,
                                                     BindingResult bindingResult,
                                                     final @RequestParam("vecchia_password") String vecchia,
                                                     final @RequestParam("nuova_password") String nuova,
                                                     final @RequestParam("conferma_password") String conferma) {

        lettore.setPassword(qualePassword(vecchia, nuova, conferma).getBytes());
        String s = controlliPreliminari(bindingResult, vecchia, lettore);
        if (!s.isEmpty()) return new BiblionetResponse(s, false);


        //autenticazioneService.aggiornaLettore(lettore);

        return new BiblionetResponse("Dati aggiornati", true);
    }



}
