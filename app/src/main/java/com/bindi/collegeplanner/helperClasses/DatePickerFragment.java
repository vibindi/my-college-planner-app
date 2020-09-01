package com.bindi.collegeplanner.helperClasses;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    TextInputEditText dateEvent;
    TextView monthT;
    TextView dayT;
    TextView yearT;

    public DatePickerFragment(TextInputEditText dateEvent, TextView month, TextView day, TextView year){
        this.dateEvent = dateEvent;
        this.monthT = month;
        this.dayT = day;
        this.yearT = year;
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
        dateEvent.setText(showMonth(month+1) + " " + day + ", " + year);
        monthT.setText(month+1 + "");
        dayT.setText(day + "");
        yearT.setText(year + "");
    }

    public String showMonth(int month){
        String str = "";
        switch(month){
            case 1:
                str = "January"; break;
            case 2:
                str = "February";break;
            case 3:
                str = "March";break;
            case 4:
                str = "April";break;
            case 5:
                str = "May";break;
            case 6:
                str = "June";break;
            case 7:
                str = "July";break;
            case 8:
                str = "August";break;
            case 9:
                str = "September";break;
            case 10:
                str = "October";break;
            case 11:
                str = "November";break;
            case 12:
                str = "December";break;
            default:
                str = "";break;
        }
        return str;
    }

}
