package com.fsteller.mobile.android.teammatesapp.model.base;

import java.util.ArrayList;

/**
 * Project: ${PROJECT_NAME}
 * Package: ${PACKAGE_NAME}
 * <p/>
 * Description:
 * Created by fhernandezs on 02/01/14 for iTeammates.
 */
public interface ICollection {

    public void setSearchTerm(final String newTerm);

    public String getSearchTerm();

    public boolean addCollection(final Integer collectionId);

    public ArrayList<Integer> getCollection(final Integer collectionId);

    public void clearCollection(final Integer collectionId);

    public boolean addItemToCollection(final Integer collectionId, final Integer itemId);

    public boolean removeItemFromCollection(final Integer collectionId, final Integer itemId);

    public boolean isItemCollected(final Integer collectionId, final Integer itemId);

    public int getCollectionSize(final Integer collectionId);

    public boolean changeCollectionItemState(final int collectionId, final Integer itemId, final boolean collected);

    public interface ItemStateChanged {
        public void onCollectionItemStateChanged(final Integer collectionId, final Integer itemId, final boolean collected);
    }

}
