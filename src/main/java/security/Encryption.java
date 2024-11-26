package security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class Encryption {

    String passwordToHash = "password";
    String generatedPassword = null;

    String encryption;

    public Encryption() {

    }

    public void setEncryption(String encryption) {
        this.encryption = encryption;
    }

    public String getHash(String password, Boolean s) throws NoSuchAlgorithmException {
        String salt = getSalt();
        try {
            // this.encryption options MD5 SHA-1 SHA-246 SHA-384 SHA512
            MessageDigest md = MessageDigest.getInstance(this.encryption);
            md.update(password.getBytes());
            if(s){
                md.update(salt.getBytes());
            }
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    private static String getSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt.toString();
    }

    public String toRot13(String input){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if       (c >= 'a' && c <= 'm') c += 13;
            else if  (c >= 'A' && c <= 'M') c += 13;
            else if  (c >= 'n' && c <= 'z') c -= 13;
            else if  (c >= 'N' && c <= 'Z') c -= 13;
            sb.append(c);
        }
        return sb.toString();
    }


}
