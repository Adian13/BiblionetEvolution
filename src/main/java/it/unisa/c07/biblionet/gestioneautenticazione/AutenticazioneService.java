package it.unisa.c07.biblionet.gestioneautenticazione;
import it.unisa.c07.biblionet.common.UtenteRegistrato;


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

}
