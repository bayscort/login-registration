package com.sgedts.base.config.interceptor;

import com.sgedts.base.constant.LoggerConstant;
import com.sgedts.base.util.SecurityUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Component
public class RequestInterceptor implements HandlerInterceptor {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            String username = SecurityUtil.getUsername()
                    .orElse("_");
            MDC.put(LoggerConstant.MDC_USERNAME, username);

            String loggerId = request.getHeader(LoggerConstant.HEADER_LOGGER_ID);
            if (StringUtils.isNotBlank(loggerId)) {
                MDC.put(LoggerConstant.MDC_LOGGER_ID, loggerId);
            } else {
                MDC.put(LoggerConstant.MDC_LOGGER_ID, UUID.randomUUID().toString());
            }

        } catch (Exception e) {
            logger.error("Failed-RequestInterceptor", e);
        }
        return true;
    }

}
