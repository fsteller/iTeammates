package com.fsteller.mobile.android.teammatesapp.activities.teams;

import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fsteller.mobile.android.teammatesapp.R;
import com.fsteller.mobile.android.teammatesapp.TC;
import com.fsteller.mobile.android.teammatesapp.activities.base.ActivityMaintenanceBase;
import com.fsteller.mobile.android.teammatesapp.activities.base.IMaintenance;
import com.fsteller.mobile.android.teammatesapp.helpers.database.TeammatesContract;
import com.fsteller.mobile.android.teammatesapp.utils.Adapters;
import com.fsteller.mobile.android.teammatesapp.utils.Image.ImageUtils;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by fhernandezs on 27/12/13 for iTeammates.
 */
public final class TeamsMaintenance extends ActivityMaintenanceBase {

    //<editor-fold desc="Constants">

    private static final int INDEX = TC.Activity.Maintenance.TEAMS;
    private static final String TAG = TeamsMaintenance.class.getSimpleName();

    //</editor-fold>
    //<editor-fold desc="Variables">

    private String mSearchTerm = "";
    private EditText collectionName = null;
    private EditText collectionKey = null;
    private ImageView headerImage = null;
    private ListView mListView = null;

    //</editor-fold>
    //<editor-fold desc="Constructor">
    public TeamsMaintenance() {
        super(INDEX);
    }
    //</editor-fold>

    //<editor-fold desc="Overridden Methods">

    //<editor-fold desc="Activity">

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Intent mIntent = getIntent();
        final Bundle extras = mIntent != null && mIntent.hasCategory(TC.Activity.PARAMS.ID) ?
                mIntent.getBundleExtra(TC.Activity.PARAMS.ID) : savedInstanceState;

        this.setIsKeyBoardEnabled(false);
        this.setContentView(R.layout.activity_teams_maintenance);

        this.mImageLoader = ImageUtils.setupImageLoader(this, R.drawable.ic_default_picture);
        this.collectionKey = (EditText) findViewById(R.id.collection_lookup_key_text);
        this.collectionName = (EditText) findViewById(R.id.collection_title_text);
        this.headerImage = (ImageView) findViewById(R.id.header_image);
        this.mListView = (ListView) findViewById(R.id.list_view);

        this.collectionKey.addTextChangedListener(this);

        final TextView headerTitle = (TextView) findViewById(R.id.title_label);
        final ImageButton button = (ImageButton) findViewById(R.id.header_button);
        final TextView headerDescription = (TextView) findViewById(R.id.title_description_label);
        headerTitle.setText(getResources().getString(R.string.teamsMaintenace_titleLabel));
        headerDescription.setText(getResources().getString(R.string.teamsMaintenace_titleDescriptionLabel));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Raising intent to pick image up...");
                final Intent intent = new Intent(Intent.ACTION_GET_CONTENT,
                        android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);

                intent.setType("image/*");
                //intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(Intent.createChooser
                        (intent, getString(R.string.selectPicture)), TC.Activity.ActivityActionRequest.PickImage);
            }
        });

        this.loadData(extras);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.maintenance_team, menu);

        final MenuItem buttonItem = menu.findItem(R.id.action_finish);

        if (buttonItem != null) {
            final Button buttonView = (Button) buttonItem.getActionView();
            if (buttonView != null) {
                buttonView.setOnClickListener(this);
                buttonView.setText(getResources().getString(R.string.action_finish));
            }
        }

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        this.restartLoader(TC.Queries.ContactsQuery.SIMPLE_QUERY_ID);
        this.mImageLoader.loadImage(getImageRef(), headerImage);
        this.collectionName.setText(getEntityName());
        this.collectionKey.setText(mSearchTerm);
    }

    @Override
    public void onBackPressed() {
        finalize(RESULT_CANCELED, null);
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {

        if (outState != null) {
            outState.putString(TC.Activity.PARAMS.COLLECTION_NAME, getEntityName());
            outState.putString(TC.Activity.PARAMS.COLLECTION_IMAGE_REF, getImageStringRef());
            outState.putIntegerArrayList(TC.Activity.PARAMS.COLLECTION_ITEMS, getCollection(getMaintenanceId()));
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(final Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            final int id = getMaintenanceId();
            final ArrayList<Integer> temp = savedInstanceState.getIntegerArrayList(TC.Activity.PARAMS.COLLECTION_ITEMS);
            setEntityName(savedInstanceState.getString(TC.Activity.PARAMS.COLLECTION_NAME));
            setImageRef(savedInstanceState.getString(TC.Activity.PARAMS.COLLECTION_IMAGE_REF));

            addCollection(id);
            if (temp != null)
                for (final int i : temp)
                    addItemToCollection(id, i);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.mSearchTerm = null;
        this.collectionName = null;
        this.collectionKey = null;
        this.headerImage = null;
        this.mListView = null;
    }

    //</editor-fold>
    //<editor-fold desc="TextWatcher">

    @Override
    public void afterTextChanged(final Editable s) {
        mSearchTerm = s.toString().trim();
        restartLoader(mSearchTerm.isEmpty() ?
                TC.Queries.ContactsQuery.SIMPLE_QUERY_ID :
                TC.Queries.ContactsQuery.FILTER_QUERY_ID1
        );
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

        if (mCursorAdapter != null) {
            mCursorAdapter.swapCursor(null);
            switch (cursorLoader.getId()) {
                case TC.Queries.ContactsQuery.SIMPLE_QUERY_ID:
                case TC.Queries.ContactsQuery.FILTER_QUERY_ID1:
                    mCursorAdapter.swapCursor(data);
            }
        }
    }

    @Override
    public void onLoaderReset(final Loader<Cursor> cursorLoader) {

        if (cursorLoader != null)
            switch (cursorLoader.getId()) {
                case TC.Queries.ContactsQuery.SIMPLE_QUERY_ID:
                case TC.Queries.ContactsQuery.FILTER_QUERY_ID1:
                    mCursorAdapter.swapCursor(null);
            }
    }

    //</editor-fold>
    //<editor-fold desc="Button.OnClickListener">

    @Override
    public void onClick(final View v) {
        if (checkData(this))
            finalize(RESULT_OK, getResult());
    }

    //</editor-fold>

    @Override
    protected Intent getResult() {

        final Intent result = new Intent();
        final Bundle extras = new Bundle();

        extras.putInt(TC.Activity.PARAMS.ID, getMaintenanceId());
        extras.putString(TC.Activity.PARAMS.COLLECTION_NAME, getEntityName());
        extras.putString(TC.Activity.PARAMS.COLLECTION_IMAGE_REF, getImageStringRef());
        extras.putIntegerArrayList(TC.Activity.PARAMS.COLLECTION_ITEMS, getCollection(getMaintenanceId()));
        extras.putLong(TC.Activity.PARAMS.COLLECTION_CREATE_DATE, Calendar.getInstance().getTimeInMillis());
        result.putExtra(TC.Activity.PARAMS.ID, extras);

        return result;
    }

    @Override
    protected boolean checkData(final IMaintenance entity) {

        setEntityName(collectionName.getText().toString());
        if (isNullOrEmpty(entity.getEntityName())) {
            showMessage(getResources().getString(R.string.no_entity_name), Toast.LENGTH_SHORT);
            return false;
        }

        if (getCollectionSize(getMaintenanceId()) < 1) {
            showMessage(getResources().getString(R.string.no_selected_contacts), Toast.LENGTH_SHORT);
            return false;
        }
        return true;
    }

    @Override
    public void CollectionItemStateChanged(final Integer collectionId, final Integer itemId, final boolean checked) {
        final boolean isCollected = isItemCollected(collectionId, itemId);
        if (checked && !isCollected)
            addItemToCollection(collectionId, itemId);
        else if (isCollected)
            removeItemFromCollection(collectionId, itemId);

    }

    //</editor-fold>
    //<editor-fold desc="Private Methods">

    private void loadData(final Bundle extras) {

        new Thread() {
            @Override
            public void run() {
                //Loads data form a database stored team
                Log.i(TAG, "Loading team data...");
                final int teamId = extras != null ? extras.getInt(TC.Activity.PARAMS.ID, -1) : -1;

                if (teamId > 0) {
                    final String[] projection = TC.Queries.TeamsQuery.TEAMS_CONTACT_PROJECTION;
                    final Uri teamsContactsUri = TeammatesContract.Teams.Contacts.getTeamContactUri(teamId);
                    final Cursor data = getContentResolver().query(teamsContactsUri, projection, null, null, null);
                    if (data != null) {
                        try {
                            data.moveToFirst();
                            setEntityName(data.getString(TC.Queries.TeamsQuery.NAME));
                            setImageRef(data.getString(TC.Queries.TeamsQuery.IMAGE_REF));

                            Log.i(TAG, String.format("Loading '%s' contacts...", getEntityName()));
                            addItemToCollection(getMaintenanceId(), data.getInt(TC.Queries.TeamsQuery.CONTACT_TOKEN));
                            while (data.moveToNext())
                                addItemToCollection(getMaintenanceId(), data.getInt(TC.Queries.TeamsQuery.CONTACT_TOKEN));
                        } catch (Exception e) {
                            Log.e(TAG, e.getMessage(), e);
                            e.printStackTrace();
                        } finally {
                            data.close();
                        }
                    }
                }

                mCursorAdapter = new ContactsAdapter(TeamsMaintenance.this);
                mListView.setOnScrollListener(TeamsMaintenance.this);
                mListView.setAdapter(mCursorAdapter);
            }
        }.start();
    }

    private Loader<Cursor> getContacts() {

        final Uri contentUri = TC.Queries.ContactsQuery.CONTENT_URI;
        final String selection = TC.Queries.ContactsQuery.SELECTION;
        final String sortOrder = TC.Queries.ContactsQuery.SORT_ORDER;
        final String[] projection = TC.Queries.ContactsQuery.PROJECTION;

        return new CursorLoader(this, contentUri, projection, selection, null, sortOrder);
    }

    private Loader<Cursor> getContactsFilteredBySearchTerm(final String searchTerm) {

        final int id = getMaintenanceId();
        final int size = getCollectionSize(id);
        final String[] params = new String[size];
        final String sortOrder = TC.Queries.ContactsQuery.SORT_ORDER;
        final String[] projection = TC.Queries.ContactsQuery.PROJECTION;
        final Uri contentUri = Uri.withAppendedPath(TC.Queries.ContactsQuery.FILTER_URI, Uri.encode(searchTerm));

        int counter = 0;
        String selection = TC.Queries.ContactsQuery.SELECTION;
        for (final Integer i : getCollection(id)) {
            selection += String.format("AND %s <> ?", projection[1]);
            params[counter++] = i.toString();
        }

        return new CursorLoader(this, contentUri, projection, selection, params, sortOrder);
    }

    //</editor-fold>

    //<editor-fold desc="Inner Class">

    private final class ContactsAdapter extends Adapters.CursorAdapter implements CompoundButton.OnCheckedChangeListener {

        public ContactsAdapter(final Context context) {
            super(context,
                    TC.Queries.ContactsQuery.SORT_KEY,
                    R.layout.listview_item_contact,
                    TC.Queries.ContactsQuery.PROJECTION,
                    new int[]{
                            R.id.contact_name,
                            R.id.contact_status,
                            R.id.contact_image,
                            R.id.contact_check
                    }
            );
        }

        @Override
        protected View setupView(final View view) {
            final ContactItem mContactItem = new ContactItem();
            mContactItem.contact_name = (TextView) view.findViewById(R.id.contact_name);
            mContactItem.contact_phone = (TextView) view.findViewById(R.id.contact_status);
            mContactItem.contact_thumbnail = (ImageView) view.findViewById(R.id.contact_image);
            mContactItem.check = (CheckBox) view.findViewById(R.id.contact_check);
            mContactItem.check.setOnCheckedChangeListener(this);
            view.setTag(mContactItem);
            return view;
        }

        @Override
        public void bindView(final View view, final Context context, final Cursor cursor) {

            final int id = cursor.getInt(TC.Queries.ContactsQuery.LOOKUP_KEY);
            final ContactItem mContactItem = (ContactItem) view.getTag();

            setBasicText(mContactItem.contact_phone, cursor.getString(TC.Queries.ContactsQuery.CONTACT_STAUS));
            setHighlightedText(mContactItem.contact_name, cursor.getString(TC.Queries.ContactsQuery.CONTACT_NAME), mSearchTerm);
            setImageView(mContactItem.contact_thumbnail, mImageLoader, cursor.getString(TC.Queries.ContactsQuery.CONTACT_PHOTO_DATA));
            mContactItem.check.setChecked(isItemCollected(getMaintenanceId(), id));
            mContactItem.check.setTag(id);
        }

        @Override
        public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
            final Integer id = (Integer) buttonView.getTag();
            CollectionItemStateChanged(getMaintenanceId(), id, isChecked);
        }

        private final class ContactItem {

            ImageView contact_thumbnail = null;
            TextView contact_phone = null;
            TextView contact_name = null;
            CheckBox check = null;
        }
    }

    //</editor-fold>
}

