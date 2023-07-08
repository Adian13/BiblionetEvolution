package TestGestioneBiblioteca;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import it.unisa.c07.biblionet.common.Libro;
import it.unisa.c07.biblionet.common.UtenteRegistrato;
import it.unisa.c07.biblionet.common.UtenteRegistratoDTO;
import it.unisa.c07.biblionet.gestionebiblioteca.BibliotecaDTO;
import it.unisa.c07.biblionet.gestionebiblioteca.repository.*;
import it.unisa.c07.biblionet.gestionebiblioteca.bookapiadapter.BookApiAdapter;
import it.unisa.c07.biblionet.gestionebiblioteca.service.BibliotecaService;
import it.unisa.c07.biblionet.gestionebiblioteca.service.PrenotazioneLibriServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class PrenotazioneLibriServiceImplTest {

    @Mock
    private LibroBibliotecaDAO libroBibliotecaDAO;

    @Mock
    private BookApiAdapter bookApiAdapter;

    @Mock
    private PossessoDAO possessoDAO;

    @Mock
    private BibliotecaDAO bibliotecaDAO;

    @Mock
    private TicketPrestitoDAO ticketPrestitoDAO;

    @InjectMocks
    private PrenotazioneLibriServiceImpl prenotazioneLibriService;

    @Mock
    private BibliotecaService bibliotecaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void bibliotecaDaModel() {
        // Test logic here
    }

    @Test
    void loginBiblioteca() {
            // Create a test biblioteca
            String email = "test@biblioteca.com";
            String password = "password";
            Biblioteca biblioteca = bibliotecaService.save(new BibliotecaDTO());
            // Mock the behavior of the bibliotecaDAO
            when(bibliotecaDAO.findByEmailAndPassword(email, password.getBytes()));
            // Invoke the method being tested
            UtenteRegistrato result = prenotazioneLibriService.loginBiblioteca(email, password);

            // Verify the result
            Assertions.assertNotEquals(result,null);
            verify(bibliotecaDAO).findByEmailAndPassword(email, password.getBytes());
        }

    @Test
    void findBibliotecaByEmailAndPassword() {
        // Test logic here
    }

    @Test
    void findUtenteRegistratoByEmail() {
        // Test logic here
    }

    @Test
    void visualizzaListaLibriCompleta() {
        // Test logic here
    }

    @Test
    void visualizzaListaLibriPerTitolo() {
        // Test logic here
    }

    @Test
    void visualizzaListaLibriPerBiblioteca() {
        // Test logic here
    }

    @Test
    void visualizzaListaLibriPerGenere() {
        // Test logic here
    }

    @Test
    void richiediPrestito() {
        // Test logic here
    }

    @Test
    void getBibliotecheLibro() {
        // Test logic here
    }

    @Test
    void getLibroByID() {
        // Test logic here
    }

    @Test
    void getTicketsByBiblioteca() {
        // Test logic here
    }

    @Test
    void getTicketByID() {
        // Test logic here
    }

    @Test
    void accettaRichiesta() {
        // Test logic here
    }

    @Test
    void rifiutaRichiesta() {
        // Test logic here
    }

    @Test
    void chiudiTicket() {
        // Test logic here
    }

    @Test
    void getTicketsByEmailLettore() {
        // Test logic here
    }

    @Test
    void findByTitoloContains() {
        // Test logic here
    }

    @Test
    void inserimentoPerIsbn() {
        // Test logic here
    }

    @Test
    void inserimentoPerTitoloAutore() {
        // Test logic here
    }

    @Test
    void prenotazioneEffettuata() {
        // Test logic here
    }

    @Test
    void recuperaPossesso() {
        // Test logic here
    }

    @Test
    void eliminaLibroBiblioteca() {
        // Test logic here
    }

    @Test
    void prestitoLibro() {
        // Test logic here
    }
    @Test
    <E>
    void cercaLibroPerTitolo() {
        // Test logic here
        String titolo = "Il signore degli anelli";
        LibroBiblioteca libroBiblioteca = new LibroBiblioteca();
        libroBiblioteca.setIsbn("isbnlibro");
        libroBiblioteca.setTitolo("Il signore degli anelli");
        libroBiblioteca.setAutore("J.R.R. Tolkien");
        libroBiblioteca.setDescrizione("1954");

        List<LibroBiblioteca> listaLibri = new ArrayList<>();
        listaLibri.add(libroBiblioteca);

        when(bookApiAdapter.getLibroDaBookApi("isbmlibro",libroBiblioteca));

        List result = prenotazioneLibriService.findByTitoloContains(titolo).stream().collect(Collectors.toList());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(Libro.class, result.get(0).getClass());
    }

    @Test
    void restituisciLibro() {
    }

    @Test
    void elencaPrestitiLettore() {
        // Test logic here
        String emailUtente = "mario@example.com";
        UtenteRegistrato utente = new UtenteRegistrato();
        utente.setEmail("esempio@email.com");
        utente.setPassword(Arrays.toString("password".getBytes()));
        utente.setProvincia("Milano");
        utente.setCitta("Milano");
        utente.setVia("Via Example");
        utente.setRecapitoTelefonico("1234567890");
        utente.setTipo("LETTORE");
        UtenteRegistrato utenteB = new UtenteRegistrato();
        utenteB.setEmail("biblioteca@email.com");
        utenteB.setPassword(Arrays.toString("password".getBytes()));
        utenteB.setProvincia("Milano");
        utenteB.setCitta("Milano");
        utenteB.setVia("Via Example");
        utenteB.setRecapitoTelefonico("1234567890");
        utenteB.setTipo("BIBLIOTECA");

        List<TicketPrestito> listaPrestiti = new ArrayList<>();
        TicketPrestito ticketPrestito1 = new TicketPrestito();
        ticketPrestito1.setLettore(utente);
        ticketPrestito1.setBiblioteca(bibliotecaDAO.findByEmail(utenteB.getEmail()));
        ticketPrestito1.setStato(TicketPrestito.Stati.IN_ATTESA_DI_RESTITUZIONE);
        ticketPrestito1.setDataRichiesta(LocalDateTime.of(2023, 02,12,12,10));
        TicketPrestito ticketPrestito2 = new TicketPrestito();
        ticketPrestito2.setLettore(utente);
        ticketPrestito1.setBiblioteca(bibliotecaDAO.findByEmail(utenteB.getEmail()));
        ticketPrestito2.setStato(TicketPrestito.Stati.RIFIUTATO);
        ticketPrestito2.setDataRestituzione(LocalDateTime.now());
        ticketPrestito2.setDataRichiesta(LocalDateTime.of(2023, 02,12,12,10));

        when(ticketPrestitoDAO.findAllByLettoreEmail(emailUtente)).thenReturn(listaPrestiti);

        List<TicketPrestito> result = prenotazioneLibriService.getTicketsByEmailLettore( emailUtente);

        assertNotNull(result);
        assertEquals(2, result.size());
    }
}