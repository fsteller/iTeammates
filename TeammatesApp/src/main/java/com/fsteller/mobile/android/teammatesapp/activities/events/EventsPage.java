package com.fsteller.mobile.android.teammatesapp.activities.events;

import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.fsteller.mobile.android.teammatesapp.R;
import com.fsteller.mobile.android.teammatesapp.TC;
import com.fsteller.mobile.android.teammatesapp.activities.base.FragmentPageBase;
import com.fsteller.mobile.android.teammatesapp.activities.base.IPageManager;

/**
 * Created by fhernandezs on 24/12/13 for iTeammates.
 */
public final class EventsPage extends FragmentPageBase implements AdapterView.OnItemClickListener {

    //<editor-fold desc="Constants">

    private static final int PAGE_INDEX = IPageManager.EVENTS_PAGE;
    private static final String TAG = EventsPage.class.getSimpleName();

    //</editor-fold>
    //<editor-fold desc="Constructor">

    public EventsPage() {
        super(PAGE_INDEX, TC.Broadcast.DB_UPDATE_EVENTS_RECEIVE);
    }

    //</editor-fold>

    //<editor-fold desc="Overridden Methods">

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {

        this.mCallback.addCollection(IPageManager.EVENTS_PAGE);
        final View rootView = inflater.inflate(R.layout.fragment_page_events, container, false);
        if (rootView != null) {

        }
        return rootView;
    }

    //<editor-fold desc="FragmentPageBase Methods">

    //<editor-fold desc="LoaderManager.LoaderCallbacks<Cursor> Methods">

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    //</editor-fold>

    //<editor-fold desc="AdapterView.OnItemClickListener">

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    //</editor-fold>

    //</editor-fold>
    //</editor-fold>

    @Override
    protected void processBroadcast(final Intent intent) {
        if (intent != null) {
            Log.d(TAG, String.format("Processing broadcast request: %s", intent.getAction()));
            restartLoader(TC.Queries.TeammatesTeams.FILTER_QUERY_ID1, EMPTY_STRING);
            mCallback.clearCollection(getPageIndex());
        }
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        return false;
    }

    @Override
    public void onDestroyActionMode(final ActionMode mode) {

    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }


}
