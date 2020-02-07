package ua.org.training.workshop.web.dto;

import javafx.util.Pair;
import ua.org.training.workshop.domain.Status;
import ua.org.training.workshop.service.StatusService;
import ua.org.training.workshop.utility.ApplicationConstants;
import ua.org.training.workshop.utility.Utility;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * @author kissik
 */
public class StatusDTO {
    private String code;
    private String name;
    private String value;
    private List nextStatuses;

    public StatusDTO(Locale locale, Status status) {
        this.code = status.getCode();
        this.name = status.getName();
        this.value = Utility.getBundleMessage(locale, ApplicationConstants.BUNDLE_REQUEST_STATUS_PREFIX
                + code.toLowerCase());
        StatusService statusService = new StatusService();
        this.nextStatuses = statusService
                .findByCode(status.getCode())
                .getNextStatuses()
                .stream()
                .map(nextStatus ->
                        new Pair(
                                nextStatus.getCode(),
                                Utility.getBundleMessage(locale,
                                        ApplicationConstants.BUNDLE_REQUEST_STATUS_PREFIX
                                                + nextStatus.getCode().toLowerCase()))
                )
                .collect(Collectors.toList());
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List getNextStatuses() {
        return nextStatuses;
    }

    public void setNextStatuses(List nextStatuses) {
        this.nextStatuses = nextStatuses;
    }

    @Override
    public String toString() {
        return this.code + " " + this.name + ": " + this.nextStatuses.toString();
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
