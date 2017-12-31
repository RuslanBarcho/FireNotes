package radonsoft.firenotes.Fragments;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import radonsoft.firenotes.AppDatabase;
import radonsoft.firenotes.Helpers.RecyclerViewAdapter;
import radonsoft.firenotes.Models.Note;
import radonsoft.firenotes.NoteActivity;
import radonsoft.firenotes.R;


public class NoteFragment extends Fragment {
    View mRootView;

    RecyclerView recyclerView;
    RecyclerViewAdapter adapter;

    AppDatabase db;
    Toolbar toolbar;

    public static int position;
    private ActionMode mActionMode;

    public static List<Note> noteList = new ArrayList<Note>();
    private ArrayList<Integer> toDelete = new ArrayList<Integer>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_note, container, false);
        toolbar = (Toolbar) mRootView.findViewById(R.id.main_toolbar);
        getActivity().setTitle("Notes");
        setHasOptionsMenu(true);
        recyclerView = (RecyclerView) mRootView.findViewById(R.id.recycler);
        initialNotes();
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

        // Set listeners on items by POSITION
        adapter.setItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                NoteFragment.position = noteList.size() - position;
                Toast.makeText(getContext(), "Ты жмакнул на заметку " + position, Toast.LENGTH_SHORT).show();
                if (mActionMode == null){
                    changeActivity(position);
                }
                if (mActionMode != null & !toDelete.contains(position)){
                    toDelete.add(position);
                } else {
                    if(toDelete.size()!= 0){
                        toDelete.remove(toDelete.indexOf(position));
                        if (toDelete.size() == 0){
                            mActionMode.finish();
                        }
                    }
                }
            }
        });
        adapter.setItemLongClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                NoteFragment.position = position;
                if (mActionMode == null){
                    mActionMode = getActivity().startActionMode(mActionCallback);
                    toDelete.add(position);
                }
            }
        });
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

    private ActionMode.Callback mActionCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            Toast.makeText(getContext(), "Ты долго жмакнул на заметку " + position, Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getContext(), "Нажата кнопка удалить ", Toast.LENGTH_SHORT).show();
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
        initialNotes();
        super.onResume();
    }

    @Override
    public void onPause() {
        if (mActionMode != null){
            mActionMode.finish();
        }
        super.onPause();
    }
}