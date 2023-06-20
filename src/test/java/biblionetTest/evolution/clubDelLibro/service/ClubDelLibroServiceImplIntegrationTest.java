package biblionetTest.evolution.clubDelLibro.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import it.unisa.c07.biblionet.BiblionetApplication;
import it.unisa.c07.biblionet.gestioneclubdellibro.ClubDelLibroService;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.ClubDelLibro;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.ClubDelLibroDAO;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Esperto;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.EspertoDAO;
import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringRunner;


import lombok.Getter;
import lombok.Setter;

/**
 * Implementa l'integration testing del service per il sottosistema
 * ClubDelLibro.
 * @author Luca Topo
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BiblionetApplication.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class ClubDelLibroServiceImplIntegrationTest implements
                                                    ApplicationContextAware {

    @Autowired
    @Setter
    @Getter
    private ApplicationContext applicationContext;

    @Autowired
    private ClubDelLibroDAO clubDAO;

    @Autowired
    private EspertoDAO espertoDAO;

    @Autowired
    private ClubDelLibroService clubService;

    @BeforeEach
    public void init() {
        BiblionetApplication.init(applicationContext);
    }

    /**
     * Verifica il corretto funzionamento della funzionalità di creazione
     * di un Club del Libro.
     */
    @Test
    public void creaClubDelLibroValido() {
        var esperto = (Esperto) espertoDAO.findById("eliaviviani@gmail.com")
                                          .get();

        var c = new ClubDelLibro(
            "Test",
            "ClubDiProva",
            esperto
        );

        var club = this.clubService.creaClubDelLibro(c);

        Assertions.assertNotEquals(club, null);
        Assert.assertEquals(club.getNome(), c.getNome());
        Assert.assertEquals(club.getDescrizione(), c.getDescrizione());
        Assert.assertEquals(club.getEsperto(), c.getEsperto());

        var clubInDB = this.clubDAO.findById(club.getIdClub());

        assertFalse(clubInDB.isEmpty());
        assertEquals(c.getNome(), clubInDB.get().getNome());
        assertEquals(c.getDescrizione(), clubInDB.get().getDescrizione());
        assertEquals(
            c.getEsperto().getEmail(),
            clubInDB.get().getEsperto().getEmail()
        );

    }

    /**
     * Verifica il corretto funzionamento della funzionalità di modifica
     * di un Club del Libro.
     */
    @Test
    public void modificaClubDelLibroValido() {
        var clubBase = clubDAO.findAll().get(0);
        var id = clubBase.getIdClub();

        var club = clubDAO.findById(id);

        club.get().setNome("Nuovo nome");

        var clubModificato = this.clubService.modificaDatiClub(club.get());

        var clubInDB = this.clubDAO.findById(id);

        assertTrue(clubInDB.isPresent());
        assertNotEquals(clubBase, clubInDB.get());
        Assert.assertEquals(
            clubModificato.getDescrizione(),
            clubInDB.get().getDescrizione()
        );
    }

}