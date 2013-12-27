package com.fsteller.mobile.android.teammatesapp.fragments.base;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.SearchView;

import com.fsteller.mobile.android.teammatesapp.R;
import com.fsteller.mobile.android.teammatesapp.utils.Image.ImageLoader;

/**
 * Created by fhernandezs on 24/12/13 for iTeammates.
 */
public abstract class FragmentPageBase extends BaseFragment implements AbsListView.OnScrollListener, SearchView.OnQueryTextListener, AbsListView.MultiChoiceModeListener {

    //<editor-fold desc="Constants">

    private static final String TAG = FragmentPageBase.class.getSimpleName();

    //</editor-fold>
    //<editor-fold desc="Variables">

    protected static ImageLoader mImageLoader = null;
    protected final DbUpdateReceiver mReceiver;
    protected final IntentFilter mFilter;

    private int pageIndex = -1;

    //</editor-fold>
    //<editor-fold desc="Constructor">
    protected FragmentPageBase(final int pageIndex, final String actionFilter) {
        this.mReceiver = new DbUpdateReceiver();
        this.mFilter = new IntentFilter(actionFilter);
        this.pageIndex = pageIndex;
    }

    //</editor-fold>

    //<editor-fold desc="Overridden Methods">

    @Override
    public void onResume() {
        super.onResume();

        Log.i(TAG, String.format("Register DbUpdateReceiver[%s]", mFilter.getAction(0)));
        final LocalBroadcastManager mLBM = LocalBroadcastManager.getInstance(getActivity());
        mLBM.registerReceiver(mReceiver, mFilter);
    }

    @Override
    public void onPause() {
        super.onPause();

        /*
            In the case onPause() is called during a fling the image loader is
            un-paused to let any remaining background work complete.
        */
        if (mImageLoader != null)
            mImageLoader.setPauseWork(false);
        /*
            In the case onPause() is called broadcast receivers must be unregistered
            to avoid issues
        */
        Log.i(TAG, String.format("Un-register DbUpdateReceiver[%s]", mFilter));
        final LocalBroadcastManager mLBM = LocalBroadcastManager.getInstance(getActivity());
        mLBM.unregisterReceiver(mReceiver);
    }

    //<editor-fold desc="AbsListView.OnScrollListener Methods">

    @Override
    public void onScrollStateChanged(final AbsListView view, final int scrollState) {
        // Pause image loader to ensure smoother scrolling when flinging
        if (mImageLoader != null) {
            if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING)
                mImageLoader.setPauseWork(true);
            else
                mImageLoader.setPauseWork(false);
        }
    }

    @Override
    public void onScroll(final AbsListView view, final int firstVisibleItem, final int visibleItemCount, final int totalItemCount) {

    }

    //</editor-fold>
    //<editor-fold desc="SearchView.OnQueryTextListener Implementation">

    @Override
    public boolean onQueryTextSubmit(final String query) {
        // Nothing needs to happen when the user submits the search string
        return true;
    }

    //</editor-fold>
    //<editor-fold desc="AbsListView.MultiChoiceModeListener Methods">

    @Override
    public void onItemCheckedStateChanged(final ActionMode mode, final int position, final long id, final boolean checked) {
        final View rootView = mListView.getChildAt(position);
        if (rootView != null) {
            final View cardView = rootView.findViewById(R.id.card);
            if (cardView != null) {
                cardView.setBackgroundResource(getBackgroundResource(checked));
                mCallback.itemStateChanged(this, (int) id, checked);
            }
        }
    }

    @Override
    public boolean onPrepareActionMode(final ActionMode mode, final Menu menu) {
        return false;
    }

    @Override
    public void onDestroyActionMode(final ActionMode mode) {

    }
    //</editor-fold>
    //</editor-fold>
    //<editor-fold desc="Protected Methods">

    protected final int getPageIndex() {
        return pageIndex;
    }

    protected final void restartLoader(final int queryId, final String newFilter) {
        Log.i(TAG, String.format("Reloading displayed data, using filter: '%s'", newFilter));
        mSearchTerm = isNullOrEmpty(newFilter) ? EMPTY_STRING : newFilter.trim();
        final LoaderManager mLoaderManager = getLoaderManager();
        if (mLoaderManager != null)
            getLoaderManager().restartLoader(queryId, null, this);
    }

    protected abstract void processBroadcast(final Intent intent);

    protected boolean sendActionRequest(final int requestCode) {
        final boolean result = requestCode > 0;
        if (result)
            mCallback.actionRequest(this, requestCode);
        return result;
    }

    //</editor-fold>

    //<editor-fold desc="Inner classes">

    /**
     * Receiver to wake up when Broadcast is updated
     * It refreshes the UI by re-querying a new the cursor
     */
    protected final class DbUpdateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            Log.d(this.getClass().getSimpleName(), String.format("DbUpdateReceiver: context (%s) ", context));
            processBroadcast(intent);
        }
    }

    //</editor-fold>
}
