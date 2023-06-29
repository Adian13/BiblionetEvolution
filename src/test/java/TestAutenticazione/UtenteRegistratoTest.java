package TestAutenticazione;
import it.unisa.c07.biblionet.gestioneautenticazione.repository.UtenteRegistrato;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UtenteRegistratoTest {

    @Test
    void setPassword_shouldSetHashedPassword() {
        // Preparazione dei dati di test
        String email = "example@example.com";
        String password = "password";
        String provincia = "Napoli";
        String citta = "Napoli";
        String via = "Via Example";
        String recapitoTelefonico = "1234567890";
        String tipo = "Utente";

        UtenteRegistrato utente = new UtenteRegistrato(email, password, provincia, citta, via, recapitoTelefonico, tipo);

        // Esecuzione del metodo da testare
        utente.setPassword(password);

        // Verifica del risultato
        assertNotNull(utente.getPassword());
        assertNotEquals(password, utente.getPassword());

        // Verifica che la password sia stata effettivamente hashata
        byte[] hashedPassword = utente.getPassword();
        // Verifica che la password hashata non sia vuota
        assertNotNull(hashedPassword);
        // Verifica che la password hashata sia diversa dalla password in chiaro
        assertNotEquals(password, new String(hashedPassword));
    }

    @Test
    void setHashedPassword_shouldSetPassword() {
        // Preparazione dei dati di test
        String email = "example@example.com";
        String password = "password";
        String provincia = "Napoli";
        String citta = "Napoli";
        String via = "Via Example";
        String recapitoTelefonico = "1234567890";
        String tipo = "Utente";

        UtenteRegistrato utente = new UtenteRegistrato(email, password, provincia, citta, via, recapitoTelefonico, tipo);

        // Esecuzione del metodo da testare
        byte[] hashedPassword = "hashedPassword".getBytes();
        utente.setHashedPassword(hashedPassword);

        // Verifica del risultato
        assertArrayEquals(hashedPassword, utente.getPassword());
    }
}