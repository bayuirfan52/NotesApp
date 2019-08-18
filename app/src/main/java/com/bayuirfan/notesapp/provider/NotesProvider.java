package com.bayuirfan.notesapp.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bayuirfan.notesapp.database.NoteHelper;
import com.bayuirfan.notesapp.features.MainActivity;

import static com.bayuirfan.notesapp.database.DatabaseContract.AUTHORITY;
import static com.bayuirfan.notesapp.database.DatabaseContract.NoteColumns.CONTENT_URI;
import static com.bayuirfan.notesapp.database.DatabaseContract.TABLE_NOTE;

public class NotesProvider extends ContentProvider {
    private static final int NOTE = 1;
    private static final int NOTE_ID = 2;
    private static final UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private NoteHelper noteHelper;

    static {
        mUriMatcher.addURI(AUTHORITY, TABLE_NOTE, NOTE);
        mUriMatcher.addURI(AUTHORITY, TABLE_NOTE + "/#", NOTE_ID);
    }

    @Override
    public boolean onCreate() {
        noteHelper = NoteHelper.getInstance(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        noteHelper.open();
        Cursor cursor;
        switch (mUriMatcher.match(uri)){
            case NOTE:
                cursor = noteHelper.queryProvider();
                break;
            case NOTE_ID:
                cursor = noteHelper.queryByIdProvider(uri.getLastPathSegment());
                break;
            default:
                cursor = null;
                break;
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {

        return null;
    }
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri,@Nullable ContentValues values) {
        noteHelper.open();
        long added;
        if (mUriMatcher.match(uri) == NOTE) {
            added = noteHelper.insertProvider(values);
        } else {
            added = 0;
        }

        notifyChange();
        return Uri.parse(CONTENT_URI + "/" + added);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        noteHelper.open();
        int deleted;
        if (mUriMatcher.match(uri) == NOTE_ID){
            deleted = noteHelper.deleteProvider(uri.getLastPathSegment());
        } else {
            deleted = 0;
        }

        notifyChange();
        return deleted;
    }

    @Override
    public int update( @NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        noteHelper.open();
        int updated;
        if (mUriMatcher.match(uri) == NOTE_ID){
            updated = noteHelper.updateProvider(uri.getLastPathSegment(), values);
        } else {
            updated = 0;
        }

        notifyChange();
        return updated;
    }

    private void notifyChange(){
        if (getContext() != null){
            getContext().getContentResolver().notifyChange(CONTENT_URI, new MainActivity.DataObserver(new Handler(), getContext()));
        }
    }
}
