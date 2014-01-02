package com.fsteller.mobile.android.teammatesapp.activities.base;

/**
 * Created by fhernandezs on 26/12/13 for iTeammates.
 */
public interface IPageManager extends ICollection, NavigationDrawerFragment.NavigationDrawerCallbacks {

    public void actionRequest(final int collectionId, final int requestCode);
}
