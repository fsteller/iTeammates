package com.fsteller.mobile.android.teammatesapp.model;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.fsteller.mobile.android.teammatesapp.TC;
import com.fsteller.mobile.android.teammatesapp.model.base.AbstractEntity;
import com.fsteller.mobile.android.teammatesapp.model.base.IEventsEntity;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Project: iTeammates
 * Subpackage: model
 * <p/>
 * Description: this class represents iTeammates Event (to be) stored at the database.
 * It must be used in order to get access to an event data,
 * or to make a new entity that would become a bundle in order to persist it.
 * <p/>
 * Created by fhernandezs on 13/02/14.
 */
public class EventsEntity extends AbstractEntity implements IEventsEntity {

    //<editor-fold desc="Constants">

    public static final int TEAMS = TC.Activity.Maintenance.TEAMS;
    public static final int CONTACTS = TC.Activity.Maintenance.CONTACTS;
    private static final String TAG = EventsEntity.class.getSimpleName();
    private static final Calendar mCalendar = new GregorianCalendar();

    //</editor-fold>
    //<editor-fold desc="Variables">

    private Callback mCallback = null;

    private int minutesTo = 50;
    private int minutesFrom = 0;
    private int calendarId = -1;
    private int yearFrom = mCalendar.get(Calendar.YEAR);
    private int yearTo = mCalendar.get(Calendar.YEAR);
    private int monthFrom = mCalendar.get(Calendar.MONTH);
    private int monthTo = mCalendar.get(Calendar.MONTH);
    private int dayFrom = mCalendar.get(Calendar.DAY_OF_MONTH) + 1;
    private int dayTo = mCalendar.get(Calendar.DAY_OF_MONTH) + 1;
    private int hourFrom = mCalendar.get(Calendar.HOUR_OF_DAY) + 1;
    private int hourTo = mCalendar.get(Calendar.HOUR_OF_DAY) + 2;


    private String description = "";
    private String mTimeZone = TimeZone.getDefault().getID();

    //</editor-fold>
    //<editor-fold desc="Constructor">

    public EventsEntity() {
        super(TEAMS);
        addCollection(CONTACTS);
    }

    //</editor-fold>

    //<editor-fold desc="IEventsEntity">

    @Override
    public void setCallback(final Callback callback) {
        this.mCallback = callback;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(final String description) {
        final String mDescription = description.trim();
        if (!this.description.equals(mDescription)) {
            this.description = mDescription;
            this.setIsRequiredToBeSaved(true);
        }
    }

    @Override
    public int getCalendarId() {
        return calendarId;
    }

    @Override
    public void setCalendarId(final int calendarId) {
        if (this.calendarId != calendarId) {
            this.calendarId = calendarId;
            this.setIsRequiredToBeSaved(true);
        }
    }

    @Override
    public boolean setDateFrom(final int year, final int month, final int day) {

        final boolean result = yearFrom != year || monthFrom != month || dayFrom != day;

        if (result) {
            final GregorianCalendar calendarFrom = new GregorianCalendar(year, month, day, hourFrom, minutesFrom);
            if (calendarFrom.getTimeInMillis() < GregorianCalendar.getInstance().getTimeInMillis()) {
                final DateFormat dateFormat = DateFormat.getDateInstance();
                Log.e(TAG, String.format("Date From: '%s' is not valid, date from can not be lower than current date.", dateFormat.format(calendarFrom.getTime())));
                return false;
            }

            this.dayFrom = day;
            this.yearFrom = year;
            this.monthFrom = month;
            this.setIsRequiredToBeSaved(true);

            // Notifies that DateTimeFrom value has been changed
            if (mCallback != null)
                mCallback.OnDateTimeHasBeenUpdated(this, true, false);

            // Validates that DateTimeFrom can not be higher than DateTimeTo. If so, it moves
            // DateTimeTo to the next hour from DateTimeFrom and adds 50 more minutes.
            final GregorianCalendar calendarTo = new GregorianCalendar(yearTo, monthTo, dayTo, hourTo, minutesTo);
            if (calendarTo.getTimeInMillis() < calendarFrom.getTimeInMillis()) {
                dayTo = dayFrom;
                yearTo = yearFrom;
                monthTo = monthFrom;
                hourTo = hourFrom + 1;
                minutesTo = 50;

                if (mCallback != null)
                    mCallback.OnDateTimeHasBeenUpdated(this, false, true);
            }

        }
        return result;
    }

    @Override
    public boolean setDateTo(final int year, final int month, final int day) {

        final boolean result = yearTo != year || monthTo != month || dayTo != day;

        if (result) {
            final GregorianCalendar calendarTo = new GregorianCalendar(year, month, day, hourTo, minutesTo);
            final GregorianCalendar calendarFrom = new GregorianCalendar(yearFrom, monthFrom, dayFrom, hourFrom, minutesFrom);
            if (calendarTo.getTimeInMillis() < calendarFrom.getTimeInMillis()) {
                final DateFormat dateFormat = DateFormat.getDateInstance();
                Log.e(TAG, String.format("Date To: '%s' is not valid, date to can no be lower than date from.", dateFormat.format(calendarTo.getTime())));
                return false;
            }

            this.dayTo = day;
            this.yearTo = year;
            this.monthTo = month;
            this.setIsRequiredToBeSaved(true);

            // Notifies that DateTimeTo value has been changed
            if (mCallback != null)
                mCallback.OnDateTimeHasBeenUpdated(this, false, true);
        }
        return result;
    }

    @Override
    public boolean setTimeFrom(final int hour, final int minutes) {

        final boolean result = hourFrom != hour || minutesFrom != minutes;

        if (result) {
            final GregorianCalendar calendarFrom = new GregorianCalendar(yearFrom, monthFrom, dayFrom, hour, minutes);
            if (calendarFrom.getTimeInMillis() < GregorianCalendar.getInstance().getTimeInMillis()) {
                final DateFormat dateFormat = DateFormat.getDateInstance();
                Log.e(TAG, String.format("Time From: '%s' is not valid, time from can no be lower than current time.", dateFormat.format(calendarFrom.getTime())));
                return false;
            }

            this.hourFrom = hour;
            this.minutesFrom = minutes;
            this.setIsRequiredToBeSaved(true);

            // Notifies that DateTimeFrom value has been changed
            if (mCallback != null)
                mCallback.OnDateTimeHasBeenUpdated(this, true, false);

            // Validates that DateTimeFrom can not be higher than DateTimeTo. If so, it moves
            // DateTimeTo to the next hour from DateTimeFrom and adds 50 more minutes.
            final GregorianCalendar calendarTo = new GregorianCalendar(yearTo, monthTo, dayTo, hourTo, minutesTo);
            if (calendarTo.getTimeInMillis() < calendarFrom.getTimeInMillis()) {
                dayTo = dayFrom;
                yearTo = yearFrom;
                monthTo = monthFrom;
                hourTo = hourFrom + 1;
                minutesTo = 50;

                if (mCallback != null)
                    mCallback.OnDateTimeHasBeenUpdated(this, false, true);
            }
        }
        return true;
    }

    @Override
    public boolean setTimeTo(final int hour, final int minutes) {

        final boolean result = hourTo != hour || minutesTo != minutes;

        if (result) {
            final GregorianCalendar calendarTo = new GregorianCalendar(yearTo, monthTo, dayTo, hour, minutes);
            final GregorianCalendar calendarFrom = new GregorianCalendar(yearFrom, monthFrom, dayFrom, hourFrom, minutesFrom);
            if (calendarTo.getTimeInMillis() < calendarFrom.getTimeInMillis()) {
                final DateFormat dateFormat = DateFormat.getDateInstance();
                Log.e(TAG, String.format("Time To: '%s' is not valid, time to can no be lower than time to.", dateFormat.format(calendarTo.getTime())));
                return false;
            }

            this.hourTo = hour;
            this.minutesTo = minutes;
            this.setIsRequiredToBeSaved(true);

            // Notifies that DateTimeTo value has been changed
            if (mCallback != null)
                mCallback.OnDateTimeHasBeenUpdated(this, false, true);
        }
        return result;
    }

    @Override
    public int getYearFrom() {
        return yearFrom;
    }

    @Override
    public int getYearTo() {
        return yearTo;
    }

    @Override
    public int getMonthFrom() {
        return monthFrom;
    }

    @Override
    public int getMonthTo() {
        return monthTo;
    }

    @Override
    public int getDayFrom() {
        return dayFrom;
    }

    @Override
    public int getDayTo() {
        return dayTo;
    }

    @Override
    public int getHourFrom() {
        return hourFrom;
    }

    @Override
    public int getHourTo() {
        return hourTo;
    }

    @Override
    public int getMinutesFrom() {
        return minutesFrom;
    }

    @Override
    public int getMinutesTo() {
        return minutesTo;
    }

    @Override
    public String getTimeZone() {
        return mTimeZone;
    }

    @Override
    public void setTimeZone(final String timeZone) {
        mTimeZone = timeZone;
        setIsRequiredToBeSaved(true);

        // Notifies that TimeZone value has been changed
        if (mCallback != null)
            mCallback.OnTimeZoneHasBeenUpdated(this);
    }

    //</editor-fold>
    //<editor-fold desc="IMaintenance">

    @Override
    public Bundle getResult() {
        return null;
    }

    @Override
    public void loadData(final Context context, final Bundle extras) {
        startTrackingChanges();
    }

    //</editor-fold>

}
