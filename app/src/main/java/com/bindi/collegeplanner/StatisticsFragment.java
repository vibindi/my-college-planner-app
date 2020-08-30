package com.bindi.collegeplanner;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bindi.collegeplanner.databaseClasses.GlobalKeys;
import com.bindi.collegeplanner.databaseClasses.Globals;
import com.bindi.collegeplanner.displayClasses.CollegeActivity;

public class StatisticsFragment extends Fragment {

    Globals globals = Globals.getInstance();

    CardView dreamSchoolCard;
    TextView dreamSchoolNameText;
    TextView dreamSchoolStatusText;

    TextView totalAppFees;
    TextView numFeeWaivers;
    TextView cheapestSchoolTitle;
    TextView cheapestSchool;

    int pos = -1;

    public StatisticsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_statistics, container, false);
        getActivity().setTitle("Statistics");

        // Dream School
        dreamSchoolCard = v.findViewById(R.id.dreamSchoolCard);
        dreamSchoolNameText = v.findViewById(R.id.dreamSchoolName);
        dreamSchoolStatusText = v.findViewById(R.id.dreamSchoolStatus);
        if (!globals.collegeItems.isEmpty()){
            for (int i = 0; i < globals.collegeItems.size(); i++){
                if (globals.collegeItems.get(i).isDreamSchool()){
                    pos = i;
                }
            }
            if (pos >= 0){
                dreamSchoolNameText.setText(globals.collegeItems.get(pos).getCollegeName());
                dreamSchoolStatusText.setText(dreamSchoolStatusText.getText() + globals.collegeItems.get(pos).getStatus() + " ");
                dreamSchoolCard.setBackgroundColor(Color.parseColor(globals.collegeItems.get(pos).getColorStr()));
            }else{
                dreamSchoolNameText.setText("Dream School Not Set");
                dreamSchoolNameText.setTextColor(getResources().getColor(android.R.color.black));
                dreamSchoolStatusText.setText("");
                dreamSchoolStatusText.setTextColor(getResources().getColor(android.R.color.black));
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) dreamSchoolNameText.getLayoutParams();
                params.addRule(RelativeLayout.CENTER_IN_PARENT);
                dreamSchoolNameText.setLayoutParams(params);
            }
        }
        else{
            dreamSchoolNameText.setText("Dream School Not Set");
            dreamSchoolNameText.setTextColor(getResources().getColor(android.R.color.black));
            dreamSchoolStatusText.setText("");
            dreamSchoolStatusText.setTextColor(getResources().getColor(android.R.color.black));
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) dreamSchoolNameText.getLayoutParams();
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            dreamSchoolNameText.setLayoutParams(params);
        }
        dreamSchoolCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pos >= 0) {
                    Intent intent = new Intent(getActivity(), CollegeActivity.class);
                    intent.putExtra(GlobalKeys.loadingDirection, pos + "");
                    startActivity(intent);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder
                            .setTitle("You Haven't Selected A Dream School!")
                            .setMessage("Try selecting your dream school in its information screen.")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    Dialog dialog = builder.create();
                    dialog.show();
                }
            }
        });

        double sumAppFees = 0.0;
        int num = 0;
        String str = "";
        double cheapestTuition = 0.0;
        if (globals.collegeItems.isEmpty() == false){
            str = globals.collegeItems.get(0).getCollegeName();
            String newStr = globals.collegeItems.get(0).getTuition().substring(1);
            String doubleStr = "";
            for (int j = 0; j < newStr.length(); j++){
                if (!newStr.substring(j,j+1).equals(",")){
                    doubleStr += newStr.substring(j, j+1);
                }
            }
            cheapestTuition = Double.parseDouble(doubleStr);
        }

        for (int i = 0; i < globals.collegeItems.size(); i++){

            // app fees
            if (globals.collegeItems.get(i).hasFeeWaiver() == false){
                sumAppFees += Double.parseDouble(globals.collegeItems.get(i).getAppFee().substring(1));
            }

            // fee waivers
            if (globals.collegeItems.get(i).hasFeeWaiver()){
                num++;
            }

            // cheapest school
            String newStr = globals.collegeItems.get(i).getTuition().substring(1);
            String doubleStr = "";
            for (int j = 0; j < newStr.length(); j++){
                if (!newStr.substring(j,j+1).equals(",")){
                    doubleStr += newStr.substring(j, j+1);
                }
            }
            double currTuition = Double.parseDouble(doubleStr);
            if (i >= 1){
                if (currTuition < cheapestTuition){
                    str = globals.collegeItems.get(i).getCollegeName();
                    cheapestTuition = currTuition;
                }else if (currTuition == cheapestTuition){
                    str += "\n" + globals.collegeItems.get(i).getCollegeName();
                }
            }
        }

        // Total Application Fees
        totalAppFees = v.findViewById(R.id.totalAppFees);

        String appFeeText = Double.toString(sumAppFees);
        int decimalPlaces = appFeeText.length() - appFeeText.indexOf(".") - 1;
        if (decimalPlaces == 1){
            totalAppFees.setText("$" + sumAppFees + "0");
        }else{
            totalAppFees.setText("$" + sumAppFees + "");
        }

        // Number of Fee Waivers
        numFeeWaivers = v.findViewById(R.id.numOfFeeWaivers);
        numFeeWaivers.setText(num + "");

        // Cheapest school
        cheapestSchoolTitle = v.findViewById(R.id.cheapestSchoolTitle);
        for (int x = 0; x < str.length(); x++){
            if (str.substring(x, x+1).equals("\n")){
                cheapestSchoolTitle.setText("Schools With Lowest Tuition");
            }
        }
        cheapestSchool = v.findViewById(R.id.cheapestSchool);
        String tuitionStr = "$" + cheapestTuition;
        tuitionStr = tuitionStr.substring(0, tuitionStr.indexOf(".")+1) + "00";
        if (!str.equals("")){
            cheapestSchool.setText(str + "\n(" + tuitionStr + ")" );
        }else{
            cheapestSchool.setText("You have not added any colleges yet.");
        }

        return v;
    }
}