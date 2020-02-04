package ua.org.training.workshop.service.dto;

import ua.org.training.workshop.domain.HistoryRequest;
import ua.org.training.workshop.utilities.UtilitiesClass;

import javax.rmi.CORBA.Util;
import java.util.Locale;

/**
 * @author kissik
 */
public class HistoryRequestDTO {
    private String title;
    private String description;
    private StatusDTO status;
    private AccountDTO author;
    private AccountDTO user;
    private String dateCreated;
    private String dateUpdated;
    private String price;
    private String cause;
    private String language;
    private String review;
    private Long rating;

    public HistoryRequestDTO(Locale locale, HistoryRequest historyRequest){
        this.title = historyRequest.getTitle();
        this.description = historyRequest.getDescription();
        this.status = new StatusDTO(locale, historyRequest.getStatus());
        this.author = new AccountDTO(historyRequest.getAuthor());
        this.user = new AccountDTO(historyRequest.getUser());
        this.dateCreated = UtilitiesClass.getLocaleDate(locale, historyRequest.getDateCreated());
        this.dateUpdated = UtilitiesClass.getLocaleDate(locale, historyRequest.getDateUpdated());
        this.price = UtilitiesClass.getLocalePrice(locale, historyRequest.getPrice());
        this.cause = UtilitiesClass.getParameterString(
                historyRequest.getCause(),
                UtilitiesClass.APP_STRING_DEFAULT_VALUE);
        this.language = historyRequest.getLanguage();
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

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public Long getRating() {
        return rating;
    }

    public void setRating(Long rating) {
        this.rating = rating;
    }
}
