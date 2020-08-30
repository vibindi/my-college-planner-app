package com.bindi.collegeplanner.addContentClasses;

import androidx.appcompat.app.AppCompatActivity;

import com.bindi.collegeplanner.R;
import com.bindi.collegeplanner.databaseClasses.GlobalKeys;
import com.bindi.collegeplanner.databaseClasses.Globals;
import com.bindi.collegeplanner.displayClasses.CollegeActivity;
import com.bindi.collegeplanner.itemClasses.EssayItem;
import com.bindi.collegeplanner.itemClasses.LinkItem;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class AddEssayActivity extends AppCompatActivity {

    Globals globals = Globals.getInstance();

    TextInputEditText essayTitle;
    TextInputEditText essayTopic;
    TextInputEditText wordCountLimit;

    FloatingActionButton addEssay;

    Context c;
    Activity a;

    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.DarkAppTheme);
        setContentView(R.layout.activity_add_essay);

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

        setTitle("Add Essay For " + globals.collegeItems.get(position).getCollegeName());

        essayTitle = findViewById(R.id.essayTitle);
        essayTopic = findViewById(R.id.essayTopic);
        wordCountLimit = findViewById(R.id.essayWordCount);

        addEssay = findViewById(R.id.addEssay);
        addEssay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!essayTitle.getText().toString().equals("") && !essayTopic.getText().toString().equals("") && !wordCountLimit.getText().toString().equals("")){
                    //String str = resourceName.getText().toString() + resourceLink.getText().toString();
                    //Toast.makeText(c, str, Toast.LENGTH_SHORT).show();
                    globals.collegeItems.get(position).addEssayItem(new EssayItem(essayTitle.getText().toString(), essayTopic.getText().toString(), wordCountLimit.getText().toString()));
                    globals.saveAll(c);
                    Intent intent = new Intent(AddEssayActivity.this, CollegeActivity.class);
                    intent.putExtra(GlobalKeys.loadingDirection, position + "");
                    startActivity(intent);
                }else{
                    Toast.makeText(c, "Must Enter Title, Topic, and Word Count Limit", Toast.LENGTH_SHORT).show();
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