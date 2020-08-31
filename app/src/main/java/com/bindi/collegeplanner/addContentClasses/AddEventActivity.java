package com.bindi.collegeplanner.addContentClasses;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.bindi.collegeplanner.R;
import com.bindi.collegeplanner.databaseClasses.CollegeDb;
import com.bindi.collegeplanner.databaseClasses.CollegeSearchDb;
import com.bindi.collegeplanner.databaseClasses.Globals;
import com.bindi.collegeplanner.helperClasses.SearchModel;
import com.bindi.collegeplanner.itemClasses.CollegeItem;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;
import ir.mirrajabi.searchdialog.core.Searchable;

public class AddEventActivity extends AppCompatActivity {

    Globals globals = Globals.getInstance();
    Context c;
    Activity a;

    TextInputEditText collegeName;
    TextInputEditText eventDate;

    FloatingActionButton addEventFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.DarkAppTheme);
        setContentView(R.layout.activity_add_event);
        setTitle("Add Event");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        c = this;
        a = this;

        collegeName = findViewById(R.id.collegeName);
        collegeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SimpleSearchDialogCompat<>(a, "Search For College", "Enter College Name",
                        null, initCollegeData(), new SearchResultListener<Searchable>() {
                    @Override
                    public void onSelected(BaseSearchDialogCompat baseSearchDialogCompat, Searchable searchable, int i) {
                        //collegeName.setText("" + searchable.getTitle());
                        CollegeSearchDb collegeSearchDb = new CollegeSearchDb();
                        CollegeDb collegeDb = new CollegeDb();
                        int position = collegeSearchDb.getCollegesList().indexOf(searchable.getTitle());
                        collegeName.setText(collegeDb.getCollegesList().get(position));
                        baseSearchDialogCompat.dismiss();
                    }
                }).show();
            }
        });

        eventDate = findViewById(R.id.dateEvent);
        eventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
            }
        });

        addEventFab = findViewById(R.id.addEvent);
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