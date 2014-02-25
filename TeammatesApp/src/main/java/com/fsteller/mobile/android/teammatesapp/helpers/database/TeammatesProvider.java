package com.fsteller.mobile.android.teammatesapp.helpers.database;

import android.content.ContentProvider;
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
 * Project: ${PROJECT_NAME}
 * Package: ${PACKAGE_NAME}
 * <p/>
 * Description:
 * Created by fhernandezs on 26/12/13 for iTeammates.
 */
public final class TeammatesProvider extends ContentProvider {


    //<editor-fold desc="Constants">

    private static final String TAG = TeammatesProvider.class.getSimpleName();
    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static final int TEAMS = 1001;
    static final int TEAM_ID = 1002;
    static final int TEAM_FILTER = 1003;
    static final int TEAM_CONTACTS = 2001;
    private static final int TEAM_CONTACT_ID = 2002;

    static final int EVENTS = 3001;
    static final int EVENT_ID = 3002;
    static final int EVENT_FILTER = 3003;

    //</editor-fold>
    //<editor-fold desc="Variables">
    private TeammatesDb teammatesDb;

    //</editor-fold>
    //<editor-fold desc="Static Declarations">

    static {
        // Table teams related uris
        URI_MATCHER.addURI(TeammatesContract.AUTHORITY, TeammatesContract.Teams.PATH, TEAMS);
        URI_MATCHER.addURI(TeammatesContract.AUTHORITY, TeammatesContract.Teams.PATH_ID, TEAM_ID);
        URI_MATCHER.addURI(TeammatesContract.AUTHORITY, TeammatesContract.Teams.PATH_EMPTY_FILTER, TEAM_FILTER);
        URI_MATCHER.addURI(TeammatesContract.AUTHORITY, TeammatesContract.Teams.PATH_NUMERIC_FILTER, TEAM_FILTER);
        URI_MATCHER.addURI(TeammatesContract.AUTHORITY, TeammatesContract.Teams.PATH_TEXT_FILTER, TEAM_FILTER);

        //URI_MATCHER.addURI(TeammatesContract.AUTHORITY, TeammatesContract.TeamsMaintenance.PATH_FILTER, TEAM_FILTER);

        //SearchManager.SUGGEST_URI_PATH_QUERY
        //URI_MATCHER.addURI(TeammatesContract.AUTHORITY, TeammatesContract.TeamsMaintenance.PATH_EXPRESSION_FILTER, TEAM_EX_FILTER_MATCH);


        // Table teams.contacts related uris
        URI_MATCHER.addURI(TeammatesContract.AUTHORITY, TeammatesContract.Teams.Contacts.PATH, TEAM_CONTACTS);
        URI_MATCHER.addURI(TeammatesContract.AUTHORITY, TeammatesContract.Teams.Contacts.ID_PATH, TEAM_CONTACT_ID);
    }

    //</editor-fold>

    //<editor-fold desc="Overridden Methods">

    @Override
    public boolean onCreate() {
        teammatesDb = new TeammatesDb(getContext());
        return true;
    }

    @Override
    public String getType(final Uri uri) {
        Log.d(TAG, "getType(" + uri + ")");
        switch (URI_MATCHER.match(uri)) {
            case TEAMS:
                return TeammatesContract.Teams.CONTENT_TYPE;
            case TEAM_ID:
                return TeammatesContract.Teams.CONTENT_ITEM_TYPE;
            case TEAM_FILTER:
                return TeammatesContract.Teams.CONTENT_TYPE;
            case TEAM_CONTACTS:
                return TeammatesContract.Teams.Contacts.CONTENT_TYPE;
            case TEAM_CONTACT_ID:
                return TeammatesContract.Teams.Contacts.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Cannot determine type. Unsupported uri: " + uri);
        }
    }

    @Override
    public Uri insert(final Uri uri, final ContentValues values) {
        Log.d(TAG, String.format("Insert(%s) values(%s)", uri, values));
        switch (URI_MATCHER.match(uri)) {
            case TEAMS:
                return mInsert(uri, TeammatesContract.Teams.PATH, values);
            case TEAM_CONTACTS:
                return mInsert(uri, TeammatesContract.Teams.Contacts.CONTENT_DIRECTORY, values);
            default:
                throw new IllegalArgumentException(String.
                        format("Error: uri(%s) doesn't support Insert operation.", uri));
        }
    }

    @Override
    public int delete(final Uri uri, final String selection, final String[] selectionArgs) {
        Log.d(TAG, "delete(" + uri + "," + selection + "," + Arrays.toString(selectionArgs) + ")");
        switch (URI_MATCHER.match(uri)) {
            case TEAMS:
            case TEAM_ID:
            case TEAM_FILTER:
                return mDelete(uri, TeammatesContract.Teams.PATH, selection, selectionArgs);
            case TEAM_CONTACTS:
            case TEAM_CONTACT_ID:
                return mDelete(uri, TeammatesContract.Teams.Contacts.CONTENT_DIRECTORY, selection, selectionArgs);
            default:
                throw new IllegalArgumentException(String.
                        format("Error: uri(%s) doesn't support Delete operation.", uri));
        }
    }

    @Override
    public int update(final Uri uri, final ContentValues values, final String selection, final String[] selectionArgs) {
        Log.d(TAG, "delete(" + uri + "," + selection + "," + Arrays.toString(selectionArgs) + ")");

        switch (URI_MATCHER.match(uri)) {
            case TEAMS:
            case TEAM_ID:
            case TEAM_FILTER:
                return mUpdate(uri, TeammatesContract.Teams.PATH, values, selection, selectionArgs);
            case TEAM_CONTACTS:
            case TEAM_CONTACT_ID:
                return mUpdate(uri, TeammatesContract.Teams.Contacts.CONTENT_DIRECTORY, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException(String.
                        format("Error: uri(%s) doesn't support Delete operation.", uri));
        }
    }

    @Override
    public Cursor query(final Uri uri, final String[] projection, String selection, final String[] selectionArgs, final String sortOrder) {
        Log.d(TAG, "mQuery( " + uri + ", " +
                Arrays.toString(projection) + ", " + selection + ", " +
                Arrays.toString(selectionArgs) + ", " + sortOrder + " )");

        final int matcher = URI_MATCHER.match(uri);
        switch (matcher) {
            case TEAMS:
            case TEAM_ID:
            case TEAM_FILTER:
                return mQuery(TeammatesContract.Teams.getQueryBuilder(uri, matcher),
                        projection, selection, selectionArgs, sortOrder);
            case TEAM_CONTACTS:
            case TEAM_CONTACT_ID:
                return mQuery(TeammatesContract.Teams.Contacts.getQueryBuilder(uri, matcher),
                        projection, selection, selectionArgs, sortOrder);
            default:
                Log.e(TAG, String.format("Error: uri(%s) doesn't support Query operation.", uri));
                return null;
        }
    }

    //</editor-fold>
    //<editor-fold desc="Private Methods">

    private Uri mInsert(final Uri uri, final String tableName, final ContentValues values) {
        long rowId = -1;
        Uri result = null;
        final SQLiteDatabase db = teammatesDb.getWritableDatabase();

        if (db != null)
            try {
                rowId = db.insertWithOnConflict(tableName, null, values, SQLiteDatabase.CONFLICT_IGNORE);
            } catch (Exception e) {
                Log.e(TAG, String.format("Unknown Error: %s", e.getMessage()), e);
                e.printStackTrace();
            }

        final Context mContext = getContext();
        if (rowId != -1 && mContext != null) {
            final Uri itemUri = ContentUris.withAppendedId(uri, rowId);
            mContext.getContentResolver().notifyChange(itemUri, null);
            result = itemUri;
        } else
            Log.d(TAG, "Failed to mInsert " + values + " to " + uri);
        return result;
    }

    private int mDelete(final Uri uri, final String tableName, final String selection, final String[] selectionArgs) {
        int count = 0;
        final Context mContext = getContext();
        final SQLiteDatabase db = teammatesDb.getWritableDatabase();
        if (db != null && mContext != null) {
            count = db.delete(tableName, selection, selectionArgs);
            if (count > 0)
                mContext.getContentResolver().notifyChange(uri, null);
        }
        return count;
    }

    private Cursor mQuery(final SQLiteQueryBuilder qb, final String[] projection, final String selection, final String[] selectionArgs, final String sortOrder) {
        Cursor cursor = null;
        final SQLiteDatabase db = this.teammatesDb.getReadableDatabase();
        if (db != null && qb != null) {
            cursor = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        }
        return cursor;
    }

    private int mUpdate(final Uri uri, final String tableName, final ContentValues values, final String selection, final String[] selectionArgs) {
        Log.d(TAG, "update(" + uri + "," + values + "," + selection + "," + Arrays.toString(selectionArgs) + ")");

        int count = 0;
        final Context mContext = getContext();
        final SQLiteDatabase db = teammatesDb.getWritableDatabase();
        if (db != null && mContext != null) {
            count = db.update(tableName, values, selection, selectionArgs);
            if (count > 0)
                mContext.getContentResolver().notifyChange(uri, null);
        }
        return count;
    }

    //</editor-fold>
}

