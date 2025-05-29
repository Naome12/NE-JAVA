package com.ne.example.auth.jwt;


import com.ne.example.auth.exceptions.InvalidJwtException;
import com.ne.example.employees.Employee;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SignatureException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

@AllArgsConstructor
@Service
@Slf4j
public class JwtService {
    private final JwtConfig config;

    public Jwt generateAccessToken(Employee user){
        return generateToken(user, config.getAccessTokenExpiration());
    }

    public Jwt generateRefreshToken(Employee user){
        return generateToken(user, config.getRefreshTokenExpiration());
    }

    private Jwt generateToken(Employee user, long tokenExpiration){
        var claims = Jwts.claims()
                .subject(user.getId().toString())
                .add("email", user.getEmail())
                .add("mobile", user.getMobile())
                .add("role", user.getRole())
                .add("status", user.getStatus())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * tokenExpiration))
                .build();
        return new Jwt(claims, config.getSecretKey());
    }

    public Jwt parseToken(String token) {
        try {
            var claims = getClaims(token);
            return new Jwt(claims, config.getSecretKey());
        } catch (ExpiredJwtException ex) {
            log.debug("Token expired: {}", ex.getMessage()); // Debug level only
            throw new InvalidJwtException("Token expired");
        } catch (SignatureException ex) {
            log.debug("Invalid token signature: {}", ex.getMessage()); // Debug level only
            throw new InvalidJwtException("Invalid token signature");
        } catch (JwtException | IllegalArgumentException ex) {
            log.debug("Invalid token: {}", ex.getMessage()); // Debug level only
            throw new InvalidJwtException("Invalid token");
        }
    }

    private Claims getClaims(String token){
        return Jwts.parser()
                .verifyWith(config.getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
