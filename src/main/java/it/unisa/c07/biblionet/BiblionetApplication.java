package it.unisa.c07.biblionet;

import it.unisa.c07.biblionet.gestionebiblioteca.bookapiadapter.BookApiAdapter;
import it.unisa.c07.biblionet.gestionebiblioteca.bookapiadapter.GoogleBookApiAdapterImpl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.io.File;
import java.io.IOException;
import java.util.Base64;

/**
 * Questa è la main class del progetto, che fa partire l'applicazione e popola
 * il database.
 */
@SpringBootApplication
public class BiblionetApplication {

    private static BookApiAdapter bookApiAdapter = new GoogleBookApiAdapterImpl();

/*
    public static Libro getLibroFromAPI(String isbn, Genere... generi) {
        Libro libro = bookApiAdapter.getLibroDaBookApi(isbn);
        if (libro == null) {
            libro = new Libro("Not Found", "Autore", isbn,
                    LocalDateTime.now(), "Descrizione", "Casa Editrice");
        }
        //libro.setGeneri(Arrays.asList(generi));

        return libroDAO.save(libro);
    }
   */

    public static void main(String[] args) {

                SpringApplication.run(BiblionetApplication.class, args);

        //init(configurableApplicationContext);
    }

    public static void init(ApplicationContext applicationContext) {
    }

   /* public static String getCopertinaClubFromUrl(String filePath) {
        try{
            byte[] fileContent = FileUtils.readFileToByteArray(new File(filePath));
            return Base64.getEncoder().encodeToString(fileContent);
        }
        catch (IOException ex){
            ex.printStackTrace();
            return "noImage";
        }

    }*/
}
