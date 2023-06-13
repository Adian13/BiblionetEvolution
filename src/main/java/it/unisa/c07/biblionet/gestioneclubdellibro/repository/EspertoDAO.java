package it.unisa.c07.biblionet.gestioneclubdellibro.repository;

import it.unisa.c07.biblionet.common.UtenteRegistrato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Questa classe rappresenta il DAO di un Esperto.
 */
@Repository
public interface EspertoDAO extends JpaRepository<UtenteRegistrato, String> {

    /**
     * Implementa la funzionalità di ricerca di un utente Esperto nel DB.
     * @param email dell'utente da cercare.
     * @param password dell'utente da cercare.
     * @return dell'utente trovato.
     */
    Esperto findByEmailAndPassword(String email, byte[] password);

    /**
     * Implementa la funzionalità di ricerca di tutti gli Esperti nel DB.
     * @return dell'utente trovato.
     */
    @Query("SELECT e FROM Esperto e")
    List<Esperto> findAllEsperti();

    /**
     * Query custom che recupera dal DB una lista
     * di esperti che contengono il genere passato.
     * @param nome Il nome del genere
     * @return Esperti trovati
     */
    @Query("SELECT e FROM Esperto e "
            +  "WHERE UPPER(CONCAT(e.nome, ' ', e.cognome)) "
            + "LIKE UPPER(concat('%', ?1,'%'))")
    List<Esperto> findByNomeLike(String nome);

    @Query("SELECT e FROM Esperto e WHERE e.email=?1")
    Esperto findByID(String email);

    @Query("SELECT e FROM Esperto e WHERE e.email=?1 and e.tipo=?2")
    Esperto findEspertoByEmail(String email, String tipo);


}
