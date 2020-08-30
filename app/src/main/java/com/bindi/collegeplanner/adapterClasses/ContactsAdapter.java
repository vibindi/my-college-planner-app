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
import com.bindi.collegeplanner.itemClasses.ContactItem;
import com.bindi.collegeplanner.itemClasses.LinkItem;

import java.util.ArrayList;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder>{

    Globals globals = Globals.getInstance();

    private ArrayList<ContactItem> mContactItems;
    private ContactsAdapter.OnItemClickListener mListener;

    private Context c;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(ContactsAdapter.OnItemClickListener listener){
        mListener = listener;
    }

    public static class ContactsViewHolder extends RecyclerView.ViewHolder{

        public CardView mResourceCard;
        public TextView mResourceName;
        public TextView mResourceInfo;

        public ContactsViewHolder(@NonNull View itemView, final ContactsAdapter.OnItemClickListener listener) {
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

    public ContactsAdapter(ArrayList<ContactItem> classItems, Context context){
        mContactItems = classItems;
        c = context;
    }

    @NonNull
    @Override
    public ContactsAdapter.ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.resource_item, parent, false);
        ContactsAdapter.ContactsViewHolder cvh = new ContactsAdapter.ContactsViewHolder(v, mListener);
        return cvh;
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsAdapter.ContactsViewHolder holder, int position) {
        ContactItem currentItem = mContactItems.get(position);

        String colorStr = "#";
        String infoStr = "";

        ArrayList<String> colleges = new ArrayList<>();

        for (int i = 0; i < globals.collegeItems.size(); i++){
            for (int j = 0; j < globals.collegeItems.get(i).getContactItems().size(); j++){
                if (globals.collegeItems.get(i).getContactItems().get(j).equals(currentItem)){
                    //colorStr = globals.collegeItems.get(i).getColorStr();
                    //infoStr = globals.collegeItems.get(i).getCollegeName();
                    colleges.add(globals.collegeItems.get(i).getCollegeName());
                    if (colorStr.equals("#")){
                        colorStr = globals.collegeItems.get(i).getColorStr();
                    }
                }
            }
        }

        for (int x = 0; x < colleges.size(); x++){
            if (infoStr.equals("")){
                infoStr += colleges.get(x);
            }else{
                infoStr += ", " + colleges.get(x);
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
        return mContactItems.size();
    }

    public ArrayList<ContactItem> getData() {
        return mContactItems;
    }

}
