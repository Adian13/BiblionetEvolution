package TestAutenticazione;

import io.jsonwebtoken.Claims;
import it.unisa.c07.biblionet.utils.Utils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UtilsTest {

    @Test
    void getClaimsFromTokenWithoutKey_shouldReturnClaims() {
        // Preparazione dei dati di test
        String token = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0Iiwicm9sZSI6IkFkbWluIiwiaWF0IjoxNjIzMjI2MzUwLCJleHAiOjE2MjMyMzAwMDB9.-9j9v5Eop6AJQ8Wb-3J1Df5Z5s-N7scgA1XkOs_GDKM";

        // Esecuzione del metodo da testare
        Claims result = Utils.getClaimsFromTokenWithoutKey(token);

        // Verifica del risultato
        assertNotNull(result);
    }

    @Test
    void isUtenteBiblioteca_shouldReturnTrueForBibliotecaRole() {
        // Preparazione dei dati di test
        String token = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0Iiwicm9sZSI6IkJpYmxpZWN0ZWkiLCJpYXQiOjE2MjMyMjYzNTAsImV4cCI6MTYyMzIzMDAwMH0.bU4kPBvTttCLkkYB2DoDeIGt9Xs7tTRFgWsLkdTTah8";

        // Esecuzione del metodo da testare
        boolean result = Utils.isUtenteBiblioteca(token);

        // Verifica del risultato
        assertTrue(result);
    }

    @Test
    void isUtenteBiblioteca_shouldReturnFalseForNonBibliotecaRole() {
        // Preparazione dei dati di test
        String token = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0Iiwicm9sZSI6IkxlZXRybyIsImlhdCI6MTYyMzIyNjM1MCwiZXhwIjoxNjIzMjMwMDAwfQ.-TZl2SxZyDdCGm_GsZi9kExNWTgvgvDJDEO8r-1vDXI";

        // Esecuzione del metodo da testare
        boolean result = Utils.isUtenteBiblioteca(token);

        // Verifica del risultato
        assertFalse(result);
    }

    @Test
    void isUtenteLettore_shouldReturnTrueForLettoreRole() {
        // Preparazione dei dati di test
        String token = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0Iiwicm9sZSI6IkxldHRvcmUiLCJpYXQiOjE2MjMyMjYzNTAsImV4cCI6MTYyMzIzMDAwMH0.-TZl2SxZyDdCGm_GsZi9kExNWTgvgvDJDEO8r-1vDXI";

        // Esecuzione del metodo da testare
        boolean result = Utils.isUtenteLettore(token);

        // Verifica del risultato
        assertTrue(result);
    }

    @Test
    void isUtenteLettore_shouldReturnFalseForNonLettoreRole() {
        // Preparazione dei dati di test
        String token = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0Iiwicm9sZSI6IkVzcGVzdG8iLCJpYXQiOjE2MjMyMjYzNTAsImV4cCI6MTYyMzIzMDAwMH0.aC5rVdu5aL0CDk-D4jg89PfLoV_9GziYmJjsCRpCvv0";

        // Esecuzione del metodo da testare
        boolean result = Utils.isUtenteLettore(token);

        // Verifica del risultato
        assertFalse(result);
    }

    @Test
    void isUtenteEsperto_shouldReturnTrueForEspertoRole() {
        // Preparazione dei dati di test
        String token = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0Iiwicm9sZSI6IkVzcGVzdG8iLCJpYXQiOjE2MjMyMjYzNTAsImV4cCI6MTYyMzIzMDAwMH0.aC5rVdu5aL0CDk-D4jg89PfLoV_9GziYmJjsCRpCvv0";

        // Esecuzione del metodo da testare
        boolean result = Utils.isUtenteEsperto(token);

        // Verifica del risultato
        assertTrue(result);
    }

    @Test
    void isUtenteEsperto_shouldReturnFalseForNonEspertoRole() {
        // Preparazione dei dati di test
        String token = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0Iiwicm9sZSI6IkxldHRvcmUiLCJpYXQiOjE2MjMyMjYzNTAsImV4cCI6MTYyMzIzMDAwMH0.-TZl2SxZyDdCGm_GsZi9kExNWTgvgvDJDEO8r-1vDXI";

        // Esecuzione del metodo da testare
        boolean result = Utils.isUtenteEsperto(token);

        // Verifica del risultato
        assertFalse(result);
    }

    @Test
    void getSubjectFromToken_shouldReturnSubject() {
        // Preparazione dei dati di test
        String token = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0Iiwicm9sZSI6IkFkbWluIiwiaWF0IjoxNjIzMjI2MzUwLCJleHAiOjE2MjMyMzAwMDB9.-9j9v5Eop6AJQ8Wb-3J1Df5Z5s-N7scgA1XkOs_GDKM";

        // Esecuzione del metodo da testare
        String result = Utils.getSubjectFromToken(token);

        // Verifica del risultato
        assertEquals("test", result);
    }
}