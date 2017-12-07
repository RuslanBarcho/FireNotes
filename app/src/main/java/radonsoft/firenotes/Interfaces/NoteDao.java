package radonsoft.firenotes.Interfaces;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import radonsoft.firenotes.Models.Note;

/**
 * Created by RRCFo on 08.12.2017.
 */
@Dao
public interface NoteDao {
    @Query("SELECT * FROM note")
    List<Note> getAllNotes();

    @Insert
    void insertAll(Note... notes);

    @Delete
    void delete(Note note);
}
