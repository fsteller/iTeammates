package com.fsteller.mobile.android.teammatesapp.helpers.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Project: ${PROJECT_NAME}
 * Package: ${PACKAGE_NAME}
 * <p/>
 * Description:
 * Created by fhernandezs on 26/12/13 for iTeammates.
 */
public final class TeammatesDb extends SQLiteOpenHelper {

    private static final String TAG = TeammatesDb.class.getSimpleName();

    //<editor-fold desc="Constructor">

    public TeammatesDb(Context context) {
        super(context, Constants.DB_NAME, null, Constants.DB_VERSION);
    }

    //</editor-fold>

    //<editor-fold desc="Overridden Methods">
    @Override
    public void onCreate(final SQLiteDatabase db) {
        Log.i(TAG, "Broadcast: creating tables structures...");
        db.execSQL(Constants.Teams.CreateTable);
        db.execSQL(Constants.Events.CreateTable);
        db.execSQL(Constants.Places.CreateTable);
        db.execSQL(Constants.Schedules.CreateTable);
        db.execSQL(Constants.Notifications.CreateTable);
        db.execSQL(Constants.Configuration.CreateTable);

        db.execSQL(Constants.Contacts.CreateTable);
        db.execSQL(Constants.EventsContacts.CreateTable);
        db.execSQL(Constants.TeamsNotifications.CreateTable);
        db.execSQL(Constants.ContactsNotifications.CreateTable);

        Log.i(TAG, "Broadcast: structure done.");
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int i, final int i2) {
        Log.i(TAG, "Broadcast: upgrading database...");
        Log.i(TAG, "Broadcast: deleting structures...");

        db.execSQL(Constants.Teams.DropTable);
        db.execSQL(Constants.Events.DropTable);
        db.execSQL(Constants.Places.DropTable);
        db.execSQL(Constants.Schedules.DropTable);
        db.execSQL(Constants.Notifications.DropTable);
        db.execSQL(Constants.Configuration.DropTable);

        db.execSQL(Constants.Contacts.DropTable);
        db.execSQL(Constants.EventsContacts.DropTable);
        db.execSQL(Constants.TeamsNotifications.DropTable);
        db.execSQL(Constants.ContactsNotifications.DropTable);

        this.onCreate(db);
    }

    @Override
    public void onOpen(final SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    @Override
    public String toString() {
        return String.format("%s.version_%s", Constants.DB_NAME, Constants.DB_VERSION);
    }

    //</editor-fold>
    //<editor-fold desc="Private Methods">

    private long insert(final String tableName, final ContentValues values) {

        Log.d(TAG, String.format("%s insertOrIgnore on %s", tableName, values));
        long result = -1;
        final SQLiteDatabase db = getWritableDatabase();
        if (db != null)
            try {
                result = db.insertWithOnConflict(tableName, null, values, SQLiteDatabase.CONFLICT_IGNORE);
            } catch (Exception e) {
                Log.e(TAG, String.format("Unknown Error: %s", e.getMessage()), e);
            } finally {
                db.close();
            }
        return result;
    }

    //</editor-fold>
    //<editor-fold desc="Public Enums">

    public static enum DbEntities {Teams, TeamsTeammates}

    //</editor-fold>

    //<editor-fold desc="Inner classes">

    private final class Teams implements IDbEntity {

        private Teams() {
        }

        public long insert(final ContentValues values) {
            return TeammatesDb.this.insert(Constants.Teams.TableName, values);
        }

        public int update(final int[] ids, final ContentValues values) {
            int result = -1;
            return result;
        }

        public ContentValues getData(final int[] ids) {
            return null;
        }

        public Cursor loadAll() {
            final SQLiteDatabase readableDatabase = getReadableDatabase();

            Cursor result = null;
            if (readableDatabase != null) {
                result = readableDatabase.query(
                        Constants.Teams.TableName,
                        Constants.Teams.Fields, null, null, null, null,
                        Constants.Teams.Fields[0], null);


            }
            return result;
        }

    }

    /*
    private final class Teammates implements IEntity {

        @Override
        public Cursor loadAll() {
            return null;
        }

        @Override
        public long insert(ContentValues values) {
            return TeammatesDb.this.insert(Constants.teams.TABLE_NAME, values);
        }

        @Override
        public int update(int[] ids, ContentValues contentValues) {
            return 0;
        }

        @Override
        public ContentValues getData(int[] ids) {
            return null;
        }
    }*/

    private final class TeamsTeammates implements IDbEntity {

        @Override
        public Cursor loadAll() {
            return null;
        }

        @Override
        public long insert(ContentValues values) {
            return TeammatesDb.this.insert(Constants.Contacts.TableName, values);
        }

        @Override
        public int update(int[] ids, ContentValues contentValues) {
            return 0;
        }

        @Override
        public ContentValues getData(int[] ids) {
            return null;
        }
    }

    //</editor-fold>
}

