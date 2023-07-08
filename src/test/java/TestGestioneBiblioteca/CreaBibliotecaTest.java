package TestGestioneBiblioteca;

import it.unisa.c07.biblionet.common.Libro;
import it.unisa.c07.biblionet.common.UtenteRegistrato;
import it.unisa.c07.biblionet.gestionebiblioteca.BibliotecaDTO;
import it.unisa.c07.biblionet.gestionebiblioteca.repository.Biblioteca;
import it.unisa.c07.biblionet.gestionebiblioteca.repository.LibroBiblioteca;
import it.unisa.c07.biblionet.gestionebiblioteca.repository.Possesso;
import it.unisa.c07.biblionet.gestionebiblioteca.repository.TicketPrestito;

import java.util.Arrays;

public class CreaBibliotecaTest  {

    public void createBiblioteca() {
        UtenteRegistrato utente = new UtenteRegistrato();
        utente.setEmail("esempio@email.com");
        utente.setPassword(Arrays.toString("password".getBytes()));
        utente.setProvincia("Milano");
        utente.setCitta("Milano");
        utente.setVia("Via Example");
        utente.setRecapitoTelefonico("1234567890");
        utente.setTipo("utente");


    }
}