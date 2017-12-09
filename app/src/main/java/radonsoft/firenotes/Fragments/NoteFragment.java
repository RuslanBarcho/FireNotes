package radonsoft.firenotes.Fragments;

import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.ActionMode;
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
import radonsoft.firenotes.MainActivity;
import radonsoft.firenotes.Models.Note;
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
                if (mActionMode != null & !toDelete.contains(position)){
                    toDelete.add(position);
                } else {
                    toDelete.remove(position);
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

    private ActionMode.Callback mActionCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            Toast.makeText(getContext(), "Ты долго жмакнул на заметку " + NoteFragment.position, Toast.LENGTH_SHORT).show();
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
                    break;

                default:

                    break;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {
            mActionMode = null;
        }
    };

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

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
