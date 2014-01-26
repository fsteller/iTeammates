package com.fsteller.mobile.android.teammatesapp.activities.teams;

import android.app.Activity;
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
import com.fsteller.mobile.android.teammatesapp.TC;
import com.fsteller.mobile.android.teammatesapp.activities.base.FragmentPageBase;
import com.fsteller.mobile.android.teammatesapp.activities.base.IPageManager;
import com.fsteller.mobile.android.teammatesapp.utils.Adapters;
import com.fsteller.mobile.android.teammatesapp.utils.Image.ImageUtils;


/**
 * Created by fhernandezs on 24/12/13 for iTeammates.
 */
public final class TeamsPage extends FragmentPageBase implements AdapterView.OnItemClickListener {

    //<editor-fold desc="Constants">

    private static final int PAGE_INDEX = IPageManager.TEAMS_PAGE;
    private static final String TAG = TeamsPage.class.getSimpleName();

    //</editor-fold>
    //<editor-fold desc="Variables">

    private boolean isPortrait = true;

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

        this.mCallback.addCollection(IPageManager.TEAMS_PAGE);
        final View rootView = inflater.inflate(R.layout.fragment_page_teams, container, false);
        if (rootView != null) {

            rootView.setOnClickListener(mHideInputClass);
            mEmptyView = rootView.findViewById(android.R.id.empty);
            mListView = (AbsListView) rootView.findViewById(R.id.list_teammates_page);
            isPortrait = (rootView.findViewById(R.id.fragment_teamsPortraitPage) != null);
            if (getActivity() != null)
                imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

            hideSoftKeyboard(rootView);
        }
        Log.d(TAG, "onCreated");
        return rootView;
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final Activity mActivity = getActivity();

        setHasOptionsMenu(true);
        mCursorAdapter = new TeamsPortraitListAdapter(mActivity, isPortrait ?
                R.layout.listview_item_page : R.layout.gridview_item_page);
        mListView.setAdapter(mCursorAdapter);
        mListView.setOnItemClickListener(this);
        mListView.setMultiChoiceModeListener(this);
        mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        mImageLoader = ImageUtils.setupImageLoader(mActivity, R.drawable.ic_default_picture);
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
        final int cId = getPageIndex();
        mCallback.clearCollection(cId);
        mCallback.CollectionItemStateChanged(cId, ((TeamItem) view.getTag()).id, true);
        mCallback.actionRequest(cId, TC.Activity.ContextActionRequest.Edit);
    }

    //</editor-fold>
    //<editor-fold desc="FragmentPageBase">

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
    //<editor-fold desc="AbsListView.MultiChoiceModeListener">

    @Override
    public boolean onCreateActionMode(final ActionMode mode, final Menu menu) {
        MenuInflater inflater = mode.getMenuInflater();
        if (inflater != null) {
            mode.setTitle(R.string.TeamsPage_actionTitle);
            inflater.inflate(R.menu.teammates_context, menu);
        }
        return true;
    }

    @Override
    public boolean onActionItemClicked(final ActionMode mode, final MenuItem item) {
        final int requestCode = item.getItemId() == R.id.action_share ?
                TC.Activity.ContextActionRequest.Share : item.getItemId() == R.id.action_delete ?
                TC.Activity.ContextActionRequest.Delete : -1;

        return sendActionRequest(getPageIndex(), requestCode);
    }

    //</editor-fold>
    //<editor-fold desc="LoaderManager.LoaderCallbacks<Cursor>">

    @Override
    public Loader<Cursor> onCreateLoader(final int id, final Bundle bundle) {
        /*
            Creates and return a CursorLoader that will take care of
            creating a Cursor for the data being displayed.
        */
        Log.d(TAG, String.format("Creating loader, for TeamsQuery with searchTerm = '%s', id = %s", mSearchTerm, id));
        switch (id) {
            case TC.Queries.TeamsQuery.SIMPLE_QUERY_ID:
                return getQueryTeams();
            case TC.Queries.TeamsQuery.FILTER_QUERY_ID1:
                return getQueryFilteredByTearSearch(mSearchTerm);
            default:
                Log.e(TAG, "onCreateLoader - incorrect COLLECTION_ID provided (" + id + ")");
                return null;
        }
    }

    @Override
    public void onLoadFinished(final Loader<Cursor> cursorLoader, final Cursor data) {
        super.onLoadFinished(cursorLoader, data);
        if (cursorLoader.getId() == TC.Queries.TeamsQuery.SIMPLE_QUERY_ID ||
                cursorLoader.getId() == TC.Queries.TeamsQuery.FILTER_QUERY_ID1 ||
                cursorLoader.getId() == TC.Queries.TeamsQuery.FILTER_QUERY_ID2) {
            mCursorAdapter.swapCursor(data);
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
            mCursorAdapter.swapCursor(null);
    }

    //</editor-fold>

    @Override
    protected void processBroadcast(final Intent intent) {
        if (intent != null) {
            Log.d(TAG, String.format("Processing broadcast request: %s", intent.getAction()));
            restartLoader(TC.Queries.TeamsQuery.FILTER_QUERY_ID1, EMPTY_STRING);
        }
    }

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

    private final class TeamsPortraitListAdapter extends Adapters.CursorAdapter {

        public TeamsPortraitListAdapter(final Context context, final int layout) {
            super(context,
                    TC.Queries.TeamsQuery.SORT_KEY, layout,
                    TC.Queries.TeamsQuery.TEAMS_PROJECTION,
                    new int[]{
                            R.id.listView_item_creation,
                            R.id.listView_item_update,
                            R.id.listView_item_title,
                            R.id.listView_item_image,
                            R.id.listView_item_description
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
            view.setTag(mTeamItem);

            if (mTeamItem.team_thumbnail != null)
                mTeamItem.team_thumbnail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        final int position = mListView.getPositionForView(view);
                        final int id = (Integer) mTeamItem.team_thumbnail.getTag();
                        final boolean checked = !mCallback.isItemCollected(getPageIndex(), id);
                        mListView.setItemChecked(position, checked);
                    }
                });

            return view;
        }

        @Override
        public void bindView(final View view, final Context context, final Cursor cursor) {

            final int id = cursor.getInt(TC.Queries.TeamsQuery.ID);
            final TeamItem teamItem = (TeamItem) view.getTag();

            setHighlightedText(teamItem.team_title, cursor.getString(TC.Queries.TeamsQuery.NAME), mSearchTerm);
            setImageView(teamItem.team_thumbnail, mImageLoader, cursor.getString(TC.Queries.TeamsQuery.IMAGE_REF));
            setDateText(teamItem.team_update, getResources().getString(R.string.update_prefix), cursor.getLong(TC.Queries.TeamsQuery.UPDATED_AT));
            setDateText(teamItem.team_creation, getResources().getString(R.string.creation_prefix), cursor.getLong(TC.Queries.TeamsQuery.CREATED_AT));

            teamItem.id = id;
            teamItem.team_thumbnail.setTag(id);
            teamItem.cardView.setBackgroundResource(getBackgroundResource(mCallback.isItemCollected(getPageIndex(), id)));
        }
    }

    private static final class TeamItem {

        int id = -1;
        TextView team_title = null;
        TextView team_update = null;
        TextView team_creation = null;
        ImageView team_thumbnail = null;
        TextView team_description = null;
        View cardView = null;
    }

    //</editor-fold>
}
