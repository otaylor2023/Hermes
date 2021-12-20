package com.hermes;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.fragment.app.DialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    private Button button;
    private MarkerData markerData;

    public DatePickerFragment(Button button, MarkerData markerData) {
        this.button = button;
        this.markerData = markerData;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day);

        SimpleDateFormat format = new SimpleDateFormat("EEE, MMM d, yyyy", Locale.US);
        String dateStr = format.format(cal.getTime());
        button.setText(dateStr);
        markerData.setDate(cal.getTime());
    }
}