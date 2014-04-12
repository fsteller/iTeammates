package com.fsteller.mobile.android.teammatesapp.activities.base;

import android.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;

import com.fsteller.mobile.android.teammatesapp.image.Loader;
import com.fsteller.mobile.android.teammatesapp.image.Utils;

/**
 * Project: iTeammates
 * Subpackage: activities.base
 * <p/>
 * Description:
 * <p/>
 * Created by fhernandezs on 26/12/13 for iTeammates.
 */
public abstract class FragmentBase extends Fragment {

    //<editor-fold desc="Variables">

    protected final HideInputClass mHideInputClass = new HideInputClass();
    protected View mEmptyView = null;
    protected AbsListView mListView = null;
    protected InputMethodManager imm = null;
    protected Loader mLoader = null;

    //</editor-fold>

    //<editor-fold desc="Overridden">

    protected static boolean isNullOrEmpty(final CharSequence text) {
        return text != null && !String.valueOf(text).trim().isEmpty();
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (mLoader == null)
            mLoader = Utils.setupImageLoader(getActivity(), getFragmentDefaultImage());
    }

    //</editor-fold>
    //<editor-fold desc="Protected">

    @Override
    public void onPause() {
        super.onPause();
        /*
            In the case onPause() is called during a fling the image loader is
            un-paused to let any remaining background work complete.
        */
        if (mLoader != null)
            mLoader.setPauseWork(false);

    }

    protected abstract int getFragmentDefaultImage();

    protected void hideSoftKeyboard(final View view) {
        if (imm != null)
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
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
