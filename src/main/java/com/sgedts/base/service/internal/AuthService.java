package com.sgedts.base.service.internal;

import com.sgedts.base.bean.GeneralWrapper;
import com.sgedts.base.bean.internal.TokenValidationRequest;
import com.sgedts.base.bean.internal.TokenValidationResponse;
import com.sgedts.base.constant.LoggerConstant;
import com.sgedts.base.util.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedClientException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Optional;

@Service
public class AuthService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final RestTemplate restTemplate;

    @Value("${services.user.check-session}")
    private String AUTH_CHECK_SESSION_URL;

    @Autowired
    public AuthService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean checkValidation() {
        try {
            TokenValidationRequest request = new TokenValidationRequest();
            request.setToken(SecurityUtil.getToken());

            HttpHeaders headers = new HttpHeaders();
            headers.set(LoggerConstant.HEADER_LOGGER_ID, MDC.get(LoggerConstant.MDC_LOGGER_ID));

            ResponseEntity<GeneralWrapper<TokenValidationResponse>> response = restTemplate
                    .exchange(new URI(AUTH_CHECK_SESSION_URL),
                            HttpMethod.POST, new HttpEntity<>(request, headers),
                            new ParameterizedTypeReference<>() {
                            });
            return Optional.ofNullable(response.getBody())
                    .map(GeneralWrapper::getData)
                    .map(TokenValidationResponse::isValid)
                    .orElseThrow(() -> new UnauthorizedClientException(request.getToken()));
        } catch (Exception ex) {
            logger.error("failed to validate token : {}", ex.getMessage());
            return false;
        }
    }
}
