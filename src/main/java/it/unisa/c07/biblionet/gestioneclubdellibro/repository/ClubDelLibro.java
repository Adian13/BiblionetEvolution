package it.unisa.c07.biblionet.gestioneclubdellibro.repository;


import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Evento;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Esperto;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Lettore;
import it.unisa.c07.biblionet.utils.Length;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.NonNull;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import javax.persistence.*;
import java.util.List;
import java.util.Set;

/**
 * Questa classe rappresenta un Club del Libro.
 * Un club del libro ha sede nella biblioteca dove il suo esperto,
 * proprietario del club, lavora.
 * Un club possiede uno o più generi di riferimento.
 * Un club è composto da uno o più lettori.
 * In un club possono essere organizzati degli eventi.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
public class ClubDelLibro {

    /**
     * Rappresenta l'ID autogenerato di un club.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int idClub;

    /**
     * Rappresenta il nome di un club.
     */
    @NonNull
    @Column(nullable = false, length = Length.LENGTH_30)
    private String nome;

    /**
     * Rappresenta la descrizione di un club.
     */
    @NonNull
    @Column(nullable = false, length = Length.LENGTH_255)
    private String descrizione;

    /**
     *  Rappresenta l'immagine di copertina di un club.
     *  L'annotazione serve ad Hibernate, poichè Blob è una interfaccia,
     *  e di conseguenza non saprebbe come mapparla all'interno di un DB,
     *  tramite l'utilizzo di questa annotazione,
     *  l'immagine viene salvata come un Large OBject.
     *
     */
    @Lob
    @ToString.Exclude
    private String immagineCopertina;

    /**
     * Rappresenta l'esperto proprietario di un club.
     */
    @NonNull
    @ManyToOne
    @JoinColumn(nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Esperto esperto;

    /**
     * Rappresenta i lettori di un club.
     */
    @ManyToMany(mappedBy = "clubs")
    @ToString.Exclude
    private List<Lettore> lettori;

    /**
     * Rappresenta i generi di un club.
     */
    @ElementCollection
    private Set<String> generi;

    /**
     * Rappresenta gli eventi di un club.
     */
    @OneToMany(mappedBy = "club")
    @ToString.Exclude
    private List<Evento> eventi;


}
