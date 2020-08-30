package com.bindi.collegeplanner.editorClasses;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.bindi.collegeplanner.MainActivity;
import com.bindi.collegeplanner.R;
import com.bindi.collegeplanner.databaseClasses.CollegeDb;
import com.bindi.collegeplanner.databaseClasses.CollegeSearchDb;
import com.bindi.collegeplanner.databaseClasses.GlobalKeys;
import com.bindi.collegeplanner.databaseClasses.Globals;
import com.bindi.collegeplanner.databaseClasses.MajorDb;
import com.bindi.collegeplanner.databaseClasses.StatusDb;
import com.bindi.collegeplanner.helperClasses.NumberTextWatcher;
import com.bindi.collegeplanner.helperClasses.SearchModel;
import com.bindi.collegeplanner.itemClasses.CollegeItem;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.jaredrummler.android.colorpicker.ColorPickerDialog;
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener;

import java.util.ArrayList;

import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;
import ir.mirrajabi.searchdialog.core.Searchable;

public class CollegeEditActivity extends AppCompatActivity implements ColorPickerDialogListener {

    Globals globals = Globals.getInstance();

    private static final int RESULT_LOAD_IMAGE = 1;
    //ImageView testImage;

    Context c;
    Activity a;

    TextInputEditText collegeName;
    TextInputEditText collegeMajor;
    TextInputLayout collegeColorLayout;
    TextInputEditText collegeColor;
    TextInputEditText collegeStatus;
    TextInputEditText collegeType;
    TextInputEditText applicationType;
    TextInputEditText collegeAppFee;
    TextInputEditText collegeTuition;
    TextInputEditText collegeNotes;

    FloatingActionButton addCollegeFab;

    ColorStateList myList;

    String colorStr;

    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.DarkAppTheme);
        setContentView(R.layout.activity_add_college);
        setTitle("Edit College");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        c = this;
        a = this;

        colorStr = String.format("#%06X", (0xFFFFFF & ContextCompat.getColor(c, R.color.colorPrimary)));

        String data=null;
        boolean other = true;
        if(getIntent()!=null && getIntent().getExtras()!=null){
            Bundle bundle = getIntent().getExtras();
            if(!bundle.getString(GlobalKeys.loadingDirection).equals(null)){
                data= bundle.getString(GlobalKeys.loadingDirection);
                position = Integer.parseInt(data);
            }
        }



        collegeName = findViewById(R.id.collegeName);
        collegeName.setText(globals.collegeItems.get(position).getCollegeName());

        collegeType = findViewById(R.id.collegeType);
        collegeType.setText(globals.collegeItems.get(position).getCollegeType());

        applicationType = findViewById(R.id.applicationType);
        applicationType.setText(globals.collegeItems.get(position).getAppType());

        collegeMajor = findViewById(R.id.collegeMajor);
        collegeMajor.setText(globals.collegeItems.get(position).getCollegeMajor());
        collegeMajor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SimpleSearchDialogCompat<>(a, "Search For Major", "Enter College Major",
                        null, initMajorData(), new SearchResultListener<Searchable>() {
                    @Override
                    public void onSelected(BaseSearchDialogCompat baseSearchDialogCompat, Searchable searchable, int i) {
                        if (searchable.getTitle().equals("I Did Not Find My Major")){
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(c);
                            final View view = getLayoutInflater().inflate(R.layout.change_major_dialog, null);
                            final TextInputEditText noteTitleText = view.findViewById(R.id.noteTitle);
                            alertDialogBuilder
                                    .setTitle("Enter Your Major")
                                    .setView(view)
                                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (!noteTitleText.getText().toString().isEmpty() && (noteTitleText.getText().toString().trim().length() > 0)){
                                                collegeMajor.setText(noteTitleText.getText().toString());
                                            }else{
                                                Toast.makeText(c, "Must Enter Title", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    })
                                    .setNegativeButton("Cancel", null);
                            Dialog dialog = alertDialogBuilder.create();
                            dialog.show();
                        }else{
                            collegeMajor.setText(searchable.getTitle());
                        }
                        baseSearchDialogCompat.dismiss();
                    }
                }).show();
            }
        });

        collegeColor = findViewById(R.id.collegeColor);
        colorStr = globals.collegeItems.get(position).getColorStr();
        collegeColor.setText(colorStr);
        collegeColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorPickerDialog.newBuilder()
                        .setDialogType(ColorPickerDialog.TYPE_CUSTOM)
                        .setAllowPresets(false)
                        .setColor(Color.parseColor(colorStr))
                        .show((FragmentActivity) a);
            }
        });
        collegeColorLayout = findViewById(R.id.collegeColorLayout);
        setIconColor();

        collegeStatus = findViewById(R.id.collegeStatus);
        collegeStatus.setText(globals.collegeItems.get(position).getStatus());
        collegeStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SimpleSearchDialogCompat<>(a, "List Your College Status", "Enter College Status",
                        null, initStatusData(), new SearchResultListener<Searchable>() {
                    @Override
                    public void onSelected(BaseSearchDialogCompat baseSearchDialogCompat, Searchable searchable, int i) {
                        collegeStatus.setText(searchable.getTitle());
                        baseSearchDialogCompat.dismiss();
                    }
                }).show();
            }
        });

        collegeAppFee = findViewById(R.id.collegeAppFee);
        if (!globals.collegeItems.get(position).getAppFee().equals("$0.00")){
            collegeAppFee.setText(globals.collegeItems.get(position).getAppFee());
        }
        collegeAppFee.addTextChangedListener(new NumberTextWatcher(collegeAppFee, "#,###"));

        collegeTuition = findViewById(R.id.collegeTuition);
        if (!globals.collegeItems.get(position).getTuition().equals("$0.00")){
            collegeTuition.setText(globals.collegeItems.get(position).getTuition());
        }
        collegeTuition.addTextChangedListener(new NumberTextWatcher(collegeTuition, "#,###"));

        collegeNotes = findViewById(R.id.collegeNotes);
        collegeNotes.setText(globals.collegeItems.get(position).getNotes());

        addCollegeFab = findViewById(R.id.addCollege);
        addCollegeFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!collegeName.getText().toString().isEmpty() && !collegeMajor.getText().toString().isEmpty()){

                    boolean enrolledOnce = true;
                    for (CollegeItem collegeItem : globals.collegeItems){
                        if (!collegeItem.equals(globals.collegeItems.get(position)) && collegeStatus.getText().toString().equals("Enrolled") && collegeItem.getStatus().equals("Enrolled")){
                            enrolledOnce = false;
                        }
                    }
                    //CollegeItem collegeItem = new CollegeItem(collegeName.getText().toString(), collegeMajor.getText().toString(), collegeColor.getText().toString(), collegeStatus.getText().toString());
                    if (enrolledOnce){
                        globals.collegeItems.get(position).setCollegeMajor(collegeMajor.getText().toString());
                        if (collegeColor.getText().toString().equals("#FFFFFF")){
                            globals.collegeItems.get(position).setColorStr("#ECEDF3");
                        }else{
                            globals.collegeItems.get(position).setColorStr(collegeColor.getText().toString());
                        }
                        globals.collegeItems.get(position).setStatus(collegeStatus.getText().toString());
                        if (!collegeAppFee.getText().toString().equals("")){
                            globals.collegeItems.get(position).setAppFee(collegeAppFee.getText().toString());
                        }
                        if (!collegeTuition.getText().toString().equals("")){
                            globals.collegeItems.get(position).setTuition(collegeTuition.getText().toString());
                        }
                        globals.collegeItems.get(position).setNotes(collegeNotes.getText().toString());
                        globals.saveColleges(globals.collegeItems, GlobalKeys.collegeKey, c);
                        globals.loadColleges(GlobalKeys.collegeKey, c);

                        if (collegeStatus.getText().toString().equals("Accepted")){
                            admittedCollegeDialog();
                        }else{
                            Intent i = new Intent(a, MainActivity.class);
                            i.putExtra(GlobalKeys.loadingDirection, GlobalKeys.collegesDirection);
                            startActivity(i);
                        }
                    }
                    else{
                        Toast.makeText(c, "Cannot Enroll To Multiple Colleges", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(c, "Must Enter College Name, Major, and Status", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void admittedCollegeDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(c);
        final View view = getLayoutInflater().inflate(R.layout.admitted_congrats, null);
        final TextView cn = view.findViewById(R.id.collegeNameText);
        cn.setText(collegeName.getText().toString() + "!");
        cn.setTextColor(Color.parseColor(collegeColor.getText().toString()));
        alertDialogBuilder
                //.setTitle("Congrats On Getting Into")
                .setView(view)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(a, MainActivity.class);
                        i.putExtra(GlobalKeys.loadingDirection, GlobalKeys.collegesDirection);
                        startActivity(i);
                    }
                });
        Dialog dialog = alertDialogBuilder.create();
        dialog.show();
    }

    private ArrayList<Searchable> initCollegeData(){
        CollegeSearchDb collegeDb = new CollegeSearchDb();
        ArrayList<Searchable> items = new ArrayList<>();
        for (String str : collegeDb.getCollegesList()){
            items.add(new SearchModel(str));
        }
        return items;
    }

    private ArrayList<Searchable> initMajorData(){
        MajorDb majorDb = new MajorDb();
        ArrayList<Searchable> items = new ArrayList<>();
        for (String str : majorDb.getTypesList()){
            items.add(new SearchModel(str));
        }
        return items;
    }

    private ArrayList<Searchable> initStatusData(){
        StatusDb statusDb = new StatusDb();
        ArrayList<Searchable> items = new ArrayList<>();
        for (String str : statusDb.getTypesList()){
            items.add(new SearchModel(str));
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

    @Override
    public void onColorSelected(int dialogId, int color) {
        colorStr = String.format("#%06X", (0xFFFFFF & color));
        collegeColor.setText(colorStr);
        setIconColor();
    }

    @Override
    public void onDialogDismissed(int dialogId) {

    }

    public void setIconColor(){
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
        collegeColorLayout.setStartIconTintList(myList);
    }

}
