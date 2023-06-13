package it.unisa.c07.biblionet.gestioneclubdellibro;

import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Genere;

import java.util.Set;

public interface GenereService {
    Genere getGenereByName(String nome);

    /**
     * Implementa la funzionalità di restituire tutti i generi
     * presenti nel database.
     * @return la lista di tutti i generi presenti nel database
     */
    Set<Genere> getAllGeneri();

    /**
     * Implementa la funzionalità di restituire tutti i generi
     * data una lista di nomi di generi.
     * @param generi i generi da trovare
     * @return la lista di generi contenente solamente i generi effettivamente
     * presenti nel database
     */
    Set<Genere> getGeneriByName(String[] generi);

}
