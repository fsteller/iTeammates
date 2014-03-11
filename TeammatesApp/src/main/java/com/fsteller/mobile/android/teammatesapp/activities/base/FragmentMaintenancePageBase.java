package com.fsteller.mobile.android.teammatesapp.activities.base;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Intent;

import com.fsteller.mobile.android.teammatesapp.TC;
import com.fsteller.mobile.android.teammatesapp.model.base.IEntity;

/**
 * Project: iTeammates
 * Subpackage: activities.base
 * <p/>
 * Description:
 * Created by fhernandezs on 23/01/14.
 */
public abstract class FragmentMaintenancePageBase extends FragmentBase {

    //<editor-fold desc="Constants">

    private final static String TAG = FragmentMaintenancePageBase.class.getSimpleName();

    //</editor-fold>
    //<editor-fold desc="Variables">

    protected IEntity mEntity = null;

    //</editor-fold>

    //<editor-fold desc="Overridden">

    @Override
    public void onAttach(final Activity activity) {
        super.onAttach(activity);
        this.mEntity = (IEntity) activity;

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mEntity = null;
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent result) {
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

}
