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
import com.fsteller.mobile.android.teammatesapp.model.EventEntity;
import com.fsteller.mobile.android.teammatesapp.model.IEntity;

/**
 * Created by fhernandezs on 27/12/13 for iTeammates.
 */
public final class EventsMaintenance extends ActivityMaintenanceBase implements Button.OnClickListener {

    //<editor-fold desc="Constants">

    private static final int LAST_PAGE = 3;
    private static final int FIRST_PAGE = 0;
    private static final int EVENTS = TC.Activity.Maintenance.EVENTS;
    private static final String TAG = EventsMaintenance.class.getSimpleName();
    private static final Class pages[] = new Class[]{EventsMaintenancePage1.class};

    //</editor-fold>
    //<editor-fold desc="Variables">

    private int lastPage = FIRST_PAGE;
    private int currentPage = FIRST_PAGE;

    //</editor-fold>
    //<editor-fold desc="Constructor">

    public EventsMaintenance() {
        super(new EventEntity());
    }

    //</editor-fold>

    //<editor-fold desc="Overridden">
    //<editor-fold desc="Activity">

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Intent mIntent = getIntent();
        final Bundle extras = mIntent != null && mIntent.hasExtra(TC.Activity.PARAMS.EXTRAS) ?
                mIntent.getBundleExtra(TC.Activity.PARAMS.EXTRAS) : savedInstanceState;

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

        final Resources rs = getResources();
        final MenuItem buttonNext = menu != null ? menu.findItem(R.id.action_next) : null;

        if (buttonNext != null) {
            Button actionButton = (Button) buttonNext.getActionView();
            if (actionButton != null) {
                actionButton.setOnClickListener(this);
                actionButton.setText(currentPage == LAST_PAGE ? rs.getString(R.string.action_finish) : rs.getString(R.string.action_next));
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
        lastPage = currentPage--;
        if (currentPage < FIRST_PAGE) {
            Log.d(TAG, "onBackPressed: Canceling events maintenance...");
            finalize(RESULT_CANCELED, null);
        } else {
            Log.d(TAG, String.format("onBackPressed: rewarding to events maintenance page %s", currentPage));
            loadMaintenancePage(pages[currentPage], null, true);
        }
    }

    //</editor-fold>
    //<editor-fold desc="ActivityMaintenanceBase">

    @Override
    protected Intent getResult() {
        return null;
    }

    @Override
    protected boolean checkData(IEntity entity) {
        return false;
    }

    //</editor-fold>
    //<editor-fold desc="Button.OnClickListener">

    @Override
    public void onClick(View v) {

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
                    ab.setSubtitle(mEntity.getEntityName());
            }
        });
    }

    //</editor-fold>

}