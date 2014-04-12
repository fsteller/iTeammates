package com.fsteller.mobile.android.teammatesapp.model.base;

/**
 * Project: iTeammates
 * Subpackage: model.base
 * <p/>
 * Description: this interface represents iTeammates Event, it contains all the fields that a
 * {@link com.fsteller.mobile.android.teammatesapp.model.EventsEntity} can handle.
 * <p/>
 * Created by fsteller on 2/15/14.
 */
public interface IEventsEntity extends IEntity {

    public abstract void setCallback(final Callback callback);

    public abstract boolean setTimeTo(final int hour, final int minutes);

    public abstract boolean setTimeFrom(final int hour, final int minutes);

    public abstract boolean setDateTo(final int year, final int month, final int day);

    public abstract boolean setDateFrom(final int year, final int month, final int day);

    public abstract String getDescription();

    public abstract void setDescription(final String description);

    public abstract int getCalendarId();

    public abstract void setCalendarId(final int calendarId);

    public abstract int getYearFrom();

    public abstract int getYearTo();

    public abstract int getMonthFrom();

    public abstract int getMonthTo();

    public abstract int getDayFrom();

    public abstract int getDayTo();

    public abstract int getHourFrom();

    public abstract int getHourTo();

    public abstract int getMinutesFrom();

    public abstract int getMinutesTo();

    public static interface Callback {
        public void OnDateTimeHasBeenUpdated(final IEventsEntity sender, final boolean isDateTimeFrom, final boolean isDateTimeTo);
    }
}
