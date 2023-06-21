package it.unisa.c07.biblionet.gestionebiblioteca.repository;

import it.unisa.c07.biblionet.common.Copertina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CopertinaRepository extends JpaRepository<Copertina, Long> {
}
