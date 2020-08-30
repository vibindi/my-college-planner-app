package com.bindi.collegeplanner.databaseClasses;

import java.util.ArrayList;

public class CollegeTypeDb {

    public ArrayList<String> collegesList;

    public CollegeTypeDb(){
        collegesList = new ArrayList<String>();
        collegesList.add("Safety");
        collegesList.add("Target");
        collegesList.add("Hard Target");
        collegesList.add("Reach");
        collegesList.add("Long Reach");
    }

    public ArrayList<String> getTypesList(){
        return collegesList;
    }
}
