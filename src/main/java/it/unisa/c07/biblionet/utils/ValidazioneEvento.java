package it.unisa.c07.biblionet.utils;

/**
 * Classe che fornisce i parametri di validazione
 * per i campi di evento.
 * @author Nicola Pagliara
 * @author Luca Topo
 */
public final class ValidazioneEvento {

    /**
     * Costruttore vuoto.
     */
    private ValidazioneEvento() { }

    /**
     * Lunghezza minima del nome dell'evento.
     */
    public static final int LUNGHEZZA_MINIMA_NOME = 1;
    /**
     * Lunghezza massima del nome di un evento.
     */
    public static final int LUNGHEZZA_MASSIMA_NOME = 30;

    /**
     * Lunghezza massima della descrizione di un evento.
     */
    public static final int LUNGHEZZA_MASSIMA_DESCRIZIONE = 255;
    /**
     * Lunghezza minima della descrizione di un evento.
     */
    public static final int LUNGHEZZA_MINIMA_DESCRIZIONE = 0;


}
