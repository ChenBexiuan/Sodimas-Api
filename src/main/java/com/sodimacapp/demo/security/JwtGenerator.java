package com.sodimacapp.demo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtGenerator {

    private static String staticJwtSecret;
    private static long staticJwtExpirationMs;
    private static Key signingKey;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration.ms}")
    private long jwtExpirationMs;

    // Este método se ejecuta una vez que Spring ha creado el componente.
    @PostConstruct
    public void init() {
        staticJwtSecret = jwtSecret;
        staticJwtExpirationMs = jwtExpirationMs;
        signingKey = Keys.hmacShaKeyFor(staticJwtSecret.getBytes(StandardCharsets.UTF_8));
        
        // --- DEBUG: Imprime el hash code de la clave cuando se inicializa ---
        System.out.println(">>> JwtGenerator inicializado. Hash de la clave: " + signingKey.hashCode());
    }

    // Genera un JWT para un usuario autenticado
    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + staticJwtExpirationMs);

        // --- DEBUG: Imprime el hash code de la clave al generar el token ---
        System.out.println(">>> GENERANDO TOKEN. Usando clave con hash: " + signingKey.hashCode());

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(signingKey, SignatureAlgorithm.HS512)
                .compact();
    }

    // Extrae el username (email) del token JWT
    public String getUsernameFromJwt(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    // Valida si un token JWT es válido
    public boolean validateToken(String token) {
        try {
            // --- DEBUG: Imprime el hash code de la clave al validar el token ---
            System.out.println(">>> VALIDANDO TOKEN. Usando clave con hash: " + signingKey.hashCode());
            
            Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            System.err.println("Error de validación de JWT: " + e.getMessage());
            return false;
        }
    }
}
