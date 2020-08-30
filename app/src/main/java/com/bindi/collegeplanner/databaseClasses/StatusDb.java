package com.bindi.collegeplanner.databaseClasses;

import java.util.ArrayList;

public class StatusDb {

    public ArrayList<String> collegesList;

    public StatusDb(){
        collegesList = new ArrayList<String>();
        collegesList.add("Applying");
        collegesList.add("Considering");
        collegesList.add("Applied");
        collegesList.add("Waitlisted");
        collegesList.add("Deferred");
        collegesList.add("Accepted");
        collegesList.add("Rejected");
        collegesList.add("Enrolled");
    }

    public ArrayList<String> getTypesList(){
        return collegesList;
    }

}
