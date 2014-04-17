package com.fsteller.mobile.android.teammatesapp.activities.events;


import android.app.Activity;
import android.app.Fragment;
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
import android.widget.EditText;
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
import com.fsteller.mobile.android.teammatesapp.activities.dialogs.DialogFragment_ZonePicker;
import com.fsteller.mobile.android.teammatesapp.image.Utils;
import com.fsteller.mobile.android.teammatesapp.model.base.IEventsEntity;
import com.fsteller.mobile.android.teammatesapp.utils.Adapters.CalendarAdapter;

import java.text.DateFormat;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Project: iTeammates
 * Subpackage: activities.events
 * <p/>
 * Description:
 * <p/>
 * Created by fhernandezs on 26/02/14.
 */
public class EventsMaintenancePage2 extends FragmentMaintenancePageBase implements IEventsEntity.Callback, AdapterView.OnItemSelectedListener, LoaderManager.LoaderCallbacks<Cursor> {

    //<editor-fold desc="Constants">

    private final static String TAG = EventsMaintenancePage2.class.getSimpleName();

    //</editor-fold>
    //<editor-fold desc="Variables">

    private IEventsEntity mEventEntity = null;

    private ImageView titleImage = null;
    private Spinner headerSpinner = null;
    private EditText decryptionText = null;
    private EditText titleText = null;

    private TextView dateFrom = null;
    private TextView timeFrom = null;
    private TextView dateTo = null;
    private TextView timeTo = null;

    private TextView repetition = null;
    private TextView timeZone = null;


    private CalendarAdapter calendarAdapter = null;

    //</editor-fold>

    //<editor-fold desc="Overridden">

    //<editor-fold desc="Fragment">

    private static void setupDateTextView(final Fragment fragment, final TextView view, final IEventsEntity entity, final boolean isOriginDate) {

        view.setText(getDateString(entity, isOriginDate));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final FragmentManager fm = fragment.getFragmentManager();
                final int mDay = isOriginDate ? entity.getDayFrom() : entity.getDayTo();
                final int mYear = isOriginDate ? entity.getYearFrom() : entity.getYearTo();
                final int mMonth = isOriginDate ? entity.getMonthFrom() : entity.getMonthTo();
                final Toast mToast = Toast.makeText(fragment.getActivity(), fragment.getResources().getText(R.string.pick_date), Toast.LENGTH_SHORT);
                final DialogFragment_DatePicker datePicker = new DialogFragment_DatePicker(mDay, mMonth, mYear, new
                        DialogFragment_DatePicker.DatePickerDialogListener() {
                            @Override
                            public void onDatePicked(final int selectedYear, final int selectedMonth, final int selectedDay) {
                                final boolean success = isOriginDate ?
                                        entity.setDateFrom(selectedYear, selectedMonth, selectedDay) :
                                        entity.setDateTo(selectedYear, selectedMonth, selectedDay);
                                if (!success)
                                    Toast.makeText(fragment.getActivity(), fragment.getResources().getText(R.string.pick_date_error), Toast.LENGTH_LONG).show();
                                //view.setText(getDateString(entity, isOriginDate));
                            }
                        });
                datePicker.setShowsDialog(true);
                datePicker.setRetainInstance(true);
                datePicker.show(fm, "date_picker");
                mToast.show();
            }
        });
        view.setClickable(true);
    }

    private static void setupTimeTextView(final Fragment fragment, final TextView view, final IEventsEntity entity, final boolean isOriginDate) {

        view.setText(getTimeString(entity, isOriginDate));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final FragmentManager fm = fragment.getFragmentManager();
                final int mHour = isOriginDate ? entity.getHourFrom() : entity.getHourTo();
                final int mMinutes = isOriginDate ? entity.getMinutesFrom() : entity.getMinutesTo();
                final Toast mToast = Toast.makeText(fragment.getActivity(), fragment.getResources().getText(R.string.pick_time), Toast.LENGTH_SHORT);
                final DialogFragment_TimePicker timePicker = new DialogFragment_TimePicker(mHour, mMinutes, new
                        DialogFragment_TimePicker.TimePickerDialogListener() {
                            @Override
                            public void onTimePicked(final int selectedHour, final int selectMinutes) {
                                final boolean success = isOriginDate ?
                                        entity.setTimeFrom(selectedHour, selectMinutes) :
                                        entity.setTimeTo(selectedHour, selectMinutes);
                                if (!success)
                                    Toast.makeText(fragment.getActivity(), fragment.getResources().getText(R.string.pick_time_error), Toast.LENGTH_LONG).show();
                                //view.setText(getTimeString(entity, isOriginDate));
                            }
                        });
                timePicker.setShowsDialog(true);
                timePicker.setRetainInstance(true);
                timePicker.show(fm, "time_picker");
                mToast.show();
            }
        });
        view.setClickable(true);
    }

    private static void setupRepetitionTextView(final Fragment fragment, final TextView view, final IEventsEntity entity) {

        view.setText(getRepetitionString(entity));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

            }
        });
    }

    private static void setupTimeZoneTextView(final Fragment fragment, final TextView view, final IEventsEntity entity) {

        view.setText(getTimeZoneString(entity));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                final FragmentManager fm = fragment.getFragmentManager();
                final Toast mToast = Toast.makeText(fragment.getActivity(), fragment.getResources().getText(R.string.pick_timeZone), Toast.LENGTH_SHORT);
                final DialogFragment_ZonePicker timeZonePicker = new DialogFragment_ZonePicker(entity.getTimeZone(), new DialogFragment_ZonePicker.ZoneSelectionListener() {
                    @Override
                    public void onZoneSelected(final TimeZone tz) {
                        entity.setTimeZone(tz.getID());
                    }
                });

                timeZonePicker.setShowsDialog(true);
                timeZonePicker.setRetainInstance(true);
                timeZonePicker.show(fm, "timeZone_picker");
                mToast.show();
            }
        });
    }


    //</editor-fold>
    //<editor-fold desc="LoaderManager.LoaderCallbacks<Cursor>">

    private static String getDateString(final IEventsEntity entity, final boolean isOriginDate) {
        final DateFormat dateFormat = DateFormat.getDateInstance();
        final int day = isOriginDate ? entity.getDayFrom() : entity.getDayTo();
        final int year = isOriginDate ? entity.getYearFrom() : entity.getYearTo();
        final int month = isOriginDate ? entity.getMonthFrom() : entity.getMonthTo();
        return dateFormat.format(new GregorianCalendar(year, month, day).getTime());
    }

    private static String getTimeString(final IEventsEntity entity, final boolean isOriginDate) {
        final DateFormat dateFormat = DateFormat.getTimeInstance();
        final int day = isOriginDate ? entity.getDayFrom() : entity.getDayTo();
        final int year = isOriginDate ? entity.getYearFrom() : entity.getYearTo();
        final int month = isOriginDate ? entity.getMonthFrom() : entity.getMonthTo();
        final int hour = isOriginDate ? entity.getHourFrom() : entity.getHourTo();
        final int minutes = isOriginDate ? entity.getMinutesFrom() : entity.getMinutesTo();
        return dateFormat.format(new GregorianCalendar(year, month, day, hour, minutes).getTime());
    }

    private static String getRepetitionString(final IEventsEntity entity) {
        return "";
    }

    //</editor-fold>
    //<editor-fold desc="AdapterView.OnItemSelectedListener">

    private static String getTimeZoneString(final IEventsEntity entity) {
        return entity.getTimeZone();
    }

    private static Loader<Cursor> getCalendars(final Activity activity, final String accountType) {
        final Uri contentUri = TC.Queries.PhoneCalendar.CONTENT_URI;
        final String selection = TC.Queries.PhoneCalendar.SELECTION;
        final String sortOrder = TC.Queries.PhoneCalendar.SORT_ORDER;
        final String[] projection = TC.Queries.PhoneCalendar.PROJECTION;
        final String[] selectionArgs = new String[]{accountType};
        return new CursorLoader(activity, contentUri, projection, selection, selectionArgs, sortOrder);
    }

    //</editor-fold>
    //<editor-fold desc="IEventsEntity.Callback">

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final Activity mActivity = getActivity();

        if (mActivity != null) {

            imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);

            calendarAdapter = new CalendarAdapter(mActivity);
            headerSpinner.setOnItemSelectedListener(this);
            headerSpinner.setAdapter(calendarAdapter);
        }

        Log.d(TAG, "onActivityCreated");
    }

    @Override
    public void onAttach(final Activity activity) {
        super.onAttach(activity);
        this.mEventEntity = (IEventsEntity) mEntity;
        this.mEventEntity.setCallback(this);
    }

    //</editor-fold>

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_events_maintenance_page2, container, false);
        if (rootView != null) {

            mEmptyView = rootView.findViewById(android.R.id.empty);
            titleImage = (ImageView) rootView.findViewById(R.id.header_image);
            titleText = (EditText) rootView.findViewById(R.id.collection_title_text);
            decryptionText = (EditText) rootView.findViewById(R.id.collection_description_text);
            headerSpinner = (Spinner) rootView.findViewById(R.id.header_spinner);

            dateFrom = (TextView) rootView.findViewById(R.id.dateFrom_edit);
            timeFrom = (TextView) rootView.findViewById(R.id.timeFrom_edit);
            dateTo = (TextView) rootView.findViewById(R.id.dateTo_edit);
            timeTo = (TextView) rootView.findViewById(R.id.timeTo_edit);

            repetition = (TextView) rootView.findViewById(R.id.calendarRepetition_edit);
            timeZone = (TextView) rootView.findViewById(R.id.timeZone_edit);

            setupDateTextView(this, dateFrom, mEventEntity, true);
            setupTimeTextView(this, timeFrom, mEventEntity, true);
            setupDateTextView(this, dateTo, mEventEntity, false);
            setupTimeTextView(this, timeTo, mEventEntity, false);

            setupRepetitionTextView(this, repetition, mEventEntity);
            setupTimeZoneTextView(this, timeZone, mEventEntity);

            final TextView titleLabel = (TextView) rootView.findViewById(R.id.title_label);
            final TextView controlView = (TextView) rootView.findViewById(R.id.header_control_label);
            final TextView descriptionLabel = (TextView) rootView.findViewById(R.id.title_description_label);
            final ImageButton button = (ImageButton) rootView.findViewById(R.id.header_button);

            titleLabel.setText(getResources().getString(R.string.eventsMaintenance_titleLabel));
            controlView.setText(getResources().getString(R.string.eventsMaintenance_titleControlLabel));
            descriptionLabel.setText(getResources().getString(R.string.eventsMaintenance_titleDescriptionLabel));
            button.setOnClickListener(new Utils.PickImage(getActivity()));

        }
        Log.d(TAG, "onCreated");
        return rootView;
    }

    //</editor-fold>
    //<editor-fold desc="Static">

    @Override
    public void onResume() {
        super.onResume();

        restartLoader(TC.Queries.PhoneCalendar.SIMPLE_QUERY_ID, this);
        mLoader.loadImage(mEventEntity.getImageRef(), titleImage);
        decryptionText.setText(mEventEntity.getDescription());
        titleText.setText(mEventEntity.getName());

    }

    @Override
    public Loader<Cursor> onCreateLoader(final int id, final Bundle args) {
        /*
            Creates and return a CursorLoader that will take care of
            creating a Cursor for the data being displayed.
        */
        if (TC.Queries.PhoneCalendar.SIMPLE_QUERY_ID == id)
            return getCalendars(getActivity(), "fsteller@gmail.com"); //TODO: replace fo a call to a real account id
        return null;
    }

    @Override
    public void onLoadFinished(final Loader<Cursor> loader, final Cursor data) {
        /*
            Swap the new cursor in.
            (The framework will take care of closing the old cursor once we return.)
        */
        if (TC.Queries.PhoneCalendar.SIMPLE_QUERY_ID == loader.getId()) {
            calendarAdapter.swapCursor(data);
            headerSpinner.setVisibility(data.getCount() > 0 ? View.VISIBLE : View.GONE);
            headerSpinner.setSelection(calendarAdapter.getPosition(mEventEntity.getCalendarId()));
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

    @Override
    public void onItemSelected(final AdapterView<?> parent, final View view, final int position, final long id) {
        final CalendarAdapter.CalendarItem mCalendarItem = (CalendarAdapter.CalendarItem) view.getTag();
        this.mEventEntity.setCalendarId(mCalendarItem.id);
    }

    @Override
    public void onNothingSelected(final AdapterView<?> parent) {

    }

    @Override
    public void OnDateTimeHasBeenUpdated(final IEventsEntity sender, final boolean isDateTimeFrom, final boolean isDateTimeTo) {
        if (isDateTimeFrom) {
            dateFrom.setText(getDateString(sender, true));
            timeFrom.setText(getTimeString(sender, true));
        }

        if (isDateTimeTo) {
            dateTo.setText(getDateString(sender, false));
            timeTo.setText(getTimeString(sender, false));
        }
    }

    @Override
    public void OnTimeZoneHasBeenUpdated(final IEventsEntity sender) {
        timeZone.setText(getTimeZoneString(sender));
    }

    @Override
    protected int getFragmentDefaultImage() {
        return R.drawable.ic_default_picture;
    }

    //</editor-fold>

}
