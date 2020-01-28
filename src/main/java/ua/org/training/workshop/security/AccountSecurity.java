package ua.org.training.workshop.security;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import ua.org.training.workshop.domain.Account;
import ua.org.training.workshop.domain.Role;
import ua.org.training.workshop.utilities.UtilitiesClass;

import java.util.Collection;
import java.util.HashSet;

/**
 * @author kissik
 */
public class AccountSecurity extends Account {
    static {
        new DOMConfigurator().doConfigure(UtilitiesClass.LOG4J_XML_PATH, LogManager.getLoggerRepository());
    }
    public static final String ANONYMOUS_ACCOUNT = "anonymous";
    public static final AccountSecurity ACCOUNT = new AccountSecurity(ANONYMOUS_ACCOUNT);

    private static Logger logger = Logger.getLogger(AccountSecurity.class);

    private AccountSecurity(String username){
        super(username);
    }

    public AccountSecurity(Account account){
        Collection<Role> roles = new HashSet<>();

        this.setUsername(account.getUsername());
        this.setFirstName(account.getFirstName());
        this.setFirstNameOrigin(account.getFirstNameOrigin());
        this.setLastName(account.getLastName());
        this.setLastNameOrigin(account.getLastNameOrigin());
        this.setPassword(account.getPassword());

        for(Role r: account.getRoles())
            roles.add(r);
        this.setRoles(roles);
    }

    public boolean isAnonymous(){
        return getRoles().isEmpty();
    }

    public boolean isAuthenticated(){
        return !getRoles().isEmpty();
    }

    public Collection<GrantedAuthority> getAuthorities() {
        return new HashSet<>(getRoles());
    }

    public boolean hasRole(String role){
        for (Role r : getRoles()) {
            if (r.getCode().equals(role)) return true;
        }
        return false;
    }
}
