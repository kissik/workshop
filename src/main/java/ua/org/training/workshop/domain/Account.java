package ua.org.training.workshop.domain;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;

/**
 * @author kissik
 */
public class Account {

    private Long id;
    private String username;
    private String password;
    private String firstName;
    private String firstNameOrigin;
    private String lastNameOrigin;
    private String lastName;
    private String email;
    private String phone;
    private boolean enabled = true;
    private LocalDate dateCreated;
    private Collection<Role> roles = new HashSet<>();

    public Account(){}
    public Account(String username) { this.username = username; }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() { return firstName; }

    public void setFirstName(String firstName) { this.firstName = firstName; }

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

    public String getLastName() { return lastName; }

    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getFullName() { return this.firstName + " " + this.lastName;}

    public String getFullNameOrigin() { return this.firstNameOrigin + " " + this.lastNameOrigin;}

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }

    public void setPhone(String phone) { this.phone = phone; }

    public boolean isEnabled() { return enabled; }

    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    public Collection<Role> getRoles() { return roles; }

    public void setRoles(Collection<Role> roles) { this.roles = roles; }

    public LocalDate getDateCreated() { return dateCreated; }

    public void setDateCreated(LocalDate dateCreated) { this.dateCreated = dateCreated; }

    @Override
    public String toString() { return username; }

}
