package com.fsteller.mobile.android.teammatesapp.activities.base;

import android.net.Uri;

/**
 * Created by fhernandezs on 08/01/14 for iTeammates.
 */
public class Entity extends Collection implements IEntity {

    //<editor-fold desc="Constants">

    private static final String TAG = Entity.class.getSimpleName();

    //</editor-fold>
    //<editor-fold desc="Variables">

    private int id;
    private String name = "";
    private String imageRef = "";
    private String description = "";
    private boolean isRequiredToBeSaved = false;

    //</editor-fold>

    //<editor-fold desc="IEntity">

    public boolean getIsRequiredToBeSaved() {
        return isRequiredToBeSaved;
    }

    public int getEntityId() {
        return id;
    }

    public void setEntityId(final Integer id) {
        this.id = id;
        this.isRequiredToBeSaved = true;
    }

    public String getEntityName() {
        return name;
    }

    public void setEntityName(final String name) {
        this.name = name.trim();
        this.isRequiredToBeSaved = true;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description.trim();
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
}
