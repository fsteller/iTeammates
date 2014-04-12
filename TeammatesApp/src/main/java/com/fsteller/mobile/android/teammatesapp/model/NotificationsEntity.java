package com.fsteller.mobile.android.teammatesapp.model;

import android.content.Context;
import android.os.Bundle;

import com.fsteller.mobile.android.teammatesapp.TC;
import com.fsteller.mobile.android.teammatesapp.model.base.AbstractEntity;
import com.fsteller.mobile.android.teammatesapp.model.base.INotificationsEntity;

/**
 * Project: iTeammates
 * Package: model
 * <p/>
 * Description:
 * <p/>
 * Created by fsteller on 2/13/14.
 */
public class NotificationsEntity extends AbstractEntity implements INotificationsEntity {

    public static final int NOTIFICATION = TC.Activity.Maintenance.NOTIFICATION;
    private static final String TAG = NotificationsEntity.class.getSimpleName();

    protected NotificationsEntity() {
        super(NOTIFICATION);
    }

    @Override
    public Bundle getResult() {
        return null;
    }

    @Override
    public void loadData(final Context context, final Bundle extras) {

    }
}
