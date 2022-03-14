package com.sgedts.base.messagebroker;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sgedts.base.constant.PendingMessageBrokerConstant;
import com.sgedts.base.service.PendingMessageBrokerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

@Service
public class Producer {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final Gson gson = new GsonBuilder().disableHtmlEscaping().serializeNulls().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create();

    private final PendingMessageBrokerService pendingMessageBrokerService;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public Producer(PendingMessageBrokerService pendingMessageBrokerService, KafkaTemplate<String, String> kafkaTemplate) {
        this.pendingMessageBrokerService = pendingMessageBrokerService;
        this.kafkaTemplate = kafkaTemplate;
    }

//    @Async
//    public void produceTopicMessage(Bean bean) {
//        if (null == bean) {
//            return;
//        }
//        String topic = TopicConstant.TOPIC_MESSAGE;
//        String message = gson.toJson(bean);
//        PendingMessageBrokerBean pendingMessageBrokerBean = pendingMessageBrokerMapper.toPendingMessageBrokerBean(topic, bean.getId(),
//                message, new Date(), PendingMessageBrokerConstant.PROCESSING);
//        long id = pendingMessageBrokerService.create(pendingMessageBrokerBean);
//        sendMessageWithId(topic, message, id);
//    }

    @Async
    public void producePendingMessageBroker(String topic, String message, long pendingMessageBrokerId) {
        sendMessageWithId(topic, message, pendingMessageBrokerId);
    }

    public void sendMessageWithId(String topic, String message, long pendingMessageBrokerId) {
        try {
            sendMessageListenableFuture(topic, message)
                    .addCallback(result -> pendingMessageBrokerService.updateToStatus(pendingMessageBrokerId, PendingMessageBrokerConstant.SENT),
                            throwable -> {
                                logger.warn("HighPriority Failed to send message with message {}", message, throwable);
                                pendingMessageBrokerService.updateToStatus(pendingMessageBrokerId, PendingMessageBrokerConstant.SCHEDULED);
                            });
        } catch (Exception e) {
            logger.warn("HighPriority Failed to send message with message {}", message, e);
            pendingMessageBrokerService.updateToStatus(pendingMessageBrokerId, PendingMessageBrokerConstant.SCHEDULED);
        }
    }

    /**
     * Send data to message broker
     *
     * @param topic   topic of message broker
     * @param message message data  of message broker
     */
    private void sendMessage(String topic, String message) {
        this.kafkaTemplate.send(topic, message);
    }

    /**
     * Send message to kafka.
     *
     * @param topic   topic
     * @param message message
     * @return {@link ListenableFuture} of this action
     */
    public ListenableFuture<SendResult<String, String>> sendMessageListenableFuture(String topic, String message) {
        return kafkaTemplate.send(topic, message, message);
    }
}
