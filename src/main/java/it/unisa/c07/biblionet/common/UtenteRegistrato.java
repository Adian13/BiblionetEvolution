package it.unisa.c07.biblionet.common;

import it.unisa.c07.biblionet.utils.BiblionetConstraints;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.Column;
import javax.persistence.InheritanceType;
import javax.validation.constraints.Pattern;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Questa classe rappresenta un utente registrato alla piattaforma.
 */
@Entity
@SuperBuilder
@Data
@NoArgsConstructor(force = true)
@EqualsAndHashCode
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class UtenteRegistrato {

    /**
     * Rappresenta l'ID di un utente registrato.
     */
    @Id
    @Column(nullable = false, length = BiblionetConstraints.LENGTH_320)
    @NonNull
    private String email;

    /**
     * Rappresenta la password di un utente registrato.
     */
    @Column(nullable = false, length = BiblionetConstraints.LENGTH_32)
    @NonNull
    private byte[] password;

    /**
     * Rappresente la provincia dove vive l'utente registrato.
     */
    @Column(nullable = false, length = BiblionetConstraints.LENGTH_30)
    @NonNull
    private String provincia;

    /**
     * Rappresenta la città dove vive l'utente registrato.
     */
    @Column(nullable = false, length = BiblionetConstraints.LENGTH_30)
    @NonNull
    private String citta;

    /**
     * Rappresenta la via dove vive l'utente registrato.
     */
    @Column(nullable = false, length = BiblionetConstraints.LENGTH_30)
    @NonNull
    @Pattern(regexp = BiblionetConstraints.ADDRESS_REGEX)
    private String via;

    /**
     * Rappresenta il recapito telefonico dell'utente registrato.
     */
    @Column(nullable = false, length = BiblionetConstraints.LENGTH_10)
    @NonNull
    @Pattern(regexp = BiblionetConstraints.PHONE_REGEX)
    private String recapitoTelefonico;


    /**
     * Rappresenta il tipo di utente.
     * Utile per essere chiamato sui figli, nella entity UtenteRegistrato
     * non ha senso di essere chiamato.
     */
    //@Transient
    private String tipo;

    /**
     *
     * @param email la mail dell'utente registrato.
     * @param password la password dell'utente registrato.
     * @param provincia la provincia dove vive l'utente.
     * @param citta la città dove vive l'utente.
     * @param via la via dove vive l'utente.
     * @param recapitoTelefonico il recapito telefonico dell'utente.
     */
    protected UtenteRegistrato(final String email, final String password,
                            final String provincia, final String citta,
                            final String via, final String recapitoTelefonico, final String tipo) {

        this.email = email;
        this.provincia = provincia;
        this.citta = citta;
        this.via = via;
        this.recapitoTelefonico = recapitoTelefonico;
        this.tipo = tipo;
        this.password = BiblionetConstraints.trasformaPassword(password);
    }
    protected UtenteRegistrato(final String email, final byte[] password,
                               final String provincia, final String citta,
                               final String via, final String recapitoTelefonico, final String tipo) {

        this.email = email;
        this.provincia = provincia;
        this.citta = citta;
        this.via = via;
        this.recapitoTelefonico = recapitoTelefonico;
        this.tipo = tipo;
        this.password = (password);
    }

    /**
     * Implementa il set della password effettuando l'hash.
     * @param password la password da settare
     */
    public void setPassword(final String password) {
        try {
            MessageDigest md;
            md = MessageDigest.getInstance("SHA-256");
            byte[] arr = md.digest(password.getBytes());
            this.password = arr;

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }


    /**
     * Permette l'inserimento di una password già hashata.
     * @param hashPassword la password
     */
    public void setHashedPassword(final byte[] hashPassword) {
        this.password = hashPassword;
    }



}
