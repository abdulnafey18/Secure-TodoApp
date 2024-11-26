package register;

import java.util.ArrayList;
import java.util.List;

public class PasswordFactory {
    public PasswordFactory() {
    }

    public Password getTest(Enum TYPE) {

        if (TYPE == null) {
            return null;
        } else if (TYPE.equals(Password.PASSWORD_TEST.SAME)) {
            return new SolidPassword.PasswordSame("Pasword do not Match");
        } else if (TYPE.equals(Password.PASSWORD_TEST.LENGTH)) {
            return new SolidPassword.PasswordLength("Password is not long enough");
        } else if (TYPE.equals(Password.PASSWORD_TEST.CAPITAL)) {
            return new SolidPassword.PasswordCapitalLetter("Password should have a Capital Letter");
        } else if (TYPE.equals(Password.PASSWORD_TEST.LOWER)) {
            return new SolidPassword.PasswordLowerLetter("Password should have a Lower Letter");
        }else if (TYPE.equals(Password.PASSWORD_TEST.NUMBER)) {
            return new SolidPassword.PasswordNumeric("Password should have a Number");
        }else if (TYPE.equals(Password.PASSWORD_TEST.COMPROMISED)) {
            return new SolidPassword.PasswordCompromised("Password is Hacked");
        }
        return null;

    }

    public String confirmPassworValid(String password1, String password2) {

        PasswordFactory pf = new PasswordFactory();
        Password pwd1 = pf.getTest(Password.PASSWORD_TEST.SAME);
        Password pwd2 = pf.getTest(Password.PASSWORD_TEST.LENGTH);
        Password pwd3 = pf.getTest(Password.PASSWORD_TEST.CAPITAL);
        Password pwd4 = pf.getTest(Password.PASSWORD_TEST.LOWER);
        Password pwd5 =  pf.getTest((Password.PASSWORD_TEST.NUMBER));
        Password pwd6 =  pf.getTest((Password.PASSWORD_TEST.COMPROMISED));
        List<Password> list = new ArrayList<Password>();
        list.add(pwd1);
        list.add(pwd2);
        list.add(pwd3);
        list.add(pwd4);
        list.add(pwd5);
        list.add(pwd6);
        String message = "";

        for (Password p : list) {
            if (p.checkPassword(password1, password2)) {
                message = p.getMessage();
                System.out.println(" message " + message);
                return message;
            }
            message = "OK";
        }
        return message;
    }
}

