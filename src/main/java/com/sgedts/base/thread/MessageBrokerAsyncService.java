package com.sgedts.base.thread;

import com.sgedts.base.bean.PendingMessageBrokerBean;
import com.sgedts.base.messagebroker.Producer;
import com.sgedts.base.service.LogConsumerService;
import com.sgedts.base.service.PendingMessageBrokerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageBrokerAsyncService {
    private final PendingMessageBrokerService pendingMessageBrokerService;
    private final LogConsumerService logConsumerService;
    private final Producer producer;

    @Autowired
    public MessageBrokerAsyncService(PendingMessageBrokerService pendingMessageBrokerService, LogConsumerService logConsumerService, Producer producer) {
        this.pendingMessageBrokerService = pendingMessageBrokerService;
        this.logConsumerService = logConsumerService;
        this.producer = producer;
    }

    @Async
    public void processAsync() {
        List<PendingMessageBrokerBean> scheduled;
        try {
            scheduled = pendingMessageBrokerService.listScheduled();
        } catch (Exception e) {
            return;
        }

        if (null != scheduled && scheduled.size() > 0) {
            scheduled.forEach(pendingMessageBrokerBean -> producer.producePendingMessageBroker(pendingMessageBrokerBean.getTopic(),
                    pendingMessageBrokerBean.getData(),
                    pendingMessageBrokerBean.getId()));
        }
    }

    @Async
    public void delete() {
        logConsumerService.deleteOldMessage();
        pendingMessageBrokerService.deleteOldMessage();
    }

}
