
package it.unisa.c07.biblionet.gestioneutenti;

import it.unisa.c07.biblionet.common.UtenteRegistrato;
import it.unisa.c07.biblionet.gestioneclubdellibro.EspertoDTO;
import it.unisa.c07.biblionet.gestioneclubdellibro.LettoreDTO;
import it.unisa.c07.biblionet.gestioneprestitilibro.BibliotecaDTO;

/**
 * @author Alessio Casolaro
 * @author Antonio Della Porta.
 */
public interface RegistrazioneService {


    /**
     * Implementa la funzionalità di registrazione un Esperto.
     * @return L'utente registrato
     */
    UtenteRegistrato registraEsperto(EspertoDTO esperto, UtenteRegistrato biblioteca);

    /**
     * Implementa la funzionalità di registrazione una Biblioteca.
     * @param biblioteca La biblioteca da registrare
     * @return L'utente registrato
     */


    UtenteRegistrato registraBiblioteca(BibliotecaDTO biblioteca, String nomeBiblioteca, String password);

    /**
     * Implementa la funzionalità di registrare un Lettore.
     * @param lettore Il lettore da registrare
     * @return Il lettore registrato
     */
    UtenteRegistrato registraLettore(LettoreDTO lettore);
    /**
     * Implementa la funzionalità di controllare se una mail è
     * presente già associata a
     * un altro utente nel database.
     * @param email la mail da controllare
     * @return true se la mail è già associata, false altrimenti
     */
    boolean isEmailRegistrata(String email);

}
