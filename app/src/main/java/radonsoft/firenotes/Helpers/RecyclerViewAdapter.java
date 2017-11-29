package radonsoft.firenotes.Helpers;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import radonsoft.firenotes.Models.Note;
import radonsoft.firenotes.R;

/**
 * Created by RRCFo on 29.11.2017.
 */

class RecyclerViewHolder extends RecyclerView.ViewHolder{
    public TextView title;
    public TextView text;

    public RecyclerViewHolder(View itemView) {
        super(itemView);
        title = (TextView)itemView.findViewById(R.id.titlee);
        text = (TextView)itemView.findViewById(R.id.textt);
    }
}

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder>{

    private List<Note> noteList = new ArrayList<Note>();

    public RecyclerViewAdapter(List<Note> noteList) {
        this.noteList = noteList;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item, parent, false);
        return new RecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        holder.title.setText(noteList.get(position).getTitle());
        holder.text.setText(noteList.get(position).getText());
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }
}
