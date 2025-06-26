package com.ticketnest.user_service.Security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Component
public class JwtUtil {

    // This will hold our secret key as a string
    private final String key;

    // Constructor - runs when this class is created
    public JwtUtil() throws NoSuchAlgorithmException {
        // Create a key generator for HmacSHA256 algorithm (used to sign JWT tokens)
        KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");

        // Generate a secret key using the key generator
        SecretKey secretKey = keyGenerator.generateKey();

        // Convert the secret key bytes to a Base64-encoded string and save it in 'key'
        // Base64 encoding makes the key safe to store as a string
        key = Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }

    // This method creates a JWT token for the given email
    public String GenerateToken(String email)
    {
        // Create an empty map to hold extra information (claims) inside the token if needed
        Map<String,Object> claims = new HashMap<>();

        // Set how long the token will be valid (here, 1 hour = 3600000 milliseconds)
        // 60*60*1000;
        long expirationMillis = 3600000;

        return Jwts.builder()
                .claims()
                // Add claims to the token (currently empty)
                .add(claims)
                // Set the token's "subject" - who this token is for (the user's email)
                .subject(email)
                // Set the time when the token is created (now)
                .issuedAt(new Date(System.currentTimeMillis()))
                // Set the expiration time - when the token will stop being valid
                .expiration(new Date(System.currentTimeMillis() + expirationMillis))
                .and()
                // Sign the token using our secret key
                .signWith(getKey())
                // Build and return the final token string
                .compact();
    }

    // Helper method to get the secret key object from the Base64 string
    private Key getKey()
    {
        // Decode the Base64 string back into bytes
        byte[] keyBytes = Decoders.BASE64.decode(key);

        // Create a Key object for signing/verifying using the decoded bytes
        return Keys.hmacShaKeyFor(keyBytes);

    }
}
