package com.app.ies.security.auth.infrastructure.security.jwt;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Component
public class KeysSecurityProvider {

    @Value("${security.keys.public}")
    private String publicKey;

    @Value("${security.keys.private}")
    private String privateKey;


    public static final String PRIVATE_KEY_HEADER = "-----BEGIN PRIVATE KEY-----";
    public static final String PRIVATE_KEY_FOOTER = "-----END PRIVATE KEY-----";
    public static final String PUBLIC_KEY_HEADER = "-----BEGIN PUBLIC KEY-----";
    public static final String PUBLIC_KEY_FOOTER = "-----END PUBLIC KEY-----";


    public PrivateKey loadPrivateKey() {
        byte[] keyBytes = readKeyAsBytes(privateKey, PRIVATE_KEY_HEADER, PRIVATE_KEY_FOOTER);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        try {
            return KeyFactory.getInstance("RSA").generatePrivate(keySpec);
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

    }

    public RSAPublicKey loadPublicKey() {
        byte[] keyBytes = readKeyAsBytes(publicKey, PUBLIC_KEY_HEADER, PUBLIC_KEY_FOOTER);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        try {
            return (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(keySpec);
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] readKeyAsBytes(String key, String keyHeader, String keyFooter) {

        // El valor de la propiedad puede ser (a) el contenido PEM embebido o
        // (b) una ruta a un archivo. Solo intentamos resolver un archivo cuando
        // el valor NO contiene la cabecera PEM; así evitamos pasar un PEM
        // multilínea a Paths.get. Capturamos también InvalidPathException porque
        // en Windows los saltos de línea son caracteres ilegales para una ruta
        // (en Linux no fallaba porque el salto de línea sí es válido en rutas).
        if (!key.contains(keyHeader)) {
            try {
                Path path = Paths.get(key);
                if (Files.exists(path)) {
                    key = Files.readString(path);
                }
            } catch (IOException | InvalidPathException e) {
                throw new RuntimeException("Error leyendo archivo de llave", e);
            }
        }

        String cleanKey = key
                .replace(keyHeader, "")
                .replace(keyFooter, "")
                .replaceAll("\\s", "");

        return Base64.getDecoder().decode(cleanKey);
    }

    /*
    private byte[] readKeyAsBytes(String key, String keyHeader, String keyFooter) {
        String cleanKey = key.replace(keyHeader, "").replace(keyFooter, "").replace("\\s", "");
        return Base64.getDecoder().decode(cleanKey);
    }
     */


}
