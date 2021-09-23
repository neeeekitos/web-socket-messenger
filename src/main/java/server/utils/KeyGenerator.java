package server.utils;

import com.google.common.hash.Hashing;
import org.apache.commons.lang3.RandomStringUtils;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

public class KeyGenerator {

    public static String generateSecureSessionKey(final String username, final String salt) throws NoSuchAlgorithmException {
        return Hashing.sha256()
                .hashString(username + salt, StandardCharsets.UTF_8)
                .toString();

    }

    public static String generateRandomKey() {
        int length = 10;
        boolean useLetters = true;
        boolean useNumbers = true;
        return RandomStringUtils.random(length, useLetters, useNumbers);
    }

    public static boolean isValidSessionKey(final String username, final String salt, final String sessionKey) throws NoSuchAlgorithmException {
        String expected = Hashing.sha256()
                .hashString(username + salt, StandardCharsets.UTF_8)
                .toString();
        return expected.equals(sessionKey);
    }
}