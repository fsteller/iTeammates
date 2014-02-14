package com.fsteller.mobile.android.teammatesapp.model;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.fsteller.mobile.android.teammatesapp.TC;
import com.fsteller.mobile.android.teammatesapp.helpers.database.TeammatesContract;
import com.fsteller.mobile.android.teammatesapp.model.base.AbstractEntity;
import com.fsteller.mobile.android.teammatesapp.model.base.IMaintenance;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by fhernandezs on 13/02/14.
 */
public class TeamsEntity extends AbstractEntity implements IMaintenance {

    public static final int TEAMS = TC.Activity.Maintenance.TEAMS;
    private static final String TAG = TeamsEntity.class.getSimpleName();

    public TeamsEntity() {
        super(TEAMS);
        this.addCollection(TEAMS);
    }

    @Override
    public Bundle getResult() {
        final Bundle extras = new Bundle();

        extras.putInt(TC.Activity.PARAMS.COLLECTION_ID, TeamsEntity.TEAMS);
        extras.putString(TC.Activity.PARAMS.COLLECTION_NAME, getEntityName());
        extras.putString(TC.Activity.PARAMS.COLLECTION_IMAGE_REF, getImageRefAsString());
        extras.putIntegerArrayList(TC.Activity.PARAMS.COLLECTION_ITEMS, getCollection(TeamsEntity.TEAMS));
        extras.putLong(TC.Activity.PARAMS.COLLECTION_CREATE_DATE, Calendar.getInstance().getTimeInMillis());

        return extras;
    }

    @Override
    public void loadData(final Context context, final Bundle extras) {

        if (extras == null)
            return;

        new Thread() {
            @Override
            public void run() {
                //Loads data form a database stored team
                Log.i(TAG, "Loading data...");
                final ArrayList<Integer> teamsIds = extras.getIntegerArrayList(TC.Activity.PARAMS.COLLECTION_ITEMS);
                if (teamsIds != null && teamsIds.size() > 0) {

                    final int id = teamsIds.get(0);
                    final String[] projection = TC.Queries.TeammatesTeams.TEAM_CONTACT_PROJECTION;
                    final Uri teamsContactsUri = TeammatesContract.Teams.Contacts.getTeamContactUri(id);
                    final Cursor data = context.getContentResolver().query(teamsContactsUri, projection, null, null, null);

                    if (data != null) {
                        try {
                            data.moveToFirst();

                            setEntityName(data.getString(TC.Queries.TeammatesTeams.NAME));
                            setImageRef(data.getString(TC.Queries.TeammatesTeams.IMAGE_REF));
                            Log.i(TAG, String.format("Loading '%s' contacts...", getEntityName()));
                            addItemToCollection(TEAMS, data.getInt(TC.Queries.TeammatesTeams.CONTACT_TOKEN));
                            while (data.moveToNext())
                                addItemToCollection(TEAMS, data.getInt(TC.Queries.TeammatesTeams.CONTACT_TOKEN));
                        } catch (Exception e) {
                            Log.e(TAG, e.getMessage(), e);
                            e.printStackTrace();
                        } finally {
                            data.close();
                        }
                    }
                }

            }
        }.start();
    }

}
