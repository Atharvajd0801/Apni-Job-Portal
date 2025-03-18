package com.atharvajd.jobquestbackend;


import java.security.SecureRandom;
import java.util.Base64;

public class JWTSecretKeyGenerator {
    public static void main(String[] args) {
        // Generate a 32-byte random secret key
        byte[] key = new byte[32]; // 256-bit key
        new SecureRandom().nextBytes(key);
        
        // Encode it as a Base64 string
        String secretKey = Base64.getEncoder().encodeToString(key);
        
        // Print the generated secret key
        System.out.println("Generated JWT Secret Key: " + secretKey);
    }
}
