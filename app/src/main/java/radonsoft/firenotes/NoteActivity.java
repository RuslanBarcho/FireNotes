package radonsoft.firenotes;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import radonsoft.firenotes.Helpers.DateDialogFragment;
import radonsoft.firenotes.Models.Note;

public class NoteActivity extends AppCompatActivity implements DateDialogFragment.YesNoListener{
    EditText title;
    EditText text;
    AppDatabase db;

    LinearLayout colorPickerLayout;
    LinearLayout colorPickerBackground;

    private boolean ifEdit;
    int noteID;
    public static List<Note> noteList = new ArrayList<>();
    public Calendar dateAndTime = Calendar.getInstance();

    DateDialogFragment dateDialog = new DateDialogFragment();
    RadioGroup colorPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null){
            dateAndTime = (Calendar) savedInstanceState.getSerializable("CALENDAR_");
            Log.i("MSG", "DATA RESTORED");
        }
        setContentView(R.layout.activity_note);

        ifEdit = getIntent().getBooleanExtra("editMode", false);
        noteID = getIntent().getIntExtra("ID", 0);

        title = findViewById(R.id.title_edit);
        text = findViewById(R.id.note_edit);
        colorPickerLayout = findViewById(R.id.check_colorLayout);
        colorPickerBackground = findViewById(R.id.check_color_background);
        colorPicker = findViewById(R.id.check_color);

        Toolbar toolbar = findViewById(R.id.note_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("New note");

        colorPickerBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (colorPickerLayout.getVisibility() == View.VISIBLE){
                    colorPickerLayout.setVisibility(View.GONE);
                    colorPickerBackground.setVisibility(View.GONE);
                }
            }
        });
        colorPicker.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case (R.id.rb_red):{
                        Toast.makeText(NoteActivity.this, "RED", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
            }
        });

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "production")
                .allowMainThreadQueries()
                .build();

        if (ifEdit){
            noteList = db.noteDao().getAllNotes();
            int pos = noteList.size() - noteID - 1;
            Note note = noteList.get(pos);
            title.setText(note.title);
            text.setText(note.text);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.note_navbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //if (id == R.id.navbar_done) {
        //    processNote();
        //    changeActivity();
        //    return true;
        //}
        if (id == R.id.navbar_color){
            if (colorPickerLayout.getVisibility() == View.VISIBLE){
                colorPickerLayout.setVisibility(View.GONE);
                colorPickerBackground.setVisibility(View.GONE);
            } else {
                colorPickerLayout.setVisibility(View.VISIBLE);
                colorPickerBackground.setVisibility(View.VISIBLE);
            }
        }

        if (id == R.id.navbar_schedule) {
            Bundle args = new Bundle();
            args.putSerializable("ACTIVITY_CALENDAR", dateAndTime);
            dateDialog.setArguments(args);
            dateDialog.show(getFragmentManager(), "tag");
        }
        return super.onOptionsItemSelected(item);
    }

    public void processNote(){
        if (ifEdit) {
            if (!text.getText().toString().equals("")) {
                noteList = db.noteDao().getAllNotes();
                int pos = noteList.size() - noteID - 1;
                Note note = noteList.get(pos);
                note.title = title.getText().toString();
                note.text = text.getText().toString();
                db.noteDao().update(noteList.get(pos));
            }
        } else {
            if (!text.getText().toString().equals("")) {
                db.noteDao().insertAll(new Note(title.getText().toString(), text.getText().toString()));
            }
        }
    }

    public void dateToast(){
        String date = DateUtils.formatDateTime(this,
                dateAndTime.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR
                        | DateUtils.FORMAT_SHOW_TIME);
        Toast.makeText(this, date, Toast.LENGTH_SHORT).show();
    }

    public void changeActivity() {
        Intent intent = new Intent();
        intent.putExtra("title", title.getText().toString());
        intent.putExtra("text", text.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (colorPickerLayout.getVisibility() == View.VISIBLE){
            colorPickerLayout.setVisibility(View.GONE);
            colorPickerBackground.setVisibility(View.GONE);
        } else{
            processNote();
            changeActivity();
            super.onBackPressed();
        }
         // optional depending on your needs
    }

    @Override
    public void onYes(Calendar time){
        dateAndTime = time;
        dateToast();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("CALENDAR_", dateAndTime);
        Log.i("MSG", "DATA SAVED");
        super.onSaveInstanceState(outState);
    }
}
