package com.smart.coffee.smartexam;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

public class DialogDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private OnDatePass dateCallback;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // restituisce il dialog
        return new DatePickerDialog(getActivity(), this, year, month, day);

    }

    // al momento dell'aggancio con l'activity che implementa l'interfaccia, setta la callback
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dateCallback = (OnDatePass) context;
    }

    // quando viene settata la data nel calendario, passa i dati all'activity con la callback
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        // setta la data nella view
        dateCallback.onDatePass(year, month, day);
    }

    // interfaccia che l'activity deve implementare per ricevere i dati dal fragment
    public interface OnDatePass{
        void onDatePass(int year, int month, int day);
    }
}
