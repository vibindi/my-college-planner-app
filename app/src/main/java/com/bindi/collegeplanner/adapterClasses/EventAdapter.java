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
import com.bindi.collegeplanner.itemClasses.EventItem;
import com.bindi.collegeplanner.itemClasses.LinkItem;
import com.bindi.collegeplanner.itemClasses.NoteItem;

import java.util.ArrayList;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder>{
    Globals globals = Globals.getInstance();

    private ArrayList<EventItem> mEventitems;
    private EventAdapter.OnItemClickListener mListener;

    private Context c;


    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(EventAdapter.OnItemClickListener listener){
        mListener = listener;
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder{

        public CardView mResourceCard;
        public TextView mResourceName;
        public TextView mResourceInfo;

        public EventViewHolder(@NonNull View itemView, final EventAdapter.OnItemClickListener listener) {
            super(itemView);
            mResourceCard = itemView.findViewById(R.id.backgroundCard);
            mResourceName = itemView.findViewById(R.id.mResourceName);
            mResourceInfo = itemView.findViewById(R.id.mResourceInfo);
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

    public EventAdapter(ArrayList<EventItem> classItems, Context context){
        mEventitems = classItems;
        c = context;
    }

    @NonNull
    @Override
    public EventAdapter.EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.resource_item, parent, false);
        EventAdapter.EventViewHolder cvh = new EventAdapter.EventViewHolder(v, mListener);
        return cvh;
    }

    @Override
    public void onBindViewHolder(@NonNull EventAdapter.EventViewHolder holder, int position) {
        EventItem currentItem = mEventitems.get(position);

        String colorStr = "#";
        String infoStr = "";

        if (currentItem.getCollegeName() == null){
            holder.mResourceInfo.setText("General Item");
        }else{
            holder.mResourceInfo.setText(currentItem.getCollegeName());
        }
        holder.mResourceName.setText(currentItem.getTitle());
        if (currentItem.getCollegeName() == null){
            holder.mResourceCard.setBackgroundColor(Color.parseColor("#3F51B5"));
        }else{
            holder.mResourceCard.setBackgroundColor(Color.parseColor(currentItem.getColorStr()));
        }
        //holder.mTaskTime.setText(currentItem.getTimeItem().getTimeString());
        //holder.mClassColor.setBackgroundColor(Color.parseColor(currentItem.getColor()));
    }

    @Override
    public int getItemCount() {
        return mEventitems.size();
    }

    public ArrayList<EventItem> getData() {
        return mEventitems;
    }
}
