package com.sgedts.base.exception;

import com.sgedts.base.bean.GeneralWrapper;
import com.sgedts.base.constant.enums.MessageResourceEnum;
import com.sgedts.base.service.MessageSourceService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.util.WebUtils;

/**
 * Handler for all exceptions and generate response in json format.
 */
@RestControllerAdvice
public class MessageExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(MessageExceptionHandler.class);
    private final MessageSourceService messageSourceService;

    @Autowired
    public MessageExceptionHandler(MessageSourceService messageSourceService) {
        this.messageSourceService = messageSourceService;
    }

    /**
     * Handle general error message.
     *
     * @param ex exception to be handled
     * @return response entity with error message
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> general(Exception ex) {
        handleErrorMessage(ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new GeneralWrapper().fail(ex, "Unexpected Error"));
    }

    /**
     * Handle unauthorized error message.
     *
     * @param ex exception to be handled
     * @return response entity with error message
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> unauthorized(AuthenticationException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new GeneralWrapper().fail(ex, HttpStatus.UNAUTHORIZED.getReasonPhrase()));
    }

    @ExceptionHandler(SessionNotValidException.class)
    public ResponseEntity<?> sessionNotValid(SessionNotValidException ex) {
        MessageResourceEnum sessionNotValid = MessageResourceEnum.SESSION_INVALID;
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new GeneralWrapper().fail(sessionNotValid.getCode(), messageSourceService.getMessage(sessionNotValid.getProperty())));
    }

    /**
     * Handle forbidden error message.
     *
     * @param ex exception to be handled
     * @return response entity with error message
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> forbidden(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new GeneralWrapper().fail(ex, HttpStatus.FORBIDDEN.getReasonPhrase()));
    }

    /**
     * Handle bad request message.
     *
     * @param ex exception to be handled
     * @return response entity with error message
     */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> badRequest(BadRequestException ex) {
        if (StringUtils.isNotBlank(ex.getCode()) &&
                StringUtils.isNotBlank(ex.getProperty())) {
            if (ex.isLogErrorMessage()) {
                handleErrorMessage(ex);
            }
            String message = (StringUtils.isBlank(ex.getErrorMessage()))
                    ? messageSourceService.getMessage(ex.getProperty())
                    : ex.getErrorMessage();
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new GeneralWrapper().fail(ex.getCode(), message));
        } else {
            if (ex.isLogErrorMessage()) {
                handleErrorMessage(ex);
            }
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new GeneralWrapper().fail(ex));
        }
    }

    /**
     * A single place to customize the response body of all exception types.
     * <p>The default implementation sets the {@link WebUtils#ERROR_EXCEPTION_ATTRIBUTE}
     * request attribute and creates a {@link ResponseEntity} from the given
     * body, headers, and status.
     *
     * @param ex      the exception
     * @param body    the body for the response
     * @param headers the headers for the response
     * @param status  the response status
     * @param request the current request
     */
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        handleErrorMessage(ex);
        super.handleExceptionInternal(ex, body, headers, status, request);
        return ResponseEntity
                .status(status)
                .headers(headers)
                .body(new GeneralWrapper().fail(ex, status.getReasonPhrase()));
    }

    private void handleErrorMessage(Exception ex) {
        logger.error("Error : ", ex);
    }
}
