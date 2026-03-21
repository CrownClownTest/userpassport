package com.graduation.userpassport.utils.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public final class PasswordHashUtils {
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private PasswordHashUtils() {
    }

    public static String hash(String password) {
        byte[] salt = new byte[16];
        SECURE_RANDOM.nextBytes(salt);
        byte[] digest = digest(password, salt);
        return Base64.getEncoder().encodeToString(salt) + ":" + Base64.getEncoder().encodeToString(digest);
    }

    public static boolean verify(String password, String storedHash) {
        if (password == null || storedHash == null || storedHash.isBlank()) {
            return false;
        }

        int separatorIndex = storedHash.indexOf(':');
        if (separatorIndex < 0) {
            return MessageDigest.isEqual(
                    password.getBytes(StandardCharsets.UTF_8),
                    storedHash.getBytes(StandardCharsets.UTF_8)
            );
        }

        String saltBase64 = storedHash.substring(0, separatorIndex);
        String digestBase64 = storedHash.substring(separatorIndex + 1);
        if (saltBase64.isEmpty() || digestBase64.isEmpty()) {
            return false;
        }

        try {
            byte[] salt = Base64.getDecoder().decode(saltBase64);
            byte[] expectedDigest = Base64.getDecoder().decode(digestBase64);
            byte[] actualDigest = digest(password, salt);
            return MessageDigest.isEqual(expectedDigest, actualDigest);
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    private static byte[] digest(String password, byte[] salt) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(salt);
            return messageDigest.digest(password.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("unsupported hash algorithm", e);
        }
    }
}
