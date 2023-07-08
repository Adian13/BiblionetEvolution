package TestGestioneBiblioteca;

import it.unisa.c07.biblionet.common.Libro;
import it.unisa.c07.biblionet.utils.BiblionetConstraints;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class LibroTest {

    private Libro libro;
    private final String titolo = "Titolo del libro";
    private final String autore = "Autore del libro";
    private final String isbn = "9781234567890";
    private final LocalDateTime annoDiPubblicazione = LocalDateTime.now();
    private final String descrizione = "Descrizione del libro";
    private final String casaEditrice = "Casa editrice";
    private final String immagineLibro = "Immagine del libro";
    private final Set<String> generi = new HashSet<>();

    @BeforeEach
    void setUp() {
        libro = new LibroImpl(titolo, autore, isbn, annoDiPubblicazione, descrizione, casaEditrice, immagineLibro, generi);
    }

    @Test
    void testGetters() {
        assertEquals(titolo, libro.getTitolo());
        assertEquals(autore, libro.getAutore());
        assertEquals(isbn, libro.getIsbn());
        assertEquals(annoDiPubblicazione, libro.getAnnoDiPubblicazione());
        assertEquals(descrizione, libro.getDescrizione());
        assertEquals(casaEditrice, libro.getCasaEditrice());
        assertEquals(immagineLibro, libro.getImmagineLibro());
        assertEquals(generi, libro.getGeneri());
    }

    @Test
    void testIdLibro() {
        assertEquals(0, libro.getIdLibro());
    }

    @Test
    void testTitoloNonNull() {
        assertNotNull(libro.getTitolo());
    }

    @Test
    void testTitoloLength() {
        assertTrue(libro.getTitolo().length() <= BiblionetConstraints.LENGTH_90);
    }

    @Test
    void testAutoreNonNull() {
        assertNotNull(libro.getAutore());
    }

    @Test
    void testAutoreLength() {
        assertTrue(libro.getAutore().length() <= BiblionetConstraints.LENGTH_60);
    }

    @Test
    void testIsbnNonNull() {
        assertNotNull(libro.getIsbn());
    }

    @Test
    void testIsbnUnique() {
        assertTrue(libro.getIsbn().length() <= BiblionetConstraints.LENGTH_13);
    }

    @Test
    void testAnnoDiPubblicazioneNonNull() {
        assertNotNull(libro.getAnnoDiPubblicazione());
    }

    @Test
    void testDescrizioneNonNull() {
        assertNotNull(libro.getDescrizione());
    }

    @Test
    void testDescrizioneLength() {
        assertTrue(libro.getDescrizione().length() <= BiblionetConstraints.LENGTH_144);
    }

    @Test
    void testCasaEditriceNonNull() {
        assertNotNull(libro.getCasaEditrice());
    }

    @Test
    void testCasaEditriceLength() {
        assertTrue(libro.getCasaEditrice().length() <= BiblionetConstraints.LENGTH_30);
    }

    @Test
    void testImmagineLibro() {
        assertEquals(immagineLibro, libro.getImmagineLibro());
    }

    @Test
    void testGeneri() {
        assertEquals(generi, libro.getGeneri());
    }

    private static class LibroImpl extends Libro {
        public LibroImpl(String titolo, String autore, String isbn, LocalDateTime annoDiPubblicazione, String descrizione, String casaEditrice, String immagineLibro, Set<String> generi) {
            super(titolo, autore, isbn, annoDiPubblicazione, descrizione, casaEditrice, immagineLibro, generi);
        }
    }
}