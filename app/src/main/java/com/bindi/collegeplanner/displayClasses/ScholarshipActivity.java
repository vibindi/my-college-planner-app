package com.bindi.collegeplanner.displayClasses;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.bindi.collegeplanner.MainActivity;
import com.bindi.collegeplanner.R;
import com.bindi.collegeplanner.adapterClasses.CollegeInfoAdapter;
import com.bindi.collegeplanner.databaseClasses.GlobalKeys;
import com.bindi.collegeplanner.databaseClasses.Globals;
import com.bindi.collegeplanner.editorClasses.CollegeEditActivity;
import com.bindi.collegeplanner.editorClasses.ScholarshipEditActivity;
import com.bindi.collegeplanner.itemClasses.CollegeInfoItem;
import com.bindi.collegeplanner.itemClasses.CollegeItem;
import com.bindi.collegeplanner.itemClasses.FinancialAidItem;

import java.util.ArrayList;

public class ScholarshipActivity extends AppCompatActivity {

    Globals globals = Globals.getInstance();

    int position;

    private RecyclerView mRecyclerView;
    private CollegeInfoAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    Context c;
    Activity a;

    String scholarshipName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scholarship);

        c = this;
        a = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.scholarshipToolbar);
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

        toolbar.setTitle(globals.scholarshipItems.get(position).getName());
        scholarshipName = globals.scholarshipItems.get(position).getName();

        mRecyclerView = findViewById(R.id.scholarshipInfoRecycler);
        mRecyclerView.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(c);

        final FinancialAidItem scholarshipItem = globals.scholarshipItems.get(position);
        final ArrayList<CollegeInfoItem> collegeInfoItems = new ArrayList<>();

        if (scholarshipItem.getAmount().equals("")){
            collegeInfoItems.add(new CollegeInfoItem(R.drawable.ic_attach_money_black_24dp, "$0.00","Amount "));
        } else{
            collegeInfoItems.add(new CollegeInfoItem(R.drawable.ic_attach_money_black_24dp, scholarshipItem.getAmount(),"Amount "));
        }
        if (!scholarshipItem.getNotes().equals("")){
            collegeInfoItems.add(new CollegeInfoItem(R.drawable.ic_edit_black_24dp, scholarshipItem.getNotes(),"Notes "));
        }
        mAdapter = new CollegeInfoAdapter(collegeInfoItems, c);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
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
            intent.putExtra(GlobalKeys.loadingDirection, GlobalKeys.scholarshipsDirection);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.edit){
            Intent i = new Intent(a, ScholarshipEditActivity.class);
            i.putExtra(GlobalKeys.loadingDirection, position + "");
            startActivity(i);
        }
        if (item.getItemId() == R.id.delete){
            globals.scholarshipItems.remove(position);
            globals.saveScholarships(globals.scholarshipItems, GlobalKeys.scholarshipsKey, this);
            globals.loadScholarships(GlobalKeys.scholarshipsKey, this);
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(GlobalKeys.loadingDirection, GlobalKeys.scholarshipsDirection);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(GlobalKeys.loadingDirection, GlobalKeys.scholarshipsDirection);
        startActivity(intent);
    }
}