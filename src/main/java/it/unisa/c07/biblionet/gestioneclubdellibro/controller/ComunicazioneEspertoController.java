package it.unisa.c07.biblionet.gestioneclubdellibro.controller;

import it.unisa.c07.biblionet.gestioneclubdellibro.ClubDelLibroService;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Esperto;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.EspertoDAO;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Lettore;
import it.unisa.c07.biblionet.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @author Alessio Casolaro
 * @author Della Porta Antonio
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/comunicazione-esperto")
public class ComunicazioneEspertoController {

    /**
     * Il service per effettuare le operazioni di persistenza.
     */
    @Qualifier("clubDelLibroService")
    @Autowired
    private final ClubDelLibroService clubService;

     @Autowired
     private EspertoDAO espertoDAO;

    /**
     * Implementa la funzionalità di mostrare gli esperti in base
     * ai generi preferiti del lettore.
     * @return la view contenente la lista
     */
    @GetMapping(value = "/visualizza-esperti-genere")
    @ResponseBody
    @CrossOrigin
    public final List<Esperto> visualizzaEspertiGeneri(@RequestHeader(name = "Authorization") final String token) {
        if(!Utils.isUtenteLettore(token)) return new ArrayList<>();
        Lettore lettore = clubService.findLettoreByEmail(Utils.getSubjectFromToken(token));
        return espertoDAO.findByGenereSet(lettore.getGenereSet());
    }

    /**
     * Implementa la funzionalità di visualizzare tutti gli Esperti
     * presenti sulla piattaforma.
     * @return la view che visualizza tutti gli Esperti
     */
    @GetMapping(value = "/lista-esperti")
    @CrossOrigin
    @ResponseBody
    public final List<Esperto> visualizzaListaEsperti() {
        return espertoDAO.findAll();
    }

    /**
     * Implementa la funzionalità di visualizzare tutti gli Esperti
     * presenti sulla piattaforma.
     * @param stringa il contenuto del filtro
     * @param filtro il nome del filtro
     * @return la view che visualizza tutti gli Esperti
     */
    @GetMapping(value = "/ricerca")
    @ResponseBody
    @CrossOrigin
    public final List<Esperto> visualizzaListaEspertiFiltrati(
            @RequestParam("stringa") final String stringa,
            @RequestParam("filtro") final String filtro) {
        switch (filtro) {
            case "nome":
                return espertoDAO.findByNome(stringa);
            case "genere":
                return espertoDAO.findByGenereSet(stringa);
            default:
                return espertoDAO.findAll();
        }
    }
}
