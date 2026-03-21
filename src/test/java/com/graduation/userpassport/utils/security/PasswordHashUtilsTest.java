package com.graduation.userpassport.utils.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordHashUtilsTest {

    @Test
    void hashAndVerify_ok() {
        String password = "P@ssw0rd-123";
        String stored = PasswordHashUtils.hash(password);

        assertNotNull(stored);
        assertTrue(stored.contains(":"));
        assertTrue(PasswordHashUtils.verify(password, stored));
        assertFalse(PasswordHashUtils.verify("wrong", stored));
    }

    @Test
    void verify_invalidStoredHash_returnsFalse() {
        assertFalse(PasswordHashUtils.verify("a", null));
        assertFalse(PasswordHashUtils.verify(null, "x:y"));
        assertFalse(PasswordHashUtils.verify("a", ""));
        assertFalse(PasswordHashUtils.verify("a", "not-base64:not-base64"));
        assertFalse(PasswordHashUtils.verify("a", ":"));
    }
}

