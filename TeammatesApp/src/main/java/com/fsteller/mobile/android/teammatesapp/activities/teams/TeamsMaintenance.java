package com.fsteller.mobile.android.teammatesapp.activities.teams;

import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.fsteller.mobile.android.teammatesapp.R;
import com.fsteller.mobile.android.teammatesapp.activities.base.BaseActivity;
import com.fsteller.mobile.android.teammatesapp.activities.base.TC;
import com.fsteller.mobile.android.teammatesapp.helpers.database.TeammatesContract;
import com.fsteller.mobile.android.teammatesapp.utils.Adapters;
import com.fsteller.mobile.android.teammatesapp.utils.Image.ImageLoader;
import com.fsteller.mobile.android.teammatesapp.utils.Image.ImageUtils;

import java.util.ArrayList;

/**
 * Created by fhernandezs on 27/12/13 for iTeammates.
 */
public final class TeamsMaintenance extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor>, AbsListView.OnScrollListener {

    //<editor-fold desc="Constants">

    private static final String TAG = TeamsMaintenance.class.getSimpleName();

    //</editor-fold>
    //<editor-fold desc="Variables">

    private String imageRef = "";
    private String teamName = "";
    private String mSearchTerm = "";
    private ArrayList<Integer> checkedContacts = new ArrayList<Integer>();
    private ContactsAdapter contactsAdapter = null;
    private ImageLoader mImageLoader = null;
    private EditText collectionName = null;
    private EditText collectionKey = null;
    private ImageView headerImage = null;
    private ListView mListView = null;

    //</editor-fold>
    //<editor-fold desc="Constructor">
    public TeamsMaintenance() {

    }
    //</editor-fold>

    //<editor-fold desc="Overridden Methods">

    //<editor-fold desc="BaseActivity">
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Intent mIntent = getIntent();
        final Bundle extras = mIntent != null && mIntent.hasCategory(TC.Activity.PARAMS.ID) ?
                mIntent.getBundleExtra(TC.Activity.PARAMS.ID) : savedInstanceState;

        this.setIsKeyBoardEnabled(false);
        this.setContentView(R.layout.activity_teams_maintenance);

        this.mImageLoader = ImageUtils.setupImageLoader(this, R.drawable.ic_default_picture);
        this.collectionName = (EditText) findViewById(R.id.collection_title_label);
        this.headerImage = (ImageView) findViewById(R.id.header_image);
        this.mListView = (ListView) findViewById(R.id.list_view);

        this.loadData(extras);
        this.restartLoader(TC.Queries.ContactsQuery.SIMPLE_QUERY_ID);
    }

    @Override
    protected void onResume() {
        super.onResume();

        this.restartLoader(TC.Queries.ContactsQuery.SIMPLE_QUERY_ID);
        this.mImageLoader.loadImage(imageRef, headerImage);
        this.collectionKey.setText(mSearchTerm);
        this.collectionName.setText(teamName);
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {

        if (outState != null) {
            outState.putString(TC.Activity.PARAMS.COLLECTION_ID, teamName);
            outState.putString(TC.Activity.PARAMS.COLLECTION_IMAGE_REF, imageRef);
            outState.putIntegerArrayList(TC.Activity.PARAMS.COLLECTION_ITEMS, checkedContacts);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(final Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            teamName = savedInstanceState.getString(TC.Activity.PARAMS.COLLECTION_ID);
            imageRef = savedInstanceState.getString(TC.Activity.PARAMS.COLLECTION_IMAGE_REF);
            checkedContacts = savedInstanceState.getIntegerArrayList(TC.Activity.PARAMS.COLLECTION_ITEMS);
        }
    }

    //</editor-fold>
    //<editor-fold desc="LoaderManager.LoaderCallbacks<Cursor>">

    @Override
    public Loader<Cursor> onCreateLoader(final int id, final Bundle bundle) {
        switch (id) {
            case TC.Queries.ContactsQuery.SIMPLE_QUERY_ID:
                return getContacts();
            case TC.Queries.ContactsQuery.FILTER_QUERY_ID1:
                return getContactsFilteredBySearchTerm(mSearchTerm);
            default:
                Log.e(TAG, String.format("OnCreateLoader - Unknown id provided (%s)", id));
                return null;
        }
    }

    @Override
    public void onLoadFinished(final Loader<Cursor> cursorLoader, final Cursor data) {

        contactsAdapter.swapCursor(null);
        switch (cursorLoader.getId()) {
            case TC.Queries.ContactsQuery.SIMPLE_QUERY_ID:
            case TC.Queries.ContactsQuery.FILTER_QUERY_ID1:
                contactsAdapter.swapCursor(data);
        }
    }

    @Override
    public void onLoaderReset(final Loader<Cursor> cursorLoader) {
        switch (cursorLoader.getId()) {
            case TC.Queries.ContactsQuery.SIMPLE_QUERY_ID:
            case TC.Queries.ContactsQuery.FILTER_QUERY_ID1:
                contactsAdapter.swapCursor(null);
        }
    }

    //</editor-fold>
    //<editor-fold desc="AbsListView.OnScrollListener">

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    //</editor-fold>
    //</editor-fold>

    private void loadData(final Bundle extras) {
        if (extras != null)
            new Thread() {
                @Override
                public void run() {
                    //Loads data form a database stored team
                    Log.i(TAG, "Loading team data...");
                    final int teamId = extras.getInt(TC.Activity.PARAMS.ID, -1);

                    if (teamId > 0) {
                        final String[] projection = TC.Queries.TeamsQuery.TEAMS_CONTACT_PROJECTION;
                        final Uri teamsContactsUri = TeammatesContract.Teams.Contacts.getTeamContactUri(teamId);
                        final Cursor data = getContentResolver().query(teamsContactsUri, projection, null, null, null);
                        if (data != null) {
                            try {
                                data.moveToFirst();
                                teamName = data.getString(TC.Queries.TeamsQuery.NAME);
                                imageRef = data.getString(TC.Queries.TeamsQuery.IMAGE_REF);
                                Log.i(TAG, String.format("Loading '%s' contacts...", teamName));
                                checkedContacts.add(data.getInt(TC.Queries.TeamsQuery.CONTACT_TOKEN));
                                while (data.moveToNext())
                                    checkedContacts.add(data.getInt(TC.Queries.TeamsQuery.CONTACT_TOKEN));
                            } catch (Exception e) {
                                Log.e(TAG, e.getMessage(), e);
                                e.printStackTrace();
                            } finally {
                                data.close();
                            }
                        }
                    }

                    contactsAdapter = new ContactsAdapter(TeamsMaintenance.this);
                    mListView.setOnScrollListener(TeamsMaintenance.this);
                    mListView.setAdapter(contactsAdapter);

                }
            }.start();
    }

    private void restartLoader(final int queryId) {
        getLoaderManager().restartLoader(queryId, null, this);
    }

    private Loader<Cursor> getContacts() {

        final Uri contentUri = TC.Queries.ContactsQuery.CONTENT_URI;
        final String selection = TC.Queries.ContactsQuery.SELECTION;
        final String sortOrder = TC.Queries.ContactsQuery.SORT_ORDER;
        final String[] projection = TC.Queries.ContactsQuery.PROJECTION;

        return new CursorLoader(this, contentUri, projection, selection, null, sortOrder);
    }

    private Loader<Cursor> getContactsFilteredBySearchTerm(final String searchTerm) {

        final String sortOrder = TC.Queries.ContactsQuery.SORT_ORDER;
        final String[] projection = TC.Queries.ContactsQuery.PROJECTION;
        final Uri contentUri = Uri.withAppendedPath(TC.Queries.ContactsQuery.FILTER_URI, Uri.encode(searchTerm));

        String selection = TC.Queries.ContactsQuery.SELECTION;
        String[] params = new String[checkedContacts.size()];
        for (int i = 0; i < checkedContacts.size(); i++) {
            selection += String.format("AND %s <> ?", projection[0]);
            params[i] = checkedContacts.get(i).toString();
        }

        return new CursorLoader(this, contentUri, projection, selection, params, sortOrder);
    }

    private final class ContactsAdapter extends Adapters.TeammatesSimpleCursorAdapter implements CompoundButton.OnCheckedChangeListener {

        public ContactsAdapter(final Context context) {
            super(context,
                    TC.Queries.ContactsQuery.SORT_KEY,
                    R.layout.listview_item_contact,
                    TC.Queries.ContactsQuery.PROJECTION,
                    new int[]{
                            R.id.contact_label,
                            R.id.contact_phone,
                            R.id.contact_image,
                            R.id.contact_check
                    }
            );
        }

        @Override
        protected View setupView(final View view) {

            final CheckBox checkBox = (CheckBox) view.findViewById(R.id.contact_check);
            if (checkBox != null)
                checkBox.setOnCheckedChangeListener(this);

            return view;
        }

        @Override
        public boolean setViewValue(final View view, final Cursor cursor, final int columnIndex) {

            switch (view.getId()) {
                case R.id.contact_label:
                    ((TextView) view).setText(cursor.getString(TC.Queries.ContactsQuery.DISPLAY_NAME));
                    break;
                case R.id.contact_phone:
                    ((TextView) view).setText(getPhoneNumber(cursor.getInt(TC.Queries.CalendarQuery.ID)));
                    break;
                case R.id.contact_image:
                    setImageView((ImageView) view, mImageLoader, cursor.getString(TC.Queries.ContactsQuery.PHOTO_THUMBNAIL_DATA));
                    break;
                case R.id.contact_check:
                    final int id = cursor.getInt(TC.Queries.CalendarQuery.ID);
                    ((CheckBox) view).setChecked(checkedContacts.contains(id));
                    view.setTag(id);
                    break;
            }

            return false;
        }

        @Override
        public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {

        }

        private String getPhoneNumber(final Integer id) {
            String output = "";
            final ContentResolver cr = getContentResolver();
            if (cr != null) {
                final Cursor pCur = cr.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        new String[]{id.toString()}, null
                );

                if (pCur != null) {
                    pCur.moveToFirst();
                    output = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    while (pCur.moveToNext()) {
                        output += ", " + pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    }
                }

            }
            return output;
        }
    }

}
