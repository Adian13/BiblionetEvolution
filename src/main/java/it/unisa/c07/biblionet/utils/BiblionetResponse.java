package it.unisa.c07.biblionet.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class BiblionetResponse {
    public static final String NON_AUTORIZZATO = "Non sei autorizzato";
    public static final String FORMATO_NON_VALIDO = "Formato dati non valido";
    public static final String OGGETTO_NON_TROVATO = "Oggetto non trovato";
    public static final String RICHIESTA_NON_VALIDA = "Richiesta non valida";
    public static final String ISCRIZIONE_OK = "Iscrizione effettuata";
    public static final String ISCRIZIONE_FALLITA = "Iscrizione fallita";
    public static final String OPERAZIONE_OK = "Operazione effettuata con successo";
    public static final String ERRORE = "Errore!";


    private Map<String, String> payload;
    private boolean isStatusOk;

    public BiblionetResponse(String value, boolean isStatusOk) {
        this.isStatusOk = isStatusOk;
        this.payload = new HashMap<>();
        payload.put("descrizione", value);
    }

}