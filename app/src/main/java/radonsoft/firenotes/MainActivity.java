package radonsoft.firenotes;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import radonsoft.firenotes.Fragments.NoteFragment;
import radonsoft.firenotes.Fragments.SettingsFragment;
import radonsoft.firenotes.Models.Note;

public class MainActivity extends AppCompatActivity {

    NoteFragment notes = new NoteFragment();
    AppDatabase db;

    FragmentTransaction ftrans = getSupportFragmentManager().beginTransaction();
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction navtrans = fragmentManager.beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_notes:
                    navtrans.replace(R.id.content, new NoteFragment()).commit();
                    return true;
                case R.id.navigation_settings:
                    navtrans.replace(R.id.content, new SettingsFragment()).commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton2);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeActivity();
            }
        });

        ftrans.replace(R.id.content, new NoteFragment());
        ftrans.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        String toShowTitle = data.getStringExtra("title");
        String toShowText = data.getStringExtra("title");
        Toast.makeText(this, toShowTitle, Toast.LENGTH_SHORT).show();
    }


    public void changeActivity(){
        Intent intent = new Intent(this, NoteActivity.class);
        startActivityForResult(intent, 1);
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
            db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "production")
                    .allowMainThreadQueries()
                    .build();
            db.noteDao().delete(NoteFragment.noteList.get(NoteFragment.position));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
