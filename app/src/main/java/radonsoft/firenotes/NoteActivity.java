package radonsoft.firenotes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import radonsoft.firenotes.Fragments.NoteFragment;

public class NoteActivity extends AppCompatActivity {

    EditText title;
    EditText text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        title = (EditText) findViewById(R.id.title_edit);
        text = (EditText) findViewById(R.id.note_edit);

        Toolbar toolbar = (Toolbar) findViewById(R.id.note_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("New note");
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
            changeActivity();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void changeActivity(){
        NoteFragment notes = new NoteFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", title.getText().toString());
        bundle.putString("text", text.getText().toString());
        notes.setArguments(bundle);

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
