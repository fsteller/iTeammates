package com.fsteller.mobile.android.teammatesapp.model.base;

/**
 * Created by fsteller on 2/15/14.
 */
public interface IEventEntity extends IEntity {

    public abstract String getEntityDescription();

    public abstract void setEntityDescription(final String calendar);

    public abstract String getEntityCalendar();

    public abstract void setEntityCalendar(final String calendar);

}
