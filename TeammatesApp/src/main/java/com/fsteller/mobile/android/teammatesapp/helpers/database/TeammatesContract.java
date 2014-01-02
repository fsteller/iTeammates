package com.fsteller.mobile.android.teammatesapp.helpers.database;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;

import java.util.Arrays;
import java.util.List;

/**
 * Created by fhernandezs on 26/12/13 for iTeammates.
 */
public final class TeammatesContract {

    //<editor-fold desc="Main Variables">

    public static final String SCHEME = "content://";
    public static final String ITEM_TEXT_SUFFIX = "/*";
    public static final String ITEM_NUMERIC_SUFFIX = "/#";
    public static final String ITEM_FILTER_PATH_SUFFIX = "filter";
    public static final String AUTHORITY = "com.fsteller.community.teammates.provider";
    public static final Uri AUTHORITY_URI = Uri.parse(SCHEME + AUTHORITY);

    //</editor-fold >
    //<editor-fold desc="Constructor">

    private TeammatesContract() {
    }

    //</editor-fold>

    //<editor-fold desc="Interfaces">

    private static interface TeamsColumns extends BaseColumns {
        public static final String NAME = Constants.Teams.Fields[1];
        public static final String IMAGE_REF = Constants.Teams.Fields[2];
        public static final String UPDATED_AT = Constants.Teams.Fields[3];
        public static final String CREATED_AT = Constants.Teams.Fields[4];
        public static final String DEFAULT_SORT_ORDER = NAME + " ASC";
        public static final String TABLE_NAME = Constants.Teams.TableName;
        public static final List<String> COLUMNS = Arrays.asList((Constants.Teams.Fields));
    }

    private static interface ContactsColumns extends BaseColumns {
        public static final String TEAM_ID = Constants.Contacts.Fields[1];
        public static final String TOKEN = Constants.Contacts.Fields[2];
        public static final String DEFAULT_SORT_ORDER = TOKEN + " DESC";
        public static final String TABLE_NAME = Constants.Contacts.TableName;
        public static final List<String> COLUMNS = Arrays.asList((Constants.Contacts.Fields));
    }

    private static interface EventsColumns extends BaseColumns {
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

    //</editor-fold>
    //<editor-fold desc="Inner Classes">

    public final static class Teams implements TeamsColumns {

        //<editor-fold desc="Constants">
        protected static final String PATH = TABLE_NAME;
        protected static final String PATH_ID = PATH + ITEM_NUMERIC_SUFFIX;
        protected static final String PATH_EMPTY_FILTER = PATH + "/" + ITEM_FILTER_PATH_SUFFIX;
        protected static final String PATH_NUMERIC_FILTER = PATH_EMPTY_FILTER + ITEM_NUMERIC_SUFFIX;
        protected static final String PATH_TEXT_FILTER = PATH_EMPTY_FILTER + ITEM_TEXT_SUFFIX;

        public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, PATH);
        public static final Uri CONTENT_FILTER_URI = Uri.withAppendedPath(CONTENT_URI, ITEM_FILTER_PATH_SUFFIX);
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd." + AUTHORITY + "." + PATH;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd." + AUTHORITY + "." + PATH;

        //</editor-fold">
        //<editor-fold desc="Constructor">
        private Teams() {
        }
        //</editor-fold >

        //<editor-fold desc="pubic methods">
        public static Uri getTeamDataUri(final long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        //</editor-fold>
        //<editor-fold desc="Protected Methods">
        protected static SQLiteQueryBuilder getQueryBuilder(final Uri uri, final int code) {
            final SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
            builder.setTables(Teams.TABLE_NAME);

            switch (code) {
                case TeammatesProvider.TEAMS:
                case TeammatesProvider.TEAM_ID:
                    if (!Teams.TABLE_NAME.equals(uri.getLastPathSegment()))
                        builder.appendWhere(String.format("%s = %s", TeamsColumns._ID, uri.getLastPathSegment()));
                    break;
                case TeammatesProvider.TEAM_FILTER:
                    if (!ITEM_FILTER_PATH_SUFFIX.equals(uri.getLastPathSegment()))
                        builder.appendWhere(String.format("%s like '%%%s%%'", TeamsColumns.NAME, uri.getLastPathSegment()));
                    break;
                default:
                    throw new IllegalArgumentException("Cannot generate query statement for uri (" + uri + "). Unsupported uri code: " + code);
            }


            return builder;
        }

        protected static String[] getProjection(final String[] projection) {
            int i = 0;
            if (projection != null)
                for (final String columnName : projection)
                    projection[i++] = fixColumnName(columnName);
            return projection;
        }

        //</editor-fold>
        //<editor-fold desc="Private Methods">
        private static String fixColumnName(String columnName) {
            return COLUMNS.contains(columnName) ?
                    String.format("%s.%s", TABLE_NAME, columnName) :
                    columnName;
        }
        //</editor-fold>

        //<editor-fold desc="Inner Classes">

        public final static class Contacts implements ContactsColumns {

            //<editor-fold desc="Constants">
            protected static final String PATH = Teams.PATH + "/#/" + TABLE_NAME;
            protected static final String ID_PATH = PATH + ITEM_NUMERIC_SUFFIX;
            public static final String CONTENT_DIRECTORY = TABLE_NAME;
            public static final String CONTENT_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd." + AUTHORITY + "." + PATH;
            public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd." + AUTHORITY + "." + PATH;

            //</editor-fold>
            //<editor-fold desc="Constructor">
            private Contacts() {
            }
            //</editor-fold>

            //<editor-fold desc="pubic methods">
            public static Uri getTeamContactUri(int id) {
                final Uri result = getTeamDataUri(id);
                return Uri.withAppendedPath(result, CONTENT_DIRECTORY);
            }

            //</editor-fold>
            //<editor-fold desc="Protected Methods">
            protected static SQLiteQueryBuilder getQueryBuilder(final Uri uri, final int code) {
                final List<String> segments = uri.getPathSegments();
                if (segments != null && code == TeammatesProvider.TEAM_CONTACTS) {
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

            protected static String[] getProjection(final String[] projection) {
                int i = 0;
                if (projection != null)
                    for (final String columnName : projection)
                        projection[i++] = fixColumnName(columnName);
                return projection;
            }

            //</editor-fold>
            //<editor-fold desc="Private Methods">
            private static String fixColumnName(final String columnName) {
                return COLUMNS.contains(columnName) ?
                        String.format("%s.%s", TABLE_NAME, columnName) :
                        Teams.fixColumnName(columnName);
            }
            //</editor-fold>
        }

        //</editor-fold>

    }

    public static final class Events implements EventsColumns {

        //<editor-fold desc="Constants">
        protected static final String PATH = TABLE_NAME;
        protected static final String PATH_ID = PATH + ITEM_NUMERIC_SUFFIX;
        protected static final String PATH_EMPTY_FILTER = PATH + "/" + ITEM_FILTER_PATH_SUFFIX;
        protected static final String PATH_NUMERIC_FILTER = PATH_EMPTY_FILTER + ITEM_NUMERIC_SUFFIX;
        protected static final String PATH_TEXT_FILTER = PATH_EMPTY_FILTER + ITEM_TEXT_SUFFIX;

        //protected static final String PATH_FILTER = PATH_EMPTY_FILTER + ITEM_TEXT_SUFFIX;


        public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, PATH);
        public static final Uri CONTENT_FILTER_URI = Uri.withAppendedPath(CONTENT_URI, ITEM_FILTER_PATH_SUFFIX);
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd." + AUTHORITY + "." + PATH;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd." + AUTHORITY + "." + PATH;

        //</editor-fold>
        //<editor-fold desc="Constructor">
        private Events() {
        }
        //</editor-fold>

        //<editor-fold desc="pubic methods">
        public static Uri getEventDataUri(final long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        //</editor-fold>
        //<editor-fold desc="protected methods">
        protected static SQLiteQueryBuilder getQueryBuilder(final Uri uri, final int code) {
            final SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
            builder.setTables(Events.TABLE_NAME);

            switch (code) {
                case TeammatesProvider.EVENTS:
                case TeammatesProvider.EVENT_ID:
                    if (!Events.TABLE_NAME.equals(uri.getLastPathSegment()))
                        builder.appendWhere(String.format("%s = %s", EventsColumns._ID, uri.getLastPathSegment()));
                    break;
                case TeammatesProvider.EVENT_FILTER:
                    if (!ITEM_FILTER_PATH_SUFFIX.equals(uri.getLastPathSegment()))
                        builder.appendWhere(String.format("%s like '%%%s%%'", EventsColumns.NAME, uri.getLastPathSegment()));
                    break;
                default:
                    throw new IllegalArgumentException("Cannot generate query statement for uri (" + uri + "). Unsupported uri code: " + code);
            }


            return builder;
        }

        protected static String[] getProjection(final String[] projection) {
            int i = 0;
            if (projection != null)
                for (final String columnName : projection)
                    projection[i++] = fixColumnName(columnName);
            return projection;
        }

        //</editor-fold>
        //<editor-fold desc="private methods">
        private static String fixColumnName(final String columnName) {
            return COLUMNS.contains(columnName) ?
                    String.format("%s.%s", TABLE_NAME, columnName) :
                    Teams.fixColumnName(columnName);
        }
        //</editor-fold>
    }

    //</editor-fold>
}

