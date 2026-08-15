package com.airtribe.meditrack.util;

import com.airtribe.meditrack.constants.Constants;

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

    public static boolean slotsOverlap(long startA, long startB, long durationMs) {
        return startA < startB + durationMs && startB < startA + durationMs;
    }

    public static boolean sameCalendarDay(long a, long b) {
        SimpleDateFormat day = new SimpleDateFormat(Constants.DATE_FORMAT);
        return day.format(new Date(a)).equals(day.format(new Date(b)));
    }
}
