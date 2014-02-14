package com.fsteller.mobile.android.teammatesapp.model;

import android.content.Context;
import android.os.Bundle;

import com.fsteller.mobile.android.teammatesapp.TC;
import com.fsteller.mobile.android.teammatesapp.model.base.AbstractEntity;

/**
 * Created by fhernandezs on 13/02/14.
 */
public class EventEntity extends AbstractEntity {

    public static final int TEAMS = TC.Activity.Maintenance.TEAMS;
    public static final int EVENTS = TC.Activity.Maintenance.EVENTS;
    public static final int CONTACTS = TC.Activity.Maintenance.CONTACTS;

    private static final String TAG = TeamsEntity.class.getSimpleName();


    private String description = "";

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

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description.trim();
        this.requiredToBeSaved();
    }


}
