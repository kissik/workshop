package ua.org.training.workshop.web.form;

public class AccountFormError {
    private String username;
    private String firstName;
    private String lastName;
    private String firstNameOrigin;
    private String lastNameOrigin;
    private String password;
    private String email;
    private String phone;
    private boolean hasErrors = false;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        this.hasErrors = true;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        this.hasErrors = true;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        this.hasErrors = true;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
        this.hasErrors = true;
    }

    public boolean haveErrors() {
        return hasErrors;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
        this.hasErrors = true;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
        this.hasErrors = true;
    }

    public String getFirstNameOrigin() {
        return firstNameOrigin;
    }

    public void setFirstNameOrigin(String firstNameOrigin) {
        this.firstNameOrigin = firstNameOrigin;
        this.hasErrors = true;
    }

    public String getLastNameOrigin() {
        return lastNameOrigin;
    }

    public void setLastNameOrigin(String lastNameOrigin) {
        this.lastNameOrigin = lastNameOrigin;
        this.hasErrors = true;
    }

    public void setHasErrors(boolean hasErrors) {
        this.hasErrors = hasErrors;
    }
}
