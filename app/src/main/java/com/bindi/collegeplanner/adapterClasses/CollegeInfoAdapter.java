package com.bindi.collegeplanner.adapterClasses;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bindi.collegeplanner.R;
import com.bindi.collegeplanner.databaseClasses.Globals;
import com.bindi.collegeplanner.itemClasses.CollegeInfoItem;
import com.github.clans.fab.FloatingActionButton;

import java.util.ArrayList;

public class CollegeInfoAdapter extends RecyclerView.Adapter<CollegeInfoAdapter.CollegeInfoViewHolder> {

    Globals globals = Globals.getInstance();

    private ArrayList<CollegeInfoItem> mCollegeInfoItems;
    private CollegeInfoAdapter.OnItemClickListener mListener;
    private CollegeInfoAdapter.OnLongClickListener mLongListener;

    private Context c;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(CollegeInfoAdapter.OnItemClickListener listener){
        mListener = listener;
    }

    public interface OnLongClickListener{
        void onLongItemClick(int position);
    }

    public void setOnLongClickListener(CollegeInfoAdapter.OnLongClickListener listener){
        mLongListener = listener;
    }

    public static class CollegeInfoViewHolder extends RecyclerView.ViewHolder{

        public ImageView imageView;
        public TextView mTeacherInfo;
        public TextView mTeacherType;

        public CollegeInfoViewHolder(@NonNull View itemView, final CollegeInfoAdapter.OnItemClickListener listener) {
            super(itemView);
            imageView = itemView.findViewById(R.id.teacherIcon);
            mTeacherInfo = itemView.findViewById(R.id.teacherInfo);
            mTeacherType = itemView.findViewById(R.id.teacherTitle);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public CollegeInfoAdapter(ArrayList<CollegeInfoItem> classInfoItems, Context context){
        mCollegeInfoItems = classInfoItems;
        c = context;
    }

    @NonNull
    @Override
    public CollegeInfoAdapter.CollegeInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.college_info_item, parent, false);
        CollegeInfoAdapter.CollegeInfoViewHolder cvh = new CollegeInfoAdapter.CollegeInfoViewHolder(v, mListener);
        return cvh;
    }

    @Override
    public void onBindViewHolder(@NonNull CollegeInfoAdapter.CollegeInfoViewHolder holder, int position) {
        CollegeInfoItem currentItem = mCollegeInfoItems.get(position);
        holder.imageView.setImageResource(currentItem.getIcon());
        //String hexColor = String.format("#%06X", (0xFFFFFF & android.R.color.darker_gray));
        holder.imageView.setImageTintList(setIconColor("#808080"));
        holder.mTeacherInfo.setText(currentItem.getInfo());
        holder.mTeacherType.setText(currentItem.getTitle());
    }

    @Override
    public int getItemCount() {
        return mCollegeInfoItems.size();
    }

    public ArrayList<CollegeInfoItem> getData() {
        return mCollegeInfoItems;
    }

    public ColorStateList setIconColor(String colorStr){
        ColorStateList myList;

        int[][] states = new int[][] {
                new int[] { android.R.attr.state_enabled}, // enabled
                new int[] {-android.R.attr.state_enabled}, // disabled
                new int[] {-android.R.attr.state_checked}, // unchecked
                new int[] { android.R.attr.state_pressed}  // pressed
        };

        int[] colors = new int[] {
                Color.parseColor(colorStr),
                Color.parseColor(colorStr),
                Color.parseColor(colorStr),
                Color.parseColor(colorStr)
        };

        myList = new ColorStateList(states, colors);
        return myList;
    }

}
