package com.fsteller.mobile.android.teammatesapp;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.fsteller.mobile.android.teammatesapp.handlers.DatabaseHandler;

import java.util.ArrayList;

/**
 * Project: ${PROJECT_NAME}
 * Package: ${PACKAGE_NAME}
 * <p/>
 * Description:
 * Created by fhernandezs on 26/12/13 for iTeammates.
 */
public class TeammatesApp extends Application implements DatabaseHandler.Callback {

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

    //<editor-fold desc="Callback">

    @Override
    public boolean addData(final Intent data) {
        //final DatabaseHandler mHelperDatabase = DatabaseHandler.getInstance(this);
        final Bundle extras = data.getBundleExtra(TC.ENTITY.EXTRAS);

        if (extras == null)
            return false;

        Log.d(TAG, String.format("Adding data (%s)", data));
        final int tag = extras.getInt(TC.ENTITY.COLLECTION_ID, -1);
        switch (tag) {
            case TC.Activity.Maintenance.TEAMS:
                DatabaseHandler.addTeam(this, extras);
                break;
            case TC.Activity.Maintenance.EVENTS:
                DatabaseHandler.addEvent(this, extras);
                break;
            case TC.Activity.Maintenance.NOTIFICATION:
                DatabaseHandler.addNotification(this, extras);
                break;
            default:
                return false;
        }
        return true;
    }

    @Override
    public boolean updateData(final Intent data) {
        //final DatabaseHandler mHelperDatabase = DatabaseHandler.getInstance(this);
        final Bundle extras = data.getBundleExtra(TC.ENTITY.EXTRAS);
        if (extras == null)
            return false;

        //Log.d(TAG, String.format("Updating data (%s) from: %s", data, mHelperDatabase));
        final int tag = extras.getInt(TC.ENTITY.COLLECTION_ID, -1);
        switch (tag) {
            case TC.Activity.Maintenance.TEAMS:
                DatabaseHandler.updateTeam(this, extras);
                break;
            case TC.Activity.Maintenance.EVENTS:
                DatabaseHandler.updateEvent(this, extras);
                break;
            case TC.Activity.Maintenance.NOTIFICATION:
                DatabaseHandler.updateNotification(this, extras);
                break;
            default:
                return false;
        }
        return true;
    }

    @Override
    public boolean deleteData(final Intent data) {
        //final DatabaseHandler mHelperDatabase = DatabaseHandler.getInstance(this);
        final Bundle extras = data.getBundleExtra(TC.ENTITY.EXTRAS);

        if (extras == null)
            return false;

        ///Log.d(TAG, String.format("Deleting data (%s) from: %s", data, mHelperDatabase));
        final int tag = extras.getInt(TC.ENTITY.COLLECTION_ID, -1);
        switch (tag) {
            case TC.Activity.Maintenance.TEAMS:
                DatabaseHandler.deleteTeams(this, extras);
                break;
            case TC.Activity.Maintenance.EVENTS:
                DatabaseHandler.deleteEvents(this, extras);
                break;
            case TC.Activity.Maintenance.NOTIFICATION:
                DatabaseHandler.deleteNotifications(this, extras);
                break;
            default:
                return false;
        }
        return true;
    }

    @Override
    public void onSendLocalBroadcastCallback(final String receiverPermission, final int action, final ArrayList params) {
        Log.d(TAG, String.format("sendBroadcast: %s", receiverPermission));
        final Intent intent = new Intent(receiverPermission);

        intent.putExtra(TC.Broadcast.BROADCAST_ACTION, action);
        intent.putStringArrayListExtra(TC.ENTITY.COLLECTION_ID, params);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    //</editor-fold>

    //</editor-fold>
}
