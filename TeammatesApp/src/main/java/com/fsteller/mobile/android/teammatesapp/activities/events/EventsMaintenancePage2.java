package com.fsteller.mobile.android.teammatesapp.activities.events;


import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.fsteller.mobile.android.teammatesapp.R;
import com.fsteller.mobile.android.teammatesapp.TC;
import com.fsteller.mobile.android.teammatesapp.activities.base.FragmentMaintenancePageBase;
import com.fsteller.mobile.android.teammatesapp.model.base.IEventEntity;
import com.fsteller.mobile.android.teammatesapp.utils.Adapters;

/**
 * Project: iTeammates
 * Subpackage: activities.events
 * <p/>
 * Description:
 * Created by fhernandezs on 26/02/14.
 */
public class EventsMaintenancePage2 extends FragmentMaintenancePageBase implements AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener, LoaderManager.LoaderCallbacks<Cursor> {

    //<editor-fold desc="Constants">

    private final static String TAG = EventsMaintenancePage2.class.getSimpleName();

    //</editor-fold>
    //<editor-fold desc="Variables">

    private IEventEntity mEventEntity = null;

    private ImageView titleImage = null;
    private Spinner headerSpinner = null;

    private Adapters.CalendarAdapter calendarAdapter = null;

    //</editor-fold>

    //<editor-fold desc="Overridden">

    //<editor-fold desc="Fragment">
    @Override
    public void onAttach(final Activity activity) {
        super.onAttach(activity);
        this.mEventEntity = (IEventEntity) mCallback;
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final Activity mActivity = getActivity();

        if (mActivity != null) {

            calendarAdapter = new Adapters.CalendarAdapter(mActivity);
            imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            mImageLoader.loadImage(mEventEntity.getImageRef(), titleImage);
            headerSpinner.setOnItemSelectedListener(this);
            headerSpinner.setAdapter(calendarAdapter);
        }

        Log.d(TAG, "onActivityCreated");
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_events_maintenance_page2, container, false);
        if (rootView != null) {

            mEmptyView = rootView.findViewById(android.R.id.empty);
            titleImage = (ImageView) rootView.findViewById(R.id.header_image);
            headerSpinner = (Spinner) rootView.findViewById(R.id.header_spinner);

            final TextView titleLabel = (TextView) rootView.findViewById(R.id.title_label);
            final TextView controlView = (TextView) rootView.findViewById(R.id.header_control_label);
            final TextView descriptionLabel = (TextView) rootView.findViewById(R.id.title_description_label);
            final ImageButton button = (ImageButton) rootView.findViewById(R.id.header_button);

            titleLabel.setText(getResources().getString(R.string.eventsMaintenance_titleLabel));
            controlView.setText(getResources().getString(R.string.eventsMaintenance_titleControlLabel));
            descriptionLabel.setText(getResources().getString(R.string.eventsMaintenance_titleDescriptionLabel));

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Raising intent to pick image up...");
                    final Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    startActivityForResult(Intent.createChooser
                            (intent, getString(R.string.selectPicture)), TC.Activity.ContextActionRequest.PickImage);
                }
            });
        }
        Log.d(TAG, "onCreated");
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        restartLoader(TC.Queries.PhoneCalendar.SIMPLE_QUERY_ID, this);
    }


    //</editor-fold>
    //<editor-fold desc="AdapterView.OnItemClickListener">
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    //</editor-fold>
    //<editor-fold desc="AdapterView.OnItemSelectedListener">
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    //</editor-fold>
    //<editor-fold desc="LoaderManager.LoaderCallbacks<Cursor>">

    @Override
    public Loader<Cursor> onCreateLoader(final int id, final Bundle args) {
        /*
            Creates and return a CursorLoader that will take care of
            creating a Cursor for the data being displayed.
        */
        if (TC.Queries.PhoneCalendar.SIMPLE_QUERY_ID == id)
            return getCalendars("fsteller@gmail.com"); //TODO: replace fo a call to a real account id
        return null;
    }

    @Override
    public void onLoadFinished(final Loader<Cursor> loader, final Cursor data) {
        /*
            Swap the new cursor in.
            (The framework will take care of closing the old cursor once we return.)
        */
        if (TC.Queries.PhoneCalendar.SIMPLE_QUERY_ID == loader.getId()) {
            headerSpinner.setVisibility(data.getCount() > 0 ? View.GONE : View.VISIBLE);
            calendarAdapter.swapCursor(data);
        }
    }

    @Override
    public void onLoaderReset(final Loader<Cursor> loader) {
        /*
            When the loader is being reset, clear the cursor from the adapter.
            This allows the cursor resources to be freed.
        */
        if (TC.Queries.PhoneCalendar.SIMPLE_QUERY_ID == loader.getId()) {
            headerSpinner.setVisibility(View.GONE);
            calendarAdapter.swapCursor(null);
        }
    }

    //</editor-fold>

    @Override
    protected int getFragmentDefaultImage() {
        return R.drawable.ic_default_picture;
    }

    //</editor-fold>
    //<editor-fold desc="Private">
    private Loader<Cursor> getCalendars(final String accountType) {
        final Uri contentUri = TC.Queries.PhoneCalendar.CONTENT_URI;
        final String selection = TC.Queries.PhoneCalendar.SELECTION;
        final String sortOrder = TC.Queries.PhoneCalendar.SORT_ORDER;
        final String[] projection = TC.Queries.PhoneCalendar.PROJECTION;
        final String[] selectionArgs = new String[]{accountType};
        return new CursorLoader(getActivity(), contentUri, projection, selection, selectionArgs, sortOrder);
    }
    //</editor-fold>
}
