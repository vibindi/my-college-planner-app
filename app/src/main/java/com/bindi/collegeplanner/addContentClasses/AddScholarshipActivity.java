package com.bindi.collegeplanner.addContentClasses;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bindi.collegeplanner.MainActivity;
import com.bindi.collegeplanner.R;
import com.bindi.collegeplanner.databaseClasses.GlobalKeys;
import com.bindi.collegeplanner.databaseClasses.Globals;
import com.bindi.collegeplanner.helperClasses.NumberTextWatcher;
import com.bindi.collegeplanner.itemClasses.FinancialAidItem;
import com.bindi.collegeplanner.itemClasses.NoteItem;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

public class AddScholarshipActivity extends AppCompatActivity {

    Globals globals = Globals.getInstance();
    TextInputEditText scholarshipName;
    TextInputEditText scholarshipAmount;
    TextInputEditText notes;
    FloatingActionButton addScholarshipFab;

    Context c;
    Activity a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.DarkAppTheme);
        setContentView(R.layout.activity_add_scholarship);
        setTitle("Add Note");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        c = this;
        a = this;
        addScholarshipFab = findViewById(R.id.addScholarship);
        scholarshipName = findViewById(R.id.scholarshipNameText);
        scholarshipAmount = findViewById(R.id.scholarshipAmountText);
        scholarshipAmount.addTextChangedListener(new NumberTextWatcher(scholarshipAmount, "#,###"));
        notes = findViewById(R.id.noteText);
        addScholarshipFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!scholarshipName.getText().toString().isEmpty() && (scholarshipName.getText().toString().trim().length() > 0)){
                    FinancialAidItem finAidItem = new FinancialAidItem(scholarshipName.getText().toString());
                    if (!scholarshipAmount.getText().toString().equals("")){
                        finAidItem.setAmount(scholarshipAmount.getText().toString());
                    }
                    if (!notes.getText().toString().equals("") && (notes.getText().toString().trim().length() > 0)){
                        finAidItem.setNotes(notes.getText().toString());
                    }
                    globals.scholarshipItems.add(finAidItem);
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

