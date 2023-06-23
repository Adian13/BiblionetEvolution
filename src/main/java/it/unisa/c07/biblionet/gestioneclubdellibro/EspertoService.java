package it.unisa.c07.biblionet.gestioneclubdellibro;

import it.unisa.c07.biblionet.common.UtenteRegistrato;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.ClubDelLibro;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Esperto;

import java.util.List;
import java.util.Set;

public interface EspertoService {

    Esperto creaEspertoDaModel(EspertoDTO form, String emailBiblioteca);

    Esperto aggiornaEspertoDaModel(EspertoDTO form, String biblioteca);

    Esperto aggiornaEsperto(Esperto utente);

    List<Esperto> findEspertiByGeneri(Set<String> generi);


    List<Esperto> getEspertiByBiblioteca(String email);

    List<String> getEspertiEmailByBiblioteca(String email);

    Esperto findEspertoByEmail(String email);

    List<Esperto> findAllEsperti();

    List<Esperto> findEspertiByNome(String nome);

    UtenteRegistrato findEspertoByEmailAndPassword(String email, byte[] password);
}
