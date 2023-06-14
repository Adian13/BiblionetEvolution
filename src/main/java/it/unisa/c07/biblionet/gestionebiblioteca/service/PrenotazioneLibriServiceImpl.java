package it.unisa.c07.biblionet.gestionebiblioteca.service;

import it.unisa.c07.biblionet.gestionebiblioteca.bookapiadapter.BookApiAdapter;
import it.unisa.c07.biblionet.common.ILibroIdAndName;
import it.unisa.c07.biblionet.common.UtenteRegistrato;
import it.unisa.c07.biblionet.gestionebiblioteca.BibliotecaDTO;
import it.unisa.c07.biblionet.gestionebiblioteca.PrenotazioneLibriService;
import it.unisa.c07.biblionet.gestionebiblioteca.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

/**
 * Implementa la classe che esplicita i metodi
 * definiti nell'interfaccia service per il
 * sottosustema PrenotazioneLibri.
 *
 * @author Viviana Pentangelo, Gianmario Voria
 */
@Service
@RequiredArgsConstructor
public class PrenotazioneLibriServiceImpl implements PrenotazioneLibriService {
    /**
     * Si occupa delle operazioni CRUD per libro.
     */
    private final LibroBibliotecaDAO libroBibliotecaDAO;
    private final BookApiAdapter bookApiAdapter;
    private final PossessoDAO possessoDAO;
    private final BibliotecaDAO bibliotecaDAO;
    private final TicketPrestitoDAO ticketPrestitoDAO;

    @Override
    public UtenteRegistrato bibliotecaDaModel(BibliotecaDTO form) {
        Biblioteca biblioteca = new Biblioteca(form);
        biblioteca.setTipo("Biblioteca");
        return aggiornaBiblioteca(biblioteca);

    }

    @Override
    public UtenteRegistrato findBibliotecaByEmailAndPassword(String email, byte[] password) {
        return bibliotecaDAO.findByEmailAndPassword(email, password);
    }

    /**
     * Implementa la funzionalità che permette
     * di visualizzare la lista completa dei libri
     * prenotabili sulla piattaforma.
     *
     * @return La lista di libri
     */
    @Override
    public List<LibroBiblioteca> visualizzaListaLibriCompleta() {
        return libroBibliotecaDAO.findAll(Sort.by("titolo"));
    }

    /**
     * Implementa la funzionalità che permette
     * di visualizzare una lista di libri prenotabili
     * filtrata per titolo.
     *
     * @param titolo Stringa che deve essere contenuta
     *               nel titolo
     * @return La lista di libri
     */
    @Override
    public List<LibroBiblioteca> visualizzaListaLibriPerTitolo(final String titolo) {
        return libroBibliotecaDAO.findByTitoloLike(titolo);
    }


    /**
     * Implementa la funzionalità che permette
     * di visualizzare la lista completa dei libri
     * prenotabili da una determinata biblioteca.
     *
     * @param email la mail della biblioteca
     * @return La lista di libri
     * <p>
     * todo completamente rifatta
     */
    @Override
    public List<LibroBiblioteca> visualizzaListaLibriPerBiblioteca(
            final String email) {
        List<LibroBiblioteca> libri = new ArrayList<>();

        List<Possesso> possessi = possessoDAO.findByBibliotecaID(email);
        for (Possesso p : possessi) {
            Optional<LibroBiblioteca> l =
                    libroBibliotecaDAO.findById(p.getPossessoID().getLibroID());
            if (!libri.contains(l.orElse(null))) {
                libri.add(l.orElse(null));
            }

        }
        return libri;
    }

    /**
     * Implementa la funzionalità che permette
     * di visualizzare la lista completa dei libri
     * prenotabili di un dato genere.
     *
     * @param genere Il nome del genere
     * @return La lista di libri
     */
    @Override
    public List<LibroBiblioteca> visualizzaListaLibriPerGenere(
            final String genere) {
/*
        List<LibroBiblioteca> libri = LibroBibliotecaDAO.findAll();
        List<LibroBiblioteca> list = new ArrayList<>();
        Genere g = genereDAO.findByName(genere);
        for (LibroBiblioteca l : libri) {
            if (l.getGeneri().contains(g)) {
                list.add(l);
            }
        }
        return list;
    todo ripristinare genere
    */
        return new ArrayList<>();
    }


    /**
     * Implementa la funzionalità che permette
     * di richiedere un prestito per un libro
     * da una biblioteca.
     *
     * @param lettore      Il lettore che lo richiede
     * @param idBiblioteca id della biblioteca
     * @param idLibro      id del libro
     * @return Il ticket aperto in attesa di approvazione
     */
    @Override
    public TicketPrestito richiediPrestito(final UtenteRegistrato lettore,
                                           final String idBiblioteca,
                                           final int idLibro) {
        TicketPrestito ticket = new TicketPrestito();
        ticket.setLettore(lettore);
        ticket.setDataRichiesta(LocalDateTime.now());
        ticket.setStato(TicketPrestito.Stati.IN_ATTESA_DI_CONFERMA);

        Biblioteca biblioteca =
                this.findBibliotecaByEmail(idBiblioteca);
        LibroBiblioteca libro = libroBibliotecaDAO.getOne(idLibro);

        ticket.setBiblioteca(biblioteca);
        ticket.setLibro(libro);

        ticketPrestitoDAO.save(ticket);
        return ticket;
    }

    /**
     * Implementa la funzionalità che permette
     * di ottenere la lista delle biblioteche
     * che posseggono un dato libro.
     *
     * @param libro Il libro di cui estrarre le biblioteche
     * @return La lista delle biblioteche che possiedono il libro
     */
    @Override
    public List<Biblioteca> getBibliotecheLibro(final LibroBiblioteca libro) {
        List<Biblioteca> lista = new ArrayList<>();

        for (Possesso p : libro.getPossessi()) {
            lista.add(this.findBibliotecaByEmail(p.getPossessoID().getBibliotecaID()));
        }
        return lista;
    }

    /**
     * Implementa la funzionalità che permette
     * di ottenere un libro dato il suo ID.
     *
     * @param id L'ID del libro da ottenere
     * @return Il libro da ottenere
     */
    @Override
    public LibroBiblioteca getLibroByID(final int id) {
        return libroBibliotecaDAO.getOne(id);
    }

    /**
     * Implementa la funzionalità che permette
     * di ottenere una lista di richieste per una biblioteca.
     *
     * @param biblioteca la biblioteca di cui vedere le richieste
     * @return La lista di richieste
     */
    @Override
    public List<TicketPrestito> getTicketsByBiblioteca(
            final Biblioteca biblioteca) {
        return ticketPrestitoDAO.
                findAllByBibliotecaEmail(biblioteca.getEmail());
    }

    /**
     * Implementa la funzionalità che permette
     * di ottenere un ticket dato il suo ID.
     *
     * @param id L'ID del ticket da recuperare
     * @return Il ticket ottenuto
     */
    @Override
    public TicketPrestito getTicketByID(final int id) {
        return ticketPrestitoDAO.getOne(id);
    }

    /**
     * Implementa la funzionalità che permette
     * di accettare la richiesta di prestito di un libro.
     *
     * @param ticket il ticket che rappresenta la richiesta
     * @param giorni il tempo di concessione del libro
     * @return Il ticket aggiornato
     */
    @Override
    public TicketPrestito accettaRichiesta(final TicketPrestito ticket,
                                           final int giorni) {
        ticket.setDataRestituzione(LocalDateTime.now().plusDays(giorni));
        ticket.setStato(TicketPrestito.Stati.IN_ATTESA_DI_RESTITUZIONE);
        LibroBiblioteca l = ticket.getLibro();
        Biblioteca b = ticket.getBiblioteca();
        Possesso pos = possessoDAO.
                getOne(new PossessoId(b.getEmail(), l.getIdLibro()));
        if (pos != null) {
            pos.setNumeroCopie(pos.getNumeroCopie() - 1);
            possessoDAO.save(pos);
        }
        ticketPrestitoDAO.save(ticket);
        return ticket;
    }

    /**
     * Implementa la funzionalità che permette
     * di rifiutare la richiesta di prestito di un libro.
     *
     * @param ticket il ticket che rappresenta la richiesta
     * @return Il ticket aggiornato
     */
    @Override
    public TicketPrestito rifiutaRichiesta(final TicketPrestito ticket) {
        ticket.setStato(TicketPrestito.Stati.RIFIUTATO);
        ticketPrestitoDAO.save(ticket);
        return ticket;
    }

    /**
     * Implementa la funzionalità che permette
     * di chiudere un ticket di prenotazione di un libro
     * quando questo viene riconsegnato.
     *
     * @param ticket il ticket che rappresenta la richiesta da chiudere
     * @return Il ticket aggiornato è stato chiuso
     */
    @Override
    public TicketPrestito chiudiTicket(final TicketPrestito ticket) {
        ticket.setStato(TicketPrestito.Stati.CHIUSO);
        LibroBiblioteca l = ticket.getLibro();
        Biblioteca b = ticket.getBiblioteca();
        Possesso pos = possessoDAO.
                getOne(new PossessoId(b.getEmail(), l.getIdLibro()));
        if (pos != null) {
            pos.setNumeroCopie(pos.getNumeroCopie() + 1);
            possessoDAO.save(pos);
        }
        return ticketPrestitoDAO.save(ticket);
    }

    /**
     * Implementa la funzionalità che permette
     * di ottenere la lista di ticket aperti da un Lettore.
     *
     * @param lettore il Lettore di cui recuperare i ticket
     * @return la lista dei ticket
     */
    @Override
    public List<TicketPrestito> getTicketsLettore(final UtenteRegistrato lettore) {
        return ticketPrestitoDAO.
                findAllByLettoreEmail(lettore.getEmail());
    }


    /**
     * Implementa la funzionalità che permette di
     * ottenere una lista di id e titoli di libri
     * sulla base di un titolo dato
     * ! Controllare prima di consegnare
     *
     * @param titolo il titolo che deve matchcare
     * @return la lista di informazioni
     */
    public List<ILibroIdAndName> findByTitoloContains(final String titolo) {
        List<ILibroIdAndName> infoLibroList =
                libroBibliotecaDAO.findByTitoloContains(titolo);

        if (infoLibroList == null) {
            infoLibroList = new ArrayList<>();
        } else if (infoLibroList.size() > 10) {
            infoLibroList = infoLibroList.subList(0, 9);
        }
        return infoLibroList;
    }

    /**
     * Implementa la funzionalità che permette
     * di creare un nuovo libro e inserirlo nella lista
     * a partire da un isbn usando una API di google.
     *
     * @param isbn         il Lettore di cui recuperare i ticket
     * @param idBiblioteca l'id della biblioteca che lo possiede
     * @param numCopie     il numero di copie possedute
     * @param generi       la lista dei generi
     * @return il libro creato
     */
    public LibroBiblioteca inserimentoPerIsbn(final String isbn,
                                              final String idBiblioteca,
                                              final int numCopie,
                                              final List<String> generi) {

        //Recupero l'oggetto Libro da Api per isbn
        LibroBiblioteca libro = bookApiAdapter.getLibroDaBookApi(isbn, new LibroBiblioteca());
        if (libro == null) {
            return null;
        }
        libro.setGeneri(new HashSet<>(generi));

        boolean exists = (libroBibliotecaDAO.findByIsbn(libro.getIsbn()) != null);
        if (!exists) libroBibliotecaDAO.save(libro);

        Biblioteca b = this.findBibliotecaByEmail(idBiblioteca);

        //Se per errore avesse inserito un libro che possiede già,
        //aggiorno semplicemente il numero di copie che ha. todo ok ma andrebbe richiamata un'apposita funzione non ricopiare il tutto qui
        for (Possesso p : b.getPossessi()) {
            if (p.getPossessoID().getLibroID() == libro.getIdLibro()) {
                p.setNumeroCopie(p.getNumeroCopie() + numCopie);
                possessoDAO.save(p);
                this.aggiornaBiblioteca(b);
                return libro;
            }
        }

        //Creo il possesso relativo al libro e alla biblioteca
        //che lo inserisce e lo memorizzo
        PossessoId pid = new PossessoId(idBiblioteca, libro.getIdLibro());
        Possesso possesso = new Possesso(pid, numCopie);
        possessoDAO.save(possesso);
        List<Possesso> plist = b.getPossessi();
        plist.add(possesso);
        b.setPossessi(plist);

        //Update della biblioteca con il nuovo possesso
        this.aggiornaBiblioteca(b);

        return libro;
    }

    /**
     * Implementa la funzionalità che permette
     * d'inserire un libro già memorizzato negli
     * archivi della piattaforma alla lista dei propri
     * libri prenotabili.
     *
     * @param idLibro      il Libro da inserire
     * @param idBiblioteca l'id della biblioteca che lo possiede
     * @param numCopie     il numero di copie possedute
     * @return il libro inserito
     */
    public LibroBiblioteca inserimentoDalDatabase(final int idLibro,
                                                  final String idBiblioteca,
                                                  final int numCopie) {
        LibroBiblioteca l = libroBibliotecaDAO.getOne(idLibro);
        Biblioteca b = this.findBibliotecaByEmail(idBiblioteca);
        //Se per errore avesse inserito un libro che possiede già,
        //aggiorno semplicemente il numero di copie che ha.
        for (Possesso p : b.getPossessi()) {
            if (p.getPossessoID().getLibroID() == idLibro) {
                p.setNumeroCopie(p.getNumeroCopie() + numCopie);
                possessoDAO.save(p);
                this.aggiornaBiblioteca(b);
                return l;
            }
        }

        //Creo e salvo il nuovo possesso
        PossessoId pid = new PossessoId(idBiblioteca, idLibro);
        Possesso p = new Possesso(pid, numCopie);
        possessoDAO.save(p);
        List<Possesso> plist = b.getPossessi();
        plist.add(p);
        b.setPossessi(plist);

        //Update della biblioteca con il nuovo possesso
        this.aggiornaBiblioteca(b);

        return l;
    }

    /**
     * Implementa la funzionalità che permette
     * d'inserire un libro attraverso un form.
     *
     * @param libro        il Libro da memorizzare
     * @param idBiblioteca l'id della biblioteca che lo possiede
     * @param numCopie     il numero di copie possedute
     * @param generi       la lista dei generi del libro
     * @return il libro inserito
     */
    public LibroBiblioteca inserimentoManuale(final LibroBiblioteca libro,
                                              final String idBiblioteca,
                                              final int numCopie,
                                              final List<String> generi) {

        Biblioteca b = this.findBibliotecaByEmail(idBiblioteca);

        //Controllo che il libro non sia già salvato
        boolean exists = false;
        LibroBiblioteca l = new LibroBiblioteca();


        libro.setGeneri(new HashSet<>(generi));
        for (LibroBiblioteca tl : libroBibliotecaDAO.findAll()) {
            if (tl.getTitolo().equals(libro.getTitolo())) {
                exists = true;
                l = tl;
            }
        }
        if (!exists) {
            l = libroBibliotecaDAO.save(libro);
        }
        //Se per errore avesse inserito un libro che possiede già,
        //aggiorno semplicemente il numero di copie che ha.
        for (Possesso p : b.getPossessi()) {
            if (p.getPossessoID().getLibroID() == l.getIdLibro()) {
                p.setNumeroCopie(p.getNumeroCopie() + numCopie);
                possessoDAO.save(p);
                this.aggiornaBiblioteca(b);
                return l;
            }
        }

        //Creo e salvo il nuovo possesso
        PossessoId pid = new PossessoId(idBiblioteca, l.getIdLibro());
        Possesso p = new Possesso(pid, numCopie);
        possessoDAO.save(p);
        List<Possesso> plist = b.getPossessi();
        plist.add(p);
        b.setPossessi(plist);

        //Update della biblioteca con il nuovo possesso
        this.aggiornaBiblioteca(b);

        return l;
    }

    /**
     * Implementa la funzionalità di trovare una biblioteca.
     *
     * @param email La mail della biblioteca
     * @return La biblioteca se c'è, altrimenti null
     */
    @Override
    public final Biblioteca findBibliotecaByEmail(final String email) {
        return bibliotecaDAO.findBibliotecaByEmail(email, "Biblioteca"); //todo usare una costante
    }

    @Override
    public List<Biblioteca> findBibliotecaByNome(String nomeBiblioteca) {
        return bibliotecaDAO.findByNome(nomeBiblioteca);
    }

    @Override
    public List<Biblioteca> findBibliotecaByCitta(String citta) {
        return bibliotecaDAO.findByCitta(citta);
    }

    @Override
    public List<Biblioteca> findAllBiblioteche() {
        return bibliotecaDAO.findAllBiblioteche();
    }

    /**
     * Implementa la funzionalità di salvataggio delle modifiche
     * all'account biblioteca.
     *
     * @param utente La biblioteca da aggiornare
     * @return la biblioteca aggiornata
     */
    @Override
    public Biblioteca aggiornaBiblioteca(final Biblioteca utente) {
        return bibliotecaDAO.save(utente);
    }


}

