package radonsoft.firenotes;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import radonsoft.firenotes.Models.Note;

public class NoteActivity extends AppCompatActivity {

    EditText title;
    EditText text;
    private boolean ifEdit;
    AppDatabase db;
    int noteID;
    public static List<Note> noteList = new ArrayList<Note>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        ifEdit = getIntent().getBooleanExtra("editMode", false);
        noteID = getIntent().getIntExtra("ID", 0);

        title = (EditText) findViewById(R.id.title_edit);
        text = (EditText) findViewById(R.id.note_edit);

        Toolbar toolbar = (Toolbar) findViewById(R.id.note_toolbar);
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
            if (ifEdit) {
                if (!title.getText().toString().equals("") & !text.getText().toString().equals("")) {
                    noteList = db.noteDao().getAllNotes();
                    int pos = noteList.size() - noteID - 1;
                    Note note = noteList.get(pos);
                    note.title = title.getText().toString();
                    note.text = text.getText().toString();
                    db.noteDao().update(noteList.get(pos));
                }
            } else {
                if (!title.getText().toString().equals("") & !text.getText().toString().equals("")) {
                    db.noteDao().insertAll(new Note(title.getText().toString(), text.getText().toString()));
                }
            }
            changeActivity();
            return true;
        }

        return super.onOptionsItemSelected(item);
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
        changeActivity();
        super.onBackPressed();  // optional depending on your needs
    }
}
