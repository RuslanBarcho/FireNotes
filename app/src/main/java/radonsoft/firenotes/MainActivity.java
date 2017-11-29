package radonsoft.firenotes;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import radonsoft.firenotes.Fragments.NoteFragment;
import radonsoft.firenotes.Fragments.SettingsFragment;

public class MainActivity extends AppCompatActivity {
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

        ftrans.replace(R.id.content, new NoteFragment());
        ftrans.commit();
    }

}
