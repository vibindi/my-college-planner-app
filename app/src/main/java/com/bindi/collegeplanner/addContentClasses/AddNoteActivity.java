package com.bindi.collegeplanner.addContentClasses;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bindi.collegeplanner.MainActivity;
import com.bindi.collegeplanner.R;
import com.bindi.collegeplanner.databaseClasses.GlobalKeys;
import com.bindi.collegeplanner.databaseClasses.Globals;
import com.bindi.collegeplanner.itemClasses.NoteItem;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

public class AddNoteActivity extends AppCompatActivity {

    Globals globals = Globals.getInstance();
    TextInputEditText noteTitle;
    TextInputEditText noteText;
    FloatingActionButton addNoteFab;

    Context c;
    Activity a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.DarkAppTheme);
        setContentView(R.layout.activity_add_note);
        setTitle("Add Note");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        c = this;
        a = this;
        addNoteFab = findViewById(R.id.addNote);
        noteTitle = findViewById(R.id.noteTitle);
        noteText = findViewById(R.id.noteText);
        addNoteFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!noteTitle.getText().toString().isEmpty() && !noteText.getText().toString().isEmpty() && (noteTitle.getText().toString().trim().length() > 0) && (noteText.getText().toString().trim().length() > 0)){
                    globals.noteItems.add(new NoteItem(noteTitle.getText().toString(), noteText.getText().toString()));
                    globals.saveNotes(globals.noteItems, GlobalKeys.notesKey, c);
                    Intent intent = new Intent(a, MainActivity.class);
                    intent.putExtra(GlobalKeys.loadingDirection, GlobalKeys.notesDirection);
                    startActivity(intent);

                }else{
                    Toast.makeText(c, "Must Enter Title And Text", Toast.LENGTH_SHORT).show();
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
