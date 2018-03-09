package radonsoft.firenotes.Fragments;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import radonsoft.firenotes.AppDatabase;
import radonsoft.firenotes.Helpers.RecyclerViewAdapter;
import radonsoft.firenotes.MainActivity;
import radonsoft.firenotes.Models.Note;
import radonsoft.firenotes.NoteActivity;
import radonsoft.firenotes.R;

public class NoteFragment extends Fragment implements MainActivity.FragmentLifecycle {
    View mRootView;
    RelativeLayout noNotesScreen;
    RecyclerView recyclerView;
    RecyclerViewAdapter adapter;
    AppDatabase db;
    Toolbar toolbar;
    ActionMode mActionMode;
    Button createNote;

    public List<Note> noteList = new ArrayList<>();
    private ArrayList<Integer> toDelete = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_note, container, false);
        toolbar = mRootView.findViewById(R.id.main_toolbar);
        noNotesScreen = mRootView.findViewById(R.id.no_notes_screen);
        recyclerView = mRootView.findViewById(R.id.recycler);
        createNote = mRootView.findViewById(R.id.create_note);
        setHasOptionsMenu(true);
        initialNotes();

        createNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), NoteActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        return mRootView;
    }

    public void initialNotes() {
        // Initial and build database
        db = Room.databaseBuilder(getContext(), AppDatabase.class, "production")
                .allowMainThreadQueries()
                .build();

        // Get list of notes from database
        noteList = db.noteDao().getAllNotes();
        Collections.reverse(noteList);

        // Configure recyclerview
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, 1);
        staggeredGridLayoutManager.setAutoMeasureEnabled(true);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        adapter = new RecyclerViewAdapter(noteList);
        recyclerView.setAdapter(adapter);

        //check if any notes are in list
        if (adapter.getItemCount() == 0){
            noNotesScreen.setVisibility(View.VISIBLE);
        } else {
            noNotesScreen.setVisibility(View.GONE);
        }

        // Set listeners on items by POSITION
        adapter.setItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                if (mActionMode == null){
                    changeActivity(position);
                }
                if (mActionMode != null & !toDelete.contains(position)){
                    toDelete.add(position);
                    setActionModeTitle();
                } else {
                    if(toDelete.size()!= 0){
                        toDelete.remove(toDelete.indexOf(position));
                        setActionModeTitle();
                        if (toDelete.size() == 0){
                            mActionMode.setTitle("");
                            mActionMode.finish();
                        }
                    }
                }
            }
        });
        adapter.setItemLongClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                if (mActionMode == null){
                    mActionMode = getActivity().startActionMode(mActionCallback);
                    toDelete.add(position);
                    setActionModeTitle();
                }
            }
        });
    }

    public void setActionModeTitle(){
        mActionMode.setTitle(String.valueOf(toDelete.size()) + " selected");
    }

    public void deleteNotes(){
        db = Room.databaseBuilder(getContext(), AppDatabase.class, "production")
                .allowMainThreadQueries()
                .build();
        for (Integer item: toDelete) {
            db.noteDao().delete(noteList.get(item));
        }
        initialNotes();
        toDelete.clear();
    }

    public void changeActivity(int pos){
        Intent intent = new Intent(getContext(), NoteActivity.class);
        intent.putExtra("editMode", true);
        intent.putExtra("ID", pos);
        startActivityForResult(intent, 1);
    }

    public ActionMode.Callback mActionCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            MenuInflater inflater = getActivity().getMenuInflater();
            inflater.inflate(R.menu.action_mode_notes, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            int id = menuItem.getItemId();
            switch (id){
                case R.id.action_delete:
                    deleteNotes();
                    mActionMode.finish();
                    break;
                default:
                    break;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {
            mActionMode = null;
            toDelete.clear();
            initialNotes();
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        initialNotes();
    }

    @Override
    public void onPauseFragment() {
        Log.i("TEST", "ON PAUSE FRAGMENT");
        if (mActionMode != null) mActionMode.finish();
    }

    @Override
    public void onResumeFragment() {

    }
}