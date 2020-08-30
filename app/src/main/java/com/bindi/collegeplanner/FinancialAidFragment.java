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
import com.bindi.collegeplanner.adapterClasses.ScholarshipsAdapter;
import com.bindi.collegeplanner.addContentClasses.AddNoteActivity;
import com.bindi.collegeplanner.addContentClasses.AddScholarshipActivity;
import com.bindi.collegeplanner.databaseClasses.GlobalKeys;
import com.bindi.collegeplanner.databaseClasses.Globals;
import com.bindi.collegeplanner.displayClasses.NoteActivity;
import com.bindi.collegeplanner.displayClasses.ScholarshipActivity;
import com.github.clans.fab.FloatingActionButton;


/**
 * A simple {@link Fragment} subclass.
 */
public class FinancialAidFragment extends Fragment {

    Globals globals = Globals.getInstance();

    private RecyclerView mRecyclerView;
    private ScholarshipsAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    TextView statusDisplayText;

    FloatingActionButton addScholarshipFab;

    public FinancialAidFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_financial_aid, container, false);
        getActivity().setTitle("Scholarships");

        statusDisplayText = v.findViewById(R.id.statusDisplayText);

        if (globals.scholarshipItems.isEmpty()){
            statusDisplayText.setText("Hit the + button to add a new scholarship.");
        }

        mRecyclerView = v.findViewById(R.id.scholarshipsRecycler);
        mRecyclerView.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new ScholarshipsAdapter(globals.scholarshipItems, getContext());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new ScholarshipsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getActivity(), ScholarshipActivity.class);
                intent.putExtra(GlobalKeys.loadingDirection, position + "");
                startActivity(intent);
            }
        });

        addScholarshipFab = v.findViewById(R.id.addFinancialAid);
        addScholarshipFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddScholarshipActivity.class);
                startActivity(intent);
            }
        });
        return v;
    }
}
