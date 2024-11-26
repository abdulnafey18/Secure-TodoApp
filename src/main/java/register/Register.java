package register;

public class Register {
    String name;
    String password;
    String confirmPassword;
    String email;

    boolean valid;

    public Register(String name, String password, String confirmPassword, String email) {
        this.name = name;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.email = email;
    }

    public String checkIfValid() {

        if (checkIfBlank()){
            return "Entries cant be Blank";
        }
        if( ! checkIfPasswordSame()){
            return "Passwords are not the Same";
        }
        if(! checkIfLengthGreterSix()){
            return "Password has to have six characters";
        }
        if( checkIfConainsCapitalLetter()){
            return "Password has to have a capital letter";
        }
        if( checkIfConainsLowerLetter()){
            return "Password has to have a lower case letter";
        }
        if( checkIfConainsNumeric()){
            return "Password need t6 have a Numeric";
        }
        if( checkIfValidEmail()){
            return "Enter a valid Email";
        }
        return "";
    }

    private boolean checkIfBlank() {
        if (this.name.isBlank() || this.password.isBlank() || this.confirmPassword.isBlank() || this.email.isBlank()) {
            System.out.println("Fill in all Entries");
            return true;
        }
        return false;
    }

    private boolean checkIfPasswordSame() {
        if (this.password.equals(this.confirmPassword)) {
            return true;
        }
        return false;
    }

    private boolean checkIfLengthGreterSix() {
        if (this.password.length() <= 6) {
            return false;
        }
        return true;
    }

    private boolean checkIfConainsCapitalLetter() {
        if (this.password.matches(".*[A-Z].*")) {
            return false;
        }
        return true;
    }

    private boolean checkIfConainsLowerLetter() {
        if (this.password.matches(".*[a-z].*")) {
            return false;
        }
        return true;
    }

    private boolean checkIfConainsNumeric() {
        if (this.password.matches(".*[0-9].*")) {
            return false;
        }
        return true;
    }

    private boolean checkIfValidEmail() {
        if (this.email.matches(".*[@].*")) {
            return false;
        }
        return true;
    }

}
