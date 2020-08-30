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
import com.bindi.collegeplanner.itemClasses.ContactItem;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

public class AddCounselorActivity extends AppCompatActivity {

    Globals globals = Globals.getInstance();

    TextInputEditText name;
    TextInputEditText phone;
    TextInputEditText email;
    TextInputEditText notes;

    FloatingActionButton addAC;

    Context c;
    Activity a;

    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.DarkAppTheme);
        setContentView(R.layout.activity_add_ac);

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

        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        notes = findViewById(R.id.noteText);

        addAC = findViewById(R.id.addContact);
        addAC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!name.getText().toString().equals("")){
                    //String str = resourceName.getText().toString() + resourceLink.getText().toString();
                    //Toast.makeText(c, str, Toast.LENGTH_SHORT).show();
                    ContactItem contactItem = new ContactItem("AC", name.getText().toString());
                    if (!phone.getText().toString().equals("")){
                        contactItem.setPhone(phone.getText().toString());
                    }
                    if (!email.getText().toString().equals("")){
                        contactItem.setEmail(email.getText().toString());
                    }
                    if (!notes.getText().toString().equals("")){
                        contactItem.setNotes(notes.getText().toString());
                    }
                    globals.collegeItems.get(position).addContactItem(contactItem);
                    globals.saveAll(c);
                    Intent intent = new Intent(AddCounselorActivity.this, CollegeActivity.class);
                    intent.putExtra(GlobalKeys.loadingDirection, position + "");
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