package com.fsteller.mobile.android.teammatesapp.model.base;

/**
 * Project: iTeammates
 * Subpackage: model.base
 * <p/>
 * Description: this interface represents iTeammates Team, it contains all the fields that a
 * {@link com.fsteller.mobile.android.teammatesapp.model.TeamsEntity} can handle.
 * <p/>
 * Created by fsteller on 2/15/14.
 */
public interface ITeamsEntity extends IEntity {

    public abstract String getDescription();

    public abstract void setDescription(final String description);

}
