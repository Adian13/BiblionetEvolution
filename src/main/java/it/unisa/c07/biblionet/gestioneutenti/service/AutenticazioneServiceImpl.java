package it.unisa.c07.biblionet.gestioneutenti.service;

import it.unisa.c07.biblionet.common.*;
import it.unisa.c07.biblionet.gestionebiblioteca.BibliotecaService;
import it.unisa.c07.biblionet.gestioneclubdellibro.ClubDelLibroService;
import it.unisa.c07.biblionet.gestionebiblioteca.PrenotazioneLibriService;
import it.unisa.c07.biblionet.gestioneclubdellibro.EspertoService;
import it.unisa.c07.biblionet.gestioneclubdellibro.LettoreService;
import it.unisa.c07.biblionet.gestioneutenti.AutenticazioneService;
import it.unisa.c07.biblionet.utils.BiblionetConstraints;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Implementa la classe che esplicita i metodi
 * definiti nell'interfaccia service per il
 * sottosistema Autenticazione.
 *
 * @author Ciro Maiorino, Giulio Triggiani
 */
@Service
@RequiredArgsConstructor
public class AutenticazioneServiceImpl implements AutenticazioneService {

    private final EspertoService espertoService;
    private final LettoreService lettoreService;
    private final BibliotecaService bibliotecaService;

    /**
     * I.
     */


    /**
     * Implementa la funzionalità di login
     * per un Utente registrato.
     *
     * @param email    dell'utente.
     * @param password dell'utente.
     * @return un utente registrato.
     */
    @Override
    public UtenteRegistrato login(final String email, final String password) {

        byte[] arr = BiblionetConstraints.trasformaPassword(password);
        UtenteRegistrato u;

        if ((u = espertoService.findEspertoByEmailAndPassword(email, arr)) != null) {
            return u;
        }
        else if ((u = bibliotecaService.findBibliotecaByEmailAndPassword(email, arr)) != null) {
            return u;
        } else {
            u = lettoreService.findLettoreByEmailAndPassword(email, arr);
            return u;
        }

    }

    /**
     * Implementa la funzionalità che
     * identifica un utente in sessione.
     *
     * @param utente registrato che si trova già in sessione.
     * @return true se l'utente è un lettore altrimenti false.
     */
    @Override
    public boolean isLettore(final UtenteRegistrato utente) {
        return utente.getTipo().equalsIgnoreCase("Lettore");
    }

    /**
     * Implementa la funzionalità che
     * identifica di un utente in sessione.
     *
     * @param utente registrato che si trova già in sessione.
     * @return true se l'utente è un esperto altrimenti false.
     */
    @Override
    public boolean isEsperto(final UtenteRegistrato utente) {
        return utente.getTipo().equalsIgnoreCase("Esperto");
    }

    /**
     * Implementa la funzionalità che
     * identifica un utente in sessione.
     *
     * @param utente registrato che si trova già in sessione.
     * @return true se l'utente è una biblioteca altrimenti false.
     */
    @Override
    public boolean isBiblioteca(final UtenteRegistrato utente) {
        return utente.getTipo().equalsIgnoreCase("Biblioteca");
    }




}