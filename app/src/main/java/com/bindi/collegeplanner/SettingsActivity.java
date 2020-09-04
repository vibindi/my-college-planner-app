package com.bindi.collegeplanner;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.LightingColorFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.bindi.collegeplanner.databaseClasses.GlobalKeys;
import com.bindi.collegeplanner.databaseClasses.Globals;
import com.bindi.collegeplanner.displayClasses.CollegeActivity;
import com.google.android.material.textfield.TextInputEditText;

public class SettingsActivity extends AppCompatActivity {

    Context c;
    Activity a;
    public static LayoutInflater inflater;
    public static FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.DarkAppTheme);
        setContentView(R.layout.settings_activity);
        c = this;
        a = this;
        fm = getSupportFragmentManager();
        inflater = getLayoutInflater();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment(c, a))
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {

        Globals globals;

        Context c;
        Activity a;

        private SettingsFragment(Context c, Activity a){
            globals = Globals.getInstance();
            this.c = c;
            this.a = a;
        }

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            final Preference changeProfile = findPreference("key_profile");
            changeProfile.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    changeProfileDialog(c, a);
                    return true;
                }
            });

            Preference aboutPreference = findPreference("key_about");
            aboutPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    showInfoDialog(c);
                    return true;
                }
            });

            Preference privacyPolicyPreference = findPreference("key_privacy_policy");
            privacyPolicyPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse("https://github.com/vb270/my-college-planner"));
                    startActivity(i);
                    return false;
                }
            });

            Preference feedbackPreference = findPreference("key_send_feedback");
            feedbackPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    sendFeedback(c);
                    return true;
                }
            });

            Preference deleteDataPreference = findPreference("key_delete_data");
            deleteDataPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    globals.deleteAllData();
                    globals.saveAll(c);
                    Intent intent = new Intent(getActivity(), IntroActivity.class);
                    startActivity(intent);
                    return true;
                }
            });

            Preference openSourcePreference = findPreference("key_os_libraries");
            openSourcePreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent intent = new Intent(getActivity(), LibraryActivity.class);
                    startActivity(intent);
                    return true;
                }
            });

        }
    }

    public static void showInfoDialog(Context context){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        View view = inflater.inflate(R.layout.about_dialog,null);

        alertDialogBuilder
                .setTitle("About")
                .setView(view)
                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton("CLOSE", null);
        Dialog dialog = alertDialogBuilder.create();
        dialog.show();
        //dialog.getWindow().getDecorView().getBackground().setColorFilter(new LightingColorFilter(0xFF000000, ContextCompat.getColor(context, R.color.colorPrimaryDark)));

    }

    public static void changeProfileDialog(final Context context, final Activity activity){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        final View view = inflater.inflate(R.layout.change_profile_dialog, null);
        final TextInputEditText firstName = view.findViewById(R.id.firstNameText);
        final TextInputEditText lastName = view.findViewById(R.id.lastNameText);
        alertDialogBuilder
                .setTitle("Change Name")
                .setView(view)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!firstName.getText().toString().equals("") && !lastName.getText().toString().equals("")){
                            String fullName = firstName.getText().toString() + " " + lastName.getText().toString();
                            Globals.getInstance().fullName = fullName;
                            Globals.getInstance().saveString(fullName, GlobalKeys.nameKey, context);
                            //Toast.makeText(context, fullName, Toast.LENGTH_LONG).show();
                            activity.startActivity(new Intent(activity, MainActivity.class));
                        }else{
                            Toast.makeText(context, "Must Enter First and Last Name", Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", null);
        Dialog dialog = alertDialogBuilder.create();
        dialog.show();
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
        String subject = "Feedback for MyCollegePlanner";
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}