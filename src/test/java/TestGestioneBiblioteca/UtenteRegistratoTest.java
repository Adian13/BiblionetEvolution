package TestGestioneBiblioteca;
import it.unisa.c07.biblionet.common.UtenteRegistrato;
import it.unisa.c07.biblionet.utils.BiblionetConstraints;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

public  class UtenteRegistratoTest {

    private UtenteRegistrato utenteRegistrato;
    private final String email = "test@example.com";
    private final String password = "password";
    private final String provincia = "Provincia";
    private final String citta = "Città";
    private final String via = "Via";
    private final String recapitoTelefonico = "1234567890";
    private final String tipo = "Tipo";

    @BeforeEach
    void setUp() {
        utenteRegistrato = new UtenteRegistrato(
                email, password, provincia, citta, via, recapitoTelefonico, tipo);
    }

    @Test
    void testGetters() {
        assertEquals(email, utenteRegistrato.getEmail());
        assertArrayEquals(hashPassword(password), utenteRegistrato.getPassword());
        assertEquals(provincia, utenteRegistrato.getProvincia());
        assertEquals(citta, utenteRegistrato.getCitta());
        assertEquals(via, utenteRegistrato.getVia());
        assertEquals(recapitoTelefonico, utenteRegistrato.getRecapitoTelefonico());
        assertEquals(tipo, utenteRegistrato.getTipo());
    }

    @Test
    void testEmailNonNull() {
        assertNotNull(utenteRegistrato.getEmail());
    }

    @Test
    void testEmailLength() {
        assertTrue(utenteRegistrato.getEmail().length() <= BiblionetConstraints.LENGTH_320);
    }

    @Test
    void testPasswordNonNull() {
        assertNotNull(utenteRegistrato.getPassword());
    }

    @Test
    void testPasswordHash() {
        assertArrayEquals(hashPassword(password), utenteRegistrato.getPassword());
    }

    @Test
    void testProvinciaNonNull() {
        assertNotNull(utenteRegistrato.getProvincia());
    }

    @Test
    void testProvinciaLength() {
        assertTrue(utenteRegistrato.getProvincia().length() <= BiblionetConstraints.LENGTH_30);
    }

    @Test
    void testCittaNonNull() {
        assertNotNull(utenteRegistrato.getCitta());
    }

    @Test
    void testCittaLength() {
        assertTrue(utenteRegistrato.getCitta().length() <= BiblionetConstraints.LENGTH_30);
    }

    @Test
    void testViaNonNull() {
        assertNotNull(utenteRegistrato.getVia());
    }

    @Test
    void testViaLength() {
        assertTrue(utenteRegistrato.getVia().length() <= BiblionetConstraints.LENGTH_30);
    }

    @Test
    void testRecapitoTelefonicoNonNull() {
        assertNotNull(utenteRegistrato.getRecapitoTelefonico());
    }

    @Test
    void testRecapitoTelefonicoPattern() {
        assertTrue(utenteRegistrato.getRecapitoTelefonico().matches(BiblionetConstraints.PHONE_REGEX));
    }

    @Test
    void testTipo() {
        assertEquals(tipo, utenteRegistrato.getTipo());
    }

    private byte[] hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return md.digest(password.getBytes());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

}