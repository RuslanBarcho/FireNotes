package radonsoft.firenotes;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import radonsoft.firenotes.Interfaces.NoteDao;
import radonsoft.firenotes.Models.Note;

/**
 * Created by RRCFo on 08.12.2017.
 */
@Database(entities = {Note.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract NoteDao noteDao();
}
