package com.sgedts.base.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sgedts.base.constant.LoggerConstant;
import com.sgedts.base.util.adapter.ISODateAdapter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;
import java.util.stream.Collectors;

@Service
public class LoggingService {
    private final String[] EXCLUDED_URL = {"/health"};

    @Value(value = "${services.my-config.log-request}")
    private String isLogRequest;
    @Value(value = "${services.my-config.log-response}")
    private String isLogResponse;
    @Value(value = "${services.my-config.log-request}")
    private String isLogKafka;

    private final DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    public LoggingService() {
        this.df.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    private final ObjectWriter om = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .setDateFormat(df)
            .writer();

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final Gson gson = new GsonBuilder().disableHtmlEscaping()
            .serializeNulls()
            .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
            .registerTypeAdapter(Date.class, new ISODateAdapter())
            .create();

    public void logRequest(HttpServletRequest request, Object obj) {
        try {
            if (!"1".equals(isLogRequest))
                return;
            if (StringUtils.startsWithAny(request.getRequestURI(), EXCLUDED_URL)) {
                return;
            }
            String method = request.getMethod();
            String requestURI = request.getRequestURI();
            String parameterMap = getParameterMap(request);
            String body = getBodyData(obj);
            printRequestLog(method, requestURI, parameterMap, body);
        } catch (Exception ex) {
            logger.error("Failed-logRequest", ex);
        }
    }

    public void logResponse(HttpServletRequest request, HttpServletResponse response, Object obj) {
        try {
            if (!"1".equals(isLogResponse))
                return;
            if (StringUtils.startsWithAny(request.getRequestURI(), EXCLUDED_URL)) {
                return;
            }
            String method = request.getMethod();
            String requestURI = request.getRequestURI();
            int responseCode = response.getStatus();
            String body = getBodyData(obj);

            printResponseLog(method, requestURI, responseCode, body);
        } catch (Exception ex) {
            logger.error("Failed-logResponse", ex);
        }
    }

    public void logKafkaConsumer(String username, String loggerId, String messageId, String data) {
        if (!"1".equals(isLogKafka))
            return;
        MDC.put(LoggerConstant.MDC_USERNAME, username);
        if (StringUtils.isBlank(loggerId)) {
            MDC.put(LoggerConstant.MDC_LOGGER_ID, messageId);
        } else {
            MDC.put(LoggerConstant.MDC_LOGGER_ID, loggerId);
        }

        String logRequest = "=== START-KAFKA-CONSUMER ===";
        if (StringUtils.isNotBlank(username)) {
            logRequest += "\n\tUSERNAME" + "\t : " + username;
        }
        if (StringUtils.isNotBlank(loggerId)) {
            logRequest += "\n\tLOGGER_ID" + "\t : " + loggerId;
        }
        if (StringUtils.isNotBlank(messageId)) {
            logRequest += "\n\tMESSAGE_ID" + "\t : " + messageId;
        }
        if (StringUtils.isNotBlank(data)) {
            logRequest += "\n\tDATA" + "\t\t : " + data;
        }
        logRequest += "\n=== END-KAFKA-CONSUMER ===";
        logger.info(logRequest);
    }

    private void printRequestLog(String method, String requestURI, String parameterMap, String body) {
        String logRequest = "=== START-REQUEST ===";
        logRequest += "\n\t[" + method + "] - [" + requestURI + "]";
        if (StringUtils.isNotBlank(parameterMap)) {
            logRequest += "\n\tPARAMETER_MAP" + "\t : " + parameterMap;
        }
        if (StringUtils.isNotBlank(body)) {
            logRequest += "\n\tREQUEST_BODY" + "\t : " + body;
        }
        logRequest += "\n=== END-REQUEST ===";
        logger.info(logRequest);
    }

    private void printResponseLog(String method, String requestURI, int responseCode, String body) {
        String logResponse = "=== START-RESPONSE ===";
        logResponse += "\n\t[" + method + "] - [" + requestURI + "]";
        logResponse += "\n\tRESPONSE_CODE" + "\t : " + responseCode;
        if (StringUtils.isNotBlank(body)) {
            logResponse += "\n\tRESPONSE_BODY" + "\t : " + body;
        }
        logResponse += "\n=== END-RESPONSE ===";
        logger.info(logResponse);
    }

    private String getBodyData(Object obj) {
        String body = "";
        if (null != obj) {
            if (obj instanceof String) {
                body = (String) obj;
            } else {
                try {
                    body = om.writeValueAsString(obj);
                } catch (Exception ex) {
                    logger.error("Failed-getBodyData-objectMapper", ex);
                    body = gson.toJson(obj);
                }
            }
        }
        return body;
    }

    private String getParameterMap(HttpServletRequest request) {
        String parameterMap = "";
        if (null != request.getParameterMap() && request.getParameterMap().size() > 0) {
            parameterMap = request.getParameterMap()
                    .entrySet()
                    .stream()
                    .map((entry) -> {
                        if (null == entry.getValue()) {
                            return entry.getKey() + "=" + null;
                        } else {
                            return Arrays.stream(entry.getValue())
                                    .map(val -> entry.getKey() + "=" + val)
                                    .collect(Collectors.joining("&"));
                        }
                    })
                    .collect(Collectors.joining("&"));
        }
        return parameterMap;
    }

}
