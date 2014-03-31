package com.fsteller.mobile.android.teammatesapp.database;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import java.util.Arrays;


/**
 * Project: iTeammates
 * Subpackage: database
 * <p/>
 * Description:
 * <p/>
 * Created by fhernandezs on 26/12/13 for iTeammates.
 */
public final class ContentProvider extends android.content.ContentProvider {

    //<editor-fold desc="Constants">

    static final int TEAMS = 1001;
    static final int TEAM_ID = 1002;
    static final int TEAM_FILTER = 1003;
    static final int TEAM_CONTACTS = 2001;
    static final int TEAM_CONTACT_ID = 2002;
    static final int EVENTS = 3001;
    static final int EVENT_ID = 3002;
    static final int EVENT_FILTER = 3003;
    static final int MEDIA_CONTENT = 9001;
    static final int MEDIA_CONTENT_ID = 9002;
    private static final String TAG = ContentProvider.class.getSimpleName();
    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    //</editor-fold>
    //<editor-fold desc="Variables">
    private Helper teammatesDbHelper;

    //</editor-fold>
    //<editor-fold desc="Static Declarations">

    static {
        // Table teams related uris
        URI_MATCHER.addURI(Contract.AUTHORITY, Contract.Teams.PATH, TEAMS);
        URI_MATCHER.addURI(Contract.AUTHORITY, Contract.Teams.PATH_ID, TEAM_ID);
        URI_MATCHER.addURI(Contract.AUTHORITY, Contract.Teams.PATH_TEXT_FILTER, TEAM_FILTER);
        URI_MATCHER.addURI(Contract.AUTHORITY, Contract.Teams.PATH_EMPTY_FILTER, TEAM_FILTER);
        URI_MATCHER.addURI(Contract.AUTHORITY, Contract.Teams.PATH_NUMERIC_FILTER, TEAM_FILTER);

        // Table teams.contacts related uris
        URI_MATCHER.addURI(Contract.AUTHORITY, Contract.Teams.Contacts.PATH, TEAM_CONTACTS);
        URI_MATCHER.addURI(Contract.AUTHORITY, Contract.Teams.Contacts.ID_PATH, TEAM_CONTACT_ID);

        // Table mediaContent related uris
        URI_MATCHER.addURI(Contract.AUTHORITY, Contract.MediaContent.PATH, MEDIA_CONTENT);
        URI_MATCHER.addURI(Contract.AUTHORITY, Contract.MediaContent.PATH_ID, MEDIA_CONTENT_ID);
    }

    //</editor-fold>

    //<editor-fold desc="Overridden Methods">

    @Override
    public boolean onCreate() {
        teammatesDbHelper = new Helper(getContext());
        return true;
    }

    @Override
    public String getType(final Uri uri) {
        Log.d(TAG, "getType(" + uri + ")");
        switch (URI_MATCHER.match(uri)) {

            //------------------- Teams -----------------------
            case TEAMS:
            case TEAM_FILTER:
                return Contract.Teams.CONTENT_TYPE;
            case TEAM_ID:
                return Contract.Teams.CONTENT_ITEM_TYPE;

            //------------------- TeamsContact ----------------
            case TEAM_CONTACTS:
                return Contract.Teams.Contacts.CONTENT_TYPE;
            case TEAM_CONTACT_ID:
                return Contract.Teams.Contacts.CONTENT_ITEM_TYPE;

            //------------------- MediaContent ----------------
            case MEDIA_CONTENT:
                return Contract.MediaContent.CONTENT_TYPE;
            case MEDIA_CONTENT_ID:
                return Contract.MediaContent.CONTENT_ITEM_TYPE;

            default:
                throw new IllegalArgumentException("Cannot determine type. Unsupported uri: " + uri);
        }
    }

    @Override
    public Uri insert(final Uri uri, final ContentValues values) {
        Log.d(TAG, String.format("Insert into %s values: %s", uri, values));
        switch (URI_MATCHER.match(uri)) {
            case TEAMS:
                return mInsert(uri, Contract.Teams.PATH, values);
            case TEAM_CONTACTS:
                return mInsert(uri, Contract.Teams.Contacts.CONTENT_DIRECTORY, values);
            case MEDIA_CONTENT:
                return mInsert(uri, Contract.MediaContent.PATH, values);
            default:
                throw new IllegalArgumentException(String.
                        format("Error: uri (%s) doesn't support INSERT operation.", uri));
        }
    }

    @Override
    public int delete(final Uri uri, final String selection, final String[] selectionArgs) {
        Log.d(TAG, String.format("Delete from %s\nWith selection: %s\nUsing arguments: %s",
                uri, selection, Arrays.toString(selectionArgs)));
        switch (URI_MATCHER.match(uri)) {
            case TEAMS:
            case TEAM_ID:
            case TEAM_FILTER:
                return mDelete(uri, Contract.Teams.PATH, selection, selectionArgs);
            case TEAM_CONTACTS:
            case TEAM_CONTACT_ID:
                return mDelete(uri, Contract.Teams.Contacts.CONTENT_DIRECTORY, selection, selectionArgs);
            default:
                throw new IllegalArgumentException(String.
                        format("Error: uri (%s) doesn't support DELETE operation.", uri));
        }
    }

    @Override
    public int update(final Uri uri, final ContentValues values, final String selection, final String[] selectionArgs) {
        Log.d(TAG, String.format("Update into %s\nWith selection: %s\nUsing arguments: %s",
                uri, selection, Arrays.toString(selectionArgs)));

        switch (URI_MATCHER.match(uri)) {
            case TEAMS:
            case TEAM_ID:
            case TEAM_FILTER:
                return mUpdate(uri, Contract.Teams.PATH, values, selection, selectionArgs);
            case TEAM_CONTACTS:
            case TEAM_CONTACT_ID:
                return mUpdate(uri, Contract.Teams.Contacts.CONTENT_DIRECTORY, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException(String.
                        format("Error: uri(%s) doesn't support UPDATE operation.", uri));
        }
    }

    @Override
    public Cursor query(final Uri uri, final String[] projection, String selection, final String[] selectionArgs, final String sortOrder) {
        Log.d(TAG, String.format("Query into %s\nWith projection: %s\nAnd selection: %s\nUsing arguments: %s\nAnd sort by: %s",
                uri, Arrays.toString(projection), selection, Arrays.toString(selectionArgs), sortOrder));

        final int matcher = URI_MATCHER.match(uri);
        switch (matcher) {
            case TEAMS:
            case TEAM_ID:
            case TEAM_FILTER:
                return mQuery(Contract.Teams.getQueryBuilder(uri, matcher),
                        projection, selection, selectionArgs, sortOrder);
            case TEAM_CONTACTS:
            case TEAM_CONTACT_ID:
                return mQuery(Contract.Teams.Contacts.getQueryBuilder(uri, matcher),
                        projection, selection, selectionArgs, sortOrder);
            case MEDIA_CONTENT:
            case MEDIA_CONTENT_ID:
                return mQuery(Contract.MediaContent.getQueryBuilder(uri, matcher),
                        projection, selection, selectionArgs, sortOrder);
            default:
                Log.e(TAG, String.format("Error: uri(%s) doesn't support Query operation.", uri));
                return null;
        }
    }

    //</editor-fold>
    //<editor-fold desc="Private Methods">

    private Uri mInsert(final Uri uri, final String tableName, final ContentValues values) {
        final SQLiteDatabase db = teammatesDbHelper.getWritableDatabase();

        if (db != null) {
            try {

                final Context mContext = getContext();
                final long rowId = db.insertOrThrow(tableName, null, values);
                if (rowId != -1 && mContext != null) {
                    final Uri itemUri = ContentUris.withAppendedId(uri, rowId);
                    mContext.getContentResolver().notifyChange(itemUri, null);
                    return itemUri;
                } else {
                    Log.e(TAG, String.format("Table %s failed to INSERT %s into %s", tableName, values, uri));
                }

            } catch (Exception e) {
                Log.e(TAG, String.format("Unhandled error during INSERT operation: %s", e.getMessage()), e);
                e.printStackTrace();
            }
        }
        return null;
    }

    private int mDelete(final Uri uri, final String tableName, final String selection, final String[] selectionArgs) {
        int count = 0;
        final Context mContext = getContext();
        final SQLiteDatabase db = teammatesDbHelper.getWritableDatabase();
        if (db != null && mContext != null) {
            count = db.delete(tableName, selection, selectionArgs);
            if (count > 0)
                mContext.getContentResolver().notifyChange(uri, null);
        }
        return count;
    }

    private Cursor mQuery(final SQLiteQueryBuilder qb, final String[] projection, final String selection, final String[] selectionArgs, final String sortOrder) {
        Cursor cursor = null;
        final SQLiteDatabase db = this.teammatesDbHelper.getReadableDatabase();
        if (db != null && qb != null) {
            cursor = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        }
        return cursor;
    }

    private int mUpdate(final Uri uri, final String tableName, final ContentValues values, final String selection, final String[] selectionArgs) {
        int count = 0;
        final Context mContext = getContext();
        final SQLiteDatabase db = teammatesDbHelper.getWritableDatabase();
        if (db != null && mContext != null) {
            count = db.update(tableName, values, selection, selectionArgs);
            if (count > 0)
                mContext.getContentResolver().notifyChange(uri, null);
        }
        return count;
    }

    //</editor-fold>
}

