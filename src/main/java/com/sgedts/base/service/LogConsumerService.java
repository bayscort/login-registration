package com.sgedts.base.service;

import com.sgedts.base.model.LogConsumer;
import com.sgedts.base.repository.LogConsumerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@Service
public class LogConsumerService {
    private final LogConsumerRepository logConsumerRepository;

    @Autowired
    public LogConsumerService(LogConsumerRepository logConsumerRepository) {
        this.logConsumerRepository = logConsumerRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public long create(String topic, String messageId, String data) {
        LogConsumer logConsumer = new LogConsumer();
        logConsumer.setTopic(topic);
        logConsumer.setMessageId(messageId);
        logConsumer.setData(data);
        logConsumer = logConsumerRepository.save(logConsumer);
        return logConsumer.getId();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateError(long id, String errorMessage) {
        if (id == 0) return;
        logConsumerRepository.findById(id).ifPresent(logConsumer -> {
            logConsumer.setErrorMessage(errorMessage);
            logConsumerRepository.save(logConsumer);
        });
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteOldMessage() {
        try {
            Date deleteBefore = Date.from(Instant.now().minus(Duration.ofDays(30)));
            logConsumerRepository.deleteBefore(deleteBefore);
        } catch (Exception ignored) {}
    }
}
