package com.bayuirfan.notesapp.database;

import android.provider.BaseColumns;

class DatabaseContract {

    static String TABLE_NOTE = "note";

    static final class NoteColumns implements BaseColumns {
        static String TITLE = "title";
        static String DESC = "desc";
        static String DATE = "date";
    }
}
