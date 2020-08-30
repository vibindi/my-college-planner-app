package com.bindi.collegeplanner;

import android.content.Intent;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bindi.collegeplanner.adapterClasses.ContactsAdapter;
import com.bindi.collegeplanner.adapterClasses.PortalsAdapter;
import com.bindi.collegeplanner.addContentClasses.AddPortalActivity;
import com.bindi.collegeplanner.databaseClasses.Globals;
import com.bindi.collegeplanner.helperClasses.WebviewActivity;
import com.bindi.collegeplanner.itemClasses.ContactItem;
import com.bindi.collegeplanner.itemClasses.LinkItem;
import com.github.clans.fab.FloatingActionButton;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ContactsFragment extends Fragment {

    Globals globals = Globals.getInstance();

    private RecyclerView mRecyclerView;
    private ContactsAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    FloatingActionButton editResourceFab;

    TextView statusDisplayText;

    public ContactsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_resources, container, false);
        getActivity().setTitle("Contacts");

        mRecyclerView = v.findViewById(R.id.resourcesLayout);
        mRecyclerView.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(getContext());

        final ArrayList<ContactItem> contactItems = new ArrayList<>();
        for (int i = 0; i < globals.collegeItems.size(); i++){
            for (int j = 0; j < globals.collegeItems.get(i).getContactItems().size(); j++){
                contactItems.add(globals.collegeItems.get(i).getContactItems().get(j));
            }
        }

        statusDisplayText = v.findViewById(R.id.statusDisplayText);

        if (contactItems.isEmpty()){
            statusDisplayText.setText("Hit the + button to add a new contact.");
        }

        mAdapter = new ContactsAdapter(contactItems, getContext());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new ContactsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(getContext(), contactItems.get(position).getName(), Toast.LENGTH_SHORT).show();
            }
        });

        editResourceFab = v.findViewById(R.id.editResource);
        editResourceFab.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.fab_add));
        editResourceFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (globals.collegeItems.size() == 0){
                    Toast.makeText(getContext(), "No contact has been added.", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(getActivity(), AddPortalActivity.class);
                    startActivity(intent);
                }
            }
        });

        return v;
    }
}
