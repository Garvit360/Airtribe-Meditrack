package com.airtribe.meditrack.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    private static final SimpleDateFormat dateFormat =
            new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public static String formatDateTime(long timestamp) {
        return dateFormat.format(new Date(timestamp));
    }

    public static long parseDateTime(String dateTimeStr) throws ParseException {
        return dateFormat.parse(dateTimeStr).getTime();
    }

    public static boolean isFutureDate(long timestamp) {
        return timestamp > System.currentTimeMillis();
    }
}
