package fr.corentin_owen.utils;

import fr.corentin_owen.security.KeyType;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * Class {@link RSAUtils} which is a utility for RAS
 *
 * @author Corentin - Owen
 * @version 12/2021
 */
public class RSAUtils {

    /**
     * Generate a key pair
     * @return the key pair
     */
    public static Map<KeyType, byte[]> generateKeys() {
        Map<KeyType, byte[]> keys = new HashMap<>();
        KeyPairGenerator keyGenerator= null;
        try {
            keyGenerator = KeyPairGenerator.getInstance("RSA");
            keyGenerator.initialize(2048);
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Error during initialization of the key generator: " + e);
            System.exit(0);
        }

        KeyPair keyPair = keyGenerator.generateKeyPair();

        keys.put(KeyType.PRIVATE, keyPair.getPrivate().getEncoded());
        keys.put(KeyType.PUBLIC, keyPair.getPublic().getEncoded());

        return keys;
    }
}