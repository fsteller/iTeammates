package com.fsteller.mobile.android.teammatesapp.model.base;

import android.net.Uri;

/**
 * Project: ${PROJECT_NAME}
 * Package: ${PACKAGE_NAME}
 * <p/>
 * Description:
 * Created by fhernandezs on 02/01/14 for iTeammates.
 */
public interface IEntity extends ICollection {


    public int getEntityId();

    public void setEntityId(int id);

    public String getEntityName();

    public void setEntityName(final String name);

    public Uri getImageRef();

    public void setImageRef(final Uri ref);

    public String getImageRefAsString();

    public void setImageRef(final String ref);

}
