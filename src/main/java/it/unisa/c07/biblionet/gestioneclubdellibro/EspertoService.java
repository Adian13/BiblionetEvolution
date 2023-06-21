package it.unisa.c07.biblionet.gestioneclubdellibro;

import it.unisa.c07.biblionet.common.UtenteRegistrato;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Esperto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
@Service
public interface EspertoService {
    Esperto findByEmail(String email);
    Esperto aggiornaEsperto(EspertoDTO espertoDTO);
    List<Esperto> findEspertiByGeneri(Set<String> generi);

    UtenteRegistrato aggiornaEspertoDaModel(EspertoDTO form, UtenteRegistrato biblioteca);
    UtenteRegistrato creaEspertoDaModel(EspertoDTO form, UtenteRegistrato biblioteca);

    List<Esperto> findAll();

    Esperto save(Esperto esperto);
}
