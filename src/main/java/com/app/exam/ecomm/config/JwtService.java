package com.app.exam.ecomm.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    private static final String SECRET_KEY = "xJP3iN7j4tKyoFX/r4b23jRXfx0lsXuuXc2Cg55H8al8k5dUWHYpadmhu72+VzJlDkfqBqH7D+4T6oRnBZybpqF/rMK/0CDnLmlEE5X3y3fTpuC8mFu3/MZBh/nr3A87iS+ZE/8u9HMLMeyx/mZoaYnWAYTa+DXhlts4bpImHIEFI7cLXX8V5acvHZsXbAa8oOvewyR67Bb4rWQoSRYT+/YJYKZLIB9JyysrArrGY/XEmNaKT66tQOI32RjoR63WJYk36Nb24t6tz+5nMo5AgtNXiMj4+UVYnzvMZCTRqHTKmGeZVvxWdZT5BBHJgVzLaQtF0aAKqkuYpk4+xm9L2s4AOIkf998i+uTTzG5j+s4=";
    private long refreshExpiration = 1000 * 60 * 24;

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractEmail(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + refreshExpiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    ;
}
