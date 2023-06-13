package it.unisa.c07.biblionet.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.impl.DefaultJwtParser;

public class Utils {
    private Utils(){}
    public static final String CHIAVE_SEGRETA = "pretzel"; //todo deve sparire da qua
    public static Claims getClaimsFromTokenWithoutKey(String token){
        token = token.substring(7);
        String[] splitToken = token.split("\\.");
        String unsignedToken = splitToken[0] + "." + splitToken[1] + ".";

        DefaultJwtParser parser = new DefaultJwtParser();
        Jwt<?, ?> jwt = parser.parse(unsignedToken);
        return (Claims) jwt.getBody();
    }

    public static boolean isUtenteBiblioteca(String token){
        if(token == null) return false;
        return "Biblioteca".equalsIgnoreCase((String) getClaimsFromTokenWithoutKey(token).get("role"));
    }
    public static boolean isUtenteLettore(String token){
        if(token == null) return false;
        return "Lettore".equalsIgnoreCase((String) getClaimsFromTokenWithoutKey(token).get("role"));
    }
    public static boolean isUtenteEsperto(String token){
        if(token == null) return false;
        return "Esperto".equalsIgnoreCase((String) getClaimsFromTokenWithoutKey(token).get("role"));
    }
    public static String getSubjectFromToken(String token){
        if(token == null) return null;
        return getClaimsFromTokenWithoutKey(token).getSubject();
    }
}
