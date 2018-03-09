package radonsoft.firenotes;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import radonsoft.firenotes.Fragments.NoteFragment;
import radonsoft.firenotes.Fragments.SettingsFragment;
import radonsoft.firenotes.Helpers.ViewPagerAdapter;

public class MainActivity extends AppCompatActivity  {
    NoteFragment noteFragment;
    SettingsFragment settingsFragment = new SettingsFragment();
    ViewPagerAdapter adapter;

    public void setupViewPager(ViewPager viewPager){
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(noteFragment, "Notes");
        adapter.addFragment(settingsFragment, "Settings");
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(pageChangeListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null){
            noteFragment = new NoteFragment();
            settingsFragment = new SettingsFragment();
        } else {
           noteFragment = (NoteFragment) getSupportFragmentManager().getFragment(savedInstanceState, "NOTES");
           settingsFragment = (SettingsFragment) getSupportFragmentManager().getFragment(savedInstanceState, "SETTINGS");
        }

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ViewPager viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        TabLayout tabLayout = findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);

        if (Build.VERSION.SDK_INT >= 26){
            int flags = viewPager.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
            viewPager.setSystemUiVisibility(flags);
            this.getWindow().setNavigationBarColor(getColor(R.color.colorPrimary));
        } else{
            this.getWindow().setNavigationBarColor(Color.parseColor("#BDBDBD"));
        }

        FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButton2);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeActivity();
            }
        });
    }

    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

        @Override
        public void onPageSelected(int position) {
            if (position == 1) ((FragmentLifecycle) adapter.getItem(0)).onPauseFragment();
        }

        @Override
        public void onPageScrollStateChanged(int state) {}
    };

    public interface FragmentLifecycle {
        void onPauseFragment();
        void onResumeFragment();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {}

    public void changeActivity(){
        Intent intent = new Intent(this, NoteActivity.class);
        intent.putExtra("editMode", false);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState){
        savedInstanceState.putBoolean("FRAGMENTS", true);
        getSupportFragmentManager().putFragment(savedInstanceState, "NOTES", noteFragment);
        getSupportFragmentManager().putFragment(savedInstanceState, "SETTINGS", settingsFragment);
        super.onSaveInstanceState(savedInstanceState);
    }
}
