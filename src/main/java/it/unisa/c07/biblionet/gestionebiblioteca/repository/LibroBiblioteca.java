package it.unisa.c07.biblionet.gestionebiblioteca.repository;


import it.unisa.c07.biblionet.common.Libro;
import lombok.*;
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
@NoArgsConstructor
public class LibroBiblioteca extends Libro {

    /**
     * Rappresenta i tickets di cui fa parte il libro.
     */
    @OneToMany(mappedBy = "libro")
    @ToString.Exclude
    private List<TicketPrestito> tickets;


    /**
     * Rappresenta la relazione di possesso con una biblioteca.
     */
    @OneToMany(mappedBy = "possessoID.libroID")
    @ToString.Exclude
    private List<Possesso> possessi;

   /* public LibroBiblioteca(
            String titolo,
            String autore,
            String isbn,
            LocalDateTime annoDiPubblicazione,
            String descrizione,
            String casaEditrice,
            String immagineLibro,
            Set<String> generi,
            List<Possesso> possessi,
            List<TicketPrestito> tickets){
        super(titolo, autore, isbn, annoDiPubblicazione, descrizione, casaEditrice, immagineLibro, generi);
        this.possessi = possessi;
        this.tickets = tickets;
    }*/

}
