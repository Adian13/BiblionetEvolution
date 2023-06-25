package it.unisa.c07.biblionet.gestioneclubdellibro;

import java.util.Set;

import it.unisa.c07.biblionet.gestioneclubdellibro.repository.ClubDelLibro;
import org.springframework.web.multipart.MultipartFile;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Classe che rappresenta il form per la creazione di un club del libro.
 */
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class ClubDTO {

    /**
     * Nome del club.
     */
    @NonNull
    private String nome;

    /**
     * Descrizione del club.
     */
    @NonNull
    private String descrizione;

    /**
     * Lista di generi del club.
     */
    @NonNull
    private Set<String> generi;

    /**
     * Copertina del club.
     */

    private MultipartFile copertina;

    public ClubDTO(ClubDelLibro clubDelLibro){
        this.nome = clubDelLibro.getNome();
        this.descrizione = clubDelLibro.getDescrizione();
        this.generi = clubDelLibro.getGeneri();
        this.copertina = null;
    }
}
