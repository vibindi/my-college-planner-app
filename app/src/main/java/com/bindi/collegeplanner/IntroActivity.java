package com.bindi.collegeplanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bindi.collegeplanner.databaseClasses.Globals;
import com.bindi.collegeplanner.onboardingClasses.OnboardingPageAdapter;
import com.bindi.collegeplanner.onboardingClasses.ScreenItem;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class IntroActivity extends AppCompatActivity {

    Globals globals = Globals.getInstance();
    Context c;


    private ViewPager screenPager;
    OnboardingPageAdapter onboardingPageAdapter;

    Button nextButton;
    Button skipButton;

    TabLayout tabLayout;

    int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.IntroAppTheme);
        super.onCreate(savedInstanceState);

        c = getApplicationContext();

        globals.loadAll(getApplicationContext());

        //Toast.makeText(getApplicationContext(), globals.firstKey, Toast.LENGTH_LONG).show();

        if (!globals.firstKey.equals("first")){
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }
        setContentView(R.layout.activity_intro);

        final List<ScreenItem> mList = new ArrayList<>();
        mList.add(new ScreenItem("Information At A Glance", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book", R.drawable.check_list));
        mList.add(new ScreenItem("Detailed Calendar", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book", R.drawable.calendar));
        mList.add(new ScreenItem("Relevant Statistics", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book", R.drawable.graphics));
        mList.add(new ScreenItem("Good Luck!", "Are you ready to enter the college application process? Don't stress. You got this!", R.drawable.high_five));

        nextButton = findViewById(R.id.next_button);
        nextButton.setElevation(0f);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                position = screenPager.getCurrentItem();
                if (position < mList.size()){
                    position++;
                    screenPager.setCurrentItem(position);
                }
                if (position == mList.size()){
                    globals.firstKey = "notfirst";
                    globals.saveAll(getApplicationContext());
                    globals.loadAll(getApplicationContext());
                    Intent i = new Intent(c, MainActivity.class);
                    startActivity(i);
                }
            }
        });
        skipButton = findViewById(R.id.skip_button);
        skipButton.setElevation(0f);
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                globals.firstKey = "notfirst";
                globals.saveAll(getApplicationContext());
                globals.loadAll(getApplicationContext());
                Intent i = new Intent(c, MainActivity.class);
                startActivity(i);
            }
        });

        tabLayout = findViewById(R.id.tabLayout);

        screenPager = findViewById(R.id.view_pager_intro);
        onboardingPageAdapter = new OnboardingPageAdapter(this, mList);
        screenPager.setAdapter(onboardingPageAdapter);
        screenPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == (mList.size() - 1)){
                    nextButton.setText("Get Started");
                }else{
                    nextButton.setText("Next");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        tabLayout.setupWithViewPager(screenPager);
    }
}