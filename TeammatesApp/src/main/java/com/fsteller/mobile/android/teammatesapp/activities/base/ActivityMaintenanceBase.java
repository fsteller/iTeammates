package com.fsteller.mobile.android.teammatesapp.activities.base;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.fsteller.mobile.android.teammatesapp.TC;
import com.fsteller.mobile.android.teammatesapp.utils.Adapters;
import com.fsteller.mobile.android.teammatesapp.utils.Image.ImageLoader;

/**
 * Created by fhernandezs on 02/01/14 for iTeammates.
 */
public abstract class ActivityMaintenanceBase extends ActivityCollection implements IMaintenance {

    //<editor-fold desc="Constants">

    private final static String TAG = ActivityMaintenanceBase.class.getSimpleName();

    //</editor-fold>
    //<editor-fold desc="Variables">

    private final int index;

    private int id;
    private String name = "";
    private String imageRef = "";
    private String description = "";
    private boolean isRequieredToBeSaved = false;

    protected Adapters.CursorAdapter mCursorAdapter = null;
    protected ImageLoader mImageLoader = null;

    //</editor-fold>
    //<editor-fold desc="Constructor">

    protected ActivityMaintenanceBase(final int index) {
        this.index = index;
        this.addCollection(getMaintenanceId());
    }

    //</editor-fold>

    //<editor-fold desc="Public Methods">

    public boolean getIsRequiredToBeSaved() {
        return isRequieredToBeSaved;
    }

    public int getEntityId() {
        return id;
    }

    public void setEntityId(final Integer id) {
        this.id = id;
        this.isRequieredToBeSaved = true;
    }

    public int getMaintenanceId() {
        return index;
    }

    public String getEntityName() {
        return name;
    }

    public void setEntityName(final String name) {
        this.name = name.trim();
        this.isRequieredToBeSaved = true;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description.trim();
        this.isRequieredToBeSaved = true;
    }

    public Uri getImageRef() {
        return Uri.parse(this.imageRef);
    }

    public String getImageRefAsString() {
        return this.imageRef;
    }

    public void setImageRef(final Uri ref) {
        this.imageRef = ref.toString();
        this.isRequieredToBeSaved = true;
    }

    public void setImageRef(final String ref) {
        this.imageRef = ref.trim();
        this.isRequieredToBeSaved = true;
    }

    //</editor-fold>
    //<editor-fold desc="Protected Methods">

    protected void restartLoader(final int queryId, LoaderManager.LoaderCallbacks callbacks) {
        getLoaderManager().restartLoader(queryId, null, callbacks);
    }

    protected void finalize(final int result, final Intent intent) {
        Log.d(TAG, String.format("Finalizing, resultCode: %s", result));
        setResult(result, intent);
        finish();
    }

    protected abstract Intent getResult();

    protected abstract boolean checkData(IMaintenance entity);

    //</editor-fold>
    //<editor-fold desc="Overridden">

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null || resultCode == Activity.RESULT_CANCELED)
            return;

        if (requestCode == TC.Activity.ContextActionRequest.PickImage) {
            Log.i(TAG, String.format("Image picked up (%s)", data.getData()));
            final Uri imageUri = data.getData();
            if (imageUri != null) {
                setImageRef(imageUri);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCursorAdapter != null)
            mCursorAdapter.swapCursor(null);

        mCursorAdapter = null;
        mImageLoader = null;
        description = null;
        imageRef = null;
        name = null;
    }

    //</editor-fold>

}
