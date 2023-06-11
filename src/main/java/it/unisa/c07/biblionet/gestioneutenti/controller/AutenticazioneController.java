package it.unisa.c07.biblionet.gestioneutenti.controller;

import it.unisa.c07.biblionet.gestioneutenti.AutenticazioneService;
import it.unisa.c07.biblionet.config.JwtGeneratorInterface;
import it.unisa.c07.biblionet.entity.UtenteRegistrato;
import it.unisa.c07.biblionet.utils.BiblionetResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


/**
 * Implementa il controller per il sottosistema
 * Autenticazione.
 * @author Ciro Maiorino , Giulio Triggiani
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/autenticazione")
public class AutenticazioneController {
    /**
     * Il service per effettuare le operazioni di persistenza.
     */
    private final AutenticazioneService autenticazioneService;
    private final JwtGeneratorInterface jwtGenerator;
    /**
     * Implementa la funzionalità che permette
     * di visualizzare la view del login.
     * @param model il Model
     * @return la pagina dove è visualizzato

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String visualizzaLogin(final Model model) {
        model.addAttribute("loggedUser", null);
        return "autenticazione/login";
    }
   */

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

        UtenteRegistrato utente = autenticazioneService.login(email, password);

        if (utente == null) {
            return new BiblionetResponse("I dati di autenticazione non sono validi", false);
        } else {
            return new BiblionetResponse(jwtGenerator.generateToken(utente), true);
        }
    }

    /**
     * Implenta la funzionalità che permette
     * di effettuare il logout dell'utente
     * togliendolo dalla sessione.
     * @param status contiene i dati della sessione.
     * @return Rimanda alla pagina di index.

    @RequestMapping(value = "/logout", method = RequestMethod.GET)

    public String logout(final SessionStatus status) {
        status.setComplete();
        return "index";
    }
    */

    /**
     * Implementa la funzionalità che permette
     * di aggiungere un utente alla sessione.
     * @return dell'utente in sessione.

    @ModelAttribute("loggedUser")
    public UtenteRegistrato utenteRegistrato() {
        return new UtenteRegistrato();
    }
     */
}
