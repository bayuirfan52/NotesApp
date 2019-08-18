package com.bayuirfan.notesapp.utils;

import com.bayuirfan.notesapp.model.Note;

import java.util.ArrayList;

public interface LoadNotesCallback {
    void preExecute();
    void postExecute(ArrayList<Note> noteArrayList);
}
