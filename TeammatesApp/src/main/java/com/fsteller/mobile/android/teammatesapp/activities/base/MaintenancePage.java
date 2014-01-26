package com.fsteller.mobile.android.teammatesapp.activities.base;

/**
 * Created by fhernandezs on 23/01/14.
 */
public interface MaintenancePage extends ICollection {

    public int getPageIndex();

    public void setSearchTerm(final String newTerm);

    public String getSearchTerm();
}
