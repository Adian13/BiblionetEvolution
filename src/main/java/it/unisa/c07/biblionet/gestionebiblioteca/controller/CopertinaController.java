package it.unisa.c07.biblionet.gestionebiblioteca.controller;

import it.unisa.c07.biblionet.common.Copertina;
import it.unisa.c07.biblionet.gestionebiblioteca.repository.CopertinaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api/immagini")

public class CopertinaController {
    @Autowired
        private CopertinaRepository immagineRepository;

        @PostMapping("/copertina/caricafile")
        public ResponseEntity<?> caricaImmagine(@RequestParam("file") MultipartFile file) {
            try {
                Copertina immagine = new Copertina();
                immagine.setDati(file.getBytes());
                immagineRepository.save(immagine);
                return ResponseEntity.ok().build();
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
    @GetMapping("/copertina/{id}")
    public ResponseEntity<byte[]> getImmagine(@PathVariable("id") Long id) {
        Optional<Copertina> optionalImmagine = immagineRepository.findById(id);
        if (optionalImmagine.isPresent()) {
            Copertina immagine = optionalImmagine.get();
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(immagine.getDati());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteImmagine(@PathVariable("id") Long id) {
        if (immagineRepository.existsById(id)) {
            immagineRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
