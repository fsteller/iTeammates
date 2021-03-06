package com.fsteller.mobile.android.teammatesapp.activities.events;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
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
import com.fsteller.mobile.android.teammatesapp.image.Utils;
import com.fsteller.mobile.android.teammatesapp.model.EventsEntity;
import com.fsteller.mobile.android.teammatesapp.model.base.IEventsEntity;
import com.fsteller.mobile.android.teammatesapp.utils.Adapters.CalendarAdapter;
import com.fsteller.mobile.android.teammatesapp.utils.Adapters.CursorAdapter;
import com.fsteller.mobile.android.teammatesapp.utils.Text;


/**
 * Project: iTeammates
 * Subpackage: activities.events
 * <p/>
 * Description:
 * <p/>
 * Created by fhernandezs on 23/01/14.
 */
public class EventsMaintenancePage1 extends FragmentMaintenancePageBase implements AdapterView.OnItemSelectedListener, AbsListView.OnScrollListener, LoaderManager.LoaderCallbacks<Cursor> {

    //<editor-fold desc="Constants">

    private static final String TAG = EventsMaintenancePage1.class.getSimpleName();

    //</editor-fold>
    //<editor-fold desc="Variables">

    private IEventsEntity mEventEntity = null;

    private ImageView titleImage = null;
    private Spinner headerSpinner = null;
    private EditText decriptionText = null;
    private EditText titleText = null;

    private EventsParticipantsAdapter teamsAdapter = null;
    private CalendarAdapter calendarAdapter = null;

    //</editor-fold>

    //<editor-fold desc="Overridden">
    //<editor-fold desc="Fragment">

    @Override
    public void onAttach(final Activity activity) {
        super.onAttach(activity);
        this.mEventEntity = (IEventsEntity) mEntity;
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final Activity mActivity = getActivity();

        if (mActivity != null) {

            imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);

            calendarAdapter = new CalendarAdapter(mActivity);
            teamsAdapter = new EventsParticipantsAdapter(mActivity);

            headerSpinner.setOnItemSelectedListener(this);
            headerSpinner.setAdapter(calendarAdapter);
            mListView.setOnScrollListener(this);
            mListView.setAdapter(teamsAdapter);
        }

        Log.d(TAG, "onActivityCreated");
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_events_maintenance_page1, container, false);
        if (rootView != null) {

            mEmptyView = rootView.findViewById(android.R.id.empty);
            mListView = (AbsListView) rootView.findViewById(R.id.list_view);
            titleImage = (ImageView) rootView.findViewById(R.id.header_image);
            headerSpinner = (Spinner) rootView.findViewById(R.id.header_spinner);
            titleText = (EditText) rootView.findViewById(R.id.collection_title_text);
            decriptionText = (EditText) rootView.findViewById(R.id.collection_description_text);

            final TextView titleLabel = (TextView) rootView.findViewById(R.id.title_label);
            final TextView controlView = (TextView) rootView.findViewById(R.id.header_control_label);
            final TextView descriptionLabel = (TextView) rootView.findViewById(R.id.title_description_label);

            titleLabel.setText(getResources().getString(R.string.eventsMaintenance_titleLabel));
            controlView.setText(getResources().getString(R.string.eventsMaintenance_titleControlLabel));
            descriptionLabel.setText(getResources().getString(R.string.eventsMaintenance_titleDescriptionLabel));

            final EditText eventNameText = (EditText) rootView.findViewById(R.id.collection_title_text);
            final TextView eventNameView = (TextView) rootView.findViewById(R.id.collection_title_label);
            final EditText eventDescriptionText = (EditText) rootView.findViewById(R.id.collection_description_text);
            final TextView eventDescriptionView = (TextView) rootView.findViewById(R.id.collection_description_label);
            final TextView lookupKeyTitleText = (TextView) rootView.findViewById(R.id.collection_lookup_key_text);
            final TextView lookupKeyTitleView = (TextView) rootView.findViewById(R.id.collection_lookup_key_label);
            final ImageButton button = (ImageButton) rootView.findViewById(R.id.header_button);

            eventNameText.setSelectAllOnFocus(true);
            eventNameText.setHint(getResources().getString(R.string.eventsMaintenance_lookupTitle1_hint));
            eventNameText.setText(mEventEntity.getName());

            eventDescriptionText.setSelectAllOnFocus(true);
            eventDescriptionText.setText(mEventEntity.getDescription());
            eventDescriptionText.setHint(getResources().getString(R.string.eventsMaintenance_descriptionHint));
            eventDescriptionView.setText(getResources().getString(R.string.eventsMaintenance_DescriptionLabel));
            eventNameView.setText(getResources().getString(R.string.eventsMaintenance_lookupTitle1_label));
            lookupKeyTitleText.setHint(getResources().getString(R.string.eventsMaintenance_lookupHint));
            lookupKeyTitleView.setText(getResources().getString(R.string.eventsMaintenance_lookupLabel));

            eventNameText.addTextChangedListener(new Text.AfterTextChangedWatcher() {
                @Override
                public void afterTextChanged(final Editable s) {
                    mEventEntity.setName(s.toString());
                }
            });

            eventDescriptionText.addTextChangedListener(new Text.AfterTextChangedWatcher() {
                @Override
                public void afterTextChanged(final Editable s) {
                    mEventEntity.setDescription(s.toString());
                }
            });

            lookupKeyTitleText.addTextChangedListener(new Text.AfterTextChangedWatcher() {
                @Override
                public void afterTextChanged(final Editable s) {
                    mEventEntity.setSearchTerm(s.toString().trim());
                    restartLoader(TC.Queries.TeammatesTeams.FILTER_QUERY_ID1, EventsMaintenancePage1.this);
                }
            });

            button.setOnClickListener(new Utils.PickImage(getActivity()));
        }
        Log.d(TAG, "onCreated");
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        this.restartLoader(TC.Queries.PhoneCalendar.SIMPLE_QUERY_ID, this);
        this.restartLoader(TC.Queries.TeammatesTeams.FILTER_QUERY_ID1, this);
        mLoader.loadImage(mEventEntity.getImageRef(), titleImage);
        decriptionText.setText(mEventEntity.getDescription());
        titleText.setText(mEventEntity.getName());
    }

    //</editor-fold>
    //<editor-fold desc="AbsListView.OnScrollListener">

    @Override
    public void onScrollStateChanged(final AbsListView view, final int scrollState) {
        // Pause image loader to ensure smoother scrolling when flinging
        if (mLoader != null) {
            if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING)
                mLoader.setPauseWork(true);
            else
                mLoader.setPauseWork(false);
        }
        hideSoftKeyboard(view);
    }

    @Override
    public void onScroll(final AbsListView view, final int firstVisibleItem, final int visibleItemCount, final int totalItemCount) {

    }

    //</editor-fold>
    //<editor-fold desc="AdapterView.OnItemSelectedListener">

    @Override
    public void onItemSelected(final AdapterView<?> parent, final View view, final int position, final long id) {
        final CalendarAdapter.CalendarItem mCalendarItem = (CalendarAdapter.CalendarItem) view.getTag();
        this.mEventEntity.setCalendarId(mCalendarItem.id);
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
                return getParticipantsFilteredByTermSearch(mEventEntity.getSearchTerm());
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
                headerSpinner.setVisibility(data.getCount() > 0 ? View.VISIBLE : View.GONE);
                headerSpinner.setSelection(calendarAdapter.getPosition(mEventEntity.getCalendarId()));
                break;
            case TC.Queries.TeammatesTeams.FILTER_QUERY_ID1:
                mEmptyView.setVisibility(data.getCount() > 0 ? View.GONE : View.VISIBLE);
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
                mEmptyView.setVisibility(View.VISIBLE);
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
    //<editor-fold desc="Private">

    private Loader<Cursor> getParticipantsFilteredByTermSearch(final String searchTerm) {

        /*
           Returns a new CursorLoader for querying the teams table. No arguments are used
           for the selection clause. The search string is either encoded onto the content URI,
           or no contacts search string is used. The other search criteria are constants. See
           the PhoneContacts interface.
        */

        final Uri teamsUri = isNullOrEmpty(searchTerm) ?
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

    private final class EventsParticipantsAdapter extends CursorAdapter implements CompoundButton.OnCheckedChangeListener {

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
            teamItem.team_check.setChecked(mEventEntity.isItemCollected(EventsEntity.TEAMS, id));
            setHighlightedText(teamItem.team_title, cursor.getString(TC.Queries.TeammatesTeams.NAME), mEventEntity.getSearchTerm());
            setImageView(teamItem.team_thumbnail, mLoader, cursor.getString(TC.Queries.TeammatesTeams.IMAGE_REF));
            setDateText(teamItem.team_update, getResources().getString(R.string.update_prefix), cursor.getLong(TC.Queries.TeammatesTeams.UPDATED_AT));
            setDateText(teamItem.team_creation, getResources().getString(R.string.creation_prefix), cursor.getLong(TC.Queries.TeammatesTeams.CREATED_AT));
        }

        @Override
        public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
            final Integer id = (Integer) buttonView.getTag();
            mEventEntity.changeCollectionItemState(EventsEntity.TEAMS, id, isChecked);
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
