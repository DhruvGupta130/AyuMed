package com.example.system.configuration;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;

@Service
public class JwtUtils {

    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24;  // 1 day
    private final SecretKey key;

    public JwtUtils() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
        String secretKey = Base64.getEncoder().encodeToString(keyGen.generateKey().getEncoded());
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }
    public String generateToken(UserDetails userDetails) {
        String roles = userDetails.getAuthorities().toString();
        roles = roles.replace("[", "").replace("]", "");
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .claim("username", userDetails.getUsername())
                .claim("role",roles)
                .signWith(key)
                .compact();
    }
    public Claims parseToken(String token) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
    }
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = parseToken(token).getSubject();
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    public boolean isTokenExpired(String token) {
        return parseToken(token).getExpiration().before(new Date());
    }

    public String getUserNameFromToken(String token) {
        token = token.replace("Bearer ", "");
        return parseToken(token).getSubject();
    }
}