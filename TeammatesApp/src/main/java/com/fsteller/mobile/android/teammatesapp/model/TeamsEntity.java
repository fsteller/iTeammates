package com.fsteller.mobile.android.teammatesapp.model;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.fsteller.mobile.android.teammatesapp.TC;
import com.fsteller.mobile.android.teammatesapp.database.Contract;
import com.fsteller.mobile.android.teammatesapp.model.base.AbstractEntity;
import com.fsteller.mobile.android.teammatesapp.model.base.ITeamsEntity;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Project: iTeammates
 * Subpackage: model
 * <p/>
 * Description: this class represents iTeammates Team (to be) stored at the database.
 * It must be used in order to get access to a team data,
 * or to make a new entity that would become a bundle in order to persist it.
 * <p/>
 * Created by fhernandezs on 13/02/14.
 */
public class TeamsEntity extends AbstractEntity implements ITeamsEntity {

    //<editor-fold desc="Constants">

    public static final int TEAMS = TC.Activity.Maintenance.TEAMS;
    private static final String TAG = TeamsEntity.class.getSimpleName();

    //</editor-fold>
    //<editor-fold desc="Variables">

    private String description = "";

    //</editor-fold>
    //<editor-fold desc="Constructor">

    public TeamsEntity() {
        super(TEAMS);
        this.addCollection(TEAMS);
    }

    //</editor-fold>

    //<editor-fold desc="ITeamsEntity">

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

    //</editor-fold>
    //<editor-fold desc="IMaintenance">

    @Override
    public Bundle getResult() {
        final Bundle extras = new Bundle();

        extras.putInt(TC.ENTITY.ID, getId());
        extras.putInt(TC.ENTITY.COLLECTION_ID, TEAMS);
        extras.putString(TC.ENTITY.COLLECTION_NAME, getName());
        extras.putString(TC.ENTITY.COLLECTION_IMAGE_REF, getImageRefAsString());
        extras.putIntegerArrayList(TC.ENTITY.COLLECTION_ITEMS, getCollection(TEAMS));
        extras.putLong(TC.ENTITY.COLLECTION_CREATE_DATE, Calendar.getInstance().getTimeInMillis());

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
                final ArrayList<Integer> teamsIds = extras.getIntegerArrayList(TC.ENTITY.COLLECTION_ITEMS);
                if (teamsIds != null && teamsIds.size() > 0) {

                    final int id = teamsIds.get(0);
                    final String[] projection = TC.Queries.TeammatesTeams.TEAM_CONTACT_PROJECTION;
                    final Uri teamsContactsUri = Contract.Teams.Contacts.getTeamContactUri(id);
                    final Cursor data = context.getContentResolver().query(teamsContactsUri, projection, null, null, null);

                    if (data != null) {
                        try {
                            data.moveToFirst();

                            setId(id);
                            setName(data.getString(TC.Queries.TeammatesTeams.NAME));
                            setImageRef(data.getString(TC.Queries.TeammatesTeams.IMAGE_REF));
                            Log.i(TAG, String.format("Loading '%s' contacts...", getName()));
                            addItemToCollection(TEAMS, data.getInt(TC.Queries.TeammatesTeams.CONTACT_TOKEN));
                            while (data.moveToNext())
                                addItemToCollection(TEAMS, data.getInt(TC.Queries.TeammatesTeams.CONTACT_TOKEN));
                        } catch (Exception e) {
                            Log.e(TAG, e.getMessage(), e);
                            e.printStackTrace();
                        } finally {
                            data.close();
                            startTrackingChanges();
                        }
                    }
                }

            }
        }.start();
    }

    //</editor-fold>
}
