package com.bayuirfan.notesapp.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.bayuirfan.notesapp.model.Note;

import java.util.ArrayList;

import static com.bayuirfan.notesapp.database.DatabaseContract.NoteColumns.DATE;
import static com.bayuirfan.notesapp.database.DatabaseContract.NoteColumns.DESC;
import static com.bayuirfan.notesapp.database.DatabaseContract.NoteColumns.TITLE;
import static com.bayuirfan.notesapp.database.DatabaseContract.NoteColumns._ID;
import static com.bayuirfan.notesapp.database.DatabaseContract.TABLE_NOTE;

public class NoteHelper {
    private static DatabaseHelper databaseHelper;
    private static NoteHelper INSTANCE;

    private static SQLiteDatabase database;

    private NoteHelper(Context context){
        databaseHelper = new DatabaseHelper(context);
    }

    public static NoteHelper getInstance(Context context){
        if (INSTANCE == null){
            synchronized (SQLiteDatabase.class){
                if (INSTANCE == null){
                    INSTANCE = new NoteHelper(context);
                }
            }
        }

        return INSTANCE;
    }

    public void open() throws SQLException {
        database = databaseHelper.getWritableDatabase();
    }

    public void close(){
        databaseHelper.close();

        if (database.isOpen()){
            database.close();
        }
    }

    public ArrayList<Note> getAllNotes(){
        ArrayList<Note> arrayList = new ArrayList<>();
        @SuppressLint("Recycle") Cursor cursor = database.query(TABLE_NOTE, null,
                null,
                null,
                null,
                null,
                _ID + " ASC",
                null);

        cursor.moveToFirst();
        Note note;
        if (cursor.getCount() > 0){
            do {
                note = new Note();
                note.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                note.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TITLE)));
                note.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(DESC)));
                note.setDate(cursor.getString(cursor.getColumnIndexOrThrow(DATE)));

                arrayList.add(note);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }

        cursor.close();
        return arrayList;
    }

    public long insertData(Note note){
        ContentValues args = new ContentValues();
        args.put(TITLE, note.getTitle());
        args.put(DESC, note.getDescription());
        args.put(DATE, note.getDate());

        return database.insert(TABLE_NOTE, null, args);
    }

    public int updateNote(Note note){
        ContentValues args = new ContentValues();
        args.put(TITLE, note.getTitle());
        args.put(DESC, note.getDescription());
        args.put(DATE, note.getDate());

        return database.update(TABLE_NOTE, args, _ID + " = '" + note.getId() + "'", null);
    }

    public int deleteNote(int id){
        return database.delete(TABLE_NOTE, _ID + " = '" + id + "'", null);
    }

    public Cursor queryByIdProvider(String id){
        return database.query(TABLE_NOTE, null,
                _ID + " = ?",
                new String[]{id},
                null,
                null,
                null,
                null);
    }

    public Cursor queryProvider(){
        return database.query(TABLE_NOTE,
                null,
                null,
                null,
                null,
                null,
                _ID + " ASC");
    }

    public long insertProvider(ContentValues values){
        return database.insert(TABLE_NOTE, null, values);
    }

    public int updateProvider(String id, ContentValues values){
        return database.update(TABLE_NOTE, values, _ID + " = ?", new String[]{id});
    }

    public int deleteProvider(String id){
        return database.delete(TABLE_NOTE, _ID + " = ?", new String[]{id});
    }
}
