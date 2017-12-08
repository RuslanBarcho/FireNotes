package radonsoft.firenotes;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import radonsoft.firenotes.Models.Note;

public class NoteActivity extends AppCompatActivity {

    EditText title;
    EditText text;
    private boolean ifEdit;
    AppDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        title = (EditText) findViewById(R.id.title_edit);
        text = (EditText) findViewById(R.id.note_edit);

        Toolbar toolbar = (Toolbar) findViewById(R.id.note_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("New note");

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "production")
                .allowMainThreadQueries()
                .build();
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
            // TODO: save to database
            db.noteDao().insertAll(new Note(title.getText().toString(), text.getText().toString() ));
            changeActivity();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void changeActivity(){

        Intent intent = new Intent();
        intent.putExtra("title", title.getText().toString());
        intent.putExtra("text", text.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed()
    {
        changeActivity();
        super.onBackPressed();  // optional depending on your needs
    }
}
