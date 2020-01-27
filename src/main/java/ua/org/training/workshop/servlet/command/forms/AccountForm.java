package ua.org.training.workshop.servlet.command.forms;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.util.Arrays;

/**
 * @author kissik
 */
public class AccountForm {
    private String username;
    private String password;
    private String confirmPassword;
    private String firstName;
    private String lastName;
    private String firstNameOrigin;
    private String lastNameOrigin;
    private String email;
    private String phone;
    private String[] role = {"USER"};

    @NotNull(message = "{validation.text.error.required.field}")
    @Size(min = 6, max = 50, message = "{validation.text.error.from.six.to.fifty}")
    @Pattern(regexp="[a-z_]{1}[0-9a-z_]*", message = "{validation.username.symbols}")
    public String getUsername() { return username; }

    public void setUsername(String userName) { this.username = userName; }

    @NotNull(message = "{validation.text.error.required.field}")
    @Size(min = 8, max = 50, message = "{validation.text.error.from.eight.to.fifty}")
    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public String getConfirmPassword() { return confirmPassword; }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    @NotNull(message = "{validation.text.error.required.field}")
    @Size(min = 3, max = 50, message = "{validation.text.error.from.three.to.fifty}")
    public String getFirstName() { return firstName; }

    public void setFirstName(String firstName) { this.firstName = firstName; }

    @NotNull(message = "{validation.text.error.required.field}")
    @Size(min = 3, max = 50, message = "{validation.text.error.from.three.to.fifty}")
    public String getLastName() { return lastName; }

    public void setLastName(String lastName) { this.lastName = lastName; }

    @NotNull(message = "{validation.text.error.required.field}")
    @Size(min = 6, max = 50, message = "{validation.text.error.from.six.to.fifty}")
    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    @NotNull(message = "{validation.text.error.required.field}")
    @Pattern(regexp="\\+\\d{12}", message="+380001112233")
    public String getPhone() { return phone; }

    public void setPhone(String phone) { this.phone = phone; }

    @NotNull(message = "{validation.text.error.at.least.one}")
    public String[] getRole() { return role; }

    public void setRole(String[] role) { this.role = role; }

    public String toString() {
        return new StringBuilder()
                .append(" username: " + username)
                .append(" firstName: " + firstName)
                .append(" lastName: " + lastName)
                .append(" firstNameOrigin: " + firstNameOrigin)
                .append(" lastNameOrigin: " + lastNameOrigin)
                .append(" email: " + email)
                .append(" phone: " + phone)
                .append(" role:" + Arrays.toString(role))
                .toString();
    }

    @NotNull(message = "{validation.text.error.required.field}")
    @Size(min = 3, max = 50, message = "{validation.text.error.from.three.to.fifty}")
    public String getFirstNameOrigin() {
        return firstNameOrigin;
    }

    public void setFirstNameOrigin(String firstNameOrigin) {
        this.firstNameOrigin = firstNameOrigin;
    }

    @NotNull(message = "{validation.text.error.required.field}")
    @Size(min = 3, max = 50, message = "{validation.text.error.from.three.to.fifty}")
    public String getLastNameOrigin() {
        return lastNameOrigin;
    }

    public void setLastNameOrigin(String lastNameOrigin) {
        this.lastNameOrigin = lastNameOrigin;
    }
}
