package com.bindi.collegeplanner;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bindi.collegeplanner.adapterClasses.CollegesAdapter;
import com.bindi.collegeplanner.adapterClasses.OverviewCollegesAdapter;
import com.bindi.collegeplanner.databaseClasses.GlobalKeys;
import com.bindi.collegeplanner.databaseClasses.Globals;
import com.bindi.collegeplanner.displayClasses.CollegeActivity;
import com.bindi.collegeplanner.itemClasses.CollegeItem;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class OverviewFragment extends Fragment {

    Globals globals = Globals.getInstance();

    TextView enrolledSchool;
    TextView enrolledTitle;
    RelativeLayout enrolledView;

    private RecyclerView mAppliedSchoolsRecycler;
    private OverviewCollegesAdapter mAppliedAdapter;
    private RecyclerView.LayoutManager mAppliedLayoutManager;
    ArrayList<CollegeItem> appliedSchools;
    CardView appliedSchoolsCard;
    TextView noAppliedText;

    private RecyclerView mAcceptedSchoolsRecycler;
    private OverviewCollegesAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<CollegeItem> acceptedSchools;
    CardView acceptedSchoolsCard;
    TextView noAcceptedText;

    private RecyclerView mDeferredSchoolsRecycler;
    private OverviewCollegesAdapter mDeferredAdapter;
    private RecyclerView.LayoutManager mDeferredLayoutManager;
    ArrayList<CollegeItem> deferredSchools;
    CardView deferredSchoolsCard;
    TextView nodeferredText;

    private RecyclerView mWaitlistedSchoolsRecycler;
    private OverviewCollegesAdapter mWaitlistedAdapter;
    private RecyclerView.LayoutManager mWaitlistedLayoutManager;
    ArrayList<CollegeItem> waitlistedSchools;
    CardView waitlistedSchoolsCard;
    TextView noWaitlistedText;

    private RecyclerView mConsideringSchoolsRecycler;
    private OverviewCollegesAdapter mConsideringAdapter;
    private RecyclerView.LayoutManager mConsideringLayoutManager;
    ArrayList<CollegeItem> consideringSchools;
    CardView consideringSchoolsCard;
    TextView noConsideringText;



    public OverviewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_overview, container, false);
        getActivity().setTitle("Overview");

        enrolledSchool = v.findViewById(R.id.enrolledSchool);
        enrolledTitle = v.findViewById(R.id.enrolledTitle);
        enrolledView = v.findViewById(R.id.enrolledView);
        CollegeItem college  = null;
        boolean isEnrolled = false;
        int cp = -1;
        for (int x = 0; x < globals.collegeItems.size(); x++){
            if (globals.collegeItems.get(x).getStatus().equals("Enrolled")){
                isEnrolled = true;
                college = globals.collegeItems.get(x);
                cp = x;
            }
        }

        final boolean enrollCheck = isEnrolled;
        final int pc = cp;
        enrolledSchool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (enrollCheck){
                    Intent intent = new Intent(getActivity(), CollegeActivity.class);
                    intent.putExtra(GlobalKeys.loadingDirection, pc + "");
                    startActivity(intent);
                }
            }
        });

        if (isEnrolled){
            enrolledSchool.setText(college.getCollegeName());
            enrolledSchool.setTextColor(Color.parseColor(college.getColorStr()));
        }else{
            enrolledView.setVisibility(View.GONE);
        }

        appliedSchools = new ArrayList<>();
        for (int i = 0; i < globals.collegeItems.size(); i++){
            if (globals.collegeItems.get(i).getStatus().equals("Applied")){
                appliedSchools.add(globals.collegeItems.get(i));
            }
        }
        noAppliedText = v.findViewById(R.id.noAppliedText);
        if (appliedSchools.isEmpty()){
            appliedSchoolsCard = v.findViewById(R.id.appliedSchoolsCardView);
            ViewGroup.LayoutParams params = appliedSchoolsCard.getLayoutParams();
            params.height = 300;
            appliedSchoolsCard.setLayoutParams(params);
            noAppliedText.setText("You haven't applied to any colleges yet.");
        }
        else{
            mAppliedSchoolsRecycler = v.findViewById(R.id.appliedSchoolsRecycler);
            mAppliedSchoolsRecycler.setHasFixedSize(false);
            mAppliedLayoutManager = new LinearLayoutManager(getContext());
            mAppliedAdapter = new OverviewCollegesAdapter(appliedSchools, getContext());
            mAppliedSchoolsRecycler.setLayoutManager(mAppliedLayoutManager);
            mAppliedSchoolsRecycler.setAdapter(mAppliedAdapter);
            mAppliedAdapter.setOnItemClickListener(new OverviewCollegesAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    Intent intent = new Intent(getActivity(), CollegeActivity.class);
                    intent.putExtra(GlobalKeys.loadingDirection, globals.collegeItems.indexOf(appliedSchools.get(position)) + "");
                    startActivity(intent);
                }
            });
        }

        acceptedSchools = new ArrayList<>();
        for (int i = 0; i < globals.collegeItems.size(); i++){
            if (globals.collegeItems.get(i).getStatus().equals("Accepted")){
                acceptedSchools.add(globals.collegeItems.get(i));
            }
        }
        noAcceptedText = v.findViewById(R.id.noAcceptedText);
        if (acceptedSchools.isEmpty()){
            acceptedSchoolsCard = v.findViewById(R.id.acceptedSchoolsCardView);
            ViewGroup.LayoutParams params = acceptedSchoolsCard.getLayoutParams();
            params.height = 300;
            acceptedSchoolsCard.setLayoutParams(params);
            noAcceptedText.setText("You haven't been accepted to any schools yet. You will!");
        }
        else{
            mAcceptedSchoolsRecycler = v.findViewById(R.id.acceptedSchoolsRecycler);
            mAcceptedSchoolsRecycler.setHasFixedSize(false);
            mLayoutManager = new LinearLayoutManager(getContext());
            mAdapter = new OverviewCollegesAdapter(acceptedSchools, getContext());
            mAcceptedSchoolsRecycler.setLayoutManager(mLayoutManager);
            mAcceptedSchoolsRecycler.setAdapter(mAdapter);
            mAdapter.setOnItemClickListener(new OverviewCollegesAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    Intent intent = new Intent(getActivity(), CollegeActivity.class);
                    intent.putExtra(GlobalKeys.loadingDirection, globals.collegeItems.indexOf(acceptedSchools.get(position)) + "");
                    startActivity(intent);
                }
            });
        }

        deferredSchools = new ArrayList<>();
        for (int i = 0; i < globals.collegeItems.size(); i++){
            if (globals.collegeItems.get(i).getStatus().equals("Deferred")){
                deferredSchools.add(globals.collegeItems.get(i));
            }
        }
        nodeferredText = v.findViewById(R.id.noDeferredText);
        if (deferredSchools.isEmpty()){
            deferredSchoolsCard = v.findViewById(R.id.deferredSchoolsCard);
            ViewGroup.LayoutParams params = deferredSchoolsCard.getLayoutParams();
            params.height = 300;
            deferredSchoolsCard.setLayoutParams(params);
            nodeferredText.setText("You haven't been deferred from any schools yet.");
        }
        else{
            mDeferredSchoolsRecycler = v.findViewById(R.id.deferredSchoolsRecycler);
            mDeferredSchoolsRecycler.setHasFixedSize(false);
            mDeferredLayoutManager = new LinearLayoutManager(getContext());
            mDeferredAdapter = new OverviewCollegesAdapter(deferredSchools, getContext());
            mDeferredSchoolsRecycler.setLayoutManager(mDeferredLayoutManager);
            mDeferredSchoolsRecycler.setAdapter(mDeferredAdapter);
            mDeferredAdapter.setOnItemClickListener(new OverviewCollegesAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    Intent intent = new Intent(getActivity(), CollegeActivity.class);
                    intent.putExtra(GlobalKeys.loadingDirection, globals.collegeItems.indexOf(deferredSchools.get(position)) + "");
                    startActivity(intent);
                }
            });
        }

        waitlistedSchools = new ArrayList<>();
        for (int i = 0; i < globals.collegeItems.size(); i++){
            if (globals.collegeItems.get(i).getStatus().equals("Waitlisted")){
                waitlistedSchools.add(globals.collegeItems.get(i));
            }
        }
        noWaitlistedText = v.findViewById(R.id.noWaitlistedText);
        if (waitlistedSchools.isEmpty()){
            waitlistedSchoolsCard = v.findViewById(R.id.waitlistedSchoolsCardView);
            ViewGroup.LayoutParams params = waitlistedSchoolsCard.getLayoutParams();
            params.height = 300;
            waitlistedSchoolsCard.setLayoutParams(params);
            noWaitlistedText.setText("You haven't been waitlisted by any schools yet.");
        }
        else{
            mWaitlistedSchoolsRecycler = v.findViewById(R.id.waitlistedSchoolsRecycler);
            mWaitlistedSchoolsRecycler.setHasFixedSize(false);
            mWaitlistedLayoutManager = new LinearLayoutManager(getContext());
            mWaitlistedAdapter = new OverviewCollegesAdapter(waitlistedSchools, getContext());
            mWaitlistedSchoolsRecycler.setLayoutManager(mWaitlistedLayoutManager);
            mWaitlistedSchoolsRecycler.setAdapter(mWaitlistedAdapter);
            mWaitlistedAdapter.setOnItemClickListener(new OverviewCollegesAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    Intent intent = new Intent(getActivity(), CollegeActivity.class);
                    intent.putExtra(GlobalKeys.loadingDirection, globals.collegeItems.indexOf(waitlistedSchools.get(position)) + "");
                    startActivity(intent);
                }
            });
        }

        consideringSchools = new ArrayList<>();
        for (int i = 0; i < globals.collegeItems.size(); i++){
            if (globals.collegeItems.get(i).getStatus().equals("Considering") || globals.collegeItems.get(i).getStatus().equals("Applying")){
                consideringSchools.add(globals.collegeItems.get(i));
            }
        }
        noConsideringText = v.findViewById(R.id.noConsideringText);
        if (consideringSchools.isEmpty()){
            consideringSchoolsCard = v.findViewById(R.id.consideringSchoolsCardView);
            ViewGroup.LayoutParams params = consideringSchoolsCard.getLayoutParams();
            params.height = 300;
            consideringSchoolsCard.setLayoutParams(params);
            noConsideringText.setText("You haven't listed any schools you are considering or applying to yet.");
        }
        else{
            mConsideringSchoolsRecycler = v.findViewById(R.id.consideringSchoolsRecycler);
            mConsideringSchoolsRecycler.setHasFixedSize(false);
            mConsideringLayoutManager = new LinearLayoutManager(getContext());
            mConsideringAdapter = new OverviewCollegesAdapter(consideringSchools, getContext());
            mConsideringSchoolsRecycler.setLayoutManager(mConsideringLayoutManager);
            mConsideringSchoolsRecycler.setAdapter(mConsideringAdapter);
            mConsideringAdapter.setOnItemClickListener(new OverviewCollegesAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    Intent intent = new Intent(getActivity(), CollegeActivity.class);
                    intent.putExtra(GlobalKeys.loadingDirection, globals.collegeItems.indexOf(consideringSchools.get(position)) + "");
                    startActivity(intent);
                }
            });
        }

        return v;
    }
}
