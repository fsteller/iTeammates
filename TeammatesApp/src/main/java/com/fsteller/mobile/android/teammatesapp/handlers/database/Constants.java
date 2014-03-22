package com.fsteller.mobile.android.teammatesapp.handlers.database;

import android.provider.BaseColumns;

import com.fsteller.mobile.android.teammatesapp.TC;

/**
 * Project: iTeammates
 * Subpackage: handlers.database
 * <p/>
 * Description: Contains all the database objects used for maintenance. Those objects implements
 * {@link Constants.TABLE} abstract class
 * and provides access to the database maintenance scripts like: Create table, Add default values,
 * and Drop table.
 * <p/>
 * Created by fhernandezs on 26/12/13 for iTeammates.
 */
final class Constants {

    //<editor-fold desc="Variables">

    /**
     * Is a {@link Constants.TableTeams} instance. A maintenance object that contains the basic scripts and information of this database table.
     */
    public static final TABLE Teams = new TableTeams();
    /**
     * Is a {@link Constants.TableEvents} instance. A maintenance object that contains the basic scripts and information of this database table.
     */
    public static final TABLE Events = new TableEvents();
    /**
     * Is a {@link Constants.TablePlaces} instance. A maintenance object that contains the basic scripts and information of this database table.
     */
    public static final TABLE Places = new TablePlaces();
    /**
     * Is a {@link Constants.TableContacts} instance. A maintenance object that contains the basic scripts and information of this database table.
     */
    public static final TABLE Contacts = new TableContacts();
    /**
     * Is a {@link Constants.TableSchedules} instance. A maintenance object that contains the basic scripts and information of this database table.
     */
    public static final TABLE Schedules = new TableSchedules();
    /**
     * Is a {@link Constants.TableNotifications} instance. A maintenance object that contains the basic scripts and information of this database table.
     */
    public static final TABLE Notifications = new TableNotifications();
    /**
     * Is a {@link Constants.TableConfiguration} instance. A maintenance object that contains the basic scripts and information of this database table.
     */
    public static final TABLE Configuration = new TableConfiguration();
    /**
     * Is a {@link Constants.TableEventsContacts} instance. A maintenance object that contains the basic scripts and information of this database table.
     */
    public static final TABLE EventsContacts = new TableEventsContacts();
    /**
     * Is a {@link Constants.TableTeamsNotifications} instance. A maintenance object that contains the basic scripts and information of this database table.
     */
    public static final TABLE TeamsNotifications = new TableTeamsNotifications();
    /**
     * Is a {@link Constants.TableContactsNotifications} instance. A maintenance object that contains the basic scripts and information of this database table.
     */
    public static final TABLE ContactsNotifications = new TableContactsNotifications();
    /**
     * Is a {@link Constants.TableMediaContent} instance. A maintenance object that contains the basic scripts and information of this database table.
     */
    public static final TABLE MediaContent = new TableMediaContent();
    /**
     * Is a {@link Constants.TableMediaTypes} instance. A maintenance object that contains the basic scripts and information of this database table.
     */
    public static final TABLE MediaTypes = new TableMediaTypes();

    //</editor-fold>

    //<editor-fold desc="Mayor Constants">
    public final static int DB_VERSION = 1;
    public final static String DB_NAME = "teammates.database";
    //</editor-fold>
    //<editor-fold desc="Table Names Constants, used to give names to tables">
    private final static String TEAMS_TABLE_NAME = "teams";
    private final static String EVENTS_TABLE_NAME = "events";
    private final static String PLACES_TABLE_NAME = "places";
    //private final static String CONTACTS_TABLE_NAME = "contacts";
    private final static String CONTACTS_TABLE_NAME = "contacts";
    private final static String SCHEDULES__TABLE_NAME = "schedules";
    //private final static String TEAMS_TEAMS = "teams_teams";
    private final static String CONFIGURATION_TABLE_NAME = "configuration";
    private final static String NOTIFICATIONS_TABLE_NAME = "notifications";
    private final static String EVENTS_CONTACTS_TABLE_NAME = "events_contacts";
    private final static String TEAMS_NOTIFICATIONS_TABLE_NAME = "teams_notifications";
    private final static String CONTACTS_NOTIFICATIONS_TABLE_NAME = "contacts_notifications";
    private final static String MEDIA_CONTENT_TABLE_NAME = "media_content";
    private final static String MEDIA_TYPES_TABLE_NAME = "media_types";
    //</editor-fold>
    //<editor-fold desc="ID Fields Names Constants, used with foreign keys referenced fields">
    private final static String TEAM_ID_TABLE_FIELD = "team_id";
    private final static String EVENT_ID_TABLE_FIELD = "event_id";
    private final static String PLACE_ID_TABLE_FIELD = "place_id";
    //private final static String FIELD_PARENT_ID = "parent_id";
    private final static String CONTACT_ID_TABLE_FIELD = "contact_id";
    private final static String ACCOUNT_ID_TABLE_FIELD = "account_id";
    private final static String SCHEDULE_ID_TABLE_FIELD = "schedule_id";
    private final static String NOTIFICATION_ID_TABLE_FIELD = "notification_id";
    private final static String CALENDAR_ID_TABLE_FIELD = "calendar_id";
    //</editor-fold>
    //<editor-fold desc="Regular Field Names Constants, used to give name to the tables columns">
    private final static String NAME_TABLE_FIELD = "name";
    private final static String BODY_TABLE_FIELD = "body";
    private final static String COLOR_TABLE_FIELD = "color";
    private final static String CONTACT_TOKEN_TABLE_FIELD = "token";
    private final static String SCHEDULE_DATA_TABLE_FIELD = "schedule_data";
    private final static String DIRECTIONS_DATA_TABLE_FIELD = "directions_data";
    private final static String IMAGE_REFERENCE_TABLE_FIELD = "image_ref";
    private final static String CREATED_AT_TABLE_FILED = "date_creation";
    private final static String UPDATED_AT_TABLE_FIELD = "date_updated";
    private final static String MEDIA_TYPE_TABLE_FIELD = "media_type";
    private final static String MEDIA_BLOB_TABLE_FIELD = "media_blob";
    private final static String MEDIA_TYPE_DESC_TABLE_FIELD = "media_type_desc";

    private final static String ID_TABLE_FIELD = BaseColumns._ID;

    //</editor-fold>
    //<editor-fold desc="Fields Configuration Constants, used to setup columns during tables creation">
    private final static String FIELD_ID_PRIMARYKEY_CONFIGURATION = " Integer Not Null Primary Key Autoincrement, ";
    private final static String FIELD_ID_FOREIGNKEY_CONFIGURATION = " Integer Not Null, ";
    private final static String FIELD_NAME_CONFIGURATION = " Text Not Null, ";
    private final static String FIELD_CREATED_AT_CONFIGURATION = " Int Not Null, ";
    private final static String FIELD_UPDATED_AT_CONFIGURATION = " Int Not Null, ";
    private final static String FIELD_IMAGE_REFERENCE_CONFIGURATION = " Text, ";
    //</editor-fold>

    //<editor-fold desc="SQL Create Tables Sentences Constants">
    private final static String TEAMS_TABLE_CREATE_SCRIPT = "Create Table " + TEAMS_TABLE_NAME + " ( "
            + ID_TABLE_FIELD + FIELD_ID_PRIMARYKEY_CONFIGURATION
            + NAME_TABLE_FIELD + FIELD_NAME_CONFIGURATION
            + CREATED_AT_TABLE_FILED + FIELD_CREATED_AT_CONFIGURATION
            + UPDATED_AT_TABLE_FIELD + FIELD_UPDATED_AT_CONFIGURATION
            + IMAGE_REFERENCE_TABLE_FIELD + FIELD_IMAGE_REFERENCE_CONFIGURATION
            + "Unique(" + NAME_TABLE_FIELD + ") On Conflict Ignore);";
    private final static String EVENTS_TABLE_CREATE_SCRIPT = "Create Table " + EVENTS_TABLE_NAME + " ( "
            + ID_TABLE_FIELD + FIELD_ID_PRIMARYKEY_CONFIGURATION
            + NAME_TABLE_FIELD + FIELD_NAME_CONFIGURATION
            + CREATED_AT_TABLE_FILED + FIELD_CREATED_AT_CONFIGURATION
            + UPDATED_AT_TABLE_FIELD + FIELD_UPDATED_AT_CONFIGURATION
            + IMAGE_REFERENCE_TABLE_FIELD + FIELD_IMAGE_REFERENCE_CONFIGURATION
            + PLACE_ID_TABLE_FIELD + FIELD_ID_FOREIGNKEY_CONFIGURATION
            + SCHEDULE_ID_TABLE_FIELD + FIELD_ID_FOREIGNKEY_CONFIGURATION
            + CALENDAR_ID_TABLE_FIELD + " Text, "
            + "Foreign key (" + PLACE_ID_TABLE_FIELD + ") References " + PLACES_TABLE_NAME + " (" + ID_TABLE_FIELD + ") On Delete Cascade, "
            + "Foreign key (" + SCHEDULE_ID_TABLE_FIELD + ") References " + SCHEDULES__TABLE_NAME + " (" + ID_TABLE_FIELD + ") On Delete Cascade, "
            + "Unique(" + NAME_TABLE_FIELD + ") On Conflict Ignore);";
    private final static String PLACES_TABLE_CREATE_SCRIPT = "Create Table " + PLACES_TABLE_NAME + " ( "
            + ID_TABLE_FIELD + FIELD_ID_PRIMARYKEY_CONFIGURATION
            + DIRECTIONS_DATA_TABLE_FIELD + " text );";
    /*private final static String CONTACTS_TABLE_CREATE_SCRIPT = "Create Table " + CONTACTS_TABLE_NAME +" ( "
            + ID_TABLE_FIELD + FIELD_ID_PRIMARYKEY_CONFIGURATION
            + CONTACT_TOKEN_TABLE_FIELD + " Text Not Null,"
            + " Unique(" + CONTACT_TOKEN_TABLE_FIELD + ") On Conflict Ignore);";*/
    private final static String SCHEDULES_TABLE_CREATE_SCRIPT = "Create Table " + SCHEDULES__TABLE_NAME + " ( "
            + ID_TABLE_FIELD + FIELD_ID_PRIMARYKEY_CONFIGURATION
            + SCHEDULE_DATA_TABLE_FIELD + " text );";
    private final static String NOTIFICATIONS_TABLE_CREATE_SCRIPT = "Create Table " + NOTIFICATIONS_TABLE_NAME + " ( "
            + ID_TABLE_FIELD + FIELD_ID_PRIMARYKEY_CONFIGURATION
            + BODY_TABLE_FIELD + " text,"
            + COLOR_TABLE_FIELD + " int );";
    private final static String CONFIGURATION_TABLE_CREATE_SCRIPT = "Create Table " + CONFIGURATION_TABLE_NAME + " ( "
            + ID_TABLE_FIELD + FIELD_ID_PRIMARYKEY_CONFIGURATION
            + ACCOUNT_ID_TABLE_FIELD + " text );";
    /*private final static String CREATE_TABLE_TEAMS_TEAMS = "Create Table " + TEAMS_TEAMS + " ( "
            + TEAM_ID_TABLE_FIELD + FIELD_ID_FOREIGNKEY_CONFIGURATION
            + FIELD_PARENT_ID + FIELD_ID_FOREIGNKEY_CONFIGURATION
            + "Primary Key (" + TEAM_ID_TABLE_FIELD + ", " + FIELD_PARENT_ID + "),"
            + " Foreign key (" + TEAM_ID_TABLE_FIELD + ") References " + TEAMS_TABLE_NAME + " (" + ID_TABLE_FIELD + "), "
            + " Foreign key (" + FIELD_PARENT_ID + ") References " + TEAMS_TABLE_NAME + "  (" + ID_TABLE_FIELD + "));";*/
   /* private final static String CONTACTS_TABLE_CREATE_SCRIPT = "Create Table " + CONTACTS_TABLE_NAME + " ( "
            + TEAM_ID_TABLE_FIELD + FIELD_ID_FOREIGNKEY_CONFIGURATION
            + CONTACT_ID_TABLE_FIELD + FIELD_ID_FOREIGNKEY_CONFIGURATION
            + "Primary Key (" + TEAM_ID_TABLE_FIELD + ", " + CONTACT_ID_TABLE_FIELD + ")"
            + " Foreign key (" + TEAM_ID_TABLE_FIELD + ") References " + TEAMS_TABLE_NAME + " (" + ID_TABLE_FIELD + "), "
            + " Foreign key (" + CONTACT_ID_TABLE_FIELD + ") References " + CONTACTS_TABLE_NAME + " (" + ID_TABLE_FIELD + "));";*/
    private final static String CONTACTS_TABLE_CREATE_SCRIPT = "Create Table " + CONTACTS_TABLE_NAME + " ("
            + ID_TABLE_FIELD + FIELD_ID_PRIMARYKEY_CONFIGURATION
            + TEAM_ID_TABLE_FIELD + FIELD_ID_FOREIGNKEY_CONFIGURATION
            + CONTACT_TOKEN_TABLE_FIELD + " Integer Not Null, "
            + "Unique(" + TEAM_ID_TABLE_FIELD + ", " + CONTACT_TOKEN_TABLE_FIELD + ") On Conflict Ignore, "
            + "Foreign key (" + TEAM_ID_TABLE_FIELD + ") References " + TEAMS_TABLE_NAME + " (" + ID_TABLE_FIELD + ") On Delete Cascade);";
    private final static String EVENTS_CONTACTS_TABLE_CREATE_SCRIPT = "Create Table " + EVENTS_CONTACTS_TABLE_NAME + " ( "
            + EVENT_ID_TABLE_FIELD + FIELD_ID_FOREIGNKEY_CONFIGURATION
            + CONTACT_ID_TABLE_FIELD + FIELD_ID_FOREIGNKEY_CONFIGURATION
            + "Primary Key (" + EVENT_ID_TABLE_FIELD + ", " + CONTACT_ID_TABLE_FIELD + ") "
            + "Foreign key (" + EVENT_ID_TABLE_FIELD + ") References " + EVENTS_TABLE_NAME + "(" + ID_TABLE_FIELD + "), "
            + "Foreign key (" + CONTACT_ID_TABLE_FIELD + ") References " + CONTACTS_TABLE_NAME + " (" + ID_TABLE_FIELD + "));";
    private final static String TEAMS_NOTIFICATIONS_TABLE_CREATE_SCRIPT = "Create Table " + TEAMS_NOTIFICATIONS_TABLE_NAME + " ( "
            + TEAM_ID_TABLE_FIELD + FIELD_ID_FOREIGNKEY_CONFIGURATION
            + NOTIFICATION_ID_TABLE_FIELD + FIELD_ID_FOREIGNKEY_CONFIGURATION
            + "Primary Key (" + TEAM_ID_TABLE_FIELD + ", " + NOTIFICATION_ID_TABLE_FIELD + ") "
            + "Foreign key (" + TEAM_ID_TABLE_FIELD + ") References " + TEAMS_TABLE_NAME + " (" + ID_TABLE_FIELD + "), "
            + "Foreign key (" + NOTIFICATION_ID_TABLE_FIELD + ") References " + NOTIFICATIONS_TABLE_NAME + " (" + ID_TABLE_FIELD + "));";
    private final static String CONTACTS_NOTIFICATIONS_TABLE_CREATE_SCRIPT = "Create Table " + CONTACTS_NOTIFICATIONS_TABLE_NAME + " ( "
            + CONTACT_ID_TABLE_FIELD + FIELD_ID_FOREIGNKEY_CONFIGURATION
            + NOTIFICATION_ID_TABLE_FIELD + FIELD_ID_FOREIGNKEY_CONFIGURATION
            + "Primary Key (" + CONTACT_ID_TABLE_FIELD + ", " + NOTIFICATION_ID_TABLE_FIELD + ") "
            + "Foreign key (" + CONTACT_ID_TABLE_FIELD + ") References " + CONTACTS_TABLE_NAME + " (" + ID_TABLE_FIELD + "), "
            + "Foreign key (" + NOTIFICATION_ID_TABLE_FIELD + ") References " + NOTIFICATIONS_TABLE_NAME + " (" + ID_TABLE_FIELD + "));";
    private final static String MEDIA_CONTENT_TABLE_CREATE_SCRIPT = "Create Table " + MEDIA_CONTENT_TABLE_NAME + " ( "
            + ID_TABLE_FIELD + FIELD_ID_PRIMARYKEY_CONFIGURATION
            + MEDIA_TYPE_TABLE_FIELD + FIELD_ID_FOREIGNKEY_CONFIGURATION
            + MEDIA_BLOB_TABLE_FIELD + " BLOB Not Null, "
            + CREATED_AT_TABLE_FILED + FIELD_CREATED_AT_CONFIGURATION
            + UPDATED_AT_TABLE_FIELD + FIELD_UPDATED_AT_CONFIGURATION
            + "Foreign key (" + MEDIA_TYPE_TABLE_FIELD + ") References " + MEDIA_TYPES_TABLE_NAME + " (" + ID_TABLE_FIELD + "));";
    private final static String MEDIA_TYPES_TABLE_CREATE_SCRIPT = "Create Table " + MEDIA_TYPES_TABLE_NAME + " ("
            + ID_TABLE_FIELD + FIELD_ID_FOREIGNKEY_CONFIGURATION
            + MEDIA_TYPE_DESC_TABLE_FIELD + " Text Not Null, "
            + "Primary Key (" + ID_TABLE_FIELD + "));";

    //</editor-fold>
    //<editor-fold desc="SQL Add Default Table Rows Sentences Constants">
    private final static String TEAMS_TABLE_ADD_DEFAULT_DATA_SCRIPT = ";";
    private final static String EVENTS_TABLE_ADD_DEFAULT_DATA_SCRIPT = ";";
    private final static String PLACES_TABLE_ADD_DEFAULT_DATA_SCRIPT = ";";
    //private final static String CONTACTS_TABLE_ADD_DEFAULT_DATA_SCRIPT = ";";
    private final static String SCHEDULES_TABLE_ADD_DEFAULT_DATA_SCRIPT = ";";
    private final static String NOTIFICATIONS_TABLE_ADD_DEFAULT_DATA_SCRIPT = ";";
    private final static String CONFIGURATION_TABLE_ADD_DEFAULT_DATA_SCRIPT = ";";
    private final static String MEDIA_CONTENT_TABLE_ADD_DEFAULT_DATA_SCRIPT = ";";
    private final static String MEDIA_TYPES_TABLE_ADD_DEFAULT_DATA_SCRIPT = "Insert Into " + MEDIA_TYPES_TABLE_NAME
            + " Values (" + TC.MediaContentTypes.Image + ", \"Images\");";

    //private final static String ADD_DEFAULTS_TABLE_TEAMS_TEAMS = ";";
    private final static String CONTACTS_TABLE_ADD_DEFAULT_DATA_SCRIPT = ";";
    private final static String EVENTS_CONTACTS_TABLE_ADD_DEFAULT_DATA_SCRIPT = ";";
    private final static String TEAMS_NOTIFICATIONS_TABLE_ADD_DEFAULT_DATA_SCRIPT = ";";
    private final static String CONTACTS_NOTIFICATIONS_TABLE_ADD_DEFAULT_DATA_SCRIPT = ";";

    //</editor-fold>
    //<editor-fold desc="SQL DROP Tables Sentences Constants">
    private final static String TEAMS_TABLE_DROP_SCRIPT = "DROP TABLE IF EXISTS " + TEAMS_TABLE_NAME + ";";
    private final static String EVENTS_TABLE_DROP_SCRIPT = "DROP TABLE IF EXISTS " + EVENTS_TABLE_NAME + ";";
    private final static String PLACES_TABLE_DROP_SCRIPT = "DROP TABLE IF EXISTS " + PLACES_TABLE_NAME + ";";
    //private final static String CONTACTS_TABLE_DROP_SCRIPT = "DROP TABLE IF EXISTS " + CONTACTS_TABLE_NAME + ";";
    private final static String SCHEDULES_TABLE_DROP_SCRIPT = "DROP TABLE IF EXISTS " + SCHEDULES__TABLE_NAME + ";";
    private final static String NOTIFICATIONS_TABLE_DROP_SCRIPT = "DROP TABLE IF EXISTS " + NOTIFICATIONS_TABLE_NAME + ";";
    private final static String CONFIGURATION_TABLE_DROP_SCRIPT = "DROP TABLE IF EXISTS " + CONFIGURATION_TABLE_NAME + ";";
    private final static String MEDIA_CONTENT_TABLE_DROP_SCRIPT = "DROP TABLE IF EXISTS " + MEDIA_CONTENT_TABLE_NAME + ";";
    private final static String MEDIA_TYPES_TABLE_DROP_SCRIPT = "DROP TABLE IF EXISTS " + MEDIA_TYPES_TABLE_NAME + ";";

    //private final static String DROP_TABLE_TEAMS_TEAMS = "DROP TABLE IF EXISTS " + TEAMS_TEAMS + ";";
    private final static String CONTACTS_TABLE_DROP_SCRIPT = "DROP TABLE IF EXISTS " + CONTACTS_TABLE_NAME + ";";
    private final static String EVENTS_CONTACTS_TABLE_DROP_SCRIPT = "DROP TABLE IF EXISTS " + EVENTS_CONTACTS_TABLE_NAME + ";";
    private final static String TEAMS_NOTIFICATIONS_TABLE_DROP_SCRIPT = "DROP TABLE IF EXISTS " + TEAMS_NOTIFICATIONS_TABLE_NAME + ";";
    private final static String CONTACTS_NOTIFICATIONS_TABLE_DROP_SCRIPT = "DROP TABLE IF EXISTS " + CONTACTS_NOTIFICATIONS_TABLE_NAME + ";";

    //</editor-fold>

    //<editor-fold desc="Constructor">

    /**
     * Private constructor used in order to prevent instantiation of this class.
     */
    private Constants() {
    }
    //</editor-fold>
    //<editor-fold desc="InnerClasses">

    /**
     * Project: iTeammates
     * Subpackage: handlers.database.Constants
     * <p/>
     * Description: this is a basic abstract class used to allocate the basic scrips of a database
     * table, this also has the table name and an array with all the table fields names in order to
     * make it easier to access that information.
     * <p/>
     * Created by fhernandezs on 26/12/13 for iTeammates.
     */
    public abstract static class TABLE {
        public final String TableName;
        public final String CreateTableScript;
        public final String InitializeTableScript;
        public final String DropTableScript;
        public final String[] Fields;

        /**
         * Constructor for {@link Constants.TABLE}
         * abstract class, it requires the table name, tables fields, and some basic scripts as parameters.
         *
         * @param tableName             Actual database table name.
         * @param createTableScript     Script to be used to create this table.
         * @param initializeTableScript Script to be used to initialize this table with defaults values.
         * @param dropTableScript       Script to be used to erase this table from the database.
         * @param fields                Array of fields in this table
         */
        TABLE(final String tableName, final String createTableScript,
              final String initializeTableScript,
              final String dropTableScript,
              final String... fields) {
            TableName = tableName;
            CreateTableScript = createTableScript;
            InitializeTableScript = initializeTableScript;
            DropTableScript = dropTableScript;
            Fields = fields;
        }
    }

    /**
     * Collection of information required to create, drop or update table Teams of the iTeammates
     * database.
     */
    private static final class TableTeams extends TABLE {
        TableTeams() {
            super(TEAMS_TABLE_NAME, TEAMS_TABLE_CREATE_SCRIPT, TEAMS_TABLE_ADD_DEFAULT_DATA_SCRIPT, TEAMS_TABLE_DROP_SCRIPT,
                    ID_TABLE_FIELD, NAME_TABLE_FIELD, IMAGE_REFERENCE_TABLE_FIELD, UPDATED_AT_TABLE_FIELD, CREATED_AT_TABLE_FILED);
        }
    }

    /**
     * Collection of information required to create, drop or update table Events of the iTeammates
     * database.
     */
    private static final class TableEvents extends TABLE {
        TableEvents() {
            super(EVENTS_TABLE_NAME, EVENTS_TABLE_CREATE_SCRIPT, EVENTS_TABLE_ADD_DEFAULT_DATA_SCRIPT, EVENTS_TABLE_DROP_SCRIPT,
                    ID_TABLE_FIELD, NAME_TABLE_FIELD, IMAGE_REFERENCE_TABLE_FIELD, UPDATED_AT_TABLE_FIELD, CREATED_AT_TABLE_FILED, PLACE_ID_TABLE_FIELD, SCHEDULE_ID_TABLE_FIELD, CALENDAR_ID_TABLE_FIELD);
        }
    }

    /**
     * Collection of information required to create, drop or update table Places of the iTeammates
     * database.
     */
    private static final class TablePlaces extends TABLE {
        TablePlaces() {
            super(PLACES_TABLE_NAME, PLACES_TABLE_CREATE_SCRIPT, PLACES_TABLE_ADD_DEFAULT_DATA_SCRIPT, PLACES_TABLE_DROP_SCRIPT,
                    ID_TABLE_FIELD, DIRECTIONS_DATA_TABLE_FIELD);
        }
    }

    /*
    private static final class TableContacts extends TABLE {
        TableContacts() {
            super(CONTACTS_TABLE_NAME, CONTACTS_TABLE_CREATE_SCRIPT, CONTACTS_TABLE_ADD_DEFAULT_DATA_SCRIPT, CONTACTS_TABLE_DROP_SCRIPT,
                  ID_TABLE_FIELD, CONTACT_TOKEN_TABLE_FIELD);
        }
    }
    */

    /**
     * Collection of information required to create, drop or update table Schedules of the iTeammates
     * database.
     */
    private static final class TableSchedules extends TABLE {
        TableSchedules() {
            super(SCHEDULES__TABLE_NAME, SCHEDULES_TABLE_CREATE_SCRIPT, SCHEDULES_TABLE_ADD_DEFAULT_DATA_SCRIPT, SCHEDULES_TABLE_DROP_SCRIPT,
                    ID_TABLE_FIELD, SCHEDULE_DATA_TABLE_FIELD);
        }
    }

    /**
     * Collection of information required to create, drop or update table Notifications of the iTeammates
     * database.
     */
    private static final class TableNotifications extends TABLE {
        TableNotifications() {
            super(NOTIFICATIONS_TABLE_NAME, NOTIFICATIONS_TABLE_CREATE_SCRIPT, NOTIFICATIONS_TABLE_ADD_DEFAULT_DATA_SCRIPT, NOTIFICATIONS_TABLE_DROP_SCRIPT,
                    ID_TABLE_FIELD, BODY_TABLE_FIELD, COLOR_TABLE_FIELD);
        }
    }

    /*
    private static final class TableTeamsTeams extends TABLE {
        TableTeamsTeams() {
            super(TEAMS_TEAMS,CREATE_TABLE_TEAMS_TEAMS, ADD_DEFAULTS_TABLE_TEAMS_TEAMS, DROP_TABLE_TEAMS_TEAMS,
                  TEAM_ID_TABLE_FIELD, FIELD_PARENT_ID);
        }
    }*/

    /**
     * Collection of information required to create, drop or update table Contacts of the iTeammates
     * database.
     */
    private static final class TableContacts extends TABLE {
        TableContacts() {
            super(CONTACTS_TABLE_NAME, CONTACTS_TABLE_CREATE_SCRIPT, CONTACTS_TABLE_ADD_DEFAULT_DATA_SCRIPT, CONTACTS_TABLE_DROP_SCRIPT,
                    ID_TABLE_FIELD, TEAM_ID_TABLE_FIELD, CONTACT_TOKEN_TABLE_FIELD);
        }
    }

    /**
     * Collection of information required to create, drop or update table EventContacts of the iTeammates
     * database.
     */
    private static final class TableEventsContacts extends TABLE {
        TableEventsContacts() {
            super(EVENTS_CONTACTS_TABLE_NAME, EVENTS_CONTACTS_TABLE_CREATE_SCRIPT, EVENTS_CONTACTS_TABLE_ADD_DEFAULT_DATA_SCRIPT, EVENTS_CONTACTS_TABLE_DROP_SCRIPT,
                    EVENT_ID_TABLE_FIELD, CONTACT_ID_TABLE_FIELD);
        }
    }

    /**
     * Collection of information required to create, drop or update table TeamsNotifications of the iTeammates
     * database.
     */
    private static final class TableTeamsNotifications extends TABLE {
        TableTeamsNotifications() {
            super(TEAMS_NOTIFICATIONS_TABLE_NAME, TEAMS_NOTIFICATIONS_TABLE_CREATE_SCRIPT, TEAMS_NOTIFICATIONS_TABLE_ADD_DEFAULT_DATA_SCRIPT, TEAMS_NOTIFICATIONS_TABLE_DROP_SCRIPT,
                    TEAM_ID_TABLE_FIELD, NOTIFICATION_ID_TABLE_FIELD);
        }
    }

    /**
     * Collection of information required to create, drop or update table ContactsNotifications of the iTeammates
     * database.
     */
    private static final class TableContactsNotifications extends TABLE {
        TableContactsNotifications() {
            super(CONTACTS_NOTIFICATIONS_TABLE_NAME, CONTACTS_NOTIFICATIONS_TABLE_CREATE_SCRIPT, CONTACTS_NOTIFICATIONS_TABLE_ADD_DEFAULT_DATA_SCRIPT, CONTACTS_NOTIFICATIONS_TABLE_DROP_SCRIPT,
                    CONTACT_ID_TABLE_FIELD, NOTIFICATION_ID_TABLE_FIELD);
        }
    }

    /**
     * Collection of information required to create, drop or update table Configuration of the iTeammates
     * database.
     */
    private static final class TableConfiguration extends TABLE {
        TableConfiguration() {
            super(CONFIGURATION_TABLE_NAME, CONFIGURATION_TABLE_CREATE_SCRIPT, CONFIGURATION_TABLE_ADD_DEFAULT_DATA_SCRIPT, CONFIGURATION_TABLE_DROP_SCRIPT,
                    ID_TABLE_FIELD, ACCOUNT_ID_TABLE_FIELD);
        }
    }

    /**
     * Collection of information required to create, drop or update table MediaContent of the iTeammates
     * database.
     */
    private static final class TableMediaContent extends TABLE {
        TableMediaContent() {
            super(MEDIA_CONTENT_TABLE_NAME, MEDIA_CONTENT_TABLE_CREATE_SCRIPT, MEDIA_CONTENT_TABLE_ADD_DEFAULT_DATA_SCRIPT, MEDIA_CONTENT_TABLE_DROP_SCRIPT,
                    ID_TABLE_FIELD, MEDIA_TYPE_TABLE_FIELD, MEDIA_BLOB_TABLE_FIELD, UPDATED_AT_TABLE_FIELD, CREATED_AT_TABLE_FILED);
        }
    }

    /**
     * Collection of information required to create, drop or update table MediaTypes of the iTeammates
     * database.
     */
    private static final class TableMediaTypes extends TABLE {
        TableMediaTypes() {
            super(MEDIA_TYPES_TABLE_NAME, MEDIA_TYPES_TABLE_CREATE_SCRIPT, MEDIA_TYPES_TABLE_ADD_DEFAULT_DATA_SCRIPT, MEDIA_TYPES_TABLE_DROP_SCRIPT,
                    ID_TABLE_FIELD, MEDIA_TYPE_DESC_TABLE_FIELD);
        }
    }
    //</editor-fold>
}
