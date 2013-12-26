package com.fsteller.mobile.android.teammatesapp;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fsteller.mobile.android.teammatesapp.fragments.base.NavigationDrawerFragment;
import com.fsteller.mobile.android.teammatesapp.fragments.events.EventsPage;
import com.fsteller.mobile.android.teammatesapp.fragments.notification.NotificationsPage;
import com.fsteller.mobile.android.teammatesapp.fragments.teams.TeamsPage;

public class Teammates extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    //<editor-fold desc="Constants">

    private static final Class[] activities = new Class[]{EventsPage.class, TeamsPage.class, NotificationsPage.class};

    //</editor-fold>
    //<editor-fold desc="Variables">

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;


    //</editor-fold>

    //<editor-fold desc="Overridden Methods">

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teammates);

        mTitle = getTitle();
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(final int position) {
        // update the main content by replacing fragments

        try {

            final Fragment mFragment = (Fragment) activities[position + 1].newInstance();
            final FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
            fragmentTransaction.replace(R.id.container, mFragment).commit();

        } catch (Exception e) {
            e.printStackTrace();
        }

        //final FragmentManager fragmentManager = getFragmentManager();
        //fragmentManager.beginTransaction().replace(R.id.container,PlaceholderFragment.newInstance(position + 1)).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.teammates, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //</editor-fold>
    //<editor-fold desc="Public Methods">

    public void onSectionAttached(final int number) {
        mTitle = getResources().getStringArray(R.array.app_activities)[number - 1];
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setTitle(mTitle);
        }
    }

    //</editor-fold>
}
