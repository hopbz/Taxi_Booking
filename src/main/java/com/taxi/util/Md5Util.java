package com.taxi.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public final class Md5Util {

    private Md5Util() {
    }

    public static String hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder();
            for (byte b : digest) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();
        } catch (Exception e) {
            throw new IllegalStateException("Không thể mã hóa MD5", e);
        }
    }
}
