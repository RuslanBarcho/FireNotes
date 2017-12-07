package radonsoft.firenotes.Fragments;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import radonsoft.firenotes.AppDatabase;
import radonsoft.firenotes.FireNotes;
import radonsoft.firenotes.Helpers.RecyclerViewAdapter;
import radonsoft.firenotes.Models.Note;
import radonsoft.firenotes.R;


public class NoteFragment extends Fragment {
    View mRootView;

    RecyclerView recyclerView;
    RecyclerViewAdapter adapter;
    RecyclerView.LayoutManager layoutManager;

    AppDatabase db;

    public static int position;

    public static List<Note> noteList = new ArrayList<Note>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_note, container, false);
        recyclerView = (RecyclerView) mRootView.findViewById(R.id.recycler);
        initialNotes();

        return mRootView;
    }

    public void initialNotes(){
        db = Room.databaseBuilder(getContext(), AppDatabase.class, "production")
                .allowMainThreadQueries()
                .build();
        noteList = db.noteDao().getAllNotes();
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerViewAdapter(noteList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        initialNotes();
        super.onResume();
    }

}
