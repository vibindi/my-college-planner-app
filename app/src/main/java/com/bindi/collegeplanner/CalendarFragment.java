package com.bindi.collegeplanner;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.exceptions.OutOfDateRangeException;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.bindi.collegeplanner.adapterClasses.CollegesAdapter;
import com.bindi.collegeplanner.adapterClasses.EventAdapter;
import com.bindi.collegeplanner.addContentClasses.AddCounselorActivity;
import com.bindi.collegeplanner.addContentClasses.AddEventActivity;
import com.bindi.collegeplanner.databaseClasses.GlobalKeys;
import com.bindi.collegeplanner.databaseClasses.Globals;
import com.bindi.collegeplanner.displayClasses.CollegeActivity;
import com.bindi.collegeplanner.itemClasses.CollegeItem;
import com.bindi.collegeplanner.itemClasses.EventItem;
import com.github.clans.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 */
public class CalendarFragment extends Fragment {

    Globals globals = Globals.getInstance();

    //CustomCalendar calendarView;

    private RecyclerView mRecyclerView;
    private EventAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    ArrayList<EventItem> showedEvents; // current showed events
    String selectedCollege = "All Events";
    TextView dateText;

    CalendarView calendarView;

    Context c;

    FloatingActionButton addEvent;

    public CalendarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_calendar, container, false);
        getActivity().setTitle("");
        setHasOptionsMenu(true);

        dateText = v.findViewById(R.id.dateText);
        addEvent = v.findViewById(R.id.addEvent);

        c = getContext();

        // START --> ALL EVENTS
        // when doing month do 1 less than wanted

        calendarView = v.findViewById(R.id.calendarView);

        mRecyclerView = v.findViewById(R.id.eventsView);
        mRecyclerView.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(getContext());

        ArrayList<EventItem> dayEvents = new ArrayList<>();
        for (int i = 0; i < globals.generalEvents.size(); i++){
            EventItem e = globals.generalEvents.get(i);
            Calendar c = Calendar.getInstance();
            if (((c.getTime().getYear() + 1900) == e.getYear()) && (c.getTime().getMonth() == (e.getMonth() - 1)) && (c.getTime().getDate() == e.getDay())){
                dayEvents.add(e);
            }
        }
        for (int i = 0; i < globals.collegeItems.size(); i++){
            for (int x = 0; x < globals.collegeItems.get(i).getEventItems().size(); x++){
                EventItem e = globals.collegeItems.get(i).getEventItems().get(x);
                Calendar c = Calendar.getInstance();
                if (((c.getTime().getYear() + 1900) == e.getYear()) && (c.getTime().getMonth() == (e.getMonth() - 1)) && (c.getTime().getDate() == e.getDay())){
                    dayEvents.add(e);
                }
            }
        }

        adapterStuff(dayEvents);

        allEvents();

        addEvent = v.findViewById(R.id.addEvent);
        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(c, AddEventActivity.class);
                startActivity(intent);
            }
        });

        return v;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_calendar, menu);
        MenuItem item = menu.findItem(R.id.collegeChoose);
        Spinner spinner = (Spinner) MenuItemCompat.getActionView(item);

        final ArrayList<String> chooseList = new ArrayList<>();
        chooseList.add("All Events");
        chooseList.add("General Events");

        for (CollegeItem ci : globals.collegeItems){
            chooseList.add(ci.getCollegeName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>
                (getContext(), R.layout.simple_spinner_item, chooseList);
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCollege = chooseList.get(position);
                if (selectedCollege.equals("All Events")){

                    allEvents();
                }else if (selectedCollege.equals("General Events")){
                    generalEvents();
                }else{
                    CollegeItem selectedItem = null;
                    for (int i = 0; i < globals.collegeItems.size(); i++){
                        if (selectedCollege.equals(globals.collegeItems.get(i).getCollegeName())){
                            selectedItem = globals.collegeItems.get(i);
                        }
                    }
                    showCollege(selectedItem);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void allEvents(){
        Calendar calendar = Calendar.getInstance();

        List<EventDay> events = new ArrayList<>();

        for (int i = 0; i < globals.generalEvents.size(); i++){
            EventItem e = globals.generalEvents.get(i);
            Calendar c = Calendar.getInstance();
            c.set(e.getYear() + 0, e.getMonth() - 1, e.getDay() + 0);
            events.add(new EventDay(c, R.drawable.ic_event));
        }
        for (int i = 0; i < globals.collegeItems.size(); i++){
            for (int j = 0; j < globals.collegeItems.get(i).getEventItems().size(); j++){
                EventItem e = globals.collegeItems.get(i).getEventItems().get(j);
                Calendar c = Calendar.getInstance();
                c.set(e.getYear() + 0, e.getMonth() - 1, e.getDay() + 0);
                events.add(new EventDay(c, R.drawable.ic_event));
            }
        }

        try {
            calendarView.setDate(new Date((calendar.getTime().getYear()), (calendar.getTime().getMonth()), calendar.getTime().getDate()));
        } catch (OutOfDateRangeException e) {
            e.printStackTrace();
        }

        ArrayList<EventItem> dayEvents = new ArrayList<>();
        for (int i = 0; i < globals.generalEvents.size(); i++){
            EventItem e = globals.generalEvents.get(i);
            Calendar c = Calendar.getInstance();
            if (((c.getTime().getYear() + 1900) == e.getYear()) && (c.getTime().getMonth() == (e.getMonth() - 1)) && (c.getTime().getDate() == e.getDay())){
                dayEvents.add(e);
            }
        }
        for (int i = 0; i < globals.collegeItems.size(); i++){
            for (int x = 0; x < globals.collegeItems.get(i).getEventItems().size(); x++){
                EventItem e = globals.collegeItems.get(i).getEventItems().get(x);
                Calendar c = Calendar.getInstance();
                if (((c.getTime().getYear() + 1900) == e.getYear()) && (c.getTime().getMonth() == (e.getMonth() - 1)) && (c.getTime().getDate() == e.getDay())){
                    dayEvents.add(e);
                }
            }
        }

        adapterStuff(dayEvents);

        //dateText.setText((calendar.getTime().getYear() + 1900) + " " + (calendar.getTime().getMonth() + 1) + " " + calendar.getTime().getDate());
        dateText.setText(showMonth(calendar) + " " + calendar.getTime().getDate() + ", " + (calendar.getTime().getYear() + 1900));

        calendarView.setEvents(events);
        calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                Calendar clickedDayCalendar = eventDay.getCalendar();
                dateText.setText(showMonth(clickedDayCalendar) + " " + clickedDayCalendar.getTime().getDate() + ", " + (clickedDayCalendar.getTime().getYear() + 1900));

                ArrayList<EventItem> dayEvents = new ArrayList<>();
                for (int i = 0; i < globals.generalEvents.size(); i++){
                    EventItem e = globals.generalEvents.get(i);
                    if (((clickedDayCalendar.getTime().getYear() + 1900) == e.getYear()) && (clickedDayCalendar.getTime().getMonth() == (e.getMonth() - 1)) && (clickedDayCalendar.getTime().getDate() == e.getDay())){
                        dayEvents.add(e);
                    }
                }
                for (int i = 0; i < globals.collegeItems.size(); i++){
                    for (int x = 0; x < globals.collegeItems.get(i).getEventItems().size(); x++){
                        EventItem e = globals.collegeItems.get(i).getEventItems().get(x);
                        if (((clickedDayCalendar.getTime().getYear() + 1900) == e.getYear()) && (clickedDayCalendar.getTime().getMonth() == (e.getMonth() - 1)) && (clickedDayCalendar.getTime().getDate() == e.getDay())){
                            dayEvents.add(e);
                        }
                    }
                }
                adapterStuff(dayEvents);
            }
        });
    }

    public void generalEvents(){
        Calendar calendar = Calendar.getInstance();

        List<EventDay> events = new ArrayList<>();

        for (int i = 0; i < globals.generalEvents.size(); i++){
            EventItem e = globals.generalEvents.get(i);
            Calendar c = Calendar.getInstance();
            c.set(e.getYear() + 0, e.getMonth() - 1, e.getDay() + 0);
            events.add(new EventDay(c, R.drawable.ic_event));
        }

        try {
            calendarView.setDate(new Date((calendar.getTime().getYear()), (calendar.getTime().getMonth()), calendar.getTime().getDate()));
        } catch (OutOfDateRangeException e) {
            e.printStackTrace();
        }

        ArrayList<EventItem> dayEvents = new ArrayList<>();
        for (int i = 0; i < globals.generalEvents.size(); i++){
            EventItem e = globals.generalEvents.get(i);
            Calendar c = Calendar.getInstance();
            if (((c.getTime().getYear() + 1900) == e.getYear()) && (c.getTime().getMonth() == (e.getMonth() - 1)) && (c.getTime().getDate() == e.getDay())){
                dayEvents.add(e);
            }
        }

        adapterStuff(dayEvents);

        //dateText.setText((calendar.getTime().getYear() + 1900) + " " + (calendar.getTime().getMonth() + 1) + " " + calendar.getTime().getDate());
        dateText.setText(showMonth(calendar) + " " + calendar.getTime().getDate() + ", " + (calendar.getTime().getYear() + 1900));

        calendarView.setEvents(events);
        calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                Calendar clickedDayCalendar = eventDay.getCalendar();
                dateText.setText(showMonth(clickedDayCalendar) + " " + clickedDayCalendar.getTime().getDate() + ", " + (clickedDayCalendar.getTime().getYear() + 1900));

                ArrayList<EventItem> dayEvents = new ArrayList<>();
                for (int i = 0; i < globals.generalEvents.size(); i++){
                    EventItem e = globals.generalEvents.get(i);
                    if (((clickedDayCalendar.getTime().getYear() + 1900) == e.getYear()) && (clickedDayCalendar.getTime().getMonth() == (e.getMonth() - 1)) && (clickedDayCalendar.getTime().getDate() == e.getDay())){
                        dayEvents.add(e);
                    }
                }
                adapterStuff(dayEvents);
            }
        });
    }

    public void showCollege(final CollegeItem collegeItem){
        Calendar calendar = Calendar.getInstance();

        List<EventDay> events = new ArrayList<>();

        for (int j = 0; j < collegeItem.getEventItems().size(); j++){
            EventItem e = collegeItem.getEventItems().get(j);
            Calendar c = Calendar.getInstance();
            c.set(e.getYear() + 0, e.getMonth() - 1, e.getDay() + 0);
            events.add(new EventDay(c, R.drawable.ic_event));
        }

        try {
            calendarView.setDate(new Date((calendar.getTime().getYear()), (calendar.getTime().getMonth()), calendar.getTime().getDate()));
        } catch (OutOfDateRangeException e) {
            e.printStackTrace();
        }

        ArrayList<EventItem> dayEvents = new ArrayList<>();
        for (int x = 0; x < collegeItem.getEventItems().size(); x++){
            EventItem e = collegeItem.getEventItems().get(x);
            Calendar c = Calendar.getInstance();
            if (((c.getTime().getYear() + 1900) == e.getYear()) && (c.getTime().getMonth() == (e.getMonth() - 1)) && (c.getTime().getDate() == e.getDay())){
                dayEvents.add(e);
            }
        }

        adapterStuff(dayEvents);

        //dateText.setText((calendar.getTime().getYear() + 1900) + " " + (calendar.getTime().getMonth() + 1) + " " + calendar.getTime().getDate());
        dateText.setText(showMonth(calendar) + " " + calendar.getTime().getDate() + ", " + (calendar.getTime().getYear() + 1900));

        calendarView.setEvents(events);
        calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                Calendar clickedDayCalendar = eventDay.getCalendar();
                dateText.setText(showMonth(clickedDayCalendar) + " " + clickedDayCalendar.getTime().getDate() + ", " + (clickedDayCalendar.getTime().getYear() + 1900));

                ArrayList<EventItem> dayEvents = new ArrayList<>();
                for (int x = 0; x < collegeItem.getEventItems().size(); x++){
                    EventItem e = collegeItem.getEventItems().get(x);
                    if (((clickedDayCalendar.getTime().getYear() + 1900) == e.getYear()) && (clickedDayCalendar.getTime().getMonth() == (e.getMonth() - 1)) && (clickedDayCalendar.getTime().getDate() == e.getDay())){
                        dayEvents.add(e);
                    }
                }
                adapterStuff(dayEvents);
            }
        });
    }


    public String showMonth(Calendar calendar){
        int month = (calendar.getTime().getMonth() + 1);
        String str = "";
        switch(month){
            case 1:
                str = "January"; break;
            case 2:
                str = "February";break;
            case 3:
                str = "March";break;
            case 4:
                str = "April";break;
            case 5:
                str = "May";break;
            case 6:
                str = "June";break;
            case 7:
                str = "July";break;
            case 8:
                str = "August";break;
            case 9:
                str = "September";break;
            case 10:
                str = "October";break;
            case 11:
                str = "November";break;
            case 12:
                str = "December";break;
            default:
                str = "";break;
        }
        return str;
    }

    public void adapterStuff(final ArrayList<EventItem> dayEvents){
        mAdapter = new EventAdapter(dayEvents, getContext());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new EventAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final int position) {

                if (dayEvents.get(position).getCollegeName() == null){
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(c);
                    alertDialogBuilder
                            .setTitle(dayEvents.get(position).getTitle())
                            .setMessage(dateText.getText().toString())
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                    Dialog dialog = alertDialogBuilder.create();
                    dialog.show();
                }else{
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(c);
                    alertDialogBuilder
                            .setTitle(dayEvents.get(position).getTitle())
                            .setMessage(dayEvents.get(position).getCollegeName() + "\n" + dateText.getText().toString())
                            .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    for (int x = 0; x < globals.collegeItems.size(); x++){
                                        for (int y = 0; y < globals.collegeItems.get(x).getEventItems().size(); y++){
                                            if (globals.collegeItems.get(x).getEventItems().get(y).getCollegeName().equals(dayEvents.get(position).getCollegeName())
                                            && globals.collegeItems.get(x).getEventItems().get(y).getTitle().equals(dayEvents.get(position).getTitle())
                                            && globals.collegeItems.get(x).getEventItems().get(y).getDay() == (dayEvents.get(position).getDay())
                                            && globals.collegeItems.get(x).getEventItems().get(y).getMonth() ==(dayEvents.get(position).getMonth())
                                            && globals.collegeItems.get(x).getEventItems().get(y).getYear() == (dayEvents.get(position).getYear())
                                            && globals.collegeItems.get(x).getEventItems().get(y).getColorStr().equals(dayEvents.get(position).getColorStr())){
                                                globals.collegeItems.get(x).getEventItems().remove(y);
                                                globals.saveColleges(globals.collegeItems, GlobalKeys.collegeKey, c);
                                                globals.loadColleges(GlobalKeys.collegeKey, c);
                                            }
                                        }
                                    }
                                    Intent intent = new Intent(c, MainActivity.class);
                                    intent.putExtra(GlobalKeys.loadingDirection, GlobalKeys.calendarDirection);
                                    startActivity(intent);
                                }
                            });
                    Dialog dialog = alertDialogBuilder.create();
                    dialog.show();
                }
            }
        });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                if (dy<0 && !addEvent.isShown())
                    addEvent.show(true);
                else if(dy>0 && addEvent.isShown())
                    addEvent.hide(true);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
