package ua.org.training.workshop.web.dto;

import ua.org.training.workshop.domain.Account;

/**
 * @author kissik
 */
public class AccountDTO {

    private String username;
    private String firstName;
    private String firstNameOrigin;
    private String lastNameOrigin;
    private String lastName;
    private String email;
    private String phone;

    public AccountDTO(Account account) {
        this.username = account.getUsername();
        this.firstName = account.getFirstName();
        this.firstNameOrigin = account.getFirstNameOrigin();
        this.lastName = account.getLastName();
        this.lastNameOrigin = account.getLastNameOrigin();
        this.phone = account.getPhone();
        this.email = account.getEmail();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstNameOrigin() {
        return firstNameOrigin;
    }

    public void setFirstNameOrigin(String firstNameOrigin) {
        this.firstNameOrigin = firstNameOrigin;
    }

    public String getLastNameOrigin() {
        return lastNameOrigin;
    }

    public void setLastNameOrigin(String lastNameOrigin) {
        this.lastNameOrigin = lastNameOrigin;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }

    public String getFullNameOrigin() {
        return this.firstNameOrigin + " " + this.lastNameOrigin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return username;
    }
}
