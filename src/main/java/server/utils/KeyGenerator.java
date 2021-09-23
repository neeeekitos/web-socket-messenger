package server.utils;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

public class KeyGenerator {

    public static String generateSessionKey(final String username, final String salt) throws NoSuchAlgorithmException {
        return Hashing.sha256()
                .hashString(username + salt, StandardCharsets.UTF_8)
                .toString();
    }

    public static boolean isValidSessionKey(final String username, final String salt, final String sessionKey) throws NoSuchAlgorithmException {
        String expected = Hashing.sha256()
                .hashString(username + salt, StandardCharsets.UTF_8)
                .toString();
        return expected.equals(sessionKey);
    }
}