package com.smart.coffee.smartexam;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Calendar;

public class DialogTimeFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private OnTimePass timeCallback;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        timeCallback = (OnTimePass) getActivity();
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        timeCallback.onTimePass(hour, minute);
    }


    public interface OnTimePass{
        void onTimePass(int hour, int minute);
    }
}
