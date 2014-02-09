package com.fsteller.mobile.android.teammatesapp.activities.base;

import android.app.Fragment;
import android.app.LoaderManager;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;

import com.fsteller.mobile.android.teammatesapp.utils.Image.ImageLoader;
import com.fsteller.mobile.android.teammatesapp.utils.Image.ImageUtils;

/**
 * Created by fhernandezs on 26/12/13 for iTeammates.
 */
public abstract class FragmentBase extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    //<editor-fold desc="Constants">

    protected static final String TAG = FragmentBase.class.getSimpleName();
    protected static final String EMPTY_STRING = "";

    //</editor-fold>
    //<editor-fold desc="Variables">

    protected View mEmptyView = null;
    protected AbsListView mListView = null;
    protected InputMethodManager imm = null;
    protected static ImageLoader mImageLoader = null;

    protected final HideInputClass mHideInputClass = new HideInputClass();
    protected String mSearchTerm = "";

    //</editor-fold>

    //<editor-fold desc="Overridden">

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (mImageLoader == null)
            mImageLoader = ImageUtils.setupImageLoader(getActivity(), getFragmentDefaultImage());
    }

    @Override
    public void onPause() {
        super.onPause();
        /*
            In the case onPause() is called during a fling the image loader is
            un-paused to let any remaining background work complete.
        */
        if (mImageLoader != null)
            mImageLoader.setPauseWork(false);

    }

    //</editor-fold>
    //<editor-fold desc="Protected Methods">

    protected abstract int getFragmentDefaultImage();

    protected void hideSoftKeyboard(final View view) {
        if (imm != null)
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    protected static boolean isNullOrEmpty(final CharSequence text) {
        return text == null || String.valueOf(text).trim().isEmpty();
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
