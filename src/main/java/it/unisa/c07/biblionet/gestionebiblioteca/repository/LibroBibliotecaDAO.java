package it.unisa.c07.biblionet.gestionebiblioteca.repository;

import it.unisa.c07.biblionet.common.ILibroIdAndName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Questa classe rappresenta il DAO di un Libro.
 */
@Repository
public interface LibroBibliotecaDAO extends JpaRepository<LibroBiblioteca, Integer> {

    /**
     * Query custom per il recupero dal DB di una lista
     * di libri che contengono la stringa passata
     * come parametro.
     * @param titolo La stringa che deve essere
     *               contenuta
     * @return La lista dei libri che contengono
     *          la stringa
     */
    @Query("SELECT l FROM LibroBiblioteca l "
            + "WHERE UPPER(l.titolo) LIKE UPPER(concat('%', ?1,'%'))")
    List<LibroBiblioteca> findByTitoloLike(String titolo);

    /**
     * Query custom per il recupero dal DB di una lista
     * di libri che contengono la stringa passata
     * come parametro.
     * @param titolo La stringa che deve essere
     *               contenuta
     * @return La lista dei libri che contengono
     *          la stringa
     */
    @Query("SELECT l.idLibro AS idLibro, l.titolo AS titolo "
            + "FROM LibroBiblioteca l "
            + "WHERE UPPER(l.titolo) LIKE UPPER(concat('%', ?1,'%'))")
    List<ILibroIdAndName> findByTitoloContains(String titolo);

    /**
     * Query custom per il recupero dal DB di un libro
     * @param isbn del libro
     * @return il libro associato all'isbn
     */
    @Query("SELECT l FROM LibroBiblioteca l "
            + "WHERE l.isbn = ?1")
    LibroBiblioteca findByIsbn(String isbn);
}
