package com.bindi.collegeplanner.adapterClasses;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bindi.collegeplanner.R;
import com.bindi.collegeplanner.databaseClasses.Globals;
import com.bindi.collegeplanner.itemClasses.CollegeItem;

import java.util.ArrayList;

public class OverviewCollegesAdapter extends RecyclerView.Adapter<OverviewCollegesAdapter.CollegesViewHolder> {
    Globals globals = Globals.getInstance();

    private ArrayList<CollegeItem> mCollegeItems;
    private OverviewCollegesAdapter.OnItemClickListener mListener;

    private Context c;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OverviewCollegesAdapter.OnItemClickListener listener){
        mListener = listener;
    }

    public static class CollegesViewHolder extends RecyclerView.ViewHolder{

        public CardView mResourceCard;
        public TextView mCollegeName;
        public TextView mCollegeInfo;

        public CollegesViewHolder(@NonNull View itemView, final OverviewCollegesAdapter.OnItemClickListener listener) {
            super(itemView);
            mResourceCard = itemView.findViewById(R.id.bgCard);
            mCollegeName = itemView.findViewById(R.id.mCollegeName);
            mCollegeInfo = itemView.findViewById(R.id.mCollegeInfo);
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

    public OverviewCollegesAdapter(ArrayList<CollegeItem> classItems, Context context){
        mCollegeItems = classItems;
        c = context;
    }

    @NonNull
    @Override
    public OverviewCollegesAdapter.CollegesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.overview_college_item, parent, false);
        OverviewCollegesAdapter.CollegesViewHolder cvh = new OverviewCollegesAdapter.CollegesViewHolder(v, mListener);
        return cvh;
    }

    @Override
    public void onBindViewHolder(@NonNull OverviewCollegesAdapter.CollegesViewHolder holder, int position) {
        CollegeItem currentItem = mCollegeItems.get(position);
        holder.mResourceCard.setBackgroundColor(Color.parseColor(currentItem.getColorStr()));
        holder.mCollegeName.setText(currentItem.getCollegeName());
        holder.mCollegeInfo.setText(currentItem.getCollegeMajor() + " | "  + currentItem.getCollegeType() + " | " + currentItem.getAppType() + " ");
    }

    @Override
    public int getItemCount() {
        return mCollegeItems.size();
    }

    public ArrayList<CollegeItem> getData() {
        return mCollegeItems;
    }
}
