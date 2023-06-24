package it.unisa.c07.biblionet.gestioneautenticazione.service;

import it.unisa.c07.biblionet.gestioneautenticazione.AutenticazioneService;
import it.unisa.c07.biblionet.gestioneautenticazione.repository.UtenteRegistrato;
import it.unisa.c07.biblionet.gestioneautenticazione.repository.UtenteRegistratoDAO;
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

    private final UtenteRegistratoDAO utenteRegistratoDAO;

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
        return utenteRegistratoDAO.findUtenteRegistratoByEmailAndPassword(email, BiblionetConstraints.trasformaPassword(password));
    }

}