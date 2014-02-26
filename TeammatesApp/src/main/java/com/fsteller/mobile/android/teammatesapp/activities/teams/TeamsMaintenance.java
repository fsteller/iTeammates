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
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.fsteller.mobile.android.teammatesapp.R;
import com.fsteller.mobile.android.teammatesapp.TC;
import com.fsteller.mobile.android.teammatesapp.activities.base.ActivityMaintenanceBase;
import com.fsteller.mobile.android.teammatesapp.model.TeamsEntity;
import com.fsteller.mobile.android.teammatesapp.model.base.IEntity;
import com.fsteller.mobile.android.teammatesapp.model.base.ITeamEntity;
import com.fsteller.mobile.android.teammatesapp.utils.Adapters;
import com.fsteller.mobile.android.teammatesapp.utils.Text;
import com.fsteller.mobile.android.teammatesapp.utils.image.ImageLoader;
import com.fsteller.mobile.android.teammatesapp.utils.image.ImageUtils;

/**
 * Project: iTeammates
 * Subpackage: activities.teams
 * <p/>
 * Description:
 * Created by fhernandezs on 27/12/13 for iTeammates.
 */
public final class TeamsMaintenance extends ActivityMaintenanceBase implements ITeamEntity, LoaderManager.LoaderCallbacks<Cursor>, AbsListView.OnScrollListener, Button.OnClickListener {

    //<editor-fold desc="Constants">

    private static final String TAG = TeamsMaintenance.class.getSimpleName();

    //</editor-fold>
    //<editor-fold desc="Variables">

    private SimpleCursorAdapter mCursorAdapter = null;
    private ImageLoader mImageLoader = null;
    private EditText collectionName = null;
    private EditText collectionKey = null;
    private ImageView headerImage = null;
    private ListView mListView = null;
    private View mEmptyView = null;

    //</editor-fold>
    //<editor-fold desc="Constructor">

    public TeamsMaintenance() {
        super(new TeamsEntity());
        ITeamEntity mTeamsEntity = (ITeamEntity) mEntity;
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

        this.setContentView(R.layout.activity_teams_maintenance);

        this.mEntity.loadData(this, extras);
        this.mImageLoader = ImageUtils.setupImageLoader(this, R.drawable.ic_default_picture);
        this.collectionKey = (EditText) findViewById(R.id.collection_lookup_key_text);
        this.collectionName = (EditText) findViewById(R.id.collection_title_text);
        this.headerImage = (ImageView) findViewById(R.id.header_image);
        this.mListView = (ListView) findViewById(R.id.list_view);
        this.mEmptyView = findViewById(android.R.id.empty);

        this.collectionName.addTextChangedListener(new Text.AfterTextChangedWatcher() {
            @Override
            public void afterTextChanged(final Editable s) {
                mEntity.setEntityName(s.toString());
            }
        });
        this.collectionKey.addTextChangedListener(new Text.AfterTextChangedWatcher() {
            @Override
            public void afterTextChanged(final Editable s) {
                mEntity.setSearchTerm(s.toString());
                restartLoader(TC.Queries.PhoneContacts.FILTER_QUERY_ID1, TeamsMaintenance.this);
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
        this.mImageLoader.loadImage(mEntity.getImageRef(), headerImage);
        this.collectionName.setText(mEntity.getEntityName());
        this.collectionKey.setText(mEntity.getSearchTerm());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.collectionName = null;
        this.collectionKey = null;
        this.headerImage = null;
        this.mListView = null;
    }

    //</editor-fold>
    //<editor-fold desc="ActivityMaintenanceBase">

    @Override
    protected boolean checkData(final IEntity entity) {
        if (isNullOrEmpty(getEntityName())) {
            showMessage(getResources().getString(R.string.no_entity_name), Toast.LENGTH_SHORT);
            return false;
        }

        if (getCollectionSize(TeamsEntity.TEAMS) < 1) {
            showMessage(getResources().getString(R.string.no_selected_contacts), Toast.LENGTH_SHORT);
            return false;
        }
        return true;
    }

    //</editor-fold>
    //<editor-fold desc="LoaderManager.LoaderCallbacks<Cursor>">

    @Override
    public Loader<Cursor> onCreateLoader(final int id, final Bundle bundle) {
        switch (id) {
            case TC.Queries.PhoneContacts.SIMPLE_QUERY_ID:
            case TC.Queries.PhoneContacts.FILTER_QUERY_ID1:
                return getContactsFilteredBySearchTerm(mEntity.getSearchTerm());
            default:
                Log.e(TAG, String.format("OnCreateLoader - Unknown id provided (%s)", id));
                return null;
        }
    }

    @Override
    public void onLoadFinished(final Loader<Cursor> cursorLoader, final Cursor data) {

        if (mCursorAdapter != null) {
            mEmptyView.setVisibility(data.getCount() > 0 ? View.GONE : View.VISIBLE);
            mCursorAdapter.swapCursor(data);
        }
    }

    @Override
    public void onLoaderReset(final Loader<Cursor> cursorLoader) {

        if (cursorLoader != null && mCursorAdapter != null) {
            mEmptyView.setVisibility(View.VISIBLE);
            mCursorAdapter.swapCursor(null);
        }
    }

    //</editor-fold>
    //<editor-fold desc="AbsListView.OnScrollListener">

    @Override
    public void onScrollStateChanged(final AbsListView view, final int scrollState) {
        // Pause image loader to ensure smoother scrolling when flinging
        final boolean scrollFling = scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING;
        if (mImageLoader != null) {
            if (scrollFling)
                mImageLoader.setPauseWork(true);
            else
                mImageLoader.setPauseWork(false);
        }
        if (scrollFling)
            hideSoftKeyboard(view);
    }

    @Override
    public void onScroll(final AbsListView view, final int firstVisibleItem, final int visibleItemCount, final int totalItemCount) {

    }

    //</editor-fold>
    //<editor-fold desc="Button.OnClickListener">

    @Override
    public void onClick(final View v) {
        if (checkData(mEntity)) {
            hideSoftKeyboard(v);
            Intent mIntent = new Intent();
            mIntent.putExtra(TC.ENTITY.EXTRAS, mEntity.getResult());
            finalize(RESULT_OK, mEntity.isRequiredToBeSaved() ? mIntent : null);
        }
    }

    //</editor-fold>
    //</editor-fold>
    //<editor-fold desc="Private Methods">

    private Loader<Cursor> getContactsFilteredBySearchTerm(final String searchTerm) {

        final String selection = TC.Queries.PhoneContacts.SELECTION;
        final String sortOrder = TC.Queries.PhoneContacts.SORT_ORDER;
        final String[] projection = TC.Queries.PhoneContacts.PROJECTION;
        final Uri contentUri = !isNullOrEmpty(searchTerm) ?
                Uri.withAppendedPath(TC.Queries.PhoneContacts.FILTER_URI, Uri.encode(searchTerm)) :
                TC.Queries.PhoneContacts.CONTENT_URI;

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
            mContactItem.contact_check = (CheckBox) view.findViewById(R.id.contact_check);
            mContactItem.contact_check.setOnCheckedChangeListener(this);
            view.setTag(mContactItem);
            return view;
        }

        @Override
        public void bindView(final View view, final Context context, final Cursor cursor) {

            final int id = cursor.getInt(TC.Queries.PhoneContacts.ID);
            final ContactItem mContactItem = (ContactItem) view.getTag();

            mContactItem.contact_check.setTag(id);
            mContactItem.contact_check.setChecked(mEntity.isItemCollected(TeamsEntity.TEAMS, id));
            setBasicText(mContactItem.contact_phone, cursor.getString(TC.Queries.PhoneContacts.CONTACT_STATUS));
            setHighlightedText(mContactItem.contact_name, cursor.getString(TC.Queries.PhoneContacts.CONTACT_NAME), mEntity.getSearchTerm());
            setImageView(mContactItem.contact_thumbnail, mImageLoader, cursor.getString(TC.Queries.PhoneContacts.CONTACT_PHOTO_DATA));
        }

        @Override
        public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
            final Integer id = (Integer) buttonView.getTag();
            mEntity.changeCollectionItemState(TeamsEntity.TEAMS, id, isChecked);
        }

        private final class ContactItem {

            ImageView contact_thumbnail = null;
            TextView contact_phone = null;
            TextView contact_name = null;
            CheckBox contact_check = null;
        }
    }

    //</editor-fold>
}

