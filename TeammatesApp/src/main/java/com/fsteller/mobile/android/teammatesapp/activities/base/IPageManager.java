package com.fsteller.mobile.android.teammatesapp.activities.base;

import com.fsteller.mobile.android.teammatesapp.TC;
import com.fsteller.mobile.android.teammatesapp.model.base.ICollection;

/**
 * Project: iTeammates
 * Subpackage: activities.base
 * <p/>
 * Description:
 * Created by fhernandezs on 26/12/13 for iTeammates.
 */
public interface IPageManager extends ICollection, NavigationDrawerFragment.NavigationDrawerCallbacks {

    public static final Integer TEAMS_PAGE = TC.Activity.Maintenance.TEAMS;
    public static final Integer EVENTS_PAGE = TC.Activity.Maintenance.EVENTS;
    public static final Integer NOTIFICATIONS_PAGE = TC.Activity.Maintenance.NOTIFICATION;

    public void actionRequest(final int collectionId, final int requestCode);
}
