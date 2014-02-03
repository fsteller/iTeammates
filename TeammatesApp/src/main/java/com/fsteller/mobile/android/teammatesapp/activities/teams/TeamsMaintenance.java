package com.fsteller.mobile.android.teammatesapp.activities.teams;

import android.app.LoaderManager;
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
import android.widget.AbsListView;
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
import com.fsteller.mobile.android.teammatesapp.utils.Text;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by fhernandezs on 27/12/13 for iTeammates.
 */
public final class TeamsMaintenance extends ActivityMaintenanceBase implements LoaderManager.LoaderCallbacks<Cursor>, AbsListView.OnScrollListener, Button.OnClickListener {

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
    private View emptyView = null;

    //</editor-fold>
    //<editor-fold desc="Constructor">

    public TeamsMaintenance() {
        super(INDEX);
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

        this.loadData(extras);
        this.setContentView(R.layout.activity_teams_maintenance);


        this.mImageLoader = ImageUtils.setupImageLoader(this, R.drawable.ic_default_picture);
        this.collectionKey = (EditText) findViewById(R.id.collection_lookup_key_text);
        this.collectionName = (EditText) findViewById(R.id.collection_title_text);
        this.headerImage = (ImageView) findViewById(R.id.header_image);
        this.mListView = (ListView) findViewById(R.id.list_view);
        this.collectionName.addTextChangedListener(new Text.AfterTextChangedWatcher() {
            @Override
            public void afterTextChanged(final Editable s) {
                setEntityName(s.toString());
            }
        });
        this.collectionKey.addTextChangedListener(new Text.AfterTextChangedWatcher() {
            @Override
            public void afterTextChanged(final Editable s) {
                mSearchTerm = s.toString().trim();
                restartLoader(mSearchTerm.isEmpty() ?
                        TC.Queries.PhoneContacts.SIMPLE_QUERY_ID :
                        TC.Queries.PhoneContacts.FILTER_QUERY_ID1,
                        TeamsMaintenance.this
                );
            }
        });

        final TextView headerTitle = (TextView) findViewById(R.id.title_label);
        final ImageButton button = (ImageButton) findViewById(R.id.header_button);
        final TextView headerDescription = (TextView) findViewById(R.id.title_description_label);

        button.setOnClickListener(mHideSoftInputClass);
        headerTitle.setOnClickListener(mHideSoftInputClass);
        headerDescription.setOnClickListener(mHideSoftInputClass);

        headerTitle.setText(getResources().getString(R.string.teamsMaintenance_titleLabel));
        headerDescription.setText(getResources().getString(R.string.teamsMaintenance_titleDescriptionLabel));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Raising intent to pick image up...");
                final Intent intent = new Intent(Intent.ACTION_GET_CONTENT,
                        android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);

                intent.setType("image/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                startActivityForResult(Intent.createChooser
                        (intent, getString(R.string.selectPicture)), TC.Activity.ContextActionRequest.PickImage);
            }
        });

        this.mListView.setOnScrollListener(this);
        this.mCursorAdapter = new ContactsAdapter(this);
        this.mListView.setAdapter(mCursorAdapter);
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
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        this.restartLoader(TC.Queries.PhoneContacts.SIMPLE_QUERY_ID, this);
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
            outState.putString(TC.Activity.PARAMS.COLLECTION_IMAGE_REF, getImageRefAsString());
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
    //<editor-fold desc="ActivityMaintenanceBase">

    @Override
    protected Intent getResult() {

        final Intent result = new Intent();
        final Bundle extras = new Bundle();

        extras.putInt(TC.Activity.PARAMS.ID, getEntityId());
        extras.putInt(TC.Activity.PARAMS.COLLECTION_ID, getMaintenanceId());
        extras.putString(TC.Activity.PARAMS.COLLECTION_NAME, getEntityName());
        extras.putString(TC.Activity.PARAMS.COLLECTION_IMAGE_REF, getImageRefAsString());
        extras.putIntegerArrayList(TC.Activity.PARAMS.COLLECTION_ITEMS, getCollection(getMaintenanceId()));
        extras.putLong(TC.Activity.PARAMS.COLLECTION_CREATE_DATE, Calendar.getInstance().getTimeInMillis());
        result.putExtra(TC.Activity.PARAMS.EXTRAS, extras);

        return result;
    }

    @Override
    protected boolean checkData(final IMaintenance entity) {
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
        else if (isCollected && !checked)
            removeItemFromCollection(collectionId, itemId);

    }

    //</editor-fold>
    //<editor-fold desc="LoaderManager.LoaderCallbacks<Cursor>">

    @Override
    public Loader<Cursor> onCreateLoader(final int id, final Bundle bundle) {
        switch (id) {
            case TC.Queries.PhoneContacts.SIMPLE_QUERY_ID:
                return getContacts();
            case TC.Queries.PhoneContacts.FILTER_QUERY_ID1:
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
                case TC.Queries.PhoneContacts.SIMPLE_QUERY_ID:
                case TC.Queries.PhoneContacts.FILTER_QUERY_ID1:
                    mCursorAdapter.swapCursor(data);
            }
            emptyView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoaderReset(final Loader<Cursor> cursorLoader) {

        if (cursorLoader != null && mCursorAdapter != null)
            switch (cursorLoader.getId()) {
                case TC.Queries.PhoneContacts.SIMPLE_QUERY_ID:
                case TC.Queries.PhoneContacts.FILTER_QUERY_ID1:
                    mCursorAdapter.swapCursor(null);
            }
    }

    //</editor-fold>
    //<editor-fold desc="AbsListView.OnScrollListener">

    @Override
    public void onScrollStateChanged(final AbsListView view, final int scrollState) {
        // Pause image loader to ensure smoother scrolling when flinging
        if (mImageLoader != null) {
            if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING)
                mImageLoader.setPauseWork(true);
            else
                mImageLoader.setPauseWork(false);
        }
        hideSoftKeyboard(view);
    }

    @Override
    public void onScroll(final AbsListView view, final int firstVisibleItem, final int visibleItemCount, final int totalItemCount) {

    }

    //</editor-fold>
    //<editor-fold desc="Button.OnClickListener">

    @Override
    public void onClick(final View v) {
        if (checkData(this))
            finalize(RESULT_OK, getIsRequiredToBeSaved() ? getResult() : null);
    }

    //</editor-fold>
    //</editor-fold>
    //<editor-fold desc="Private Methods">

    private void loadData(final Bundle extras) {

        if (extras == null)
            return;

        new Thread() {
            @Override
            public void run() {
                //Loads data form a database stored team
                Log.i(TAG, "Loading team data...");
                final ArrayList<Integer> teamsIds = extras.getIntegerArrayList(TC.Activity.PARAMS.COLLECTION_ITEMS);
                if (teamsIds != null && teamsIds.size() > 0) {

                    final int id = teamsIds.get(0);
                    final String[] projection = TC.Queries.TeammatesTeams.TEAM_CONTACT_PROJECTION;
                    final Uri teamsContactsUri = TeammatesContract.Teams.Contacts.getTeamContactUri(id);
                    final Cursor data = getContentResolver().query(teamsContactsUri, projection, null, null, null);

                    if (data != null) {
                        try {
                            data.moveToFirst();

                            setEntityId(id);
                            setEntityName(data.getString(TC.Queries.TeammatesTeams.NAME));
                            setImageRef(data.getString(TC.Queries.TeammatesTeams.IMAGE_REF));
                            Log.i(TAG, String.format("Loading '%s' contacts...", getEntityName()));
                            addItemToCollection(getMaintenanceId(), data.getInt(TC.Queries.TeammatesTeams.CONTACT_TOKEN));
                            while (data.moveToNext())
                                addItemToCollection(getMaintenanceId(), data.getInt(TC.Queries.TeammatesTeams.CONTACT_TOKEN));
                        } catch (Exception e) {
                            Log.e(TAG, e.getMessage(), e);
                            e.printStackTrace();
                        } finally {
                            data.close();
                        }
                    }
                }

            }
        }.start();
    }

    private Loader<Cursor> getContactsFilteredBySearchTerm(final String searchTerm) {

        final int id = getMaintenanceId();
        final int size = getCollectionSize(id);
        final String[] params = new String[size];
        final String sortOrder = TC.Queries.PhoneContacts.SORT_ORDER;
        final String[] projection = TC.Queries.PhoneContacts.PROJECTION;
        final Uri contentUri = Uri.withAppendedPath(TC.Queries.PhoneContacts.FILTER_URI, Uri.encode(searchTerm));

        int counter = 0;
        String selection = TC.Queries.PhoneContacts.SELECTION;
        for (final Integer i : getCollection(id)) {
            selection += String.format("AND %s <> ?", projection[0]);
            params[counter++] = i.toString();
        }

        return new CursorLoader(this, contentUri, projection, selection, params, sortOrder);
    }

    private Loader<Cursor> getContacts() {

        final Uri contentUri = TC.Queries.PhoneContacts.CONTENT_URI;
        final String selection = TC.Queries.PhoneContacts.SELECTION;
        final String sortOrder = TC.Queries.PhoneContacts.SORT_ORDER;
        final String[] projection = TC.Queries.PhoneContacts.PROJECTION;

        return new CursorLoader(this, contentUri, projection, selection, null, sortOrder);
    }

    //</editor-fold>

    //<editor-fold desc="Inner Class">

    private final class ContactsAdapter extends Adapters.CursorAdapter implements CompoundButton.OnCheckedChangeListener {

        public ContactsAdapter(final Context context) {
            super(context,
                    TC.Queries.PhoneContacts.SORT_KEY,
                    R.layout.listview_item_contact,
                    TC.Queries.PhoneContacts.PROJECTION,
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

            final int id = cursor.getInt(TC.Queries.PhoneContacts.ID);
            final ContactItem mContactItem = (ContactItem) view.getTag();

            mContactItem.check.setTag(id);
            mContactItem.check.setChecked(isItemCollected(getMaintenanceId(), id));
            setBasicText(mContactItem.contact_phone, cursor.getString(TC.Queries.PhoneContacts.CONTACT_STATUS));
            setHighlightedText(mContactItem.contact_name, cursor.getString(TC.Queries.PhoneContacts.CONTACT_NAME), mSearchTerm);
            setImageView(mContactItem.contact_thumbnail, mImageLoader, cursor.getString(TC.Queries.PhoneContacts.CONTACT_PHOTO_DATA));
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

