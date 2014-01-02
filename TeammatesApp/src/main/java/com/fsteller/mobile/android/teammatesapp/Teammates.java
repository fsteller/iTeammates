package com.fsteller.mobile.android.teammatesapp;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.fsteller.mobile.android.teammatesapp.activities.base.ActivityBase;
import com.fsteller.mobile.android.teammatesapp.activities.base.IPageManager;
import com.fsteller.mobile.android.teammatesapp.activities.base.NavigationDrawerFragment;
import com.fsteller.mobile.android.teammatesapp.activities.base.TC;
import com.fsteller.mobile.android.teammatesapp.activities.dialogs.DialogFragment_About;
import com.fsteller.mobile.android.teammatesapp.activities.dialogs.DialogFragment_Login;
import com.fsteller.mobile.android.teammatesapp.activities.dialogs.DialogFragment_Settings;
import com.fsteller.mobile.android.teammatesapp.activities.dialogs.DialogFragment_Share;
import com.fsteller.mobile.android.teammatesapp.activities.events.Events;
import com.fsteller.mobile.android.teammatesapp.activities.events.EventsPage;
import com.fsteller.mobile.android.teammatesapp.activities.notification.Notifications;
import com.fsteller.mobile.android.teammatesapp.activities.notification.NotificationsPage;
import com.fsteller.mobile.android.teammatesapp.activities.teams.TeamsMaintenance;
import com.fsteller.mobile.android.teammatesapp.activities.teams.TeamsPage;

import java.util.ArrayList;

public class Teammates extends ActivityBase implements IPageManager {

    //<editor-fold desc="Constants">

    private static final String TAG = Teammates.class.getSimpleName();
    private static final Class[] childActivities = new Class[]{Events.class, TeamsMaintenance.class, Notifications.class};
    private static final Class[] mFragmentPages = new Class[]{EventsPage.class, TeamsPage.class, NotificationsPage.class};
    private static final Class[] childDialogs = new Class[]{DialogFragment_Settings.class, DialogFragment_Login.class, DialogFragment_Share.class, DialogFragment_About.class};

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


    private int currentPage = 0;
    private String accountId = null;

    //</editor-fold>

    //<editor-fold desc="Overridden Methods">

    //<editor-fold desc="Activity Methods">

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
        return processMenuRequest(item.getItemId());
    }
    //</editor-fold>

    //<editor-fold desc="IPageManager">

    @Override
    public void actionRequest(final int collectionId, int requestCode) {

    }

    //<editor-fold desc="ICollection">

    @Override
    public void clearItemCollection(final Integer CollectionId) {

    }

    @Override
    public boolean isItemCollected(final Integer CollectionId, final Integer itemId) {
        return false;
    }

    @Override
    public void itemStateChanged(final Integer CollectionId, final Integer itemId, final boolean checked) {

    }

    //</editor-fold>

    //</editor-fold>

    //<editor-fold desc="NavigationDrawerFragment.NavigationDrawerCallbacks Methods">

    @Override
    public void onNavigationDrawerItemSelected(final int position) {
        // update the main content by replacing fragments

        try {

            final Fragment mFragment = (Fragment) mFragmentPages[position].newInstance();
            final FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            //fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
            fragmentTransaction.replace(R.id.container, mFragment).commit();
            onSectionAttached(position);
            currentPage = position;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }

        //final FragmentManager fragmentManager = getFragmentManager();
        //fragmentManager.beginTransaction().replace(R.id.container,PlaceholderFragment.newInstance(position + 1)).commit();
    }

    @Override
    public void onNavigationDrawerActionCalled(final Actions action) {
        switch (action) {
            case Login:
                break;
            case Settings:
                break;
            default:
        }
    }

    //</editor-fold>

    //</editor-fold>
    //<editor-fold desc="Public Methods">

    public void onSectionAttached(final int position) {
        mTitle = getResources().getStringArray(R.array.app_activities)[position];
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
    //<editor-fold desc="Private Methods">
    private boolean processMenuRequest(final int menuItemId) {

        final int requestCode =
                menuItemId == R.id.action_login ?
                        TC.Activity.MenuRequest.Login : menuItemId == R.id.action_addItem ?
                        TC.Activity.MenuRequest.AddItem : menuItemId == R.id.action_settings ?
                        TC.Activity.MenuRequest.Settings : menuItemId == R.id.action_about ?
                        TC.Activity.MenuRequest.About : -1;

        if (requestCode == TC.Activity.MenuRequest.AddItem)
            return invokeAction(this, TC.Activity.MenuRequest.AddItem, getActivityParams(currentPage, accountId, null));

        return (requestCode == TC.Activity.MenuRequest.About ||
                requestCode == TC.Activity.MenuRequest.Login ||
                requestCode == TC.Activity.MenuRequest.Settings) &&
                invokeDialog(this, requestCode);
    }
    //</editor-fold>
    //<editor-fold desc="Static Methods">

    private Intent getActivityParams(final int currentPage, final String accountId, final ArrayList<Integer> dataIds) {
        final Bundle extras = new Bundle();

        extras.putString(TC.Activity.PARAMS.ACCOUNT_ID, accountId);
        extras.putIntegerArrayList(TC.Activity.PARAMS.COLLECTION_ID, dataIds);

        final Intent intent = new Intent(this, childActivities[currentPage]);
        intent.putExtra(TC.Activity.PARAMS.ID, extras);

        return intent;
    }

    private static boolean invokeAction(final Activity activity, final int requestCode, final Intent intent) {
        if (requestCode > 0) {
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            activity.startActivityForResult(intent, requestCode);
            return true;
        }
        return false;
    }

    private static boolean invokeDialog(final Activity activity, final int request) {
        final int dialog =
                request == TC.Activity.Dialog.Settings ? Dialogs.Settings :
                        request == TC.Activity.Dialog.SignIn ? Dialogs.Login :
                                request == TC.Activity.Dialog.Share ? Dialogs.Share :
                                        request == TC.Activity.Dialog.About ? Dialogs.About : -1;

        if (dialog != -1) {
            try {
                final FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
                final DialogFragment fd = (DialogFragment) childDialogs[dialog].newInstance();
                ft.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
                fd.show(ft, "");
                return true;
            } catch (Exception e) {
                Log.e(TAG, "Error: " + e.getMessage(), e);
            }
        }
        return false;
    }

    //</editor-fold>

    //<editor-fold desc="Interfaces">

    private static interface Dialogs {
        static final int Settings = 0;
        static final int Login = 1;
        static final int Share = 2;
        static final int About = 3;
    }

    //</editor-fold>
}
