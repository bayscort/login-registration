package com.sgedts.base.repository;

import com.sgedts.base.model.LogConsumer;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Date;

public interface LogConsumerRepository extends CrudRepository<LogConsumer, Long> {
    @Modifying
    @Transactional
    @Query("delete from LogConsumer " +
            " where createdDate < :dateBefore")
    void deleteBefore(@Param("dateBefore") Date dateBefore);
}
