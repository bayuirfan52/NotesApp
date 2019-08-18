package com.bayuirfan.notesapp.utils.helper;

import android.database.Cursor;

import com.bayuirfan.notesapp.model.Note;

import java.util.ArrayList;

import static com.bayuirfan.notesapp.database.DatabaseContract.NoteColumns.DATE;
import static com.bayuirfan.notesapp.database.DatabaseContract.NoteColumns.DESC;
import static com.bayuirfan.notesapp.database.DatabaseContract.NoteColumns.TITLE;
import static com.bayuirfan.notesapp.database.DatabaseContract.NoteColumns._ID;

public class MappingHelper {
    public static ArrayList<Note> mapCursorToArrayList(Cursor noteCursor){
        ArrayList<Note> noteArrayList = new ArrayList<>();

        while (noteCursor.moveToNext()){
            int id = noteCursor.getInt(noteCursor.getColumnIndexOrThrow(_ID));
            String title = noteCursor.getString(noteCursor.getColumnIndexOrThrow(TITLE));
            String description = noteCursor.getString(noteCursor.getColumnIndexOrThrow(DESC));
            String date = noteCursor.getString(noteCursor.getColumnIndexOrThrow(DATE));
            noteArrayList.add(new Note(id, title, description, date));
        }

        return noteArrayList;
    }
}
