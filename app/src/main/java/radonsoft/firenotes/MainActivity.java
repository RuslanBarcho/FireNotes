package radonsoft.firenotes;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import radonsoft.firenotes.Fragments.NoteFragment;
import radonsoft.firenotes.Fragments.SettingsFragment;

public class MainActivity extends AppCompatActivity {
    int[][] states = new int[][] {
            new int[] { android.R.attr.state_enabled}, // enabled
            new int[] {-android.R.attr.state_enabled}, // disabled
            new int[] {-android.R.attr.state_checked}, // unchecked
            new int[] { android.R.attr.state_pressed}  // pressed
    };

    int[] colors = new int[] {
            Color.BLACK,
            Color.BLACK,
            Color.BLACK,
            Color.BLACK
    };
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

        ColorStateList myList = new ColorStateList(states, colors);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setItemIconTintList(myList);
        navigation.setItemTextColor(myList);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButton2);

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
        //Toast.makeText(this, toShowTitle, Toast.LENGTH_SHORT).show();
    }

    public void changeActivity(){
        Intent intent = new Intent(this, NoteActivity.class);
        intent.putExtra("editMode", false);
        startActivityForResult(intent, 1);
    }
}
