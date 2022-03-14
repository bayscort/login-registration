package com.sgedts.base.service;

import com.sgedts.base.bean.PendingMessageBrokerBean;
import com.sgedts.base.constant.PendingMessageBrokerConstant;
import com.sgedts.base.mapper.PendingMessageBrokerMapper;
import com.sgedts.base.model.PendingMessageBroker;
import com.sgedts.base.repository.PendingMessageBrokerRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PendingMessageBrokerService {
    private final PendingMessageBrokerRepository pendingMessageBrokerRepository;
    private final PendingMessageBrokerMapper pendingMessageBrokerMapper;

    public PendingMessageBrokerService(PendingMessageBrokerRepository pendingMessageBrokerRepository, PendingMessageBrokerMapper pendingMessageBrokerMapper) {
        this.pendingMessageBrokerRepository = pendingMessageBrokerRepository;
        this.pendingMessageBrokerMapper = pendingMessageBrokerMapper;
    }

    public long create(PendingMessageBrokerBean pendingMessageBrokerBean) {
        PendingMessageBroker pendingMessageBroker = pendingMessageBrokerMapper.toEntity(pendingMessageBrokerBean);
        pendingMessageBroker = pendingMessageBrokerRepository.save(pendingMessageBroker);
        return pendingMessageBroker.getId();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<PendingMessageBrokerBean> listScheduled() {
        List<PendingMessageBrokerBean> listResult = pendingMessageBrokerRepository.findAllByStatus(PageRequest.of(0, 500), PendingMessageBrokerConstant.SCHEDULED)
                .stream()
                .map(pendingMessageBrokerMapper::toBean)
                .collect(Collectors.toList());

        List<Long> listId = listResult.stream()
                .map(PendingMessageBrokerBean::getId)
                .collect(Collectors.toList());
        pendingMessageBrokerRepository.updateScheduledToProcessing(listId);
        return listResult;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteOldMessage() {
        try {
            Date deleteBefore = Date.from(Instant.now().minus(Duration.ofDays(30)));
            pendingMessageBrokerRepository.deleteSentBefore(deleteBefore);
        } catch (Exception ignored) {}
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateToStatus(Long id, String status) {
        pendingMessageBrokerRepository.findById(id)
                .ifPresent(pendingMessageBroker -> {
                    pendingMessageBroker.setStatus(status);
                    pendingMessageBroker.setModifiedDate(new Date());
                    pendingMessageBrokerRepository.save(pendingMessageBroker);
                });
    }
}
