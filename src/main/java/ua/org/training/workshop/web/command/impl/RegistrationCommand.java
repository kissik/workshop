package ua.org.training.workshop.web.command.impl;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.mindrot.jbcrypt.BCrypt;
import ua.org.training.workshop.domain.Account;
import ua.org.training.workshop.exception.WorkshopException;
import ua.org.training.workshop.security.SecurityAccount;
import ua.org.training.workshop.security.SecurityService;
import ua.org.training.workshop.service.AccountService;
import ua.org.training.workshop.service.RoleService;
import ua.org.training.workshop.utility.ApplicationConstants;
import ua.org.training.workshop.utility.Utility;
import ua.org.training.workshop.web.command.Command;
import ua.org.training.workshop.web.form.AccountFormError;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class RegistrationCommand implements Command {

    private final static String DEFAULT_ROLE = "USER";
    private final static int MIN_THREE = 3;
    private final static int MIN_SIX = 6;
    private final static int MIN_EIGHT = 8;
    private final static int MAX_FIFTY = 50;

    static {
        new DOMConfigurator().doConfigure(ApplicationConstants.LOG4J_XML_PATH, LogManager.getLoggerRepository());
    }

    private final static Logger LOGGER = Logger.getLogger(RegistrationCommand.class);

    private AccountService accountService = new AccountService();
    private RoleService roleService = new RoleService();

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response) {

        HttpSession session = request.getSession();

        SecurityAccount securityAccount = SecurityService.loadAccountSecurity(request);

        if (!securityAccount.getUsername().equals(ApplicationConstants.APP_ANONYMOUS_ACCOUNT_USERNAME)) {
            SecurityService.removeLoggedUser(request, securityAccount.getUsername());
            return Pages.REGISTRATION_FORM_PAGE;
        }

        if (request.getMethod().equals("GET")) {
            LOGGER.debug("Registration form was send");
            return Pages.REGISTRATION_FORM_PAGE;
        } else {

            AccountFormError errors = new AccountFormError();

            LOGGER.debug("error has errors = " + errors.haveErrors());

            Account account = toAccount(request);
            String password = Utility.getUTF8String(
                    request.getParameter(
                            ApplicationConstants.RequestAttributes.APP_PASSWORD_ATTRIBUTE),
                    ApplicationConstants.APP_STRING_DEFAULT_VALUE);
            String confirmPassword = Utility.getUTF8String(
                    request.getParameter(
                            ApplicationConstants.RequestAttributes.ACCOUNT_CONFIRM_PASSWORD_ATTRIBUTE),
                    ApplicationConstants.APP_STRING_DEFAULT_VALUE);
            LOGGER.info(password + " = " + confirmPassword);
            account.setRoles(Collections.singletonList(roleService.findByCode(DEFAULT_ROLE)));
            validatePasswordLength(password, errors);
            validateConfirmPassword(
                    password,
                    confirmPassword,
                    errors);
            account.setPassword(BCrypt.hashpw(
                    password, BCrypt.gensalt(ApplicationConstants.APP_BCRYPT_SALT)));
            LOGGER.debug("error has errors = " + errors.haveErrors());

            validation(Utility.getLocale(request),
                    account,
                    errors);
            LOGGER.debug("error has errors = " + errors.haveErrors());

            if (!errors.haveErrors())
                try {
                    LOGGER.debug("Try to register new account!");
                    accountService.registerAccount(account);
                } catch (WorkshopException e) {
                    LOGGER.error("New account error: " + e.getMessage());
                }
            session.setAttribute("account", account);
            session.setAttribute("errors", errors);

            if (errors.haveErrors()) {
                return Pages.REGISTRATION_FORM_PAGE;
            }
            clearRequestAttributes(request);
            return Pages.REGISTRATION_FORM_OK;
        }
    }

    private void clearRequestAttributes(HttpServletRequest request) {
        request.getSession().removeAttribute(ApplicationConstants.RequestAttributes.ACCOUNT_CONFIRM_PASSWORD_ATTRIBUTE);
        request.getSession().removeAttribute(ApplicationConstants.RequestAttributes.ACCOUNT_EMAIL_ATTRIBUTE);
        request.getSession().removeAttribute(ApplicationConstants.RequestAttributes.ACCOUNT_FIRST_NAME_ATTRIBUTE);
        request.getSession().removeAttribute(ApplicationConstants.RequestAttributes.ACCOUNT_FIRST_NAME_ORIGIN_ATTRIBUTE);
        request.getSession().removeAttribute(ApplicationConstants.RequestAttributes.ACCOUNT_LAST_NAME_ATTRIBUTE);
        request.getSession().removeAttribute(ApplicationConstants.RequestAttributes.ACCOUNT_LAST_NAME_ORIGIN_ATTRIBUTE);
        request.getSession().removeAttribute(ApplicationConstants.RequestAttributes.ACCOUNT_PHONE_ATTRIBUTE);
        request.getSession().removeAttribute(ApplicationConstants.RequestAttributes.APP_PASSWORD_ATTRIBUTE);
        request.getSession().removeAttribute(ApplicationConstants.RequestAttributes.APP_USERNAME_ATTRIBUTE);
    }

    private Account toAccount(HttpServletRequest request) {
        Account account = new Account();
        account.setUsername(
                Utility.getUTF8String(
                        request.getParameter(ApplicationConstants.RequestAttributes.APP_USERNAME_ATTRIBUTE),
                        ApplicationConstants.APP_STRING_DEFAULT_VALUE));
        account.setFirstName(
                Utility.getUTF8String(
                        request.getParameter(
                                ApplicationConstants.RequestAttributes.ACCOUNT_FIRST_NAME_ATTRIBUTE
                        ),
                        ApplicationConstants.APP_STRING_DEFAULT_VALUE));
        account.setLastName(
                Utility.getUTF8String(
                        request.getParameter(
                                ApplicationConstants.RequestAttributes.ACCOUNT_LAST_NAME_ATTRIBUTE
                        ),
                        ApplicationConstants.APP_STRING_DEFAULT_VALUE));
        LOGGER.info("firstNameOrigin : " + request.getParameter(
                ApplicationConstants.RequestAttributes.ACCOUNT_FIRST_NAME_ORIGIN_ATTRIBUTE
        ));
        account.setFirstNameOrigin(
                Utility.getUTF8String(
                        request.getParameter(
                                ApplicationConstants.RequestAttributes.ACCOUNT_FIRST_NAME_ORIGIN_ATTRIBUTE
                        ),
                        ApplicationConstants.APP_STRING_DEFAULT_VALUE));
        account.setLastNameOrigin(
                Utility.getUTF8String(
                        request.getParameter(
                                ApplicationConstants.RequestAttributes.ACCOUNT_LAST_NAME_ORIGIN_ATTRIBUTE
                        ),
                        ApplicationConstants.APP_STRING_DEFAULT_VALUE));
        account.setEmail(
                Utility.getUTF8String(
                        request.getParameter(
                                ApplicationConstants.RequestAttributes.ACCOUNT_EMAIL_ATTRIBUTE
                        ),
                        ApplicationConstants.APP_STRING_DEFAULT_VALUE));
        account.setPhone(
                Utility.getUTF8String(
                        request.getParameter(
                                ApplicationConstants.RequestAttributes.ACCOUNT_PHONE_ATTRIBUTE
                        ),
                        ApplicationConstants.APP_STRING_DEFAULT_VALUE));
        account.setEnabled(true);
        LOGGER.info("try to paste to DB: " + account.getFullNameOrigin());
        return account;
    }

    private void validation(Locale locale, Account account, AccountFormError errors) {
        ResourceBundle bundle = ResourceBundle.getBundle(
                ApplicationConstants.APP_MESSAGES_BUNDLE_NAME,
                locale);

        validateUsernameLength(account.getUsername(), errors);
        validateUsernamePattern(
                account.getUsername(),
                bundle.getString("pattern.username"),
                errors);
        validateUsername(account.getUsername(), errors);

        validateFirstNameLength(account.getFirstName(), errors);
        validateFirstNamePattern(
                account.getFirstName(),
                bundle.getString("pattern.eng.name"),
                errors);

        validateFirstNameOriginLength(account.getFirstNameOrigin(), errors);
        validateFirstNameOriginPattern(
                account.getFirstNameOrigin(),
                bundle.getString("pattern.ukr.name"),
                errors);

        validateLastNameLength(account.getLastName(), errors);
        validateLastNamePattern(
                account.getLastName(),
                bundle.getString("pattern.eng.name"),
                errors);

        validateLastNameOriginLength(account.getLastNameOrigin(), errors);
        validateLastNameOriginPattern(
                account.getLastNameOrigin(),
                bundle.getString("pattern.ukr.name"),
                errors);

        validateEmailPattern(
                account.getEmail(),
                bundle.getString("pattern.email"),
                errors);
        validateEmailLength(account.getEmail(), errors);
        validateEmail(account.getEmail(), errors);

        validatePhonePattern(
                account.getPhone(),
                bundle.getString("pattern.phone"),
                errors);
        validatePhone(account.getPhone(), errors);
    }

    private void validateLastNamePattern(String lastName, String pattern, AccountFormError errors) {
        Pattern r = Pattern.compile(pattern);
        if (!r.matcher(lastName).matches())
            errors.setLastName("validation.eng.name.symbols");
    }

    private void validateLastNameOriginPattern(String lastNameOrigin, String pattern, AccountFormError errors) {
        Pattern r = Pattern.compile(pattern);
        if (!r.matcher(lastNameOrigin).matches())
            errors.setLastNameOrigin("validation.ukr.name.symbols");
    }

    private void validateFirstNamePattern(String firstName, String pattern, AccountFormError errors) {
        Pattern r = Pattern.compile(pattern);
        if (!r.matcher(firstName).matches())
            errors.setFirstName("validation.eng.name.symbols");
    }

    private void validateFirstNameOriginPattern(String firstNameOrigin, String pattern, AccountFormError errors) {
        Pattern r = Pattern.compile(pattern);
        if (!r.matcher(firstNameOrigin).matches())
            errors.setFirstNameOrigin("validation.ukr.name.symbols");
    }

    private void validateFirstNameLength(String firstName, AccountFormError errors) {
        if (notValidFieldLength(firstName, MIN_THREE, MAX_FIFTY))
            errors.setFirstName("validation.text.error.from.three.to.fifty");
    }

    private void validateFirstNameOriginLength(String firstNameOrigin, AccountFormError errors) {
        if (notValidFieldLength(firstNameOrigin, MIN_THREE, MAX_FIFTY))
            errors.setFirstNameOrigin("validation.text.error.from.three.to.fifty");
    }

    private void validateLastNameLength(String lastName, AccountFormError errors) {
        if (notValidFieldLength(lastName, MIN_THREE, MAX_FIFTY))
            errors.setLastName("validation.text.error.from.three.to.fifty");
    }

    private void validateLastNameOriginLength(String lastNameOrigin, AccountFormError errors) {
        if (notValidFieldLength(lastNameOrigin, MIN_THREE, MAX_FIFTY))
            errors.setLastName("validation.text.error.from.three.to.fifty");
    }

    private void validateConfirmPassword(String password, String confirmPassword, AccountFormError errors) {
        if (!password.equals(confirmPassword))
            errors.setPassword("error.password");
    }

    private void validateUsernamePattern(String username, String pattern, AccountFormError errors) {
        Pattern r = Pattern.compile(pattern);
        if (!r.matcher(username).matches())
            errors.setUsername("validation.username.symbols");
    }

    private void validateEmailPattern(String email, String pattern, AccountFormError errors) {
        Pattern r = Pattern.compile(pattern);
        LOGGER.info(email + " : " + pattern);
        if (!r.matcher(email).matches())
            errors.setEmail("validation.email.symbols");
    }

    private void validatePhonePattern(String phone, String pattern, AccountFormError errors) {
        Pattern r = Pattern.compile(pattern);
        if (!r.matcher(phone).matches())
            errors.setPhone("validation.phone.symbols");
    }

    private void validateUsernameLength(String username, AccountFormError errors) {
        if (notValidFieldLength(username, MIN_SIX, MAX_FIFTY))
            errors.setUsername("validation.text.error.from.six.to.fifty");
    }

    private void validateEmailLength(String email, AccountFormError errors) {
        if (notValidFieldLength(email, MIN_SIX, MAX_FIFTY))
            errors.setEmail("validation.text.error.from.six.to.fifty");
    }

    private void validatePasswordLength(String password, AccountFormError errors) {
        if (notValidFieldLength(password, MIN_EIGHT, MAX_FIFTY))
            errors.setPassword("validation.text.error.from.eight.to.fifty");
    }

    private boolean notValidFieldLength(String field, int min, int max) {
        return (field.length() < min || field.length() > max);
    }

    private void validateUsername(String username, AccountFormError errors) {
        try {
            accountService.getAccountByUsername(username);
        } catch (WorkshopException e) {
            LOGGER.error("error: " + e.getMessage());
            return;
        }
        errors.setUsername("error.duplicate");
        LOGGER.info("Validation failed: duplicate username -> " + username);
    }

    private void validateEmail(String email, AccountFormError errors) {
        try {
            accountService.getAccountByEmail(email);
        } catch (WorkshopException e) {
            LOGGER.error("error: " + e.getMessage());
            return;
        }
        LOGGER.debug("Validation failed: duplicate email -> " + email);
        errors.setEmail("error.duplicate");
    }

    private void validatePhone(String phone, AccountFormError errors) {
        try {
            accountService.getAccountByPhone(phone);
        } catch (WorkshopException e) {
            LOGGER.error("error: " + e.getMessage());
            return;
        }
        LOGGER.debug("Validation failed: duplicate phone -> " + phone);
        errors.setPhone("error.duplicate");
    }
}
