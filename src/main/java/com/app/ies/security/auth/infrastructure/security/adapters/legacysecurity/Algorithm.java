package com.app.ies.security.auth.infrastructure.security.adapters.legacysecurity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Algorithm {
    public static String MD5 = "MD5";

    private static String toHexadecimal(byte[] digest) {
        String hash = "";
        if (digest != null)
            for (int d = 0; d < digest.length; d++) {
                int b = digest[d] & 0xFF;
                if (Integer.toHexString(b).length() == 1)
                    hash = hash + "0";
                hash = hash + Integer.toHexString(b);
            }
        return hash;
    }

    public static String getStringMessageDigest(String message, String algorithm) {
        byte[] digest = null;
        byte[] buffer = message.getBytes();
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            System.out.println("Digest: " + messageDigest);
            messageDigest.reset();
            messageDigest.update(buffer);
            digest = messageDigest.digest();
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("Error creando Digest");
        }
        return toHexadecimal(digest);
    }

    public static String getStringMessageDigest(String message, String algorithm, int iterator) {
        String md = getStringMessageDigest(message, algorithm);
        for (int it = 0; it < iterator; it++)
            md = getStringMessageDigest(md, algorithm);
        return md;
    }
}