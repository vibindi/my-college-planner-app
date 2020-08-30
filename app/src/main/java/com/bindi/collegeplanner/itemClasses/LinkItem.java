package com.bindi.collegeplanner.itemClasses;

import androidx.annotation.DrawableRes;

public class LinkItem {

    public String name;
    public String website;
    public String color;
    public @DrawableRes int resId;
    public String username;
    public String password;
    public boolean isPortal;

    public LinkItem(String name, String website, boolean isPortal) {
        this.name = name;
        this.website = website;
        this.isPortal = isPortal;
        this.color = "";
        this.username = "";
        this.password = "";
    }

    public LinkItem(String name, String website, String color, @DrawableRes int resId) {
        this.name = name;
        this.website = website;
        this.color = color;
        this.resId = resId;
        this.username = "";
        this.password = "";
        this.isPortal = false;
    }

    public LinkItem(String name, String website, String color, int resId, String username, String password) {
        this.name = name;
        this.website = website;
        this.color = color;
        this.resId = resId;
        this.username = username;
        this.password = password;
        this.isPortal = false;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isPortal() {
        return isPortal;
    }

    public void setPortal(boolean portal) {
        isPortal = portal;
    }
}
