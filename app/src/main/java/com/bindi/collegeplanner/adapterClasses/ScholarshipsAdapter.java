package com.bindi.collegeplanner.adapterClasses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bindi.collegeplanner.R;
import com.bindi.collegeplanner.databaseClasses.Globals;
import com.bindi.collegeplanner.itemClasses.FinancialAidItem;
import com.bindi.collegeplanner.itemClasses.NoteItem;

import java.util.ArrayList;

public class ScholarshipsAdapter extends RecyclerView.Adapter<ScholarshipsAdapter.ScholarshipsViewHolder>{

    Globals globals = Globals.getInstance();

    private ArrayList<FinancialAidItem> mScholarshipItems;
    private ScholarshipsAdapter.OnItemClickListener mListener;

    private Context c;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(ScholarshipsAdapter.OnItemClickListener listener){
        mListener = listener;
    }

    public static class ScholarshipsViewHolder extends RecyclerView.ViewHolder{

        public TextView mScholarshipName;
        public TextView mScholarshipAmount;

        public ScholarshipsViewHolder(@NonNull View itemView, final ScholarshipsAdapter.OnItemClickListener listener) {
            super(itemView);
            mScholarshipName = itemView.findViewById(R.id.mNoteTitle);
            mScholarshipAmount = itemView.findViewById(R.id.mNoteText);
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

    public ScholarshipsAdapter(ArrayList<FinancialAidItem> classItems, Context context){
        mScholarshipItems = classItems;
        c = context;
    }

    @NonNull
    @Override
    public ScholarshipsAdapter.ScholarshipsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent, false);
        ScholarshipsAdapter.ScholarshipsViewHolder cvh = new ScholarshipsAdapter.ScholarshipsViewHolder(v, mListener);
        return cvh;
    }

    @Override
    public void onBindViewHolder(@NonNull ScholarshipsAdapter.ScholarshipsViewHolder holder, int position) {
        FinancialAidItem currentItem = mScholarshipItems.get(position);
        holder.mScholarshipName.setText(currentItem.getName());
        if (currentItem.getAmount().equals("")){
            holder.mScholarshipAmount.setText("$0.00");
        }else{
            holder.mScholarshipAmount.setText(currentItem.getAmount());
        }
    }

    @Override
    public int getItemCount() {
        return mScholarshipItems.size();
    }

    public ArrayList<FinancialAidItem> getData() {
        return mScholarshipItems;
    }

}
