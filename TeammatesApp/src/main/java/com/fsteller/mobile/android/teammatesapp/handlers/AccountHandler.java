package com.fsteller.mobile.android.teammatesapp.handlers;

import android.content.Context;

/**
 * Project: ${PROJECT_NAME}
 * Package: ${PACKAGE_NAME}
 * <p/>
 * Description:
 * Created by fhernandezs on 03/01/14 for iTeammates.
 */
public final class AccountHandler {

    private Context mContext = null;
    private static AccountHandler mHelperAccount = null;

    private AccountHandler(final Context context) {
        mContext = context;
    }

    public static AccountHandler getInstance(final Context context) {
        if (mHelperAccount != null)
            mHelperAccount = new AccountHandler(context);
        return mHelperAccount;
    }
}
