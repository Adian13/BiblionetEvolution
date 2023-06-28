package it.unisa.c07.biblionet.gestioneclubdellibro;
import it.unisa.c07.biblionet.common.UtenteRegistratoDTO;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Esperto;
import it.unisa.c07.biblionet.utils.BiblionetConstraints;
import lombok.*;
import lombok.experimental.SuperBuilder;
import javax.validation.constraints.Pattern;
import java.util.Set;

@Getter
@Data
@SuperBuilder
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class EspertoDTO extends UtenteRegistratoDTO {
    /**
     * Rappresenta l'ID di un utente registrato.
     */


    private Set<String> generi;

    @NonNull
    private String nome;
    @NonNull
    private String cognome;
    @NonNull
    private String username;

    public EspertoDTO(@NonNull String email, @NonNull String password, @NonNull String provincia, @NonNull String citta, @NonNull @Pattern(regexp = BiblionetConstraints.ADDRESS_REGEX) String via, @NonNull @Pattern(regexp = BiblionetConstraints.PHONE_REGEX) String recapitoTelefonico, String nome, String cognome, String username, Set<String> generi) {
        super(email, password, provincia, citta, via, recapitoTelefonico);
        this.cognome = cognome;
        this.nome = nome;
        this.username = username;
        this.generi = generi;
    }
    public EspertoDTO(@NonNull String email, @NonNull byte[] password, @NonNull String provincia, @NonNull String citta, @NonNull @Pattern(regexp = BiblionetConstraints.ADDRESS_REGEX) String via, @NonNull @Pattern(regexp = BiblionetConstraints.PHONE_REGEX) String recapitoTelefonico, String nome, String cognome, String username, Set<String> generi) {
        super(email, password, provincia, citta, via, recapitoTelefonico);
        this.cognome = cognome;
        this.nome = nome;
        this.username = username;
        this.generi = generi;
    }

    public EspertoDTO(Esperto esperto) {
        super(esperto.getEmail(), esperto.getPassword(), esperto.getProvincia(), esperto.getCitta(), esperto.getVia(), esperto.getRecapitoTelefonico());
        this.cognome = esperto.getCognome();
        this.nome = esperto.getNome();
        this.username = esperto.getUsername();
        this.generi = esperto.getGeneri();
    }
}