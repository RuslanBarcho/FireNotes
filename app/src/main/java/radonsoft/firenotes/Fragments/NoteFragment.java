package radonsoft.firenotes.Fragments;

import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import radonsoft.firenotes.AppDatabase;
import radonsoft.firenotes.Helpers.RecyclerViewAdapter;
import radonsoft.firenotes.Models.Note;
import radonsoft.firenotes.R;


public class NoteFragment extends Fragment {
    View mRootView;

    RecyclerView recyclerView;
    RecyclerViewAdapter adapter;

    AppDatabase db;

    public static int position;

    public static List<Note> noteList = new ArrayList<Note>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_note, container, false);
        Toolbar toolbar = (Toolbar) mRootView.findViewById(R.id.main_toolbar);
        AppCompatActivity activity = new AppCompatActivity();
        setHasOptionsMenu(true);
        recyclerView = (RecyclerView) mRootView.findViewById(R.id.recycler);
        initialNotes();
        return mRootView;
    }


    public void initialNotes() {
        db = Room.databaseBuilder(getContext(), AppDatabase.class, "production")
                .allowMainThreadQueries()
                .build();
        noteList = db.noteDao().getAllNotes();
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLayoutManager);
        adapter = new RecyclerViewAdapter(noteList);
        recyclerView.setAdapter(adapter);
        adapter.setItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                NoteFragment.position = position;
                Toast.makeText(getContext(), "Ты жмакнул на заметку " + NoteFragment.position, Toast.LENGTH_SHORT).show();
            }
        });
        adapter.setItemLongClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                NoteFragment.position = position;
                Toast.makeText(getContext(), "Ты долго жмакнул на заметку " + NoteFragment.position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.note_navbar, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.navbar_done) {
            db = Room.databaseBuilder(getContext(), AppDatabase.class, "production")
                    .allowMainThreadQueries()
                    .build();
            db.noteDao().delete(NoteFragment.noteList.get(NoteFragment.position));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        initialNotes();
        super.onResume();
    }

}
