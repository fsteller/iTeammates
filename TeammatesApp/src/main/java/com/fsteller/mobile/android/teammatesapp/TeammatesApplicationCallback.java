package com.fsteller.mobile.android.teammatesapp;

import android.content.Intent;

import java.util.ArrayList;

/**
 * Project: iTeammates
 * Subpackage: teammatesapp
 * <p/>
 * Description:
 * Created by fhernandezs on 27/12/13 for iTeammates.
 */
public interface TeammatesApplicationCallback {

    public boolean addData(final Intent data);

    public boolean updateData(final Intent data);

    public boolean deleteData(final Intent data);

    public void onSendLocalBroadcastCallback(final Object sender, final String receiverPermission, final int action, final ArrayList params);

}
