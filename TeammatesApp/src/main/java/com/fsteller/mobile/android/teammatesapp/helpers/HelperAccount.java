package com.fsteller.mobile.android.teammatesapp.helpers;

import android.content.Context;

/**
 * Project: ${PROJECT_NAME}
 * Package: ${PACKAGE_NAME}
 * <p/>
 * Description:
 * Created by fhernandezs on 03/01/14 for iTeammates.
 */
public final class HelperAccount {

    private Context mContext = null;
    private static HelperAccount mHelperAccount = null;

    private HelperAccount(final Context context) {
        mContext = context;
    }

    public static HelperAccount getInstance(final Context context) {
        if (mHelperAccount != null)
            mHelperAccount = new HelperAccount(context);
        return mHelperAccount;
    }
}
