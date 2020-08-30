package com.bindi.collegeplanner.itemClasses;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;

import java.util.ArrayList;
import java.util.List;

public class CollegeItem {

    private String collegeName;
    private List<EventItem> eventItems;
    private boolean isDreamSchool;
    private String collegeMajor;
    private String colorStr;
    private String status;
    private String appFee;
    private String tuition;
    private String appType;
    private String collegeType;
    private boolean hasFeeWaiver;
    private ArrayList<LinkItem> resourceItems;
    private ArrayList<EssayItem> essayItems;
    private ArrayList<ContactItem> contactItems;
    private String notes;

    public CollegeItem(String collegeName, String collegeMajors, String colorStr, String status) {
        this.collegeName = collegeName;
        this.collegeMajor = collegeMajors;
        this.colorStr = colorStr;
        this.status = status;
        this.isDreamSchool = false;
        this.hasFeeWaiver = false;
        this.appFee = "$0.00";
        this.tuition = "$0.00";
        this.appType = "";
        resourceItems = new ArrayList<LinkItem>();
        essayItems = new ArrayList<EssayItem>();
        contactItems = new ArrayList<ContactItem>();
        eventItems = new ArrayList<EventItem>();
        this.notes = "";
    }

    public String getCollegeName() {
        return collegeName;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }

    public List<EventItem> getEventItems() {
        return eventItems;
    }

    public void setEventItems(List<EventItem> eventItems) {
        this.eventItems = eventItems;
    }

    public void addEventItem(EventItem eventItem){
        this.eventItems.add(eventItem);
    }

    public EventItem getEventItem(int position){
        return this.eventItems.get(position);
    }

    public boolean isDreamSchool() {
        return isDreamSchool;
    }

    public void setDreamSchool(boolean dreamSchool) {
        isDreamSchool = dreamSchool;
    }

    public String getCollegeMajor() {
        return collegeMajor;
    }

    public void setCollegeMajor(String collegeMajor) {
        this.collegeMajor = collegeMajor;
    }

    public String getColorStr() {
        return colorStr;
    }

    public void setColorStr(String colorStr) {
        this.colorStr = colorStr;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAppFee() {
        return appFee;
    }

    public void setAppFee(String appFee) {
        this.appFee = appFee;
    }

    public String getTuition() {
        return tuition;
    }

    public void setTuition(String tuition) {
        this.tuition = tuition;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType (String appType) {
        this.appType = appType;
    }

    public String getCollegeType() {
        return collegeType;
    }

    public void setCollegeType(String collegeType) {
        this.collegeType = collegeType;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public ArrayList<LinkItem> getResourceItems() {
        return resourceItems;
    }

    public void setResourceItems(ArrayList<LinkItem> resourceItems) {
        this.resourceItems = resourceItems;
    }

    public void addResourceItem(LinkItem linkItem){
        this.resourceItems.add(linkItem);
    }

    public ArrayList<EssayItem> getEssayItems(){return essayItems;}
    public void setEssayItems(ArrayList<EssayItem> essayItems){
        this.essayItems = essayItems;
    }
    public void addEssayItem(EssayItem essayItem){this.essayItems.add(essayItem);}

    public ArrayList<ContactItem> getContactItems() {
        return contactItems;
    }

    public void setContactItems(ArrayList<ContactItem> contactItems) {
        this.contactItems = contactItems;
    }
    public void addContactItem(ContactItem contactItem){this.contactItems.add(contactItem);}

    public boolean hasFeeWaiver() {
        return hasFeeWaiver;
    }

    public void setHasFeeWaiver(boolean hasFeeWaiver) {
        this.hasFeeWaiver = hasFeeWaiver;
    }
}
