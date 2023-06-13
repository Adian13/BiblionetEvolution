package it.unisa.c07.biblionet.gestioneprestitilibro.repository;


import it.unisa.c07.biblionet.gestioneprestitilibro.repository.Possesso;
import it.unisa.c07.biblionet.gestioneprestitilibro.repository.TicketPrestito;
import it.unisa.c07.biblionet.utils.Length;
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
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
public class LibroBiblioteca {

    /**
     * Rappresenta l'ID autogenerato di un libro.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int idLibro;

    /**
     * Rappresenta il titolo di un libro.
     */
    @NonNull
    @Column(length = Length.LENGTH_90)
    private String titolo;

    /**
     * Rappresenta l'autore di un libro.
     */
    @NonNull
    @Column(length = Length.LENGTH_60)
    private String autore;

    /**
     * Rappresenta il codice ISBN di un libro se presente.
     */
    @Column(unique = true, length = Length.LENGTH_13)
    @NonNull
    private String isbn;

    /**
     * Rappresenta l'anno di pubblicazione di un libro.
     */
    @Column(nullable = false)
    @NonNull
    private LocalDateTime annoDiPubblicazione;

    /**
     * Rappresenta la descrizione di un libro.
     */
    @Column(nullable = false, length = Length.LENGTH_144)
    @NonNull
    private String descrizione;

    /**
     * Rappresenta la casa editrice di un libro.
     */
    @Column(nullable = false, length = Length.LENGTH_30)
    @NonNull
    private String casaEditrice;

    /**
     * Rappresenta l'immagine di copertina del libro.
     */
    @Lob
    @ToString.Exclude
    private String immagineLibro;

    /**
     * Rappresenta i tickets di cui fa parte il libro.
     */
    @OneToMany(mappedBy = "libro")
    @ToString.Exclude
    private List<TicketPrestito> tickets;

    /**
     * Rappresenta i generi di un libro.
     */
    @ElementCollection
    @ToString.Exclude
    private Set<String> generi;


    /**
     * Rappresenta la relazione di possesso con una biblioteca.
     */
    @OneToMany(mappedBy = "possessoID.libroID")
    @ToString.Exclude
    private List<Possesso> possessi;
}
