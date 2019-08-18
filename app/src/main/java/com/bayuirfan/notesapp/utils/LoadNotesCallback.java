package com.bayuirfan.notesapp.utils;

import android.database.Cursor;

public interface LoadNotesCallback {
    void preExecute();
    void postExecute(Cursor cursor);
}
