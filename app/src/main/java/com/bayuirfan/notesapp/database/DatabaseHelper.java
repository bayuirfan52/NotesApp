package com.bayuirfan.notesapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.bayuirfan.notesapp.database.DatabaseContract.NoteColumns;
import static com.bayuirfan.notesapp.database.DatabaseContract.TABLE_NOTE;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "dbnoteapp";
    private static final int DATABASE_VERSION = 1;
    private static final String SQL_CREATE_NOTE_TABLE = String.format("CREATE TABLE %s " +
            "(%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "%s TEXT NOT NULL, " +
            "%s TEXT NOT NULL, " +
            "%s TEXT NOT NULL)",
            TABLE_NOTE,
            NoteColumns._ID,
            NoteColumns.TITLE,
            NoteColumns.DESC,
            NoteColumns.DATE);

    DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_NOTE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTE);
        onCreate(db);
    }
}
