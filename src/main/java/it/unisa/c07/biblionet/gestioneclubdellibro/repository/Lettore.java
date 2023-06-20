package it.unisa.c07.biblionet.gestioneclubdellibro.repository;

import it.unisa.c07.biblionet.common.UtenteRegistrato;
import it.unisa.c07.biblionet.gestioneclubdellibro.LettoreDTO;
import it.unisa.c07.biblionet.utils.BiblionetConstraints;
import lombok.*;
import lombok.experimental.SuperBuilder;
import javax.persistence.*;
import javax.validation.constraints.Pattern;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import java.util.List;
import java.util.Set;

/**
 * Questa classe rappresenta un Lettore.
 * Un Lettore può essere interessato a più generi,
 * può partecipare a più eventi,
 * e far parte di più club.
 */
@Entity
@SuperBuilder
@Data
@Getter
@Setter
@NoArgsConstructor(force = true)
public class Lettore extends UtenteRegistrato {

    /**
     * Rappresenta un lettore sulla piattaforma.
     */
    @NonNull
    @Column(nullable = false, length = BiblionetConstraints.LENGTH_30)
    private String username;

    /**
     * Rappresenta il nome del lettore.
     */
    @NonNull
    @Column(nullable = false, length = BiblionetConstraints.LENGTH_30)
    @Pattern(regexp = BiblionetConstraints.NAME_REGEX)
    private String nome;

    /**
     * Rappresenta il cognome di un lettore.
     */
    @NonNull
    @Column(nullable = false, length = BiblionetConstraints.LENGTH_30)
    @Pattern(regexp = BiblionetConstraints.NAME_REGEX)
    private String cognome;

    /**
     * Rappresenta i generi che interessano a un lettore.
     */
    @ElementCollection
    private Set<String> nomeGeneri;

    /**
     * Rappresenta i clubs a cui il lettore appartiene.
     */
    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    @ToString.Exclude
    private List<ClubDelLibro> clubs;

    /**
     * Rappresenta gli eventi a cui prende parte.
     */
    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    @ToString.Exclude
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Evento> eventi;


    /**
     *
     * @param email la email del lettore.
     * @param password la password del lettore.
     * @param provincia la provincia dove vive
     * @param citta la città del lettore.
     * @param via la via dove vive.
     * @param recapitoTelefonico il recapito del lettore.
     * @param username l'username del lettore.
     * @param nome il nome del lettore.
     * @param cognome il cognome del lettore.
     */
    public Lettore(final String email, final String password,
                   final String provincia, final String citta,
                   final String via, final String recapitoTelefonico,
                   final String username, final String nome,
                   final String cognome) {
        super(email, password, provincia, citta, via, recapitoTelefonico, "Lettore");
        this.username = username;
        this.nome = nome;
        this.cognome = cognome;
    }

    public Lettore(LettoreDTO dto){
        super(dto.getEmail(), dto.getPassword(), dto.getProvincia(), dto.getCitta(), dto.getVia(), dto.getRecapitoTelefonico(), "Esperto");
        this.username = dto.getUsername();
        this.nome = dto.getNome();
        this.cognome = dto.getCognome();
    }

    @Override
    public boolean equals(final Object obj) throws NullPointerException {
        if (obj.getClass().equals(this.getClass())) {
            Lettore lettore = (Lettore) obj;
            return (this.getEmail().equals(lettore.getEmail())
                    && this.getUsername().equals(lettore.getUsername())
                    && this.getCognome().equals(lettore.getCognome())
                    && this.getNome().equals(lettore.getNome()));
        }
        return false;
    }



    public Set<String> getGeneri() {
        return getNomeGeneri();
    }
}
