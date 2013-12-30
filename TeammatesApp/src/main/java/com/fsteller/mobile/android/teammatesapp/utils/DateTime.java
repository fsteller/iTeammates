package com.fsteller.mobile.android.teammatesapp.utils;

import android.content.Context;
import android.text.format.DateUtils;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by fhernandezs on 27/12/13 for iTeammates.
 */
public final class DateTime {

    private DateTime() {
    }

    private static final String TAG = DateTime.class.getSimpleName();

    public static final String DateFormat = "dd/MM/yyyy";
    public static final String DateTimeFormat = "dd/MM/yyyy hh:mm:ss.SSS";

    public static String formatDateTime(Context context, String timeToFormat) {

        String finalDateTime = "";
        SimpleDateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date date = null;
        if (timeToFormat != null) {
            try {
                date = iso8601Format.parse(timeToFormat);
            } catch (ParseException e) {
                Log.e(TAG, "Error during formatting date: %s", e);
                date = null;
            }

            if (date != null) {
                int flags = 0;
                long when = date.getTime();
                flags |= DateUtils.FORMAT_SHOW_TIME;
                flags |= DateUtils.FORMAT_SHOW_DATE;
                flags |= DateUtils.FORMAT_ABBREV_MONTH;
                flags |= DateUtils.FORMAT_SHOW_YEAR;

                finalDateTime = DateUtils.formatDateTime(context, when + TimeZone.getDefault().getOffset(when), flags);
            }
        }
        return finalDateTime;
    }

    public static String getDate(long milliSeconds, String dateFormat) {
        // AddItem a DateFormatter object for displaying date in specified format.
        DateFormat formatter = new SimpleDateFormat(dateFormat);

        // AddItem a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
}
