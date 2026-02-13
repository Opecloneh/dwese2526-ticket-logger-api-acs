package org.iesalixar.daw2.acs.dwese2526_ticket_logger_api_acs.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.security.KeyPair;

import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Autowired
    private KeyPair jwtKeyPair;

    private static final long JWT_EXPIRATION = 3_600_000L; // 1 hora

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(jwtKeyPair.getPublic())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String generateToken(String username, List<String> roles) {
        return Jwts.builder()
                .subject(username) // Configura claim "sub" (nombre usuario)
                .claim("roles", roles) // Incluye los roles como claim adiccional
                .issuedAt(new Date())// Fecha de emision del token
                .expiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION)) // Expira en 1 hora
                .signWith(jwtKeyPair.getPrivate(), Jwts.SIG.RS256) // Firma el token con la clave secreta
                .compact(); // Genera el token en formato JWT
    }

    public boolean validateToken(String token, String username) {
        try {
            // 1) Parseo del token + verificacion de firma con la PUBLIC KEY del KeyPair
            Claims claims = Jwts.parser()
                    .verifyWith(jwtKeyPair.getPublic()) // Valida firma RSA
                    .build()
                    .parseSignedClaims(token) // Token firmado (JWS)
                    .getPayload();  // Claims ya verificados

            // 2) Comprobacion del subject (sub)
            String tokenUsername = claims.getSubject();
            if (tokenUsername == null || !tokenUsername.equals(username)) {
                return false;
            }

            // 3) Comprobacion de expiracion (exp)
            Date exp = claims.getExpiration();
            return exp != null && exp.after(new Date());
        } catch (Exception e) {
            // Firma invalida, token expirado, token manipulado, etc
            return false;
        }
    }

    public boolean isTokenExpired(Claims claims){
        return claims.getExpiration().before(new Date());
    }
}
