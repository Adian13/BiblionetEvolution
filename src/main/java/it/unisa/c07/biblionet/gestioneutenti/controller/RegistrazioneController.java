package it.unisa.c07.biblionet.gestioneutenti.controller;

import it.unisa.c07.biblionet.common.UtenteRegistratoDTO;
import it.unisa.c07.biblionet.gestioneclubdellibro.EspertoDTO;
import it.unisa.c07.biblionet.gestioneclubdellibro.LettoreDTO;
import it.unisa.c07.biblionet.gestionebiblioteca.BibliotecaDTO;
import it.unisa.c07.biblionet.gestionebiblioteca.PrenotazioneLibriService;
import it.unisa.c07.biblionet.gestioneutenti.RegistrazioneService;
import it.unisa.c07.biblionet.utils.BiblionetResponse;
import it.unisa.c07.biblionet.utils.BiblionetConstraints;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author Alessio Casolaro
 * @author Antonio Della Porta
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/registrazione")
public final class RegistrazioneController {

    /**
     * Il service per effettuare le operazioni di persistenza.
     */
    private final RegistrazioneService registrazioneService;
    private final PrenotazioneLibriService prenotazioneLibriService;

    /**
     * Implementa la funzionalità di visualizzare
     * la scelta di registrazione.
     *
     * @return La pagina di visualizzazione

     @RequestMapping(value = "/", method = RequestMethod.GET)
     public String visualizzaRegistrazione() {
     return "registrazione/registrazione";
     }
     */

    /**
     * Implementa la funzionalità di registrazione di
     * scegliere il tipo di utente da registrare.
     *
     * @param bibliotecaEmail la mail dell'account della biblioteca
     *                        dove l'esperto lavora
     * @return La view che visualizza il form di registrazione scelto.
     * @return la view per effettuare il login
     * @RequestMapping(value = "/register", method = RequestMethod.POST)
     * public Boolean effettuaRegistrazione(final @RequestBody
     * RequestRegister request) {
     * <p>
     * // controlla che la mail non sia registrata
     * if(registrazioneService.isEmailRegistrata(request.getEmail()))
     * return false;
     * <p>
     * if(request.getRole().equalsIgnoreCase("biblioteca")){
     * registrazioneService.registraBiblioteca(new Biblioteca(request.getEmail(),request.getPassword(),request.getProvincia(),request.getCitta(),request.getVia(),request.getRecapitoTelefonico(),request.getNome()));
     * return true;
     * }
     * <p>
     * else if(request.getRole().equalsIgnoreCase("esperto")){
     * registrazioneService.registraEsperto(new Esperto(request.getEmail(), request.getPassword(), request.getProvincia(),request.getCitta(),request.getVia(),request.getRecapitoTelefonico(), request.getUsername(), request.getNome(), request.getCognome(), registrazioneService.getBibliotecaByEmail("")));
     * return true;
     * }
     * <p>
     * else if(request.getRole().equalsIgnoreCase("lettore")){
     * //replica per lettore e controlla che non ci siano altre mail
     * registrazioneService.registraLettore(new Lettore(request.getEmail(), request.getPassword(), request.getProvincia(),request.getCitta(),request.getVia(),request.getRecapitoTelefonico(), request.getUsername(), request.getNome(), request.getCognome()));
     * }
     * return false;
     * }
     * <p>
     * <p>
     * /**
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
        registrazioneService.aggiornaEsperto(esperto, bibliotecaEmail);
        return new BiblionetResponse("Registrazione ok", true);
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
                                                     @RequestParam String nomeBiblioteca,
                                                     BindingResult bindingResult,
                                                     @RequestParam("conferma_password") String password
    ) {
        System.out.println(biblioteca.getPassword());
        String s = controlliPreliminari(bindingResult, password, biblioteca);
        if (!s.isEmpty()) {
            return new BiblionetResponse(s, false);
        }
        registrazioneService.aggiornaBiblioteca(biblioteca);
        return new BiblionetResponse("Registrazione effettuata correttamente", true);
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

        registrazioneService.registraLettore(lettore);
        return new BiblionetResponse("Registrazione effettuata correttamente", true);
    }

    private String controlliPreliminari(BindingResult bindingResult, String password, UtenteRegistratoDTO utenteRegistrato) {
        if (bindingResult.hasErrors()) {
            return "Errore di validazione";
        }
        if (registrazioneService.isEmailRegistrata(utenteRegistrato.getEmail())) {
            return "Il sistema presenta un account già registrato per questo indirizzo e-mail.";
        }
        if (!BiblionetConstraints.passwordRispettaVincoli(utenteRegistrato.getPassword(), password)) {
            return "Password non adeguata";
        }
        return "";
    }

}
