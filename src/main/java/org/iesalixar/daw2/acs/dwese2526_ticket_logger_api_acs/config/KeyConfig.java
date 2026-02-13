package org.iesalixar.daw2.acs.dwese2526_ticket_logger_api_acs.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;

@Configuration
public class KeyConfig {

    // Ruta al keystore
    @Value("${jwt.keystore.path}")
    private String keystorePath;

    // Password del keystore
    @Value("${jwt.keystore.password}")
    private String keystorePassword;

    // Alias del par de claves dentro del keystore
    @Value("${jwt.keystore.alias}")
    private String keystoreAlias;

    // Tipo ode keystore PKCS12 (p12)
    @Value("${jwt.keystore.type:PKCS12}")
    private String keystoreType;

    // Carga el par de claves desde le keystore y lo expone como bean
    @Bean
    public KeyPair jwtKeyPair() throws Exception {
        // 1) Abrir el keystore
        KeyStore keyStore = KeyStore.getInstance(keystoreType);

        // 2) Cargar el fichero del keystore usando su contraseña
        try (FileInputStream fis = new FileInputStream(keystorePath)) {
            keyStore.load(fis, keystorePassword.toCharArray());
        }
        // 3) Leer la clave privada
        PrivateKey privateKey = (PrivateKey) keyStore.getKey(
                keystoreAlias, keystorePassword.toCharArray()
        );

        // 4) Leer la clave publica desde el certificado del alias
        PublicKey publicKey = keyStore.getCertificate(keystoreAlias).getPublicKey();

        // 5) Devolver ambas en un KeyPair
        return new KeyPair(publicKey, privateKey);
    }
}
