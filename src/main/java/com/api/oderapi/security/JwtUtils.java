package com.api.oderapi.security;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

// import com.api.oderapi.exceptions.ValidateServiceException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.SignatureException;

import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtUtils {

    @Value("${jwt.password}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private String timeExpiration;

    // GENERAR TOKEN
    public String generateAccesstoken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + Long.parseLong(timeExpiration));

        return Jwts.builder() // BUILD GENERA EL TOKEN
                .setSubject(username) // SE MANDA EL NOMBRE DEL SUJETO A QUIEN SE EMITE EL TOKEN
                .setIssuedAt(now) // FECHA DE EMISION
                .setExpiration(expiryDate) // FECHA DE EXPIRACION
                .signWith(getSignatureKey(), SignatureAlgorithm.HS256)// ENCRIPT CONTRASEñA(ALGORITMO Y LLAVE)
                .compact();
    }

    // GENERA KEY DEL TOKEN
    public Key getSignatureKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // VALIDAR TOKEN CORRECTO
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()// decodifica el token
                    .setSigningKey(getSignatureKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            // Jwts.parserBuilder().setSigningKey(token).parseClaimsJws(token);//deprecated
            return true;

        } catch (UnsupportedJwtException e) {
            log.error("JWT in a particular format/configuration that does not match the format expected");
        } catch (MalformedJwtException e) {
            log.error(" JWT was not correctly constructed and should be rejected");
        } catch (SignatureException e) {
            log.error("Signature or verifying an existing signature of a JWT failed");
        } catch (ExpiredJwtException e) {
            log.error("JWT was accepted after it expired and must be rejected");
        }
        // catch (IllegalArgumentException e) {
        // log.error("Signature or verifying an existing signature of a JWT failed");
        // }
        return false;
    }

    // OBTENER EL PAYLOAD
    public Claims extractAllClaims(String token) {
        Jwts.parserBuilder()
                .setSigningKey(getSignatureKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return null;
    }

    // OBTENER UN DATO DEL PAYLOAD (extractAllClaims)
    public <T> T getClaim(String token, Function<Claims, T> claimFunction) {
        Claims claims = extractAllClaims(token);
        return claimFunction.apply(claims);
    }

    // FUNCION QUE OBTIENE EL NOMBRE DEL PAYLOAD (getClaim)
    public String getNombre(String token) {
        return getClaim(token, Claims::getSubject);
        // return Jwts.parserBuilder()
        // .setSigningKey(getSignatureKey())
        // .build()
        // .parseClaimsJws(token)
        // .getBody()
        // .getSubject();
    }

}
