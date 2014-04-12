package com.fsteller.mobile.android.teammatesapp.activities.base;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.fsteller.mobile.android.teammatesapp.TC;
import com.fsteller.mobile.android.teammatesapp.model.base.IEntity;
import com.fsteller.mobile.android.teammatesapp.model.base.IMaintenance;

import java.util.ArrayList;

/**
 * Project: iTeammates
 * Subpackage: activities.base
 * <p/>
 * Description:
 * Created by fhernandezs on 02/01/14 for iTeammates.
 */
public abstract class ActivityMaintenanceBase extends ActivityBase implements IEntity {

    //<editor-fold desc="Constants">

    private final static String TAG = ActivityMaintenanceBase.class.getSimpleName();

    //</editor-fold>
    //<editor-fold desc="Variables">

    protected IMaintenance mEntity = null;

    //</editor-fold>
    //<editor-fold desc="Constructor">

    protected ActivityMaintenanceBase(final IMaintenance entity) {
        mEntity = entity;
    }

    //</editor-fold>

    //<editor-fold desc="Protected">

    protected void restartLoader(final int queryId, LoaderManager.LoaderCallbacks callbacks) {
        LoaderManager mLoaderManager = getLoaderManager();
        if (mLoaderManager != null)
            mLoaderManager.restartLoader(queryId, null, callbacks);
    }

    protected void finalize(final int result, final Intent intent) {
        Log.d(TAG, String.format("Finalizing, resultCode: %s", result));
        setResult(result, intent);
        finish();
    }

    protected abstract boolean checkData(final IEntity entity);

    //</editor-fold>
    //<editor-fold desc="Overridden">

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent result) {
        super.onActivityResult(requestCode, resultCode, result);

        if (result == null || resultCode == Activity.RESULT_CANCELED)
            return;

        switch (requestCode) {
            case TC.MediaStore.Pick_Image:
                mEntity.setImageRef(TC.MediaStore.URI_TMP_FILE);
                break;
            default:
        }

    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        if (outState != null) {
            outState.putBundle(TC.ENTITY_DATA, mEntity.getResult());
            super.onSaveInstanceState(outState);
        }
    }

    @Override
    protected void onRestoreInstanceState(final Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            final Bundle mBundle = savedInstanceState.getBundle(TC.ENTITY_DATA);
            mEntity.loadData(this, mBundle);
        }
    }

    @Override
    public void onBackPressed() {
        hideSoftKeyboard(getCurrentFocus());
        finalize(RESULT_CANCELED, null);
    }

    //<editor-fold desc="IMaintenance">

    @Override
    public int getId() {
        return mEntity.getId();
    }

    @Override
    public void setId(final int id) {
        mEntity.setId(id);
    }

    @Override
    public String getName() {
        return mEntity.getName();
    }

    @Override
    public void setName(final String name) {
        mEntity.setName(name);
    }

    @Override
    public Uri getImageRef() {
        return mEntity.getImageRef();
    }

    @Override
    public void setImageRef(final String ref) {
        mEntity.setImageRef(ref);
    }

    @Override
    public void setImageRef(final Uri ref) {
        mEntity.setImageRef(ref);
    }

    @Override
    public String getImageRefAsString() {
        return mEntity.getImageRefAsString();
    }

    @Override
    public String getSearchTerm() {
        return mEntity.getSearchTerm();
    }

    @Override
    public void setSearchTerm(final String newTerm) {
        mEntity.setSearchTerm(newTerm);
    }

    @Override
    public boolean addCollection(final Integer collectionId) {
        return mEntity.addCollection(collectionId);
    }

    @Override
    public ArrayList<Integer> getCollection(final Integer collectionId) {
        return mEntity.getCollection(collectionId);
    }

    @Override
    public boolean addItemToCollection(final Integer collectionId, final Integer itemId) {
        return mEntity.addItemToCollection(collectionId, itemId);
    }

    @Override
    public boolean removeItemFromCollection(final Integer collectionId, final Integer itemId) {
        return mEntity.removeItemFromCollection(collectionId, itemId);
    }

    @Override
    public boolean isItemCollected(final Integer collectionId, final Integer itemId) {
        return mEntity.isItemCollected(collectionId, itemId);
    }

    @Override
    public void clearCollection(final Integer collectionId) {
        mEntity.clearCollection(collectionId);
    }

    @Override
    public int getCollectionSize(final Integer collectionId) {
        return mEntity.getCollectionSize(collectionId);
    }

    @Override
    public boolean changeCollectionItemState(final int collectionId, final Integer itemId, final boolean collected) {
        return mEntity.changeCollectionItemState(collectionId, itemId, collected);
    }

    /// /</editor-fold>

    //</editor-fold>
}
