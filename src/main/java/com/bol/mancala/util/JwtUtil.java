package com.bol.mancala.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    public static final String SECRET_KEY = "SECRET";

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    public String createToken(Map<String, Object> claims, String subject) {
        logger.info("Creating new token");
        return Jwts.builder().setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + 10 * 60 * 60 * 1000))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }

    public String extractUserName(String jwt) {
        return extractClaims(jwt, Claims::getSubject);
    }

    public Date extractExpiration(String jwt) {
        return extractClaims(jwt, Claims::getExpiration);
    }

    public <T> T extractClaims(String jwt, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(jwt);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String jwt) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(jwt).getBody();
    }

    public Boolean validateToken(String jwt, UserDetails userDetails) {
        logger.info("Authorizing token");
        String userName = extractUserName(jwt);
        return Optional.ofNullable(userName).isPresent() && userName.equals(userDetails.getUsername()) && !isTokenExpired(jwt);
    }

    public Boolean isTokenExpired(String jwt) {
        logger.info("Checking if authorization is expired");
        return extractExpiration(jwt).before(new Date());
    }
}
