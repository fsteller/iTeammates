package com.fsteller.mobile.android.teammatesapp.activities.events;


import android.app.Activity;
import android.app.FragmentManager;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
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
import android.widget.Toast;

import com.fsteller.mobile.android.teammatesapp.R;
import com.fsteller.mobile.android.teammatesapp.TC;
import com.fsteller.mobile.android.teammatesapp.activities.base.FragmentMaintenancePageBase;
import com.fsteller.mobile.android.teammatesapp.activities.dialogs.DialogFragment_DatePicker;
import com.fsteller.mobile.android.teammatesapp.activities.dialogs.DialogFragment_TimePicker;
import com.fsteller.mobile.android.teammatesapp.model.base.IEventEntity;
import com.fsteller.mobile.android.teammatesapp.utils.Adapters;
import com.fsteller.mobile.android.teammatesapp.utils.image.ImageUtils;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Project: iTeammates
 * Subpackage: activities.events
 * <p/>
 * Description:
 * Created by fhernandezs on 26/02/14.
 */
public class EventsMaintenancePage2 extends FragmentMaintenancePageBase implements AdapterView.OnItemSelectedListener, LoaderManager.LoaderCallbacks<Cursor> {

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
        this.mEventEntity = (IEventEntity) mEntity;
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

            final Activity context = getActivity();
            final FragmentManager fm = getFragmentManager();
            final TextView titleLabel = (TextView) rootView.findViewById(R.id.title_label);
            final TextView controlView = (TextView) rootView.findViewById(R.id.header_control_label);
            final TextView descriptionLabel = (TextView) rootView.findViewById(R.id.title_description_label);
            final ImageButton button = (ImageButton) rootView.findViewById(R.id.header_button);

            final TextView dateFrom = (TextView) rootView.findViewById(R.id.dateFrom_edit);
            final TextView timeFrom = (TextView) rootView.findViewById(R.id.timeFrom_edit);
            final TextView dateTo = (TextView) rootView.findViewById(R.id.dateTo_edit);
            final TextView timeTo = (TextView) rootView.findViewById(R.id.timeTo_edit);

            titleLabel.setText(getResources().getString(R.string.eventsMaintenance_titleLabel));
            controlView.setText(getResources().getString(R.string.eventsMaintenance_titleControlLabel));
            descriptionLabel.setText(getResources().getString(R.string.eventsMaintenance_titleDescriptionLabel));

            button.setOnClickListener(new ImageUtils.PickImage(context));
            setupDateTextView(context, dateFrom, fm, 1);
            setupTimeTextView(context, timeFrom, fm, 0);
            setupDateTextView(context, dateTo, fm, 1);
            setupTimeTextView(context, timeTo, fm, 1);
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
    //<editor-fold desc="AdapterView.OnItemSelectedListener">
    @Override
    public void onItemSelected(final AdapterView<?> parent, final View view, final int position, final long id) {
        final TextView mView = (TextView) parent.findViewById(R.id.spinner_item_title);
        final CharSequence mCharSequence = mView.getText();
        if (mCharSequence != null)
            mEventEntity.setEntityCalendar(mCharSequence.toString());
    }

    @Override
    public void onNothingSelected(final AdapterView<?> parent) {

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
            headerSpinner.setVisibility(data.getCount() > 0 ? View.VISIBLE : View.GONE);
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

    private static void setupDateTextView(final Context context, final TextView view, final FragmentManager fm, final int adtionaldays) {

        final Calendar mCalendar = new GregorianCalendar();
        final int mYear = mCalendar.get(Calendar.YEAR);
        final int mMonth = mCalendar.get(Calendar.MONTH);
        final int mDay = mCalendar.get(Calendar.DAY_OF_MONTH);

        view.setText(getDateString(mYear, mMonth, mDay, adtionaldays));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Toast.makeText(context, context.getResources().getText(R.string.select_date), Toast.LENGTH_SHORT).show();
                final DialogFragment_DatePicker datePicker = new DialogFragment_DatePicker(mDay, mMonth, mYear, new
                        DialogFragment_DatePicker.DatePickerDialogListener() {
                            @Override
                            public void onDatePicked(final int selectedYear, final int selectedMonth, final int selectedDay) {
                                view.setText(getDateString(selectedYear, selectedMonth, selectedDay, 0));
                            }
                        });
                datePicker.setShowsDialog(true);
                datePicker.setRetainInstance(true);
                datePicker.show(fm, "date_picker");
            }
        });
        view.setClickable(true);
    }

    private static void setupTimeTextView(final Context context, final TextView view, final FragmentManager fm, final int additionalHours) {

        final Calendar mCalendar = new GregorianCalendar();
        final int mMinutes = mCalendar.get(Calendar.MINUTE);
        final int mHour = mCalendar.get(Calendar.HOUR_OF_DAY);

        view.setText(getTimeString(mHour, mMinutes, additionalHours));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Toast.makeText(context, context.getResources().getText(R.string.select_time), Toast.LENGTH_SHORT).show();
                final DialogFragment_TimePicker timePicker = new DialogFragment_TimePicker(mHour, mMinutes, new
                        DialogFragment_TimePicker.TimePickerDialogListener() {
                            @Override
                            public void onTimePicked(final int selectedHour, final int selectMinutes) {
                                view.setText(getTimeString(selectedHour, selectMinutes, 0));
                            }
                        });
                timePicker.setShowsDialog(true);
                timePicker.setRetainInstance(true);
                timePicker.show(fm, "time_picker");
            }
        });
        view.setClickable(true);
    }

    private static String getDateString(final int selectedYear, final int selectedMonth, final int selectedDay, final int additionalDays) {
        final DateFormat dateFormat = DateFormat.getDateInstance();
        return dateFormat.format(new GregorianCalendar(selectedYear, selectedMonth, selectedDay + additionalDays).getTime());
    }

    private static String getTimeString(final int selectedHour, final int selectMinutes, final int additionalHours) {
        final DateFormat dateFormat = DateFormat.getTimeInstance();
        return dateFormat.format(new GregorianCalendar(Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH, selectedHour + additionalHours, selectMinutes).getTime());
    }

    //</editor-fold>

}
