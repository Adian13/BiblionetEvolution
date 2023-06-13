package it.unisa.c07.biblionet.common;

/**
 * Implementa la custom query result per la ricerca dei Libri per nome.
 */
public interface ILibroIdAndName {
    /**
     * Implementa la funzionalità di ottenimento dell'id di un libro.
     * @return L'id del libro
     */
    Integer getIdLibro();

    /**
     * Implementa la funzionalità di ottenimento del titolo di un libro.
     * @return il titolo di un libro.
     */
    String getTitolo();
}
