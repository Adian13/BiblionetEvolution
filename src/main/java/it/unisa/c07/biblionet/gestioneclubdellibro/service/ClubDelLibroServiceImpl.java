package it.unisa.c07.biblionet.gestioneclubdellibro.service;

import it.unisa.c07.biblionet.common.UtenteRegistrato;
import it.unisa.c07.biblionet.gestioneclubdellibro.ClubDelLibroService;
import it.unisa.c07.biblionet.gestioneclubdellibro.EspertoDTO;
import it.unisa.c07.biblionet.gestioneclubdellibro.LettoreDTO;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    @Autowired
    private final ClubDelLibroDAO clubDAO;
    @Autowired
    private final UtenteRegistratoDAO utenteRegistratoDAO;
    @Autowired
    private final EspertoDAO espertoDAO;
    @Autowired
    private final LettoreDAO lettoreDAO;


    @Override
    public final List<ClubDelLibro> findClubsByEsperto(Esperto esperto) {
        return esperto.getClubs();
    }

    @Override
    public final List<ClubDelLibro> findClubsByLettore(Lettore lettore) {
        return lettore.getClubs();
    }


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
     * Implementa la funzionalità che permette
     * a un lettore di effettuare
     * l'iscrizione a un club del libro.
     *
     * @param club    Il club al quale iscriversi
     * @param lettore Il lettore che si iscrive
     * @return true se è andato a buon fine, false altrimenti
     */
    @Override
    public Boolean partecipaClub(final ClubDelLibro club,
                                 final Lettore lettore) {
        List<ClubDelLibro> listaClubs = lettore.getClubs();
        if (listaClubs == null) {
            listaClubs = new ArrayList<>();
        }
        listaClubs.add(club);
        lettore.setClubs(listaClubs);
        this.aggiornaLettore(lettore);
        return true;
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

      /**
     * Implementa la funzionalità di salvataggio delle modifiche
     * all'account esperto.
     *
     * @param utente L'esperto da aggiornare
     * @return l'esperto aggiornato
     */

    public Esperto aggiornaEsperto(final Esperto utente) {
        return espertoDAO.save(utente);
    }



    /**
     * Implementa la funzionalità di trovare un lettore.
     *
     * @param email La mail dell lettore
     * @return Il lettore se c'è, altrimenti null
     */
    @Override
    public final Lettore findLettoreByEmail(final String email) {
        return lettoreDAO.findByEmail(email, "Lettore");
    }


    @Override
    public Lettore aggiornaLettore(final Lettore utente) {
        return lettoreDAO.save(utente);
    }

    @Override
    public List<Esperto> findAll() {
        return espertoDAO.findAll();
    }

    @Override
    public List<Esperto> findByNome(String nome) {
        return espertoDAO.findByNomeLike(nome);
    }

    @Override
    public UtenteRegistrato findByEmailAndPassword(String email, byte[] password) {
        return utenteRegistratoDAO.findByEmailAndPassword(email, password);
    }


    @Override
    public Lettore getByEmail(String email) {
        return null;
    }

    @Override
    public UtenteRegistrato creaLettoreDaModel(LettoreDTO form) {
        return lettoreDAO.save(new Lettore(form));
    }

    @Override
    public UtenteRegistrato aggiornaLettoreDaModel(LettoreDTO form) {
        return creaLettoreDaModel(form);
    }

}
