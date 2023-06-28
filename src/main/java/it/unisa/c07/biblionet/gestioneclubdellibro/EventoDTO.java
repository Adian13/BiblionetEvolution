package it.unisa.c07.biblionet.gestioneclubdellibro;

import java.time.LocalDate;
import java.time.LocalTime;

import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Evento;
import it.unisa.c07.biblionet.utils.BiblionetConstraints;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Size;

/**
 * Questa classe rappresenta un form
 * contenente i dati di un evento.
 * @author Nicola Pagliara
 * @author Luca Topo
 */
@Data
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
public class EventoDTO {

    /**
     * Il nome dell'evento.
     */
    @NonNull
    @Size(min = BiblionetConstraints.LUNGHEZZA_MINIMA_NOME, max = BiblionetConstraints.LUNGHEZZA_MASSIMA_NOME)
    private String nome;

    /**
     * Una descrizione dell'evento.
     */
    @NonNull
    @Size(min = BiblionetConstraints.LUNGHEZZA_MINIMA_DESCRIZIONE, max = BiblionetConstraints.LUNGHEZZA_MASSIMA_DESCRIZIONE)
    private String descrizione;

    /**
     * La data dell'evento.
     */
    @NonNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate data;

    /**
     * L'ora dell'evento.
     */
    @NonNull
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime ora;

    /**
     * Il libro associato all'evento.
     */
    private Integer libro;

    public EventoDTO(Evento evento){
        this.data = LocalDate.from(evento.getDataOra());
        this.ora = LocalTime.from(evento.getDataOra());
        this.nome = evento.getNomeEvento();
        if(evento.getLibro() != null)
            this.libro = evento.getLibro().getIdLibro();
        this.descrizione = evento.getDescrizione();
    }
}