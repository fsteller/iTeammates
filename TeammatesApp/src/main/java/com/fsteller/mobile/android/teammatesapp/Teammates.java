package com.fsteller.mobile.android.teammatesapp;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.fsteller.mobile.android.teammatesapp.activities.base.ActivityBase;
import com.fsteller.mobile.android.teammatesapp.activities.base.IPageManager;
import com.fsteller.mobile.android.teammatesapp.activities.base.NavigationDrawerFragment;
import com.fsteller.mobile.android.teammatesapp.activities.dialogs.DialogFragment_About;
import com.fsteller.mobile.android.teammatesapp.activities.dialogs.DialogFragment_Login;
import com.fsteller.mobile.android.teammatesapp.activities.dialogs.DialogFragment_Settings;
import com.fsteller.mobile.android.teammatesapp.activities.dialogs.DialogFragment_Share;
import com.fsteller.mobile.android.teammatesapp.activities.events.EventsMaintenance;
import com.fsteller.mobile.android.teammatesapp.activities.events.EventsPage;
import com.fsteller.mobile.android.teammatesapp.activities.notifications.Notifications;
import com.fsteller.mobile.android.teammatesapp.activities.notifications.NotificationsPage;
import com.fsteller.mobile.android.teammatesapp.activities.teams.TeamsMaintenance;
import com.fsteller.mobile.android.teammatesapp.activities.teams.TeamsPage;
import com.fsteller.mobile.android.teammatesapp.model.Collection;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;

import java.util.ArrayList;

import static com.google.android.gms.common.GooglePlayServicesUtil.isGooglePlayServicesAvailable;

public final class Teammates extends ActivityBase implements
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,
        IPageManager {

    //<editor-fold desc="Constants">

    private static final String TAG = Teammates.class.getSimpleName();
    private static final Class[] childActivities = new Class[]{EventsMaintenance.class, TeamsMaintenance.class, Notifications.class};
    private static final Class[] mFragmentPages = new Class[]{EventsPage.class, TeamsPage.class, NotificationsPage.class};
    private static final Class[] childDialogs = new Class[]{DialogFragment_Settings.class, DialogFragment_Login.class, DialogFragment_Share.class, DialogFragment_About.class};

    private static final String STATE_RESOLVING_ERROR = "resolving_error";

    //</editor-fold>
    //<editor-fold desc="Variables">
    // Request code to use when launching the resolution activity
    private static final int REQUEST_RESOLVE_ERROR = 1001;
    // Unique tag for the error dialog fragment
    private static final String DIALOG_ERROR = "dialog_error";
    private final Collection mCollection = new Collection();
    // Client used to interact with Google APIs
    private GoogleApiClient mGoogleApiClient;
    // Bool to track whether the app is already resolving an error
    private boolean mResolvingError = false;

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    private CharSequence mTitle;
    private int currentPage = 0;

    //</editor-fold>

    //<editor-fold desc="Overridden">

    //<editor-fold desc="Activity">

    private static int getMaintenanceId(int pageId) {
        switch (pageId) {
            case 0:
                return TC.Activity.Maintenance.EVENTS;
            case 1:
                return TC.Activity.Maintenance.TEAMS;
            case 2:
                return TC.Activity.Maintenance.NOTIFICATION;
            default:
                return -1;
        }
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
                request == TC.Activity.Dialog.Settings ?
                        Dialogs.Settings : request == TC.Activity.Dialog.SignIn ?
                        Dialogs.Login : request == TC.Activity.Dialog.Share ?
                        Dialogs.Share : request == TC.Activity.Dialog.About ?
                        Dialogs.About : -1;

        if (dialog != -1) {
            try {
                final FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
                final DialogFragment fd = (DialogFragment) childDialogs[dialog].newInstance();
                ft.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
                fd.show(ft, "dialog");
                return true;
            } catch (Exception e) {
                Log.e(TAG, "Error: " + e.getMessage(), e);
            }
        }
        return false;
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_teammates);

        // Set up application header
        this.mTitle = getTitle();
        this.mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);

        // Set up the drawer.
        this.mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        this.mResolvingError = savedInstanceState != null
                && savedInstanceState.getBoolean(STATE_RESOLVING_ERROR, false);

        this.mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API, null)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .build();
    }

    @Override
    protected void onResume() {
        super.onResume();

        final int result = isGooglePlayServicesAvailable(this);
        if (result != ConnectionResult.SUCCESS) {
            GooglePlayServicesUtil.
                    getErrorDialog(result, this, TC.MediaStore.GOOGLEPLAYSERVICES_ERROR).show();
        }
    }

    @Override
    protected void onStop() {

        if (mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();

        super.onStop();
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

    //</editor-fold>
    //<editor-fold desc="GooglePlayServicesClient.ConnectionCallbacks">

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_RESOLVING_ERROR, mResolvingError);
    }

    //</editor-fold>
    //<editor-fold desc="GooglePlayServicesClient.OnConnectionFailedListener">

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        return processMenuRequest(item.getItemId());
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        clearCollection(getMaintenanceId(currentPage));

        if (requestCode == REQUEST_RESOLVE_ERROR) {
            mResolvingError = false;
            if (resultCode == RESULT_OK)
                // Make sure the app is not already connected or attempting to connect
                if (!mGoogleApiClient.isConnecting() && !mGoogleApiClient.isConnected())
                    mGoogleApiClient.connect();
        }

        if (data != null && resultCode != RESULT_CANCELED)
            resolveDataAction(requestCode, data);

    }

    //</editor-fold>
    //<editor-fold desc="IPageManager">

    @Override
    public void onConnected(final Bundle bundle) {
        mResolvingError = false;
        showMessage("Connected!", Toast.LENGTH_SHORT);
    }

    @Override
    public void onConnectionSuspended(final int i) {
        mGoogleApiClient.connect();
    }

    //<editor-fold desc="ICollection">

    @Override
    public void onConnectionFailed(final ConnectionResult result) {
        if (!mResolvingError) {
            if (result.hasResolution()) {
                try {
                    mResolvingError = true;
                    result.startResolutionForResult(this, REQUEST_RESOLVE_ERROR);
                } catch (final IntentSender.SendIntentException e) {

                    e.printStackTrace();
                    Log.e(TAG, "There was an error with the resolution intent. Try again.");

                    mGoogleApiClient.connect();
                }
            } else {
                // Show dialog using GooglePlayServicesUtil.getErrorDialog()
                showErrorDialog(result.getErrorCode());
                mResolvingError = true;
            }
        }
    }

    @Override
    public void actionRequest(final int collectionId, final int requestCode) {
        if (contextActionRequest(collectionId, requestCode)) {
            Log.i(TAG, String.format("Action with code %s has been succeeded.", requestCode));
        }
    }

    @Override
    public String getSearchTerm() {
        return mCollection.getSearchTerm();
    }

    @Override
    public void setSearchTerm(final String newTerm) {
        mCollection.setSearchTerm(newTerm);
    }

    @Override
    public boolean addCollection(final Integer collectionId) {
        return mCollection.addCollection(collectionId);
    }

    @Override
    public boolean isItemCollected(final Integer collectionId, final Integer itemId) {
        return mCollection.isItemCollected(collectionId, itemId);
    }

    @Override
    public int getCollectionSize(final Integer collectionId) {
        return mCollection.getCollectionSize(collectionId);
    }

    @Override
    public ArrayList<Integer> getCollection(final Integer collectionId) {
        return mCollection.getCollection(collectionId);
    }

    @Override
    public void clearCollection(final Integer collectionId) {
        mCollection.clearCollection(collectionId);
    }

    //</editor-fold>
    //<editor-fold desc="NavigationDrawerFragment.NavigationDrawerCallbacks Methods">

    @Override
    public boolean addItemToCollection(final Integer collectionId, final Integer itemId) {
        return mCollection.addItemToCollection(collectionId, itemId);
    }

    @Override
    public boolean removeItemFromCollection(final Integer collectionId, final Integer itemId) {
        return mCollection.removeItemFromCollection(collectionId, itemId);
    }

    //</editor-fold>
    //</editor-fold>

    //</editor-fold>
    //<editor-fold desc="Private">

    @Override
    public boolean changeCollectionItemState(final int collectionId, final Integer itemId, final boolean collected) {
        return mCollection.changeCollectionItemState(collectionId, itemId, collected);
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
        } catch (final Exception e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }
    }

    private void onSectionAttached(final int position) {
        mTitle = getResources().getStringArray(R.array.app_activities)[position];
    }

    private void restoreActionBar() {
        final ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setTitle(mTitle);
        }
    }

    private boolean loginToPlus() {
        if (!mGoogleApiClient.isConnected())
            mGoogleApiClient.connect();

        return mGoogleApiClient.isConnecting();
    }

    /* Creates a dialog for an error message */
    private void showErrorDialog(final int errorCode) {
        // Create a fragment for the error dialog
        final ErrorDialogFragment dialogFragment = new ErrorDialogFragment();
        // Pass the error that should be displayed
        final Bundle args = new Bundle();
        args.putInt(DIALOG_ERROR, errorCode);
        dialogFragment.setArguments(args);
        dialogFragment.show(getFragmentManager(), "errordialog");
    }

    /* Called from ErrorDialogFragment when the dialog is dismissed. */
    private void onDialogDismissed() {
        mResolvingError = false;
    }

    private Intent getActivityParams(final int collectionId, final ArrayList<Integer> dataIds) {
        final Bundle extras = new Bundle();

        extras.putInt(TC.ENTITY.COLLECTION_ID, collectionId);
        //extras.putString(TC.ENTITY.ACCOUNT_ID, accountId);
        extras.putIntegerArrayList(TC.ENTITY.COLLECTION_ITEMS, dataIds);

        final Intent intent = new Intent(this, childActivities[currentPage]);
        intent.putExtra(TC.ENTITY.EXTRAS, extras);

        return intent;
    }

    private boolean contextActionRequest(final int collectionId, final int requestCode) {

        final ArrayList<Integer> ids = getCollection(collectionId);
        Log.d(TAG, String.format("Action with code %s on page %s was called for " +
                "one o mere items collection with id %s", requestCode, currentPage, collectionId));

        if (requestCode == TC.Activity.ContextActionRequest.Delete)
            return resolveDataAction(TC.Activity.DataActionRequest.Delete, getActivityParams(collectionId, ids));

        if (requestCode == TC.Activity.ContextActionRequest.Edit)
            return invokeAction(this, TC.Activity.ContextActionRequest.Edit, getActivityParams(collectionId, ids));

        return requestCode == TC.Activity.ContextActionRequest.Share && invokeDialog(this, TC.Activity.ContextActionRequest.Share);
    }

    private boolean resolveDataAction(final int requestCode, final Intent data) {

        switch (requestCode) {
            case TC.Activity.DataActionRequest.Add:
                return app.addData(data);
            case TC.Activity.DataActionRequest.Update:
                return app.updateData(data);
            case TC.Activity.DataActionRequest.Delete:
                return app.deleteData(data);
            default:
                Log.e(TAG, String.format("Error at resolveDataAction: no action taken for intent(%s).", data));
                return false;
        }
    }

    private boolean processMenuRequest(final int menuItemId) {

        final int requestCode = menuItemId == R.id.action_addItem ?
                TC.Activity.MenuActionRequest.AddItem : menuItemId == R.id.action_login ?
                TC.Activity.MenuActionRequest.Login : menuItemId == R.id.action_settings ?
                TC.Activity.MenuActionRequest.Settings : menuItemId == R.id.action_about ?
                TC.Activity.MenuActionRequest.About : -1;

        if (requestCode == TC.Activity.MenuActionRequest.Login)
            return loginToPlus();

        if (requestCode == TC.Activity.MenuActionRequest.AddItem)
            return invokeAction(this, requestCode, getActivityParams(getMaintenanceId(currentPage), null));


        return (requestCode == TC.Activity.MenuActionRequest.About ||
                requestCode == TC.Activity.MenuActionRequest.Settings) &&
                invokeDialog(this, requestCode);
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
    //<editor-fold desc="Inner Classes">

    /* A fragment to display an error dialog */
    private class ErrorDialogFragment extends DialogFragment {
        public ErrorDialogFragment() {
        }

        @Override
        public Dialog onCreateDialog(final Bundle savedInstanceState) {
            // Get the error code and retrieve the appropriate dialog
            final int errorCode = this.getArguments().getInt(DIALOG_ERROR);
            return GooglePlayServicesUtil.getErrorDialog(errorCode, getActivity(), REQUEST_RESOLVE_ERROR);
        }

        @Override
        public void onDismiss(final DialogInterface dialog) {
            Teammates.this.onDialogDismissed();
        }
    }

    //</editor-fold>
}
