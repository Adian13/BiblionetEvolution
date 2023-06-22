package biblionetTest.evolution.preferenzeDiLettura.service;

import it.unisa.c07.biblionet.BiblionetApplication;
import it.unisa.c07.biblionet.gestioneclubdellibro.PreferenzeDiLetturaService;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Esperto;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Genere;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.GenereDAO;
import it.unisa.c07.biblionet.gestioneutenti.AutenticazioneService;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Implementa l'integration testing del service per il sottosistema
 * Preferenze Di Lettura.
 * @author Antonio Della Porta
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BiblionetApplication.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class PreferenzeDiLetturaServiceImplIntegrationTest {

    @Autowired
    @Setter
    @Getter
    private ApplicationContext applicationContext;

    @Autowired
    private PreferenzeDiLetturaService preferenzeDiLetturaService;

    @Autowired
    private GenereDAO genereDAO;

    @BeforeEach
    public void init() {
        BiblionetApplication.init(applicationContext);
    }

    /**
     * Metodo che si occupa di testare
     * la funzione di ricerca di un
     * genere facendo una iterazione nel service.
     *
    @Test
    public void getGeneriByName1IT() {
        List<Genere> generiDB = new ArrayList<>();
        generiDB.add(genereDAO.findByName("Fantasy"));
        String[] generi ={"Fantasy"};
        assertEquals(generiDB, preferenzeDiLetturaService.getGeneriByName(generi));
    }*/

}