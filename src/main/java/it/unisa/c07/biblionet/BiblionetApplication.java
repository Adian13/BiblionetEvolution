package it.unisa.c07.biblionet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Questa è la main class del progetto, che fa partire l'applicazione e popola
 * il database.
 */
@SpringBootApplication
public class BiblionetApplication {
    public static void main(String[] args) {
        SpringApplication.run(BiblionetApplication.class, args);
    }
}
