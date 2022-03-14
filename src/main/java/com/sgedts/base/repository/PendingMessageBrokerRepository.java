package com.sgedts.base.repository;

import com.sgedts.base.constant.PendingMessageBrokerConstant;
import com.sgedts.base.model.PendingMessageBroker;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

/**
 * Repository for {@link PendingMessageBroker} entity.
 */

public interface PendingMessageBrokerRepository extends CrudRepository<PendingMessageBroker, Long> {
    List<PendingMessageBroker> findAllByStatus(Pageable pageable, String status);

    @Modifying
    @Transactional
    @Query(value = "update PendingMessageBroker " +
            " set status = '" + PendingMessageBrokerConstant.PROCESSING + "' " +
            " where id in (:listId) " +
            " and status = '" + PendingMessageBrokerConstant.SCHEDULED + "'")
    void updateScheduledToProcessing(@Param("listId") List<Long> listId);

    @Modifying
    @Transactional
    @Query("delete from PendingMessageBroker " +
            " where status = '" + PendingMessageBrokerConstant.SENT + "' " +
            " and sendDate < :dateBefore")
    void deleteSentBefore(@Param("dateBefore") Date dateBefore);
}
