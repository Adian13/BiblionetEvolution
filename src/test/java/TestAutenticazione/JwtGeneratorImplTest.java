package TestAutenticazione;

import it.unisa.c07.biblionet.config.JwtGeneratorImpl;
import it.unisa.c07.biblionet.gestioneautenticazione.repository.UtenteRegistrato;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class JwtGeneratorImplTest {
    @Mock
    private UtenteRegistrato user;

    @InjectMocks
    private JwtGeneratorImpl jwtGenerator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void generateToken_shouldReturnJwtTokenAndMessage() {
        // Preparazione dei dati di test
        String expectedToken = "jwtToken";
        String expectedMessage = "message";

        when(user.getEmail()).thenReturn("example@example.com");
        when(user.getTipo()).thenReturn("user");

        // Esecuzione del metodo da testare
        Map<String, String> result = jwtGenerator.generateToken(user);

        // Verifica del risultato
        assertEquals(expectedToken, result.get("token"));
        assertEquals(expectedMessage, result.get("message"));

        // Verifica delle interazioni con le dipendenze
        verify(user, times(1)).getEmail();
        verify(user, times(1)).getTipo();
    }
}