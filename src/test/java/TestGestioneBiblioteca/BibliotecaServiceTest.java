package TestGestioneBiblioteca;

import it.unisa.c07.biblionet.common.UtenteRegistrato;
import it.unisa.c07.biblionet.gestionebiblioteca.BibliotecaDTO;
import it.unisa.c07.biblionet.gestionebiblioteca.repository.*;
import it.unisa.c07.biblionet.gestionebiblioteca.service.BibliotecaService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

class BibliotecaServiceTest {
    @Mock
    private BibliotecaDAO bibliotecaDAO;

    @Mock
    private LibroBibliotecaDAO libroBibliotecaDAO;

    @Mock
    private TicketPrestitoDAO ticketPrestitoDAO;

    @InjectMocks
    private BibliotecaService bibliotecaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindBibliotecaByEmail() {
        String email = "example@example.com";
        Biblioteca expectedBiblioteca = new Biblioteca();
        when(bibliotecaDAO.findByEmail(email)).thenReturn(expectedBiblioteca);

        Biblioteca result = bibliotecaService.findByEmail(email);

        Assertions.assertEquals(expectedBiblioteca, result);
        verify(bibliotecaDAO, times(1)).findByEmail(email);
    }

    @Test
    void testFindAllUtentiRegistrati() {
        List<UtenteRegistrato> expectedUtentiRegistrati = new ArrayList<>();
        when(bibliotecaService.findAllUtentiRegistrati()).thenReturn(expectedUtentiRegistrati);

        List<UtenteRegistrato> result = bibliotecaService.findAllUtentiRegistrati();

        Assertions.assertEquals(expectedUtentiRegistrati, result);
        verify(bibliotecaService, times(1)).findAllUtentiRegistrati();
    }

    @Test
    void testAggiornaBiblioteca() {

    }


}