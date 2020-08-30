package com.bindi.collegeplanner.displayClasses;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bindi.collegeplanner.MainActivity;
import com.bindi.collegeplanner.NotesFragment;
import com.bindi.collegeplanner.R;
import com.bindi.collegeplanner.databaseClasses.GlobalKeys;
import com.bindi.collegeplanner.databaseClasses.Globals;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import org.w3c.dom.Text;

public class NoteActivity extends AppCompatActivity {

    Globals globals = Globals.getInstance();

    int position;

    RelativeLayout mainLayout;

    EditText editNoteText;

    Context c;

    FloatingActionButton editNoteFab;

    boolean inEditMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.DarkAppTheme);
        setContentView(R.layout.activity_note);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        c = this;
        String data=null;
        boolean other = true;
        if(getIntent()!=null && getIntent().getExtras()!=null){
            Bundle bundle = getIntent().getExtras();
            if(!bundle.getString(GlobalKeys.loadingDirection).equals(null)){
                data= bundle.getString(GlobalKeys.loadingDirection);
                position = Integer.parseInt(data);
            }
        }
        setTitle(globals.noteItems.get(position).getTitle());

        editNoteText = findViewById(R.id.noteTextEdit);
        editNoteText.setText(globals.noteItems.get(position).getText());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.notes_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            globals.noteItems.get(position).setText(editNoteText.getText().toString());
            globals.saveNotes(globals.noteItems, GlobalKeys.notesKey, c);
            globals.loadNotes(GlobalKeys.notesKey, c);

            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(GlobalKeys.loadingDirection, GlobalKeys.notesDirection);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.delete){
            globals.noteItems.remove(position);
            globals.saveNotes(globals.noteItems, GlobalKeys.notesKey, this);
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(GlobalKeys.loadingDirection, GlobalKeys.notesDirection);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.editTitle){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(c);
            final View view = getLayoutInflater().inflate(R.layout.change_note_title_dialog, null);
            final TextInputEditText noteTitleText = view.findViewById(R.id.noteTitle);
            noteTitleText.setText(globals.noteItems.get(position).getTitle());
            alertDialogBuilder
                    .setTitle("Change Name")
                    .setView(view)
                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (!noteTitleText.getText().toString().equals("")){
                                globals.noteItems.get(position).setTitle(noteTitleText.getText().toString());
                                globals.saveNotes(globals.noteItems, GlobalKeys.notesKey, c);
                                globals.loadNotes(GlobalKeys.notesKey, c);
                                setTitle(globals.noteItems.get(position).getTitle());
                            }else{
                                Toast.makeText(c, "Must Enter Title", Toast.LENGTH_LONG).show();
                            }
                        }
                    })
                    .setNegativeButton("Cancel", null);
            Dialog dialog = alertDialogBuilder.create();
            dialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

}
