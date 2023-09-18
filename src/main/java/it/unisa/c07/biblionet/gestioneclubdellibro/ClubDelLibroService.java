package it.unisa.c07.biblionet.gestioneclubdellibro;

import it.unisa.c07.biblionet.gestioneclubdellibro.repository.ClubDelLibro;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Esperto;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Implementa l'interfaccia service
 * per il sottosistema ClubDelLibro.
 * @author Viviana Pentangelo, Gianmario Voria
 */
public interface ClubDelLibroService {


    List<Object> dettagliClub(List<ClubDelLibro> listaClubs);

    /**
     * Implementa la funzionalità che permette
     * a un Esperto di creare un Club del Libro.
     * @param club Il Club del Libro da memorizzare
     * @return Il Club del Libro appena creato
     */


    ClubDelLibro creaClubDelLibro(ClubDTO clubDTO, Esperto esperto) throws IOException;

    /**
     * Implementa la funzionalità che permette
     * di visualizzare tutti i club del libro.
     * @return La lista dei club
     */
    List<ClubDelLibro> visualizzaClubsDelLibro();


    /**
     * Implementa la funzionalità che permette
     * di filtrare tutti i club del libro.
     * @param filtro Un predicato che descrive come filtrare i Club
     * @return La lista dei club
     */
    List<ClubDelLibro> visualizzaClubsDelLibro(Predicate<ClubDelLibro> filtro);


    /**
     * Implementa la funzionalità che permette
     * di modificare ed
     * effettuare l'update di un club.
     * @param club Il club da modificare
     * @return Il club modificato
     */
    ClubDelLibro salvaClub(ClubDelLibro club);

    /**
     * Implementa la funzionalità che permette
     * di recuperare un
     * club dato il suo ID.
     * @param id L'ID del club da recuperare
     * @return Il club recuperato
     */
    ClubDelLibro getClubByID(int id);


    /**
     * Funzione di utilità che permette di leggere la città
     * in cui si trova un Club del Libro.
     * @param club il club da cui prendere la città
     * @return la città del Club
     */
    String getCittaFromClubDelLibro(ClubDelLibro club);



    /**
     * Restituisce tutte le citta nel sistema.
     * @return Tutte le citta nel sistema.
     */
    Set<String> getCitta();


    List<ClubDTO> getInformazioniClubs(List<ClubDelLibro> clubDelLibroList);
}
