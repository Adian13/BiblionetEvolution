package it.unisa.c07.biblionet.gestioneclubdellibro.service;

import it.unisa.c07.biblionet.gestioneclubdellibro.ClubDelLibroService;
import it.unisa.c07.biblionet.gestioneclubdellibro.EspertoService;
import it.unisa.c07.biblionet.gestioneclubdellibro.LettoreService;
import it.unisa.c07.biblionet.gestioneclubdellibro.PreferenzeDiLetturaService;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Esperto;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Genere;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Lettore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Alessio Casolaro
 * @author Antonio Della Porta
 */
@Service
@RequiredArgsConstructor
public class PreferenzeDiLetturaServiceImpl implements
        PreferenzeDiLetturaService {


    private final EspertoService espertoService;
    private final LettoreService lettoreService;

    /**
     * Implementa la funzionalità di aggiungere una lista di generi
     * ad un esperto.
     * @param generi i generi da inserire
     * @param esperto l'esperto a cui inserirli
     */
    @Override
    public void addGeneriEsperto(final Set<Genere> generi,
                                 final Esperto esperto) {
        Set<String> setGeneri = new HashSet<>();
        for(Genere genere: generi){
            setGeneri.add(genere.getNome());
        }
        esperto.setNomeGeneri(setGeneri);
        espertoService.aggiornaEsperto(esperto);
    }

    /**
     * Implementa la funzionalità di aggiungere una lista di generi
     * a un lettore.
     * @param generi i generi da inserire
     * @param lettore il lettore a cui inserirli
     */
    @Override
    public void addGeneriLettore(final Set<Genere> generi,
                                 final Lettore lettore) {
        Set<String> setGeneri = new HashSet<>();
        for(Genere genere: generi){
            setGeneri.add(genere.getNome());
        }
        lettore.setNomeGeneri(setGeneri);
        lettoreService.aggiornaLettore(lettore);

    }


}
