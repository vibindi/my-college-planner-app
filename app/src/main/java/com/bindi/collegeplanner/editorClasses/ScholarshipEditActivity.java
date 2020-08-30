package com.bindi.collegeplanner.editorClasses;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.bindi.collegeplanner.MainActivity;
import com.bindi.collegeplanner.R;
import com.bindi.collegeplanner.databaseClasses.GlobalKeys;
import com.bindi.collegeplanner.databaseClasses.Globals;
import com.bindi.collegeplanner.helperClasses.NumberTextWatcher;
import com.bindi.collegeplanner.itemClasses.CollegeItem;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.jaredrummler.android.colorpicker.ColorPickerDialog;

import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;
import ir.mirrajabi.searchdialog.core.Searchable;

public class ScholarshipEditActivity extends AppCompatActivity {

    Globals globals = Globals.getInstance();

    private static final int RESULT_LOAD_IMAGE = 1;

    Context c;
    Activity a;

    TextInputEditText scholarshipName;
    TextInputEditText scholarshipAmount;
    TextInputEditText notes;
    FloatingActionButton addScholarshipFab;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.DarkAppTheme);
        setContentView(R.layout.activity_add_scholarship);
        setTitle("Edit College");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        c = this;
        a = this;

        String data=null;
        boolean other = true;
        if(getIntent()!=null && getIntent().getExtras()!=null){
            Bundle bundle = getIntent().getExtras();
            if(!bundle.getString(GlobalKeys.loadingDirection).equals(null)){
                data= bundle.getString(GlobalKeys.loadingDirection);
                position = Integer.parseInt(data);
            }
        }



        scholarshipName = findViewById(R.id.scholarshipNameText);
        scholarshipName.setText(globals.scholarshipItems.get(position).getName());

        notes = findViewById(R.id.noteText);
        notes.setText(globals.scholarshipItems.get(position).getNotes());

        scholarshipAmount = findViewById(R.id.scholarshipAmountText);
        if (!globals.scholarshipItems.get(position).getAmount().equals("")){
            scholarshipAmount.setText(globals.scholarshipItems.get(position).getAmount());
        }
        scholarshipAmount.addTextChangedListener(new NumberTextWatcher(scholarshipAmount, "#,###"));

        addScholarshipFab = findViewById(R.id.addScholarship);
        addScholarshipFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!scholarshipName.getText().toString().isEmpty()){
                    globals.scholarshipItems.get(position).setName(scholarshipName.getText().toString());
                    if (!scholarshipAmount.getText().toString().equals("")){
                        globals.scholarshipItems.get(position).setAmount(scholarshipAmount.getText().toString());
                    }
                    if (!notes.getText().toString().equals("") && (notes.getText().toString().trim().length() > 0)){
                        globals.scholarshipItems.get(position).setNotes(notes.getText().toString());
                    }
                    globals.saveScholarships(globals.scholarshipItems, GlobalKeys.scholarshipsKey, c);
                    Intent intent = new Intent(a, MainActivity.class);
                    intent.putExtra(GlobalKeys.loadingDirection, GlobalKeys.scholarshipsDirection);
                    startActivity(intent);
                }else{
                    Toast.makeText(c, "Must Enter Name", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
