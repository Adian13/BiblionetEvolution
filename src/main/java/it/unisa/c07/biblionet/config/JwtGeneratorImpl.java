package it.unisa.c07.biblionet.config;

import io.jsonwebtoken.SignatureAlgorithm;
import it.unisa.c07.biblionet.gestioneautenticazione.repository.UtenteRegistrato;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import io.jsonwebtoken.Jwts;


@Service
public class JwtGeneratorImpl implements JwtGeneratorInterface{

    @Value("${jwt.secret}")
    private String secret;

    @Value("${app.jwttoken.message}")
    private String message;

    @Override
    public Map<String, String> generateToken(UtenteRegistrato user) {
        String jwtToken="";
        jwtToken = Jwts.builder().setSubject(user.getEmail()).claim("role",
                user.getTipo()).setIssuedAt(new Date()).signWith(SignatureAlgorithm.HS256, secret).compact();
        Map<String, String> jwtTokenGen = new HashMap<>();
        jwtTokenGen.put("token", jwtToken);
        jwtTokenGen.put("message", message);
        return jwtTokenGen;
    }
}
