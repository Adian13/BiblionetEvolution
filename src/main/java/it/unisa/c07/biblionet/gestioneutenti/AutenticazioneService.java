package it.unisa.c07.biblionet.gestioneutenti;


import it.unisa.c07.biblionet.entity.ClubDelLibro;
import it.unisa.c07.biblionet.entity.Biblioteca;
import it.unisa.c07.biblionet.entity.Esperto;
import it.unisa.c07.biblionet.entity.Lettore;
import it.unisa.c07.biblionet.entity.UtenteRegistrato;

import java.util.List;
import java.util.Set;

/**
 * @author Ciro Maiorino , Giulio Triggiani
 * Interfaccia per i metodi del sottosistema Autenticazione.
 */
public interface AutenticazioneService {
     /**
      * Firma del metodo che implementa la funzione di login.
      * @param email dell'utente da loggare.
      * @param password dell'utente da loggare.
      * @return dell'utente da loggato.
      */
     UtenteRegistrato login(String email, String password);

     /**
      * Firma del metodo che implementa l'identificazione di un lettore.
      * @param utente registrato che si trova in sessione.
      * @return true se l'utente è un lettore altrimenti false.
      */
     boolean isLettore(UtenteRegistrato utente);

     /**
      * Firma del metodo che implementa l'identificazione di un esperto.
      * @param utente registrato che si trova in sessione.
      * @return true se l'utente è un esperto altrimenti false.
      */
     boolean isEsperto(UtenteRegistrato utente);

     /**
      * Firma del metodo che implementa l'identificazione di una biblioteca.
      * @param utente registrato che si trova in sessione.
      * @return true se l'utente è una biblioteca altrimenti false.
      */
     boolean isBiblioteca(UtenteRegistrato utente);

     /**
      * Implementa la funzionalità di salvataggio delle modifiche
      * all'account biblioteca.
      * @param utente La biblioteca da aggiornare
      * @return la biblioteca aggiornata
      */
     Biblioteca aggiornaBiblioteca(Biblioteca utente);

     /**
      * Implementa la funzionalità di salvataggio delle modifiche
      * all'account esperto.
      * @param utente L'esperto da aggiornare
      * @return l'esperto aggiornato
      */
     Esperto aggiornaEsperto(Esperto utente);

     /**
      * Implementa la funzionalità di salvataggio delle modifiche
      * all'account lettore.
      * @param utente Lettore da aggiornare
      * @return il lettore aggiornato
      */
     Lettore aggiornaLettore(Lettore utente);

     List<Biblioteca> findBibliotecaByNome(String nomeBiblioteca);

     List<Esperto> findAllEsperti();

     List<Biblioteca> findAllBiblioteche();

     /**
      * Implementa la funzionalità di trovare un esperto.
      * @param email La mail dell esperto
      * @return L'esperto se c'è, altrimenti null
      */
     Esperto findEspertoByEmail(String email);

     /**
      * Implementa la funzionalità di trovare un lettore.
      * @param email La mail dell lettore
      * @return Il lettore se c'è, altrimenti null
      */
     Lettore findLettoreByEmail(String email);

     List<Esperto> findEspertiByNome(String nome);

     List<Biblioteca> findBibliotecaByCitta(String citta);

     /**
      * Implementa la funzionalità di trovare una biblioteca.
      * @param email La mail della biblioteca
      * @return La biblioteca se c'è, altrimenti null
      */
     Biblioteca findBibliotecaByEmail(String email);

     List<Esperto> findEspertiByGeneri(Set<String> generi);


     List<ClubDelLibro> findClubsEsperto(Esperto esperto);

     List<ClubDelLibro> findClubsLettore(Lettore lettore);
}