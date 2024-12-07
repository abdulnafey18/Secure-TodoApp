package register;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class SolidPasswordTest {
    String password1;
    String password2;
    String msg;
    String expected;

    Password pwd;
    PasswordFactory pwf;

    @BeforeMethod
    public void setUp() {
        pwf = new PasswordFactory();
    }

    @Test(description = "This will test if passwords are the same", priority = 1)
    public void testConfirmPasswordValidSame() {
        pwd = pwf.getTest(Password.PASSWORD_TEST.SAME);

        password1 = "password";
        password2 = "passwor";
        expected = "Passwords do not Match";

        boolean result = pwd.checkPassword(password1, password2);
        msg = pwd.getMessage();

        Assert.assertFalse(result, "Password validation should fail for mismatched passwords.");
        Assert.assertEquals(msg, expected, "Validation message mismatch!");
    }

    @Test(description = "This will test if password length is greater than 6", priority = 2)
    public void testConfirmPasswordValidLength() {
        pwd = pwf.getTest(Password.PASSWORD_TEST.LENGTH);

        password1 = "pass";
        password2 = "pass";
        expected = "Password is not long enough";

        boolean result = pwd.checkPassword(password1, password2);
        msg = pwd.getMessage();

        Assert.assertFalse(result, "Password validation should fail for short passwords.");
        Assert.assertEquals(msg, expected, "Validation message mismatch!");
    }

    @Test(description = "This will test if password has a capital letter", priority = 3)
    public void testConfirmPasswordValidCapital() {
        pwd = pwf.getTest(Password.PASSWORD_TEST.CAPITAL);

        password1 = "password";
        password2 = "password";
        expected = "Password should have a Capital Letter";

        boolean result = pwd.checkPassword(password1, password2);
        msg = pwd.getMessage();

        Assert.assertFalse(result, "Password validation should fail for missing capital letter.");
        Assert.assertEquals(msg, expected, "Validation message mismatch!");
    }

    @Test(description = "This will test if password has a lowercase letter", priority = 4)
    public void testConfirmPasswordValidLower() {
        pwd = pwf.getTest(Password.PASSWORD_TEST.LOWER);

        password1 = "PASSWORD";
        password2 = "PASSWORD";
        expected = "Password should have a Lower Letter";

        boolean result = pwd.checkPassword(password1, password2);
        msg = pwd.getMessage();

        Assert.assertFalse(result, "Password validation should fail for missing lowercase letter.");
        Assert.assertEquals(msg, expected, "Validation message mismatch!");
    }

    @Test(description = "This will test if password has a number", priority = 5)
    public void testConfirmPasswordValidNumber() {
        pwd = pwf.getTest(Password.PASSWORD_TEST.NUMBER);

        password1 = "Password";
        password2 = "Password";
        expected = "Password should have a Number";

        boolean result = pwd.checkPassword(password1, password2);
        msg = pwd.getMessage();

        Assert.assertFalse(result, "Password validation should fail for missing number.");
        Assert.assertEquals(msg, expected, "Validation message mismatch!");
    }

    @Test(description = "This will test if password has been compromised", priority = 6)
    public void testConfirmPasswordCompromised() {
        pwd = pwf.getTest(Password.PASSWORD_TEST.COMPROMISED);

        password1 = "password";
        password2 = "password";
        expected = "Password is compromised";

        boolean result = pwd.checkPassword(password1, password2);
        msg = pwd.getMessage();

        Assert.assertFalse(result, "Password validation should fail for compromised passwords.");
        Assert.assertEquals(msg, expected, "Validation message mismatch!");
    }

    @Test(description = "This will test if password has not been compromised", priority = 7)
    public void testConfirmPasswordNotCompromised() {
        pwd = pwf.getTest(Password.PASSWORD_TEST.COMPROMISED);

        password1 = "27Sept234567";
        password2 = "27Sept234567";
        expected = "Password is Hacked";

        boolean result = pwd.checkPassword(password1, password2);
        msg = pwd.getMessage();

        Assert.assertTrue(result, "Password validation should pass for secure passwords.");
        Assert.assertNotEquals(msg, expected, "Validation message mismatch for secure passwords!");
    }
}