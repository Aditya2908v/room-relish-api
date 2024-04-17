package org.example.roomrelish.services;

import org.jetbrains.annotations.TestOnly;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@TestOnly
public class JwtService {

    // Secret key used for signing and validating JWT tokens
    @Value("${application.security.jwt.secret-key}")
    private String SECRET_KEY;
    @Value("${application.security.jwt.expiration}")
    private long jwtExpirationInMs;
    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshTokenExpirationInMs;

    // Method to extract username from JWT token
    public String extractUsername(String jwtToken) {
        return extractClaim(jwtToken, Claims::getSubject);
    }

    // Method to extract claim from JWT token
    public <T> T extractClaim(String jwtToken, Function<Claims, T> claimsResolver) {
        final Claims claims = extractClaims(jwtToken);
        return claimsResolver.apply(claims);
    }

    // Method to extract claims from JWT token
    /*
        Step 1 : Create a new instance of JwtParserBuilder
        Step 2 : Set the signing key used to verify the token
        Step 3 : Build the Jwt Parser
        Step 4 : Parse the JWT token and verify its signature
        Step 5 : Extract and return the claims (payload) from the token
     */
    private Claims extractClaims(String jwtToken) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(jwtToken)
                .getBody();
    }

    // Method to generate JWT token with specified claims and expiration time
    /*
        Step 1 : Create a JWT token builder
        Step 2 : Set the claims (payload) of the token
        Step 3 : Set the subject (username) of the token
        Step 4 : Set the issued at time of token
        Step 5 : Se the expiration time of the token
        Step 6 : Sign the token with the specified signing key and algorithm
        Step 7 : Compact the token into its final string representation
     */
    public String generateToken(Map<String, Object> claims, UserDetails userDetails) {
        return buildToken(claims, userDetails, jwtExpirationInMs);
    }

    public String generateRefreshToken( UserDetails userDetails) {
        return buildToken(new HashMap<>(), userDetails,refreshTokenExpirationInMs);
    }

    private String buildToken(Map<String, Object> claims, UserDetails userDetails, long expiration) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Overloaded method to generate JWT token with default claims and expiration time
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    // Method to validate JWT token
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    // Method to check if JWT token is expired
    /*
        if the expiration time is before the current time, it returns 'true'
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Method to extract expiration time from JWT token
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Method to retrieve signing key
    /*
        generated a signing key using HMAC SHA algorithm.
     */
    private Key getSignInKey() {
        byte[] keyBytes = SECRET_KEY.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
