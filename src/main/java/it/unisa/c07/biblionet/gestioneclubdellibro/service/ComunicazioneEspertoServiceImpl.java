package it.unisa.c07.biblionet.gestioneclubdellibro.service;


import it.unisa.c07.biblionet.gestioneclubdellibro.ClubDelLibroService;
import it.unisa.c07.biblionet.gestioneclubdellibro.ComunicazioneEspertoService;
import it.unisa.c07.biblionet.gestioneclubdellibro.EspertoService;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Esperto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Alessio Casolaro
 * @author Antonio Della Porta
 */
@Service
@RequiredArgsConstructor
public class ComunicazioneEspertoServiceImpl
                                    implements ComunicazioneEspertoService {

    private final EspertoService espertoService;

    /**
     * Implementa la funzionalità che permette
     * di visualizzare la lista completa dei libri
     * prenotabili di un dato genere.
     * @param genere Il nome del genere
     * @return La lista di libri
     *
     * todo testo inesatto
     */
    @Override
    public List<Esperto> visualizzaEspertiPerGenere(
            final String genere) {
        List<Esperto> list = espertoService.findAllEsperti();
        List<Esperto> list2 = new ArrayList<>();
        for (Esperto e : list) {
            if (e.getGeneri().contains(this.primaLetteraMaiuscola(genere))) {
                list2.add(e);
            }
        }
        return list2;
    }
    private String primaLetteraMaiuscola(String stringa){
        return stringa.substring(0, 1).toUpperCase() + stringa.substring(1);
    }
}
