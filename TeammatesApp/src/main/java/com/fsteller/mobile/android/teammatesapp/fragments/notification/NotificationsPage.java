package com.fsteller.mobile.android.teammatesapp.fragments.notification;

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
import com.fsteller.mobile.android.teammatesapp.fragments.base.FragmentPageBase;
import com.fsteller.mobile.android.teammatesapp.fragments.base.TC;

/**
 * Created by fhernandezs on 24/12/13 for iTeammates.
 */
public class NotificationsPage extends FragmentPageBase implements AdapterView.OnItemClickListener {

    //<editor-fold desc="Constants">

    private static final int PAGE_INDEX = 0x0003;
    private static final String TAG = NotificationsPage.class.getSimpleName();

    //</editor-fold>
    //<editor-fold desc="Constructor">

    public NotificationsPage() {
        super(PAGE_INDEX, TC.Broadcast.DB_UPDATE_NOTIFICATIONS_RECEIVE);
    }

    //</editor-fold>

    //<editor-fold desc="Overridden Methods">

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_page_notify, container, false);
        if (rootView != null) {

        }
        return rootView;
    }

    //<editor-fold desc="FragmentPageBase Methods">

    //<editor-fold desc="LoaderManager.LoaderCallbacks<Cursor> Methods">

    @Override
    protected void processBroadcast(final Intent intent) {
        if (intent != null) {
            Log.d(TAG, String.format("Processing broadcast request: %s", intent.getAction()));
            restartLoader(TC.Queries.TeamsQuery.FILTER_QUERY_ID1, EMPTY_STRING);
            mCallback.clearItemCollection();
        }
    }

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
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }


}
