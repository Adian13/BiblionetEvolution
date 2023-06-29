package TestAutenticazione;

import it.unisa.c07.biblionet.utils.BiblionetConstraints;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BiblionetConstraintsTest {

    @Test
    void passwordRispettaVincoli_shouldReturnTrueForValidPassword() {
        // Preparazione dei dati di test
        String password = "password";
        byte[] hashedPassword = BiblionetConstraints.trasformaPassword(password);

        // Esecuzione del metodo da testare
        boolean result = BiblionetConstraints.passwordRispettaVincoli(hashedPassword, password);

        // Verifica del risultato
        assertTrue(result);
    }

    @Test
    void passwordRispettaVincoli_shouldReturnFalseForInvalidPassword() {
        // Preparazione dei dati di test
        String password = "123456";
        byte[] hashedPassword = BiblionetConstraints.trasformaPassword(password);

        // Esecuzione del metodo da testare
        boolean result = BiblionetConstraints.passwordRispettaVincoli(hashedPassword, password);

        // Verifica del risultato
        assertFalse(result);
    }

    @Test
    void confrontoPassword_shouldReturnValidPasswordForMatchingPasswords() {
        // Preparazione dei dati di test
        String nuovaPassword = "newpassword";
        String confermaPassword = "newpassword";

        // Esecuzione del metodo da testare
        String result = BiblionetConstraints.confrontoPassword(nuovaPassword, confermaPassword);

        // Verifica del risultato
        assertEquals(nuovaPassword, result);
    }

    @Test
    void confrontoPassword_shouldReturnEmptyStringForNonMatchingPasswords() {
        // Preparazione dei dati di test
        String nuovaPassword = "newpassword";
        String confermaPassword = "confirmpassword";

        // Esecuzione del metodo da testare
        String result = BiblionetConstraints.confrontoPassword(nuovaPassword, confermaPassword);

        // Verifica del risultato
        assertEquals("", result);
    }
}