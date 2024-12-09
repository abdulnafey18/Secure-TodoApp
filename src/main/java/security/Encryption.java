package security;

import org.mindrot.jbcrypt.BCrypt;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class Encryption {
    // Sensitive data exposure mitigation
    private static final String ALGORITHM = "AES";
    private static final String SECRET_KEY = "MySecretKey12345"; // Replace this with a secure key source

    // Hash a password using BCrypt
    public static String hashPassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        System.out.println("Hashing password: " + password); // Debugging log
        String hashed = BCrypt.hashpw(password, BCrypt.gensalt());
        System.out.println("Generated Hash: " + hashed); // Debugging log
        return hashed;
    }

    // Verify a password against a hashed password
    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        if (plainPassword == null || hashedPassword == null) {
            throw new IllegalArgumentException("Password and hash cannot be null");
        }
        System.out.println("Verifying password: " + plainPassword); // Debugging log
        System.out.println("Against hash: " + hashedPassword); // Debugging log
        boolean result = BCrypt.checkpw(plainPassword, hashedPassword);
        System.out.println("Verification result: " + result); // Debugging log
        return result;
    }

    // Encrypt sensitive data using AES
    public static String encrypt(String data) throws Exception {
        if (data == null || data.isEmpty()) {
            throw new IllegalArgumentException("Data to encrypt cannot be null or empty");
        }
        SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        byte[] encryptedData = cipher.doFinal(data.getBytes());
        String encryptedString = Base64.getEncoder().encodeToString(encryptedData);
        System.out.println("Encrypted Data: " + encryptedString); // Debugging log
        return encryptedString;
    }

    // Decrypt sensitive data using AES
    public static String decrypt(String encryptedData) throws Exception {
        if (encryptedData == null || encryptedData.isEmpty()) {
            throw new IllegalArgumentException("Data to decrypt cannot be null or empty");
        }
        SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        byte[] originalData = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
        String decryptedString = new String(originalData);
        System.out.println("Decrypted Data: " + decryptedString); // Debugging log
        return decryptedString;
    }
}