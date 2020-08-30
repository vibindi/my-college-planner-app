package com.bindi.collegeplanner;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bindi.collegeplanner.adapterClasses.PortalsAdapter;
import com.bindi.collegeplanner.adapterClasses.ResourcesAdapter;
import com.bindi.collegeplanner.addContentClasses.AddCollegeActivity;
import com.bindi.collegeplanner.addContentClasses.AddPortalActivity;
import com.bindi.collegeplanner.databaseClasses.GlobalKeys;
import com.bindi.collegeplanner.databaseClasses.Globals;
import com.bindi.collegeplanner.helperClasses.WebviewActivity;
import com.bindi.collegeplanner.itemClasses.LinkItem;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class PortalsFragment extends Fragment {

    Globals globals = Globals.getInstance();

    private RecyclerView mRecyclerView;
    private PortalsAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    FloatingActionButton editResourceFab;

    TextView statusDisplayText;

    public PortalsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_resources, container, false);
        getActivity().setTitle("Portals");

        mRecyclerView = v.findViewById(R.id.resourcesLayout);
        mRecyclerView.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(getContext());

        final ArrayList<LinkItem> linkItems = new ArrayList<>();
        for (int i = 0; i < globals.collegeItems.size(); i++){
            for (int j = 0; j < globals.collegeItems.get(i).getResourceItems().size(); j++){
                if (globals.collegeItems.get(i).getResourceItems().get(j).isPortal()){
                    linkItems.add(globals.collegeItems.get(i).getResourceItems().get(j));
                }
            }
        }

        statusDisplayText = v.findViewById(R.id.statusDisplayText);

        if (linkItems.isEmpty()){
            statusDisplayText.setText("Hit the + button to add a new portal.");
        }

        mAdapter = new PortalsAdapter(linkItems, getContext());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new PortalsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent i = new Intent(getActivity(), WebviewActivity.class);
                i.putExtra("resourceWebsite", linkItems.get(position).getWebsite() + "");
                //Intent i = new Intent(Intent.ACTION_VIEW);
                //i.setData(Uri.parse(globals.resourceItems.get(position).getWebsite()));
                startActivity(i);
            }
        });

        editResourceFab = v.findViewById(R.id.editResource);
        editResourceFab.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.fab_add));
        editResourceFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (globals.collegeItems.size() == 0){
                    Toast.makeText(getContext(), "No college has been added.", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(getActivity(), AddPortalActivity.class);
                    startActivity(intent);
                }
            }
        });

        return v;
    }
}
