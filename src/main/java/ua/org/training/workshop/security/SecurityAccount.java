package ua.org.training.workshop.security;

import ua.org.training.workshop.domain.Account;
import ua.org.training.workshop.domain.Role;
import ua.org.training.workshop.utility.ApplicationConstants;

import java.util.Collection;
import java.util.HashSet;

/**
 * @author kissik
 */
/*TODO stream*/
public class SecurityAccount extends Account {
    public static final SecurityAccount ACCOUNT =
            new SecurityAccount(ApplicationConstants.APP_ANONYMOUS_ACCOUNT_USERNAME);

    private SecurityAccount(String username) {
        super(username);
    }

    public SecurityAccount(Account account) {
        this.setUsername(account.getUsername());
        this.setFirstName(account.getFirstName());
        this.setFirstNameOrigin(account.getFirstNameOrigin());
        this.setLastName(account.getLastName());
        this.setLastNameOrigin(account.getLastNameOrigin());
        this.setRoles(new HashSet<>(account.getRoles()));
    }

    public boolean isAnonymous() {
        return getRoles().isEmpty();
    }

    public boolean isAuthenticated() {
        return !getRoles().isEmpty();
    }

    public Collection<GrantedAuthority> getAuthorities() {
        return new HashSet<>(getRoles());
    }

    public boolean hasRole(String role) {
        for (Role r : getRoles()) {
            if (r.getCode().equals(role)) return true;
        }
        return false;
    }
}
