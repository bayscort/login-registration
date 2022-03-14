package com.sgedts.base.constant.enums;

public enum MessageResourceEnum {
    SUCCESS("00"),
    SUCCESS_WITH_MESSAGE("01"),
    FAILED("02"),
    FAILED_WITH_MESSAGE("03"),

    SESSION_INVALID("U000", "session.invalid"),
    REQUEST_NOT_VALID("R000", "request.not.valid"),

    INVALID_FILE_SIZE("T002", "invalid.file.size"),
    INVALID_FILE("T003", "file.invalid"),
    INVALID_TEMPLATE_FILE("T004", "invalid.template.file"),
    FILE_NOT_FOUND("T005", "file.not.found"),
    FILE_HAS_BEEN_PROCESSED("T006", "file.has.been.processed");

    private String code;
    private String property;
    private String message;

    MessageResourceEnum(String code) {
        this.code = code;
    }

    MessageResourceEnum(String code, String property) {
        this.code = code;
        this.property = property;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
