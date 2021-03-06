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
import android.widget.TimePicker;

import com.fsteller.mobile.android.teammatesapp.R;

public class DialogFragment_TimePicker extends DialogFragment {

    private int mHour = 0;
    private int mMinutes = 0;
    private TimePickerDialogListener mListener = null;

    public DialogFragment_TimePicker(final int hour, final int minutes, final TimePickerDialogListener listener) {
        super();
        this.mHour = hour;
        this.mMinutes = minutes;
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
        final View rootView = inflater.inflate(R.layout.dialog_time_picker, null);

        if (rootView != null) {
            final TimePicker mTimePicker = (TimePicker) rootView.findViewById(R.id.date_timePicker);
            mTimePicker.setCurrentHour(mHour);
            mTimePicker.setCurrentMinute(mMinutes);
            mTimePicker.setOnTimeChangedListener(
                    new TimePicker.OnTimeChangedListener() {
                        @Override
                        public void onTimeChanged(final TimePicker view, final int hourOfDay, final int minutes) {
                            mMinutes = minutes;
                            mHour = hourOfDay;

                        }
                    }
            );
        }

        builder.setPositiveButton(R.string.action_finish, new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, final int id) {
                if (mListener != null)
                    mListener.onTimePicked(mHour, mMinutes);
            }
        });

        // Create the AlertDialog object and return it
        builder.setView(rootView);
        Dialog result = builder.create();
        setStyle(STYLE_NORMAL, android.R.style.Theme_Dialog);
        result.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return result;
    }

    /* The activity that creates an instance of this dialog fragment must
         * implement this interface in order to receive event callbacks.
         * Each method passes the DialogFragment in case the host needs to query it. */
    public interface TimePickerDialogListener {
        public void onTimePicked(final int selectedHour, final int selectMinutes);
    }
}
