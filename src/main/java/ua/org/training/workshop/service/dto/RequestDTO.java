package ua.org.training.workshop.service.dto;

import ua.org.training.workshop.domain.Request;
import ua.org.training.workshop.utilities.UtilitiesClass;

import java.util.Locale;

/**
 * @author kissik
 */
public class RequestDTO {
    private String title;
    private String description;
    private StatusDTO status;
    private AccountDTO author;
    private AccountDTO user;
    private String closed = "app.literal.false";
    private String dateCreated;
    private String dateUpdated;
    private String price;
    private String cause;
    private String language;

    public RequestDTO(Locale locale, Request request){
        this.title = request.getTitle();
        this.description = request.getDescription();
        this.status = new StatusDTO(locale, request.getStatus());
        this.author = new AccountDTO(request.getAuthor());
        this.user = new AccountDTO(request.getUser());
        this.dateCreated = UtilitiesClass.getLocaleDate(locale, request.getDateCreated());
        this.dateUpdated = UtilitiesClass.getLocaleDate(locale, request.getDateUpdated());
        this.price = UtilitiesClass.getLocalePrice(locale, request.getPrice());
        this.cause = UtilitiesClass.getParameterString(request.getCause(),
                UtilitiesClass.APP_STRING_DEFAULT_VALUE);
        this.language = request.getLanguage();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public StatusDTO getStatus() {
        return status;
    }

    public void setStatus(StatusDTO status) {
        this.status = status;
    }

    public AccountDTO getUser() {
        return user;
    }

    public void setUser(AccountDTO user) {
        this.user = user;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(String dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public String toString(){
        return this.title + ": " + this.description;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public AccountDTO getAuthor() {
        return author;
    }

    public void setAuthorDTO(AccountDTO author) {
        this.author = author;
    }

    public String getClosed() {
        return closed;
    }

    public void setClosed(String closed) {
        this.closed = closed;
    }
}
