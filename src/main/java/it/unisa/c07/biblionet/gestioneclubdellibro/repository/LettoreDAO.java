package it.unisa.c07.biblionet.gestioneclubdellibro.repository;

import it.unisa.c07.biblionet.common.UtenteRegistrato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Questa classe rappresenta il DAO di un Lettore.
 */
@Repository
public interface LettoreDAO extends JpaRepository<UtenteRegistrato, String> {
    /**
     * Implementa la funzionalità di ricerca di un utente Lettore nel DB.
     * @param email dell'utente da cercare.
     * @param password dell'utente da cercare.
     * @return dell'utente trovato.
     */
    Lettore findByEmailAndPassword(String email, byte[] password);

    /**
     * Query custom che recupera dal DB un lettore dato il
     * suo id.
     * @param email L'ID del lettore
     * @return Lettore trovato
     */
    @Query("SELECT l FROM Lettore l WHERE l.email=?1")
    Lettore findByID(String email);

    @Query("SELECT l FROM Lettore l WHERE l.email=?1 and l.tipo=?2")
    Lettore findLettoreByEmail(String email, String tipo);










}
