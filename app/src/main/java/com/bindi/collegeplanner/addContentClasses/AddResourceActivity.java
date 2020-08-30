package com.bindi.collegeplanner.addContentClasses;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bindi.collegeplanner.R;
import com.bindi.collegeplanner.databaseClasses.GlobalKeys;
import com.bindi.collegeplanner.databaseClasses.Globals;
import com.bindi.collegeplanner.displayClasses.CollegeActivity;
import com.bindi.collegeplanner.itemClasses.LinkItem;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class AddResourceActivity extends AppCompatActivity {

    Globals globals = Globals.getInstance();

    TextInputLayout resourceNameLayout;
    TextInputEditText resourceName;
    TextInputLayout resourceLinkLayout;
    TextInputEditText resourceLink;

    MaterialCheckBox portalCheck;

    FloatingActionButton addResource;

    Context c;
    Activity a;

    int position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.DarkAppTheme);
        setContentView(R.layout.activity_add_resource);

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

        setTitle("Add Resource For " + globals.collegeItems.get(position).getCollegeName());

        resourceNameLayout = findViewById(R.id.resourceNameLayout);
        resourceName = findViewById(R.id.resourceName);
        resourceLinkLayout = findViewById(R.id.resourceLinkLayout);
        resourceLink = findViewById(R.id.resourceLink);
        portalCheck = findViewById(R.id.portalCheck);

        addResource = findViewById(R.id.addResource);
        addResource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!resourceName.getText().toString().equals("") && !resourceLink.getText().toString().equals("")){
                    //String str = resourceName.getText().toString() + resourceLink.getText().toString();
                    //Toast.makeText(c, str, Toast.LENGTH_SHORT).show();
                    globals.collegeItems.get(position).addResourceItem(new LinkItem(resourceName.getText().toString(), resourceLink.getText().toString(), portalCheck.isChecked()));
                    globals.saveAll(c);
                    Intent intent = new Intent(AddResourceActivity.this, CollegeActivity.class);
                    intent.putExtra(GlobalKeys.loadingDirection, position + "");
                    startActivity(intent);
                }else{
                    Toast.makeText(c, "Must Enter Name And Link", Toast.LENGTH_SHORT).show();
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
