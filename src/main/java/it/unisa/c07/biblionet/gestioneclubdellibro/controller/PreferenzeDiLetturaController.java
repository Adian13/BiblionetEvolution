package it.unisa.c07.biblionet.gestioneclubdellibro.controller;


import it.unisa.c07.biblionet.gestioneclubdellibro.*;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Genere;
import it.unisa.c07.biblionet.utils.BiblionetResponse;
import it.unisa.c07.biblionet.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import java.util.Set;

/**
 * @author Alessio Casolaro
 * @author Antonio Della Porta
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/preferenze-di-lettura")
public class PreferenzeDiLetturaController {

    /**
     * Il service per effettuare le operazioni di persistenza.
     */
    @Autowired
    private  PreferenzeDiLetturaService preferenzeDiLetturaService;
    @Autowired
    private GenereService genereService;
    @Autowired
    private LettoreService lettoreService;
    @Autowired
    private EspertoService espertoService;

    /**
     * Implementa la funzionalità di controllare se l'utente in sessione
     * può modificare i generi e restituisce una lista dei generi che può aggiungere
     * ottenuta dalla lista totale dei generi, da cui sono stati rimossi i generi già padroneggiati dall'utente
     *
     * @return la view d'inserimento dei generi se l'utente è
     * Esperto o Lettore, la home altrimenti,

    @RequestMapping("/generi")
    @ResponseBody
    @CrossOrigin
    public List<String> generiLetterari(
            @RequestHeader(name = "Authorization") final String token
    ) {
        List<String> nomiGenere;

        if (Utils.isUtenteEsperto(token)) {
            Esperto esperto = autenticazioneService.findEspertoByEmail(Utils.getSubjectFromToken(token));
            nomiGenere = new ArrayList<>(esperto.getGeneri());
        } else if (Utils.isUtenteLettore(token)) {
            Lettore lettore = autenticazioneService.findLettoreByEmail(Utils.getSubjectFromToken(token));
            nomiGenere = new ArrayList<>(lettore.getGeneri());
        } else return new ArrayList<>();


        Set<Genere> allGeneri = genereService.getAllGeneri();
        Set<Genere> generiUtente = genereService.getGeneriByName(nomiGenere.toArray(new String[0]));
        if (generiUtente != null) {
            for (Genere g : generiUtente) {
                allGeneri.remove(g);
            }
        } else {
            generiUtente = new HashSet<>();
        }

        return new ArrayList<Genere>(generiUtente);

    }
     todo questa funzione non è mai usata
*/
    /**
     * Implementa la funzionalità d'inserire o rimuovere generi a un esperto
     * oppure a un lettore.
     *
     * @param generi i generi che il lettore o l'esperto dovranno possedere
     * @return la pagina home
     */
    @PostMapping(value = "/modifica-generi")
    @CrossOrigin
    @ResponseBody
    public BiblionetResponse modificaGeneri(@RequestParam("genere") final String[] generi,
                                            @RequestHeader(name = "Authorization") final String token) {

        Set<Genere> toAdd = genereService.getGeneriByName(generi);

        if (Utils.isUtenteEsperto(token)) {
            preferenzeDiLetturaService
                    .addGeneri(toAdd, espertoService.findByEmail(Utils.getSubjectFromToken(token)));
            return new BiblionetResponse("Generi modificati", true);
        }
        if (Utils.isUtenteLettore(token)) {
            preferenzeDiLetturaService.addGeneri(toAdd, lettoreService.findByEmail(Utils.getSubjectFromToken(token)));
            return new BiblionetResponse("Generi modificati", true);
        }

        return new BiblionetResponse("Errore modifica generi utente", false);
    }
}
