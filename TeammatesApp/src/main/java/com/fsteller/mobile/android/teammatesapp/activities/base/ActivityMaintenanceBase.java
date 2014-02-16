package com.fsteller.mobile.android.teammatesapp.activities.base;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.fsteller.mobile.android.teammatesapp.TC;
import com.fsteller.mobile.android.teammatesapp.model.base.IEntity;
import com.fsteller.mobile.android.teammatesapp.model.base.IMaintenance;

import java.util.ArrayList;

/**
 * Created by fhernandezs on 02/01/14 for iTeammates.
 */
public abstract class ActivityMaintenanceBase extends ActivityBase implements IMaintenance {

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

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        /*
        if (outState != null) {
            outState.putString(TC.ENTITY.COLLECTION_NAME, mEntity.getEntityName());
            outState.putString(TC.ENTITY.COLLECTION_IMAGE_REF, mEntity.getImageRefAsString());
            outState.putIntegerArrayList(TC.ENTITY.COLLECTION_ITEMS, mEntity.getCollection(TeamsEntity.TEAMS));
        } */
        if (outState != null)
            outState.putBundle(TC.ENTITY_DATA, mEntity.getResult());

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(final Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            final Bundle mBundle = savedInstanceState.getBundle(TC.ENTITY_DATA);
            mEntity.loadData(this, mBundle);
        }

        /*
        if (savedInstanceState != null) {
            final ArrayList<Integer> temp = savedInstanceState.getIntegerArrayList(TC.ENTITY.COLLECTION_ITEMS);
            mEntity.setEntityName(savedInstanceState.getString(TC.ENTITY.COLLECTION_NAME));
            mEntity.setImageRef(savedInstanceState.getString(TC.ENTITY.COLLECTION_IMAGE_REF));

            mEntity.addCollection(TeamsEntity.TEAMS);
            if (temp != null)
                for (final int i : temp)
                    mEntity.addItemToCollection(TeamsEntity.TEAMS, i);
        }*/
    }

    //<editor-fold desc="IMaintenance">

    @Override
    public int getEntityId() {
        return mEntity.getEntityId();
    }

    @Override
    public void setEntityId(final int id) {
        mEntity.setEntityId(id);
    }

    @Override
    public String getEntityName() {
        return mEntity.getEntityName();
    }

    @Override
    public void setEntityName(final String name) {
        mEntity.setEntityName(name);
    }

    @Override
    public Uri getImageRef() {
        return mEntity.getImageRef();
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
    public void setImageRef(final String ref) {
        mEntity.setImageRef(ref);
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

    @Override
    public boolean isRequiredToBeSaved() {
        return mEntity.isRequiredToBeSaved();
    }

    @Override
    public Bundle getResult() {
        return mEntity.getResult();
    }

    @Override
    public void loadData(final Context context, final Bundle extras) {
        mEntity.loadData(context, extras);
    }

    /// /</editor-fold>

    //</editor-fold>
}
