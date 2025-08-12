package com.example.Enotes_API_Service.service.Impl;

import com.example.Enotes_API_Service.entity.User;
import com.example.Enotes_API_Service.exception.JwtTokenExpiredException;
import com.example.Enotes_API_Service.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtServiceImpl implements JwtService {

    private String secretKey = "";

    public JwtServiceImpl(){
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
            SecretKey sk = keyGen.generateKey();
            secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public String generateToken(User user) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("role", user.getRoles());
        claims.put("status", user.getStatus().getIsActive());
        return Jwts.builder()
                .claims().add(claims)
                .subject(user.getEmail())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+ 10 * 60 * 1000))
                .and()
                .signWith(getKey())
                .compact();
    }

    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public String extractUsername(String token) {
        try {
            Claims claims = extractAllClaims(token);
            return claims.getSubject();
        } catch (ExpiredJwtException e) {
            throw new JwtTokenExpiredException("Token is expired");
        } catch (JwtException e) {
            throw new JwtTokenExpiredException("Invalid jwt token");
        }
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(decryptKey(secretKey)).build()
                .parseSignedClaims(token).getPayload();
    }

    private SecretKey decryptKey(String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public Boolean validateToken(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        Boolean isExpired = isTokenExpired(token);
        return username.equalsIgnoreCase(userDetails.getUsername()) && !isExpired;
    }

    private Boolean isTokenExpired(String token) {
        Claims claims = extractAllClaims(token);
        Date expiryDate = claims.getExpiration();
        return expiryDate.before(new Date());
    }
}
