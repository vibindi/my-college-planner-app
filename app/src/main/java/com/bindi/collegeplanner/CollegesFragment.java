package com.bindi.collegeplanner;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bindi.collegeplanner.adapterClasses.CollegesAdapter;
import com.bindi.collegeplanner.adapterClasses.ResourcesAdapter;
import com.bindi.collegeplanner.addContentClasses.AddCollegeActivity;
import com.bindi.collegeplanner.addContentClasses.AddNoteActivity;
import com.bindi.collegeplanner.databaseClasses.ApplicationTypeDb;
import com.bindi.collegeplanner.databaseClasses.CollegeDb;
import com.bindi.collegeplanner.databaseClasses.CollegeSearchDb;
import com.bindi.collegeplanner.databaseClasses.CollegeTypeDb;
import com.bindi.collegeplanner.databaseClasses.GlobalKeys;
import com.bindi.collegeplanner.databaseClasses.Globals;
import com.bindi.collegeplanner.databaseClasses.MajorDb;
import com.bindi.collegeplanner.databaseClasses.StatusDb;
import com.bindi.collegeplanner.displayClasses.CollegeActivity;
import com.bindi.collegeplanner.displayClasses.NoteActivity;
import com.bindi.collegeplanner.helperClasses.SearchModel;
import com.bindi.collegeplanner.itemClasses.CollegeItem;
import com.github.clans.fab.FloatingActionButton;

import java.util.ArrayList;

import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;
import ir.mirrajabi.searchdialog.core.Searchable;


/**
 * A simple {@link Fragment} subclass.
 */
public class CollegesFragment extends Fragment {

    FloatingActionButton addCollege;

    Globals globals = Globals.getInstance();

    TextView statusDisplayText;

    private RecyclerView mRecyclerView;
    private CollegesAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    Context c;
    Activity a;

    boolean collegesExist;

    public CollegesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_colleges, container, false);
        getActivity().setTitle("Colleges");
        setHasOptionsMenu(true);

        a = getActivity();
        c = getContext();

        statusDisplayText = v.findViewById(R.id.statusDisplayText);

        if (globals.collegeItems.isEmpty()){
            statusDisplayText.setText("Hit the + button to add a new college.");
            collegesExist = false;
        }else{
            collegesExist = true;
        }


        mRecyclerView = v.findViewById(R.id.collegesLayout);
        mRecyclerView.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new CollegesAdapter(globals.collegeItems, getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new CollegesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getActivity(), CollegeActivity.class);
                intent.putExtra(GlobalKeys.loadingDirection, position + "");
                startActivity(intent);
            }
        });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                if (dy<0 && !addCollege.isShown())
                    addCollege.show(true);
                else if(dy>0 && addCollege.isShown())
                    addCollege.hide(true);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });



        addCollege = v.findViewById(R.id.addCollege);
        addCollege.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddCollegeActivity.class);
                startActivity(intent);
            }
        });

        return v;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.college_fragment_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.search){
            new SimpleSearchDialogCompat<>(a, "Search For Your College", "Enter The College Name",
                    null, initCollegeData(), new SearchResultListener<Searchable>() {
                @Override
                public void onSelected(BaseSearchDialogCompat baseSearchDialogCompat, Searchable searchable, int i) {
                    int y = 0;
                    for (int x = 0; x < globals.collegeItems.size(); x++){
                        if (globals.collegeItems.get(x).getCollegeName().equals(searchable.getTitle())){
                            y = x;
                        }
                    }
                    Toast.makeText(c, y + "", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), CollegeActivity.class);
                    intent.putExtra(GlobalKeys.loadingDirection, y + "");
                    startActivity(intent);
                    baseSearchDialogCompat.dismiss();
                }
            }).show();
        }
        if (item.getItemId() == R.id.collegeFilter){
            String[] filterOptions = {"Filter By Major", "Filter By Status", "Filter By College Type", "Filter By Application Type", "No Filter"};

            new AlertDialog.Builder(getContext())
                    .setSingleChoiceItems(filterOptions, 4, null)
                    .setPositiveButton("APPLY", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.dismiss();
                            int selectedPosition = ((AlertDialog)dialog).getListView().getCheckedItemPosition();

                            if (collegesExist){
                                // Do something useful withe the position of the selected radio button
                                if (selectedPosition == 0){
                                    new SimpleSearchDialogCompat<>(a, "Search For Major", "Enter College Major",
                                            null, initMajorData(), new SearchResultListener<Searchable>() {
                                        @Override
                                        public void onSelected(BaseSearchDialogCompat baseSearchDialogCompat, Searchable searchable, int i) {
                                            String filter = searchable.getTitle();
                                            final ArrayList<CollegeItem> collegeItems = new ArrayList<>();
                                            for (CollegeItem c : globals.collegeItems){
                                                if (c.getCollegeMajor().equals(filter)){
                                                    collegeItems.add(c);
                                                }
                                            }
                                            if (collegeItems.isEmpty()){
                                                statusDisplayText.setText("No Colleges Matched That Filter");
                                            }else{
                                                statusDisplayText.setText("");
                                            }
                                            mAdapter = new CollegesAdapter(collegeItems, getContext());
                                            mRecyclerView.setLayoutManager(mLayoutManager);
                                            mRecyclerView.setAdapter(mAdapter);

                                            mAdapter.setOnItemClickListener(new CollegesAdapter.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(int position) {
                                                    Intent intent = new Intent(getActivity(), CollegeActivity.class);
                                                    intent.putExtra(GlobalKeys.loadingDirection, globals.collegeItems.indexOf(collegeItems.get(position)) + "");
                                                    startActivity(intent);
                                                }
                                            });

                                            baseSearchDialogCompat.dismiss();
                                        }
                                    }).show();
                                }else if (selectedPosition == 1){
                                    new SimpleSearchDialogCompat<>(a, "List Your College Status", "Enter College Status",
                                            null, initStatusData(), new SearchResultListener<Searchable>() {
                                        @Override
                                        public void onSelected(BaseSearchDialogCompat baseSearchDialogCompat, Searchable searchable, int i) {
                                            String filter = searchable.getTitle();
                                            final ArrayList<CollegeItem> collegeItems = new ArrayList<>();
                                            for (CollegeItem c : globals.collegeItems){
                                                if (c.getStatus().equals(filter)){
                                                    collegeItems.add(c);
                                                }
                                            }
                                            if (collegeItems.isEmpty()){
                                                statusDisplayText.setText("No Colleges Matched That Filter");
                                            }else{
                                                statusDisplayText.setText("");
                                            }
                                            mAdapter = new CollegesAdapter(collegeItems, getContext());
                                            mRecyclerView.setLayoutManager(mLayoutManager);
                                            mRecyclerView.setAdapter(mAdapter);

                                            mAdapter.setOnItemClickListener(new CollegesAdapter.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(int position) {
                                                    Intent intent = new Intent(getActivity(), CollegeActivity.class);
                                                    intent.putExtra(GlobalKeys.loadingDirection, globals.collegeItems.indexOf(collegeItems.get(position)) + "");
                                                    startActivity(intent);
                                                }
                                            });

                                            baseSearchDialogCompat.dismiss();
                                        }
                                    }).show();
                                }else if (selectedPosition == 2){
                                    new SimpleSearchDialogCompat<>(a, "List Your College Type", "Enter College Type",
                                            null, initCollegeTypeData(), new SearchResultListener<Searchable>() {
                                        @Override
                                        public void onSelected(BaseSearchDialogCompat baseSearchDialogCompat, Searchable searchable, int i) {
                                            String filter = searchable.getTitle();
                                            final ArrayList<CollegeItem> collegeItems = new ArrayList<>();
                                            for (CollegeItem c : globals.collegeItems){
                                                if (c.getCollegeType().equals(filter)){
                                                    collegeItems.add(c);
                                                }
                                            }
                                            if (collegeItems.isEmpty()){
                                                statusDisplayText.setText("No Colleges Matched That Filter");
                                            }else{
                                                statusDisplayText.setText("");
                                            }
                                            mAdapter = new CollegesAdapter(collegeItems, getContext());
                                            mRecyclerView.setLayoutManager(mLayoutManager);
                                            mRecyclerView.setAdapter(mAdapter);

                                            mAdapter.setOnItemClickListener(new CollegesAdapter.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(int position) {
                                                    Intent intent = new Intent(getActivity(), CollegeActivity.class);
                                                    intent.putExtra(GlobalKeys.loadingDirection, globals.collegeItems.indexOf(collegeItems.get(position)) + "");
                                                    startActivity(intent);
                                                }
                                            });

                                            baseSearchDialogCompat.dismiss();
                                        }
                                    }).show();
                                }else if (selectedPosition == 3){
                                    new SimpleSearchDialogCompat<>(a, "List Your Application Type", "Enter Application Type",
                                            null, initApplicationTypeData(), new SearchResultListener<Searchable>() {
                                        @Override
                                        public void onSelected(BaseSearchDialogCompat baseSearchDialogCompat, Searchable searchable, int i) {
                                            String filter = searchable.getTitle();
                                            final ArrayList<CollegeItem> collegeItems = new ArrayList<>();
                                            for (CollegeItem c : globals.collegeItems){
                                                if (c.getAppType().equals(filter)){
                                                    collegeItems.add(c);
                                                }
                                            }
                                            if (collegeItems.isEmpty()){
                                                statusDisplayText.setText("No Colleges Matched That Filter");
                                            }else{
                                                statusDisplayText.setText("");
                                            }
                                            mAdapter = new CollegesAdapter(collegeItems, getContext());
                                            mRecyclerView.setLayoutManager(mLayoutManager);
                                            mRecyclerView.setAdapter(mAdapter);

                                            mAdapter.setOnItemClickListener(new CollegesAdapter.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(int position) {
                                                    Intent intent = new Intent(getActivity(), CollegeActivity.class);
                                                    intent.putExtra(GlobalKeys.loadingDirection, globals.collegeItems.indexOf(collegeItems.get(position)) + "");
                                                    startActivity(intent);
                                                }
                                            });

                                            baseSearchDialogCompat.dismiss();
                                        }
                                    }).show();
                                }else{
                                    if (collegesExist){
                                        statusDisplayText.setText("");
                                    }else{
                                        statusDisplayText.setText("Hit the + button to add a new college!");
                                    }
                                    mAdapter = new CollegesAdapter(globals.collegeItems, getContext());
                                    mRecyclerView.setLayoutManager(mLayoutManager);
                                    mRecyclerView.setAdapter(mAdapter);
                                    mAdapter.setOnItemClickListener(new CollegesAdapter.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(int position) {
                                            Intent intent = new Intent(getActivity(), CollegeActivity.class);
                                            intent.putExtra(GlobalKeys.loadingDirection, position + "");
                                            startActivity(intent);
                                        }
                                    });
                                }
                            }else{
                                Toast.makeText(getContext(), "Must Add Colleges To Filter", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setNeutralButton("RESET", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (collegesExist){
                                statusDisplayText.setText("");
                            }else{
                                statusDisplayText.setText("Hit the + button to add a new college!");
                            }
                            statusDisplayText.setText("");
                            mAdapter = new CollegesAdapter(globals.collegeItems, getContext());
                            mRecyclerView.setLayoutManager(mLayoutManager);
                            mRecyclerView.setAdapter(mAdapter);
                            mAdapter.setOnItemClickListener(new CollegesAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(int position) {
                                    Intent intent = new Intent(getActivity(), CollegeActivity.class);
                                    intent.putExtra(GlobalKeys.loadingDirection, position + "");
                                    startActivity(intent);
                                }
                            });
                        }
                    })
                    .show();
        }

        return super.onOptionsItemSelected(item);
    }

    private ArrayList<Searchable> initCollegeData(){
        CollegeSearchDb collegeDb = new CollegeSearchDb();
        ArrayList<Searchable> items = new ArrayList<>();
        for (CollegeItem collegeItem : globals.collegeItems){
            items.add(new SearchModel(collegeItem.getCollegeName()));
        }
        return items;
    }

    private ArrayList<Searchable> initMajorData(){
        MajorDb majorDb = new MajorDb();
        ArrayList<Searchable> items = new ArrayList<>();
        for (String str : majorDb.getTypesList()){
            items.add(new SearchModel(str));
        }
        return items;
    }

    private ArrayList<Searchable> initStatusData(){
        StatusDb statusDb = new StatusDb();
        ArrayList<Searchable> items = new ArrayList<>();
        for (String str : statusDb.getTypesList()){
            items.add(new SearchModel(str));
        }
        return items;
    }

    private ArrayList<Searchable> initApplicationTypeData(){
        ApplicationTypeDb applicationTypeDb = new ApplicationTypeDb();
        ArrayList<Searchable> items = new ArrayList<>();
        for (String str : applicationTypeDb.getTypesList()){
            items.add(new SearchModel(str));
        }
        return items;
    }

    private ArrayList<Searchable> initCollegeTypeData(){
        CollegeTypeDb collegeTypeDb = new CollegeTypeDb();
        ArrayList<Searchable> items = new ArrayList<>();
        for (String str : collegeTypeDb.getTypesList()){
            items.add(new SearchModel(str));
        }
        return items;
    }


}
