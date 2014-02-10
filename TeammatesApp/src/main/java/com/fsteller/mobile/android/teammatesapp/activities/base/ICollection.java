package com.fsteller.mobile.android.teammatesapp.activities.base;

import java.util.List;

/**
 * Created by fhernandezs on 02/01/14 for iTeammates.
 */
public interface ICollection {

    public void setSearchTerm(final String newTerm);

    public String getSearchTerm();

    public void addCollection(final Integer collectionId);

    public List<Integer> getCollection(final Integer collectionId);

    public void clearCollection(final Integer collectionId);

    public void addItemToCollection(final Integer collectionId, final Integer itemId);

    public void removeItemFromCollection(final Integer collectionId, final Integer itemId);

    public boolean isItemCollected(final Integer collectionId, final Integer itemId);

    public int getCollectionSize(final Integer collectionId);

    public void changeCollectionItemState(final int collectionId, final Integer itemId, final boolean collected);

    public interface ItemStateChanged {
        public void onCollectionItemStateChanged(final Integer collectionId, final Integer itemId, final boolean collected);
    }

}
