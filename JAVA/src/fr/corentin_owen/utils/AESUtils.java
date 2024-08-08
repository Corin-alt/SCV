package fr.corentin_owen.utils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Random;

/**
 * Class {@link AESUtils} which is a utility for encryption
 *
 * @author Corentin - Owen
 * @version 12/2021
 */
public class AESUtils {

    /**
     * Attribute(s)
     */
    private static List<String> values = Arrays.asList(
            "1", "2", "3", "4", "5", "6",
            "7", "8", "9", "a", "b", "c",
            "d", "e", "f", "g", "h", "i",
            "j", "k", "l", "n", "o", "p",
            "q", "r", "s", "t", "u", "v",
            "w", "x", "y", "z", "A", "B",
            "C", "D", "E", "F", "G", "H",
            "I", "J", "K", "L", "N", "O",
            "P", "Q", "R", "S", "T", "U",
            "V", "W", "X", "Y", "Z");


    /**
     * Generate a random key
     *
     * @return the key
     */
    public static String generateRandomKey() {
        Random rand = new Random();
        StringBuilder key = new StringBuilder(16);
        for (int i = 0; i < 16; i++) {
            key.append(values.get(rand.nextInt(values.size())));
        }
        return key.toString();
    }

    /**
     * Method to encrypt message in AES
     *
     * @param pass the password
     * @param str  the message
     * @return encrypted message
     */
    public static String encrypt(String pass, String str) {
        SecretKeySpec specification = new SecretKeySpec(pass.getBytes(), "AES");
        byte[] bytes = null;
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, specification);
            bytes = cipher.doFinal(str.getBytes());
        } catch (NoSuchAlgorithmException | NoSuchPaddingException |
                InvalidKeyException | IllegalBlockSizeException |
                BadPaddingException e) {
            System.err.println("Error during encryption: " + e);
            System.exit(0);
        }
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * Method to decrypt message in AES
     *
     * @param pass   the password
     * @param encode encrypted message
     * @return decrypted message
     */
    public static String decrypt(String pass, String encode) {
        SecretKeySpec specification = new SecretKeySpec(pass.getBytes(), "AES");
        byte[] decode = Base64.getDecoder().decode(encode);
        byte[] bytes = null;
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, specification);
            bytes = cipher.doFinal(decode);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException |
                InvalidKeyException | IllegalBlockSizeException |
                BadPaddingException e) {
            System.err.println("Error during decryption: " + e);
            System.exit(0);
        }
        return new String(bytes);
    }
}
