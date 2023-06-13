package it.unisa.c07.biblionet.gestioneclubdellibro;

import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Esperto;

import java.util.List;

/**
 * @author Alessio Casolaro
 * @author Antonio Della Porta
 */
public interface ComunicazioneEspertoService {


    /**
     * Implementa la funzionalità che permette
     * di visualizzare la lista completa dei libri
     * prenotabili di un dato genere.
     * @param genere Il nome del genere
     * @return La lista di libri
     */
    List<Esperto> visualizzaEspertiPerGenere(String genere);
}
