package com.fsteller.mobile.android.teammatesapp.activities.base;

import com.fsteller.mobile.android.teammatesapp.TC;

/**
 * Created by fhernandezs on 26/12/13 for iTeammates.
 */
public interface IPageManager extends ICollection, NavigationDrawerFragment.NavigationDrawerCallbacks {

    public static final Integer TEAMS_PAGE = TC.Activity.Maintenance.TEAMS;
    public static final Integer EVETS_PAGE = TC.Activity.Maintenance.EVENTS;
    public static final Integer NOTIFICATIONS_PAGE = TC.Activity.Maintenance.NOTIFICATION;

    public void actionRequest(final int collectionId, final int requestCode);
}
