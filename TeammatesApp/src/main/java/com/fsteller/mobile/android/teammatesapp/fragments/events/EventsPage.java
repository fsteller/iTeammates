package com.fsteller.mobile.android.teammatesapp.fragments.events;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fsteller.mobile.android.teammatesapp.R;
import com.fsteller.mobile.android.teammatesapp.fragments.base.FragmentPageBase;

/**
 * Created by fhernandezs on 24/12/13 for iTeammates.
 */
public class EventsPage extends FragmentPageBase {

    //<editor-fold desc="Constants">

    private static final int PAGE_INDEX = 0x0001;

    //</editor-fold>
    //<editor-fold desc="Constructor">

    public EventsPage() {
    }

    //</editor-fold>

    //<editor-fold desc="Overridden Methods">

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_page_events, container, false);
        if (rootView != null) {

        }
        return rootView;
    }

    //<editor-fold desc="FragmentPageBase Methods">

    @Override
    protected int getPageIndex() {
        return PAGE_INDEX;
    }

    //</editor-fold>
    //</editor-fold>
}
