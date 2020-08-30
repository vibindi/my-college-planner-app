package com.bindi.collegeplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.bindi.collegeplanner.databaseClasses.Globals;
import com.google.android.material.button.MaterialButton;

public class LibraryActivity extends AppCompatActivity {

    Globals globals = Globals.getInstance();

    Button fabLicense;
    Button fabWebsite;

    Button colorpickerLicense;
    Button colorpickerWebsite;

    Button materialviewLicense;
    Button materialviewWebsite;

    Button searchdialogLicense;
    Button searchdialogWebsite;

    Button flaticonLicense;
    Button flaticonWebsite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.DarkAppTheme);
        setContentView(R.layout.activity_os_lib);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Open Source Libraries");

        fabLicense = findViewById(R.id.fablicense);
        fabLicense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uriUrl = Uri.parse("https://github.com/Clans/FloatingActionButton/blob/master/LICENSE");
                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                startActivity(launchBrowser);
            }
        });
        fabWebsite = findViewById(R.id.fabwebsite);
        fabWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uriUrl = Uri.parse("https://github.com/Clans/FloatingActionButton");
                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                startActivity(launchBrowser);
            }
        });

        colorpickerLicense = findViewById(R.id.colorpickerlicense);
        colorpickerLicense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uriUrl = Uri.parse("https://github.com/jaredrummler/ColorPicker/blob/master/LICENSE");
                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                startActivity(launchBrowser);
            }
        });
        colorpickerWebsite = findViewById(R.id.colorpickerwebsite);
        colorpickerWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uriUrl = Uri.parse("https://github.com/jaredrummler/ColorPicker");
                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                startActivity(launchBrowser);
            }
        });

        materialviewLicense = findViewById(R.id.materialcalendarviewlicense);
        materialviewLicense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uriUrl = Uri.parse("https://github.com/Applandeo/Material-Calendar-View/blob/master/LICENSE.md");
                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                startActivity(launchBrowser);
            }
        });
        materialviewWebsite = findViewById(R.id.materialcalendarviewwebsite);
        materialviewWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uriUrl = Uri.parse("https://github.com/Applandeo/Material-Calendar-View");
                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                startActivity(launchBrowser);
            }
        });

        searchdialogLicense = findViewById(R.id.searchdialoglicense);
        searchdialogLicense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uriUrl = Uri.parse("https://github.com/mirrajabi/search-dialog/blob/master/LICENSE.md");
                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                startActivity(launchBrowser);
            }
        });
        searchdialogWebsite = findViewById(R.id.searchdialogwebsite);
        searchdialogWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uriUrl = Uri.parse("https://github.com/mirrajabi/search-dialog");
                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                startActivity(launchBrowser);
            }
        });

        flaticonLicense = findViewById(R.id.flaticonlicense);
        flaticonLicense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uriUrl = Uri.parse("https://www.flaticon.com/authors/freepik");
                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                startActivity(launchBrowser);
            }
        });
        flaticonWebsite = findViewById(R.id.flaticonwebsite);
        flaticonWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uriUrl = Uri.parse("https://www.flaticon.com/");
                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                startActivity(launchBrowser);
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