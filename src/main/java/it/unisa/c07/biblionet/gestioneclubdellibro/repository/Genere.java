package it.unisa.c07.biblionet.gestioneclubdellibro.repository;
import it.unisa.c07.biblionet.utils.BiblionetConstraints;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Questa classe rappresenta un Genere letterario.
 * Un genere possiede un nome ed una descrizione.
 * Un genere ha una lista di esperti che lo conoscono,
 * una lista di lettori a cui piace,
 * una lista di libri di quel genere
 * e una lista di club incentrati su di esso.
 */
@Entity
@Data
@EqualsAndHashCode
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
public class Genere {

    /**
     * Rappresenta il nome del genere,
     * nonchè il suo identificativo.
     */
    @Id
    @NonNull
    @Column(nullable = false, length = BiblionetConstraints.LENGTH_30)
    private String nome;

    /**
     * Rappresenta la descrizione del genere.
     */
    @NonNull
    @Column(nullable = false, length = BiblionetConstraints.LENGTH_255)
    @EqualsAndHashCode.Exclude
    private String descrizione;

    /**
     * Rappresenta la lista di lettori
     * a cui piace questo genere.
     */

}
