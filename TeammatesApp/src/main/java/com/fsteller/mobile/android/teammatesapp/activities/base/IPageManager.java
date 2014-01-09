package com.fsteller.mobile.android.teammatesapp.activities.base;

/**
 * Created by fhernandezs on 26/12/13 for iTeammates.
 */
public interface IPageManager extends ICollection, NavigationDrawerFragment.NavigationDrawerCallbacks {

    public static final Integer EVETS_PAGE = 0x0001;
    public static final Integer TEAMS_PAGE = 0x0002;
    public static final Integer COLLECTIONS_PAGE = 0x0003;

    public void actionRequest(final int collectionId, final int requestCode);
}
