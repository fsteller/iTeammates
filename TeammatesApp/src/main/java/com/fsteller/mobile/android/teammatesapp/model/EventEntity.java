package com.fsteller.mobile.android.teammatesapp.model;

import android.content.Context;
import android.os.Bundle;

import com.fsteller.mobile.android.teammatesapp.TC;
import com.fsteller.mobile.android.teammatesapp.model.base.AbstractEntity;
import com.fsteller.mobile.android.teammatesapp.model.base.IEventEntity;

/**
 * Created by fhernandezs on 13/02/14.
 */
public class EventEntity extends AbstractEntity implements IEventEntity {

    public static final int TEAMS = TC.Activity.Maintenance.TEAMS;
    public static final int EVENTS = TC.Activity.Maintenance.EVENTS;
    public static final int CONTACTS = TC.Activity.Maintenance.CONTACTS;

    private static final String TAG = TeamsEntity.class.getSimpleName();


    private String description = "";
    private String calendar = "";

    public EventEntity() {
        super(EVENTS);
        addCollection(TEAMS);
        addCollection(CONTACTS);
    }


    @Override
    public Bundle getResult() {
        return null;
    }

    @Override
    public void loadData(final Context context, final Bundle extras) {


        startTrackingChanges();
    }

    public String getEntityDescription() {
        return description;
    }

    public void setEntityDescription(final String description) {
        final String mDescription = description.trim();
        if (!this.description.equals(mDescription)) {
            this.description = mDescription;
            this.setIsRequiredToBeSaved(true);
        }
    }

    public String getEntityCalendar() {
        return calendar;
    }

    public void setEntityCalendar(final String calendar) {
        final String mCalendar = calendar.trim();
        if (!this.calendar.equals(mCalendar)) {
            this.calendar = mCalendar;
            this.setIsRequiredToBeSaved(true);
        }
    }
}
