package com.fsteller.mobile.android.teammatesapp;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.fsteller.mobile.android.teammatesapp.activities.base.TC;
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

    private HelperAccount mHelperAccount;
    private HelperDatabase mHelperDatabase;
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
        this.mHelperAccount = HelperAccount.getInstance(this);
        this.mHelperDatabase = HelperDatabase.getInstance(this);
        this.pref = PreferenceManager.getDefaultSharedPreferences(this);
        Log.i(TAG, "onCreated");
    }

    @Override
    public String toString() {
        return TAG;
    }

    //<editor-fold desc="TeammatesApplicationCallback Methods">


    @Override
    public boolean addData(final Intent data) {

        Log.d(TAG, String.format("Adding data (%s) to: %s", data, mHelperDatabase));
        final Bundle extras = data.getBundleExtra(TC.Activity.PARAMS.ID);
        if (extras == null)
            return false;

        final int id = extras.getInt(TC.Activity.PARAMS.ID);
        switch (id) {
            case TC.Activity.Mantinace.TEAMS:
                mHelperDatabase.addTeam(this, extras);
                break;
            case TC.Activity.Mantinace.EVENTS:
                mHelperDatabase.addEvent(this, extras);
                break;
            case TC.Activity.Mantinace.NOTIFICATION:
                mHelperDatabase.addNotification(this, extras);
                break;
            default:
                return false;
        }
        return true;
    }

    @Override
    public boolean updateData(final Intent data) {
        Log.d(TAG, String.format("Updating data (%s) on: %s", data, mHelperDatabase));
        final Bundle extras = data.getBundleExtra(TC.Activity.PARAMS.ID);
        if (extras == null)
            return false;

        final int id = extras.getInt(TC.Activity.PARAMS.ID);
        switch (id) {
            case TC.Activity.Mantinace.TEAMS:
                mHelperDatabase.updateTeam(this, data);
                break;
            case TC.Activity.Mantinace.EVENTS:
                mHelperDatabase.updateEvent(this, data);
                break;
            case TC.Activity.Mantinace.NOTIFICATION:
                mHelperDatabase.updateNotification(this, data);
                break;
            default:
                return false;
        }
        return true;
    }

    @Override
    public boolean deleteData(final Intent data) {
        Log.d(TAG, String.format("Deleting data (%s) from: %s", data, mHelperDatabase));

        final Bundle extras = data.getBundleExtra(TC.Activity.PARAMS.ID);
        if (extras == null)
            return false;

        final int tag = extras.getInt(TC.Activity.PARAMS.ID, -1);
        switch (tag) {
            case TC.Activity.Mantinace.TEAMS:
                mHelperDatabase.deleteTeams(this, extras);
                break;
            case TC.Activity.Mantinace.EVENTS:
                mHelperDatabase.deleteEvents(this, extras);
                break;
            case TC.Activity.Mantinace.NOTIFICATION:
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
        intent.putStringArrayListExtra(TC.Activity.PARAMS.ID, params);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }


    //</editor-fold>

    //</editor-fold>
    //<editor-fold desc="Protected Methods">

    protected HelperDatabase getHelperDatabase() {

        // Setup database helpers
        if (mHelperDatabase == null)
            this.mHelperDatabase = HelperDatabase.getInstance(this);

        return mHelperDatabase;
    }

    protected HelperAccount getHelperAccount() {
        if (mHelperAccount == null)
            mHelperAccount = HelperAccount.getInstance(this);
        return mHelperAccount;
    }

    //</editor-fold>
}
