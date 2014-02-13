package com.fsteller.mobile.android.teammatesapp.model;

import android.content.Context;
import android.os.Bundle;

/**
 * Created by fhernandezs on 02/01/14 for iTeammates.
 */
public interface IMaintenance extends IEntity {

    public boolean getIsRequiredToBeSaved();

    void loadData(final Context context, final Bundle extras);
}
