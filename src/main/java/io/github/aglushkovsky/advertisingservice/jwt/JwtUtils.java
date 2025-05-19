package io.github.aglushkovsky.advertisingservice.jwt;

import io.github.aglushkovsky.advertisingservice.entity.User;
import io.github.aglushkovsky.advertisingservice.entity.enumeration.Role;
import io.jsonwebtoken.*;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class JwtUtils {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private int expiration;

    public String generateAccessToken(@NonNull User user) {
        return Jwts.builder()
                .subject(user.getLogin())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .claim("id", user.getId())
                .claim("login", user.getLogin())
                .claim("role", user.getRole())
                .signWith(getSecretKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public void validateAccessToken(@NonNull String token) {
        Jwts.parser()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token);
    }

    public Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private SecretKey getSecretKey() {
        byte[] decodedSecret = Base64.getDecoder().decode(secret);
        return new SecretKeySpec(decodedSecret, "HmacSHA512");
    }

    public static JwtAuthentication createJwtAuthentication(Claims claims) {
        return JwtAuthentication.builder()
                .id(claims.get("id", Long.class))
                .login(claims.get("login", String.class))
                .authorities(List.of(getRole(claims)))
                .build();
    }

    private static Role getRole(Claims claims) {
        String role = claims.get("role", String.class);
        return Role.valueOf(role);
    }
}
