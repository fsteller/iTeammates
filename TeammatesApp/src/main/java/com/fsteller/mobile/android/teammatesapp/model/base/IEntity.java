package com.fsteller.mobile.android.teammatesapp.model.base;

import android.net.Uri;

/**
 * Project: iTeammates
 * Subpackage: model.base
 * <p/>
 * Description:
 * <p/>
 * Created by fhernandezs on 02/01/14 for iTeammates.
 */
public interface IEntity extends ICollection {


    public int getId();

    public void setId(int id);

    public String getName();

    public void setName(final String name);

    public Uri getImageRef();

    public void setImageRef(final String ref);

    public void setImageRef(final Uri ref);

    public String getImageRefAsString();

}
