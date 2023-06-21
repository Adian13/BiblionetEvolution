package it.unisa.c07.biblionet.gestioneclubdellibro.repository;

import it.unisa.c07.biblionet.common.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibroDAO extends JpaRepository<Libro, String> {

    Libro save(Libro libro);

}
