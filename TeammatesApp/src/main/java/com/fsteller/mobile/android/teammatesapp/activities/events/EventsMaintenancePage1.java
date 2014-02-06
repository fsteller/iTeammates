package com.fsteller.mobile.android.teammatesapp.activities.events;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.fsteller.mobile.android.teammatesapp.R;
import com.fsteller.mobile.android.teammatesapp.TC;
import com.fsteller.mobile.android.teammatesapp.activities.base.FragmentMaintenancePageBase;
import com.fsteller.mobile.android.teammatesapp.activities.base.IPageManager;
import com.fsteller.mobile.android.teammatesapp.activities.base.MaintenancePage;
import com.fsteller.mobile.android.teammatesapp.helpers.database.TeammatesContract;
import com.fsteller.mobile.android.teammatesapp.utils.Adapters;
import com.fsteller.mobile.android.teammatesapp.utils.Text;

import java.util.List;

/**
 * Created by fhernandezs on 23/01/14.
 */
public class EventsMaintenancePage1 extends FragmentMaintenancePageBase implements MaintenancePage, AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener, AbsListView.OnScrollListener, LoaderManager.LoaderCallbacks<Cursor> {

    //<editor-fold desc="Constants">

    public static final String TAG = EventsMaintenancePage1.class.getSimpleName();
    public static final int PAGE_INDEX = IPageManager.EVENTS_PAGE + 1;

    private static final int CALENDARS = 0xF0001;
    private static final int CONTACTS = 0xF0002;
    private static final int TEAMS = 0xF0003;

    //</editor-fold>
    //<editor-fold desc="Variables">

    private View emptyView = null;
    private ImageView titleImage = null;
    private Spinner headerSpinner = null;

    private EventsParticipantsAdapter teamsAdapter = null;
    private Adapters.CalendarAdapter calendarAdapter = null;

    //</editor-fold>

    //<editor-fold desc="Overridden">
    //<editor-fold desc="Fragment">

    @Override
    public int getPageIndex() {
        return PAGE_INDEX;
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final Activity mActivity = getActivity();

        if (mActivity != null) {
            teamsAdapter = new EventsParticipantsAdapter(mActivity);
            calendarAdapter = new Adapters.CalendarAdapter(mActivity);
            imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            mImageLoader.loadImage(mCallback.getImageRef(), titleImage);
            headerSpinner.setAdapter(calendarAdapter);
            headerSpinner.setVisibility(View.VISIBLE);
            headerSpinner.setOnItemSelectedListener(this);
            mListView.setOnScrollListener(this);
            mListView.setAdapter(teamsAdapter);
        }

        restartLoader(TC.Queries.PhoneCalendar.SIMPLE_QUERY_ID);
        Log.d(TAG, "onActivityCreated");
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {

        this.addCollection(TEAMS);
        this.addCollection(CONTACTS);
        this.addCollection(CALENDARS);
        final View rootView = inflater.inflate(R.layout.fragment_events_maintenance_page1, container, false);
        if (rootView != null) {

            final TextView titleView = (TextView) rootView.findViewById(R.id.title_label);
            final TextView descriptionView = (TextView) rootView.findViewById(R.id.title_description_label);
            final TextView controlView = (TextView) rootView.findViewById(R.id.header_control_label);
            final EditText teamNameText = (EditText) rootView.findViewById(R.id.collection_title_text);
            final TextView teamNameView = (TextView) rootView.findViewById(R.id.collection_title_label);
            final EditText teamDescriptionText = (EditText) rootView.findViewById(R.id.collection_description_text);
            final TextView teamDescriptionView = (TextView) rootView.findViewById(R.id.collection_description_label);
            final TextView lookupKeyTitleText = (TextView) rootView.findViewById(R.id.collection_lookup_key_text);
            final TextView lookupKeyTitleView = (TextView) rootView.findViewById(R.id.collection_lookup_key_label);

            emptyView = rootView.findViewById(android.R.id.empty);
            mListView = (AbsListView) rootView.findViewById(R.id.list_view);
            titleImage = (ImageView) rootView.findViewById(R.id.header_image);
            headerSpinner = (Spinner) rootView.findViewById(R.id.header_spinner);

            titleView.setText(getResources().getString(R.string.eventsMaintenance_titleLabel));
            controlView.setText(getResources().getString(R.string.eventsMaintenance_titleControlLabel));
            descriptionView.setText(getResources().getString(R.string.eventsMaintenance_titleDescriptionLabel));
            teamDescriptionView.setText(getResources().getString(R.string.eventsMaintenance_DescriptionLabel));
            teamNameView.setText(getResources().getString(R.string.eventsMaintenance_lookupTitle1_label));
            lookupKeyTitleView.setText(getResources().getString(R.string.eventsMaintenance_lookupLabel));

            teamNameText.setHint(getResources().getString(R.string.eventsMaintenance_lookupTitle1_hint));
            lookupKeyTitleText.setHint(getResources().getString(R.string.eventsMaintenance_lookupHint));
            teamDescriptionText.setHint(getResources().getString(R.string.eventsMaintenance_descriptionHint));

            teamNameText.setSelectAllOnFocus(true);
            teamNameText.setText(mCallback.getEntityName());
            teamNameText.addTextChangedListener(new Text.AfterTextChangedWatcher() {
                @Override
                public void afterTextChanged(final Editable s) {
                    mCallback.setEntityName(s.toString());
                }
            });

            lookupKeyTitleText.addTextChangedListener(new Text.AfterTextChangedWatcher() {
                @Override
                public void afterTextChanged(final Editable s) {
                    setSearchTerm(s.toString().trim());
                    restartLoader(getSearchTerm().isEmpty() ?
                            TC.Queries.PhoneContacts.SIMPLE_QUERY_ID :
                            TC.Queries.PhoneContacts.FILTER_QUERY_ID1
                    );
                }
            });

            final ImageButton button = (ImageButton) rootView.findViewById(R.id.header_button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Raising intent to pick image up...");
                    final Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    startActivityForResult(Intent.createChooser
                            (intent, getString(R.string.selectPicture)), TC.Activity.ContextActionRequest.PickImage);
                }
            });
        }
        Log.d(TAG, "onCreated");
        return rootView;
    }

    //</editor-fold>
    //<editor-fold desc="AbsListView.OnScrollListener">

    @Override
    public void onScrollStateChanged(final AbsListView view, final int scrollState) {
        // Pause image loader to ensure smoother scrolling when flinging
        if (mImageLoader != null) {
            if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING)
                mImageLoader.setPauseWork(true);
            else
                mImageLoader.setPauseWork(false);
        }
        hideSoftKeyboard(view);
    }

    @Override
    public void onScroll(final AbsListView view, final int firstVisibleItem, final int visibleItemCount, final int totalItemCount) {

    }

    //</editor-fold>
    //<editor-fold desc="AdapterView.OnItemClickListener">

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    //</editor-fold>
    //<editor-fold desc="AdapterView.OnItemSelectedListener">

    @Override
    public void onItemSelected(final AdapterView<?> parent, final View view, final int position, final long id) {
    }

    @Override
    public void onNothingSelected(final AdapterView<?> parent) {
    }

    //</editor-fold>
    //<editor-fold desc="LoaderManager.LoaderCallbacks<Cursor>">

    @Override
    public Loader<Cursor> onCreateLoader(final int id, final Bundle args) {
        /*
            Creates and return a CursorLoader that will take care of
            creating a Cursor for the data being displayed.
        */
        switch (id) {
            case TC.Queries.PhoneCalendar.SIMPLE_QUERY_ID:
                return getCalendars("fsteller@gmail.com"); //TODO: replace fo a call to a real account id
            case TC.Queries.TeammatesTeams.FILTER_QUERY_ID1:
                return getTeamFilteredByTermSearch(getSearchTerm());
            default:
                Log.e(TAG, "onCreateLoader - incorrect ID provided (" + id + ")");
                return null;
        }
    }

    @Override
    public void onLoadFinished(final Loader<Cursor> loader, final Cursor data) {
        /*
            Swap the new cursor in.
            (The framework will take care of closing the old cursor once we return.)
        */
        switch (loader.getId()) {
            case TC.Queries.PhoneCalendar.SIMPLE_QUERY_ID:
                calendarAdapter.swapCursor(data);
                break;
            case TC.Queries.TeammatesTeams.FILTER_QUERY_ID1:
                emptyView.setVisibility(View.GONE);
                teamsAdapter.swapCursor(data);
                break;
        }
    }

    @Override
    public void onLoaderReset(final Loader<Cursor> loader) {
        /*
            When the loader is being reset, clear the cursor from the adapter.
            This allows the cursor resources to be freed.
        */
        switch (loader.getId()) {
            case TC.Queries.PhoneCalendar.SIMPLE_QUERY_ID:
                calendarAdapter.swapCursor(null);
                break;
            case TC.Queries.TeammatesTeams.FILTER_QUERY_ID1:
                teamsAdapter.swapCursor(null);
                break;
        }
    }

    //</editor-fold>
    //</editor-fold>
    //<editor-fold desc="Private Methods">

    private void restartLoader(final int queryFilter) {
        final LoaderManager mLoaderManager = getLoaderManager();
        if (mLoaderManager != null)
            mLoaderManager.restartLoader(queryFilter, null, this);
    }

    private Loader<Cursor> getTeamFilteredByTermSearch(final String searchTerm) {
        String selection = TC.Queries.TeammatesTeams.SELECTION;
        final String sortOrder = TC.Queries.TeammatesTeams.SORT_ORDER;
        final String[] projection = TC.Queries.TeammatesTeams.TEAMS_PROJECTION;
        final Uri contentUri = Uri.withAppendedPath(TC.Queries.TeammatesTeams.FILTER_URI, Uri.encode(searchTerm));
       /*
           Returns a new CursorLoader for querying the teams table. No arguments are used
           for the selection clause. The search string is either encoded onto the content URI,
           or no contacts search string is used. The other search criteria are constants. See
           the PhoneContacts interface.
        */
        return new CursorLoader(getActivity(), contentUri, projection, selection, null, sortOrder);
    }

    private Loader<Cursor> getTeamFilteredBySelectedTeams(final List<Integer> selectedTeams) {
        String selection = TC.Queries.TeammatesTeams.SELECTION;
        final Uri contentUri = TC.Queries.TeammatesTeams.CONTENT_URI;
        final String sortOrder = TC.Queries.TeammatesTeams.SORT_ORDER;
        final String[] projection = TC.Queries.TeammatesTeams.TEAMS_PROJECTION;

        if (selectedTeams.size() > 0) {
            selection += "AND (";
            final Object[] objects = selectedTeams.toArray();
            for (int i = 0; i < objects.length; i++) {
                selection += String.format("%s='%s'", TeammatesContract.Teams._ID, objects[i]);
                if (i < selectedTeams.size() - 1)
                    selection += " OR ";
            }
            selection += ")";
        } else selection = "1=0";
        /*
               Returns a new CursorLoader for querying the teams table. No arguments are used
               for the selection clause. The search string is either encoded onto the content URI,
               or no contacts search string is used. The other search criteria are constants. See
               the PhoneContacts interface.
        */
        return new CursorLoader(getActivity(), contentUri, projection, selection, null, sortOrder);
    }

    private Loader<Cursor> getCalendars(final String accountType) {
        final Uri contentUri = TC.Queries.PhoneCalendar.CONTENT_URI;
        final String selection = TC.Queries.PhoneCalendar.SELECTION;
        final String sortOrder = TC.Queries.PhoneCalendar.SORT_ORDER;
        final String[] projection = TC.Queries.PhoneCalendar.PROJECTION;
        final String[] selectionArgs = new String[]{accountType};
        return new CursorLoader(getActivity(), contentUri, projection, selection, selectionArgs, sortOrder);
    }

    //</editor-fold>

    //<editor-fold desc="Inner Class">

    private final class EventsParticipantsAdapter extends Adapters.CursorAdapter implements CompoundButton.OnCheckedChangeListener {

        public EventsParticipantsAdapter(final Context context) {
            super(context,
                    TC.Queries.PhoneContacts.SORT_KEY,
                    R.layout.listview_item_contact,
                    TC.Queries.PhoneContacts.PROJECTION,
                    new int[]{
                            R.id.contact_name,
                            R.id.contact_status,
                            R.id.contact_image,
                            R.id.contact_check
                    }
            );
        }

        @Override
        protected View setupView(final View view) {
            return view;
        }

        @Override
        public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {

        }
    }

    //</editor-fold>
}
