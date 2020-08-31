package com.bindi.collegeplanner.databaseClasses;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.View;
import android.widget.Toast;

import com.bindi.collegeplanner.R;
import com.bindi.collegeplanner.itemClasses.CollegeItem;
import com.bindi.collegeplanner.itemClasses.EventItem;
import com.bindi.collegeplanner.itemClasses.FinancialAidItem;
import com.bindi.collegeplanner.itemClasses.LinkItem;
import com.bindi.collegeplanner.itemClasses.NoteItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class Globals {

    private static Globals instance = new Globals();
    // Getter-Setters
    public static Globals getInstance() {
        return instance;
    }
    public static void setInstance(Globals instance) {
        Globals.instance = instance;
    }

    public String fullName;
    public String firstKey; // first or notfirst

    public ArrayList<EventItem> generalEvents;
    public ArrayList<CollegeItem> collegeItems;
    public ArrayList<NoteItem> noteItems;
    public ArrayList<FinancialAidItem> scholarshipItems;
    public ArrayList<LinkItem> resourceItems;

    private Globals(){
        fullName = "Your Name";
        firstKey = "first";
        collegeItems = new ArrayList<>();
        generalEvents = new ArrayList<>();
        noteItems = new ArrayList<>();
        scholarshipItems = new ArrayList<>();
        resourceItems = new ArrayList<>();
    }

    public void deleteAllData(){
        generalEvents = new ArrayList<>();
        collegeItems = new ArrayList<>();
        noteItems = new ArrayList<>();
        resourceItems = new ArrayList<>();
        scholarshipItems = new ArrayList<>();
        fullName = "Your Name";
        firstKey = "first";
    }

    public void addAllResources(Context c){
        resourceItems.add(new LinkItem("College Board", "https://www.collegeboard.org", "#0077c7", R.drawable.college_board));
        resourceItems.add(new LinkItem("ACT", "https://www.act.org", "#FF073365", R.drawable.act_logo)); //"#002e61"
        resourceItems.add(new LinkItem("Khan Academy", "https://www.khanacademy.org", "#14bf95", R.drawable.khan_logo));
        resourceItems.add(new LinkItem("Common App", "https://apply.commonapp.org/login", "#1769b2", R.drawable.common_app));
        resourceItems.add(new LinkItem("Coalition", "https://www.mycoalition.org", "#2b7fbb", R.drawable.coalition_logo));
        resourceItems.add(new LinkItem("CollegeVine", "https://www.collegevine.com/dashboard", "#00ba69", R.drawable.collegevine));
        resourceItems.add(new LinkItem("Naviance", "https://www.naviance.com/", "#00737a", R.drawable.naviance_logo));
        saveResources(resourceItems, GlobalKeys.resourcesKey, c);
    }

    public void loadAll(Context c){
        fullName = loadString(GlobalKeys.nameKey, c);
        if (fullName.equals("")){
            fullName = "Your Name";
        }
        firstKey = loadString(GlobalKeys.firstKey, c);
        if (firstKey.equals("")){
            firstKey = "first";
        }
        collegeItems = loadColleges(GlobalKeys.collegeKey, c);
        generalEvents = loadGeneralEvents(GlobalKeys.generalEventsKey, c);
        generalEvents.clear();
        generalEvents.add(new EventItem("Common Application opens for fall 2021 cycle", 2020, 8, 1));
        generalEvents.add(new EventItem("FAFSA Application opens for fall 2021 cycle", 2020, 10, 1));
        generalEvents.add(new EventItem("CSS Application opens for fall 2021 cycle", 2020, 10, 1));
        generalEvents.add(new EventItem("National Decision Day", 2021, 5, 1));
        generalEvents.add(new EventItem("Temporary", 2020, 8, 31));
        saveGeneralEvents(generalEvents, GlobalKeys.generalEventsKey, c);
        scholarshipItems = loadScholarships(GlobalKeys.scholarshipsKey, c);
        noteItems = loadNotes(GlobalKeys.notesKey, c);
        ArrayList<LinkItem> tempResourceItems = loadResources(GlobalKeys.resourcesKey, c);
        if (resourceItems.isEmpty()){
            addAllResources(c);
            for (int i = 0; i < tempResourceItems.size(); i++){
                resourceItems.get(i).setUsername(tempResourceItems.get(i).getUsername());
                resourceItems.get(i).setPassword(tempResourceItems.get(i).getPassword());
            }
        }
    }

    public void saveAll(Context c){
        saveString(fullName, GlobalKeys.nameKey, c);
        saveString(firstKey, GlobalKeys.firstKey, c);
        saveColleges(collegeItems, GlobalKeys.collegeKey, c);
        saveGeneralEvents(generalEvents, GlobalKeys.generalEventsKey, c);
        saveScholarships(scholarshipItems, GlobalKeys.scholarshipsKey, c);
        saveNotes(noteItems, GlobalKeys.notesKey, c);
        saveResources(resourceItems, GlobalKeys.resourcesKey, c);
    }

    // ----------------- SAVING METHODS -------------------- \\

    public void saveString(String s, String fileName, Context c){
        FileOutputStream fos = null;
        try {
            fos = c.openFileOutput(fileName, Context.MODE_PRIVATE);
            fos.write(s.getBytes());
            //Toast.makeText(c, "File has been saved", Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (fos != null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public String loadString(String fileName, Context c){
        String returnStr = "";
        FileInputStream fis = null;
        try {
            fis = c.openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;

            while ((text = br.readLine()) != null){
                //sb.append(text).append("\n");
                sb.append(text).append("");
            }
            returnStr = sb.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (returnStr != null){
            return returnStr;
        }
        return "";
    }

    public void saveColleges(ArrayList<CollegeItem> list, String fileName, Context c){
        SharedPreferences sharedPreferences = c.getSharedPreferences("SharedPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(fileName, json);
        editor.apply();
    }
    public ArrayList<CollegeItem> loadColleges(String fileName, Context c){
        SharedPreferences sharedPreferences = c.getSharedPreferences("SharedPrefs", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(fileName, null);
        Type type = new TypeToken<ArrayList<CollegeItem>>() {}.getType();
        ArrayList<CollegeItem> strList = gson.fromJson(json, type);

        if (strList == null){
            return new ArrayList<CollegeItem>();
        }
        return strList;
    }

    public void saveGeneralEvents(ArrayList<EventItem> list, String fileName, Context c){
        SharedPreferences sharedPreferences = c.getSharedPreferences("SharedPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(fileName, json);
        editor.apply();
    }
    public ArrayList<EventItem> loadGeneralEvents(String fileName, Context c){
        SharedPreferences sharedPreferences = c.getSharedPreferences("SharedPrefs", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(fileName, null);
        Type type = new TypeToken<ArrayList<EventItem>>() {}.getType();
        ArrayList<EventItem> strList = gson.fromJson(json, type);

        if (strList == null){
            return new ArrayList<EventItem>();
        }
        return strList;
    }

    public void saveNotes(ArrayList<NoteItem> list, String fileName, Context c){
        SharedPreferences sharedPreferences = c.getSharedPreferences("SharedPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(fileName, json);
        editor.apply();
    }
    public ArrayList<NoteItem> loadNotes(String fileName, Context c){
        SharedPreferences sharedPreferences = c.getSharedPreferences("SharedPrefs", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(fileName, null);
        Type type = new TypeToken<ArrayList<NoteItem>>() {}.getType();
        ArrayList<NoteItem> strList = gson.fromJson(json, type);

        if (strList == null){
            return new ArrayList<NoteItem>();
        }
        return strList;
    }

    public void saveScholarships(ArrayList<FinancialAidItem> list, String fileName, Context c){
        SharedPreferences sharedPreferences = c.getSharedPreferences("SharedPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(fileName, json);
        editor.apply();
    }
    public ArrayList<FinancialAidItem> loadScholarships(String fileName, Context c){
        SharedPreferences sharedPreferences = c.getSharedPreferences("SharedPrefs", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(fileName, null);
        Type type = new TypeToken<ArrayList<FinancialAidItem>>() {}.getType();
        ArrayList<FinancialAidItem> strList = gson.fromJson(json, type);

        if (strList == null){
            return new ArrayList<FinancialAidItem>();
        }
        return strList;
    }

    public void saveResources(ArrayList<LinkItem> list, String fileName, Context c){
        SharedPreferences sharedPreferences = c.getSharedPreferences("SharedPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(fileName, json);
        editor.apply();
    }
    public ArrayList<LinkItem> loadResources(String fileName, Context c){
        SharedPreferences sharedPreferences = c.getSharedPreferences("SharedPrefs", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(fileName, null);
        Type type = new TypeToken<ArrayList<LinkItem>>() {}.getType();
        ArrayList<LinkItem> strList = gson.fromJson(json, type);

        if (strList == null){
            return new ArrayList<LinkItem>();
        }
        return strList;
    }

    public void sortCollegeList(){
        Collections.sort(collegeItems, new Comparator<CollegeItem>() {
            @Override
            public int compare(CollegeItem o1, CollegeItem o2) {
                return o1.getCollegeName().compareTo(o2.getCollegeName());
            }
        });
    }

    public String getEmojiByUnicode(int unicode){
        return new String(Character.toChars(unicode));
    }

    public boolean isDarkEnough(String fontColor){
        return true;
    }
}