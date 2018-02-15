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
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import radonsoft.firenotes.Helpers.DateDialogFragment;
import radonsoft.firenotes.Models.Note;

public class NoteActivity extends AppCompatActivity implements DateDialogFragment.YesNoListener{

    EditText title;
    EditText text;
    private boolean ifEdit;
    AppDatabase db;
    int noteID;
    public static List<Note> noteList = new ArrayList<>();
    public Calendar dateAndTime = Calendar.getInstance();
    DateDialogFragment datedia = new DateDialogFragment();

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

        Toolbar toolbar = findViewById(R.id.note_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("New note");

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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.navbar_done) {
            processNote();
            changeActivity();
            return true;
        }

        if (id == R.id.navbar_schedule) {
            Bundle args = new Bundle();
            args.putSerializable("ACTIVITY_CALENDAR", dateAndTime);
            datedia.setArguments(args);
            datedia.show(getFragmentManager(), "tag");
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
        processNote();
        changeActivity();
        super.onBackPressed();  // optional depending on your needs
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
