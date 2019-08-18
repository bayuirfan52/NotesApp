package com.bayuirfan.notesapp.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bayuirfan.notesapp.R;
import com.bayuirfan.notesapp.features.NoteAddUpdateActivity;
import com.bayuirfan.notesapp.model.Note;
import com.bayuirfan.notesapp.utils.CustomOnClickListener;

import java.util.ArrayList;

import static com.bayuirfan.notesapp.features.NoteAddUpdateActivity.EXTRA_NOTE;
import static com.bayuirfan.notesapp.features.NoteAddUpdateActivity.EXTRA_POSITION;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {
    private ArrayList<Note> noteArrayList = new ArrayList<>();
    private Activity activity;

    public NoteAdapter(Activity activity){
        this.activity = activity;
    }

    public ArrayList<Note> getListNotes(){
        return noteArrayList;
    }

    public void setListNotes(ArrayList<Note> noteArrayList){
        if (noteArrayList.size() > 0) this.noteArrayList.clear();

        this.noteArrayList.addAll(noteArrayList);
        notifyDataSetChanged();
    }

    public void addItem(Note note){
        this.noteArrayList.add(note);
        notifyItemInserted(noteArrayList.size() - 1);
    }

    public void updateItem(int position, Note note){
        this.noteArrayList.set(position, note);
        notifyItemChanged(position);
    }

    public void removeItem(int position){
        this.noteArrayList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, noteArrayList.size());
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new NoteViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int i) {
        holder.tvTitle.setText(noteArrayList.get(i).getTitle());
        holder.tvDesc.setText(noteArrayList.get(i).getDescription());
        holder.tvDate.setText(noteArrayList.get(i).getDate());
        holder.itemCardView.setOnClickListener(new CustomOnClickListener(i, (view, position) -> {
            Intent intent = new Intent(activity, NoteAddUpdateActivity.class);
            intent.putExtra(EXTRA_NOTE, noteArrayList.get(position));
            intent.putExtra(EXTRA_POSITION, position);
            activity.startActivityForResult(intent, NoteAddUpdateActivity.REQUEST_UPDATE);
        }));
    }

    @Override
    public int getItemCount() {
        return noteArrayList.size();
    }

    class NoteViewHolder extends RecyclerView.ViewHolder{
        final TextView tvTitle, tvDate, tvDesc;
        final CardView itemCardView;

        NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_item_title);
            tvDate = itemView.findViewById(R.id.tv_item_date);
            tvDesc = itemView.findViewById(R.id.tv_item_desc);
            itemCardView = itemView.findViewById(R.id.cv_item_note);
        }
    }
}
