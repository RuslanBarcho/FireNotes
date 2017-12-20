package radonsoft.firenotes.Models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by RRCFo on 29.11.2017.
 */

@Entity
public class Note {

    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "title")
    public String title;
    @ColumnInfo(name = "text")
    public String text;

    public Note(){

    }

    public Note(String title, String text){
        this.title = title;
        this.text = text;
    }
    public int getId(){
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
