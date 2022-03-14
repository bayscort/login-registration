package com.sgedts.base.messagebroker;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sgedts.base.bean.SessionBean;
import com.sgedts.base.constant.TopicConstant;
import com.sgedts.base.exception.BadRequestException;
import com.sgedts.base.service.LogConsumerService;
import com.sgedts.base.service.LoggingService;
import com.sgedts.base.service.SessionService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class Consumer {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final Gson gson = new GsonBuilder().disableHtmlEscaping().serializeNulls().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create();

    private final LoggingService loggingService;
    private final LogConsumerService logConsumerService;
    private final SessionService sessionService;

    @Autowired
    public Consumer(LoggingService loggingService, LogConsumerService logConsumerService, SessionService sessionService) {
        this.loggingService = loggingService;
        this.logConsumerService = logConsumerService;
        this.sessionService = sessionService;
    }

    @KafkaListener(topics = TopicConstant.SYNC_SESSION)
    public void syncSession(String message) {
        long idLogConsumer = 0;
        try {
            SessionBean sessionBean = gson.fromJson(message, SessionBean.class);
            String messageId = (StringUtils.isNotBlank(sessionBean.getMessageId())) ? sessionBean.getMessageId() : generateUniqueId();
            loggingService.logKafkaConsumer(sessionBean.getUsername(), sessionBean.getLoggerId(), sessionBean.getMessageId(), message);
            idLogConsumer = logConsumerService.create(TopicConstant.SYNC_SESSION, messageId, message);
            sessionService.syncSession(sessionBean);
        } catch (Exception ex) {
            logConsumerService.updateError(idLogConsumer, ex.getMessage());
            handleMessageBrokerErrorMessage(TopicConstant.SYNC_SESSION, message, ex);
        }
    }

    private void handleMessageBrokerErrorMessage(String topic, String topicMessage, Exception ex) {
        logger.error("Failed to process consumer, with topic : {}, message : {}, Error : {}", topic, topicMessage, ex.getMessage());
        throw new BadRequestException(ex.getMessage());
    }

    private String generateUniqueId() {
        String uniqueIdPrefix = "generated-";
        String date = DateFormatUtils.format(new Date(), "yyyyMMdd");
        String uuid = UUID.randomUUID().toString();
        return uniqueIdPrefix + date + "-" + uuid;
    }
}
