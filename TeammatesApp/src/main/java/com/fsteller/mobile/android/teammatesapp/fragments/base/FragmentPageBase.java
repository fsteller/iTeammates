package com.fsteller.mobile.android.teammatesapp.fragments.base;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.fsteller.mobile.android.teammatesapp.Teammates;

/**
 * Created by fhernandezs on 24/12/13 for iTeammates.
 */
public abstract class FragmentPageBase extends Fragment {

    protected Teammates mTeammates = null;

    @Override
    public void onAttach(final Activity activity) {
        super.onAttach(activity);
        mTeammates = ((Teammates) activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mTeammates = null;
    }

    protected abstract int getPageIndex();
}
