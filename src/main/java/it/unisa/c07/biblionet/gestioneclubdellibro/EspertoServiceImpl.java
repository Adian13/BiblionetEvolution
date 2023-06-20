package it.unisa.c07.biblionet.gestioneclubdellibro;

import it.unisa.c07.biblionet.common.UtenteRegistrato;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Esperto;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.EspertoDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
@Service
public class EspertoServiceImpl implements EspertoService {

@Autowired
private EspertoDAO espertoRepository;

    @Override
    public Esperto findByEmail(final String email) {
        return espertoRepository.findByEmail(email);
    }

    @Override
    public final List<Esperto> findEspertiByGeneri(final Set<String> generi) {
        List<Esperto> toReturn = new ArrayList<>();
        Boolean b = espertoRepository.findAll().stream()
                .filter(esperto -> generi.contains(esperto.getGeneri()))
                .peek(toReturn::add)
                .findFirst()
                .isPresent();

        return toReturn;
    }
    @Override
    public UtenteRegistrato aggiornaEspertoDaModel(EspertoDTO form, UtenteRegistrato biblioteca) {
        if(biblioteca == null) biblioteca = findByEmail(form.getEmail()).getBiblioteca(); //todo ok, e se cambio anche la mail?
        return espertoRepository.save(new Esperto(form, biblioteca));
    }
    public UtenteRegistrato creaEspertoDaModel(EspertoDTO form, UtenteRegistrato biblioteca) {
        return espertoRepository.save(new Esperto(form, biblioteca));
    }

    @Override
    public List<Esperto> findAll() {
       return espertoRepository.findAll();
    }


}
