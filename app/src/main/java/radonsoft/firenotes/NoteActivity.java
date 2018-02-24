package radonsoft.firenotes;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
    Toolbar toolbar;

    int noteID;
    int color;
    private boolean ifEdit;
    public static List<Note> noteList = new ArrayList<>();
    public Calendar dateAndTime = Calendar.getInstance();

    DateDialogFragment dateDialog = new DateDialogFragment();
    RadioGroup colorPicker;
    Animation slideUp;
    Animation slideDown;

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

        toolbar = findViewById(R.id.note_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("New note");

        colorPickerBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (colorPickerLayout.getVisibility() == View.VISIBLE){
                    hideColorPicher();
                }
            }
        });
        colorPicker.check(R.id.rb_white);
        colorPicker.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                color = checkedId;
                setColor(parseColor(color));
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
            colorPicker.check(note.color);
        }
        slideUp = AnimationUtils.loadAnimation(this, R.anim.side_up);
        slideDown = AnimationUtils.loadAnimation(this, R.anim.side_down);
        setColor(parseColor(colorPicker.getCheckedRadioButtonId()));
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
        if (id == R.id.navbar_color){
            if (colorPickerLayout.getVisibility() == View.VISIBLE){
                hideColorPicher();
            } else {
                showColorPicker();
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

    private int parseColor(int color_id){
        switch (color_id){
            case (R.id.rb_red): return Color.parseColor("#F77272");
            case (R.id.rb_green): return Color.parseColor("#A8FA9B");
            case (R.id.rb_blue): return Color.parseColor("#7C8FF7");
            case (R.id.rb_yellow): return Color.parseColor("#F7E272");
            case (R.id.rb_violet): return Color.parseColor("#D086E3");
            default: return Color.parseColor("#ffffff");
        }
    }
    private void hideColorPicher(){
        colorPickerLayout.startAnimation(slideUp);
        colorPicker.setVisibility(View.GONE);
        colorPickerLayout.setVisibility(View.GONE);
        colorPickerBackground.setVisibility(View.GONE);
    }

    private void showColorPicker(){
        colorPickerLayout.startAnimation(slideDown);
        colorPicker.setVisibility(View.VISIBLE);
        colorPickerLayout.setVisibility(View.VISIBLE);
        colorPickerBackground.setVisibility(View.VISIBLE);
    }

    private void setColor(int color){
        Window window = getWindow();
        window.setStatusBarColor(color);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(color));
        text.setBackgroundColor(color);
        title.setBackgroundColor(color);
    }

    public void processNote(){
        if (ifEdit) {
            if (!text.getText().toString().equals("")) {
                noteList = db.noteDao().getAllNotes();
                int pos = noteList.size() - noteID - 1;
                Note note = noteList.get(pos);
                note.title = title.getText().toString();
                note.text = text.getText().toString();
                note.color = color;
                db.noteDao().update(noteList.get(pos));
            }
        } else {
            if (!text.getText().toString().equals("")) {
                db.noteDao().insertAll(new Note(title.getText().toString(), text.getText().toString(), color));
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
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (colorPickerLayout.getVisibility() == View.VISIBLE){
            colorPickerLayout.startAnimation(slideUp);
            colorPickerLayout.setVisibility(View.GONE);
            colorPickerBackground.setVisibility(View.GONE);
        } else{
            processNote();
            changeActivity();
            super.onBackPressed();
        }
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
