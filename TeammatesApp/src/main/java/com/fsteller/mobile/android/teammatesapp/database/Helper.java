package com.fsteller.mobile.android.teammatesapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Project: iTeammates
 * Subpackage: database
 * <p/>
 * Description: ins in charge of create and upgrade the iTeammates data base, it takes advantage of
 * ${@link Constants} static class in order
 * to extract all the required scripts to create, initialize and upgrade the database.
 * <p/>
 * Created by fhernandezs on 26/12/13 for iTeammates.
 */
public final class Helper extends SQLiteOpenHelper {

    //<editor-fold desc="Constants">
    private static final String TAG = Helper.class.getSimpleName();
    //</editor-fold>
    //<editor-fold desc="Constructor">

    /**
     * Initializes the ${@link SQLiteOpenHelper} with the provided context.<P/>
     * It also internally sets the database name and version using
     * ${@link Constants} static class
     * as reference.
     *
     * @param context Brings global information about an application environment to the database.
     */
    public Helper(final Context context) {
        super(context, Constants.DB_NAME, null, Constants.DB_VERSION);
    }

    //</editor-fold>

    //<editor-fold desc="Overridden Methods">
    @Override
    public void onCreate(final SQLiteDatabase db) {
        Log.i(TAG, "Broadcast: Creating database table structures...");

        db.execSQL(Constants.MediaTypes.CreateTableScript);
        db.execSQL(Constants.MediaContent.CreateTableScript);

        db.execSQL(Constants.Teams.CreateTableScript);
        db.execSQL(Constants.Events.CreateTableScript);
        db.execSQL(Constants.Places.CreateTableScript);
        db.execSQL(Constants.Schedules.CreateTableScript);
        db.execSQL(Constants.Notifications.CreateTableScript);
        db.execSQL(Constants.Configuration.CreateTableScript);

        db.execSQL(Constants.Contacts.CreateTableScript);
        db.execSQL(Constants.EventsContacts.CreateTableScript);
        db.execSQL(Constants.TeamsNotifications.CreateTableScript);
        db.execSQL(Constants.ContactsNotifications.CreateTableScript);

        Log.i(TAG, "Broadcast: Database structure creation done...");
        Log.i(TAG, "Broadcast: Initializing database tables with default data...");

        db.execSQL(Constants.MediaTypes.InitializeTableScript);
        //db.execSQL(Constants.MediaContent.InitializeTableScript);

        Log.i(TAG, "Broadcast: Initialization done...");

    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int i, final int i2) {
        Log.i(TAG, "Broadcast: Upgrading database table structures...");
        Log.i(TAG, "Broadcast: Deleting structures...");

        db.execSQL(Constants.MediaContent.DropTableScript);
        db.execSQL(Constants.MediaTypes.DropTableScript);

        db.execSQL(Constants.EventsContacts.DropTableScript);
        db.execSQL(Constants.TeamsNotifications.DropTableScript);
        db.execSQL(Constants.ContactsNotifications.DropTableScript);

        db.execSQL(Constants.Teams.DropTableScript);
        db.execSQL(Constants.Events.DropTableScript);
        db.execSQL(Constants.Places.DropTableScript);
        db.execSQL(Constants.Contacts.DropTableScript);
        db.execSQL(Constants.Schedules.DropTableScript);
        db.execSQL(Constants.Notifications.DropTableScript);
        db.execSQL(Constants.Configuration.DropTableScript);

        Log.i(TAG, "Broadcast: Structures Deleted...");

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
}
