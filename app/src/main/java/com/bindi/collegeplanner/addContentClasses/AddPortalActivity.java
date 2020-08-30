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
import com.bindi.collegeplanner.databaseClasses.CollegeDb;
import com.bindi.collegeplanner.databaseClasses.CollegeSearchDb;
import com.bindi.collegeplanner.databaseClasses.GlobalKeys;
import com.bindi.collegeplanner.databaseClasses.Globals;
import com.bindi.collegeplanner.displayClasses.CollegeActivity;
import com.bindi.collegeplanner.helperClasses.SearchModel;
import com.bindi.collegeplanner.itemClasses.CollegeItem;
import com.bindi.collegeplanner.itemClasses.LinkItem;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;
import ir.mirrajabi.searchdialog.core.Searchable;

public class AddPortalActivity extends AppCompatActivity {

    Globals globals = Globals.getInstance();

    TextInputEditText collegeName;
    TextInputLayout resourceNameLayout;
    TextInputEditText resourceName;
    TextInputLayout resourceLinkLayout;
    TextInputEditText resourceLink;

    FloatingActionButton addResource;

    Context c;
    Activity a;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.DarkAppTheme);
        setContentView(R.layout.activity_add_portal);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        c = this;
        a = this;

        setTitle("Add Portal");

        collegeName = findViewById(R.id.collegeName);
        collegeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SimpleSearchDialogCompat<>(a, "Search For College", "Enter College Name",
                        null, initCollegeData(), new SearchResultListener<Searchable>() {
                    @Override
                    public void onSelected(BaseSearchDialogCompat baseSearchDialogCompat, Searchable searchable, int i) {
                        //collegeName.setText("" + searchable.getTitle());
                        int position = -1;
                        for (int j = 0; j < globals.collegeItems.size(); j++){
                            if (globals.collegeItems.get(j).getCollegeName().equals(searchable.getTitle())){
                                position = j;
                            }

                        }
                        collegeName.setText(globals.collegeItems.get(position).getCollegeName());
                        baseSearchDialogCompat.dismiss();
                    }
                }).show();
            }
        });

        resourceNameLayout = findViewById(R.id.resourceNameLayout);
        resourceName = findViewById(R.id.resourceName);
        resourceLinkLayout = findViewById(R.id.resourceLinkLayout);
        resourceLink = findViewById(R.id.resourceLink);;

        addResource = findViewById(R.id.addResource);
        addResource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!collegeName.getText().toString().isEmpty() && !resourceName.getText().toString().equals("") && !resourceLink.getText().toString().equals("")){
                    //String str = resourceName.getText().toString() + resourceLink.getText().toString();
                    //Toast.makeText(c, str, Toast.LENGTH_SHORT).show();

                    int position = 0;

                    for (int i = 0; i < globals.collegeItems.size(); i++){
                        if(collegeName.getText().toString().equals(globals.collegeItems.get(i).getCollegeName())){
                            position = i;
                        }
                    }

                    globals.collegeItems.get(position).addResourceItem(new LinkItem(resourceName.getText().toString(), resourceLink.getText().toString(), true));
                    globals.saveAll(c);
                    Intent intent = new Intent(AddPortalActivity.this, MainActivity.class);
                    intent.putExtra(GlobalKeys.loadingDirection,GlobalKeys.portalsDirection);
                    startActivity(intent);
                }else{
                    Toast.makeText(c, "Must Enter College, Portal Name And Link", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private ArrayList<Searchable> initCollegeData(){
        CollegeSearchDb collegeDb = new CollegeSearchDb();
        ArrayList<Searchable> items = new ArrayList<>();
        for (CollegeItem str : globals.collegeItems){
            items.add(new SearchModel(str.getCollegeName()));
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