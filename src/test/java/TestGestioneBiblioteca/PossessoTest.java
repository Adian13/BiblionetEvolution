package TestGestioneBiblioteca;

import it.unisa.c07.biblionet.gestionebiblioteca.repository.Possesso;
import it.unisa.c07.biblionet.gestionebiblioteca.repository.PossessoId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PossessoTest {

    @Test
    public void testConstructorAndGetters() {
        PossessoId possessoId = new PossessoId("1L", 21);
        int numeroCopie = 5;

        Possesso possesso = new Possesso(possessoId, numeroCopie);

        Assertions.assertEquals(possessoId, possesso.getPossessoID());
        Assertions.assertEquals(numeroCopie, possesso.getNumeroCopie());
    }

    @Test
    public void testAllArgsConstructor() {
        PossessoId possessoId = new PossessoId("1L", 1);
        int numeroCopie = 5;

        Possesso possesso = new Possesso(possessoId, numeroCopie);

        Assertions.assertEquals(possessoId, possesso.getPossessoID());
        Assertions.assertEquals(numeroCopie, possesso.getNumeroCopie());
    }
}