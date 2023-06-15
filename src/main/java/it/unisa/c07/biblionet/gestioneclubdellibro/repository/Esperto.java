package it.unisa.c07.biblionet.gestioneclubdellibro.repository;

import it.unisa.c07.biblionet.common.UtenteRegistrato;
import it.unisa.c07.biblionet.gestioneclubdellibro.EspertoDTO;
import it.unisa.c07.biblionet.utils.BiblionetConstraints;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.Set;

/**
 * Questa classe rappresenta un Esperto
 * Un esperto possiede un username ,un nome e un cognome.
 * Un esperto ha una biblioteca per cui lavora,
 * una lista di generi di cui è esperto,
 * e una lista di club che gestisce.
 */
@Entity
@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(force = true)
public class Esperto extends UtenteRegistrato {

    /**
     * Rappresenta l'username dell'esperto.
     */
    @NonNull
    @Column(nullable = false, length = BiblionetConstraints.LENGTH_30)
    private String username;

    /**
     * Rappresenta il nome dell'esperto.
     */
    @NonNull
    @Column(nullable = false, length = BiblionetConstraints.LENGTH_30)
    @Pattern(regexp = BiblionetConstraints.NAME_REGEX, message = "Il campo deve contenere esattamente 9 cifre.")
    private String nome;

    /**
     * Rappresenta il cognome dell'esperto.
     */
    @NonNull
    @Column(nullable = false, length = BiblionetConstraints.LENGTH_30)
    @Pattern(regexp = BiblionetConstraints.NAME_REGEX)
    private String cognome;

    /**
     * Rappresenta la bibloteca dove lavora l'esperto.
     */
    @ManyToOne
    @JoinColumn(nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UtenteRegistrato biblioteca;

    /**
     * Rappresenta la lista di generi di cui un esperto è esperto.
     */
    @ElementCollection
    private Set<String> nomeGeneri;

    /**
     * Rappresenta la lista di club gestiti dall'esperto.
     */
    @OneToMany
    @ToString.Exclude
    private List<ClubDelLibro> clubs;



    /**
     *
     * @param email È la mail dell'esperto.
     * @param password È la password di accesso dell'esperto.
     * @param provincia È la provincia in cui lavora l'esperto.
     * @param citta È la città in cui lavora l'esperto.
     * @param via È l'indirizzo in cui lavora l'esperto.
     * @param recapitoTelefonico È il numero di telefono dell'esperto.
     * @param username È l'username dell'esperto.
     * @param nome È il nome dell'esperto.
     * @param cognome È il cognome dell'esperto.
     * @param biblioteca È la biblioteca dove lavora l'esperto.
     */
    public Esperto(final String email, final String password,
                   final String provincia, final String citta,
                   final String via,
                   final String recapitoTelefonico, final String username,
                   final String nome, final String cognome,
                   final UtenteRegistrato biblioteca) {
        super(email, password, provincia, citta, via, recapitoTelefonico, "Esperto");
        this.username = username;
        this.nome = nome;
        this.cognome = cognome;
        this.biblioteca = biblioteca;
    }

    public Esperto(EspertoDTO dto, UtenteRegistrato biblioteca){
        super(dto.getEmail(), dto.getPassword(), dto.getProvincia(), dto.getCitta(), dto.getVia(), dto.getRecapitoTelefonico(), "Esperto");
        this.username = dto.getUsername();
        this.nome = dto.getNome();
        this.cognome = dto.getCognome();
        this.biblioteca = biblioteca;
        this.nomeGeneri = dto.getGeneri();
    }

    public Set<String> getGeneri() {
        return getNomeGeneri();
    }


}
