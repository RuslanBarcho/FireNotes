package radonsoft.firenotes.Fragments;

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

import radonsoft.firenotes.Helpers.RecyclerViewAdapter;
import radonsoft.firenotes.Models.Note;
import radonsoft.firenotes.R;


public class NoteFragment extends Fragment {
    View mRootView;
    RecyclerView recyclerView;
    RecyclerViewAdapter adapter;
    RecyclerView.LayoutManager layoutManager;

    String title;
    String text;

    Bundle bundle;

    List<Note> noteList = new ArrayList<Note>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_note, container, false);
        recyclerView = (RecyclerView) mRootView.findViewById(R.id.recycler);

        bundle = getArguments();
        if (bundle != null){
            title = getArguments().getString("title");
            text = getArguments().getString("text");
        }

        initialNotes();
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerViewAdapter(noteList);
        recyclerView.setAdapter(adapter);

        return mRootView;
    }

    public void initialNotes(){
        if (bundle != null){
            noteList.add(new Note(title, text));
        }
        noteList.add(new Note("Test title one", getString(R.string.loremipsumone)));
        noteList.add(new Note("Test title two", getString(R.string.loremipsumtwo)));
        noteList.add(new Note("Test title three", getString(R.string.loremipsumthree)));
    }

}
