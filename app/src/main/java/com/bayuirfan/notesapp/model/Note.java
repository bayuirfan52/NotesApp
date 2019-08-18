package com.bayuirfan.notesapp.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import static com.bayuirfan.notesapp.database.DatabaseContract.NoteColumns.DATE;
import static com.bayuirfan.notesapp.database.DatabaseContract.NoteColumns.DESC;
import static com.bayuirfan.notesapp.database.DatabaseContract.NoteColumns.TITLE;
import static com.bayuirfan.notesapp.database.DatabaseContract.NoteColumns._ID;
import static com.bayuirfan.notesapp.database.DatabaseContract.getColumnInt;
import static com.bayuirfan.notesapp.database.DatabaseContract.getColumnString;

public class Note implements Parcelable {
    private int id;
    private String title;
    private String description;
    private String date;

    public Note() {}

    public Note(int id, String title, String description, String date) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
    }

    public Note(Cursor cursor){
        this.id = getColumnInt(cursor, _ID);
        this.title = getColumnString(cursor, TITLE);
        this.description = getColumnString(cursor, DESC);
        this.date = getColumnString(cursor, DATE);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeString(this.date);
    }

    private Note(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.description = in.readString();
        this.date = in.readString();
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel source) {
            return new Note(source);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };
}
