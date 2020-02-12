package ua.org.training.workshop.web.dto;

import ua.org.training.workshop.domain.HistoryRequest;
import ua.org.training.workshop.utility.ApplicationConstants;
import ua.org.training.workshop.utility.Utility;

import java.util.Locale;

/**
 * @author kissik
 */
public class HistoryRequestDTO {
    private Long id;
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

    public HistoryRequestDTO(Locale locale, HistoryRequest historyRequest) {
        this.id = historyRequest.getId();
        this.title = historyRequest.getTitle();
        this.description = historyRequest.getDescription();
        this.status = new StatusDTO(locale, historyRequest.getStatus());
        this.author = new AccountDTO(historyRequest.getAuthor());
        this.user = new AccountDTO(historyRequest.getUser());
        this.dateCreated = Utility.getLocaleDate(locale, historyRequest.getDateCreated());
        this.dateUpdated = Utility.getLocaleDate(locale, historyRequest.getDateUpdated());
        this.price = Utility.getLocalePrice(locale, historyRequest.getPrice());
        this.cause = Utility.getParameterString(
                historyRequest.getCause(),
                ApplicationConstants.APP_STRING_DEFAULT_VALUE);
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

    public String toString() {
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
