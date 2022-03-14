package com.sgedts.base.bean;

import java.util.Date;

public class SessionBean extends LoggerBean {
    private String messageId;
    private boolean valid;
    private String email;
    private String sessionId;
    private Date authModifiedDate;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Date getAuthModifiedDate() {
        return authModifiedDate;
    }

    public void setAuthModifiedDate(Date authModifiedDate) {
        this.authModifiedDate = authModifiedDate;
    }
}
