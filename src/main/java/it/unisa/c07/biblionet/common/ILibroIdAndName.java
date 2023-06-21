package it.unisa.c07.biblionet.common;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Implementa la custom query result per la ricerca dei Libri per nome.
 */
@Repository
public interface ILibroIdAndName extends JpaRepository<Libro,Long> {
    /**
     * Implementa la funzionalità di ottenimento dell'id di un libro.
     * @return L'id del libro
     */
    Integer getId();

    /**
     * Implementa la funzionalità di ottenimento del titolo di un libro.
     * @return il titolo di un libro.
     */
    String getTitolo();
}
