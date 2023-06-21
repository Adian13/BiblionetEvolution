package it.unisa.c07.biblionet.gestioneclubdellibro;

import it.unisa.c07.biblionet.common.UtenteRegistrato;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EspertoServiceImpl implements EspertoService {

@Autowired
private EspertoDAO espertoRepository;
@Autowired
private GenereDAO genereDAO;
    @Override
    public Esperto findByEmail(final String email) {
        return espertoRepository.findByEmail(email);
    }

    @Override
    public final List<Esperto> findEspertiByGeneri(final Set<String> generi) {
        return  espertoRepository.findAll().stream()
                .filter(esperto -> generi.containsAll(esperto.getGenereSet())).collect(Collectors.toList());
    }
    @Override
    public UtenteRegistrato aggiornaEspertoDaModel(EspertoDTO form, UtenteRegistrato biblioteca) {
        if(biblioteca == null) biblioteca = findByEmail(form.getEmail()).getBiblioteca(); //todo ok, e se cambio anche la mail?
        return espertoRepository.save(createEsperto(form, biblioteca));
    }
    public UtenteRegistrato creaEspertoDaModel(EspertoDTO form, UtenteRegistrato biblioteca) {
        return espertoRepository.save(createEsperto(form, biblioteca));
    }

    @Override
    public List<Esperto> findAll() {
       return espertoRepository.findAll();
    }

    @Override
    public Esperto save(Esperto esperto) {
       return espertoRepository.save(esperto);
    }

    public Esperto aggiornaEsperto(EspertoDTO espertoDTO){
        Esperto esperto =espertoRepository.findByEmail(espertoDTO.getEmail());
        esperto.setCitta(espertoDTO.getCitta());
        esperto.setEmail(espertoDTO.getEmail());
        esperto.setVia(espertoDTO.getVia());
        esperto.setGenereSet(setGeneri(espertoDTO.getGeneri()));
        esperto.setNome(espertoDTO.getNome());
        esperto.setCognome(espertoDTO.getCognome());
        return esperto;

    }

    private Set<Genere> setGeneri(Set<String> generi) {
        Set<Genere> genereSet= null;
        for (String s : generi) {
            genereSet.add(genereDAO.findByName(GenereS.valueOf(s).toString()));
        }
        return genereSet;
    }
    public Esperto createEsperto(EspertoDTO dto, UtenteRegistrato biblioteca) {
        Esperto e = new Esperto();
        e.setEmail(dto.getEmail());
        e.setPassword(dto.getPassword().toString());
        e.setProvincia(dto.getProvincia());
        e.setCitta(dto.getCitta());
        e.setVia(dto.getVia());
        e.setRecapitoTelefonico(dto.getRecapitoTelefonico());
        e.setTipo("Esperto");
        e.setUsername(dto.getUsername());
        e.setNome(dto.getNome());
        e.setCognome(dto.getCognome());
        e.setBiblioteca(biblioteca);
        e.setGenereSet(setGeneri(dto.getGeneri()));
        return e;
    }
}
