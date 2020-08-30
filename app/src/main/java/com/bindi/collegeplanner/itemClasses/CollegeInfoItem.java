package com.bindi.collegeplanner.itemClasses;

import androidx.annotation.DrawableRes;

public class CollegeInfoItem {

    @DrawableRes int icon;
    String info;
    String title;

    public CollegeInfoItem(@DrawableRes int icon, String info, String title) {
        this.icon = icon;
        this.info = info;
        this.title = title;
    }

    public @DrawableRes int getIcon() {
        return icon;
    }

    public void setIcon(@DrawableRes int icon) {
        this.icon = icon;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
