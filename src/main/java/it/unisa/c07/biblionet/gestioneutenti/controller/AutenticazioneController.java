package it.unisa.c07.biblionet.gestioneutenti.controller;

import it.unisa.c07.biblionet.gestioneutenti.AutenticazioneService;
import it.unisa.c07.biblionet.config.JwtGeneratorInterface;
import it.unisa.c07.biblionet.common.UtenteRegistrato;
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
            return new BiblionetResponse(BiblionetResponse.LOGIN_FALLITO, false);
        } else {
            return new BiblionetResponse(jwtGenerator.generateToken(utente), true);
        }
    }

}
