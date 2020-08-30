package com.bindi.collegeplanner;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bindi.collegeplanner.databaseClasses.GlobalKeys;
import com.bindi.collegeplanner.databaseClasses.Globals;
import com.bindi.collegeplanner.itemClasses.CollegeItem;
import com.google.android.material.navigation.NavigationView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    Globals globals = Globals.getInstance();
    Context c;
    Activity a;

    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    // Make sure to be using androidx.appcompat.app.ActionBarDrawerToggle version.
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        c = this;
        a = this;

        // Set a Toolbar to replace the ActionBar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // This will display an Up icon (<-), we will replace it with hamburger later
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);
        // Find our drawer view
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle();
        // Setup toggle to display hamburger icon with nice animation
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerToggle.syncState();
        // Tie DrawerLayout events to the ActionBarToggle
        // Find our drawer view
        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        // Setup drawer view
        setupDrawerContent(nvDrawer);
        mDrawer.addDrawerListener(drawerToggle);

        String data=null;
        boolean other = true;
        if(getIntent()!=null && getIntent().getExtras()!=null){
            Bundle bundle = getIntent().getExtras();
            if(!bundle.getString(GlobalKeys.loadingDirection).equals(null)){
                data= bundle.getString(GlobalKeys.loadingDirection);
                if (data.equals(GlobalKeys.notesDirection)){
                    other = false;
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.flContent, new NotesFragment());
                    ft.addToBackStack(null);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft.commit();
                    setTitle("Notes");
                    nvDrawer.getMenu().getItem(4).setChecked(true);
                }
                else if (data.equals(GlobalKeys.scholarshipsDirection)){
                    other = false;
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.flContent, new FinancialAidFragment());
                    ft.addToBackStack(null);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft.commit();
                    setTitle("Scholarships");
                    nvDrawer.getMenu().getItem(3).setChecked(true);
                }
                else if (data.equals(GlobalKeys.collegesDirection)){
                    other = false;
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.flContent, new CollegesFragment());
                    ft.addToBackStack(null);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft.commit();
                    setTitle("Colleges");
                    nvDrawer.getMenu().getItem(2).setChecked(true);
                } else if (data.equals(GlobalKeys.resourcesDirection)){
                    other = false;
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.flContent, new ResourcesFragment());
                    ft.addToBackStack(null);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft.commit();
                    setTitle("Common Resources");
                    nvDrawer.getMenu().getItem(6).setChecked(true);
                } else if (data.equals(GlobalKeys.portalsDirection)){
                    other = false;
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.flContent, new PortalsFragment());
                    ft.addToBackStack(null);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft.commit();
                    setTitle("Portals");
                    nvDrawer.getMenu().getItem(5).setChecked(true);
                }
            }
        }

        if (other){
            Fragment fragment = new OverviewFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.flContent, fragment);
            fragmentTransaction.addToBackStack(fragment.toString());
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragmentTransaction.commit();
            nvDrawer.getMenu().getItem(0).setChecked(true);
        }

        View headerLayout = nvDrawer.inflateHeaderView(R.layout.nav_header);
        TextView nameText = headerLayout.findViewById(R.id.studentName);
        nameText.setText(globals.fullName);
        TextView statusText = headerLayout.findViewById(R.id.studentStatus);
        for (int i = 0; i < globals.collegeItems.size(); i++){
            if (globals.collegeItems.get(i).getStatus().equals("Enrolled")){
                statusText.setText("Enrolled");
            }
        }
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass = OverviewFragment.class;
        boolean help = false;
        boolean settings = false;
        switch(menuItem.getItemId()) {
            case R.id.overview:
                fragmentClass = OverviewFragment.class;
                break;
            case R.id.calendar:
                fragmentClass = CalendarFragment.class;
                break;
            case R.id.colleges:
                fragmentClass = CollegesFragment.class;
                break;
            case R.id.financialAid:
                fragmentClass = FinancialAidFragment.class;
                break;
            case R.id.notes:
                fragmentClass = NotesFragment.class;
                break;
            case R.id.portals:
                fragmentClass = PortalsFragment.class;
                break;
            case R.id.resources:
                fragmentClass = ResourcesFragment.class;
                break;
            case R.id.statistics:
                fragmentClass = StatisticsFragment.class;
                break;
            case R.id.help:
                help = true;
                sendFeedback(c);
                break;
            case R.id.settings:
                settings = true;
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            default:
                fragmentClass = OverviewFragment.class;
        }

        // Close the navigation drawer
        if (!help && !settings) {
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
            ft.replace(R.id.flContent, fragment).commit();


            // Highlight the selected item has been done by NavigationView
            menuItem.setChecked(true);
            // Set action bar title
            setTitle(menuItem.getTitle());
        }
        if (!help){
            mDrawer.closeDrawers();
        }
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        // NOTE: Make sure you pass in a valid toolbar reference.  ActionBarDrawToggle() does not require it
        // and will not render the hamburger icon without it.
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open,  R.string.drawer_close);
    }

    // `onPostCreate` called when activity start-up is complete after `onStart()`
    // NOTE 1: Make sure to override the method with only a single `Bundle` argument
    // Note 2: Make sure you implement the correct `onPostCreate(Bundle savedInstanceState)` method.
    // There are 2 signatures and only `onPostCreate(Bundle state)` shows the hamburger icon.
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        /*switch (item.getItemId()) {
            case R.id.settings:
                startActivity(new Intent(this, SettingsActivity.class));
        }*/
        return super.onOptionsItemSelected(item);
    }

    public static void sendFeedback(Context context) {
        String body = null;
        try {
            body = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
            body = "\n\n-----------------------------\nPlease don't remove this information\n Device OS: Android \n Device OS version: " +
                    Build.VERSION.RELEASE + "\n App Version: " + body + "\n Device Brand: " + Build.BRAND +
                    "\n Device Model: " + Build.MODEL + "\n Device Manufacturer: " + Build.MANUFACTURER;
        } catch (PackageManager.NameNotFoundException e) {

        }

        String email = "help.mycollegeplanner@gmail.com";
        String subject = "Feedback for College Planner";
        String chooserTitle = "Send Feedback Email";

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, body);
        //emailIntent.setType("message/rfc822");
        context.startActivity(Intent.createChooser(emailIntent, chooserTitle));
    }

    @Override
    public void onBackPressed(){
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

}
