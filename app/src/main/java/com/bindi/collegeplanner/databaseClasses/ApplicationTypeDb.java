package com.bindi.collegeplanner.databaseClasses;

import java.util.ArrayList;

public class ApplicationTypeDb {

    public ArrayList<String> collegesList;

    public ApplicationTypeDb(){
        collegesList = new ArrayList<String>();
        collegesList.add("Early Decision");
        collegesList.add("Early Action");
        collegesList.add("Restricted Early Action");
        collegesList.add("Rolling Admission");
        collegesList.add("Regular Admission");
    }

    public ArrayList<String> getTypesList(){
        return collegesList;
    }
}
