package com.fsteller.mobile.android.teammatesapp.activities.base;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

import com.fsteller.mobile.android.teammatesapp.TC;
import com.fsteller.mobile.android.teammatesapp.model.IEntity;

/**
 * Created by fhernandezs on 23/01/14.
 */
public abstract class FragmentMaintenancePageBase extends FragmentBase {

    //<editor-fold desc="Constants">

    private final static String TAG = FragmentMaintenancePageBase.class.getSimpleName();

    //</editor-fold>
    //<editor-fold desc="Variables">

    protected IEntity mCallback = null;

    //</editor-fold>

    //<editor-fold desc="Overridden">

    @Override
    public void onAttach(final Activity activity) {
        super.onAttach(activity);
        this.mCallback = (IEntity) activity;

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
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

    //<editor-fold desc="IMaintenancePage">

    //</editor-fold>
    //</editor-fold>
    //<editor-fold desc="Protected">

    protected void restartLoader(final int queryFilter, final LoaderManager.LoaderCallbacks callbacks) {
        final LoaderManager mLoaderManager = getLoaderManager();
        if (mLoaderManager != null)
            mLoaderManager.restartLoader(queryFilter, null, callbacks);
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
