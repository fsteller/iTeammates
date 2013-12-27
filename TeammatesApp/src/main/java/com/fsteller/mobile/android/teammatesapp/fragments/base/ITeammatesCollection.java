package com.fsteller.mobile.android.teammatesapp.fragments.base;

import android.app.Fragment;

import java.util.ArrayList;

/**
 * Created by fhernandezs on 26/12/13 for iTeammates.
 */
public interface ITeammatesCollection {
    public ArrayList<Integer> getCollectionItems();

    public boolean isItemCollected(final Integer itemId);

    public void actionRequest(final Fragment sender, final int requestCode);

    public void itemStateChanged(final Fragment sender, final Integer itemId, final boolean checked);

    public void clearItemCollection();
}
