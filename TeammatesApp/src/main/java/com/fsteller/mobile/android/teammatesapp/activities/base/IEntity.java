package com.fsteller.mobile.android.teammatesapp.activities.base;

import android.net.Uri;

/**
 * Created by fhernandezs on 02/01/14 for iTeammates.
 */
public interface IEntity extends ICollection {

    public int getMaintenanceId();

    public String getEntityName();

    public void setEntityName(final String name);

    public String getDescription();

    public void setDescription(final String description);

    public Uri getImageRef();

    public String getImageRefAsString();

    public void setImageRef(final Uri ref);

    public void setImageRef(final String ref);


}
