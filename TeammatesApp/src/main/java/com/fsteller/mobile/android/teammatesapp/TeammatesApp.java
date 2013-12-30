package com.fsteller.mobile.android.teammatesapp;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.fsteller.mobile.android.teammatesapp.activities.base.TC;

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
    public TeammatesApp(){
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

    //<editor-fold desc="TeammatesApplicationCallback Methods">

    @Override
    public boolean addData(Intent data) {
        return false;
    }


    @Override
    public boolean updateData(Intent data) {
        return false;
    }

    @Override
    public boolean deleteData(Intent data) {
        return false;
    }

    @Override
    public void onSendLocalBroadcastCallback(final Object sender, final String receiverPermission, final int action, final ArrayList params) {
        Log.d(TAG, String.format("sendBroadcast: %s", receiverPermission));
        final Intent intent = new Intent(receiverPermission);

        intent.putExtra(TC.Broadcast.BROADCAST_ACTION, action);
        intent.putStringArrayListExtra(TC.Activity.PARAMS.ID, params);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Override
    public String toString() {
        return TAG;
    }

    //</editor-fold>
    //</editor-fold>

}
