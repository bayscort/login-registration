package com.sgedts.base.config.interceptor;

import com.sgedts.base.exception.SessionNotValidException;
import com.sgedts.base.model.Session;
import com.sgedts.base.service.SessionService;
import com.sgedts.base.service.internal.AuthService;
import com.sgedts.base.util.SecurityUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class SessionAuthorizationInterceptor implements HandlerInterceptor {
    private final SessionService sessionService;
    private final AuthService authService;

    @Value(value = "${services.my-config.single-signon}")
    private String isSingleSignOn;

    @Autowired
    public SessionAuthorizationInterceptor(SessionService sessionService, AuthService authService) {
        this.sessionService = sessionService;
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String requestURI = request.getRequestURI();
        if (!"1".equals(isSingleSignOn))
            return true;

        if (StringUtils.startsWithIgnoreCase(requestURI, "/api/back-office")) {
            boolean isValid;
            Session session = sessionService.getSessionById(SecurityUtil.getSessionId().orElseThrow());
            if (session == null) {
                isValid = authService.checkValidation();
            } else {
                isValid = session.isValid();
            }
            if (!isValid) {
                throw new SessionNotValidException();
            }
        }
        return true;
    }

}
