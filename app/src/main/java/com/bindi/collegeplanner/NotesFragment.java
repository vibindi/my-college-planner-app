package com.bindi.collegeplanner;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bindi.collegeplanner.adapterClasses.NotesAdapter;
import com.bindi.collegeplanner.addContentClasses.AddNoteActivity;
import com.bindi.collegeplanner.databaseClasses.GlobalKeys;
import com.bindi.collegeplanner.databaseClasses.Globals;
import com.bindi.collegeplanner.displayClasses.NoteActivity;
import com.github.clans.fab.FloatingActionButton;


/**
 * A simple {@link Fragment} subclass.
 */
public class NotesFragment extends Fragment {

    Globals globals = Globals.getInstance();

    private RecyclerView mRecyclerView;
    private NotesAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    TextView statusDisplayText;

    FloatingActionButton newNoteFab;

    public NotesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_notes, container, false);
        getActivity().setTitle("Notes");

        statusDisplayText = v.findViewById(R.id.statusDisplayText);

        if (globals.noteItems.isEmpty()){
            statusDisplayText.setText("Hit the + button to add a new note.");
        }

        mRecyclerView = v.findViewById(R.id.notesRecycler);
        mRecyclerView.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new NotesAdapter(globals.noteItems, getContext());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new NotesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getActivity(), NoteActivity.class);
                intent.putExtra(GlobalKeys.loadingDirection, position + "");
                startActivity(intent);
                //Toast.makeText(getContext(), position + "", Toast.LENGTH_LONG).show();
            }
        });

        newNoteFab = v.findViewById(R.id.addNote);
        newNoteFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddNoteActivity.class);
                startActivity(intent);
            }
        });
        return v;
    }
}
