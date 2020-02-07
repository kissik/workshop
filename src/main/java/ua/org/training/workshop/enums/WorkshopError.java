package ua.org.training.workshop.enums;

public enum WorkshopError {

    PRICE_NOT_FOUND_ERROR(10, "error.price.not.found"),
    CAUSE_NOT_FOUND_ERROR(15, "error.cause.not.found"),
    COMMENT_NOT_FOUND_ERROR(20, "error.comment.not.found"),
    RATING_NOT_FOUND_ERROR(25, "error.rating.not.found"),

    DATABASE_CONNECTION_ERROR(100, "error.db.connection"),

    ACCOUNT_NOT_FOUND_ERROR(200, "error.account.not.found"),
    ACCOUNT_CREATE_NEW_ERROR(201, "error.account.create"),
    ACCOUNT_LIST_IS_EMPTY_ERROR(202, "error.account.list.empty"),
    ACCOUNT_UPDATE_ERROR(210, "error.account.update"),

    ROLE_NOT_FOUND_ERROR(250, "error.role.not.found"),
    ROLE_LIST_IS_EMPTY_ERROR(251, "error.role.list.empty"),

    REQUEST_NOT_FOUND_ERROR(300, "error.request.not.found"),
    REQUEST_CREATE_NEW_ERROR(301, "error.request.create"),
    REQUEST_LIST_IS_EMPTY_ERROR(302, "error.request.list.empty"),

    REQUEST_HISTORY_NOT_FOUND_ERROR(330, "error.request.history.not.found"),
    REQUEST_LIST_HISTORY_IS_EMPTY_ERROR(331, "error.request.history.list.not.found"),

    STATUS_NOT_FOUND_ERROR(350, "error.status.not.found"),

    RIGHT_VIOLATION_ERROR(500, "error.right.violation"),

    NPE(600, "error.null.pointer.exception");

    private int code;
    private String message;

    WorkshopError(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int code() {
        return code;
    }

    public String message() {
        return message;
    }
}
