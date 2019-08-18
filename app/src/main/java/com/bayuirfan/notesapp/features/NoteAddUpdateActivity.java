package com.bayuirfan.notesapp.features;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bayuirfan.notesapp.R;
import com.bayuirfan.notesapp.database.NoteHelper;
import com.bayuirfan.notesapp.model.Note;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NoteAddUpdateActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText edtTitle, edtDescription;

    public static final String EXTRA_NOTE = "extra_note";
    public static final String EXTRA_POSITION = "extra_position";

    public boolean isEdit = false;
    public static final int REQUEST_UPDATE = 0x101;
    public static final int RESULT_UPDATE = 0x102;

    public static final int REQUEST_ADD = 0x201;
    public static final int RESULT_ADD = 0x202;

    public static final int RESULT_DELETE = 0x301;

    private static final int ACTION_DELETE = 0x1;
    private static final int ACTION_CLOSE = 0x2;

    private Note note;
    private int position;

    private NoteHelper noteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_add_update);

        edtTitle = findViewById(R.id.edt_title);
        edtDescription = findViewById(R.id.edt_desc);
        Button btnSubmit = findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(this);

        noteHelper = NoteHelper.getInstance(this.getApplicationContext());
        note = getIntent().getParcelableExtra(EXTRA_NOTE);
        if (note != null){
            position = getIntent().getIntExtra(EXTRA_POSITION,0);
            isEdit = true;
        } else {
            note = new Note();
        }

        String actionBarTitle;
        String btnTitle;

        if (isEdit){
            actionBarTitle = getResources().getString(R.string.change);
            btnTitle = getResources().getString(R.string.update);
            if (note != null){
                edtTitle.setText(note.getTitle());
                edtDescription.setText(note.getDescription());
            }
        } else {
            actionBarTitle = getResources().getString(R.string.add);
            btnTitle = getResources().getString(R.string.submit);
        }

        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle(actionBarTitle);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        btnSubmit.setText(btnTitle);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_submit){
            String title = edtTitle.getText().toString().trim();
            String description = edtDescription.getText().toString().trim();

            if (TextUtils.isEmpty(title)) {
                edtTitle.setError(getResources().getString(R.string.empty_message));
                return;
            }

            note.setTitle(title);
            note.setDescription(description);

            Intent intent = new Intent();
            intent.putExtra(EXTRA_NOTE, note);
            intent.putExtra(EXTRA_POSITION, position);

            if (isEdit){
                long result = noteHelper.updateNote(note);
                if (result > 0){
                    setResult(RESULT_UPDATE, intent);
                    finish();
                } else {
                    showToast(getResources().getString(R.string.error_update_message));
                }
            } else {
                note.setDate(getCurrentDate());
                long result = noteHelper.insertData(note);

                if (result > 0){
                    note.setId((int) result);
                    setResult(RESULT_ADD, intent);
                    finish();
                } else {
                    showToast(getResources().getString(R.string.error_add_message));
                }
            }

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        showDialogAction(ACTION_CLOSE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isEdit){
            getMenuInflater().inflate(R.menu.menu_add_update, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                showDialogAction(ACTION_CLOSE);
                break;
            case R.id.action_delete:
                showDialogAction(ACTION_DELETE);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDialogAction(int TYPE){
        final boolean isDialogClose = TYPE == ACTION_CLOSE;
        String dialogTitle, dialogMessage;

        if (isDialogClose) {
            dialogTitle = getResources().getString(R.string.cancel);
            dialogMessage = getResources().getString(R.string.question_cancel);
        } else {
            dialogTitle = getResources().getString(R.string.delete);
            dialogMessage = getResources().getString(R.string.question_delete);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(dialogTitle)
                .setMessage(dialogMessage)
                .setNegativeButton(getResources().getString(R.string.no), (dialog, which) -> dialog.cancel())
                .setPositiveButton(getResources().getString(R.string.yes), ((dialog, which) -> {
                    if (isDialogClose){
                        closeActivity();
                    } else {
                        long result = noteHelper.deleteNote(note.getId());
                        if (result > 0){
                            Intent intent = new Intent();
                            intent.putExtra(EXTRA_POSITION, position);
                            setResult(RESULT_DELETE, intent);
                            closeActivity();
                        } else {
                            showToast(getResources().getString(R.string.error_delete_message));
                        }
                    }
                }));
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showToast(String message){
        Toast.makeText(NoteAddUpdateActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void closeActivity(){
        finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    private String getCurrentDate(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}