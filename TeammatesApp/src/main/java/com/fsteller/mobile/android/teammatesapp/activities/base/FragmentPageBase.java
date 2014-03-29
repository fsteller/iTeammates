package com.fsteller.mobile.android.teammatesapp.activities.base;

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
import android.widget.AbsListView;
import android.widget.SearchView;

import com.fsteller.mobile.android.teammatesapp.R;
import com.fsteller.mobile.android.teammatesapp.TC;
import com.fsteller.mobile.android.teammatesapp.utils.Adapters;

/**
 * Project: iTeammates
 * Subpackage: activities.base
 * <p/>
 * Description:
 * Created by by fhernandezs on 23/01/14.
 */
public abstract class FragmentPageBase extends FragmentBase implements AbsListView.OnScrollListener, SearchView.OnQueryTextListener, AbsListView.MultiChoiceModeListener {

    //<editor-fold desc="Constants">

    private static final String TAG = FragmentPageBase.class.getSimpleName();

    //</editor-fold>
    //<editor-fold desc="Variables">

    private final IntentFilter mFilter;
    private final DbUpdateReceiver mReceiver;
    protected Adapters.CursorAdapter mCursorAdapter;
    protected IPageManager mCallback = null;
    private int pageIndex = -1;

    //</editor-fold>
    //<editor-fold desc="Constructor">

    protected FragmentPageBase(final int pageIndex, final String actionFilter) {
        this.mFilter = new IntentFilter(actionFilter);
        this.mReceiver = new DbUpdateReceiver();
        this.pageIndex = pageIndex;
    }

    //</editor-fold>

    //<editor-fold desc="Overridden">

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
            In the case onPause() is called broadcast receivers must be unregistered
            to avoid issues
        */
        Log.i(TAG, String.format("Un-register DbUpdateReceiver[%s]", mFilter));
        final LocalBroadcastManager mLBM = LocalBroadcastManager.getInstance(getActivity());
        mLBM.unregisterReceiver(mReceiver);
    }


    @Override
    public void onAttach(final Activity activity) {
        super.onAttach(activity);
        mCallback = ((IPageManager) activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

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
    //<editor-fold desc="SearchView.OnQueryTextListener">

    @Override
    public boolean onQueryTextSubmit(final String query) {
        // Nothing needs to happen when the user submits the search string
        return true;
    }

    //</editor-fold>
    //<editor-fold desc="AbsListView.MultiChoiceModeListener">

    @Override
    public void onItemCheckedStateChanged(final ActionMode mode, final int position, final long id, final boolean checked) {
        mCallback.changeCollectionItemState(getPageIndex(), (int) id, checked);
    }

    @Override
    public boolean onPrepareActionMode(final ActionMode mode, final Menu menu) {
        return false;
    }

    @Override
    public void onDestroyActionMode(final ActionMode mode) {
        mCallback.clearCollection(getPageIndex());
    }

    //</editor-fold>
    //</editor-fold>
    //<editor-fold desc="Protected">

    protected final int getPageIndex() {
        return pageIndex;
    }

    protected final void restartLoader(final LoaderManager.LoaderCallbacks callbacks) {
        final LoaderManager mLoaderManager = getLoaderManager();
        if (mLoaderManager != null)
            getLoaderManager().restartLoader(TC.Queries.TeammatesTeams.FILTER_QUERY_ID1, null, callbacks);
    }

    protected abstract void processBroadcast(final Intent intent);

    protected boolean sendActionRequest(final int collectionId, final int requestCode) {
        final boolean result = requestCode > 0;
        if (result)
            mCallback.actionRequest(collectionId, requestCode);
        return result;
    }

    protected static int getBackgroundResource(final boolean checked) {
        return checked ?
                R.drawable.holo_card_selected :
                R.drawable.holo_card_white;
    }

    //</editor-fold>

    //<editor-fold desc="Inner classes">

    /**
     * Receiver to wake up when Broadcast is updated
     * It refreshes the UI by re-querying a new the cursor
     */
    public final class DbUpdateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            Log.d(this.getClass().getSimpleName(), String.format("DbUpdateReceiver: context (%s) ", context));
            processBroadcast(intent);
        }
    }

    //</editor-fold>
}
