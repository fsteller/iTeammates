package com.fsteller.mobile.android.teammatesapp.activities.base;

import android.app.LoaderManager;
import android.database.Cursor;
import android.widget.AbsListView;

/**
 * Created by fhernandezs on 02/01/14 for iTeammates.
 */
public interface IMaintenance extends LoaderManager.LoaderCallbacks<Cursor>, AbsListView.OnScrollListener {
}
