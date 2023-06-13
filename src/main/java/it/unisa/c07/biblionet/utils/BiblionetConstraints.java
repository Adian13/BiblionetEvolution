package it.unisa.c07.biblionet.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * Implementa la funzionalità di verifica che una stringa
 * rispetti una regex.
 */
public class BiblionetConstraints {
    private BiblionetConstraints() {}

    /**
     * Lunghezza minima del nome dell'evento.
     */
    public static final int LUNGHEZZA_MINIMA_NOME = 1;
    /**
     * Lunghezza massima del nome di un evento.
     */
    public static final int LUNGHEZZA_MASSIMA_NOME = 30;

    /**
     * Lunghezza massima della descrizione di un evento.
     */
    public static final int LUNGHEZZA_MASSIMA_DESCRIZIONE = 255;
    /**
     * Lunghezza minima della descrizione di un evento.
     */
    public static final int LUNGHEZZA_MINIMA_DESCRIZIONE = 0;
    /**
     * Lunghezza 10.
     */
    public static final int LENGTH_10 = 10;

    /**
     * Lunghezza 13.
     */
    public static final int LENGTH_13 = 13;

    /**
     * Lunghezza 30.
     */
    public static final int LENGTH_30 = 30;

    /**
     * Lunghezza 32.
     */
    public static final int LENGTH_32 = 32;

    /**
     * Lunghezza 60.
     */
    public static final int LENGTH_60 = 60;

    /**
     * Lunghezza 90.
     */
    public static final int LENGTH_90 = 90;

    /**
     * Lunghezza 255.
     */
    public static final int LENGTH_255 = 255;

    /**
     * Lunghezza 144.
     */
    public static final int LENGTH_144 = 144;

    /**
     * Lunghezza 320.
     */
    public static final int LENGTH_320 = 320;
    public static final String NAME_REGEX = "^[A-zÀ-ù ‘-]{2,60}$";
    public static final String PHONE_REGEX = "^\\d{10}$";
    public static final String ADDRESS_REGEX = "^[0-9A-zÀ-ù ‘-]{2,30}$";


    public static boolean passwordRispettaVincoli(byte[] passwordUtente, String password){
        if(password.length() <= 7) return false;
        return Arrays.equals(passwordUtente, trasformaPassword(password));
    }
    public static boolean confrontoPassword(String nuova, String conferma){
        if (!nuova.isEmpty() && !conferma.isEmpty()) {
            if (nuova.length() <= 7)
                return false;
            return nuova.equals(conferma);
        }
        return false;
    }

    public static byte[] trasformaPassword(String password){
        try {
            MessageDigest md;
            md = MessageDigest.getInstance("SHA-256");
            return md.digest(password.getBytes());

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }


}
