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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.fsteller.mobile.android.teammatesapp.R;
import com.fsteller.mobile.android.teammatesapp.TC;
import com.fsteller.mobile.android.teammatesapp.activities.base.FragmentMaintenancePageBase;
import com.fsteller.mobile.android.teammatesapp.model.EventEntity;
import com.fsteller.mobile.android.teammatesapp.utils.Adapters;
import com.fsteller.mobile.android.teammatesapp.utils.Text;

/**
 * Created by fhernandezs on 23/01/14.
 */
public class EventsMaintenancePage1 extends FragmentMaintenancePageBase implements AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener, AbsListView.OnScrollListener, LoaderManager.LoaderCallbacks<Cursor> {

    //<editor-fold desc="Constants">

    public static final String TAG = EventsMaintenancePage1.class.getSimpleName();

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
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final Activity mActivity = getActivity();

        if (mActivity != null) {
            teamsAdapter = new EventsParticipantsAdapter(mActivity);
            calendarAdapter = new Adapters.CalendarAdapter(mActivity);
            imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            mImageLoader.loadImage(mCallback.getImageRef(), titleImage);
            headerSpinner.setAdapter(calendarAdapter);
            headerSpinner.setOnItemSelectedListener(this);
            mListView.setOnScrollListener(this);
            mListView.setAdapter(teamsAdapter);
        }

        Log.d(TAG, "onActivityCreated");
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {

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
                    mCallback.setSearchTerm(s.toString().trim());
                    restartLoader(TC.Queries.TeammatesTeams.FILTER_QUERY_ID1, EventsMaintenancePage1.this);
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

    @Override
    public void onResume() {
        super.onResume();
        restartLoader(TC.Queries.PhoneCalendar.SIMPLE_QUERY_ID, this);
        restartLoader(TC.Queries.TeammatesTeams.FILTER_QUERY_ID1, this);
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
                return getParticipantsFilteredByTermSearch(mCallback.getSearchTerm());
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
                headerSpinner.setVisibility(View.VISIBLE);
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
                headerSpinner.setVisibility(View.GONE);
                calendarAdapter.swapCursor(null);
                break;
            case TC.Queries.TeammatesTeams.FILTER_QUERY_ID1:
                emptyView.setVisibility(View.VISIBLE);
                teamsAdapter.swapCursor(null);
                break;
        }
    }

    //</editor-fold>

    @Override
    protected int getFragmentDefaultImage() {
        return R.drawable.ic_default_picture;
    }

    //</editor-fold>
    //<editor-fold desc="Private Methods">


/*
    private Cursor getParticipantsFilteredByTermSearch(final String searchTerm) {

        Cursor result = null;
        final Activity mActivity = getActivity();
        final Uri teamsUri = Uri.withAppendedPath(TC.Queries.TeammatesTeams.FILTER_URI, Uri.encode(searchTerm));
        final Uri contactsUri = Uri.withAppendedPath(TC.Queries.PhoneContacts.FILTER_URI, Uri.encode(searchTerm));

        if (mActivity != null && teamsUri != null && contactsUri != null) {
            final String teamsSelection = TC.Queries.TeammatesTeams.SELECTION;
            final String teamsSortOrder = TC.Queries.TeammatesTeams.SORT_ORDER;
            final String[] teamsProjection = TC.Queries.TeammatesTeams.PROJECTION;


            final String contactsSelection = TC.Queries.PhoneContacts.SELECTION;
            final String contactsSortOrder = TC.Queries.PhoneContacts.SORT_ORDER;
            final String[] contactsProjection = TC.Queries.PhoneContacts.PROJECTION;

            final Cursor mTeams = mActivity.getContentResolver().
                    query(teamsUri, teamsProjection, teamsSelection, null, teamsSortOrder);

            final Cursor mContacts = mActivity.getContentResolver().
                    query(contactsUri, contactsProjection, contactsSelection, null, contactsSortOrder);

            result = new MergeCursor(new Cursor[]{mTeams, mContacts});
            //return new CursorLoader(getActivity(), teamsUri, teamsProjection, teamSelection, null, teamSortOrder);
        }
        return result;
    }
    */

    private Loader<Cursor> getParticipantsFilteredByTermSearch(final String searchTerm) {

        /*
           Returns a new CursorLoader for querying the teams table. No arguments are used
           for the selection clause. The search string is either encoded onto the content URI,
           or no contacts search string is used. The other search criteria are constants. See
           the PhoneContacts interface.
        */

        final Uri teamsUri = !isNullOrEmpty(searchTerm) ?
                Uri.withAppendedPath(TC.Queries.TeammatesTeams.FILTER_URI, Uri.encode(searchTerm)) :
                TC.Queries.TeammatesTeams.CONTENT_URI;

        final String teamsSelection = TC.Queries.TeammatesTeams.SELECTION;
        final String teamsSortOrder = TC.Queries.TeammatesTeams.SORT_ORDER;
        final String[] teamsProjection = TC.Queries.TeammatesTeams.PROJECTION;

        return new CursorLoader(getActivity(), teamsUri, teamsProjection, teamsSelection, null, teamsSortOrder);
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

        final String[] FROM = Text.mergeArrays(
                TC.Queries.TeammatesTeams.PROJECTION,
                TC.Queries.PhoneContacts.PROJECTION
        );

        final int[] TO = new int[]{
                R.id.listView_item_creation,
                R.id.listView_item_update,
                R.id.listView_item_title,
                R.id.listView_item_image,
                R.id.listView_item_check,
                R.id.contact_name,
                R.id.contact_status,
                R.id.contact_image,
                R.id.contact_check
        };

        public EventsParticipantsAdapter(final Context context) {
            super(context,
                    TC.Queries.TeammatesTeams.SORT_KEY,
                    R.layout.listview_item_team,
                    TC.Queries.TeammatesTeams.PROJECTION,
                    new int[]{
                            R.id.listView_item_creation,
                            R.id.listView_item_update,
                            R.id.listView_item_title,
                            R.id.listView_item_image,
                            R.id.listView_item_check
                    }
            );
        }

        @Override
        protected View setupView(final View view) {

            final TeamItem mTeamItem = new TeamItem();
            mTeamItem.cardView = view.findViewById(R.id.card);
            mTeamItem.team_title = (TextView) view.findViewById(R.id.listView_item_title);
            mTeamItem.team_update = (TextView) view.findViewById(R.id.listView_item_update);
            mTeamItem.team_creation = (TextView) view.findViewById(R.id.listView_item_creation);
            mTeamItem.team_thumbnail = (ImageView) view.findViewById(R.id.listView_item_image);
            mTeamItem.team_check = (CheckBox) view.findViewById(R.id.listView_item_check);
            mTeamItem.team_check.setOnCheckedChangeListener(this);
            view.setTag(mTeamItem);

            return view;
        }

        @Override
        public void bindView(final View view, final Context context, final Cursor cursor) {

            final int id = cursor.getInt(TC.Queries.TeammatesTeams.ID);
            final TeamItem teamItem = (TeamItem) view.getTag();

            teamItem.team_check.setTag(id);
            teamItem.team_check.setChecked(mCallback.isItemCollected(EventEntity.TEAMS, id));
            setHighlightedText(teamItem.team_title, cursor.getString(TC.Queries.TeammatesTeams.NAME), mCallback.getSearchTerm());
            setImageView(teamItem.team_thumbnail, mImageLoader, cursor.getString(TC.Queries.TeammatesTeams.IMAGE_REF));
            setDateText(teamItem.team_update, getResources().getString(R.string.update_prefix), cursor.getLong(TC.Queries.TeammatesTeams.UPDATED_AT));
            setDateText(teamItem.team_creation, getResources().getString(R.string.creation_prefix), cursor.getLong(TC.Queries.TeammatesTeams.CREATED_AT));
        }

        @Override
        public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
            final Integer id = (Integer) buttonView.getTag();
            mCallback.changeCollectionItemState(EventEntity.TEAMS, id, isChecked);
        }

        private final class TeamItem {

            TextView team_title = null;
            TextView team_update = null;
            TextView team_creation = null;
            ImageView team_thumbnail = null;
            CheckBox team_check = null;
            View cardView = null;
        }

    }

    //</editor-fold>
}
