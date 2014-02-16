package com.fsteller.mobile.android.teammatesapp;

import android.net.Uri;
import android.provider.CalendarContract;
import android.provider.ContactsContract.Contacts;

import com.fsteller.mobile.android.teammatesapp.helpers.database.TeammatesContract;

/**
 * Created by fhernandezs on 26/12/13 for iTeammates.
 */
public interface TC {

    public static final int _Teams = 0xf007;
    public static final int _Events = 0xf008;
    public static final int _Contacts = 0xf009;
    public static final int _Notification = 0xf010;
    public static final String ENTITY_DATA = "entity_data";

    static interface ActivityActions {

        public static final int Add = 11001;         //Menu Action
        public static final int Create = Add;       //Menu Action

        public static final int Edit = 11002;        //Menu Action
        public static final int Update = Edit;      //Menu Action

        public static final int Drop = 11003;        //Menu Action
        public static final int Delete = Drop;      //Menu Action

        public static final int Search = 11004;      //Menu Action
        public static final int PickImage = 11005;   //Menu Action


        public static final int Share = 11010;       //Menu Action
        public static final int Settings = 11011;    //Menu Action
        public static final int SignIn = 11012;      //Menu Action
        public static final int About = 11013;       //Menu Action
    }

    public static interface DatabaseActions {

        public static final int TeamAddedToDb = 101;
        public static final int EventAddedToDb = 102;
        public static final int NotificationAddedToDb = 103;

        public static final int TeamUpdatedOnDb = 201;
        public static final int EventUpdatedOnDb = 202;
        public static final int NotificationUpdatedOnDb = 203;

        public static final int TeamDeletedFromDb = 301;
        public static final int EventDeletedFromDb = 302;
        public static final int NotificationDeletedFromDb = 303;
    }

    public static interface Activity {

        public static interface ContextActionRequest {
            public static final int Edit = ActivityActions.Edit;
            public static final int Share = ActivityActions.Share;
            public static final int Delete = ActivityActions.Delete;
            public static final int PickImage = ActivityActions.PickImage;
        }

        public static interface MenuActionRequest {
            public static final int About = ActivityActions.About;
            public static final int AddItem = ActivityActions.Create;
            public static final int Login = ActivityActions.SignIn;
            public static final int Settings = ActivityActions.Settings;
        }

        public static interface DataActionRequest {
            public static final int Add = ActivityActions.Add;
            public static final int Update = ActivityActions.Update;
            public static final int Delete = ActivityActions.Delete;
        }

        public static interface Maintenance {
            public static final int TEAMS = _Teams;
            public static final int EVENTS = _Events;
            public static final int NOTIFICATION = _Notification;
            public static final int CONTACTS = _Contacts;
        }

        public static interface Dialog {
            public static final int About = ActivityActions.About;
            public static final int Share = ActivityActions.Share;
            public static final int SignIn = ActivityActions.SignIn;
            public static final int Settings = ActivityActions.Settings;
        }
    }

    public static interface Broadcast {

        public static final String BROADCAST_ACTION = "broadcastAction";
        public static final String DB_UPDATE_TEAMS_RECEIVE = "com.fsteller.community.android.teammates.DB_UPDATE_TEAMS_RECEIVE";
        public static final String DB_UPDATE_EVENTS_RECEIVE = "com.fsteller.community.android.teammates.DB_UPDATE_EVENTS_RECEIVE";
        public static final String DB_UPDATE_NOTIFICATIONS_RECEIVE = "com.fsteller.community.android.teammates.DB_UPDATE_NOTIFICATIONS_RECEIVE";
    }

    public static interface Queries {

        final static int _PhoneCalendar = 0xf1000;
        final static int _PhoneContacts = 0xf2000;
        final static int _TeammatesTeams = 0xf3000;
        final static int _TeammatesTeamsAndPhoneContacts = 0xf4000;
        final static int _EventsQuery = 0xf5000;

        public interface PhoneCalendar {

            // A list of identifiers to make able to have different queries within a loader
            final static int SIMPLE_QUERY_ID = _PhoneCalendar;
            //final static int FILTER_QUERY_ID1 = 2;
            //final static int FILTER_QUERY_ID2 = 3;
            final static Uri CONTENT_URI = CalendarContract.Calendars.CONTENT_URI;
            //final static Uri FILTER_URI = CalendarContract.Calendars.CONTENT_FILTER_URI;
            final static String SELECTION = "(("
                    + CalendarContract.Calendars.ACCOUNT_TYPE + " = 'com.google') AND ("
                    + CalendarContract.Calendars.ACCOUNT_NAME + " = ?))";
            // By default the order should be the calendar display name
            final static String SORT_ORDER = CalendarContract.Calendars.CALENDAR_DISPLAY_NAME;
            // Projection array. Creating indices for this array instead of doin dynamic lookups improves performance.
            public static final String[] PROJECTION = new String[]{
                    CalendarContract.Calendars._ID,                           // 0
                    CalendarContract.Calendars.ACCOUNT_NAME,                  // 1
                    CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,         // 2
                    CalendarContract.Calendars.CALENDAR_COLOR,                // 3
                    CalendarContract.Calendars.OWNER_ACCOUNT                  // 4
            };
            // The indices for the projection array above.
            final static int ID = 0;
            final static int ACCOUNT_NAME = 1;
            final static int DISPLAY_NAME = 2;
            final static int CALENDAR_COLOR = 3;
            final static int OWNER_ACCOUNT = 4;
            final static int SORT_KEY = 2;
        }

        public interface PhoneContacts {
            // A list of identifiers to make able to have different queries within a loader
            final static int SIMPLE_QUERY_ID = _PhoneContacts + 1;
            final static int FILTER_QUERY_ID1 = _PhoneContacts + 2;
            // A content URI for the teams table
            final static Uri CONTENT_URI = Contacts.CONTENT_URI;
            // The search/filter query Uri
            final static Uri FILTER_URI = Contacts.CONTENT_FILTER_URI;
            // The selection clause for the CursorLoader query. The search criteria defined here
            // restrict results to contacts that have a display name and are linked to visible groups.
            // Notice that the search on the string provided by the user is implemented by appending
            // the search string to CONTENT_FILTER_URI.
            final static String SELECTION = Contacts.DISPLAY_NAME_PRIMARY +
                    "<>'' AND " + Contacts.IN_VISIBLE_GROUP +
                    "=1 AND " + Contacts.HAS_PHONE_NUMBER + " !=0 ";
            // The desired sort order for the returned Cursor. In Android 3.0 and later, the primary
            // sort key allows for localization. In earlier versions. use the display name as the sort
            // key.
            final static String SORT_ORDER = Contacts.SORT_KEY_PRIMARY;
            // The projection for the CursorLoader query. This is a list of columns that the teams
            // Provider should return in the Cursor.
            final static String[] PROJECTION = {
                    // The contact's row id
                    Contacts._ID,
                    // A pointer to the contact that is guaranteed to be more permanent than _ID. Given
                    // a contact's current _ID value and LOOKUP_KEY, the teams Provider can generate
                    // a "permanent" contact URI.
                    Contacts.LOOKUP_KEY,
                    // Indicates whether or not a contact has a registered phone number
                    Contacts.HAS_PHONE_NUMBER,
                    // In platform version 3.0 and later, the teams table contains
                    // DISPLAY_NAME_PRIMARY, which either contains the contact's displayable name or
                    // some other useful identifier such as an email address. This column isn't
                    // available in earlier versions of Android, so you must use teams.CONTACT_NAME
                    // instead.
                    Contacts.DISPLAY_NAME_PRIMARY,
                    Contacts.CONTACT_STATUS,
                    // In Android 3.0 and later, the thumbnail image is pointed to by
                    // PHOTO_THUMBNAIL_URI. In earlier versions, there is no direct pointer; instead,
                    // you generate the pointer from the contact's COLLECTION_ID value and constants defined in
                    // android.provider.ContactsContract.teams.
                    Contacts.PHOTO_THUMBNAIL_URI,
                    // The sort order column for the returned Cursor, used by the AlphabetIndexer
                    SORT_ORDER,
            };
            // The query column numbers which map to each value in the projection
            final static int ID = 0;
            final static int LOOKUP_KEY = 1;
            final static int HAS_PHONE_NUMBER = 2;
            final static int CONTACT_NAME = 3;
            final static int CONTACT_STATUS = 4;
            final static int CONTACT_PHOTO_DATA = 5;
            final static int SORT_KEY = 6;
        }

        public interface TeammatesTeams {
            final static int SIMPLE_QUERY_ID = _TeammatesTeams + 1;
            final static int FILTER_QUERY_ID1 = _TeammatesTeams + 2;
            final static int FILTER_QUERY_ID2 = _TeammatesTeams + 3;
            final static Uri CONTENT_URI = TeammatesContract.Teams.CONTENT_URI;
            final static Uri FILTER_URI = TeammatesContract.Teams.CONTENT_FILTER_URI;
            final static String SELECTION = TeammatesContract.Teams.NAME + "<>''";
            final static String SORT_ORDER = TeammatesContract.Teams.DEFAULT_SORT_ORDER;

            final static String[] PROJECTION = {
                    TeammatesContract.Teams._ID,
                    TeammatesContract.Teams.NAME,
                    TeammatesContract.Teams.IMAGE_REF,
                    TeammatesContract.Teams.UPDATED_AT,
                    TeammatesContract.Teams.CREATED_AT,
            };

            final static String[] TEAM_CONTACT_PROJECTION = {
                    TeammatesContract.Teams.Contacts.TEAM_ID,
                    TeammatesContract.Teams.NAME,
                    TeammatesContract.Teams.IMAGE_REF,
                    TeammatesContract.Teams.UPDATED_AT,
                    TeammatesContract.Teams.CREATED_AT,
                    TeammatesContract.Teams.Contacts.TOKEN
            };

            // The query column numbers which map to each value in the projection

            final static int ID = 0;
            final static int NAME = 1;
            final static int IMAGE_REF = 2;
            final static int UPDATED_AT = 3;
            final static int CREATED_AT = 4;
            final static int CONTACT_TOKEN = 5;
            final static int SORT_KEY = 1;
        }

        public interface TeammatesTeamsAndPhoneContacts {

            final static int SIMPLE_QUERY_ID = _TeammatesTeamsAndPhoneContacts;

        }

        public interface EventsQuery {
            final static int SIMPLE_QUERY_ID = _EventsQuery;
            final static int FILTER_QUERY_ID1 = _EventsQuery + 1;
            final static int FILTER_QUERY_ID2 = _EventsQuery + 2;

            final static Uri CONTENT_URI = TeammatesContract.Events.CONTENT_URI;
            final static Uri FILTER_URI = TeammatesContract.Events.CONTENT_FILTER_URI;
            final static String SELECTION = TeammatesContract.Events.NAME + "<>''";
            final static String SORT_ORDER = TeammatesContract.Events.DEFAULT_SORT_ORDER;
            final static String[] EVENTS_PROJECTION = {
                    TeammatesContract.Events._ID,
                    TeammatesContract.Events.NAME,
                    TeammatesContract.Events.IMAGE_REF,
                    TeammatesContract.Events.UPDATED_AT,
                    TeammatesContract.Events.CREATED_AT,
                    TeammatesContract.Events.PLACE_ID,
                    TeammatesContract.Events.SCHEDULE_ID,
                    TeammatesContract.Events.CALENDAR_ID
            };

            // The query column numbers which map to each value in the projection
            final static int ID = 0;
            final static int NAME = 1;
            final static int IMAGE_REF = 2;
            final static int UPDATED_AT = 3;
            final static int CREATED_AT = 4;
            final static int PLACE_ID = 5;
            final static int SCHEDULE_ID = 6;
            final static int CALENDAR_ID = 7;
            final static int SORT_KEY = 1;
        }
    }

    public static interface ENTITY {
        public static final String ID = "activity_id";
        public static final String EXTRAS = "activity_Extras";
        public static final String ACCOUNT_ID = "activity_account_id";
        public static final String COLLECTION_ID = "activity_collection_id";
        public static final String COLLECTION_NAME = "activity_collection_name";
        public static final String COLLECTION_ITEMS = "activity_collection_items";
        public static final String COLLECTION_IMAGE_REF = "activity_collection_image_ref";
        public static final String COLLECTION_CREATE_DATE = "activity_collection_create_date";
        public static final String COLLECTION_UPDATE_DATE = "activity_collection_update_date";
    }

}
