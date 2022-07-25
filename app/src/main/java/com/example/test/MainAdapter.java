package com.example.test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.test.model.note;

import java.util.ArrayList;
import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.NotesViewHolder> {

    @NonNull
    private final Context context;
    private List<note> notes = new ArrayList<>();
    private final OnItemClickListener<note> onClickListener;

    public MainAdapter(@NonNull Context context, OnItemClickListener<note> onCategoryClickListener) {
        this.context = context;
        this.onClickListener = onCategoryClickListener;
    }

    @Override
    public MainAdapter.NotesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NotesViewHolder(LayoutInflater.from(context).inflate(R.layout.notes_list_item, parent, false));
    }


    @Override
    public void onBindViewHolder(NotesViewHolder holder, int position) {
        holder.setNoteItem(notes.get(position));
    }

    @Override
    public int getItemCount() {
        return notes == null ? 0 : notes.size();
    }

    public void setNotes(List<note> Notes) {
        this.notes = Notes;
    }

    class NotesViewHolder extends RecyclerView.ViewHolder {

        private final TextView title;
        private final TextView time;
        private final TextView content;
        private final View NoteItem;

        NotesViewHolder(View Item) {
            super(Item);
            title = Item.findViewById(R.id.TextView_title);
            time = Item.findViewById(R.id.TextView_timeline);
            content = Item.findViewById(R.id.TextView_content);
            this.NoteItem = Item;
        }

        private void setNoteItem(note item){
            title.setText(item.getTitle());
            time.setText(item.getTimeline());
            content.setText(item.getContent());
            NoteItem.setOnClickListener(view -> onClickListener.onItemClicked(view, item));
        }

    }
}
