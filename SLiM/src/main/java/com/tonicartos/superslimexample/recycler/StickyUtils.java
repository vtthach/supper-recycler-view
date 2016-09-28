package com.tonicartos.superslimexample.recycler;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by thach.vo on 27/09/2016.
 */
public class StickyUtils extends StickyItemImpl<Long, String> {

    public static long getBeginOfDay(long dateTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dateTime);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    public static String getFormatDateLong(long milisecond) {
        return DateFormat.getDateInstance(DateFormat.LONG)
                .format(new Date(milisecond));
    }

    public static String getFormatDate(long milisecond, String format) {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.getDefault());
        return simpleDateFormat.format(new Date(milisecond));
    }
}
