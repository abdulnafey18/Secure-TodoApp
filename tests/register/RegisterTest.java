package register;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class RegisterTest {
    String name;
    String password;
    String confirmPassword;
    String email;

    String message;
    String reply;

    @BeforeMethod
    public void setUp() {
    }

    @AfterMethod
    public void tearDown() {
    }

    @Test(description = "This will check if there is a blank entry", priority = 1, enabled = true)
    public void testCheckIfValidBlank() {
        name = "";
        password = "";
        confirmPassword = "";
        email = "";
        message = "Entries can't be Blank";
        Register register = new Register(name, password, confirmPassword, email);
        reply = register.checkIfValid();
        System.out.println(message);
        Assert.assertEquals(reply, message);
    }

    @Test(description = "This will check if password and confirm password are the same", priority = 2, enabled = true)
    public void testCheckIfValidPasswordSame() {
        name = "test";
        password = "password";
        confirmPassword = "passwor";
        email = "test@gmail.com";
        message = "Passwords are not the Same";
        Register register = new Register(name, password, confirmPassword, email);
        reply = register.checkIfValid();
        System.out.println(message);
        Assert.assertEquals(reply, message);
    }

    @Test(description = "This will check if the password is short", priority = 3, enabled = true)
    public void testCheckIfValidPasswordShort() {
        name = "test";
        password = "pass";
        confirmPassword = "pass";
        email = "test@gmail.com";
        message = "Password must have six characters";
        Register register = new Register(name, password, confirmPassword, email);
        reply = register.checkIfValid();
        System.out.println(message);
        Assert.assertEquals(reply, message);
    }

    @Test(description = "This will check if there is a capital letter", priority = 4, enabled = true)
    public void testCheckIfValidPasswordCapital() {
        name = "test";
        password = "password";
        confirmPassword = "password";
        email = "test@gmail.com";
        message = "Password must have a capital letter";
        Register register = new Register(name, password, confirmPassword, email);
        reply = register.checkIfValid();
        System.out.println(message);
        Assert.assertEquals(reply, message);
    }

    @Test(description = "This will check if there is a lowercase letter", priority = 5, enabled = true)
    public void testCheckIfValidPasswordLower() {
        name = "test";
        password = "PASSWORD";
        confirmPassword = "PASSWORD";
        email = "test@gmail.com";
        message = "Password must have a lowercase letter";
        Register register = new Register(name, password, confirmPassword, email);
        reply = register.checkIfValid();
        System.out.println(message);
        Assert.assertEquals(reply, message);
    }

    @Test(description = "This will check if there is a number", priority = 6, enabled = true)
    public void testCheckIfValidPasswordNumber() {
        name = "test";
        password = "Password";
        confirmPassword = "Password";
        email = "test@gmail.com";
        message = "Password must have a numeric value";
        Register register = new Register(name, password, confirmPassword, email);
        reply = register.checkIfValid();
        System.out.println(message);
        Assert.assertEquals(reply, message);
    }

    @Test(description = "This will check if the email is in the correct format", priority = 7, enabled = true)
    public void testCheckIfValidEmail() {
        name = "test";
        password = "Passw0rd";
        confirmPassword = "Passw0rd";
        email = "invalid_email_format";
        message = "Enter a valid Email";
        Register register = new Register(name, password, confirmPassword, email);
        reply = register.checkIfValid();
        System.out.println(message);
        Assert.assertEquals(reply, message);
    }
}