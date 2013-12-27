package com.fsteller.mobile.android.teammatesapp.helpers.databse;

import android.provider.BaseColumns;

/**
 * Created by fhernandezs on 26/12/13 for iTeammates.
 */
class Constants {

    //<editor-fold desc="Variables">
    public static final TABLE Teams = new TableTeams();
    public static final TABLE Events = new TableEvents();
    public static final TABLE Places = new TablePlaces();
    public static final TABLE Contacts = new TableContacts();
    public static final TABLE Schedules = new TableSchedules();
    public static final TABLE Notifications = new TableNotifications();
    public static final TABLE Configuration = new TableConfiguration();
    public static final TABLE EventsContacts = new TableEventsContacts();
    public static final TABLE TeamsNotifications = new TableTeamsNotifications();
    public static final TABLE ContactsNotifications = new TableContactsNotifications();
    //</editor-fold>

    //<editor-fold desc="Mayor Constants">
    public final static int DB_VERSION = 1;
    public final static String DB_NAME = "teammates.database";
    //</editor-fold>
    //<editor-fold desc="ID Fields Names Constants">
    private final static String FIELD_TEAM_ID = "team_id";
    private final static String FIELD_EVENT_ID = "event_id";
    private final static String FIELD_PLACE_ID = "place_id";
    private final static String FIELD_PARENT_ID = "parent_id";
    private final static String FIELD_CONTACT_ID = "contact_id";
    private final static String FIELD_ACCOUNT_ID = "account_id";
    private final static String FIELD_SCHEDULE_ID = "schedule_id";
    private final static String FIELD_NOTIFICATION_ID = "notification_id";
    private final static String FIELD_CALENDAR_ID = "calendar_id";
    //</editor-fold>
    //<editor-fold desc="Regular Fields Names Constants">
    private final static String FIELD_NAME = "name";
    private final static String FIELD_BODY = "body";
    private final static String FIELD_COLOR = "color";
    private final static String FIELD_CONTACT_TOKEN = "token";
    private final static String FIELD_SCHEDULE_DATA = "schedule_data";
    private final static String FIELD_DIRECTIONS_DATA = "directions_data";
    private final static String FIELD_IMAGE_REFERENCE = "image_ref";
    private final static String FIELD_CREATED_AT = "created_date";
    private final static String FIELD_UPDATED_AT = "updated_date";

    private final static String FIELD_ID = BaseColumns._ID;
    //</editor-fold>
    //<editor-fold desc="Table Names Constants">
    private final static String TEAMS = "teams";
    private final static String EVENTS = "events";
    private final static String PLACES = "places";
    //private final static String CONTACTS = "contacts";
    private final static String CONTACTS = "contacts";
    private final static String SCHEDULES = "schedules";
    private final static String TEAMS_TEAMS = "teams_teams";
    private final static String CONFIGURATION = "configuration";
    private final static String NOTIFICATIONS = "notifications";
    private final static String EVENTS_CONTACTS = "events_contacts";
    private final static String TEAMS_NOTIFICATIONS = "teams_notifications";
    private final static String CONTACTS_NOTIFICATIONS = "contacts_notifications";
    //</editor-fold>

    //<editor-fold desc="SQL Create Tables Sentences Constants">
    private final static String CREATE_TABLE_TEAMS = "Create Table " + TEAMS + " ( "
            + FIELD_ID + " Integer  Not Null Primary Key Autoincrement, "
            + FIELD_NAME + " Text Not Null, "
            + FIELD_CREATED_AT + " Int Not Null, "
            + FIELD_UPDATED_AT + " Int Not Null, "
            + FIELD_IMAGE_REFERENCE + " Text Not Null, "
            + "Unique(" + FIELD_NAME + ") On Conflict Ignore);";
    private final static String CREATE_TABLE_EVENTS = "Create Table " + EVENTS + " ( "
            + FIELD_ID + " Integer Not Null Primary Key Autoincrement, "
            + FIELD_NAME + " Text Not Null, "
            + FIELD_CREATED_AT + " Int Not Null, "
            + FIELD_UPDATED_AT + " Int Not Null, "
            + FIELD_IMAGE_REFERENCE + " Text Not Null, "
            + FIELD_PLACE_ID + " Int Not Null, "
            + FIELD_SCHEDULE_ID + " Int not Null, "
            + FIELD_CALENDAR_ID + " Text, "
            + "Foreign key (" + FIELD_PLACE_ID + ") References " + PLACES + " (" + FIELD_ID + ") On Delete Cascade, "
            + "Foreign key (" + FIELD_SCHEDULE_ID + ") References " + SCHEDULES + " (" + FIELD_ID + ") On Delete Cascade, "
            + "Unique(" + FIELD_NAME + ") On Conflict Ignore);";
    private final static String CREATE_TABLE_PLACES = "Create Table " + PLACES +
            " ( " + FIELD_ID + " Integer Not Null Primary Key Autoincrement, " + FIELD_DIRECTIONS_DATA + " text );";
    /*private final static String CREATE_TABLE_CONTACTS = "Create Table " + CONTACTS +" ( "
            + FIELD_ID + " Integer Not Null Primary Key Autoincrement, "
            + FIELD_CONTACT_TOKEN + " Text Not Null,"
            + " Unique(" + FIELD_CONTACT_TOKEN + ") On Conflict Ignore);";*/
    private final static String CREATE_TABLE_SCHEDULES = "Create Table " + SCHEDULES +
            " ( " + FIELD_ID + " Integer Not Null Primary Key Autoincrement, " + FIELD_SCHEDULE_DATA + " text );";
    private final static String CREATE_TABLE_NOTIFICATIONS = "Create Table " + NOTIFICATIONS +
            " ( " + FIELD_ID + " Integer Not Null Primary Key Autoincrement, " + FIELD_BODY + " text," + FIELD_COLOR + " int );";
    private final static String CREATE_TABLE_CONFIGURATION = "Create Table " + CONFIGURATION +
            " ( " + FIELD_ID + " Integer Not Null Primary Key Autoincrement, " + FIELD_ACCOUNT_ID + " text );";
    /*private final static String CREATE_TABLE_TEAMS_TEAMS = "Create Table " + TEAMS_TEAMS
            + " ( " + FIELD_TEAM_ID + " Integer Not Null, " + FIELD_PARENT_ID +
            " Integer Not Null, Primary Key (" + FIELD_TEAM_ID + ", " + FIELD_PARENT_ID + ")," +
            " Foreign key (" + FIELD_TEAM_ID + ") References " + TEAMS + " (" + FIELD_ID + "), " +
            " Foreign key (" + FIELD_PARENT_ID + ") References " + TEAMS + "  (" + FIELD_ID + "));";*/
   /* private final static String CREATE_TABLE_CONTACTS = "Create Table " + CONTACTS
            + " ( " + FIELD_TEAM_ID + " Integer Not Null, " + FIELD_CONTACT_ID +
            " Integer Not Null, Primary Key (" + FIELD_TEAM_ID + ", " + FIELD_CONTACT_ID + ")" +
            " Foreign key (" + FIELD_TEAM_ID + ") References " + TEAMS + " (" + FIELD_ID + "), " +
            " Foreign key (" + FIELD_CONTACT_ID + ") References " + CONTACTS + " (" + FIELD_ID + "));";*/
    private final static String CREATE_TABLE_CONTACTS = "Create Table " + CONTACTS + " ("
            + FIELD_ID + " Integer  Not Null Primary Key Autoincrement, "
            + FIELD_TEAM_ID + " Integer Not Null, "
            + FIELD_CONTACT_TOKEN + " Integer Not Null, "
            + "Unique(" + FIELD_TEAM_ID + ", " + FIELD_CONTACT_TOKEN + ") On Conflict Ignore, "
            + "Foreign key (" + FIELD_TEAM_ID + ") References " + TEAMS + " (" + FIELD_ID + ") On Delete Cascade);";
    private final static String CREATE_TABLE_EVENTS_CONTACTS = "Create Table " + EVENTS_CONTACTS
            + " ( " + FIELD_EVENT_ID + " Integer Not Null, " + FIELD_CONTACT_ID +
            " Integer Not Null, Primary Key (" + FIELD_EVENT_ID + ", " + FIELD_CONTACT_ID + ")" +
            " Foreign key (" + FIELD_EVENT_ID + ") References " + EVENTS + "(" + FIELD_ID + "), " +
            " Foreign key (" + FIELD_CONTACT_ID + ") References " + CONTACTS + " (" + FIELD_ID + "));";
    private final static String CREATE_TABLE_TEAMS_NOTIFICATIONS = "Create Table " + TEAMS_NOTIFICATIONS
            + " ( " + FIELD_TEAM_ID + " Integer Not Null, " + FIELD_NOTIFICATION_ID +
            " Integer Not Null, Primary Key (" + FIELD_TEAM_ID + ", " + FIELD_NOTIFICATION_ID + ")" +
            " Foreign key (" + FIELD_TEAM_ID + ") References " + TEAMS + " (" + FIELD_ID + "), " +
            " Foreign key (" + FIELD_NOTIFICATION_ID + ") References " + NOTIFICATIONS + " (" + FIELD_ID + "));";
    private final static String CREATE_TABLE_CONTACTS_NOTIFICATIONS = "Create Table " + CONTACTS_NOTIFICATIONS
            + " ( " + FIELD_CONTACT_ID + " Integer Not Null, " + FIELD_NOTIFICATION_ID +
            " Integer Not Null, Primary Key (" + FIELD_CONTACT_ID + ", " + FIELD_NOTIFICATION_ID + ")" +
            " Foreign key (" + FIELD_CONTACT_ID + ") References " + CONTACTS + " (" + FIELD_ID + "), " +
            " Foreign key (" + FIELD_NOTIFICATION_ID + ") References " + NOTIFICATIONS + " (" + FIELD_ID + "));";
    //</editor-fold>
    //<editor-fold desc="SQL Create Tables Constraints Sentences Constants">
    //</editor-fold>
    //<editor-fold desc="SQL Delete Tables Sentences Constants">
    private final static String DROP_TABLE_TEAMS = "DROP TABLE IF EXISTS " + TEAMS + ";";
    private final static String DROP_TABLE_EVENTS = "DROP TABLE IF EXISTS " + EVENTS + ";";
    private final static String DROP_TABLE_PLACES = "DROP TABLE IF EXISTS " + PLACES + ";";
    //private final static String DROP_TABLE_CONTACTS = "DROP TABLE IF EXISTS " + CONTACTS + ";";
    private final static String DROP_TABLE_SCHEDULES = "DROP TABLE IF EXISTS " + SCHEDULES + ";";
    private final static String DROP_TABLE_NOTIFICATIONS = "DROP TABLE IF EXISTS " + NOTIFICATIONS + ";";
    private final static String DROP_TABLE_CONFIGURATION = "DROP TABLE IF EXISTS " + CONFIGURATION + ";";
    //private final static String DROP_TABLE_TEAMS_TEAMS = "DROP TABLE IF EXISTS " + TEAMS_TEAMS + ";";
    private final static String DROP_TABLE_CONTACTS = "DROP TABLE IF EXISTS " + CONTACTS + ";";
    private final static String DROP_TABLE_EVENTS_CONTACTS = "DROP TABLE IF EXISTS " + EVENTS_CONTACTS + ";";
    private final static String DROP_TABLE_TEAMS_NOTIFICATIONS = "DROP TABLE IF EXISTS " + TEAMS_NOTIFICATIONS + ";";
    private final static String DROP_TABLE_CONTACTS_NOTIFICATIONS = "DROP TABLE IF EXISTS " + CONTACTS_NOTIFICATIONS + ";";

    //</editor-fold>

    public abstract static class TABLE {
        public final String TableName;
        public final String CreateTable;
        public final String DropTable;
        public final String[] Fields;

        protected TABLE(String tableName, String createTable, String dropTable, String[] fields) {
            TableName = tableName;
            CreateTable = createTable;
            DropTable = dropTable;
            Fields = fields;
        }
    }

    private static final class TableTeams extends TABLE {
        TableTeams() {
            super(TEAMS, CREATE_TABLE_TEAMS, DROP_TABLE_TEAMS,
                    new String[]{FIELD_ID, FIELD_NAME, FIELD_IMAGE_REFERENCE, FIELD_UPDATED_AT, FIELD_CREATED_AT});
        }
    }

    private static final class TableEvents extends TABLE {
        TableEvents() {
            super(EVENTS, CREATE_TABLE_EVENTS, DROP_TABLE_EVENTS,
                    new String[]{FIELD_ID, FIELD_NAME, FIELD_IMAGE_REFERENCE, FIELD_UPDATED_AT, FIELD_CREATED_AT, FIELD_PLACE_ID, FIELD_SCHEDULE_ID, FIELD_CALENDAR_ID});
        }
    }

    private static final class TablePlaces extends TABLE {
        TablePlaces() {
            super(PLACES, CREATE_TABLE_PLACES, DROP_TABLE_PLACES,
                    new String[]{FIELD_ID, FIELD_DIRECTIONS_DATA});
        }
    }

    /*
    private static final class TableContacts extends TABLE {
        TableContacts() {
            super(CONTACTS, CREATE_TABLE_CONTACTS, DROP_TABLE_CONTACTS,
                    new String[]{FIELD_ID, FIELD_CONTACT_TOKEN});
        }
    }
    */
    private static final class TableSchedules extends TABLE {
        TableSchedules() {
            super(SCHEDULES, CREATE_TABLE_SCHEDULES, DROP_TABLE_SCHEDULES,
                    new String[]{FIELD_ID, FIELD_SCHEDULE_DATA});
        }
    }

    private static final class TableNotifications extends TABLE {
        TableNotifications() {
            super(NOTIFICATIONS, CREATE_TABLE_NOTIFICATIONS, DROP_TABLE_NOTIFICATIONS,
                    new String[]{FIELD_ID, FIELD_BODY, FIELD_COLOR});
        }
    }

    /*
    private static final class TableTeamsTeams extends TABLE {
        TableTeamsTeams() {
            super(TEAMS_TEAMS,CREATE_TABLE_TEAMS_TEAMS,DROP_TABLE_TEAMS_TEAMS,
                    new String[]{FIELD_TEAM_ID, FIELD_PARENT_ID});
        }
    }*/

    private static final class TableContacts extends TABLE {
        TableContacts() {
            super(CONTACTS, CREATE_TABLE_CONTACTS, DROP_TABLE_CONTACTS,
                    new String[]{FIELD_ID, FIELD_TEAM_ID, FIELD_CONTACT_TOKEN});
        }
    }

    private static final class TableEventsContacts extends TABLE {
        TableEventsContacts() {
            super(EVENTS_CONTACTS, CREATE_TABLE_EVENTS_CONTACTS, DROP_TABLE_EVENTS_CONTACTS,
                    new String[]{FIELD_EVENT_ID, FIELD_CONTACT_ID});
        }
    }

    private static final class TableTeamsNotifications extends TABLE {
        TableTeamsNotifications() {
            super(TEAMS_NOTIFICATIONS, CREATE_TABLE_TEAMS_NOTIFICATIONS, DROP_TABLE_TEAMS_NOTIFICATIONS,
                    new String[]{FIELD_TEAM_ID, FIELD_NOTIFICATION_ID});
        }
    }

    private static final class TableContactsNotifications extends TABLE {
        TableContactsNotifications() {
            super(CONTACTS_NOTIFICATIONS, CREATE_TABLE_CONTACTS_NOTIFICATIONS, DROP_TABLE_CONTACTS_NOTIFICATIONS,
                    new String[]{FIELD_CONTACT_ID, FIELD_NOTIFICATION_ID});
        }
    }

    private static final class TableConfiguration extends TABLE {
        TableConfiguration() {
            super(CONFIGURATION, CREATE_TABLE_CONFIGURATION, DROP_TABLE_CONFIGURATION,
                    new String[]{FIELD_ID, FIELD_ACCOUNT_ID});
        }
    }
}
