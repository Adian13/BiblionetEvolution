package TestGestioneBiblioteca;
import it.unisa.c07.biblionet.gestionebiblioteca.repository.LibroBiblioteca;
import it.unisa.c07.biblionet.gestionebiblioteca.repository.Possesso;
import it.unisa.c07.biblionet.gestionebiblioteca.repository.TicketPrestito;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class LibroBibliotecaTest {

    @Test
    public void testConstructorAndGetters() {
        String titolo = "Il nome del vento";
        String autore = "Patrick Rothfuss";
        String isbn = "9788804668238";
        LocalDateTime annoDiPubblicazione = LocalDateTime.of(2007, 3, 27, 0, 0);
        String descrizione = "La storia di Kvothe, un mago leggendario.";
        String casaEditrice = "Mondadori";
        String immagineLibro = "url_immagine";
        Set<String> generi = Set.of("Fantasy", "Avventura");

        List<Possesso> possessi = new ArrayList<>();
        List<TicketPrestito> tickets = new ArrayList<>();

        LibroBiblioteca libro = new LibroBiblioteca(
                titolo,
                autore,
                isbn,
                annoDiPubblicazione,
                descrizione,
                casaEditrice,
                immagineLibro,
                generi,
                possessi,
                tickets
        );

        Assertions.assertEquals(titolo, libro.getTitolo());
        Assertions.assertEquals(autore, libro.getAutore());
        Assertions.assertEquals(isbn, libro.getIsbn());
        Assertions.assertEquals(annoDiPubblicazione, libro.getAnnoDiPubblicazione());
        Assertions.assertEquals(descrizione, libro.getDescrizione());
        Assertions.assertEquals(casaEditrice, libro.getCasaEditrice());
        Assertions.assertEquals(immagineLibro, libro.getImmagineLibro());
        Assertions.assertEquals(generi, libro.getGeneri());
        Assertions.assertEquals(possessi, libro.getPossessi());
        Assertions.assertEquals(tickets, libro.getTickets());
    }
}