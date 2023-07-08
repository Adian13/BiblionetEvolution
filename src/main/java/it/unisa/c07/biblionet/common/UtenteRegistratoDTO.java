package it.unisa.c07.biblionet.common;

import it.unisa.c07.biblionet.utils.BiblionetConstraints;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Pattern;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Getter
@Data
@SuperBuilder
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class UtenteRegistratoDTO {

    /**
     * Rappresenta l'ID di un utente registrato.
     */
    @NonNull
    private String email;


    /**
     * Rappresenta la password di un utente registrato.
     */
    @NonNull
    private byte[] password;

    /**
     * Rappresente la provincia dove vive l'utente registrato.
     */
    @NonNull
    private String provincia;

    /**
     * Rappresenta la città dove vive l'utente registrato.
     */
    @NonNull
    private String citta;

    /**
     * Rappresenta la via dove vive l'utente registrato.
     */
    @NonNull
    @Pattern(regexp = BiblionetConstraints.ADDRESS_REGEX)
    private String via;

    /**
     * Rappresenta il recapito telefonico dell'utente registrato.
     */
    @NonNull
    @Pattern(regexp = BiblionetConstraints.PHONE_REGEX)
    private String recapitoTelefonico;

    protected UtenteRegistratoDTO(String email,
                                String password,
                                String provincia,
                                String citta,
                                String via,
                                String recapitoTelefonico) {
            this.email = email;
            this.provincia = provincia;
            this.via = via;
            this.recapitoTelefonico = recapitoTelefonico;
            this.citta = citta;
            this.password = BiblionetConstraints.trasformaPassword(password);
    }

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
}