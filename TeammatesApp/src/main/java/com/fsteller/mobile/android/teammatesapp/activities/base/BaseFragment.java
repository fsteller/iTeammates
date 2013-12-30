package com.fsteller.mobile.android.teammatesapp.activities.base;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.EditText;

import com.fsteller.mobile.android.teammatesapp.R;

/**
 * Created by fhernandezs on 26/12/13 for iTeammates.
 */
public abstract class BaseFragment extends Fragment implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    //<editor-fold desc="Constants">

    protected static final String TAG = BaseFragment.class.getSimpleName();
    protected static final String EMPTY_STRING = "";

    //</editor-fold>
    //<editor-fold desc="Variables">

    protected View mEmptyView = null;
    protected AbsListView mListView = null;
    protected InputMethodManager imm = null;
    protected ITeammatesCollection mCallback = null;
    protected String mSearchTerm = "";

    //</editor-fold>

    //<editor-fold desc="Overridden Methods">

    @Override
    public void onAttach(final Activity activity) {
        super.onAttach(activity);
        mCallback = ((ITeammatesCollection) activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    @Override
    public void onClick(final View v) {
        hideInputMethod(v);
    }

    @Override
    public void onLoadFinished(final Loader<Cursor> cursorLoader, final Cursor data) {
        if (mEmptyView != null)
            mEmptyView.setVisibility(data.getCount() > 0 ? View.GONE : View.VISIBLE);
    }

    //</editor-fold>
    //<editor-fold desc="Protected Methods">

    protected static boolean isNullOrEmpty(final CharSequence text) {
        return text == null || String.valueOf(text).trim().isEmpty();
    }

    protected static int getBackgroundResource(final boolean checked) {
        return checked ?
                R.drawable.holo_card_selected :
                R.drawable.holo_card_white;
    }

    //</editor-fold>
    //<editor-fold desc="private Methods">

    private void hideInputMethod(final View v) {
        if (imm != null && !(v instanceof EditText))
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    //</editor-fold>
}
