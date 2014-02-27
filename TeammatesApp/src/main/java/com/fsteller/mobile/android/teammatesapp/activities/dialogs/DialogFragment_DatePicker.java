package com.fsteller.mobile.android.teammatesapp.activities.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.TextView;

import com.fsteller.mobile.android.teammatesapp.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Project: iTeammates
 * Subpackage: activities.events
 * <p/>
 * Description:
 * Created by fhernandezs on 26/02/14.
 */
public class DialogFragment_DatePicker extends DialogFragment {
    private int mDay = 1;
    private int mMonth = 1;
    private int mYear = 2000;
    private DatePicker cv = null;
    private DatePickerDialogListener mListener = null;

    /* The Fragment that creates an instance of this dialog fragment must
     * pass as parameter an implementation of this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public static interface DatePickerDialogListener {
        public void onDatePicked(final int selectYear, final int selectMonth, final int selectDay);
    }

    public DialogFragment_DatePicker(final DatePickerDialogListener listener) {
        super();
        this.mListener = listener;
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {

        // Use the Builder class for convenient dialog construction
        final Activity activity = getActivity();
        if (activity == null)
            return null;

        final LayoutInflater inflater = activity.getLayoutInflater();
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        final View rootView = inflater.inflate(R.layout.dialog_calendar_day_picker, null);

        if (rootView != null) {
            final TextView mWeekDay = (TextView) rootView.findViewById(R.id.date_weekDayLabel);

            cv = (DatePicker) rootView.findViewById(R.id.date_pickerCalendar);
            cv.init(Calendar.DAY_OF_YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH,
                    new DatePicker.OnDateChangedListener() {
                        @Override
                        public void onDateChanged(final DatePicker view, final int year, final int month, final int day) {
                            DialogFragment_DatePicker.this.mDay = day;
                            DialogFragment_DatePicker.this.mYear = year;
                            DialogFragment_DatePicker.this.mMonth = month;

                            mWeekDay.setText(getWeekDay(year, month, day));
                        }
                    });

        }

        builder.setPositiveButton(R.string.action_finish, new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, final int id) {
                if (mListener != null)
                    mListener.onDatePicked(mYear, mMonth, mDay);
            }
        });

        // Create the AlertDialog object and return it
        builder.setView(rootView);
        Dialog result = builder.create();
        setStyle(STYLE_NORMAL, android.R.style.Theme_Dialog);
        result.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return result;
    }

    private static String getWeekDay(final int year, final int month, final int day) {
        return new SimpleDateFormat("EEEE").format(new GregorianCalendar(year, month, day).getTime());
    }
}
