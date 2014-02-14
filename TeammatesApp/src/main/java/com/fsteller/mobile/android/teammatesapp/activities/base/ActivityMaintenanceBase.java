package com.fsteller.mobile.android.teammatesapp.activities.base;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.fsteller.mobile.android.teammatesapp.TC;
import com.fsteller.mobile.android.teammatesapp.model.base.IEntity;
import com.fsteller.mobile.android.teammatesapp.model.base.IMaintenance;

import java.util.ArrayList;

/**
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

    public ActivityMaintenanceBase(final IMaintenance entity) {
        mEntity = entity;
    }

    //</editor-fold>

    //<editor-fold desc="Protected Methods">

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

    protected abstract boolean checkData(IEntity entity);

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
                mEntity.setImageRef(imageUri);
            }
        }
    }

    //<editor-fold desc="IEntity">

    @Override
    public int getEntityId() {
        return mEntity.getEntityId();
    }

    @Override
    public String getEntityName() {
        return mEntity.getEntityName();
    }

    @Override
    public void setEntityName(String name) {
        mEntity.setEntityName(name);
    }

    @Override
    public Uri getImageRef() {
        return mEntity.getImageRef();
    }

    @Override
    public String getImageRefAsString() {
        return mEntity.getImageRefAsString();
    }

    @Override
    public void setImageRef(Uri ref) {
        mEntity.setImageRef(ref);
    }

    @Override
    public void setImageRef(String ref) {
        mEntity.setImageRef(ref);
    }

    @Override
    public void setSearchTerm(String newTerm) {
        mEntity.setSearchTerm(newTerm);
    }

    @Override
    public String getSearchTerm() {
        return mEntity.getSearchTerm();
    }

    @Override
    public void addCollection(Integer collectionId) {
        mEntity.addCollection(collectionId);
    }

    @Override
    public ArrayList<Integer> getCollection(Integer collectionId) {
        return mEntity.getCollection(collectionId);
    }

    @Override
    public void clearCollection(Integer collectionId) {
        mEntity.clearCollection(collectionId);
    }

    @Override
    public void addItemToCollection(Integer collectionId, Integer itemId) {
        mEntity.addItemToCollection(collectionId, itemId);
    }

    @Override
    public void removeItemFromCollection(Integer collectionId, Integer itemId) {
        mEntity.removeItemFromCollection(collectionId, itemId);
    }

    @Override
    public boolean isItemCollected(Integer collectionId, Integer itemId) {
        return mEntity.isItemCollected(collectionId, itemId);
    }

    @Override
    public int getCollectionSize(Integer collectionId) {
        return mEntity.getCollectionSize(collectionId);
    }

    @Override
    public void changeCollectionItemState(int collectionId, Integer itemId, boolean collected) {
        mEntity.changeCollectionItemState(collectionId, itemId, collected);
    }
    //</editor-fold>

    //</editor-fold>
}
