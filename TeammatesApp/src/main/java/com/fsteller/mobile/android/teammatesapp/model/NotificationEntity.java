package com.fsteller.mobile.android.teammatesapp.model;

import android.content.Context;
import android.os.Bundle;

import com.fsteller.mobile.android.teammatesapp.TC;
import com.fsteller.mobile.android.teammatesapp.model.base.AbstractEntity;
import com.fsteller.mobile.android.teammatesapp.model.base.IMaintenance;

/**
 * Project: ${PROJECT_NAME}
 * Package: ${PACKAGE_NAME}
 * <p/>
 * Description:
 * Created by fsteller on 2/13/14.
 */
public class NotificationEntity extends AbstractEntity implements IMaintenance {

    public static final int NOTIFICATION = TC.Activity.Maintenance.NOTIFICATION;

    private static final String TAG = TeamsEntity.class.getSimpleName();

    protected NotificationEntity() {
        super(NOTIFICATION);
    }

    @Override
    public Bundle getResult() {
        return null;
    }

    @Override
    public void loadData(Context context, Bundle extras) {

    }
}
