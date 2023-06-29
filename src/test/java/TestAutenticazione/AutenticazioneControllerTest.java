package TestAutenticazione;

import it.unisa.c07.biblionet.config.JwtGeneratorInterface;
import it.unisa.c07.biblionet.gestioneautenticazione.AutenticazioneService;
import it.unisa.c07.biblionet.gestioneautenticazione.controller.AutenticazioneController;
import it.unisa.c07.biblionet.gestioneautenticazione.repository.UtenteRegistrato;
import it.unisa.c07.biblionet.utils.BiblionetResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AutenticazioneControllerTest {
    @Mock
    private AutenticazioneService autenticazioneService;

    @Mock
    private JwtGeneratorInterface jwtGenerator;

    @InjectMocks
    private AutenticazioneController autenticazioneController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void login_shouldReturnBiblionetResponseWithTokenWhenLoginSuccessful() {
        // Preparazione dei dati di test
        String email = "example@example.com";
        String password = "password";
        String expectedToken = "jwtToken";
        UtenteRegistrato utente = new UtenteRegistrato();
        utente.setEmail(email);
        utente.setPassword(password);

        when(autenticazioneService.login(email, password)).thenReturn(utente);
        when(jwtGenerator.generateToken(utente)).thenReturn(expectedToken);

        // Esecuzione del metodo da testare
        BiblionetResponse response = autenticazioneController.login(email, password);

        // Verifica del risultato
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals(expectedToken, response.getMessage());

        // Verifica delle interazioni con le dipendenze
        verify(autenticazioneService, times(1)).login(email, password);
        verify(jwtGenerator, times(1)).generateToken(utente);
    }

    @Test
    void login_shouldReturnBiblionetResponseWithErrorWhenLoginFailed() {
        // Preparazione dei dati di test
        String email = "example@example.com";
        String password = "password";

        when(autenticazioneService.login(email, password)).thenReturn(null);

        // Esecuzione del metodo da testare
        BiblionetResponse response = autenticazioneController.login(email, password);

        // Verifica del risultato
        assertNotNull(response);
        assertFalse(response.isStatusOk());
        assertEquals("Login fallito.", response.getPayload().);

        // Verifica delle interazioni con le dipendenze
        verify(autenticazioneService, times(1)).login(email, password);
        verify(jwtGenerator, never()).generateToken(any(UtenteRegistrato.class));
    }
}