package com.fsteller.mobile.android.teammatesapp;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.fsteller.mobile.android.teammatesapp.helpers.HelperAccount;
import com.fsteller.mobile.android.teammatesapp.helpers.HelperDatabase;

import java.util.ArrayList;

/**
 * Created by fhernandezs on 26/12/13 for iTeammates.
 */
public class TeammatesApp extends Application implements TeammatesApplicationCallback {

    //<editor-fold desc="Constants">

    private static final String TAG = TeammatesApp.class.getSimpleName();

    //</editor-fold>
    //<editor-fold desc="Variables">

    private SharedPreferences pref;

    //</editor-fold>
    //<editor-fold desc="Constructor">
    public TeammatesApp() {
        super();
    }
    //</editor-fold>

    //<editor-fold desc="Overridden Methods">

    @Override
    public void onCreate() {
        super.onCreate();

        // Setup default shared preferences
        this.pref = PreferenceManager.getDefaultSharedPreferences(this);
        Log.i(TAG, "onCreated");
    }

    @Override
    public String toString() {
        return TAG;
    }

    //<editor-fold desc="TeammatesApplicationCallback">

    @Override
    public boolean addData(final Intent data) {
        final HelperDatabase mHelperDatabase = HelperDatabase.getInstance(this);
        final Bundle extras = data.getBundleExtra(TC.ENTITY.EXTRAS);

        if (extras == null)
            return false;

        Log.d(TAG, String.format("Adding data (%s) to: %s", data, mHelperDatabase));
        final int tag = extras.getInt(TC.ENTITY.COLLECTION_ID, -1);
        switch (tag) {
            case TC.Activity.Maintenance.TEAMS:
                mHelperDatabase.addTeam(this, extras);
                break;
            case TC.Activity.Maintenance.EVENTS:
                mHelperDatabase.addEvent(this, extras);
                break;
            case TC.Activity.Maintenance.NOTIFICATION:
                mHelperDatabase.addNotification(this, extras);
                break;
            default:
                return false;
        }
        return true;
    }

    @Override
    public boolean updateData(final Intent data) {
        final HelperDatabase mHelperDatabase = HelperDatabase.getInstance(this);
        final Bundle extras = data.getBundleExtra(TC.ENTITY.EXTRAS);

        if (extras == null)
            return false;

        Log.d(TAG, String.format("Updating data (%s) from: %s", data, mHelperDatabase));
        final int tag = extras.getInt(TC.ENTITY.COLLECTION_ID, -1);
        switch (tag) {
            case TC.Activity.Maintenance.TEAMS:
                mHelperDatabase.updateTeam(this, extras);
                break;
            case TC.Activity.Maintenance.EVENTS:
                mHelperDatabase.updateEvent(this, extras);
                break;
            case TC.Activity.Maintenance.NOTIFICATION:
                mHelperDatabase.updateNotification(this, extras);
                break;
            default:
                return false;
        }
        return true;
    }

    @Override
    public boolean deleteData(final Intent data) {
        final HelperDatabase mHelperDatabase = HelperDatabase.getInstance(this);
        final Bundle extras = data.getBundleExtra(TC.ENTITY.EXTRAS);

        if (extras == null)
            return false;

        Log.d(TAG, String.format("Deleting data (%s) from: %s", data, mHelperDatabase));
        final int tag = extras.getInt(TC.ENTITY.COLLECTION_ID, -1);
        switch (tag) {
            case TC.Activity.Maintenance.TEAMS:
                mHelperDatabase.deleteTeams(this, extras);
                break;
            case TC.Activity.Maintenance.EVENTS:
                mHelperDatabase.deleteEvents(this, extras);
                break;
            case TC.Activity.Maintenance.NOTIFICATION:
                mHelperDatabase.deleteNotifications(this, extras);
                break;
            default:
                return false;
        }
        return true;
    }

    @Override
    public void onSendLocalBroadcastCallback(final Object sender, final String receiverPermission, final int action, final ArrayList params) {
        Log.d(TAG, String.format("sendBroadcast: %s", receiverPermission));
        final Intent intent = new Intent(receiverPermission);

        intent.putExtra(TC.Broadcast.BROADCAST_ACTION, action);
        intent.putStringArrayListExtra(TC.ENTITY.COLLECTION_ID, params);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    //</editor-fold>

    //</editor-fold>
    //<editor-fold desc="Protected Methods">

    protected HelperDatabase getHelperDatabase() {

        return HelperDatabase.getInstance(this);
    }

    protected HelperAccount getHelperAccount() {
        return HelperAccount.getInstance(this);
    }

    //</editor-fold>
}
