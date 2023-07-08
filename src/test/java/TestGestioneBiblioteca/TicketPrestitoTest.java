package TestGestioneBiblioteca;

import it.unisa.c07.biblionet.common.UtenteRegistrato;
import it.unisa.c07.biblionet.gestionebiblioteca.repository.Biblioteca;
import it.unisa.c07.biblionet.gestionebiblioteca.repository.LibroBiblioteca;
import it.unisa.c07.biblionet.gestionebiblioteca.repository.TicketPrestito;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class TicketPrestitoTest {

    @Test
    public void testConstructorAndGetters() {
        TicketPrestito.Stati stato = TicketPrestito.Stati.IN_ATTESA_DI_CONFERMA;
        LocalDateTime dataRichiesta = LocalDateTime.now();
        LocalDateTime dataRestituzione = LocalDateTime.now().plusDays(7);
        LibroBiblioteca libro = new LibroBiblioteca();
        Biblioteca biblioteca = new Biblioteca();
        UtenteRegistrato lettore = new UtenteRegistrato();

        TicketPrestito ticketPrestito = new TicketPrestito();
        ticketPrestito.setStato(stato);
        ticketPrestito.setDataRichiesta(dataRichiesta);
        ticketPrestito.setDataRestituzione(dataRestituzione);
        ticketPrestito.setLibro(libro);
        ticketPrestito.setBiblioteca(biblioteca);
        ticketPrestito.setLettore(lettore);

        Assertions.assertEquals(stato, ticketPrestito.getStato());
        Assertions.assertEquals(dataRichiesta, ticketPrestito.getDataRichiesta());
        Assertions.assertEquals(dataRestituzione, ticketPrestito.getDataRestituzione());
        Assertions.assertEquals(libro, ticketPrestito.getLibro());
        Assertions.assertEquals(biblioteca, ticketPrestito.getBiblioteca());
        Assertions.assertEquals(lettore, ticketPrestito.getLettore());
    }

    @Test
    public void testAllArgsConstructor() {
        TicketPrestito.Stati stato = TicketPrestito.Stati.IN_ATTESA_DI_CONFERMA;
        LocalDateTime dataRichiesta = LocalDateTime.now();
        LocalDateTime dataRestituzione = LocalDateTime.now().plusDays(7);
        LibroBiblioteca libro = new LibroBiblioteca();
        Biblioteca biblioteca = new Biblioteca();
        UtenteRegistrato lettore = new UtenteRegistrato();

        TicketPrestito ticketPrestito = new TicketPrestito();
        ticketPrestito.setStato(stato);
        ticketPrestito.setDataRichiesta(dataRichiesta);
        ticketPrestito.setDataRestituzione(dataRestituzione);
        ticketPrestito.setLibro(libro);
        ticketPrestito.setBiblioteca(biblioteca);
        ticketPrestito.setLettore(lettore);
        ticketPrestito.setIdTicket(001);

        Assertions.assertEquals(stato, ticketPrestito.getStato());
        Assertions.assertEquals(dataRichiesta, ticketPrestito.getDataRichiesta());
        Assertions.assertEquals(dataRestituzione, ticketPrestito.getDataRestituzione());
        Assertions.assertEquals(libro, ticketPrestito.getLibro());
        Assertions.assertEquals(biblioteca, ticketPrestito.getBiblioteca());
        Assertions.assertEquals(lettore, ticketPrestito.getLettore());
    }
}