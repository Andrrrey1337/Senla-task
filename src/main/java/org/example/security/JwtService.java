package org.example.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    // превращаем наш ключ в криптографический ключ
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    public String generateToken(UserDetails userDetails) {
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return Jwts.builder()
                .subject(userDetails.getUsername()) //кому выдан
                .claim("roles", roles) // кастомные данные, роль
                .issuedAt(new Date()) // дата выдачи
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration)) // время смерти токена
                .signWith(getSigningKey()) // подписание токена ключом
                .compact(); // сборка в строку
    }

    // извлечение имени и криптографическая проверка
    public String extractUsername(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey()) // проверка подписи
                .build()
                .parseSignedClaims(token) // расшифровка и проверка
                .getPayload()
                .getSubject(); // получение username
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false; // Токен просрочен или подделан
        }
    }

}
