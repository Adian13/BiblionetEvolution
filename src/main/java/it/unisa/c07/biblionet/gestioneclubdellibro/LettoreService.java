package it.unisa.c07.biblionet.gestioneclubdellibro;

import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Lettore;
import org.springframework.stereotype.Service;

@Service
public interface LettoreService {

    Lettore save(Lettore lettore);
    Lettore findByEmail(String email);

}
