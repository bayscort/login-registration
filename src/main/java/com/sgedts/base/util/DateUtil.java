package com.sgedts.base.util;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Collections of date utility functions.
 */
public final class DateUtil {

    public static Date atEndOfDay(Date date) {
        return (null != date) ? DateUtils.addMilliseconds(DateUtils.ceiling(date, Calendar.DATE), -3) : null;
    }

    public static Date subtractDays(Date date, int days) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.DATE, -days);

        return cal.getTime();
    }

    public static Date addDays(Date date, int days) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);

        return cal.getTime();
    }

    public static Date atStartOfDay(Date date) {
        return (null != date) ? DateUtils.truncate(date, Calendar.DATE) : null;
    }

    public static long getDiffDays(Date date1, Date date2) {
        long diff = atEndOfDay(date2).getTime() - atEndOfDay(date1).getTime();
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    public static String instantDateStr(Date date) {
        return (null == date) ? null : date.toInstant().toString();
    }

    public static String toDateFormat(Date date) {
        return (null == date) ? null : DateFormatUtils.format(date, "dd-MM-yyyy");
    }

    public static String isoDateFormatter(Date date) {
        SimpleDateFormat sdf;
        sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(date);
    }

}
