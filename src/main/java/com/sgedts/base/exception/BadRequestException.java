package com.sgedts.base.exception;

import com.sgedts.base.constant.enums.MessageResourceEnum;

/**
 * {@link Exception} that will be thrown when not found.
 */
public class BadRequestException extends RuntimeException {
    private MessageResourceEnum messageResourceEnum;
    private String code;
    private String property;
    private String errorMessage;
    private boolean isLogErrorMessage = true;

    public BadRequestException(MessageResourceEnum messageResourceEnum) {
        super(messageResourceEnum.getCode());
        this.messageResourceEnum = messageResourceEnum;
        this.code = messageResourceEnum.getCode();
        this.property = messageResourceEnum.getProperty();
    }

    public BadRequestException(MessageResourceEnum messageResourceEnum, boolean isLogErrorMessage) {
        super(messageResourceEnum.getCode() + " - " + messageResourceEnum.getProperty());
        this.messageResourceEnum = messageResourceEnum;
        this.code = messageResourceEnum.getCode();
        this.property = messageResourceEnum.getProperty();
        this.isLogErrorMessage = isLogErrorMessage;
    }

    public BadRequestException(MessageResourceEnum messageResourceEnum, String errorMessage) {
        super(messageResourceEnum.getCode() + " - " + messageResourceEnum.getProperty() + " - " + errorMessage);
        this.messageResourceEnum = messageResourceEnum;
        this.code = messageResourceEnum.getCode();
        this.property = messageResourceEnum.getProperty();
        this.errorMessage = errorMessage;
    }

    public BadRequestException(String message) {
        super(message);
        this.errorMessage = message;
    }

    public BadRequestException(String message, boolean isLogErrorMessage) {
        super(message);
        this.errorMessage = message;
        this.isLogErrorMessage = isLogErrorMessage;
    }

    public MessageResourceEnum getMessageResourceEnum() {
        return messageResourceEnum;
    }

    public String getCode() {
        return code;
    }

    public String getProperty() {
        return property;
    }

    public boolean isLogErrorMessage() {
        return isLogErrorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
