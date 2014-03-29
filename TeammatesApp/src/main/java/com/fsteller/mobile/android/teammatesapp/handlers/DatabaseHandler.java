package com.fsteller.mobile.android.teammatesapp.handlers;

import android.content.ContentProviderOperation;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.fsteller.mobile.android.teammatesapp.TC;
import com.fsteller.mobile.android.teammatesapp.TeammatesApp;
import com.fsteller.mobile.android.teammatesapp.database.Contract;
import com.fsteller.mobile.android.teammatesapp.image.Utils;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Project: iTeammates
 * Subpackage: handlers
 * <p/>
 * Description: Singleton class that handles and contains the logic to create, update, or delete
 * database records of any kind for the iTeammates application.
 * <p/>
 * Created by fhernandezs on 03/01/14 for iTeammates.
 */
public final class DatabaseHandler {

    //<editor-fold desc="Constants">
    private static final String TAG = DatabaseHandler.class.getSimpleName();
    //</editor-fold>
    //<editor-fold desc="Constructor">

    /**
     * Private constructor used in order to prevent instantiation of this class.
     *
     */
    private DatabaseHandler() {

    }

    //</editor-fold>

    //<editor-fold desc="Public Methods">

    //<editor-fold desc="Add to database Methods">

    public static void addTeam(final TeammatesApp context, final Bundle data) {

        if (data == null)
            return;

        new Thread() {
            @Override
            public void run() {

                // Loads TeamsTable data from bundle parameter
                final long datetime = Calendar.getInstance().getTimeInMillis();
                final String teamName = data.getString(TC.ENTITY.COLLECTION_NAME);
                final String imageDiskRef = data.getString(TC.ENTITY.COLLECTION_IMAGE_REF);
                final long createdAt = data.getLong(TC.ENTITY.COLLECTION_CREATE_DATE, datetime);
                final long updatedAt = data.getLong(TC.ENTITY.COLLECTION_UPDATE_DATE, datetime);

                // Persist referenced image as BLOB data into the MediaContent table and gets the Uri reference
                final String imageDbRef = persistImageMediaContent(context, imageDiskRef, createdAt, updatedAt);

                // Stores TeamsTable data into ContentValues container
                final ContentValues teamsTableContentValues = new ContentValues();
                teamsTableContentValues.put(Contract.Teams.IMAGE_REF, imageDbRef);
                teamsTableContentValues.put(Contract.Teams.CREATED_AT, createdAt);
                teamsTableContentValues.put(Contract.Teams.UPDATED_AT, updatedAt);
                teamsTableContentValues.put(Contract.Teams.NAME, teamName);

                // Persist TeamInformation into TeamsTable and gets the Uri to the new registry
                final Uri uriTeam = context.getContentResolver().insert(Contract.Teams.CONTENT_URI, teamsTableContentValues);

                final ArrayList<Integer> contacts = data.getIntegerArrayList(TC.ENTITY.COLLECTION_ITEMS);
                if (contacts != null && uriTeam != null) {
                    final long id = ContentUris.parseId(uriTeam);
                    final Uri contactUri = Uri.withAppendedPath(uriTeam, Contract.Teams.Contacts.CONTENT_DIRECTORY);

                    if (contactUri != null) {
                        final ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
                        for (int contact : contacts)
                            ops.add(ContentProviderOperation.newInsert(contactUri)
                                    .withValue(Contract.Teams.Contacts.TEAM_ID, id)
                                    .withValue(Contract.Teams.Contacts.TOKEN, contact)
                                    .build());
                        try {
                            context.getContentResolver().applyBatch(Contract.AUTHORITY, ops);
                            sendCallback(context, TC.DatabaseActions.TeamAddedToDb);
                            return;
                        } catch (Exception e) {
                            Log.e(TAG, String.format("Error processing INSERT operation with Uri: %s", contactUri), e);
                            revertTransaction(contactUri, e);
                        }
                    }
                }
                if (uriTeam != null)
                    revertTransaction(uriTeam, new Exception(String.
                            format("Invalid operation: Team '%s' doesn't have contacts", teamName)));
            }
        }.start();
    }

    public static void addEvent(final Context context, final Bundle data) {

        if (data == null)
            return;

        new Thread() {
            @Override
            public void run() {

            }
        }.start();
    }

    public static void addNotification(final Context context, final Bundle data) {

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

    public static void updateTeam(final TeammatesApp context, final Bundle data) {

        if (data == null)
            return;

        new Thread() {
            @Override
            public void run() {

                final ContentValues values = new ContentValues();
                final long datetime = Calendar.getInstance().getTimeInMillis();
                final String selection = String.format("%s=?", Contract.Teams._ID);

                final int id = data.getInt(TC.ENTITY.ID);
                final String teamName = data.getString(TC.ENTITY.COLLECTION_NAME);
                final String imageRef = data.getString(TC.ENTITY.COLLECTION_IMAGE_REF);
                final long updatedAt = data.getLong(TC.ENTITY.COLLECTION_UPDATE_DATE, datetime);
                final ArrayList<Integer> contacts = data.getIntegerArrayList(TC.ENTITY.COLLECTION_ITEMS);

                values.put(Contract.Teams.NAME, teamName);
                values.put(Contract.Teams.IMAGE_REF, imageRef);
                values.put(Contract.Teams.UPDATED_AT, updatedAt);

                final String[] args = new String[]{String.valueOf(id)};
                int updated = context.getContentResolver().update(Contract.Teams.CONTENT_URI, values, selection, args);

                if (updated > 0) {
                    final Uri uri = Contract.Teams.Contacts.getTeamContactUri(id);
                    final String where = String.format("%s=?", Contract.Teams.Contacts.TEAM_ID);

                    if (uri != null && contacts != null) {
                        context.getContentResolver().delete(uri, where, args);
                        final ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
                        for (int contact : contacts)
                            ops.add(ContentProviderOperation.newInsert(uri)
                                    .withValue(Contract.Teams.Contacts.TEAM_ID, id)
                                    .withValue(Contract.Teams.Contacts.TOKEN, contact)
                                    .build());
                        try {
                            context.getContentResolver().applyBatch(Contract.AUTHORITY, ops);
                            sendCallback(context, TC.DatabaseActions.TeamAddedToDb);
                        } catch (Exception e) {
                            Log.e(TAG, String.format("Error processing INSERT operation with Uri: %s", uri), e);
                            revertTransaction(uri, e);
                        }
                    }
                }
            }
        }.start();
    }

    public static void updateEvent(final Context context, final Bundle data) {

        if (data == null)
            return;

        new Thread() {
            @Override
            public void run() {

            }
        }.start();
    }

    public static void updateNotification(final Context context, final Bundle data) {

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

    public static void deleteTeams(final TeammatesApp context, final Bundle data) {

        new Thread() {

            @Override
            public void run() {

                if (data != null) {
                    final ArrayList<Integer> itemsIds = data.getIntegerArrayList(TC.ENTITY.COLLECTION_ITEMS);
                    if (itemsIds != null) {

                        final String selection = String.format("%s=?", Contract.Teams._ID);
                        final ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
                        for (int id : itemsIds)
                            ops.add(ContentProviderOperation.newDelete(Contract.Teams.CONTENT_URI)
                                    .withSelection(selection, new String[]{String.valueOf(id)}).build());

                        try {
                            context.getContentResolver().applyBatch(Contract.AUTHORITY, ops);
                            sendCallback(context, TC.DatabaseActions.TeamDeletedFromDb);
                        } catch (Exception e) {
                            Log.e(TAG, String.format("Error processing DELETE operation with Uri: %s", Contract.Teams.CONTENT_URI), e);
                            revertTransaction(null, e);
                        }
                    } else
                        Log.d(TAG, String.format("Error processing DELETE operation, no data to be deleted. Data size: %s", data.size()));
                }
            }
        }.start();
    }

    public static void deleteEvents(final Context context, final Bundle data) {

        if (data == null)
            return;

        new Thread() {
            @Override
            public void run() {

            }
        }.start();
    }

    public static void deleteNotifications(final Context context, final Bundle data) {

        if (data == null)
            return;

        new Thread() {
            @Override
            public void run() {

            }
        }.start();
    }

    //</editor-fold>
    //<editor-fold desc="Query database Methods">

    public static byte[] getMediaContent(final Context context, final Uri imageUri) {

        byte[] mBytes = null;
        final Cursor mCursor = context.getContentResolver()
                .query(imageUri, TC.Queries.MediaContent.PROJECTION_MEDIA, TC.Queries.MediaContent.SELECTION, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
            mBytes = mCursor.getBlob(0);
            mCursor.close();
        }

        return mBytes;
    }

    //</editor-fold>
    //</editor-fold>
    //<editor-fold desc="Private Methods">

    private static String persistImageMediaContent(final Context context, final String imageDiskRef, final long createdAt, final long updatedAt) {

        // Transform  imageDiskRef (Uri referenced file) to a byte[]
        final byte[] imageByteArray = Utils.getImageAsByteArray(imageDiskRef);
        final ContentValues mediaContentTableContentValues = new ContentValues();
        mediaContentTableContentValues.put(Contract.MediaContent.CREATED_AT, createdAt);
        mediaContentTableContentValues.put(Contract.MediaContent.UPDATED_AT, updatedAt);
        mediaContentTableContentValues.put(Contract.MediaContent.MEDIA_BLOB, imageByteArray);
        mediaContentTableContentValues.put(Contract.MediaContent.MEDIA_TYPE, TC.MediaContentTypes.Image);

        // Persist MediaContentInformation into MediaContentTable and gets the Uri to the new registry
        final Uri uriMedia = context.getContentResolver()
                .insert(Contract.MediaContent.CONTENT_URI, mediaContentTableContentValues);

        if (uriMedia == null) {
            // If persistence fails, log the error and returns null as result
            Log.e(TAG, String.format("Error processing INSERT ImageMediaContent operation."), null);
            return null;
        }
        return uriMedia.toString();
    }


    private static void sendCallback(final Callback callback, final int action) {
        if (callback != null) {
            Log.d(TAG, String.format("Callback action required: %s", action));
            callback.onSendLocalBroadcastCallback(TC.Broadcast.DB_UPDATE_TEAMS_RECEIVE, action, null);
        }
    }

    private static void revertTransaction(final Uri uri, final Exception e) {
        Log.e(TAG, String.format("Unknown error during insert for uri (%s). Reverting transaction.", uri), e);
        //Todo: revert trx
    }

    //</editor-fold>

    public static interface Callback {

        public boolean addData(final Intent data);

        public boolean updateData(final Intent data);

        public boolean deleteData(final Intent data);

        public void onSendLocalBroadcastCallback(final String receiverPermission, final int action, final ArrayList params);

    }

}
