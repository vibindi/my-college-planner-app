package com.bindi.collegeplanner.addContentClasses;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bindi.collegeplanner.MainActivity;
import com.bindi.collegeplanner.R;
import com.bindi.collegeplanner.databaseClasses.CollegeDb;
import com.bindi.collegeplanner.databaseClasses.CollegeSearchDb;
import com.bindi.collegeplanner.databaseClasses.GlobalKeys;
import com.bindi.collegeplanner.databaseClasses.Globals;
import com.bindi.collegeplanner.displayClasses.CollegeActivity;
import com.bindi.collegeplanner.helperClasses.DatePickerFragment;
import com.bindi.collegeplanner.helperClasses.SearchModel;
import com.bindi.collegeplanner.itemClasses.CollegeItem;
import com.bindi.collegeplanner.itemClasses.EventItem;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;
import ir.mirrajabi.searchdialog.core.Searchable;

public class AddEventActivity extends AppCompatActivity {

    Globals globals = Globals.getInstance();
    Context c;
    Activity a;

    int position;

    TextView monthT;
    TextView dayT;
    TextView yearT;
    int month;
    int day;
    int year;

    TextInputLayout eventNameLayout;
    TextInputEditText eventName;
    TextInputLayout collegeNameLayout;
    TextInputEditText collegeName;
    TextInputLayout eventDateLayout;
    TextInputEditText eventDate;

    FloatingActionButton addEventFab;
    ColorStateList myList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.DarkAppTheme);
        setContentView(R.layout.activity_add_event);
        setTitle("Add Event");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        position = -1;

        monthT = findViewById(R.id.month);
        dayT = findViewById(R.id.day);
        yearT = findViewById(R.id.year);

        monthT.setVisibility(View.INVISIBLE);
        dayT.setVisibility(View.INVISIBLE);
        yearT.setVisibility(View.INVISIBLE);

        month = 0;
        day = 0;
        year = 0;

        c = this;
        a = this;

        eventNameLayout = findViewById(R.id.eventNameLayout);
        eventName = findViewById(R.id.eventName);

        collegeNameLayout = findViewById(R.id.collegeNameLayout);
        collegeName = findViewById(R.id.collegeName);
        collegeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SimpleSearchDialogCompat<>(a, "Search For College", "Enter College Name",
                        null, initCollegeData(), new SearchResultListener<Searchable>() {
                    @Override
                    public void onSelected(BaseSearchDialogCompat baseSearchDialogCompat, Searchable searchable, int i) {
                        //collegeName.setText("" + searchable.getTitle());
                        collegeName.setText(searchable.getTitle());
                        for (int x = 0; x < globals.collegeItems.size(); x++){
                            if (searchable.getTitle().equals(globals.collegeItems.get(x).getCollegeName())){
                                position = x;
                                setIconColor(globals.collegeItems.get(x).getColorStr());
                            }
                        }
                        baseSearchDialogCompat.dismiss();
                    }
                }).show();
            }
        });

        eventDateLayout = findViewById(R.id.eventDateLayout);
        eventDate = findViewById(R.id.dateEvent);
        eventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DatePickerFragment(eventDate, monthT, dayT, yearT);
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

        addEventFab = findViewById(R.id.addEvent);
        addEventFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!eventName.getText().toString().isEmpty() && !collegeName.getText().toString().isEmpty() && !eventDate.getText().toString().isEmpty()){
                    globals.collegeItems.get(position).addEventItem(new EventItem(eventName.getText().toString(), Integer.parseInt(yearT.getText().toString()), Integer.parseInt(monthT.getText().toString()), Integer.parseInt(dayT.getText().toString()), globals.collegeItems.get(position)));
                    globals.saveAll(c);
                    Intent intent = new Intent(c, MainActivity.class);
                    intent.putExtra(GlobalKeys.loadingDirection, GlobalKeys.calendarDirection);
                    startActivity(intent);
                } else{
                    Toast.makeText(getApplicationContext(), "Must Select A Title, College and Date", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void setIconColor(String colorStr){
        int[][] states = new int[][] {
                new int[] { android.R.attr.state_enabled}, // enabled
                new int[] {-android.R.attr.state_enabled}, // disabled
                new int[] {-android.R.attr.state_checked}, // unchecked
                new int[] { android.R.attr.state_pressed}  // pressed
        };

        int[] colors = new int[] {
                Color.parseColor(colorStr),
                Color.parseColor(colorStr),
                Color.parseColor(colorStr),
                Color.parseColor(colorStr)
        };

        myList = new ColorStateList(states, colors);
        collegeNameLayout.setStartIconTintList(myList);
        eventDateLayout.setStartIconTintList(myList);
        eventNameLayout.setStartIconTintList(myList);
    }

    private ArrayList<Searchable> initCollegeData(){
        CollegeSearchDb collegeDb = new CollegeSearchDb();
        ArrayList<Searchable> items = new ArrayList<>();
        for (CollegeItem collegeItem : globals.collegeItems){
            items.add(new SearchModel(collegeItem.getCollegeName()));
        }
        return items;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}