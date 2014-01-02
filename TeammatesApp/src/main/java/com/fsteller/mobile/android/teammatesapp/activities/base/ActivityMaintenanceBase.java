package com.fsteller.mobile.android.teammatesapp.activities.base;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.AbsListView;

import com.fsteller.mobile.android.teammatesapp.utils.Adapters;
import com.fsteller.mobile.android.teammatesapp.utils.Image.ImageLoader;

/**
 * Created by fhernandezs on 02/01/14 for iTeammates.
 */
public abstract class ActivityMaintenanceBase extends ActivityBase implements Entity, IMaintenance {

    private final static String TAG = ActivityMaintenanceBase.class.getSimpleName();

    private final int index;
    private String name = "";
    private String imageRef = "";
    private String description = "";

    protected Adapters.CursorAdapter mCursorAdapter = null;
    protected ImageLoader mImageLoader = null;

    protected ActivityMaintenanceBase(final int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public String getEntityName() {
        return name;
    }

    public void setEntityName(final String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public Uri getImageRef() {
        return Uri.parse(this.imageRef);
    }

    public String getImageStringRef() {
        return this.imageRef;
    }

    public void setImageRef(final Uri ref) {
        this.imageRef = ref.toString();
    }

    public void setImageRef(final String ref) {
        this.imageRef = ref;
    }

    protected void restartLoader(final int queryId) {
        getLoaderManager().restartLoader(queryId, null, this);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null || resultCode == Activity.RESULT_CANCELED)
            return;

        if (requestCode == TC.Activity.ActionRequest.PickImage) {
            Log.i(TAG, String.format("Image picked up (%s)", data.getData()));
            final Uri imageUri = data.getData();
            if (imageUri != null) {
                //mCallback.setCollectionDrawableUri(imageUri);
            }
        }
    }

    //<editor-fold desc="AbsListView.OnScrollListener">

    @Override
    public void onScroll(final AbsListView view, final int firstVisibleItem, final int visibleItemCount, final int totalItemCount) {

    }

    @Override
    public void onScrollStateChanged(final AbsListView view, final int scrollState) {
        // Pause image loader to ensure smoother scrolling when flinging
        if (mImageLoader != null) {
            if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING)
                mImageLoader.setPauseWork(true);
            else
                mImageLoader.setPauseWork(false);
        }

        if (mCursorAdapter != null) {
            if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING)
                mCursorAdapter.setCancelWork(true);
            else
                mCursorAdapter.setCancelWork(false);
        }
    }

    //</editor-fold>
}
