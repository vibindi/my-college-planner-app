package com.bindi.collegeplanner.itemClasses;

public class FinancialAidItem {

    private String name;
    private String amount;
    //EventItem deadline;
    private String notes;

    public FinancialAidItem(String name) {
        this.name = name;
        this.amount = "";
        this.notes = "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}