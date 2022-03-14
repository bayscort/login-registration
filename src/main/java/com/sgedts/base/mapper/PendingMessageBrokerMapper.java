package com.sgedts.base.mapper;

import com.sgedts.base.bean.PendingMessageBrokerBean;
import com.sgedts.base.model.PendingMessageBroker;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Date;
import java.util.List;

@Mapper(componentModel = "spring")
public interface PendingMessageBrokerMapper {
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "modifiedDate", ignore = true)
    PendingMessageBroker toEntity(PendingMessageBrokerBean pendingMessageBrokerBean);

    PendingMessageBrokerBean toBean(PendingMessageBroker pendingMessageBroker);

    List<PendingMessageBrokerBean> toBean(List<PendingMessageBroker> pendingMessageBroker);

    default PendingMessageBrokerBean toPendingMessageBrokerBean(String topic, String messageId, String data, Date sendDate, String status) {
        PendingMessageBrokerBean pendingMessageBrokerBean = new PendingMessageBrokerBean();
        pendingMessageBrokerBean.setTopic(topic);
        pendingMessageBrokerBean.setMessageId(messageId);
        pendingMessageBrokerBean.setData(data);
        pendingMessageBrokerBean.setSendDate(sendDate);
        pendingMessageBrokerBean.setStatus(status);
        return pendingMessageBrokerBean;
    }
}
