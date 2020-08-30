package com.bindi.collegeplanner.displayClasses;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bindi.collegeplanner.MainActivity;
import com.bindi.collegeplanner.R;
import com.bindi.collegeplanner.adapterClasses.CollegeInfoAdapter;
import com.bindi.collegeplanner.addContentClasses.AddCollegeActivity;
import com.bindi.collegeplanner.addContentClasses.AddCounselorActivity;
import com.bindi.collegeplanner.addContentClasses.AddEssayActivity;
import com.bindi.collegeplanner.addContentClasses.AddRecActivity;
import com.bindi.collegeplanner.addContentClasses.AddResourceActivity;
import com.bindi.collegeplanner.databaseClasses.GlobalKeys;
import com.bindi.collegeplanner.databaseClasses.Globals;
import com.bindi.collegeplanner.editorClasses.CollegeEditActivity;
import com.bindi.collegeplanner.itemClasses.CollegeInfoItem;
import com.bindi.collegeplanner.itemClasses.CollegeItem;
import com.bindi.collegeplanner.itemClasses.LinkItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class CollegeActivity extends AppCompatActivity {

    Globals globals = Globals.getInstance();

    int position;

    FloatingActionButton dreamSchoolFab;
    FloatingActionButton feeWaiverFab;

    com.github.clans.fab.FloatingActionButton essayFab;
    com.github.clans.fab.FloatingActionButton resourceFab;
    com.github.clans.fab.FloatingActionButton recFab;
    com.github.clans.fab.FloatingActionButton admissionsCounselorFab;

    private RecyclerView mRecyclerView;
    private CollegeInfoAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    ColorStateList myList;
    ColorStateList waiverList;

    Context c;
    Activity a;

    String collegeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_college);

        c = this;
        a = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.collegeToolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        String data=null;
        boolean other = true;
        if(getIntent()!=null && getIntent().getExtras()!=null){
            Bundle bundle = getIntent().getExtras();
            if(!bundle.getString(GlobalKeys.loadingDirection).equals(null)){
                data= bundle.getString(GlobalKeys.loadingDirection);
                position = Integer.parseInt(data);
            }
        }

        toolbar.setTitle(globals.collegeItems.get(position).getCollegeName());

        collegeName = globals.collegeItems.get(position).getCollegeName();

        feeWaiverFab = findViewById(R.id.feeWaiverFab);
        feeWaiverFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (globals.collegeItems.get(position).hasFeeWaiver()){
                    globals.collegeItems.get(position).setHasFeeWaiver(false);
                    globals.saveColleges(globals.collegeItems, GlobalKeys.collegeKey, c);
                    globals.loadColleges(GlobalKeys.collegeKey, c);
                    Snackbar snackbar = Snackbar.make((CoordinatorLayout) findViewById(R.id.mainCollegeLayout), "You do not have a fee waiver", BaseTransientBottomBar.LENGTH_SHORT);
                    snackbar.show();
                    setWaiverIconColor();
                }else{
                    globals.collegeItems.get(position).setHasFeeWaiver(true);
                    globals.saveColleges(globals.collegeItems, GlobalKeys.collegeKey, c);
                    globals.loadColleges(GlobalKeys.collegeKey, c);
                    Snackbar snackbar = Snackbar.make((CoordinatorLayout) findViewById(R.id.mainCollegeLayout), "You have a fee waiver", BaseTransientBottomBar.LENGTH_SHORT);
                    snackbar.show();
                    setWaiverIconColor();
                }
            }
        });
        setWaiverIconColor();

        dreamSchoolFab = findViewById(R.id.dreamSchoolFab);
        if (globals.collegeItems.get(position).isDreamSchool()){
            dreamSchoolFab.setImageResource(R.drawable.ic_favorite_black_24dp);
        }else{
            dreamSchoolFab.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        }
        setIconColor();
        dreamSchoolFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!globals.collegeItems.get(position).isDreamSchool()){
                    for (CollegeItem collegeItem : globals.collegeItems){
                        collegeItem.setDreamSchool(false);
                    }
                    globals.collegeItems.get(position).setDreamSchool(true);
                    globals.saveColleges(globals.collegeItems, GlobalKeys.collegeKey, c);
                    globals.loadColleges(GlobalKeys.collegeKey, c);
                    dreamSchoolFab.setImageResource(R.drawable.ic_favorite_black_24dp);
                    globals.sortCollegeList();
                    Snackbar snackbar = Snackbar.make((CoordinatorLayout) findViewById(R.id.mainCollegeLayout), "Set as dream school", BaseTransientBottomBar.LENGTH_SHORT);
                    snackbar.show();
                }else{
                    globals.collegeItems.get(position).setDreamSchool(false);
                    globals.saveColleges(globals.collegeItems, GlobalKeys.collegeKey, c);
                    globals.loadColleges(GlobalKeys.collegeKey, c);
                    dreamSchoolFab.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                    globals.sortCollegeList();
                    Snackbar snackbar = Snackbar.make((CoordinatorLayout) findViewById(R.id.mainCollegeLayout), "No Longer Dream School", BaseTransientBottomBar.LENGTH_SHORT);
                    snackbar.show();
                }
            }
        });

        mRecyclerView = findViewById(R.id.collegeInfoRecycler);
        mRecyclerView.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(c);

        final CollegeItem collegeItem = globals.collegeItems.get(position);
        final ArrayList<CollegeInfoItem> collegeInfoItems = new ArrayList<>();
        collegeInfoItems.add(new CollegeInfoItem(R.drawable.ic_label_black_24dp, collegeItem.getCollegeType(), "College Type "));
        collegeInfoItems.add(new CollegeInfoItem(R.drawable.ic_school_black_24dp, collegeItem.getCollegeMajor(), "Major "));
        if (!collegeItem.getAppType().equals("")){
            collegeInfoItems.add(new CollegeInfoItem(R.drawable.ic_access_time_black_24dp, collegeItem.getAppType(),"Application Type "));
        }
        collegeInfoItems.add(new CollegeInfoItem(R.drawable.ic_notifications_black_24dp, collegeItem.getStatus(), "Application Status "));
        if (!collegeItem.getAppFee().equals("$0.00") && !collegeItem.getAppFee().equals("$0.0")){
            collegeInfoItems.add(new CollegeInfoItem(R.drawable.ic_attach_money_black_24dp, collegeItem.getAppFee(),"Application Fee "));
        }
        if (!collegeItem.getTuition().equals("$0.00") && !collegeItem.getTuition().equals("$0.0")){
            collegeInfoItems.add(new CollegeInfoItem(R.drawable.ic_attach_money_black_24dp, collegeItem.getTuition(), "Tuition "));
        }
        for (int i = 0; i < collegeItem.getResourceItems().size(); i++){
            collegeInfoItems.add(new CollegeInfoItem(R.drawable.ic_public_black_24dp, collegeItem.getResourceItems().get(i).getName() + ": " + collegeItem.getResourceItems().get(i).getWebsite(), "Resource "));
        }
        for (int i = 0; i < collegeItem.getEssayItems().size(); i++){
            collegeInfoItems.add(new CollegeInfoItem(R.drawable.ic_document, collegeItem.getEssayItems().get(i).getTitle(), "Essay Topic "));
        }
        for (int i = 0; i < collegeItem.getContactItems().size(); i++){
            if (collegeItem.getContactItems().get(i).getContactType().equals("AC")){
                collegeInfoItems.add(new CollegeInfoItem(R.drawable.ic_person_black_24dp, collegeItem.getContactItems().get(i).getName(), "Admissions Counselor "));
            }else{
                collegeInfoItems.add(new CollegeInfoItem(R.drawable.ic_person_black_24dp, collegeItem.getContactItems().get(i).getName(), "Recommender "));
            }
        }
        if (!collegeItem.getNotes().equals("")){
            collegeInfoItems.add(new CollegeInfoItem(R.drawable.ic_edit_black_24dp, collegeItem.getNotes(),"Notes "));
        }

        mAdapter = new CollegeInfoAdapter(collegeInfoItems, c);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new CollegeInfoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (collegeInfoItems.get(position).getTitle().startsWith("Resource")){

                    final int pos = position;

                    new AlertDialog.Builder(c)
                            .setTitle(collegeInfoItems.get(pos).getInfo().substring(0, collegeInfoItems.get(pos).getInfo().indexOf(":")))
                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setMessage(collegeInfoItems.get(pos).getInfo().substring(collegeInfoItems.get(pos).getInfo().indexOf(":") + 1))
                            .setPositiveButton("VISIT RESOURCE", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Continue with delete operation
                                    Intent i = new Intent(Intent.ACTION_VIEW);
                                    i.setData(Uri.parse("https://" + collegeInfoItems.get(pos).getInfo().substring(collegeInfoItems.get(pos).getInfo().indexOf(":") + 2)));
                                    startActivity(i);
                                }
                            })

                            // A null listener allows the button to dismiss the dialog and take no further action.
                            .setNegativeButton("DELETE RESOURCE", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ArrayList<Integer> vals = new ArrayList<>();
                                    for (int i = 0; i < collegeInfoItems.size(); i++){
                                        if (collegeInfoItems.get(i).getTitle().startsWith("Resource")){
                                            vals.add(i);
                                        }
                                    }
                                    for (int x = 0; x < vals.size(); x++){
                                        if (vals.get(x) == pos){
                                            collegeItem.getResourceItems().remove(x);
                                            globals.saveAll(c);
                                            Intent i = new Intent(a, MainActivity.class);
                                            i.putExtra(GlobalKeys.loadingDirection, GlobalKeys.collegesDirection);
                                            startActivity(i);
                                        }
                                    }
                                }
                            })
                            .show();
                }
                if (collegeInfoItems.get(position).getTitle().startsWith("Essay Topic")){
                    final int pos = position;
                    String title = "";
                    String topic = "";
                    String wordCount = "";
                    ArrayList<Integer> vals = new ArrayList<>();
                    for (int i = 0; i < collegeInfoItems.size(); i++){
                        if (collegeInfoItems.get(i).getTitle().startsWith("Essay Topic")){
                            vals.add(i);
                        }
                    }
                    for (int x = 0; x < vals.size(); x++){
                        if (vals.get(x) == pos){
                            title = collegeItem.getEssayItems().get(x).getTitle();
                            topic = collegeItem.getEssayItems().get(x).getTopic();
                            wordCount = collegeItem.getEssayItems().get(x).getWordCount();
                        }
                    }

                    new AlertDialog.Builder(c)
                            .setTitle(title)
                            .setMessage("Topic:\n" + topic + "\n\nWord Count Limit: " + wordCount)
                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.

                            // A null listener allows the button to dismiss the dialog and take no further action.
                            .setNegativeButton("DELETE ESSAY TOPIC", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ArrayList<Integer> vals = new ArrayList<>();
                                    for (int i = 0; i < collegeInfoItems.size(); i++){
                                        if (collegeInfoItems.get(i).getTitle().startsWith("Essay Topic")){
                                            vals.add(i);
                                        }
                                    }
                                    for (int x = 0; x < vals.size(); x++){
                                        if (vals.get(x) == pos){
                                            collegeItem.getEssayItems().remove(x);
                                            globals.saveAll(c);
                                            Intent i = new Intent(a, MainActivity.class);
                                            i.putExtra(GlobalKeys.loadingDirection, GlobalKeys.collegesDirection);
                                            startActivity(i);
                                        }
                                    }
                                }
                            })
                            .show();
                }
                if (collegeInfoItems.get(position).getTitle().startsWith("Admissions Counselor")){

                }
                if (collegeInfoItems.get(position).getTitle().startsWith("Recommender")){

                }
            }
        });

        essayFab = findViewById(R.id.essayFab);
        essayFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CollegeActivity.this, AddEssayActivity.class);
                intent.putExtra(GlobalKeys.loadingDirection, position + "");
                startActivity(intent);
            }
        });
        resourceFab = findViewById(R.id.resourceFab);
        resourceFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CollegeActivity.this, AddResourceActivity.class);
                intent.putExtra(GlobalKeys.loadingDirection, position + "");
                startActivity(intent);
            }
        });
        recFab = findViewById(R.id.recFab);
        recFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CollegeActivity.this, AddRecActivity.class);
                intent.putExtra(GlobalKeys.loadingDirection, position + "");
                startActivity(intent);
            }
        });
        admissionsCounselorFab = findViewById(R.id.admissionsCounselorFab);
        admissionsCounselorFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CollegeActivity.this, AddCounselorActivity.class);
                intent.putExtra(GlobalKeys.loadingDirection, position + "");
                startActivity(intent);
            }
        });
    }

    public void setIconColor(){
        int[][] states = new int[][] {
                new int[] { android.R.attr.state_enabled}, // enabled
                new int[] {-android.R.attr.state_enabled}, // disabled
                new int[] {-android.R.attr.state_checked}, // unchecked
                new int[] { android.R.attr.state_pressed}  // pressed
        };

        int[] colors = new int[] {
                Color.parseColor(globals.collegeItems.get(position).getColorStr()),
                Color.parseColor(globals.collegeItems.get(position).getColorStr()),
                Color.parseColor(globals.collegeItems.get(position).getColorStr()),
                Color.parseColor(globals.collegeItems.get(position).getColorStr())
        };

        myList = new ColorStateList(states, colors);
        dreamSchoolFab.setBackgroundTintList(myList);
    }

    public void setWaiverIconColor(){
        if(globals.collegeItems.get(position).hasFeeWaiver()){
            int[][] states = new int[][] {
                    new int[] { android.R.attr.state_enabled}, // enabled
                    new int[] {-android.R.attr.state_enabled}, // disabled
                    new int[] {-android.R.attr.state_checked}, // unchecked
                    new int[] { android.R.attr.state_pressed}  // pressed
            };

            int[] colors = new int[] {
                    Color.parseColor("#4CAF50"),
                    Color.parseColor("#4CAF50"),
                    Color.parseColor("#4CAF50"),
                    Color.parseColor("#4CAF50")
            };
            waiverList = new ColorStateList(states, colors);
            feeWaiverFab.setBackgroundTintList(waiverList);
        }else{
            int[][] states = new int[][] {
                    new int[] { android.R.attr.state_enabled}, // enabled
                    new int[] {-android.R.attr.state_enabled}, // disabled
                    new int[] {-android.R.attr.state_checked}, // unchecked
                    new int[] { android.R.attr.state_pressed}  // pressed
            };

            int[] colors = new int[] {
                    Color.parseColor("#F44336"),
                    Color.parseColor("#F44336"),
                    Color.parseColor("#F44336"),
                    Color.parseColor("#F44336")
            };
            waiverList = new ColorStateList(states, colors);
            feeWaiverFab.setBackgroundTintList(waiverList);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.college_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(GlobalKeys.loadingDirection, GlobalKeys.collegesDirection);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.edit){
            Intent i = new Intent(a, CollegeEditActivity.class);
            i.putExtra(GlobalKeys.loadingDirection, position + "");
            startActivity(i);
        }
        if (item.getItemId() == R.id.delete){
            globals.collegeItems.remove(position);
            globals.saveColleges(globals.collegeItems, GlobalKeys.collegeKey, this);
            globals.loadColleges(GlobalKeys.collegeKey, this);
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(GlobalKeys.loadingDirection, GlobalKeys.collegesDirection);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    /*@Override
    public void onBackPressed(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(GlobalKeys.loadingDirection, GlobalKeys.collegesDirection);
        startActivity(intent);
    }*/

}
