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
import com.bindi.collegeplanner.itemClasses.NoteItem;

import java.util.ArrayList;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder>{

    Globals globals = Globals.getInstance();

    private ArrayList<NoteItem> mNoteItems;
    private OnItemClickListener mListener;

    private Context c;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public static class NotesViewHolder extends RecyclerView.ViewHolder{

        public TextView mNoteTitle;
        public TextView mNoteText;

        public NotesViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            mNoteTitle = itemView.findViewById(R.id.mNoteTitle);
            mNoteText = itemView.findViewById(R.id.mNoteText);
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

    public NotesAdapter(ArrayList<NoteItem> classItems, Context context){
        mNoteItems = classItems;
        c = context;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent, false);
        NotesViewHolder cvh = new NotesViewHolder(v, mListener);
        return cvh;
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        NoteItem currentItem = mNoteItems.get(position);
        holder.mNoteTitle.setText(currentItem.getTitle());
        holder.mNoteText.setText(currentItem.getText());
        //holder.mTaskTime.setText(currentItem.getTimeItem().getTimeString());
        //holder.mClassColor.setBackgroundColor(Color.parseColor(currentItem.getColor()));
    }

    @Override
    public int getItemCount() {
        return mNoteItems.size();
    }

    public ArrayList<NoteItem> getData() {
        return mNoteItems;
    }

}
