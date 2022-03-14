package com.sgedts.base.model;

import com.sgedts.base.base.BaseEntity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(
        indexes = {
                @Index(columnList = "status"),
                @Index(columnList = "topic")
        }
)
public class PendingMessageBroker extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String topic;
    private String messageId;

    @Column(columnDefinition = "TEXT")
    private String data;

    private Date sendDate;
    private String status;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date send_date) {
        this.sendDate = send_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "PendingMessageBroker{" +
                "id=" + id +
                ", topic='" + topic + '\'' +
                ", messageId='" + messageId + '\'' +
                ", data='" + data + '\'' +
                ", sendDate=" + sendDate +
                ", status='" + status + '\'' +
                '}';
    }
}
