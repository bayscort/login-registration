package com.sgedts.base.repository;

import com.sgedts.base.model.Session;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import java.util.Optional;

public interface SessionRepository extends CrudRepository<Session, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s " +
            "FROM Session s " +
            "WHERE s.sessionId = :sessionId")
    Optional<Session> getSessionBySessionId(@Param("sessionId") String sessionId);

    Optional<Session> findBySessionId(String sessionId);
}
