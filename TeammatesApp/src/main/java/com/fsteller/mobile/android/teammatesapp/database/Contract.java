package com.fsteller.mobile.android.teammatesapp.database;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;

import java.util.Arrays;
import java.util.List;

/**
 * Project: iTeammates
 * Subpackage: database
 * <p/>
 * Description: This is the iTeammates database contract class used to with
 * ${@link com.fsteller.mobile.android.teammatesapp.database.ContentProvider} in order to
 * be able to add, delete, and update registries.<p/>
 * Inside this class you would find interfaces and subclasses related to metadata required to
 * perform database operations.
 * <p/>
 * Created by fhernandezs on 26/12/13 for iTeammates.
 */
public final class Contract {

    //<editor-fold desc="Main Variables">

    /**
     * String representation of the authority related to the iTeammates database.
     */
    public static final String AUTHORITY = "com.fsteller.community.teammates.provider";
    private static final String SCHEME = "content://";
    /**
     * Authority used to access iTeammates database.
     */
    private static final Uri AUTHORITY_URI = Uri.parse(SCHEME + AUTHORITY);
    private static final String ITEM_TEXT_SUFFIX = "/*";
    private static final String ITEM_NUMERIC_SUFFIX = "/#";
    private static final String ITEM_FILTER_PATH_SUFFIX = "filter";

    //</editor-fold >
    //<editor-fold desc="Constructor">

    /**
     * Private constructor used in order to prevent instantiation of this class.
     */
    private Contract() {
    }

    //</editor-fold>

    //<editor-fold desc="Interfaces">

    /**
     * Interface that encapsulate the information related to the TEAMS table of the iTeammates database.
     */
    public static interface TeamsColumns extends BaseColumns {
        public static final String NAME = Constants.Teams.Fields[1];
        public static final String IMAGE_REF = Constants.Teams.Fields[2];
        public static final String UPDATED_AT = Constants.Teams.Fields[3];
        public static final String CREATED_AT = Constants.Teams.Fields[4];
        public static final String DEFAULT_SORT_ORDER = NAME + " ASC";
        public static final String TABLE_NAME = Constants.Teams.TableName;
        public static final List<String> COLUMNS = Arrays.asList((Constants.Teams.Fields));
    }

    /**
     * Interface that encapsulate the information related to the CONTACTS table of the iTeammates database.
     */
    public static interface ContactsColumns extends BaseColumns {
        public static final String TEAM_ID = Constants.Contacts.Fields[1];
        public static final String TOKEN = Constants.Contacts.Fields[2];
        public static final String DEFAULT_SORT_ORDER = TOKEN + " DESC";
        public static final String TABLE_NAME = Constants.Contacts.TableName;
        public static final List<String> COLUMNS = Arrays.asList((Constants.Contacts.Fields));
    }

    /**
     * Interface that encapsulate the information related to the EVENTS table of the iTeammates database.
     */
    public static interface EventsColumns extends BaseColumns {
        public static final String NAME = Constants.Events.Fields[1];
        public static final String IMAGE_REF = Constants.Events.Fields[2];
        public static final String UPDATED_AT = Constants.Events.Fields[3];
        public static final String CREATED_AT = Constants.Events.Fields[4];
        public static final String PLACE_ID = Constants.Events.Fields[5];
        public static final String SCHEDULE_ID = Constants.Events.Fields[6];
        public static final String CALENDAR_ID = Constants.Events.Fields[7];
        public static final String DEFAULT_SORT_ORDER = NAME + " ASC";
        public static final String TABLE_NAME = Constants.Events.TableName;
        public static final List<String> COLUMNS = Arrays.asList((Constants.Events.Fields));
    }

    /**
     * Interface that encapsulate the information related to the MEDIA_CONTENT table of the iTeammates database.
     */
    public static interface MediaContentColumns extends BaseColumns {
        public static final String TABLE_NAME = Constants.MediaContent.TableName;
        public static final String MEDIA_TYPE = Constants.MediaContent.Fields[1];
        public static final String MEDIA_BLOB = Constants.MediaContent.Fields[2];
        public static final String UPDATED_AT = Constants.MediaContent.Fields[3];
        public static final String CREATED_AT = Constants.MediaContent.Fields[4];
        public static final String DEFAULT_SORT_ORDER = UPDATED_AT + " ASC";
        public static final List<String> COLUMNS = Arrays.asList((Constants.MediaContent.Fields));
    }

    //</editor-fold>
    //<editor-fold desc="Inner Classes">

    public static final class Teams implements TeamsColumns {

        //<editor-fold desc="Constants">

        static final String PATH = TABLE_NAME;

        /**
         * Private constructor used in order to prevent instantiation of this class.
         */
        private Teams() {
        }

        static final String PATH_ID = PATH + ITEM_NUMERIC_SUFFIX;

        public static Uri getTeamDataUri(final long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        static final String PATH_EMPTY_FILTER = PATH + "/" + ITEM_FILTER_PATH_SUFFIX;

        static SQLiteQueryBuilder getQueryBuilder(final Uri uri, final int code) {
            final SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
            builder.setTables(Teams.TABLE_NAME);

            switch (code) {
                case ContentProvider.TEAMS:
                case ContentProvider.TEAM_ID:
                    if (!Teams.TABLE_NAME.equals(uri.getLastPathSegment()))
                        builder.appendWhere(String.format("%s = %s", TeamsColumns._ID, uri.getLastPathSegment()));
                    break;
                case ContentProvider.TEAM_FILTER:
                    if (!ITEM_FILTER_PATH_SUFFIX.equals(uri.getLastPathSegment()))
                        builder.appendWhere(String.format("%s like '%%%s%%'", TeamsColumns.NAME, uri.getLastPathSegment()));
                    break;
                default:
                    throw new IllegalArgumentException("Cannot generate query statement for uri (" + uri + "). Unsupported uri code: " + code);
            }


            return builder;
        }

        static final String PATH_NUMERIC_FILTER = PATH_EMPTY_FILTER + ITEM_NUMERIC_SUFFIX;

        public final static class Contacts implements ContactsColumns {

            //<editor-fold desc="Constants">

            public static final String CONTENT_DIRECTORY = TABLE_NAME;
            static final String PATH = Teams.PATH + "/#/" + TABLE_NAME;
            static final String ID_PATH = PATH + ITEM_NUMERIC_SUFFIX;

            /**
             * Private constructor used in order to prevent instantiation of this class.
             */
            private Contacts() {
            }

            public static Uri getTeamContactUri(int id) {
                final Uri result = getTeamDataUri(id);
                return Uri.withAppendedPath(result, CONTENT_DIRECTORY);
            }

            public static final String CONTENT_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd." + AUTHORITY + "." + PATH;

            static SQLiteQueryBuilder getQueryBuilder(final Uri uri, final int code) {
                final List<String> segments = uri.getPathSegments();
                if (segments != null && code == ContentProvider.TEAM_CONTACTS) {
                    final String teamId = segments.get(1);
                    final String joinedTables = String.format("%s INNER JOIN %s ON (%s.%s = %s.%s)",
                            Teams.TABLE_NAME, Contacts.TABLE_NAME,
                            Teams.TABLE_NAME, Teams._ID,
                            Contacts.TABLE_NAME, Contacts.TEAM_ID);

                    String where = String.format("%s.%s=%s", Teams.TABLE_NAME, Teams._ID, teamId);
                    if (!Contacts.TABLE_NAME.equals(uri.getLastPathSegment()))
                        where += String.format(" and %s.%s=%s", Contacts.TABLE_NAME, Contacts._ID,
                                uri.getLastPathSegment());

                    final SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
                    builder.setTables(joinedTables);
                    builder.appendWhere(where);
                    return builder;
                }
                return null;
            }

            public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd." + AUTHORITY + "." + PATH;

            //</editor-fold>
            //<editor-fold desc="Constructor">


            //</editor-fold>

            //<editor-fold desc="pubic methods">


            //</editor-fold>
            //<editor-fold desc="Protected Methods">


            //</editor-fold>
        }

        static final String PATH_TEXT_FILTER = PATH_EMPTY_FILTER + ITEM_TEXT_SUFFIX;

        public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, PATH);
        public static final Uri CONTENT_FILTER_URI = Uri.withAppendedPath(CONTENT_URI, ITEM_FILTER_PATH_SUFFIX);
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd." + AUTHORITY + "." + PATH;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd." + AUTHORITY + "." + PATH;

        //</editor-fold">
        //<editor-fold desc="Constructor">


        //</editor-fold >

        //<editor-fold desc="pubic methods">


        //</editor-fold>
        //<editor-fold desc="Protected Methods">



        //</editor-fold>

        //<editor-fold desc="Inner Classes">



        //</editor-fold>

    }

    public static final class Events implements EventsColumns {

        //<editor-fold desc="Constants">

        static final String PATH = TABLE_NAME;

        /**
         * Private constructor used in order to prevent instantiation of this class.
         */
        private Events() {
        }

        protected static final String PATH_ID = PATH + ITEM_NUMERIC_SUFFIX;

        public static Uri getEventDataUri(final long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        static final String PATH_EMPTY_FILTER = PATH + "/" + ITEM_FILTER_PATH_SUFFIX;

        static SQLiteQueryBuilder getQueryBuilder(final Uri uri, final int code) {
            final SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
            builder.setTables(Events.TABLE_NAME);

            switch (code) {
                case ContentProvider.EVENTS:
                case ContentProvider.EVENT_ID:
                    if (!Events.TABLE_NAME.equals(uri.getLastPathSegment()))
                        builder.appendWhere(String.format("%s = %s", EventsColumns._ID, uri.getLastPathSegment()));
                    break;
                case ContentProvider.EVENT_FILTER:
                    if (!ITEM_FILTER_PATH_SUFFIX.equals(uri.getLastPathSegment()))
                        builder.appendWhere(String.format("%s like '%%%s%%'", EventsColumns.NAME, uri.getLastPathSegment()));
                    break;
                default:
                    throw new IllegalArgumentException("Cannot generate query statement for uri (" + uri + "). Unsupported uri code: " + code);
            }


            return builder;
        }

        protected static final String PATH_NUMERIC_FILTER = PATH_EMPTY_FILTER + ITEM_NUMERIC_SUFFIX;
        protected static final String PATH_TEXT_FILTER = PATH_EMPTY_FILTER + ITEM_TEXT_SUFFIX;

        //protected static final String PATH_FILTER = PATH_EMPTY_FILTER + ITEM_TEXT_SUFFIX;

        public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, PATH);
        public static final Uri CONTENT_FILTER_URI = Uri.withAppendedPath(CONTENT_URI, ITEM_FILTER_PATH_SUFFIX);
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd." + AUTHORITY + "." + PATH;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd." + AUTHORITY + "." + PATH;

        //</editor-fold>
        //<editor-fold desc="Constructor">


        //</editor-fold>

        //<editor-fold desc="pubic methods">


        //</editor-fold>
        //<editor-fold desc="protected methods">



        //</editor-fold>

    }

    public static final class MediaContent implements MediaContentColumns {

        //<editor-fold desc="Constants">

        public static final String PATH = TABLE_NAME;

        /**
         * Private constructor used in order to prevent instantiation of this class.
         */
        private MediaContent() {
        }

        public static final String PATH_ID = PATH + ITEM_NUMERIC_SUFFIX;

        static SQLiteQueryBuilder getQueryBuilder(final Uri uri, final int code) {
            final SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
            builder.setTables(MediaContent.TABLE_NAME);

            switch (code) {
                case ContentProvider.MEDIA_CONTENT:
                case ContentProvider.MEDIA_CONTENT_ID:
                    if (!MediaContent.TABLE_NAME.equals(uri.getLastPathSegment()))
                        builder.appendWhere(String.format("%s = %s", MediaContent._ID, uri.getLastPathSegment()));
                    break;
                default:
                    throw new IllegalArgumentException("Cannot generate query statement for uri (" + uri + "). Unsupported uri code: " + code);
            }
            return builder;
        }

        public static final String PATH_EMPTY_FILTER = PATH + "/" + ITEM_FILTER_PATH_SUFFIX;
        public static final String PATH_NUMERIC_FILTER = PATH_EMPTY_FILTER + ITEM_NUMERIC_SUFFIX;
        public static final String PATH_TEXT_FILTER = PATH_EMPTY_FILTER + ITEM_TEXT_SUFFIX;

        public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, PATH);
        public static final Uri CONTENT_FILTER_URI = Uri.withAppendedPath(CONTENT_URI, ITEM_FILTER_PATH_SUFFIX);
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd." + AUTHORITY + "." + PATH;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd." + AUTHORITY + "." + PATH;

        //</editor-fold">
        //<editor-fold desc="Constructor">


        //</editor-fold >

    }

    //</editor-fold>
}

