package com.fsteller.mobile.android.teammatesapp.model;

import android.util.Log;
import android.util.SparseArray;

import com.fsteller.mobile.android.teammatesapp.model.base.ICollection;

import java.util.ArrayList;
import java.util.List;

/**
 * Project: ${PROJECT_NAME}
 * Subpackage: ${PACKAGE_NAME}
 * <p/>
 * Description:
 * Created by fsteller on 2/9/14.
 */
public class Collection implements ICollection {

    //<editor-fold desc="Constants">

    private static final String TAG = Collection.class.getSimpleName();

    //</editor-fold>
    //<editor-fold desc="Variables">
    private String mSearchTerm = "";
    private ItemStateChanged itemStateChangedListener = null;
    private final SparseArray<ArrayList<Integer>> collections = new SparseArray<ArrayList<Integer>>();

    //</editor-fold>

    //<editor-fold desc="ICollection">

    @Override
    public void setSearchTerm(String newTerm) {
        mSearchTerm = newTerm.trim();
    }

    @Override
    public String getSearchTerm() {
        return mSearchTerm;
    }

    @Override
    public boolean addCollection(final Integer collectionId) {
        Log.i(TAG, String.format("addCollection: id=%s", collectionId));
        final boolean needToChange = collections.get(collectionId) == null;
        if (needToChange)
            collections.append(collectionId, new ArrayList<Integer>());
        return needToChange;
    }

    @Override
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
    public boolean addItemToCollection(final Integer collectionId, final Integer itemId) {
        Log.i(TAG, String.format("addItemToCollection: collectionId=%s itemId=%s", collectionId, itemId));
        final List<Integer> list = collections.get(collectionId);
        final boolean needToChange = list != null && !list.contains(itemId);
        if (needToChange)
            list.add(itemId);
        return needToChange;
    }

    @Override
    public boolean removeItemFromCollection(final Integer collectionId, final Integer itemId) {
        Log.i(TAG, String.format("removeItemFromCollection: collectionId=%s itemId=%s", collectionId, itemId));
        final List<Integer> list = collections.get(collectionId);
        final boolean needToChange = list != null && list.contains(itemId);
        if (needToChange)
            list.remove(itemId);
        return needToChange;
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

    @Override
    public boolean changeCollectionItemState(int collectionId, Integer itemId, boolean collected) {

        final boolean isCollected = isItemCollected(collectionId, itemId);
        if (collected && !isCollected)
            addItemToCollection(collectionId, itemId);
        else if (!collected && isCollected)
            removeItemFromCollection(collectionId, itemId);

        if (itemStateChangedListener != null)
            itemStateChangedListener.onCollectionItemStateChanged(collectionId, itemId, collected);

        //return ((collected || isCollected) && !(collected && isCollected));
        return collected ^ isCollected;
    }

    public void setOnCollectionItemStateChanged(ItemStateChanged listener) {
        itemStateChangedListener = listener;
    }

    //</editor-fold>
}
