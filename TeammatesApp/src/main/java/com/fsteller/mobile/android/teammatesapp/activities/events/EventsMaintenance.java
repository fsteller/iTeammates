package com.fsteller.mobile.android.teammatesapp.activities.events;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.fsteller.mobile.android.teammatesapp.R;
import com.fsteller.mobile.android.teammatesapp.TC;
import com.fsteller.mobile.android.teammatesapp.activities.base.ActivityMaintenanceBase;
import com.fsteller.mobile.android.teammatesapp.model.EventsEntity;
import com.fsteller.mobile.android.teammatesapp.model.base.IEntity;
import com.fsteller.mobile.android.teammatesapp.model.base.IEventsEntity;

/**
 * Project: iTeammates
 * Subpackage: activities.events
 * <p/>
 * Description:
 * <p/>
 * Created by fhernandezs on 27/12/13 for iTeammates.
 */
public final class EventsMaintenance extends ActivityMaintenanceBase implements IEventsEntity, Button.OnClickListener {

    //<editor-fold desc="Constants">

    private static final int LAST_PAGE = 2;
    private static final int FIRST_PAGE = 0;
    private int currentPage = FIRST_PAGE;
    private static final String TAG = EventsMaintenance.class.getSimpleName();

    //</editor-fold>
    //<editor-fold desc="Variables">
    private static final Class pages[] = new Class[]{EventsMaintenancePage1.class, EventsMaintenancePage2.class, EventsMaintenancePage3.class};
    private IEventsEntity mEventEntity = null;
    private Button actionButton = null;

    //</editor-fold>
    //<editor-fold desc="Constructor">

    public EventsMaintenance() {
        super(new EventsEntity());
        mEventEntity = (IEventsEntity) mEntity;
    }

    //</editor-fold>

    //<editor-fold desc="Overridden">
    //<editor-fold desc="Activity">

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Intent mIntent = getIntent();
        final Bundle extras = mIntent != null && mIntent.hasExtra(TC.ENTITY.EXTRAS) ?
                mIntent.getBundleExtra(TC.ENTITY.EXTRAS) : savedInstanceState;

        this.setContentView(R.layout.activity_events_maintenance);
        this.mEntity.loadData(this, extras);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMaintenancePage(pages[currentPage], null, true);
        Log.d(TAG, "onResumed");
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.maintenance_event, menu);

        final MenuItem buttonNext = menu != null ? menu.findItem(R.id.action_next) : null;

        if (buttonNext != null) {
            actionButton = (Button) buttonNext.getActionView();
            if (actionButton != null) {
                actionButton.setOnClickListener(this);
                actionButton.setText(getResources().getString(R.string.action_next));
            } else return false;
        } else return false;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
            default:
                return true;
        }
    }

    @Override
    public void onBackPressed() {
        currentPage--;
        if (currentPage < FIRST_PAGE) {
            Log.d(TAG, "onBackPressed: Canceling events maintenance...");
            finalize(RESULT_CANCELED, null);
        } else if (currentPage <= LAST_PAGE) {
            Log.d(TAG, String.format("onBackPressed: rewarding to events maintenance page %s", currentPage));

            final Resources rs = getResources();
            actionButton.setText(currentPage == LAST_PAGE ?
                    rs.getString(R.string.action_finish) : rs.getString(R.string.action_next));
            loadMaintenancePage(pages[currentPage], null, true);
        }
    }

    //</editor-fold>
    //<editor-fold desc="ActivityMaintenanceBase">

    @Override
    protected boolean checkData(final IEntity entity) {
        return false;
    }

    //</editor-fold>
    //<editor-fold desc="Button.OnClickListener">

    @Override
    public void onClick(final View v) {
        final Resources rs = getResources();

        currentPage++;
        actionButton.setText(currentPage <= LAST_PAGE ?
                rs.getString(R.string.action_finish) : rs.getString(R.string.action_next));

        if (currentPage <= LAST_PAGE)
            // Load next page
            loadMaintenancePage(pages[currentPage], null, true);
        //else
        //Todo: check result and persist event
    }

    //</editor-fold>
    //<editor-fold desc="IEventsEntity">

    @Override
    public void setCallback(final Callback callback) {
        mEventEntity.setCallback(callback);
    }

    @Override
    public String getDescription() {
        return mEventEntity.getDescription();
    }

    @Override
    public void setDescription(final String description) {
        mEventEntity.setDescription(description);
    }

    @Override
    public int getCalendarId() {
        return mEventEntity.getCalendarId();
    }

    @Override
    public void setCalendarId(final int calendar) {
        mEventEntity.setCalendarId(calendar);
    }

    @Override
    public boolean setDateFrom(int year, int month, int day) {
        return mEventEntity.setDateFrom(year, month, day);
    }

    @Override
    public boolean setDateTo(int year, int month, int day) {
        return mEventEntity.setDateTo(year, month, day);
    }

    @Override
    public boolean setTimeFrom(int hour, int minutes) {
        return mEventEntity.setTimeFrom(hour, minutes);
    }

    @Override
    public boolean setTimeTo(int hour, int minutes) {
        return mEventEntity.setTimeTo(hour, minutes);
    }

    @Override
    public int getYearFrom() {
        return mEventEntity.getYearFrom();
    }

    @Override
    public int getYearTo() {
        return mEventEntity.getYearTo();
    }

    @Override
    public int getMonthFrom() {
        return mEventEntity.getMonthFrom();
    }

    @Override
    public int getMonthTo() {
        return mEventEntity.getMonthTo();
    }

    @Override
    public int getDayFrom() {
        return mEventEntity.getDayFrom();
    }

    @Override
    public int getDayTo() {
        return mEventEntity.getDayTo();
    }

    @Override
    public int getHourFrom() {
        return mEventEntity.getHourFrom();
    }

    @Override
    public int getHourTo() {
        return mEventEntity.getHourTo();
    }

    @Override
    public int getMinutesFrom() {
        return mEventEntity.getMinutesFrom();
    }

    @Override
    public int getMinutesTo() {
        return mEventEntity.getMinutesTo();
    }

    @Override
    public String getTimeZone() {
        return mEventEntity.getTimeZone();
    }

    @Override
    public void setTimeZone(final String timeZone) {
        mEventEntity.setTimeZone(timeZone);
    }

    //</editor-fold>
    //</editor-fold>
    //<editor-fold desc="Private">

    private void loadMaintenancePage(final Class fragmentClass, final Bundle params, final boolean retry) {
        new Thread() {
            @Override
            public void run() {
                Log.d(TAG, String.format("loadMaintenancePage, fragmentName: %s", fragmentClass.getSimpleName()));

                // Try to retrieve a previously logged session
                //loadSettings();
                try {
                    final Fragment fragment = Fragment.instantiate(EventsMaintenance.this, fragmentClass.getName(), params);
                    loadMaintenanceUI(fragment);
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage(), e);
                    if (retry)
                        loadMaintenancePage(EventsMaintenancePage1.class, params, false);
                }
            }
        }.start();
    }

    private void loadMaintenanceUI(final Fragment mFragment) {
        Log.d(TAG, String.format("loadMaintenanceUI, page[%s] fragmentId: %s", currentPage, mFragment.getId()));
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        final ActionBar ab = getActionBar();

        ft.replace(R.id.event_maintenance, mFragment, null);
        ft.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ft.commit();
                if (ab != null)
                    ab.setSubtitle(mEntity.getName());
            }
        });
    }

    //</editor-fold>

}