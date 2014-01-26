package com.fsteller.mobile.android.teammatesapp.utils;

import android.content.Context;
import android.database.Cursor;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AlphabetIndexer;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.fsteller.mobile.android.teammatesapp.R;
import com.fsteller.mobile.android.teammatesapp.TC;
import com.fsteller.mobile.android.teammatesapp.utils.Image.ImageLoader;

import java.util.Locale;

/**
 * Created by fhernandezs on 26/12/13 for iTeammates.
 */
public final class Adapters {

    private final static int TXT_LENGTH = 35;

    private static boolean isNullOrEmpty(final CharSequence text) {
        return text == null || String.valueOf(text).trim().isEmpty();
    }

    public static abstract class CursorAdapter extends SimpleCursorAdapter implements SimpleCursorAdapter.ViewBinder, SectionIndexer {

        protected static LayoutInflater mInflater; // Stores the layout inflater
        protected static AlphabetIndexer mAlphabetIndexer; // Stores the AlphabetIndexer instance
        protected static TextAppearanceSpan highlightTextSpan; // Stores the highlight text appearance style

        public CursorAdapter(final Context context, final int sortColumnIndex, final int layout, final String[] from, final int[] to) {
            super(context, layout, null, from, to, 0);

            final String aux = context.getString(R.string.alphabet);

            mInflater = LayoutInflater.from(context);
            mAlphabetIndexer = new AlphabetIndexer(null, sortColumnIndex, aux);
            highlightTextSpan = new TextAppearanceSpan(context, R.style.searchTextHighlight);

            setViewBinder(this);
        }

        @Override
        public View newView(final Context context, final Cursor cursor, final ViewGroup parent) {
            return setupView(super.newView(context, cursor, parent));
        }

        @Override
        public boolean setViewValue(final View view, final Cursor cursor, final int columnIndex) {
            return true;
        }


        /**
         * Overrides swapCursor to move the new Cursor into the AlphabetIndex as well as the
         * CursorAdapter.
         */
        @Override
        public Cursor swapCursor(final Cursor newCursor) {
            // Update the AlphabetIndexer with new cursor as well
            mAlphabetIndexer.setCursor(newCursor);
            return super.swapCursor(newCursor);
        }

        /**
         * An override of getCount that simplifies accessing the Cursor. If the Cursor is null,
         * getCount returns zero. As a result, no test for Cursor == null is needed.
         */
        @Override
        public int getCount() {
            if (getCursor() == null) {
                return 0;
            }
            return super.getCount();
        }

        /**
         * Defines the SectionIndexer.getSections() interface.
         */
        @Override
        public Object[] getSections() {
            return mAlphabetIndexer.getSections();
        }

        /**
         * Defines the SectionIndexer.getPositionForSection() interface.
         */
        @Override
        public int getPositionForSection(final int i) {
            if (getCursor() == null) {
                return 0;
            }
            return mAlphabetIndexer.getPositionForSection(i);
        }

        /**
         * Defines the SectionIndexer.getSectionForPosition() interface.
         */
        @Override
        public int getSectionForPosition(final int i) {
            if (getCursor() == null) {
                return 0;
            }
            return mAlphabetIndexer.getSectionForPosition(i);
        }

        /**
         * Identifies the start of the search string in the display name column of a Cursor row.
         * E.g. If displayName was "Adam" and search query (mSearchTerm) was "da" this would
         * return 1.
         *
         * @param displayName The contact display name.
         * @return The starting position of the search string in the display name, 0-based. The
         * method returns -1 if the string is not found in the display name, or if the search
         * string is empty or null.
         */
        protected static int indexOfSearchQuery(final String displayName, final String mSearchTerm) {
            if (!isNullOrEmpty(mSearchTerm)) {
                return displayName.toLowerCase(Locale.getDefault()).indexOf(
                        mSearchTerm.toLowerCase(Locale.getDefault()));
            }
            return -1;
        }

        protected static void setImageView(final ImageView view, final ImageLoader mListImageLoader, final String imageData) {
            if (mListImageLoader != null && !isNullOrEmpty(imageData))
                mListImageLoader.loadImage(imageData, view);
            else
                view.setImageResource(R.drawable.ic_default_picture);
        }

        protected static void setHighlightedText(final TextView view, final String txt, final String mSearchTerm) {
            final int startIndex = indexOfSearchQuery(txt, mSearchTerm);
                    /*
                        If the user didn't do a search, or the search string didn't match a display
                        name, show the display name without highlighting
                    */
            if (startIndex == -1)
                view.setText(txt);
            else {
                /*
                    If the search string matched the display name, applies a SpannableString to
                    highlight the search string with the displayed display name
                */
                final SpannableString highlightedName = new SpannableString(txt);
                highlightedName.setSpan(highlightTextSpan, startIndex, startIndex + mSearchTerm.length(), 0);
                view.setText(highlightedName);
            }
        }

        protected void setDateText(final TextView view, final String prefix, final long createdAt) {
            final String dateCreated = DateTime.getDate(createdAt, DateTime.DateFormat);
            view.setText(String.format(prefix, dateCreated));
        }

        protected static void setBasicText(final TextView view, final String txt) {
            String data = "";
            if (!isNullOrEmpty(txt)) {
                final int i =
                        txt.contains("\n") ?
                                txt.indexOf('\n') < TXT_LENGTH ?
                                        txt.indexOf('\n') : TXT_LENGTH < txt.length() ?
                                        TXT_LENGTH : txt.length() : TXT_LENGTH < txt.length() ?
                                TXT_LENGTH : txt.length();
                data = txt.substring(0, i);
            }
            view.setText(data);

        }

        protected abstract View setupView(final View view);

    }

    public static final class CalendarAdapter extends Adapters.CursorAdapter {

        public CalendarAdapter(final Context context) {
            super(context,
                    TC.Queries.CalendarQuery.SORT_KEY,
                    R.layout.spinner_item,
                    TC.Queries.CalendarQuery.PROJECTION,
                    new int[]{
                            R.id.spinner_item_title,
                            R.id.spinner_item_description,
                            R.id.spinner_item_color
                    }
            );
        }

        @Override
        protected View setupView(final View view) {
            return view;
        }

        @Override
        public boolean setViewValue(final View view, final Cursor cursor, final int id) {
            switch (view.getId()) {
                case R.id.spinner_item_title:
                    ((TextView) view).setText(cursor.getString(TC.Queries.CalendarQuery.DISPLAY_NAME));
                    break;
                case R.id.spinner_item_description:
                    ((TextView) view).setText(cursor.getString(TC.Queries.CalendarQuery.ACCOUNT_NAME));
                    break;
                case R.id.spinner_item_color:
                    view.setBackgroundColor(cursor.getInt(TC.Queries.CalendarQuery.CALENDAR_COLOR));
                    break;
            }
            return true;
        }

    }


}
