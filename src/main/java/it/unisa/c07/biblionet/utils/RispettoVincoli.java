package it.unisa.c07.biblionet.utils;

import it.unisa.c07.biblionet.common.UtenteRegistrato;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * Implementa la funzionalità di verifica che una stringa
 * rispetti una regex.
 */
public class RispettoVincoli {
    private RispettoVincoli() {}
    public static final String NAME_REGEX = "^[A-zÀ-ù ‘-]{2,60}$";
    public static final String PHONE_REGEX = "^\\d{10}$";
    public static final String ADDRESS_REGEX = "^[0-9A-zÀ-ù ‘-]{2,30}$";


    public static boolean passwordRispettaVincoli(byte[] passwordUtente, String password){
        if(password.length() <= 7) return false;
        System.out.println(trasformaPassword(password));
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
            byte[] arr = md.digest(password.getBytes());
            return arr;

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }


}
