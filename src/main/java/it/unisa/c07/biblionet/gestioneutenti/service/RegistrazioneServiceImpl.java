package it.unisa.c07.biblionet.gestioneutenti.service;

import it.unisa.c07.biblionet.gestioneclubdellibro.EspertoDTO;
import it.unisa.c07.biblionet.gestioneclubdellibro.ClubDelLibroService;
import it.unisa.c07.biblionet.gestioneclubdellibro.LettoreDTO;
import it.unisa.c07.biblionet.gestionebiblioteca.BibliotecaDTO;
import it.unisa.c07.biblionet.gestionebiblioteca.PrenotazioneLibriService;
import it.unisa.c07.biblionet.gestioneutenti.RegistrazioneService;
import it.unisa.c07.biblionet.common.UtenteRegistrato;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Alessio Casolaro
 * @author Antonio Della Porta.
 */
@Service
@RequiredArgsConstructor
public class RegistrazioneServiceImpl implements RegistrazioneService {

    private final PrenotazioneLibriService prenotazioneLibriService;
    private final ClubDelLibroService clubDelLibroService;


    /**
     * Implementa la funzionalità di registrare un Lettore.
     * @param lettore Il lettore da registrare
     * @return Il lettore registrato
     */
    @Override
    public final UtenteRegistrato registraLettore(final LettoreDTO lettore) {
        return clubDelLibroService.creaLettoreDaModel(lettore);
    }

    /**
     * Implementa la funzionalità di registrazione un Esperto.
     * @param esperto L'Esperto da registrare
     * @return L'utente registrato
     */
    @Override
    public final UtenteRegistrato registraEsperto(final EspertoDTO esperto, final String emailBiblioteca) {
        return clubDelLibroService.creaEspertoDaModel(esperto, prenotazioneLibriService.findBibliotecaByEmail(emailBiblioteca));
    }

    @Override
    public final UtenteRegistrato aggiornaEsperto(final EspertoDTO esperto, final String emailBiblioteca) {
        return clubDelLibroService.aggiornaEspertoDaModel(esperto, prenotazioneLibriService.findBibliotecaByEmail(emailBiblioteca));
    }

    @Override
    public final UtenteRegistrato aggiornaLettore(final LettoreDTO lettore) {
        return clubDelLibroService.aggiornaLettoreDaModel(lettore);
    }




    /**
     * Implementa la funzionalità di registrazione una Biblioteca.
     * @param biblioteca La Biblioteca da registrare
     * @return L'utente registrato
     */
    @Override
    public UtenteRegistrato registraBiblioteca(final BibliotecaDTO biblioteca) {
        return prenotazioneLibriService.bibliotecaDaModel(biblioteca);
    }
    @Override
    public UtenteRegistrato aggiornaBiblioteca(final BibliotecaDTO biblioteca) {
        return registraBiblioteca(biblioteca);
    }




    /**
     * Implementa la funzionalità di controllare se una mail è
     * presente già associata ad un altro utente nel database.
     * @param email la mail da controllare
     * @return true se la mail è già associata, false altrimenti
     */
    @Override
    public boolean isEmailRegistrata(final String email) {
        if ((clubDelLibroService.findLettoreByEmail(email)) != null) {
            return true;
        }
        if ((prenotazioneLibriService.findBibliotecaByEmail(email)) != null) {
            return true;
        }
        if ((clubDelLibroService.findEspertoByEmail(email)) != null) {
            return true;
        }
        return false;
    }




}
