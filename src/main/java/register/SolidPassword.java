package register;



import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class SolidPassword {

    public static class PasswordSame extends Password {

        public PasswordSame(String message) {
            super(message);
        }


        @Override
        public boolean checkPassword(String password1, String password2) {
            return !password1.equals(password2);
        }
    }

    public static class PasswordLength extends Password {
        private int minlength = 6;

        public PasswordLength(String message) {
            super(message);
        }

        @Override
        public boolean checkPassword(String pass1, String pass2) {

            return (pass1.length() <= minlength);
        }
    }

    public static class PasswordCapitalLetter extends Password {

        public PasswordCapitalLetter(String message) {
            super(message);
        }

        @Override
        public boolean checkPassword(String password1, String password2) {
            return !password1.matches(".*[A-Z].*");
        }
    }

    public static class PasswordLowerLetter extends Password {
        public PasswordLowerLetter(String message) {
            super(message);
        }

        @Override
        public boolean checkPassword(String password1, String password2) {
            return !password1.matches(".*[a-z].*");

        }
    }

    public static class PasswordNumeric extends Password {
        public PasswordNumeric(String message) {
            super(message);
        }

        @Override
        public boolean checkPassword(String password1, String password2) {
            return !password1.matches(".*[1-9].*");
        }
    }

    public static class PasswordCompromised extends Password {
        public PasswordCompromised(String message) {
            super(message);
        }


        @Override
        public boolean checkPassword(String password1, String password2) {
            boolean result = false;
            String sha1 = "";
            MessageDigest digest = null;
            String pawned = "https://api.pwnedpasswords.com/range/";

            try {
                digest = MessageDigest.getInstance("SHA-1");
                digest.reset();
                digest.update(password1.getBytes("utf8"));
                sha1 = String.format("%040x", new BigInteger(1, digest.digest()));
                System.out.println("Generated SHA-1: " + sha1);
            } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
                System.err.println("Error generating SHA-1 hash");
                e.printStackTrace();
                return false;
            }

            String prefixHash = sha1.substring(0, 5).toUpperCase();
            String suffixHash = sha1.substring(5).toUpperCase();
            OkHttpClient client = new OkHttpClient();
            String url = pawned + prefixHash;

            System.out.println("API URL: " + url);

            Request request = new Request.Builder().url(url).build();
            try (Response response = client.newCall(request).execute();
                 ResponseBody body = response.body()) {
                if (body != null) {
                    String hashes = body.string();
                    System.out.println("API Response: " + hashes);
                    String[] lines = hashes.split("\\r?\\n");
                    for (String line : lines) {
                        System.out.println("Checking hash line: " + line);
                        if (line.startsWith(suffixHash)) {
                            System.out.println("Password found, count: " + line.substring(line.indexOf(":") + 1));
                            result = true;
                        }
                    }
                } else {
                    System.err.println("API response body is null");
                }
            } catch (IOException e) {
                System.err.println("Error connecting to Have I Been Pwned API");
                e.printStackTrace();
            }

            return result;
        }
    }

}

