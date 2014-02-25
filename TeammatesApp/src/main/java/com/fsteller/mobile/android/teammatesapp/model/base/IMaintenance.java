package com.fsteller.mobile.android.teammatesapp.model.base;

import android.content.Context;
import android.os.Bundle;

/**
 * Project: ${PROJECT_NAME}
 * Package: ${PACKAGE_NAME}
 * <p/>
 * Description:
 * Created by fhernandezs on 02/01/14 for iTeammates.
 */
public interface IMaintenance extends IEntity {

    public boolean isRequiredToBeSaved();

    Bundle getResult();

    void loadData(final Context context, final Bundle extras);

}
