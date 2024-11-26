package register;

import java.util.ArrayList;
import java.util.List;

public abstract class Password {
    private String password1;
    private String password2;
    private String message;

    public Password() {
    }
    public Password(String message) {
        this.message = message;
    }

    public String getMessage(){
        return message;
    }
    public abstract boolean checkPassword(String pass1, String pass2);

    public static enum PASSWORD_TEST {SAME, LENGTH, CAPITAL, LOWER, NUMBER, COMPROMISED, FAIL}
}

