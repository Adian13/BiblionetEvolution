package it.unisa.c07.biblionet.gestioneclubdellibro.repository;

import it.unisa.c07.biblionet.common.Libro;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * Questa classe rappresenta un libro.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class LibroEvento extends Libro {


    /**
     * Rappresente gli eventi di cui un libro è parte.
     */
    @OneToMany(mappedBy = "libro")
    @ToString.Exclude
    private List<Evento> eventi;

  /*  public LibroEvento(
        String titolo,
        String autore,
        String isbn,
        LocalDateTime annoDiPubblicazione,
        String descrizione,
        String casaEditrice,
        String immagineLibro,
        Set<String> generi,
        List<Evento> eventi){
        super(titolo, autore, isbn, annoDiPubblicazione, descrizione, casaEditrice, immagineLibro, generi);
        this.eventi = eventi;
    }*/


}
