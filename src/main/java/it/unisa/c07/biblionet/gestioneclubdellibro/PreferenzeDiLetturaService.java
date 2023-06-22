package it.unisa.c07.biblionet.gestioneclubdellibro;

import it.unisa.c07.biblionet.common.UtenteRegistrato;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Esperto;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Genere;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Lettore;

import java.util.List;
import java.util.Set;

/**
 * @author Alessio Casolaro
 * @author Antonio Della Porta
 */
public interface PreferenzeDiLetturaService {
    public void addGeneri(Set<Genere> generi,
                          UtenteRegistrato utenteRegistrato);
}
