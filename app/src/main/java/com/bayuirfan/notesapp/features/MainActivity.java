package com.bayuirfan.notesapp.features;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.bayuirfan.notesapp.R;
import com.bayuirfan.notesapp.adapter.NoteAdapter;
import com.bayuirfan.notesapp.database.NoteHelper;
import com.bayuirfan.notesapp.model.Note;
import com.bayuirfan.notesapp.utils.LoadNotesAsync;
import com.bayuirfan.notesapp.utils.LoadNotesCallback;

import java.util.ArrayList;

import static com.bayuirfan.notesapp.features.NoteAddUpdateActivity.EXTRA_NOTE;
import static com.bayuirfan.notesapp.features.NoteAddUpdateActivity.EXTRA_POSITION;
import static com.bayuirfan.notesapp.features.NoteAddUpdateActivity.REQUEST_ADD;
import static com.bayuirfan.notesapp.features.NoteAddUpdateActivity.RESULT_ADD;
import static com.bayuirfan.notesapp.features.NoteAddUpdateActivity.RESULT_DELETE;
import static com.bayuirfan.notesapp.features.NoteAddUpdateActivity.RESULT_UPDATE;

public class MainActivity extends AppCompatActivity implements LoadNotesCallback, View.OnClickListener {
    private ProgressBar progressBar;
    private RecyclerView rvMain;
    private static final String EXTRA_STATE = "extra_state";
    private NoteAdapter noteAdapter;
    private NoteHelper noteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(getResources().getString(R.string.note));

        rvMain = findViewById(R.id.rv_main);
        progressBar = findViewById(R.id.progress_main);
        FloatingActionButton fabAdd = findViewById(R.id.fab_add);

        rvMain.setLayoutManager(new LinearLayoutManager(this));
        rvMain.setHasFixedSize(true);

        noteHelper = NoteHelper.getInstance(this.getApplicationContext());
        noteHelper.open();

        fabAdd.setOnClickListener(this);

        noteAdapter = new NoteAdapter(this);
        rvMain.setAdapter(noteAdapter);

        if (savedInstanceState == null){
            loadData();
        } else {
            ArrayList<Note> list = savedInstanceState.getParcelableArrayList(EXTRA_STATE);
            if (list != null){
                noteAdapter.setListNotes(list);
            }
        }
    }

    private void loadData(){
        new LoadNotesAsync(noteHelper, this).execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        noteHelper.close();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_STATE, noteAdapter.getListNotes());
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fab_add){
            Intent intent = new Intent(MainActivity.this, NoteAddUpdateActivity.class);
            startActivityForResult(intent, REQUEST_ADD);
        }
    }

    @Override
    public void preExecute() {
        runOnUiThread(() -> progressBar.setVisibility(View.VISIBLE));
    }

    @Override
    public void postExecute(ArrayList<Note> noteArrayList) {
        progressBar.setVisibility(View.GONE);
        noteAdapter.setListNotes(noteArrayList);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null){
            if (requestCode == REQUEST_ADD){
                if (resultCode == RESULT_ADD){
                    Note note = data.getParcelableExtra(EXTRA_NOTE);
                    noteAdapter.addItem(note);
                    rvMain.smoothScrollToPosition(noteAdapter.getItemCount() - 1);
                    showSnackbarMessage(getResources().getString(R.string.success_add_message));
                }
                else if (resultCode == RESULT_UPDATE){
                    Note note = data.getParcelableExtra(EXTRA_NOTE);
                    int position = data.getIntExtra(EXTRA_POSITION, 0);
                    noteAdapter.updateItem(position, note);
                    rvMain.smoothScrollToPosition(position);
                    showSnackbarMessage(getResources().getString(R.string.success_update_message));
                }
                else if (resultCode == RESULT_DELETE){
                    int position = data.getIntExtra(EXTRA_POSITION, 0);
                    noteAdapter.removeItem(position);
                    showSnackbarMessage(getResources().getString(R.string.success_delete_message));
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void showSnackbarMessage(String message){
        Snackbar.make(this.rvMain, message, Snackbar.LENGTH_SHORT).show();
    }
}
