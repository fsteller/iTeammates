package com.fsteller.mobile.android.teammatesapp.helpers.databse;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by fhernandezs on 26/12/13 for iTeammates.
 */
 interface Entity {

    public Cursor loadAll();

    public long insert(final ContentValues values);

    public int update(final int[] ids, final ContentValues values);

    public ContentValues getData(final int[] ids);
}
