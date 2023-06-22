package it.unisa.c07.biblionet.gestionebiblioteca;

import it.unisa.c07.biblionet.common.*;
import it.unisa.c07.biblionet.gestionebiblioteca.repository.Biblioteca;
import it.unisa.c07.biblionet.gestionebiblioteca.repository.LibroBiblioteca;
import it.unisa.c07.biblionet.gestionebiblioteca.repository.TicketPrestito;

import java.util.List;

/**
 * Implementa l'interfaccia service
 * per il sottosistema PrenotazioneLibri.
 * @author Viviana Pentangelo, Gianmario Voria
 */
public interface PrenotazioneLibriService {




    UtenteRegistrato bibliotecaDaModel(BibliotecaDTO form);

    UtenteRegistrato loginBiblioteca(String email, String password);

    UtenteRegistrato findBibliotecaByEmailAndPassword(String email, byte[] password);

    UtenteRegistrato findUtenteRegistratoByEmail(String email);

    /**
     * Implementa la funzionalità che permette
     * di visualizzare la lista completa dei libri
     * prenotabili sulla piattaforma.
     * @return La lista di libri
     */
    List<LibroBiblioteca> visualizzaListaLibriCompleta();



    /**
     * Implementa la funzionalità che permette
     * di visualizzare una lista di libri prenotabili
     * filtrata per titolo.
     * @param titolo Stringa che deve essere contenuta
     * nel titolo
     * @return La lista di libri
     */
    List<LibroBiblioteca> visualizzaListaLibriPerTitolo(String titolo);

    /**
     * Implementa la funzionalità che permette
     * di visualizzare la lista completa dei libri
     * prenotabili da una determinata biblioteca.
     * @param nomeBiblioteca Il nome della biblioteca
     * @return La lista di libri
     */
    List<LibroBiblioteca> visualizzaListaLibriPerBiblioteca(String nomeBiblioteca);

    /**
     * Implementa la funzionalità che permette
     * di visualizzare la lista completa dei libri
     * prenotabili di un dato genere.
     * @param genere Il nome del genere
     * @return La lista di libri
     */
    List<LibroBiblioteca> visualizzaListaLibriPerGenere(String genere);

    /**
     * Implementa la funzionalità che permette
     * di richiedere un prestito per un libro
     * da una biblioteca.
     * @param lettore Il lettore che lo richiede
     * @param idBiblioteca id della biblioteca
     * @param idLibro id del libro
     * @return Il ticket aperto in attesa di approvazione
     */
    TicketPrestito richiediPrestito(UtenteRegistrato lettore,
                                    String idBiblioteca,
                                    int idLibro);

    /**
     * Implementa la funzionalità che permette
     * di ottenere la lista delle biblioteche
     * che posseggono un dato libro.
     * @param libro Il libro di cui estrarre le biblioteche
     * @return La lista delle biblioteche che possiedono il libro
     */
    List<Biblioteca> getBibliotecheLibro(LibroBiblioteca libro);

    /**
     * Implementa la funzionalità che permette
     * di ottenere un libro dato il suo ID.
     * @param id L'ID del libro da ottenere
     * @return Il libro da ottenere
     */
    LibroBiblioteca getLibroByID(int id);

    /**
     * Implementa la funzionalità che permette
     * di ottenere una lista di richieste per una biblioteca.
     * @param biblioteca la biblioteca di cui vedere le richieste
     * @return La lista di richieste
     */
    List<TicketPrestito> getTicketsByBiblioteca(Biblioteca biblioteca);

    /**
     * Implementa la funzionalità che permette
     * di ottenere un ticket dato il suo ID.
     * @param id L'ID del ticket da recuperare
     * @return Il ticket ottenuto
     */
    TicketPrestito getTicketByID(int id);

    /**
     * Implementa la funzionalità che permette
     * di accettare la richiesta di prestito di un libro.
     * @param ticket il ticket che rappresenta la richiesta
     * @param giorni il tempo di concessione del libro
     * @return Il ticket aggiornato
     */
    TicketPrestito accettaRichiesta(TicketPrestito ticket, int giorni);

    /**
     * Implementa la funzionalità che permette
     * di rifiutare la richiesta di prestito di un libro.
     * @param ticket il ticket che rappresenta la richiesta
     * @return Il ticket aggiornato
     */
    TicketPrestito rifiutaRichiesta(TicketPrestito ticket);

    /**
     * Implementa la funzionalità che permette
     * di chiudere un ticket di prenotazione di un libro
     * quando questo viene riconsegnato.
     * @param ticket il ticket che rappresenta la richiesta da chiudere
     * @return Il ticket aggiornato a chiuso
     */
    TicketPrestito chiudiTicket(TicketPrestito ticket);

    /**
     * Implementa la funzionalità che permette
     * di ottenere la lista di ticket aperti da un Lettore.
     * @param emailLettore l'id del lettore di cui recuperare i ticket
     * @return la lista dei ticket
     */
    List<TicketPrestito> getTicketsByEmailLettore(String emailLettore);

    /**
     * Implementa la funzionalità che permette di
     * ottenere una lista di id e titoli di libri
     * sulla base di un titolo dato
     *
     * ! Controllare prima di consegnare
     *
     * @param titolo il titolo che deve mathcare
     * @return la lista di informazioni
     */
    List<ILibroIdAndName> findByTitoloContains(String titolo);

    /**
     * Implementa la funzionalità che permette
     * di creare un nuovo libro e inserirlo nella lista
     * a partire da un isbn usando una API di google.
     * @param isbn il Lettore di cui recuperare i ticket
     * @param idBiblioteca l'id della biblioteca che lo possiede
     * @param numCopie il numero di copie possedute
     * @param generi la lista dei generi del libro
     * @return il libro creato
     */
    LibroBiblioteca inserimentoPerIsbn(String isbn, String idBiblioteca,
                                   int numCopie, List<String> generi);

    /**
     * Implementa la funzionalità che permette
     * d'inserire un libro già memorizzato negli
     * archivi della piattaforma alla lista dei propri
     * libri prenotabili.
     * @param idLibro il Libro da inserire
     * @param idBiblioteca l'id della biblioteca che lo possiede
     * @param numCopie il numero di copie possedute
     * @return il libro inserito
     */
    LibroBiblioteca inserimentoDalDatabase(int idLibro,
                                       String idBiblioteca, int numCopie);

    /**
     * Implementa la funzionalità che permette
     * d'inserire un libro attraverso un form.
     * @param libro il Libro da memorizzare
     * @param idBiblioteca l'id della biblioteca che lo possiede
     * @param numCopie il numero di copie possedute
     * @param generi la lista dei generi del libro
     * @return il libro inserito
     */
    LibroBiblioteca inserimentoManuale(LibroBiblioteca libro, String idBiblioteca,
                                   int numCopie, List<String> generi);


    Biblioteca findBibliotecaByEmail(String email);

    List<Biblioteca> findBibliotecaByNome(String nomeBiblioteca);

    List<Biblioteca> findBibliotecaByCitta(String citta);

    List<Biblioteca> findAllBiblioteche();

    Biblioteca aggiornaBiblioteca(Biblioteca utente);

}
