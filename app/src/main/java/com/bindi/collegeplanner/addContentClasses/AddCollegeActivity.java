package com.bindi.collegeplanner.addContentClasses;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bindi.collegeplanner.MainActivity;
import com.bindi.collegeplanner.R;
import com.bindi.collegeplanner.databaseClasses.ApplicationTypeDb;
import com.bindi.collegeplanner.databaseClasses.CollegeDb;
import com.bindi.collegeplanner.databaseClasses.CollegeSearchDb;
import com.bindi.collegeplanner.databaseClasses.CollegeTypeDb;
import com.bindi.collegeplanner.databaseClasses.GlobalKeys;
import com.bindi.collegeplanner.databaseClasses.Globals;
import com.bindi.collegeplanner.databaseClasses.MajorDb;
import com.bindi.collegeplanner.databaseClasses.StatusDb;
import com.bindi.collegeplanner.helperClasses.NumberTextWatcher;
import com.bindi.collegeplanner.helperClasses.SearchModel;
import com.bindi.collegeplanner.itemClasses.CollegeItem;
import com.bindi.collegeplanner.itemClasses.EventItem;
import com.bindi.collegeplanner.itemClasses.LinkItem;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.jaredrummler.android.colorpicker.ColorPickerDialog;
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener;
import com.jaredrummler.android.colorpicker.ColorPickerView;

import org.w3c.dom.Text;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;
import ir.mirrajabi.searchdialog.core.Searchable;

import static com.bindi.collegeplanner.SettingsActivity.inflater;

public class AddCollegeActivity extends AppCompatActivity implements ColorPickerDialogListener{

    Globals globals = Globals.getInstance();

    private static final int RESULT_LOAD_IMAGE = 1;
    //ImageView testImage;

    Context c;
    Activity a;

    TextInputEditText collegeName;
    TextInputEditText collegeMajor;
    TextInputLayout collegeColorLayout;
    TextInputEditText collegeColor;
    //TextInputLayout collegePicLayout;
    //TextInputEditText collegePic;
    TextInputEditText collegeStatus;
    TextInputEditText collegeAppFee;
    TextInputEditText collegeTuition;
    TextInputEditText appType;
    TextInputEditText collegeType;
    TextInputEditText collegeNotes;

    FloatingActionButton addCollegeFab;

    ColorStateList myList;

    String colorStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.DarkAppTheme);
        setContentView(R.layout.activity_add_college);
        setTitle("Add College");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        c = this;
        a = this;

        colorStr = String.format("#%06X", (0xFFFFFF & ContextCompat.getColor(c, R.color.colorPrimary)));

        //testImage = findViewById(R.id.testImage);

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

        collegeMajor = findViewById(R.id.collegeMajor);
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

        /*collegePicLayout = findViewById(R.id.collegePicLayout);

        collegePic = findViewById(R.id.collegePic);
        collegePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (testImage.getDrawable() == null){
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
                }else{
                    testImage.setImageDrawable(null);
                    collegePic.setText("");
                }
            }
        });*/

        collegeStatus = findViewById(R.id.collegeStatus);
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
        collegeAppFee.addTextChangedListener(new NumberTextWatcher(collegeAppFee, "#,###"));

        collegeTuition = findViewById(R.id.collegeTuition);
        collegeTuition.addTextChangedListener(new NumberTextWatcher(collegeTuition, "#,###"));

        appType = findViewById(R.id.applicationType);
        appType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SimpleSearchDialogCompat<>(a, "List Your Application Type", "Enter Application Type",
                        null, initApplicationTypeData(), new SearchResultListener<Searchable>() {
                    @Override
                    public void onSelected(BaseSearchDialogCompat baseSearchDialogCompat, Searchable searchable, int i) {
                        appType.setText(searchable.getTitle());
                        baseSearchDialogCompat.dismiss();
                    }
                }).show();
            }
        });

        collegeType = findViewById(R.id.collegeType);
        collegeType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SimpleSearchDialogCompat<>(a, "List Your College Type", "Enter College Type",
                        null, initCollegeTypeData(), new SearchResultListener<Searchable>() {
                    @Override
                    public void onSelected(BaseSearchDialogCompat baseSearchDialogCompat, Searchable searchable, int i) {
                        collegeType.setText(searchable.getTitle());
                        baseSearchDialogCompat.dismiss();
                    }
                }).show();
            }
        });

        collegeNotes = findViewById(R.id.collegeNotes);

        addCollegeFab = findViewById(R.id.addCollege);
        addCollegeFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!collegeName.getText().toString().isEmpty() && !collegeMajor.getText().toString().isEmpty() && !collegeStatus.getText().toString().isEmpty() && !collegeType.getText().toString().isEmpty() && !appType.getText().toString().isEmpty()){

                    boolean shouldAdd = true;
                    boolean enrolledOnce = true;
                    for (CollegeItem collegeItem : globals.collegeItems){
                        if (collegeName.getText().toString().equals(collegeItem.getCollegeName())){
                            shouldAdd = false;
                        }
                        if (collegeStatus.getText().toString().equals("Enrolled") && collegeItem.getStatus().equals("Enrolled")){
                            enrolledOnce = false;
                        }
                    }

                    if (shouldAdd && enrolledOnce){
                        String colorStr = "";
                        if (collegeColor.getText().toString().equals("#FFFFFF")){
                            colorStr = "#ECEDF3";
                        }else{
                            colorStr = collegeColor.getText().toString();
                        }
                        CollegeItem collegeItem = new CollegeItem(collegeName.getText().toString(), collegeMajor.getText().toString(), colorStr, collegeStatus.getText().toString());
                        collegeItem.setCollegeType(collegeType.getText().toString());
                        collegeItem.setAppType(appType.getText().toString());
                        if (!collegeAppFee.getText().toString().equals("") && !collegeAppFee.getText().toString().equals("$.00")){
                            collegeItem.setAppFee(collegeAppFee.getText().toString());
                        }
                        if (!collegeTuition.getText().toString().equals("") && !collegeTuition.getText().toString().equals("$.00")){
                            collegeItem.setTuition(collegeTuition.getText().toString());
                        }
                        if (!collegeNotes.getText().toString().equals("") && (collegeNotes.getText().toString().trim().length() > 0)){
                            collegeItem.setNotes(collegeNotes.getText().toString());
                        }
                        globals.collegeItems.add(collegeItem);
                        globals.sortCollegeList();
                        globals.saveColleges(globals.collegeItems, GlobalKeys.collegeKey, c);
                        globals.loadColleges(GlobalKeys.collegeKey, c);

                        if (collegeStatus.getText().toString().equals("Accepted")){
                            admittedCollegeDialog();
                        }
                        else if (collegeStatus.getText().toString().equals("Enrolled")){
                            enrolledCollegeDialog();
                        }
                        else{
                            Intent i = new Intent(a, MainActivity.class);
                            i.putExtra(GlobalKeys.loadingDirection, GlobalKeys.collegesDirection);
                            startActivity(i);
                        }
                    }
                    else if (!enrolledOnce){
                        Toast.makeText(c, "Cannot Enroll To Multiple Colleges", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(c, "Cannot Add " + collegeName.getText().toString() + " Multiple Times", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(c, "Must Enter College Name, Major, Status, Type and Application Type", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void enrolledCollegeDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(c);
        final View view = getLayoutInflater().inflate(R.layout.admitted_congrats, null);
        final TextView ct = view.findViewById(R.id.collegeTitle);
        ct.setText("Congrats on enrolling at");
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

    private ArrayList<Searchable> initApplicationTypeData(){
        ApplicationTypeDb applicationTypeDb = new ApplicationTypeDb();
        ArrayList<Searchable> items = new ArrayList<>();
        for (String str : applicationTypeDb.getTypesList()){
            items.add(new SearchModel(str));
        }
        return items;
    }

    private ArrayList<Searchable> initCollegeTypeData(){
        CollegeTypeDb collegeTypeDb = new CollegeTypeDb();
        ArrayList<Searchable> items = new ArrayList<>();
        for (String str : collegeTypeDb.getTypesList()){
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

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null){
            collegePic.setText("Picture Submitted");
            Uri selectedImage = data.getData();
            testImage.setImageURI(selectedImage);
        }
    }*/
}
