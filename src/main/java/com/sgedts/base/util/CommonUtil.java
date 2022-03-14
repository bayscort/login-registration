package com.sgedts.base.util;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Map;

/**
 * Collections of common utility functions.
 */
public final class CommonUtil {

    /**
     * Use to format template text to notification message.<br/>
     * mapMessage use to store keyValue to replace templateText with key tag {:KEY:}<br/>
     * Example :<br/>
     * - mapMessage : ["actionName", "Jhon Doe"]<br/>
     * - templateText  : "{:actionName:} has been created"<br/>
     * - result : "Jhon Doe has been created"<br/>
     *
     * @param mapMessage   keyValue to templateText
     * @param templateText template text
     * @return result of replacement text
     */
    public static String getFormattedText(Map<String, Object> mapMessage, String templateText) {
        String formattedText = templateText;

        for (Map.Entry<String, Object> entry : mapMessage.entrySet()) {
            String key = entry.getKey();
            String value = String.valueOf(entry.getValue());
            formattedText = StringUtils.replace(formattedText, "{:" + key + ":}", value);
            formattedText = StringUtils.replace(formattedText, "{::" + key + "::}", value);
        }

        return formattedText;
    }

    /**
     * Check if object is array or iterable object
     * will return false if null
     *
     * @param obj object to be check
     * @return true if object is array or iterable
     */
    public static boolean isArrayOrCollection(Object obj) {
        if (null != obj) {
            if (obj.getClass().isArray()) {
                return true;
            } else {
                return ClassUtils.isAssignable(obj.getClass(), Collection.class);
            }
        }
        return false;
    }

}
