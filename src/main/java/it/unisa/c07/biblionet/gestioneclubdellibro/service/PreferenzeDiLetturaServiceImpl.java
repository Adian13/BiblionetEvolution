package it.unisa.c07.biblionet.gestioneclubdellibro.service;

import it.unisa.c07.biblionet.common.UtenteRegistrato;
import it.unisa.c07.biblionet.gestioneclubdellibro.ClubDelLibroService;
import it.unisa.c07.biblionet.gestioneclubdellibro.EspertoService;
import it.unisa.c07.biblionet.gestioneclubdellibro.LettoreService;
import it.unisa.c07.biblionet.gestioneclubdellibro.PreferenzeDiLetturaService;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Alessio Casolaro
 * @author Antonio Della Porta
 */
@Service
@RequiredArgsConstructor
public class PreferenzeDiLetturaServiceImpl implements PreferenzeDiLetturaService {

    @Autowired
    GenereDAO generiRepository;

    @Autowired
    private final ClubDelLibroService clubDelLibroService;

    @Autowired
    EspertoService espertoService;
    @Autowired
    LettoreService lettoreService;
    /**
     * Implementa la funzionalità di aggiungere una lista di generi
     * ad un esperto.
     * @param generi i generi da inserire
     * @param utenteRegistrato
     */
    public void addGeneri(Set<Genere> generi,
                                 UtenteRegistrato utenteRegistrato) {
        if(utenteRegistrato.getTipo().equalsIgnoreCase("Lettore")){
            Lettore l = lettoreService.findByEmail(utenteRegistrato.getEmail());
            l.getGenereSet().addAll(generi.stream().collect(Collectors.toSet()));
            lettoreService.save(l);
        }
        if(utenteRegistrato.getTipo().equalsIgnoreCase("Esperto")){
            Esperto e = espertoService.findByEmail(utenteRegistrato.getEmail());
            e.getGenereSet().addAll(generi.stream().collect(Collectors.toSet()));
            espertoService.save(e);
        }

    }

}
