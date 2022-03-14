package com.sgedts.base.service;

import com.sgedts.base.bean.SessionBean;
import com.sgedts.base.mapper.SessionMapper;
import com.sgedts.base.model.Session;
import com.sgedts.base.repository.SessionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SessionService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final SessionRepository sessionRepository;
    private final SessionMapper sessionMapper;

    @Autowired
    public SessionService(SessionRepository sessionRepository, SessionMapper sessionMapper) {
        this.sessionRepository = sessionRepository;
        this.sessionMapper = sessionMapper;
    }

    @Transactional(readOnly = true)
    public Session getSessionById(String sessionId) {
        return sessionRepository.findBySessionId(sessionId)
                .orElse(null);
    }

    @Async
    @Transactional
    public void syncSession(SessionBean sessionBean) {
        logger.debug("Sync session with sessionId : {}", sessionBean.getSessionId());

        sessionRepository.getSessionBySessionId(sessionBean.getSessionId())
                .ifPresentOrElse(session -> {
                    if (sessionBean.getAuthModifiedDate().getTime() >= session.getAuthModifiedDate().getTime()) {
                        session.setValid(sessionBean.isValid());
                        session.setAuthModifiedDate(sessionBean.getAuthModifiedDate());
                        sessionRepository.save(session);
                    }
                }, () -> {
                    Session session = sessionMapper.toEntity(sessionBean);
                    sessionRepository.save(session);
                });
    }
}
