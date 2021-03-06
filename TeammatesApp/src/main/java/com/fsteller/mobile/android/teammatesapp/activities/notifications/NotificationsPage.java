package com.fsteller.mobile.android.teammatesapp.activities.notifications;

import android.app.LoaderManager;
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
 * Project: iTeammates
 * Subpackage: activities.notification
 * <p/>
 * Description:
 * Created by fhernandezs on 24/12/13 for iTeammates.
 */
public final class NotificationsPage extends FragmentPageBase implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener {

    //<editor-fold desc="Constants">

    private static final int PAGE_INDEX = IPageManager.NOTIFICATIONS_PAGE;
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

        this.mCallback.addCollection(IPageManager.NOTIFICATIONS_PAGE);
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
            restartLoader(this);
            mCallback.clearCollection(getPageIndex());
        }
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
    public void onDestroyActionMode(final ActionMode mode) {

    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }


    @Override
    protected int getFragmentDefaultImage() {
        return R.drawable.ic_default_picture;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
