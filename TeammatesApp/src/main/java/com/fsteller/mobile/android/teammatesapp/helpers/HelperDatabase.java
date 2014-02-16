package com.fsteller.mobile.android.teammatesapp.helpers;

import android.content.ContentProviderOperation;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.fsteller.mobile.android.teammatesapp.TC;
import com.fsteller.mobile.android.teammatesapp.TeammatesApplicationCallback;
import com.fsteller.mobile.android.teammatesapp.helpers.database.TeammatesContract;
import com.fsteller.mobile.android.teammatesapp.helpers.database.TeammatesDb;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by fhernandezs on 03/01/14 for iTeammates.
 */
public final class HelperDatabase {

    //<editor-fold desc="Constants">
    private static final String TAG = HelperDatabase.class.getSimpleName();
    //</editor-fold>
    //<editor-fold desc="Variables">

    private TeammatesApplicationCallback mContext = null;
    private static HelperDatabase mHelperDatabase = null;
    private final TeammatesDb sqlHelper;

    //</editor-fold>
    //<editor-fold desc="Constructor">
    private HelperDatabase(final Context context) {
        this.sqlHelper = new TeammatesDb(context);
        this.mContext = (TeammatesApplicationCallback) context;
    }
    //</editor-fold>

    //<editor-fold desc="Public Methods">

    //<editor-fold desc="Add to database Methods">

    public void addTeam(final Context context, final Bundle data) {

        if (data == null)
            return;

        new Thread() {
            @Override
            public void run() {

                final ContentValues values = new ContentValues();
                final long datetime = Calendar.getInstance().getTimeInMillis();
                final String teamName = data.getString(TC.ENTITY.COLLECTION_NAME);
                final String imageRef = data.getString(TC.ENTITY.COLLECTION_IMAGE_REF);
                final long createdAt = data.getLong(TC.ENTITY.COLLECTION_CREATE_DATE, datetime);
                final long updatedAt = data.getLong(TC.ENTITY.COLLECTION_UPDATE_DATE, datetime);
                final ArrayList<Integer> contacts = data.getIntegerArrayList(TC.ENTITY.COLLECTION_ITEMS);

                values.put(TeammatesContract.Teams.NAME, teamName);
                values.put(TeammatesContract.Teams.IMAGE_REF, imageRef);
                values.put(TeammatesContract.Teams.CREATED_AT, createdAt);
                values.put(TeammatesContract.Teams.UPDATED_AT, updatedAt);
                final Uri uri = context.getContentResolver().insert(TeammatesContract.Teams.CONTENT_URI, values);

                if (contacts != null && uri != null) {
                    final long id = ContentUris.parseId(uri);
                    final Uri contactUri = Uri.withAppendedPath(uri, TeammatesContract.Teams.Contacts.CONTENT_DIRECTORY);

                    if (contactUri != null) {
                        final ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
                        for (int contact : contacts)
                            ops.add(ContentProviderOperation.newInsert(contactUri)
                                    .withValue(TeammatesContract.Teams.Contacts.TEAM_ID, id)
                                    .withValue(TeammatesContract.Teams.Contacts.TOKEN, contact)
                                    .build());
                        try {
                            context.getContentResolver().applyBatch(TeammatesContract.AUTHORITY, ops);
                            callback(TC.DatabaseActions.TeamAddedToDb);
                            return;
                        } catch (Exception e) {
                            Log.e(TAG, String.format("Error processing INSERT operation with Uri: %s", contactUri), e);
                            revertTransaction(contactUri, e);
                        }
                    }
                }
                if (uri != null)
                    revertTransaction(uri, new Exception(String.
                            format("Invalid operation: Team '%s' doesn't have contacts", teamName)));
            }
        }.start();
    }

    public void addEvent(final Context context, final Bundle data) {

        if (data == null)
            return;

        new Thread() {
            @Override
            public void run() {

            }
        }.start();
    }

    public void addNotification(final Context context, final Bundle data) {

        if (data == null)
            return;

        new Thread() {
            @Override
            public void run() {

            }
        }.start();
    }

    //</editor-fold>
    //<editor-fold desc="Update on database Methods">

    public void updateTeam(final Context context, final Bundle data) {

        if (data == null)
            return;

        new Thread() {
            @Override
            public void run() {

                final ContentValues values = new ContentValues();
                final long datetime = Calendar.getInstance().getTimeInMillis();
                final String selection = String.format("%s=?", TeammatesContract.Teams._ID);

                final int id = data.getInt(TC.ENTITY.ID);
                final String teamName = data.getString(TC.ENTITY.COLLECTION_NAME);
                final String imageRef = data.getString(TC.ENTITY.COLLECTION_IMAGE_REF);
                final long updatedAt = data.getLong(TC.ENTITY.COLLECTION_UPDATE_DATE, datetime);
                final ArrayList<Integer> contacts = data.getIntegerArrayList(TC.ENTITY.COLLECTION_ITEMS);

                values.put(TeammatesContract.Teams.NAME, teamName);
                values.put(TeammatesContract.Teams.IMAGE_REF, imageRef);
                values.put(TeammatesContract.Teams.UPDATED_AT, updatedAt);

                final String[] args = new String[]{String.valueOf(id)};
                int updated = context.getContentResolver().update(TeammatesContract.Teams.CONTENT_URI, values, selection, args);

                if (updated > 0) {
                    final Uri uri = TeammatesContract.Teams.Contacts.getTeamContactUri(id);
                    final String where = String.format("%s=?", TeammatesContract.Teams.Contacts.TEAM_ID);

                    if (uri != null && contacts != null) {
                        context.getContentResolver().delete(uri, where, args);
                        final ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
                        for (int contact : contacts)
                            ops.add(ContentProviderOperation.newInsert(uri)
                                    .withValue(TeammatesContract.Teams.Contacts.TEAM_ID, id)
                                    .withValue(TeammatesContract.Teams.Contacts.TOKEN, contact)
                                    .build());
                        try {
                            context.getContentResolver().applyBatch(TeammatesContract.AUTHORITY, ops);
                            callback(TC.DatabaseActions.TeamAddedToDb);
                        } catch (Exception e) {
                            Log.e(TAG, String.format("Error processing INSERT operation with Uri: %s", uri), e);
                            revertTransaction(uri, e);
                        }
                    }
                }
            }
        }.start();
    }

    public void updateEvent(final Context context, final Bundle data) {

        if (data == null)
            return;

        new Thread() {
            @Override
            public void run() {

            }
        }.start();
    }

    public void updateNotification(final Context context, final Bundle data) {

        if (data == null)
            return;

        new Thread() {
            @Override
            public void run() {

            }
        }.start();
    }

    //</editor-fold>
    //<editor-fold desc="Delete from database Methods">

    public void deleteTeams(final Context context, final Bundle data) {

        new Thread() {

            @Override
            public void run() {

                if (data != null) {
                    final ArrayList<Integer> itemsIds = data.getIntegerArrayList(TC.ENTITY.COLLECTION_ITEMS);
                    if (itemsIds != null) {

                        final String selection = String.format("%s=?", TeammatesContract.Teams._ID);
                        final ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
                        for (int id : itemsIds)
                            ops.add(ContentProviderOperation.newDelete(TeammatesContract.Teams.CONTENT_URI)
                                    .withSelection(selection, new String[]{String.valueOf(id)}).build());

                        try {
                            context.getContentResolver().applyBatch(TeammatesContract.AUTHORITY, ops);
                            callback(TC.DatabaseActions.TeamDeletedFromDb);
                        } catch (Exception e) {
                            Log.e(TAG, String.format("Error processing DELETE operation with Uri: %s", TeammatesContract.Teams.CONTENT_URI), e);
                            revertTransaction(null, e);
                        }
                    } else
                        Log.d(TAG, String.format("Error processing DELETE operation, no data to be deleted. Data size: %s", data.size()));
                }
            }
        }.start();
    }

    public void deleteEvents(final Context teammatesApplication, final Bundle data) {

        if (data == null)
            return;

        new Thread() {
            @Override
            public void run() {

            }
        }.start();
    }

    public void deleteNotifications(final Context teammatesApplication, final Bundle data) {

        if (data == null)
            return;

        new Thread() {
            @Override
            public void run() {

            }
        }.start();
    }

    //</editor-fold>

    //</editor-fold>
    //<editor-fold desc="Private Methods">

    private void callback(final int action) {
        if (mContext != null) {
            Log.d(TAG, String.format("Callback action required: %s", action));
            mContext.onSendLocalBroadcastCallback(this, TC.Broadcast.DB_UPDATE_TEAMS_RECEIVE, action, null);
        }
    }

    private void revertTransaction(Uri uri, Exception e) {
        Log.e(TAG, String.format("Unknown error during insert for uri (%s). Reverting transaction.", uri), e);
        //Todo: revert trx
    }

    //</editor-fold>
    //<editor-fold desc="Overridden Methods">


    @Override
    public String toString() {
        return String.format("DatabaseHelper.%s", sqlHelper);
    }

    //</editor-fold>
    public static HelperDatabase getInstance(final Context context) {
        if (mHelperDatabase == null)
            mHelperDatabase = new HelperDatabase(context);
        return mHelperDatabase;
    }
}
