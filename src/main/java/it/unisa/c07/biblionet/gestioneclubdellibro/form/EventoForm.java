package it.unisa.c07.biblionet.gestioneclubdellibro.form;

import java.time.LocalDate;
import java.time.LocalTime;

import it.unisa.c07.biblionet.utils.ValidazioneEvento;
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
public class EventoForm {

    /**
     * Il nome dell'evento.
     */
    @NonNull
    @Size(min = ValidazioneEvento.LUNGHEZZA_MINIMA_NOME, max = ValidazioneEvento.LUNGHEZZA_MASSIMA_NOME)
    private String nome;

    /**
     * Una descrizione dell'evento.
     */
    @NonNull
    @Size(min = ValidazioneEvento.LUNGHEZZA_MINIMA_DESCRIZIONE, max = ValidazioneEvento.LUNGHEZZA_MASSIMA_DESCRIZIONE)
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
}
