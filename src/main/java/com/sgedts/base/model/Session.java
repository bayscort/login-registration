package com.sgedts.base.model;

import com.sgedts.base.base.BaseEntity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"sessionId"})
        }
)
public class Session extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private boolean valid;
    private String email;
    private String sessionId;
    private Date authModifiedDate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
