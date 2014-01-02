package com.fsteller.mobile.android.teammatesapp.activities.base;

/**
 * Created by fhernandezs on 02/01/14 for iTeammates.
 */
public interface ICollection {

    public void clearItemCollection(final Integer collectionId);

    public boolean isItemCollected(final Integer collectionId, final Integer itemId);

    public void itemStateChanged(final Integer collectionId, final Integer itemId, final boolean checked);
}
