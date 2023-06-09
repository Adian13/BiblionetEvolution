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
    private Map<String, String> payload;
    private boolean isStatusOk;


    public BiblionetResponse(String value, boolean isStatusOk) {
        this.isStatusOk = isStatusOk;
        this.payload = new HashMap<>();
        payload.put("testo", value);
    }
}