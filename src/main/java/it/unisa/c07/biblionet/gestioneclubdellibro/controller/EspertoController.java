package it.unisa.c07.biblionet.gestioneclubdellibro.controller;

import it.unisa.c07.biblionet.gestioneclubdellibro.EspertoDTO;
import it.unisa.c07.biblionet.gestioneclubdellibro.EspertoService;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.ClubDelLibro;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Esperto;
import it.unisa.c07.biblionet.utils.BiblionetConstraints;
import it.unisa.c07.biblionet.utils.BiblionetResponse;
import it.unisa.c07.biblionet.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequiredArgsConstructor
@RequestMapping("/esperto")
public class EspertoController {

    private final EspertoService espertoService;

    /**
     * Implementa la funzionalità di registrazione di un esperto.

    @PostMapping(value = "/registrazione")
    @ResponseBody
    @CrossOrigin
    public BiblionetResponse registrazioneEsperto(final @Valid @ModelAttribute EspertoDTO esperto,
                                                  BindingResult bindingResult,
                                                  final @RequestParam("conferma_password") String password,
                                                  final @RequestParam("email_biblioteca") String bibliotecaEmail) {

        if(bindingResult.hasErrors()) return new BiblionetResponse(BiblionetResponse.FORMATO_NON_VALIDO, false);

        if(!BiblionetConstraints.passwordRispettaVincoli(esperto.getPassword(), password))
            return new BiblionetResponse(BiblionetResponse.FORMATO_NON_VALIDO, false);

        Esperto e = espertoService.creaEspertoDaModel(esperto, bibliotecaEmail);
        if(e == null) return new BiblionetResponse(BiblionetResponse.RICHIESTA_NON_VALIDA, false);
        return new BiblionetResponse("Registrazione ok", true);
    }
    */

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
    @PostMapping(value = "/conferma-modifica")
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


        if(bindingResult.hasErrors()) return new BiblionetResponse(BiblionetResponse.FORMATO_NON_VALIDO, false);

        if (!Utils.isUtenteEsperto(Utils.getSubjectFromToken(token)))
            return new BiblionetResponse(BiblionetResponse.NON_AUTORIZZATO, false);

        if (!Utils.getSubjectFromToken(token).equals(esperto.getEmail()))
            return new BiblionetResponse("Non puoi cambiare email", false); //todo non si può modificare la mail, va fatto anche per lettore e biblioteca sto controllo

        //todo tecnicamente non controllo se è un esperto
        if(espertoService.findEspertoByEmailAndPassword(esperto.getEmail(), BiblionetConstraints.trasformaPassword(vecchia)) == null) return new BiblionetResponse(BiblionetResponse.NON_AUTORIZZATO, false);
        String password = (BiblionetConstraints.confrontoPassword(nuova, conferma));
        if(password.isEmpty()) return new BiblionetResponse(BiblionetResponse.RICHIESTA_NON_VALIDA, false);
        esperto.setPassword(password);

        espertoService.aggiornaEspertoDaModel(esperto, emailBiblioteca); //todo qualche check in più sull'esistenza dell'esperto, anche se se ha il token è autoamticamente registrato

        return new BiblionetResponse("Dati aggiornati", true);
    }

    /**
     * Implementa la funzionalità di visualizzazione dei clubs
     * che l'esperto gestisce.
     *
     * @return La view di visualizzazione dei clubs che gestisce
     */
    @GetMapping(value = "area-utente/visualizza-clubs-personali-esperto")
    @ResponseBody
    @CrossOrigin
    public List<ClubDelLibro> visualizzaClubsEsperto(final @RequestHeader(name = "Authorization") String token) {
        //todo da controllare
        if (!Utils.isUtenteEsperto(Utils.getSubjectFromToken(token))) return new ArrayList<>();
        return espertoService.findEspertoByEmail(Utils.getSubjectFromToken(token)).getClubs();
    }
    @GetMapping(value = "/visualizza-esperti-biblioteca")
    @ResponseBody
    @CrossOrigin
    public List<Esperto> visualizzaEspertiBiblioteca(
            @RequestParam final String emailBiblioteca
    ) {
        return espertoService.getEspertiByBiblioteca(emailBiblioteca);
    }

    @GetMapping(value = "/visualizza-clubs-biblioteca")
    @ResponseBody
    @CrossOrigin
    public List<ClubDelLibro> visualizzaClubBiblioteca(
            @RequestParam final String emailBiblioteca
    ) {
        Set<ClubDelLibro> clubs= new HashSet<>();
        List<Esperto> esperti = espertoService.getEspertiByBiblioteca(emailBiblioteca);
        for(Esperto esperto: esperti){
            clubs.addAll(esperto.getClubs());
        }
        return new ArrayList<>(clubs);
    }



}
