package com.fsteller.mobile.android.teammatesapp.model.base;

import android.net.Uri;

import com.fsteller.mobile.android.teammatesapp.model.Collection;

/**
 * Created by fhernandezs on 08/01/14 for iTeammates.
 */
public abstract class AbstractEntity extends Collection implements IEntity, IMaintenance {

    //<editor-fold desc="Constants">

    private static final String TAG = AbstractEntity.class.getSimpleName();

    //</editor-fold>
    //<editor-fold desc="Variables">

    private int id;
    private String name = "";
    private String imageRef = "";
    private boolean isRequiredToBeSaved = false;

    //</editor-fold>
    //<editor-fold desc="Constructor">

    protected AbstractEntity(final Integer id) {
        this.id = id;
    }

    //</editor-fold>

    //<editor-fold desc="IEntity">

    public boolean isRequiredToBeSaved() {
        return isRequiredToBeSaved;
    }

    public int getEntityId() {
        return id;
    }

    public String getEntityName() {
        return name;
    }

    public void setEntityName(final String name) {
        this.name = name.trim();
        this.isRequiredToBeSaved = true;
    }

    public Uri getImageRef() {
        return Uri.parse(this.imageRef);
    }

    public String getImageRefAsString() {
        return this.imageRef;
    }

    public void setImageRef(final Uri ref) {
        this.imageRef = ref.toString();
        this.isRequiredToBeSaved = true;
    }

    public void setImageRef(final String ref) {
        this.imageRef = ref.trim();
        this.isRequiredToBeSaved = true;
    }

    //</editor-fold>

    protected void requiredToBeSaved() {
        isRequiredToBeSaved = true;
    }
}
