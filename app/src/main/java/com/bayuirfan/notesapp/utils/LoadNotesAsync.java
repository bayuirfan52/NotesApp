package com.bayuirfan.notesapp.utils;

import android.os.AsyncTask;

import com.bayuirfan.notesapp.database.NoteHelper;
import com.bayuirfan.notesapp.model.Note;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class LoadNotesAsync extends AsyncTask<Void, Void, ArrayList<Note>> {
    private final WeakReference<NoteHelper> weakNoteHelper;
    private final WeakReference<LoadNotesCallback> weakNotesCallback;

    public LoadNotesAsync(NoteHelper noteHelper, LoadNotesCallback loadNotesCallback){
        weakNoteHelper = new WeakReference<>(noteHelper);
        weakNotesCallback = new WeakReference<>(loadNotesCallback);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        weakNotesCallback.get().preExecute();
    }

    @Override
    protected ArrayList<Note> doInBackground(Void... voids) {
        return weakNoteHelper.get().getAllNotes();
    }

    @Override
    protected void onPostExecute(ArrayList<Note> noteArrayList) {
        super.onPostExecute(noteArrayList);
        weakNotesCallback.get().postExecute(noteArrayList);
    }
}
