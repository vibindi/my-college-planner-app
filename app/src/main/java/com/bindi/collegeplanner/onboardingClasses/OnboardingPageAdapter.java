package com.bindi.collegeplanner.onboardingClasses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bindi.collegeplanner.R;

import java.util.List;

public class OnboardingPageAdapter extends PagerAdapter {

    Context c;
    List<ScreenItem> screenItems;

    public OnboardingPageAdapter(Context c, List<ScreenItem> screenItems) {
        this.c = c;
        this.screenItems = screenItems;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layoutScreen = inflater.inflate(R.layout.intro_layout_screen, null);
        ImageView imgSlide = layoutScreen.findViewById(R.id.introImage);
        TextView title = layoutScreen.findViewById(R.id.titleText);
        TextView description = layoutScreen.findViewById(R.id.descriptionText);

        title.setText(screenItems.get(position).getTitle());
        description.setText(screenItems.get(position).getDescription());
        imgSlide.setImageResource(screenItems.get(position).getScreenImg());

        container.addView(layoutScreen);

        return layoutScreen;

    }

    @Override
    public int getCount() {
        return screenItems.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
