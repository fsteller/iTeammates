package com.fsteller.mobile.android.teammatesapp.activities.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.fsteller.mobile.android.teammatesapp.R;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * <p/>
 * Project: iTeammates
 * Package: activities.dialogs
 * <p/>
 * Description: based on ZonePicker class from the Android Open Source Project write on 2006.
 * <p/>
 * Created by fsteller on 4/15/14.
 */
public final class DialogFragment_ZonePicker extends DialogFragment implements AdapterView.OnItemClickListener {

    //<editor-fold desc="Constants">

    private static final String TAG = DialogFragment_ZonePicker.class.getSimpleName();

    private static final String KEY_ID = "id";  // value: String
    private static final String KEY_DISPLAYNAME = "name";  // value: String
    private static final String KEY_GMT = "gmt";  // value: String
    private static final String KEY_OFFSET = "offset";  // value: int (Integer)
    private static final String XMLTAG_TIMEZONE = "timezone";

    private static final int HOURS_1 = 60 * 60000;

    private static final int MENU_TIMEZONE = Menu.FIRST + 1;
    private static final int MENU_ALPHABETICAL = Menu.FIRST;

    private boolean mSortedByTimezone;

    //</editor-fold>
    //<editor-fold desc="Variables">

    private String defaultId;
    private ListView zonePickerList;
    private SimpleAdapter mTimezoneSortedAdapter;
    private SimpleAdapter mAlphabeticalAdapter;
    private ZoneSelectionListener mListener;

    //</editor-fold>
    //<editor-fold desc="Constructor">

    public DialogFragment_ZonePicker(final String timeZoneId, final ZoneSelectionListener listener) {
        super();
        defaultId = timeZoneId;
        setZoneSelectionListener(listener);
    }

    //</editor-fold >

    //<editor-fold desc="Overrides">

    /**
     * Constructs an adapter with TimeZone list. Sorted by TimeZone in default.
     *
     * @param sortedByName use Name for sorting the list.
     */
    private static SimpleAdapter constructTimezoneAdapter(final Context context, final boolean sortedByName) {
        return constructTimezoneAdapter(context, sortedByName,
                R.layout.listview_item_timezone);
    }

    /**
     * Constructs an adapter with TimeZone list. Sorted by TimeZone in default.
     *
     * @param sortedByName use Name for sorting the list.
     */
    private static SimpleAdapter constructTimezoneAdapter(final Context context, final boolean sortedByName, final int layoutId) {
        final String[] from = new String[]{KEY_DISPLAYNAME, KEY_GMT};
        final int[] to = new int[]{android.R.id.text1, android.R.id.text2};

        final String sortKey = (sortedByName ? KEY_DISPLAYNAME : KEY_OFFSET);
        final MyComparator comparator = new MyComparator(sortKey);
        final List<HashMap<String, Object>> sortedList = getZones(context);

        Collections.sort(sortedList, comparator);
        return new SimpleAdapter(context,
                sortedList,
                layoutId,
                from,
                to);
    }

    /**
     * Searches {@link TimeZone} from the given {@link SimpleAdapter} object, and returns
     * the index for the TimeZone.
     *
     * @param adapter SimpleAdapter constructed by
     *                {@link #constructTimezoneAdapter(Context, boolean)}.
     * @param tz      TimeZone to be searched.
     * @return Index for the given TimeZone. -1 when there's no corresponding list item.
     * returned.
     */
    private static int getTimeZoneIndex(final SimpleAdapter adapter, final TimeZone tz) {
        final String defaultId = tz.getID();
        final int listSize = adapter.getCount();
        for (int i = 0; i < listSize; i++) {
            final HashMap<?, ?> map = (HashMap<?, ?>) adapter.getItem(i);
            final String id = (String) map.get(KEY_ID);
            if (defaultId.equals(id))
                return i;
        }
        return -1;
    }

    private static List<HashMap<String, Object>> getZones(final Context context) {
        final List<HashMap<String, Object>> myData = new ArrayList<HashMap<String, Object>>();
        final long date = Calendar.getInstance().getTimeInMillis();
        try {
            final XmlResourceParser xrp = context.getResources().getXml(R.xml.timezones);
            while (xrp.next() != XmlResourceParser.START_TAG)
                continue;
            xrp.next();
            while (xrp.getEventType() != XmlResourceParser.END_TAG) {
                while (xrp.getEventType() != XmlResourceParser.START_TAG) {
                    if (xrp.getEventType() == XmlResourceParser.END_DOCUMENT) {
                        return myData;
                    }
                    xrp.next();
                }
                if (xrp.getName().equals(XMLTAG_TIMEZONE)) {
                    final String id = xrp.getAttributeValue(0);
                    final String displayName = xrp.nextText();
                    addItem(myData, id, displayName, date);
                }
                while (xrp.getEventType() != XmlResourceParser.END_TAG) {
                    xrp.next();
                }
                xrp.next();
            }
            xrp.close();
        } catch (final XmlPullParserException xppe) {
            Log.e(TAG, "Ill-formatted timezones.xml file", xppe);
        } catch (final IOException ioe) {
            Log.e(TAG, "Unable to read timezones.xml file", ioe);
        }

        return myData;
    }

    private static void addItem(final List<HashMap<String, Object>> myData, final String id, final String displayName, final long date) {
        final HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(KEY_ID, id);
        map.put(KEY_DISPLAYNAME, displayName);
        final TimeZone tz = TimeZone.getTimeZone(id);
        final int offset = tz.getOffset(date);
        final int p = Math.abs(offset);
        final StringBuilder name = new StringBuilder();
        name.append("GMT");

        if (offset < 0) {
            name.append('-');
        } else {
            name.append('+');
        }

        name.append(p / (HOURS_1));
        name.append(':');

        int min = p / 60000;
        min %= 60;

        if (min < 10) {
            name.append('0');
        }
        name.append(min);

        map.put(KEY_GMT, name.toString());
        map.put(KEY_OFFSET, offset);

        myData.add(map);
    }

    //</editor-fold>
    //<editor-fold desc="Private">

    /**
     * @param item one of items in adapters. The adapter should be constructed by
     *             {@link #constructTimezoneAdapter(Context, boolean)}.
     * @return TimeZone object corresponding to the item.
     */
    public static TimeZone obtainTimeZoneFromItem(final Object item) {
        return TimeZone.getTimeZone((String) ((Map<?, ?>) item).get(KEY_ID));
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {

        // Use the Builder class for convenient dialog construction
        final Activity activity = getActivity();
        if (activity == null)
            return null;

        final LayoutInflater inflater = activity.getLayoutInflater();
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        final View rootView = inflater.inflate(R.layout.dialog_zone_picker, null);

        Dialog result = null;
        if (rootView != null) {
            zonePickerList = (ListView) rootView.findViewById(R.id.listView_timeZones);
            mTimezoneSortedAdapter = constructTimezoneAdapter(activity, false);
            mAlphabeticalAdapter = constructTimezoneAdapter(activity, true);
            zonePickerList.setOnItemClickListener(this);

            // Sets the adapter
            setSorting(true);
            setHasOptionsMenu(true);

            // Create the AlertDialog object and return it
            builder.setView(rootView);
            result = builder.create();
            setStyle(STYLE_NORMAL, android.R.style.Theme_Dialog);
            result.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        return result;
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
        menu.add(0, MENU_ALPHABETICAL, 0, R.string.zone_list_menu_sort_alphabetically)
                .setIcon(android.R.drawable.ic_menu_sort_alphabetically);
        menu.add(0, MENU_TIMEZONE, 0, R.string.zone_list_menu_sort_by_timezone)
                .setIcon(android.R.drawable.ic_menu_sort_by_size);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(final Menu menu) {
        if (mSortedByTimezone) {
            menu.findItem(MENU_TIMEZONE).setVisible(false);
            menu.findItem(MENU_ALPHABETICAL).setVisible(true);
        } else {
            menu.findItem(MENU_TIMEZONE).setVisible(true);
            menu.findItem(MENU_ALPHABETICAL).setVisible(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {

            case MENU_TIMEZONE:
                setSorting(true);
                return true;

            case MENU_ALPHABETICAL:
                setSorting(false);
                return true;

            default:
                return false;
        }
    }

    @Override
    public void onItemClick(final AdapterView<?> listView, final View view, final int position, final long id) {
        final Map<?, ?> map = (Map<?, ?>) listView.getItemAtPosition(position);
        final String tzId = (String) map.get(KEY_ID);

        if (mListener != null)
            mListener.onZoneSelected(TimeZone.getTimeZone(tzId));
        this.dismiss();
    }

    //</editor-fold>
    //<editor-fold desc="Public">

    private void setSorting(final boolean sortByTimezone) {
        final SimpleAdapter adapter = sortByTimezone ? mTimezoneSortedAdapter : mAlphabeticalAdapter;
        final int defaultIndex = getTimeZoneIndex(adapter, TimeZone.getTimeZone(defaultId));

        zonePickerList.setAdapter(adapter);
        mSortedByTimezone = sortByTimezone;
        if (defaultIndex >= 0) {
            zonePickerList.setSelection(defaultIndex);
        }
    }

    public void setZoneSelectionListener(ZoneSelectionListener listener) {
        mListener = listener;
    }

    //</editor-fold>

    //<editor-fold desc="Interfaces">

    public static interface ZoneSelectionListener {
        // You can add any argument if you really need it...
        public void onZoneSelected(TimeZone tz);
    }

    //</editor-fold>
    //<editor-fold desc="InnerClasses">

    private static class MyComparator implements Comparator<HashMap<?, ?>> {
        private String mSortingKey;

        public MyComparator(final String sortingKey) {
            mSortingKey = sortingKey;
        }

        public int compare(final HashMap<?, ?> map1, final HashMap<?, ?> map2) {
            final Object value1 = map1.get(mSortingKey);
            final Object value2 = map2.get(mSortingKey);

            /*
             * This should never happen, but just in-case, put non-comparable
             * items at the end.
             */
            if (!isComparable(value1)) {
                return isComparable(value2) ? 1 : 0;
            } else if (!isComparable(value2)) {
                return -1;
            }

            return ((Comparable) value1).compareTo(value2);
        }

        private boolean isComparable(final Object value) {
            return (value != null) && (value instanceof Comparable);
        }
    }

    //</editor-fold>
}
