package com.fsteller.mobile.android.teammatesapp.model.base;

import android.net.Uri;

import com.fsteller.mobile.android.teammatesapp.model.Collection;

/**
 * Project: ${PROJECT_NAME}
 * Package: ${PACKAGE_NAME}
 * <p/>
 * Description:
 * Created by fhernandezs on 08/01/14 for iTeammates.
 */
public abstract class AbstractEntity extends Collection implements IEntity, IMaintenance {

    //<editor-fold desc="Variables">

    private int id;
    private String name = "";
    private String imageRef = "";
    private boolean mIsRequiredToBeSaved = false;
    private final Object mIsRequiredToBeSavedLock = new Object();

    //</editor-fold>
    //<editor-fold desc="Constructor">

    protected AbstractEntity(final Integer id) {
        this.id = id;
    }

    //</editor-fold>

    //<editor-fold desc="IEntity">

    public boolean isRequiredToBeSaved() {
        return mIsRequiredToBeSaved;
    }

    public int getEntityId() {
        return id;
    }

    public void setEntityId(int id) {
        this.id = id;
    }

    public String getEntityName() {
        return name;
    }

    public void setEntityName(final String name) {
        final String mName = name.trim();
        if (!this.name.equals(mName)) {
            this.name = mName;
            this.setIsRequiredToBeSaved(true);
        }
    }

    public Uri getImageRef() {
        return Uri.parse(this.imageRef);
    }

    public void setImageRef(final Uri ref) {
        setImageRef(ref != null ? ref.toString() : "");
    }

    public String getImageRefAsString() {
        return this.imageRef;
    }

    public void setImageRef(final String ref) {
        final String mRef = ref.trim();
        if (!this.imageRef.equals(mRef)) {
            this.imageRef = mRef;
            this.setIsRequiredToBeSaved(true);
        }
    }

    //</editor-fold>
    //<editor-fold desc="ICollection">

    @Override
    public boolean addCollection(final Integer collectionId) {
        final boolean result = super.addCollection(collectionId);
        if (result)
            setIsRequiredToBeSaved(true);
        return result;
    }

    @Override
    public void clearCollection(final Integer collectionId) {
        super.clearCollection(collectionId);
        setIsRequiredToBeSaved(true);
    }

    @Override
    public boolean addItemToCollection(final Integer collectionId, final Integer itemId) {
        final boolean result = super.addItemToCollection(collectionId, itemId);
        if (result)
            setIsRequiredToBeSaved(true);
        return result;
    }

    @Override
    public boolean removeItemFromCollection(final Integer collectionId, final Integer itemId) {
        final boolean result = super.removeItemFromCollection(collectionId, itemId);
        if (result)
            setIsRequiredToBeSaved(true);
        return result;
    }

    @Override
    public boolean changeCollectionItemState(int collectionId, Integer itemId, boolean collected) {
        final boolean result = super.changeCollectionItemState(collectionId, itemId, collected);
        if (result)
            setIsRequiredToBeSaved(true);
        return result;
    }

    //</editor-fold>
    //<editor-fold desc="Protected">
    protected void startTrackingChanges() {
        this.setIsRequiredToBeSaved(false);
    }

    protected void setIsRequiredToBeSaved(final boolean isRequiredToBeSaved) {
        synchronized (mIsRequiredToBeSavedLock) {
            this.mIsRequiredToBeSaved = isRequiredToBeSaved;
            mIsRequiredToBeSavedLock.notifyAll();
        }
    }
    //</editor-fold>
}
