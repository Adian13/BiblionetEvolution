package it.unisa.c07.biblionet.gestioneclubdellibro;

import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Lettore;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.LettoreDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LettoreServiceImpl implements LettoreService {
    @Autowired
    LettoreDAO lettoreDAO;
    @Override
    public Lettore save(Lettore lettore) {
        return lettoreDAO.save(lettore);
    }

    @Override
    public Lettore findByEmail(String email) {
        return lettoreDAO.findByEmail(email);
    }

}
