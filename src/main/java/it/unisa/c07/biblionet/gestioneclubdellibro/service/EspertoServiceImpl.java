package it.unisa.c07.biblionet.gestioneclubdellibro.service;

import it.unisa.c07.biblionet.common.UtenteRegistrato;
import it.unisa.c07.biblionet.gestioneclubdellibro.EspertoDTO;
import it.unisa.c07.biblionet.gestioneclubdellibro.EspertoService;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.ClubDelLibro;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Esperto;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.EspertoDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class EspertoServiceImpl implements EspertoService {

    private final EspertoDAO espertoDAO;



    @Override
    public Esperto creaEspertoDaModel(EspertoDTO form, String emailBiblioteca) {
        UtenteRegistrato biblioteca = espertoDAO.findByID(emailBiblioteca);
        if(biblioteca == null) return null;
        return espertoDAO.save(new Esperto(form, biblioteca));
    }

    @Override
    public Esperto aggiornaEspertoDaModel(EspertoDTO form, String biblioteca) {
        return creaEspertoDaModel(form, biblioteca);
    }

    /**
     * Implementa la funzionalità di salvataggio delle modifiche
     * all'account esperto.
     *
     * @param utente L'esperto da aggiornare
     * @return l'esperto aggiornato
     */
    @Override
    public Esperto aggiornaEsperto(final Esperto utente) {
        return espertoDAO.save(utente);
    }

    @Override
    public final List<Esperto> findEspertiByGeneri(final Set<String> generi) {
        List<Esperto> toReturn = new ArrayList<>();

        for (Esperto esperto : espertoDAO.findAllEsperti()) {
            for (String genere : esperto.getGeneri()) {
                if (generi.contains(genere) && !toReturn.contains(esperto)) {
                    toReturn.add(esperto);
                }
            }
        }
        return toReturn;
    }


    @Override
    public List<Esperto> getEspertiByBiblioteca(String email){
        return espertoDAO.findEspertoByBibliotecaEmail(email);
    }

    @Override
    public List<String> getEspertiEmailByBiblioteca(String email){
        return espertoDAO.findEspertoEmailByBibliotecaEmail(email);
    }
    @Override
    public Esperto findEspertoByEmail(final String email) {
        return espertoDAO.findEspertoByEmail(email, "Esperto");
    }

    @Override
    public List<Esperto> findAllEsperti() {
        return espertoDAO.findAllEsperti();
    }

    @Override
    public List<Esperto> findEspertiByNome(String nome) {
        return espertoDAO.findByNomeLike(nome);
    }

    @Override
    public UtenteRegistrato findEspertoByEmailAndPassword(String email, byte[] password) {
        return espertoDAO.findByEmailAndPassword(email, password);
    }

}
