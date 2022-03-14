package com.sgedts.base.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class MessageSourceService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    //    private final Locale defaultLocale = Locale.ENGLISH;
    private final Locale defaultLocale = new Locale("in", "ID");

    private final MessageSource messageSource;

    @Autowired
    public MessageSourceService(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String getMessage(String messageCode) {
        String message = messageCode;
        try {
            message = messageSource.getMessage(messageCode, null, defaultLocale);
        } catch (NoSuchMessageException e) {
            logger.error(e.getMessage(), e);
        }
        return message;
    }

    public String getMessage(String messageCode, Object[] args) {
        String message = messageCode;
        try {
            message = messageSource.getMessage(messageCode, args, defaultLocale);
        } catch (NoSuchMessageException e) {
            logger.error(e.getMessage(), e);
        }
        return message;
    }

    public String getMessage(String messageCode, Locale locale) {
        String message = messageCode;
        try {
            message = messageSource.getMessage(messageCode, null, locale);
        } catch (NoSuchMessageException e) {
            logger.error(e.getMessage(), e);
        }
        return message;
    }

    public String getMessage(String messageCode, Object[] args, Locale locale) {
        String message = messageCode;
        try {
            message = messageSource.getMessage(messageCode, args, locale);
        } catch (NoSuchMessageException e) {
            logger.error(e.getMessage(), e);
        }
        return message;
    }
}
