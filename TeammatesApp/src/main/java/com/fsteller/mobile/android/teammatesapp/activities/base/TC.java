package com.fsteller.mobile.android.teammatesapp.activities.base;

import android.net.Uri;
import android.provider.CalendarContract;
import android.provider.ContactsContract.Contacts;

import com.fsteller.mobile.android.teammatesapp.helpers.database.TeammatesContract;

/**
 * Created by fhernandezs on 26/12/13 for iTeammates.
 */
public interface TC {

    static interface ActivityActions {

        public static final int Add = 1101;         //Menu Action
        public static final int Create = Add;       //Menu Action

        public static final int Edit = 1103;        //Menu Action
        public static final int Update = Edit;      //Menu Action

        public static final int Drop = 1105;        //Menu Action
        public static final int Delete = Drop;      //Menu Action

        public static final int Search = 1106;      //Menu Action
        public static final int PickImage = 1107;   //Menu Action


        public static final int Share = 1108;       //Menu Action
        public static final int Settings = 1109;    //Menu Action
        public static final int SignIn = 1110;      //Menu Action
        public static final int About = 1111;       //Menu Action
    }

    public static interface Activity {

        public static interface PARAMS {
            public static final String ID = "activity_parameters";
            public static final String ACCOUNT_ID = "activity_account_id";
            public static final String COLLECTION_ID = "activity_collection_id";
            public static final String COLLECTION_ITEMS = "activity_collection_items";
            public static final String COLLECTION_IMAGE_REF = "activity_collection_image_ref";
        }

        public static interface MenuRequest {
            public static final int About = ActivityActions.About;
            public static final int AddItem = ActivityActions.Create;
            public static final int Login = ActivityActions.SignIn;
            public static final int Settings = ActivityActions.Settings;
        }

        public static interface ActionRequest {
            public static final int Edit = ActivityActions.Edit;
            public static final int Share = ActivityActions.Share;
            public static final int Delete = ActivityActions.Delete;
            public static final int PickImage = ActivityActions.PickImage;
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

        /**
         * This interface defines constants for the Cursor and CursorLoader, based on constants defined
         * in the {@link android.provider.ContactsContract.Contacts} class.
         */
        public interface ContactsQuery {
            // A list of identifiers to make able to have different queries within a loader
            final static int SIMPLE_QUERY_ID = 1001;
            final static int FILTER_QUERY_ID1 = 1002;
            final static int FILTER_QUERY_ID2 = 1003;
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
                    // available in earlier versions of Android, so you must use teams.DISPLAY_NAME
                    // instead.
                    Contacts.DISPLAY_NAME_PRIMARY,
                    // In Android 3.0 and later, the thumbnail image is pointed to by
                    // PHOTO_THUMBNAIL_URI. In earlier versions, there is no direct pointer; instead,
                    // you generate the pointer from the contact's ID value and constants defined in
                    // android.provider.ContactsContract.teams.
                    Contacts.PHOTO_THUMBNAIL_URI,
                    // The sort order column for the returned Cursor, used by the AlphabetIndexer
                    SORT_ORDER,
            };
            // The query column numbers which map to each value in the projection
            final static int ID = 0;
            final static int LOOKUP_KEY = 1;
            final static int HAS_PHONE_NUMBER = 2;
            final static int DISPLAY_NAME = 3;
            final static int PHOTO_THUMBNAIL_DATA = 4;
            final static int SORT_KEY = 5;
        }

        public interface CalendarQuery {

            // A list of identifiers to make able to have different queries within a loader
            final static int SIMPLE_QUERY_ID = 2001;
            //final static int FILTER_QUERY_ID1 = 2;
            //final static int FILTER_QUERY_ID2 = 3;
            final static Uri CONTENT_URI = CalendarContract.Calendars.CONTENT_URI;
            //final static Uri FILTER_URI = CalendarContract.Calendars.CONTENT_FILTER_URI;
            final static String SELECTION_BY_TYPE = "((" +
                    CalendarContract.Calendars.ACCOUNT_TYPE + " = ?))";
            final static String SORT_ORDER = CalendarContract.Calendars.CALENDAR_DISPLAY_NAME;

            // Projection array. Creating indices for this array instead of doing
            // dynamic lookups improves performance.
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

        public interface TeamsQuery {

            final static int SIMPLE_QUERY_ID = 3001;
            final static int FILTER_QUERY_ID1 = 3002;
            final static int FILTER_QUERY_ID2 = 3003;
            final static Uri CONTENT_URI = TeammatesContract.Teams.CONTENT_URI;
            final static Uri FILTER_URI = TeammatesContract.Teams.CONTENT_FILTER_URI;
            final static String SELECTION = TeammatesContract.Teams.NAME + "<>''";
            final static String SORT_ORDER = TeammatesContract.Teams.DEFAULT_SORT_ORDER;
            final static String[] TEAMS_PROJECTION = {
                    TeammatesContract.Teams._ID,
                    TeammatesContract.Teams.NAME,
                    TeammatesContract.Teams.IMAGE_REF,
                    TeammatesContract.Teams.UPDATED_AT,
                    TeammatesContract.Teams.CREATED_AT,
            };

            final static String[] TEAMS_CONTACT_PROJECTION = {
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

        public interface EventsQuery {
            final static int SIMPLE_QUERY_ID = 4001;
            final static int FILTER_QUERY_ID1 = 4002;
            final static int FILTER_QUERY_ID2 = 4003;

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

}
