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
            } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            String prefixHash = sha1.substring(0, 5).toUpperCase();
            String suffixHash = sha1.substring(5).toUpperCase();
            OkHttpClient client = new OkHttpClient();
            String url = pawned + prefixHash;

            Request request = new Request.Builder().url(url).build();
            try (Response response = client.newCall(request).execute();
                 ResponseBody body = response.body()) {
                if (body != null) {
                    String hashes = body.string();
                    String lines[] = hashes.split("\\r?\\n");
                    //System.out.println("return " + lines.length);
                    for (String line : lines) {
                        //System.out.println("hashes found "+line);
                        if (line.startsWith(suffixHash)) {
                            System.out.println(
                                    "password found, count: " + line.substring(line.indexOf(":") + 1));
                            result = true;
                        }
                    }
                }

            } catch (IOException e) {
                result = false;
                System.out.println("Not connected to the Internet");
                e.printStackTrace();
            }
            return result;
        }
    }

}

