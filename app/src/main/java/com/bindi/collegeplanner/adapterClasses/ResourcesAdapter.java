package com.bindi.collegeplanner.adapterClasses;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bindi.collegeplanner.R;
import com.bindi.collegeplanner.databaseClasses.Globals;
import com.bindi.collegeplanner.itemClasses.LinkItem;
import com.bindi.collegeplanner.itemClasses.NoteItem;

import java.util.ArrayList;

public class ResourcesAdapter extends RecyclerView.Adapter<ResourcesAdapter.ResourcesViewHolder> {

    Globals globals = Globals.getInstance();

    private ArrayList<LinkItem> mLinkItems;
    private ResourcesAdapter.OnItemClickListener mListener;

    private Context c;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(ResourcesAdapter.OnItemClickListener listener){
        mListener = listener;
    }

    public static class ResourcesViewHolder extends RecyclerView.ViewHolder{

        public CardView mResourceCard;
        public TextView mResourceName;
        public TextView mResourceInfo;

        public ResourcesViewHolder(@NonNull View itemView, final ResourcesAdapter.OnItemClickListener listener) {
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

    public ResourcesAdapter(ArrayList<LinkItem> classItems, Context context){
        mLinkItems = classItems;
        c = context;
    }

    @NonNull
    @Override
    public ResourcesAdapter.ResourcesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.resource_item, parent, false);
        ResourcesAdapter.ResourcesViewHolder cvh = new ResourcesAdapter.ResourcesViewHolder(v, mListener);
        return cvh;
    }

    @Override
    public void onBindViewHolder(@NonNull ResourcesAdapter.ResourcesViewHolder holder, int position) {
        LinkItem currentItem = mLinkItems.get(position);
        holder.mResourceCard.setBackgroundColor(Color.parseColor(currentItem.getColor()));
        holder.mResourceName.setText(currentItem.getName());
        String infoStr = "";
        boolean user = false;
        if (!currentItem.getUsername().equals("")){
            user = true;
            infoStr += "User: " + currentItem.getUsername();
        }
        if (!currentItem.getPassword().equals("")){
            if (user){
                infoStr += " ";
            }
            infoStr += "Pass: " + currentItem.getPassword();
        }
        if (infoStr.equals("")){
            infoStr = "No Credentials Given";
        }
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
