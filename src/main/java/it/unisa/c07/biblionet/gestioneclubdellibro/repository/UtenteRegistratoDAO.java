package it.unisa.c07.biblionet.gestioneclubdellibro.repository;

import it.unisa.c07.biblionet.common.UtenteRegistrato;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UtenteRegistratoDAO extends JpaRepository<UtenteRegistrato, String> {

   UtenteRegistrato findByEmailAndPassword(String email, byte[] password);
}
