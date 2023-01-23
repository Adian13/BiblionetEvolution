package it.unisa.c07.biblionet.evolution.prenotazioneLibri.service;

import it.unisa.c07.biblionet.evolution.BiblionetApplication;
import it.unisa.c07.biblionet.evolution.model.entity.utente.Biblioteca;
import it.unisa.c07.biblionet.evolution.model.dao.GenereDAO;
import it.unisa.c07.biblionet.evolution.model.dao.LibroDAO;
import it.unisa.c07.biblionet.evolution.model.dao.utente.BibliotecaDAO;
import it.unisa.c07.biblionet.evolution.model.dao.utente.LettoreDAO;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Implementa l'integration testing del service per il sottosistema
 * Prenotazione Libri.
 * @author Antonio Della Porta
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BiblionetApplication.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class PrenotazioneLibriServiceIntegrationTest {

    @Autowired
    @Setter
    @Getter
    private ApplicationContext applicationContext;

    @Autowired
    private PrenotazioneLibriService prenotazioneLibriService;

    @Autowired
    private GenereDAO genereDAO;

    @Autowired
    private LibroDAO libroDAO;

    @Autowired
    private LettoreDAO lettoreDAO;

    @Autowired
    private BibliotecaDAO bibliotecaDAO;

    @BeforeEach
    public void init() {
        BiblionetApplication.init(applicationContext);
    }

    @Test
    public void getBiblioteca(){
        Biblioteca biblioteca = bibliotecaDAO.findByID("aldomoronocera@gmail.com");
        Assertions.assertEquals(biblioteca.getNomeBiblioteca(),prenotazioneLibriService.getBibliotecaById("aldomoronocera@gmail.com").getNomeBiblioteca());
    }


}