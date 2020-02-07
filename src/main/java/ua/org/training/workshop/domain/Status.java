package ua.org.training.workshop.domain;

import java.util.Collection;
import java.util.HashSet;

/**
 * @author kissik
 */
public class Status {
    private Long id;
    private String code;
    private String name;
    private boolean close;
    private Collection<Status> nextStatuses = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public boolean isClose() {
        return close;
    }

    public void setClose(boolean close) {
        this.close = close;
    }

    public Collection<Status> getNextStatuses() {
        return nextStatuses;
    }

    public void setNextStatuses(Collection<Status> nextStatuses) {
        this.nextStatuses = nextStatuses;
    }

    public int hashCode() {
        return getCode().hashCode();
    }

    public String toString() {
        return this.code + " " + this.name + ": " + this.nextStatuses.toArray().toString();
    }
}
