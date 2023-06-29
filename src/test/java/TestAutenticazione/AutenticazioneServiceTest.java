package TestAutenticazione;

import it.unisa.c07.biblionet.gestioneautenticazione.repository.UtenteRegistrato;
import it.unisa.c07.biblionet.gestioneautenticazione.repository.UtenteRegistratoDAO;
import it.unisa.c07.biblionet.gestioneautenticazione.service.AutenticazioneServiceImpl;
import it.unisa.c07.biblionet.utils.BiblionetConstraints;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AutenticazioneServiceTest {

    private AutenticazioneServiceImpl autenticazioneService;

    @Mock
    private UtenteRegistratoDAO utenteRegistratoDAO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        autenticazioneService = new AutenticazioneServiceImpl(utenteRegistratoDAO);
    }

    @Test
    void login_shouldReturnUtenteRegistrato() {
        // Preparazione dei dati di test
        String email = "example@example.com";
        String password = "password";
        UtenteRegistrato utenteRegistrato = new UtenteRegistrato();
        // Supponiamo che il metodo findUtenteRegistratoByEmailAndPassword ritorni l'utenteRegistrato corretto
        when(utenteRegistratoDAO.findUtenteRegistratoByEmailAndPassword(email, BiblionetConstraints.trasformaPassword(password)))
                .thenReturn(utenteRegistrato);

        // Esecuzione del metodo da testare
        UtenteRegistrato result = autenticazioneService.login(email, password);

        // Verifica del risultato
        assertEquals(utenteRegistrato, result);

        // Verifica che il metodo findUtenteRegistratoByEmailAndPassword sia stato chiamato correttamente
        verify(utenteRegistratoDAO).findUtenteRegistratoByEmailAndPassword(email, BiblionetConstraints.trasformaPassword(password));
    }
}