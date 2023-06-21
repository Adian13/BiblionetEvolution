package it.unisa.c07.biblionet.gestionebiblioteca.controller;

import it.unisa.c07.biblionet.common.Libro;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.LibroDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/libri")
public class LibroController {
    @Autowired
    private LibroDAO libroRepository;

    @GetMapping("/{id}")
    public ResponseEntity<Libro> getLibro(@PathVariable("id") String id) {
        Optional<Libro> optionalLibro = libroRepository.findById(id);
        return optionalLibro.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/")
    public ResponseEntity<Libro> createLibroBase(@RequestBody Libro libro) {
        Libro nuovoLibro = libroRepository.save(libro);
        return ResponseEntity.ok(nuovoLibro);
    }
    @PostMapping("/")
    public ResponseEntity<Libro> createLibro(@RequestParam("titolo") String titolo,
                                             @RequestParam("autore") String autore,
                                             @RequestParam("isbn") String isbn,
                                             @RequestParam("annoDiPubblicazione") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime annoDiPubblicazione,
                                             @RequestParam("descrizione") String descrizione,
                                             @RequestParam("casaEditrice") String casaEditrice,
                                             @RequestParam("generi") Set<String> generi,
                                             @RequestParam("file") MultipartFile copertina) {
        try {
            Libro libro = new Libro();
            libro.setTitolo(titolo);
            libro.setAutore(autore);
            libro.setIsbn(isbn);
            libro.setAnnoDiPubblicazione(annoDiPubblicazione);
            libro.setDescrizione(descrizione);
            libro.setCasaEditrice(casaEditrice);
            libro.setGeneri(generi);

            if (!copertina.isEmpty()) {
                byte[] datiCopertina = copertina.getBytes();
                libro.setImmagineLibro(datiCopertina);
            }

            Libro nuovoLibro = libroRepository.save(libro);
            return ResponseEntity.ok(nuovoLibro);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Libro> updateLibro(@PathVariable("id") int id, @RequestBody Libro libro) {
        if (libroRepository.existsById(id+"")) {
            libro.setIdLibro(id);
            Libro libroAggiornato = libroRepository.save(libro);
            return ResponseEntity.ok(libroAggiornato);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLibro(@PathVariable("id") int id) {
        if (libroRepository.existsById(id+"")) {
            libroRepository.deleteById(id+"");
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
