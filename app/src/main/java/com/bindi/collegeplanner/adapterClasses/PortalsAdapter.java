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
import com.bindi.collegeplanner.itemClasses.LinkItem;

import java.util.ArrayList;

public class PortalsAdapter extends RecyclerView.Adapter<PortalsAdapter.PortalsViewHolder> {

    Globals globals = Globals.getInstance();

    private ArrayList<LinkItem> mLinkItems;
    private PortalsAdapter.OnItemClickListener mListener;

    private Context c;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(PortalsAdapter.OnItemClickListener listener){
        mListener = listener;
    }

    public static class PortalsViewHolder extends RecyclerView.ViewHolder{

        public CardView mResourceCard;
        public TextView mResourceName;
        public TextView mResourceInfo;

        public PortalsViewHolder(@NonNull View itemView, final PortalsAdapter.OnItemClickListener listener) {
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

    public PortalsAdapter(ArrayList<LinkItem> classItems, Context context){
        mLinkItems = classItems;
        c = context;
    }

    @NonNull
    @Override
    public PortalsAdapter.PortalsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.resource_item, parent, false);
        PortalsAdapter.PortalsViewHolder cvh = new PortalsAdapter.PortalsViewHolder(v, mListener);
        return cvh;
    }

    @Override
    public void onBindViewHolder(@NonNull PortalsAdapter.PortalsViewHolder holder, int position) {
        LinkItem currentItem = mLinkItems.get(position);

        String colorStr = "#";
        String infoStr = "";

        for (int i = 0; i < globals.collegeItems.size(); i++){
            for (int j = 0; j < globals.collegeItems.get(i).getResourceItems().size(); j++){
                if (globals.collegeItems.get(i).getResourceItems().get(j).equals(currentItem)){
                    colorStr = globals.collegeItems.get(i).getColorStr();
                    infoStr = globals.collegeItems.get(i).getCollegeName();
                }
            }
        }

        holder.mResourceCard.setBackgroundColor(Color.parseColor(colorStr));
        holder.mResourceName.setText(currentItem.getName());
        boolean user = false;
        holder.mResourceInfo.setText(infoStr);
        //holder.mTaskTime.setText(currentItem.getTimeItem().getTimeString());
        //holder.mClassColor.setBackgroundColor(Color.parseColor(currentItem.getColor()));
    }

    @Override
    public int getItemCount() {
        return mLinkItems.size();
    }

    public ArrayList<LinkItem> getData() {
        return mLinkItems;
    }

}
