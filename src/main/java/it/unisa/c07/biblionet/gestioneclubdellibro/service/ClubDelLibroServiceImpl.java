package it.unisa.c07.biblionet.gestioneclubdellibro.service;

import it.unisa.c07.biblionet.gestioneclubdellibro.ClubDelLibroService;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Implementa la classe che esplicita i metodi
 * definiti nell'interfaccia service per il
 * sottosistema ClubDelLibro.
 *
 * @author Viviana Pentangelo, Gianmario Voria
 */
@Service
@RequiredArgsConstructor
public class ClubDelLibroServiceImpl implements ClubDelLibroService {

    /**
     * Si occupa delle operazioni CRUD per un club.
     */
    private final ClubDelLibroDAO clubDAO;





    /**
     * Implementa la funzionalità che permette
     * a un Esperto di creare un Club del Libro.
     *
     * @param club Il Club del Libro da memorizzare
     * @return Il Club del Libro appena creato
     */
    @Override
    public ClubDelLibro creaClubDelLibro(final ClubDelLibro club) {
        return clubDAO.save(club);
    }

    /**
     * Implementa la funzionalità che permette
     * di visualizzare tutti i club del libro.
     *
     * @return La lista dei club
     */
    @Override
    public List<ClubDelLibro> visualizzaClubsDelLibro() {
        return this.visualizzaClubsDelLibro(x -> true);
    }

    /**
     * Implementa la funzionalità che permette
     * di filtrare tutti i club del libro.
     *
     * @param filtro Un predicato che descrive come filtrare i Club
     * @return La lista dei club
     */
    public List<ClubDelLibro> visualizzaClubsDelLibro(
            final Predicate<ClubDelLibro> filtro) {

        var clubs = this.clubDAO.findAll();

        return clubs.stream().filter(
                filtro
        ).collect(Collectors.toList());

    }


    /**
     * Implementa la funzionalità che permette
     * di modificare ed
     * effettuare l'update di un club.
     *
     * @param club Il club da modificare
     * @return Il club modificato
     */
    @Override
    public ClubDelLibro modificaDatiClub(final ClubDelLibro club) {
        return clubDAO.save(club);
    }

    /**
     * Implementa la funzionalità che permette
     * di recuperare un
     * club dato il suo ID.
     *
     * @param id L'ID del club da recuperare
     * @return Il club recuperato
     */
    @Override
    public ClubDelLibro getClubByID(final int id) {
        Optional<ClubDelLibro> club = clubDAO.findById(id);
        return club.orElse(null);
    }



    /**
     * Funzione di utilità che permette di leggere la città
     * in cui si trova un Club del Libro.
     *
     * @param club il club da cui prendere la città
     * @return la città del club
     */
    public String getCittaFromClubDelLibro(final ClubDelLibro club) {
        return club.getEsperto().getBiblioteca().getCitta();
    }


    /**
     * Restituisce tutte le citta nel sistema.
     *
     * @return Tutte le citta nel sistema
     */
    public Set<String> getCitta() {
        return this.clubDAO.findAll().stream()
                .map(this::getCittaFromClubDelLibro)
                .collect(Collectors.toSet());
    }


    /**
     * Implementa la funzionalità di prendere una lista di club
     * del libro a cui un lettore partecipa.
     *
     * @param lettore il lettore preso in esame
     * @return la lista dei club del libro a cui partecipa
     */
    @Override
    public List<ClubDelLibro> findAllByLettore(final Lettore lettore) {
        return clubDAO.findAllByLettori(lettore);
    }

    /**
     * Implementa la funzionalità di prendere una lista di club
     * del libro di cui un esperto è proprietario.
     *
     * @param esperto l' esperto preso in esame
     * @return la lista dei club del libro a cui partecipa
     */
    @Override
    public List<ClubDelLibro> findAllByEsperto(final Esperto esperto) {
        return clubDAO.findAllByEsperto(esperto);
    }

    @Override
    public List<ClubDelLibro> getClubsByEsperto(Esperto esperto){
        return clubDAO.findAllByEsperto(esperto);
    }


}
