package com.lab.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {

	@Value("${jwt.secret.key}")
    private String SECRET_KEY;

    public String generateToken(String username, List<String> roles) {
    	Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY));
    	return Jwts.builder()
    	    .setSubject(username)
    	    .claim("roles", roles)
    	    .setIssuedAt(new Date(System.currentTimeMillis()))
    	    .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
    	    .signWith(key, SignatureAlgorithm.HS256) 
    	    .compact();
    }

    public Boolean validateToken(String token, String username) {
        String tokenUsername = extractUsername(token);
        return (username.equals(tokenUsername) && !isTokenExpired(token));
    }

    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    public Date extractExpiration(String token) {
        return extractClaims(token).getExpiration();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    
    public List<String> extractRoles(String token) {
        Claims claims = extractClaims(token);
        return (List<String>) claims.get("roles");
    }

    private Claims extractClaims(String token) {
        Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY));
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
