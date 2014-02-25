package com.fsteller.mobile.android.teammatesapp.helpers.database;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Project: ${PROJECT_NAME}
 * Package: ${PACKAGE_NAME}
 * <p/>
 * Description:
 * Created by fhernandezs on 26/12/13 for iTeammates.
 */
interface IDbEntity {
    public Cursor loadAll();

    public long insert(final ContentValues values);

    public int update(final int[] ids, final ContentValues values);

    public ContentValues getData(final int[] ids);
}
