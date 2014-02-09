package com.fsteller.mobile.android.teammatesapp.activities.base;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;

import com.fsteller.mobile.android.teammatesapp.R;
import com.fsteller.mobile.android.teammatesapp.TC;
import com.fsteller.mobile.android.teammatesapp.utils.Image.ImageLoader;
import com.fsteller.mobile.android.teammatesapp.utils.Image.ImageUtils;

import java.util.List;

/**
 * Created by fhernandezs on 23/01/14.
 */
public abstract class FragmentMaintenancePageBase extends Fragment implements MaintenancePage {

    //<editor-fold desc="Constants">

    private final static String TAG = FragmentMaintenancePageBase.class.getSimpleName();

    //</editor-fold>
    //<editor-fold desc="Variables">
    private String mSearchTerm = null;

    protected View mEmptyView = null;
    protected AbsListView mListView = null;
    protected InputMethodManager imm = null;
    protected IMaintenance mCallback = null;

    protected final HideInputClass mHideInputClass = new HideInputClass();
    protected static ImageLoader mImageLoader = null;
    //</editor-fold>

    //<editor-fold desc="Overridden">

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    @Override
    public void onAttach(final Activity activity) {
        super.onAttach(activity);
        mCallback = ((IMaintenance) activity);
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (mImageLoader == null)
            mImageLoader = ImageUtils.setupImageLoader(getActivity(), R.drawable.ic_default_picture);
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null || resultCode == Activity.RESULT_CANCELED)
            return;

        if (requestCode == TC.Activity.ContextActionRequest.PickImage) {
            android.util.Log.i(TAG, String.format("Image picked up (%s)", data.getData()));
            final Uri imageUri = data.getData();
            if (imageUri != null) {
                mCallback.setImageRef(imageUri);
            }
        }
    }

    //<editor-fold desc="MaintenancePage">

    @Override
    public void setSearchTerm(final String newTerm) {
        this.mSearchTerm = newTerm;
    }

    @Override
    public String getSearchTerm() {
        return mSearchTerm;
    }

    //<editor-fold desc="ICollection">

    @Override
    public void addCollection(final Integer collectionId) {
        mCallback.addCollection(collectionId);
    }

    @Override
    public List<Integer> getCollection(final Integer collectionId) {
        return mCallback.getCollection(collectionId);
    }

    @Override
    public void clearCollection(final Integer collectionId) {
        mCallback.clearCollection(collectionId);
    }

    @Override
    public void addItemToCollection(final Integer collectionId, final Integer itemId) {
        mCallback.addItemToCollection(collectionId, itemId);
    }

    @Override
    public void removeItemFromCollection(final Integer collectionId, final Integer itemId) {
        mCallback.removeItemFromCollection(collectionId, itemId);
    }

    @Override
    public boolean isItemCollected(final Integer collectionId, final Integer itemId) {
        return mCallback.isItemCollected(collectionId, itemId);
    }

    @Override
    public int getCollectionSize(final Integer collectionId) {
        return mCallback.getCollectionSize(collectionId);
    }

    @Override
    public void CollectionItemStateChanged(final Integer collectionId, final Integer itemId, final boolean checked) {
        mCallback.CollectionItemStateChanged(collectionId, itemId, checked);
    }

    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //<editor-fold desc="Protected">

    protected void hideSoftKeyboard(final View view) {
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    protected static boolean isNullOrEmpty(String txt) {
        return txt == null || txt.isEmpty();
    }


    //</editor-fold>

    //<editor-fold desc="Inner Classes">

    private final class HideInputClass implements View.OnClickListener {

        @Override
        public void onClick(final View v) {
            hideSoftKeyboard(v);
        }
    }

    //</editor-fold>

}
