package it.unisa.c07.biblionet.gestioneautenticazione.repository;


import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Questa classe rappresenta il DAO di un Utente Registrato, usato
 * per estendere gli attori core del sistema.
 */
//@NoRepositoryBean
public interface UtenteRegistratoDAO
        extends JpaRepository<UtenteRegistrato, String> {

    //todo sola lettura
    UtenteRegistrato findUtenteRegistratoByEmailAndPassword(String email, byte[] arr);
}