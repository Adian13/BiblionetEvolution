package it.unisa.c07.biblionet.gestioneclubdellibro.controller;

import it.unisa.c07.biblionet.gestioneclubdellibro.LettoreDTO;
import it.unisa.c07.biblionet.gestioneclubdellibro.LettoreService;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.ClubDelLibro;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Lettore;
import it.unisa.c07.biblionet.utils.BiblionetConstraints;
import it.unisa.c07.biblionet.utils.BiblionetResponse;
import it.unisa.c07.biblionet.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lettore")
public class LettoreController {
    private final LettoreService lettoreService;

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
    @PostMapping(value = "/registrazione")
    @ResponseBody
    @CrossOrigin
    public BiblionetResponse registrazioneLettore(@Valid @ModelAttribute LettoreDTO lettore,
                                                  BindingResult bindingResult,
                                                  final @RequestParam("conferma_password")
                                                  String password
    ) {
        if(bindingResult.hasErrors()) return new BiblionetResponse(BiblionetResponse.FORMATO_NON_VALIDO, false);

        if(!BiblionetConstraints.passwordRispettaVincoli(lettore.getPassword(), password))
            return new BiblionetResponse(BiblionetResponse.FORMATO_NON_VALIDO, false);

        lettoreService.creaLettoreDaModel(lettore);
        return new BiblionetResponse("Registrazione effettuata correttamente", true);
    }
    @PostMapping(value = "/conferma-modifica")
    @ResponseBody
    @CrossOrigin
    public BiblionetResponse modificaDatiLettore(
            final @RequestHeader(name = "Authorization") String token,
            final @Valid @RequestParam("Lettore") LettoreDTO lettore,
            BindingResult bindingResult,
            final @RequestParam("vecchia_password") String vecchia,
            final @RequestParam("nuova_password") String nuova,
            final @RequestParam("conferma_password") String conferma) {

        if(bindingResult.hasErrors()) return new BiblionetResponse(BiblionetResponse.FORMATO_NON_VALIDO, false);

        if (!Utils.isUtenteLettore(Utils.getSubjectFromToken(token)))
            return new BiblionetResponse(BiblionetResponse.NON_AUTORIZZATO, false);

        if (!Utils.getSubjectFromToken(token).equals(lettore.getEmail()))
            return new BiblionetResponse("Non puoi cambiare email", false); //todo non si può modificare la mail, va fatto anche per lettore e biblioteca sto controllo

        //todo tecnicamente non controllo se è un lettore
        if(lettoreService.findLettoreByEmailAndPassword(lettore.getEmail(), BiblionetConstraints.trasformaPassword(vecchia)) == null) return new BiblionetResponse(BiblionetResponse.NON_AUTORIZZATO, false);
        String password = (BiblionetConstraints.confrontoPassword(nuova, conferma));
        if(password.isEmpty()) return new BiblionetResponse(BiblionetResponse.RICHIESTA_NON_VALIDA, false);
        lettore.setPassword(password);

        lettoreService.aggiornaLettoreDaModel(lettore);
        return new BiblionetResponse("Dati aggiornati", true);
    }




    /**
     * Implementa la funzionalità di visualizzazione dei clubs
     * a cui il lettore é iscritto.
     *
     * @return La view di visualizzazione dei clubs a cui é iscritto
     */
    @GetMapping(value = "area-utente/visualizza-clubs-personali-lettore")
    @ResponseBody
    @CrossOrigin
    public List<ClubDelLibro> visualizzaClubsLettore(
            final @RequestHeader(name = "Authorization") String token
    ) {
        if (!Utils.isUtenteLettore(Utils.getSubjectFromToken(token))) return new ArrayList<>();
        Lettore lettore =  lettoreService.getLettoreByEmail(Utils.getSubjectFromToken(token));
        return lettore.getClubs();
    }





}
