package com.bindi.collegeplanner;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bindi.collegeplanner.adapterClasses.NotesAdapter;
import com.bindi.collegeplanner.adapterClasses.ResourcesAdapter;
import com.bindi.collegeplanner.databaseClasses.GlobalKeys;
import com.bindi.collegeplanner.databaseClasses.Globals;
import com.bindi.collegeplanner.displayClasses.NoteActivity;
import com.bindi.collegeplanner.helperClasses.WebviewActivity;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;


/**
 * A simple {@link Fragment} subclass.
 */
public class ResourcesFragment extends Fragment {

    Globals globals = Globals.getInstance();

    private RecyclerView mRecyclerView;
    private ResourcesAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    FloatingActionButton editResourceFab;

    boolean editMode;

    public ResourcesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_resources, container, false);
        getActivity().setTitle("Common Resources");

        editMode = false;

        mRecyclerView = v.findViewById(R.id.resourcesLayout);
        mRecyclerView.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new ResourcesAdapter(globals.resourceItems, getContext());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new ResourcesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (!editMode){
                    Intent i = new Intent(getActivity(), WebviewActivity.class);
                    i.putExtra("resourceWebsite", globals.resourceItems.get(position).getWebsite() + "");
                    //Intent i = new Intent(Intent.ACTION_VIEW);
                    //i.setData(Uri.parse(globals.resourceItems.get(position).getWebsite()));
                    startActivity(i);
                }else{
                    changeProfileDialog(getContext(), getActivity(), position, globals);
                }
            }
        });

        editResourceFab = v.findViewById(R.id.editResource);
        editResourceFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editMode){
                    editMode = false;
                    getActivity().setTitle("Common Resources");
                    editResourceFab.setImageResource(R.drawable.ic_edit_black_24dp);
                }else{
                    editMode = true;
                    getActivity().setTitle("Edit Resources");
                    editResourceFab.setImageResource(R.drawable.ic_check_black_24dp);
                }
            }
        });

        return v;
    }

    public static void changeProfileDialog(final Context context, final Activity activity, final int position, final Globals globals){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        final View view = activity.getLayoutInflater().inflate(R.layout.change_credentials_dialog, null);
        final TextInputEditText username = view.findViewById(R.id.usernameText);
        username.setText(globals.resourceItems.get(position).getUsername());
        final TextInputEditText password = view.findViewById(R.id.passwordText);
        password.setText(globals.resourceItems.get(position).getPassword());
        alertDialogBuilder
                .setTitle("Edit Credentials")
                .setView(view)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        globals.resourceItems.get(position).setUsername(username.getText().toString());
                        globals.resourceItems.get(position).setPassword(password.getText().toString());
                        globals.saveResources(globals.resourceItems, GlobalKeys.resourcesKey, context);
                        globals.loadResources(GlobalKeys.resourcesKey, context);
                        Intent intent = new Intent(activity, MainActivity.class);
                        intent.putExtra(GlobalKeys.loadingDirection, GlobalKeys.resourcesDirection);
                        activity.startActivity(intent);
                    }
                })
                .setNegativeButton("Cancel", null);
        Dialog dialog = alertDialogBuilder.create();
        dialog.show();
    }
}
