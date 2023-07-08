package it.unisa.c07.biblionet.gestionebiblioteca.service;
import it.unisa.c07.biblionet.common.UtenteRegistrato;
import it.unisa.c07.biblionet.common.UtenteRegistratoDTO;
import it.unisa.c07.biblionet.gestionebiblioteca.BibliotecaDTO;
import it.unisa.c07.biblionet.gestionebiblioteca.repository.Biblioteca;
import it.unisa.c07.biblionet.gestionebiblioteca.repository.BibliotecaDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Service
@Transactional
public class BibliotecaService {
    private BibliotecaDAO bibliotecaDAO;
    private EntityManager entityManager;

    private BibliotecaMapper bibliotecaMapper;

    @Autowired
    public BibliotecaService(BibliotecaDAO bibliotecaDAO, BibliotecaMapper bibliotecaMapper) {
        this.bibliotecaDAO = bibliotecaDAO;
        this.bibliotecaMapper = bibliotecaMapper;
    }

    public BibliotecaDTO getBibliotecaByEmail(String email) {
        Biblioteca biblioteca = bibliotecaDAO.findByEmail(email);
        return bibliotecaMapper.entityToDTO(biblioteca);
    }

    public Biblioteca save(BibliotecaDTO bibliotecaDTO) {
        Biblioteca biblioteca = bibliotecaMapper.dtoToEntity(bibliotecaDTO);
        return bibliotecaDAO.save(biblioteca);
    }

    public void deleteBibliotecaByEmail(String email) {
        bibliotecaDAO.deleteByEmail(email);
    }

    public Biblioteca findByEmail(String email) {
        return entityManager.createQuery("FROM Biblioteca WHERE email = :email", Biblioteca.class)
                .setParameter("email", email)
                .getSingleResult();
    }

    public List<UtenteRegistrato> findAllUtentiRegistrati() {
        return entityManager.createQuery("FROM UtenteRegistrato", UtenteRegistrato.class)
                .getResultList();
    }

    public BibliotecaDTO update(BibliotecaDTO bibliotecaDTO){
        // Converte l'oggetto DTO in un'entità Biblioteca
        Biblioteca biblioteca = bibliotecaDAO.findByEmail(bibliotecaDTO.getEmail());
        return bibliotecaMapper.entityToDTO(biblioteca);
    }

    public void deleteByEmail(String email) {
        Biblioteca biblioteca = findByEmail(email);
        if (biblioteca != null) {
            entityManager.remove(biblioteca);
        }
    }
    /**
     * Implementa la funzionalità di trovare una biblioteca.
     *
     * @param email La mail della biblioteca
     * @return La biblioteca se c'è, altrimenti null
     */
    public final Biblioteca findBibliotecaByEmail(final String email) {
        return bibliotecaDAO.findBibliotecaByEmail(email, "Biblioteca"); //todo usare una costante
    }

    public List<Biblioteca> findByNome(String nomeBiblioteca) {
        return bibliotecaDAO.findByNome(nomeBiblioteca);
    }

    public List<Biblioteca> findByCitta(String citta) {
        return bibliotecaDAO.findByCitta(citta);
    }

    public List<Biblioteca> findAll() {
        return bibliotecaDAO.findAllBiblioteche();
    }

    /**
     * Implementa la funzionalità di salvataggio delle modifiche
     * all'account biblioteca.
     *
     * @param utente La biblioteca da aggiornare
     * @return la biblioteca aggiornata
     */
    public Biblioteca aggiornaBiblioteca(final Biblioteca utente) {
        return bibliotecaDAO.save(utente);
    }

    public List<Biblioteca> findAllBiblioteche() {
        return bibliotecaDAO.findAll();
    }
}