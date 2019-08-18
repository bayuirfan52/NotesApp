package com.bayuirfan.notesapp.utils;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;

import static com.bayuirfan.notesapp.database.DatabaseContract.NoteColumns.CONTENT_URI;

public class LoadNotesAsync extends AsyncTask<Void, Void, Cursor> {
    private final WeakReference<Context> weakContext;
    private final WeakReference<LoadNotesCallback> weakNotesCallback;

    public LoadNotesAsync(Context context, LoadNotesCallback loadNotesCallback){
        weakContext = new WeakReference<>(context);
        weakNotesCallback = new WeakReference<>(loadNotesCallback);
    }



    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        weakNotesCallback.get().preExecute();
    }

    @Override
    protected Cursor doInBackground(Void... voids) {
        Context context = weakContext.get();
        return context.getContentResolver().query(CONTENT_URI, null,null,null,null);
    }

    @Override
    protected void onPostExecute(Cursor cursor) {
        super.onPostExecute(cursor);
        weakNotesCallback.get().postExecute(cursor);
    }
}
