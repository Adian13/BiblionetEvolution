package it.unisa.c07.biblionet.autenticazione.controller;

import io.jsonwebtoken.Claims;
import it.unisa.c07.biblionet.autenticazione.service.AutenticazioneService;
import it.unisa.c07.biblionet.model.entity.utente.Biblioteca;
import it.unisa.c07.biblionet.model.entity.utente.Esperto;
import it.unisa.c07.biblionet.model.entity.utente.Lettore;
import it.unisa.c07.biblionet.model.entity.utente.UtenteRegistrato;
import it.unisa.c07.biblionet.utils.BiblionetResponse;
import it.unisa.c07.biblionet.utils.Utils;
import it.unisa.c07.biblionet.utils.validazione.RispettoVincoli;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


/**
 * @author Alessio Casolaro, Antonio Della Porta
 */
@Controller
@SessionAttributes("loggedUser")
@RequiredArgsConstructor
public class AreaUtenteController {

    /**
     * Il service per effettuare le operazioni di persistenza.
     */
    private final AutenticazioneService autenticazioneService;

    /**
     * INUTILE DOPO IL RIFACIMENTO DELLA INTERFACCIA CON REACT
     * Implementa la funzionalità di smistare l'utente sulla view di
     * modifica dati corretta.
     *
     * @return modifica_dati_biblioteca se l'account
     * da modificare é una biblioteca.
     * modifica_dati_esperto se l'account
     * da modificare é un esperto.
     * modifica_dati_lettore se l'account
     * da modificare é un lettore.
     * @RequestMapping(value = "/modifica-dati", method = RequestMethod.GET)
     * public String modificaDati(final Model model) {
     * UtenteRegistrato utente = (UtenteRegistrato)
     * model.getAttribute("loggedUser");
     * <p>
     * if (utente != null) {
     * if (autenticazioneService.isBiblioteca(utente)) {
     * Biblioteca biblioteca = (Biblioteca) utente;
     * model.addAttribute("biblioteca", biblioteca);
     * return "area-utente/modifica-dati-biblioteca";
     * <p>
     * } else if (autenticazioneService.isEsperto(utente)) {
     * Esperto esperto = (Esperto) utente;
     * model.addAttribute("esperto", esperto);
     * return "area-utente/modifica-dati-esperto";
     * <p>
     * } else if (autenticazioneService.isLettore(utente)) {
     * Lettore lettore = (Lettore) utente;
     * model.addAttribute("lettore", lettore);
     * return "area-utente/modifica-dati-lettore";
     * <p>
     * }
     * }
     * return "autenticazione/login";
     * }
     */

    private String controlliPreliminari(BindingResult bindingResult, String vecchia, UtenteRegistrato utenteRegistrato) {
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
            final @Valid @RequestParam("Biblioteca") Biblioteca biblioteca,
            BindingResult bindingResult,
            final @RequestParam("vecchia_password") String vecchia,
            final @RequestParam("nuova_password") String nuova,
            final @RequestParam("conferma_password") String conferma) {

        Claims claims = Utils.getClaimsFromTokenWithoutKey(token);
        if (!claims.getSubject().equalsIgnoreCase(biblioteca.getEmail()))
            return new BiblionetResponse("Non sei autorizzato", false);

        biblioteca.setPassword(qualePassword(vecchia, nuova, conferma));
        String s = controlliPreliminari(bindingResult, vecchia, biblioteca);
        if (!s.isEmpty()) return new BiblionetResponse("Non sei autorizzato", false);


        autenticazioneService.aggiornaBiblioteca(biblioteca);

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
            final @Valid @RequestParam("Esperto") Esperto esperto,
            BindingResult bindingResult,
            final @RequestParam("vecchia_password") String vecchia,
            final @RequestParam("nuova_password") String nuova,
            final @RequestParam("conferma_password") String conferma,
            final @RequestParam("email_biblioteca") String emailBiblioteca) {


        Esperto toUpdate = autenticazioneService.findEspertoByEmail(esperto.getEmail());
        Claims claims = Utils.getClaimsFromTokenWithoutKey(token);
        if (!claims.getSubject().equalsIgnoreCase(esperto.getEmail()))
            return new BiblionetResponse("Non sei autorizzato", false);

        esperto.setPassword(qualePassword(vecchia, nuova, conferma));
        String s = controlliPreliminari(bindingResult, vecchia, esperto);
        if (!s.isEmpty()) new BiblionetResponse(s, false);


        Biblioteca b = autenticazioneService.findBibliotecaByEmail(emailBiblioteca);
        if (b != null) {
            esperto.setBiblioteca(b);
        } else {
            esperto.setBiblioteca(toUpdate.getBiblioteca());
        }

        autenticazioneService.aggiornaEsperto(esperto);

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
    @RequestMapping(value = "/conferma-modifica-lettore",
            method = RequestMethod.POST)
    public BiblionetResponse confermaModificaLettore(final @RequestHeader(name = "Authorization") String token,
                                                     final @Valid @ModelAttribute Lettore lettore,
                                                     BindingResult bindingResult,
                                                     final @RequestParam("vecchia_password") String vecchia,
                                                     final @RequestParam("nuova_password") String nuova,
                                                     final @RequestParam("conferma_password") String conferma) {


        Claims claims = Utils.getClaimsFromTokenWithoutKey(token);
        //if(! claims.getSubject().equalsIgnoreCase(lettore.getEmail()))
        //  return new ResponseEntity<>("Non sei autorizzato", HttpStatus.FORBIDDEN);

        lettore.setPassword(qualePassword(vecchia, nuova, conferma));
        String s = controlliPreliminari(bindingResult, vecchia, lettore);
        if (!s.isEmpty()) return new BiblionetResponse(s, false);


        autenticazioneService.aggiornaLettore(lettore);

        return new BiblionetResponse("Dati aggiornati", true);
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
        {
            if (Utils.isUtenteBiblioteca(token)) {
                return autenticazioneService.findBibliotecaByEmail(Utils.getSubjectFromToken(token));
            } else if (Utils.isUtenteEsperto(token)) {
                return autenticazioneService.findEspertoByEmail(Utils.getSubjectFromToken(token));
            } else if (Utils.isUtenteLettore(token)) {
                return autenticazioneService.findLettoreByEmail(Utils.getSubjectFromToken(token));
            }
        }
        return null;
    }


    /**
     * Implementa la funzionalità di visualizzazione dei clubs
     * a cui il lettore é iscritto.
     * @param model Utilizzato per gestire la sessione.
     * @return La view di visualizzazione dei clubs a cui é iscritto

     @RequestMapping(value = "area-utente/visualizza-clubs-personali-lettore",
     method = RequestMethod.GET)
     public String visualizzaClubsLettore(final Model model) {
     Lettore utente = (Lettore) model.getAttribute("loggedUser");
     if (utente != null && autenticazioneService.isLettore(utente)) {
     model.addAttribute("clubs",
     autenticazioneService.findAllByLettori(utente));
     return "area-utente/visualizza-clubs-personali";
     }
     return "autenticazione/login";
     }
     */

    /**
     * Implementa la funzionalità di visualizzazione dei clubs
     * che l'esperto gestisce.
     * @param model Utilizzato per gestire la sessione.
     * @return La view di visualizzazione dei clubs che gestisce

     @RequestMapping(value = "area-utente/visualizza-clubs-personali-esperto",
     method = RequestMethod.GET)
     public String visualizzaClubsEsperto(final Model model) {
     Esperto utente = (Esperto) model.getAttribute("loggedUser");
     if (utente != null && autenticazioneService.isEsperto(utente)) {
     model.addAttribute("clubs",
     autenticazioneService.findAllByEsperto(utente));
     return "area-utente/visualizza-clubs-personali";
     }
     return "autenticazione/login";
     }
     */
}
