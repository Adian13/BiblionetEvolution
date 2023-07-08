package TestGestioneBiblioteca;

import it.unisa.c07.biblionet.common.UtenteRegistrato;
import it.unisa.c07.biblionet.gestionebiblioteca.BibliotecaDTO;
import it.unisa.c07.biblionet.gestionebiblioteca.PrenotazioneLibriService;
import it.unisa.c07.biblionet.gestionebiblioteca.controller.BibliotecaController;
import it.unisa.c07.biblionet.gestionebiblioteca.repository.LibroBiblioteca;
import it.unisa.c07.biblionet.gestionebiblioteca.service.BibliotecaMapper;
import it.unisa.c07.biblionet.utils.BiblionetResponse;
import it.unisa.c07.biblionet.utils.Utils;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.validation.BindingResult;


import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BibliotecaControllerTest{

    @Mock
    private PrenotazioneLibriService prenotazioneService;
    @Mock
    private BibliotecaController bibliotecaController;
    @Mock
    private BibliotecaMapper bibliotecaMapper;

    @Test
    void modificaDatiBiblioteca_WithValidData_ShouldReturnSuccessResponse() {
        // Arrange
        String token = "valid_token";
        BibliotecaDTO biblioteca = new BibliotecaDTO();
        biblioteca.setEmail("test@example.com");
        String vecchiaPassword = "old_password";
        String nuovaPassword = "new_password";
        String confermaPassword = "new_password";
        BindingResult bindingResult = mock(BindingResult.class);
        UtenteRegistrato utenteRegistrato = new UtenteRegistrato();

        when(Utils.isUtenteBiblioteca(token)).thenReturn(true);
        when(Utils.getSubjectFromToken(token)).thenReturn(biblioteca.getEmail());
        when(bindingResult.hasErrors()).thenReturn(false);
        when(prenotazioneService.loginBiblioteca(biblioteca.getEmail(), vecchiaPassword))
                .thenReturn(utenteRegistrato);

        // Act
        BiblionetResponse response = bibliotecaController.modificaDatiBiblioteca(
                token, biblioteca, bindingResult, vecchiaPassword, nuovaPassword, confermaPassword);

        // Assert
        assertNotNull(response);
        assertEquals(BiblionetResponse.OPERAZIONE_OK, response.getPayload());
        assertTrue(response.isStatusOk());
        verify(prenotazioneService).bibliotecaDaModel(biblioteca);
    }

    @Test
    void modificaDatiBiblioteca_WithInvalidToken_ShouldReturnUnauthorizedResponse() {
        // Arrange
        String token = "invalid_token";
        BibliotecaDTO biblioteca = new BibliotecaDTO();

        when(Utils.isUtenteBiblioteca(token)).thenReturn(false);

        // Act
        BiblionetResponse response = bibliotecaController.modificaDatiBiblioteca(
                token, biblioteca, mock(BindingResult.class), "", "", "");

        // Assert
        assertNotNull(response);
        assertEquals(BiblionetResponse.NON_AUTORIZZATO, response.getPayload());
        assertFalse(response.isStatusOk());
        verifyZeroInteractions(prenotazioneService);
    }

    /*@Test
    void prenotaLibro_WithValidData_ShouldReturnSuccessResponse() {
        // Arrange
        String token = "valid_token";
        int idLibro = 1;
        LibroBibliotecaDTO libroBibliotecaDTO = new LibroBibliotecaDTO();

        when(Utils.isUtenteLettore(token)).thenReturn(true);

        // Act
        BiblionetResponse response = bibliotecaController.prenotaLibro(token, idLibro, libroBibliotecaDTO);

        // Assert
        assertNotNull(response);
        assertEquals(BiblionetResponse.OPERAZIONE_OK, response.getMessage());
        assertTrue(response.isSuccess());
        verify(prenotazioneService).prenotaLibro(idLibro, libroBibliotecaDTO, token);
    }

    @Test
    void prenotaLibro_WithInvalidToken_ShouldReturnUnauthorizedResponse() {
        // Arrange
        String token = "invalid_token";
        int idLibro = 1;
        LibroBibliotecaDTO libroBibliotecaDTO = new LibroBibliotecaDTO();

        when(Utils.isUtenteRegistrato(token)).thenReturn(false);

        // Act
        BiblionetResponse response = bibliotecaController.prenotaLibro(token, idLibro, libroBibliotecaDTO);

        // Assert
        assertNotNull(response);
        assertEquals(BiblionetResponse.NON_AUTORIZZATO, response.getPayload());
        assertFalse(response.isStatusOk());
        verifyZeroInteractions(prenotazioneService);
    }
*/
    @Test
    void listaLibriBiblioteca_WithExistingBiblioteca_ShouldReturnListOfBooks() {
        // Arrange
        String token = "valid_token";
        String emailBiblioteca = "test@example.com";
        List<LibroBiblioteca> libriBiblioteca = Arrays.asList(
                new LibroBiblioteca(), new LibroBiblioteca());

        when(Utils.isUtenteBiblioteca(token)).thenReturn(true);
        when(Utils.getSubjectFromToken(token)).thenReturn(emailBiblioteca);
        when(prenotazioneService.visualizzaListaLibriPerBiblioteca(emailBiblioteca)).thenReturn(libriBiblioteca);

        // Act
        List<LibroBiblioteca> result = prenotazioneService.visualizzaListaLibriPerBiblioteca(token);

        // Assert
        assertNotNull(result);
        assertEquals(libriBiblioteca.size(), result.size());
        verify(prenotazioneService).visualizzaListaLibriPerBiblioteca(emailBiblioteca);
    }

    @Test
    void listaLibriBiblioteca_WithInvalidToken_ShouldReturnUnauthorizedResponse() {
        // Arrange
        String token = "invalid_token";

        when(Utils.isUtenteBiblioteca(token)).thenReturn(false);

        // Act
        List<BibliotecaDTO> result = bibliotecaController.visualizzaListaBiblioteche();

        // Assert
        assertNull(result);
        verifyZeroInteractions(prenotazioneService);
    }
}