package radonsoft.firenotes.Models;

/**
 * Created by RRCFo on 29.11.2017.
 */

public class Note {
    private String title;
    private String text;

    public Note(){

    }

    public Note(String title, String text){
        this.title = title;
        this.text = text;
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
