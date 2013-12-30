package com.fsteller.mobile.android.teammatesapp.activities.teams;

import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.fsteller.mobile.android.teammatesapp.R;
import com.fsteller.mobile.android.teammatesapp.activities.base.FragmentPageBase;
import com.fsteller.mobile.android.teammatesapp.activities.base.TC;
import com.fsteller.mobile.android.teammatesapp.utils.Adapters;
import com.fsteller.mobile.android.teammatesapp.utils.DateTime;


/**
 * Created by fhernandezs on 24/12/13 for iTeammates.
 */
public class TeamsPage extends FragmentPageBase implements AdapterView.OnItemClickListener {

    //<editor-fold desc="Constants">

    private static final int PAGE_INDEX = 0x0002;
    private static final String TAG = TeamsPage.class.getSimpleName();

    //</editor-fold>
    //<editor-fold desc="Variables">

    private boolean isPortrait = true;
    private Adapters.TeammatesSimpleCursorAdapter mTeamsAdapter = null;

    //</editor-fold>
    //<editor-fold desc="Constructor">

    public TeamsPage() {
        super(PAGE_INDEX, TC.Broadcast.DB_UPDATE_TEAMS_RECEIVE);
    }

    //</editor-fold>

    //<editor-fold desc="Overridden Methods">

    @Override
    public void onResume() {
        super.onResume();
        restartLoader(TC.Queries.TeamsQuery.FILTER_QUERY_ID1, mSearchTerm);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_page_teams, container, false);
        if (rootView != null) {
            mEmptyView = rootView.findViewById(android.R.id.empty);
            mListView = (AbsListView) rootView.findViewById(R.id.list_teammates_teams);
            isPortrait = (rootView.findViewById(R.id.fragment_teamsPortraitPage) != null);
            imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        }
        Log.d(TAG, "onCreated");
        return rootView;
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setHasOptionsMenu(true);

        mTeamsAdapter = isPortrait ?
                new TeamsPortraitListAdapter(getActivity()) :
                new TeamsLandscapeListAdapter(getActivity());

        mListView.setAdapter(mTeamsAdapter);
        mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        mListView.setMultiChoiceModeListener(this);
        mListView.setOnItemClickListener(this);

        Log.d(TAG, "onActivityCreated");
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        if (searchItem != null) {
            final SearchView searchView = (SearchView) searchItem.getActionView();
            if (searchView != null) {
                searchView.setOnQueryTextListener(this);
                searchView.setQuery(mSearchTerm, false);
            }
        }
    }

    //<editor-fold desc="AdapterView.OnItemClickListener">

    @Override
    public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
        mCallback.clearItemCollection();
        mCallback.itemStateChanged(this, (int) id, true);
        mCallback.actionRequest(this, TC.Activity.ActionRequest.Edit);
    }


    //</editor-fold>

    //<editor-fold desc="FragmentPageBase Methods">

    @Override
    protected void processBroadcast(final Intent intent) {
        if (intent != null) {
            Log.d(TAG, String.format("Processing broadcast request: %s", intent.getAction()));
            restartLoader(TC.Queries.TeamsQuery.FILTER_QUERY_ID1, EMPTY_STRING);
            mCallback.clearItemCollection();
        }
    }

    //<editor-fold desc="SearchView.OnQueryTextListener Methods">

    @Override
    public boolean onQueryTextChange(final String newFilter) {
        /*
        Don't do anything if the new filter is null or the same as the current filter
        Restarts the loader. This triggers onCreateLoader(),
        which builds the necessary content Uri from mSearchTerm.
        */

        if (newFilter != null && !newFilter.trim().equals(mSearchTerm))
            restartLoader(TC.Queries.TeamsQuery.FILTER_QUERY_ID1, newFilter);
        return true;
    }

    //</editor-fold>
    //<editor-fold desc="AbsListView.MultiChoiceModeListener Methods">

    @Override
    public boolean onCreateActionMode(final ActionMode mode, final Menu menu) {
        MenuInflater inflater = mode.getMenuInflater();
        if (inflater != null)
            inflater.inflate(R.menu.teammates_context, menu);
        return true;
    }

    @Override
    public boolean onActionItemClicked(final ActionMode mode, final MenuItem item) {

        mode.finish();
        final int requestCode =
                item.getItemId() == R.id.action_share ? TC.Activity.ActionRequest.Share :
                        item.getItemId() == R.id.action_delete ? TC.Activity.ActionRequest.Delete : -1;

        return sendActionRequest(requestCode);
    }

    //</editor-fold>
    //<editor-fold desc="LoaderManager.LoaderCallbacks<Cursor> Methods">
    @Override
    public Loader<Cursor> onCreateLoader(final int id, final Bundle bundle) {
        /*
            Creates and return a CursorLoader that will take care of
            creating a Cursor for the data being displayed.
        */
        Log.d(TAG, String.format("Creating loader, for TeamsQuery with searchTerm = '%s', id = %s", mSearchTerm, id));
        switch (id) {
            case TC.Queries.TeamsQuery.FILTER_QUERY_ID1:
                return getQueryFilteredByTearSearch(mSearchTerm);
            case TC.Queries.TeamsQuery.SIMPLE_QUERY_ID:
                return getQueryTeams();
            default:
                Log.e(TAG, "onCreateLoader - incorrect ID provided (" + id + ")");
                return null;
        }
    }

    @Override
    public void onLoadFinished(final Loader<Cursor> cursorLoader, final Cursor data) {
        super.onLoadFinished(cursorLoader, data);
        if (cursorLoader.getId() == TC.Queries.TeamsQuery.SIMPLE_QUERY_ID ||
                cursorLoader.getId() == TC.Queries.TeamsQuery.FILTER_QUERY_ID1 ||
                cursorLoader.getId() == TC.Queries.TeamsQuery.FILTER_QUERY_ID2) {
            mTeamsAdapter.swapCursor(data);
        }
    }

    @Override
    public void onLoaderReset(final Loader<Cursor> cursorLoader) {
        /*
            When the loader is being reset, clear the cursor from the adapter.
            This allows the cursor resources to be freed.
        */
        if (cursorLoader.getId() == TC.Queries.TeamsQuery.SIMPLE_QUERY_ID ||
                cursorLoader.getId() == TC.Queries.TeamsQuery.FILTER_QUERY_ID1)
            mTeamsAdapter.swapCursor(null);
    }

    //</editor-fold>

    //</editor-fold>

    //</editor-fold>
    //<editor-fold desc="Private Methods">

    private Loader<Cursor> getQueryTeams() {
        final String selection = TC.Queries.TeamsQuery.SELECTION;
        final Uri contentUri = TC.Queries.TeamsQuery.CONTENT_URI;
        final String sortOrder = TC.Queries.TeamsQuery.SORT_ORDER;
        final String[] projection = TC.Queries.TeamsQuery.TEAMS_PROJECTION;

        return new CursorLoader(getActivity(), contentUri, projection, selection, null, sortOrder);
    }

    private Loader<Cursor> getQueryFilteredByTearSearch(final String searchTerm) {
        final String selection = TC.Queries.TeamsQuery.SELECTION;
        final Uri contentUri = Uri.withAppendedPath(TC.Queries.TeamsQuery.FILTER_URI, Uri.encode(searchTerm));
        final String sortOrder = TC.Queries.TeamsQuery.SORT_ORDER;
        final String[] projection = TC.Queries.TeamsQuery.TEAMS_PROJECTION;

        return new CursorLoader(getActivity(), contentUri, projection, selection, null, sortOrder);
    }

    //</editor-fold>

    //<editor-fold desc="Inner Classes">

    private final class TeamsPortraitListAdapter extends Adapters.TeammatesSimpleCursorAdapter {

        public TeamsPortraitListAdapter(final Context context) {
            super(context,
                    TC.Queries.TeamsQuery.SORT_KEY,
                    R.layout.page_listview_item,
                    TC.Queries.TeamsQuery.TEAMS_PROJECTION,
                    new int[]{
                            R.id.listView_item_date,
                            R.id.listView_item_title,
                            R.id.listView_item_image,
                            R.id.listView_item_description
                    }
            );
        }

        @Override
        public boolean setViewValue(final View view, final Cursor cursor, final int i) {
            switch (view.getId()) {
                case R.id.listView_item_title:
                    setHighlightedName((TextView) view, cursor.getString(TC.Queries.TeamsQuery.NAME), mSearchTerm);
                    break;
                case R.id.listView_item_image:
                    view.setTag(cursor.getInt(TC.Queries.TeamsQuery.ID));
                    setImageView((ImageView) view, mImageLoader, cursor.getString(TC.Queries.TeamsQuery.IMAGE_REF));
                    break;
                case R.id.listView_item_date:
                    final long createdAt = cursor.getLong(TC.Queries.TeamsQuery.CREATED_AT);
                    final String dateCreated = DateTime.getDate(createdAt, DateTime.DateFormat);
                    ((TextView) view).setText(dateCreated);
                    break;
                case R.id.listView_item_description:
                    final long updatedAt = cursor.getLong(TC.Queries.TeamsQuery.UPDATED_AT);
                    final String dateUpdated = DateTime.getDate(updatedAt, DateTime.DateFormat);
                    ((TextView) view).setText(dateUpdated);
                    break;
            }
            return true;
        }

        @Override
        protected View setupView(final View view) {
            final View cardView = view.findViewById(R.id.card);
            final ImageView image = (ImageView) view.findViewById(R.id.listView_item_image);

            if (cardView != null)
                cardView.setBackgroundResource(R.drawable.holo_card_clean);

            if (image != null)
                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final int position = mListView.getPositionForView(view);
                        final boolean checked = !mCallback.isItemCollected((Integer) image.getTag());
                        mListView.setItemChecked(position, checked);
                    }
                });

            return view;
        }
    }

    private final class TeamsLandscapeListAdapter extends Adapters.TeammatesSimpleCursorAdapter {

        public TeamsLandscapeListAdapter(final Context context) {
            super(context,
                    TC.Queries.TeamsQuery.SORT_KEY,
                    R.layout.page_gridview_item,
                    TC.Queries.TeamsQuery.TEAMS_PROJECTION,
                    new int[]{
                            R.id.listView_item_date,
                            R.id.listView_item_image,
                            R.id.listView_item_title,
                            R.id.listView_item_description
                    });
        }

        @Override
        public boolean setViewValue(final View view, final Cursor cursor, final int i) {
            switch (view.getId()) {
                case R.id.listView_item_title:
                    setHighlightedName((TextView) view, cursor.getString(TC.Queries.TeamsQuery.NAME), mSearchTerm);
                    break;
                case R.id.listView_item_image:
                    view.setTag(cursor.getInt(TC.Queries.TeamsQuery.ID));
                    setImageView((ImageView) view, mImageLoader, cursor.getString(TC.Queries.TeamsQuery.IMAGE_REF));
                    break;
                case R.id.listView_item_date:
                    final long createdAt = cursor.getLong(TC.Queries.TeamsQuery.CREATED_AT);
                    final String dateCreated = DateTime.getDate(createdAt, DateTime.DateFormat);
                    ((TextView) view).setText(String.format(getString(R.string.created_label), dateCreated));
                    break;
                case R.id.listView_item_description:
                    final long updatedAt = cursor.getLong(TC.Queries.TeamsQuery.UPDATED_AT);
                    final String dateUpdated = DateTime.getDate(updatedAt, DateTime.DateFormat);
                    ((TextView) view).setText(String.format(getString(R.string.updated_label), dateUpdated));
                    break;
            }
            return true;
        }

        @Override
        protected View setupView(final View view) {
            return view;
        }
    }

    //</editor-fold>
}
