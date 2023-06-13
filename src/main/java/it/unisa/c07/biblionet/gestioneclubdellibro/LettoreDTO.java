package it.unisa.c07.biblionet.gestioneclubdellibro;

import it.unisa.c07.biblionet.common.UtenteRegistratoDTO;
import it.unisa.c07.biblionet.utils.RispettoVincoli;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.util.Set;
import javax.validation.constraints.Pattern;

@Getter
@Data
@SuperBuilder
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class LettoreDTO extends UtenteRegistratoDTO {

    @NonNull
    private String nome;
    @NonNull
    private String cognome;
    @NonNull
    private String username;

    public LettoreDTO(@NonNull String email, @NonNull String password, @NonNull String provincia, @NonNull String citta, @NonNull @Pattern(regexp = RispettoVincoli.ADDRESS_REGEX) String via, @NonNull @Pattern(regexp = RispettoVincoli.PHONE_REGEX) String recapitoTelefonico,  String nome, String cognome, String username) {
        super(email, password, provincia, citta, via, recapitoTelefonico);
        this.cognome = cognome;
        this.nome = nome;
        this.username = username;
    }
    public LettoreDTO(@NonNull String email, @NonNull byte[] password, @NonNull String provincia, @NonNull String citta, @NonNull @Pattern(regexp = RispettoVincoli.ADDRESS_REGEX) String via, @NonNull @Pattern(regexp = RispettoVincoli.PHONE_REGEX) String recapitoTelefonico,  String nome, String cognome, String username) {
        super(email, password, provincia, citta, via, recapitoTelefonico);
        this.cognome = cognome;
        this.nome = nome;
        this.username = username;
    }
}
