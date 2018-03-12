package radonsoft.firenotes;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
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
    LinearLayout colorPickerBackground;
    Toolbar toolbar;
    RadioGroup colorPicker;
    Animation slideUp;
    Animation slideDown;
    int noteID;
    int color;
    private boolean ifEdit;
    public List<Note> noteList = new ArrayList<>();
    public Calendar dateAndTime = Calendar.getInstance();
    DateDialogFragment dateDialog = new DateDialogFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null){
            dateAndTime = (Calendar) savedInstanceState.getSerializable("CALENDAR_");
        }
        setContentView(R.layout.activity_note);

        ifEdit = getIntent().getBooleanExtra("editMode", false);
        noteID = getIntent().getIntExtra("ID", 0);

        slideUp = AnimationUtils.loadAnimation(this, R.anim.side_up);
        slideDown = AnimationUtils.loadAnimation(this, R.anim.side_down);

        title = findViewById(R.id.title_edit);
        text = findViewById(R.id.note_edit);

        if (Build.VERSION.SDK_INT >= 26){
            int flags = text.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
            text.setSystemUiVisibility(flags);
            this.getWindow().setNavigationBarColor(getColor(R.color.colorPrimary));
        }

        colorPickerBackground = findViewById(R.id.check_color_background);
        colorPicker = findViewById(R.id.check_color);

        toolbar = findViewById(R.id.note_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        colorPickerBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (colorPicker.getVisibility() == View.VISIBLE){
                    setColorPickerVisibility(View.GONE, slideUp);
                }
            }
        });

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
            Note note = noteList.get(noteList.size() - noteID - 1);
            title.setText(note.title);
            text.setText(note.text);
            colorPicker.check(note.color);
        } else {
            colorPicker.check(R.id.rb_white);
        }
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
        if (id == android.R.id.home){
            processNote();
            changeActivity();
        }

        if (id == R.id.navbar_color){
            if (colorPicker.getVisibility() == View.VISIBLE){
                setColorPickerVisibility(View.GONE, slideUp);
            } else {
                setColorPickerVisibility(View.VISIBLE, slideDown);
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
            default: {
                return getColor(R.color.colorPrimary);
            }
        }
    }

    private void setColorPickerVisibility(int visibility, Animation animation){
        colorPicker.startAnimation(animation);
        colorPicker.setVisibility(visibility);
        colorPickerBackground.setVisibility(visibility);
    }

    private void setColor(int color){
        getWindow().setStatusBarColor(color);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(color));
        text.setBackgroundColor(color);
        title.setBackgroundColor(color);
        if (Build.VERSION.SDK_INT >= 26) {
            this.getWindow().setNavigationBarColor(color);
        } else {
            if (color == getColor(R.color.colorPrimary)){
                this.getWindow().setNavigationBarColor(Color.parseColor("#BDBDBD"));
            } else {
                this.getWindow().setNavigationBarColor(color);
            }
        }
    }

    public void processNote(){
        if (!text.getText().toString().equals("")) {
            if (ifEdit) {
                noteList = db.noteDao().getAllNotes();
                Note note = noteList.get(noteList.size() - noteID - 1);
                note.title = title.getText().toString();
                note.text = text.getText().toString();
                note.color = color;
                db.noteDao().update(noteList.get(noteList.size() - noteID - 1));
            } else {
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
        if (colorPicker.getVisibility() == View.VISIBLE){
            setColorPickerVisibility(View.GONE, slideUp);
        } else{
            processNote();
            changeActivity();
            super.onBackPressed();
        }
    }

    @Override
    public void onSetDate(Calendar time){
        dateAndTime = time;
        dateToast();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("CALENDAR_", dateAndTime);
        super.onSaveInstanceState(outState);
    }
}
