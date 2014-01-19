package com.fsteller.mobile.android.teammatesapp.activities.base;

import android.util.Log;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fhernandezs on 08/01/14 for iTeammates.
 */
public abstract class ActivityCollection extends ActivityBase implements ICollection {

    private static final String TAG = ActivityCollection.class.getSimpleName();
    private SparseArray<ArrayList<Integer>> collections = new SparseArray<ArrayList<Integer>>();

    //<editor-fold desc="ICollection">

    public void addCollection(final Integer collectionId) {
        Log.i(TAG, String.format("addCollection: id=%s", collectionId));
        if (collections.get(collectionId) == null)
            collections.append(collectionId, new ArrayList<Integer>());
    }

    public ArrayList<Integer> getCollection(final Integer collectionId) {
        Log.i(TAG, String.format("getCollection: id=%s", collectionId));
        return collections.get(collectionId);
    }

    @Override
    public void clearCollection(final Integer collectionId) {
        Log.i(TAG, String.format("clearCollection: id=%s", collectionId));
        if (collections.get(collectionId) != null)
            collections.get(collectionId).clear();
    }

    @Override
    public void addItemToCollection(final Integer collectionId, final Integer itemId) {
        Log.i(TAG, String.format("addItemToCollection: collectionId=%s itemId=%s", collectionId, itemId));
        final List<Integer> list = collections.get(collectionId);
        if (list != null && !list.contains(itemId))
            list.add(itemId);
    }

    @Override
    public void removeItemFromCollection(final Integer collectionId, final Integer itemId) {
        Log.i(TAG, String.format("removeItemFromCollection: collectionId=%s itemId=%s", collectionId, itemId));
        final List<Integer> list = collections.get(collectionId);
        if (list != null && list.contains(itemId))
            list.remove(itemId);
    }

    @Override
    public boolean isItemCollected(final Integer collectionId, final Integer itemId) {
        Log.i(TAG, String.format("isItemCollected: collectionId=%s itemId=%s", collectionId, itemId));
        final List<Integer> list = collections.get(collectionId);
        return list != null && list.contains(itemId);
    }

    @Override
    public int getCollectionSize(final Integer collectionId) {
        Log.i(TAG, String.format("getCollectionSize: collectionId=%s", collectionId));
        final List<Integer> list = collections.get(collectionId);
        return list != null ? list.size() : -1;
    }

    //</editor-fold>

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.collections.clear();
        this.collections = null;
    }
}
