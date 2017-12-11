package radonsoft.firenotes.Helpers;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import radonsoft.firenotes.Models.Note;
import radonsoft.firenotes.R;

/**
 * Created by RRCFo on 29.11.2017.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder>{

    private ArrayList<Integer> selectedItems = new ArrayList<Integer>();
    private boolean isSelectMode = false;

    private List<Note> noteList = new ArrayList<Note>();

    private OnItemClickListener itemClickListener = null;

    private OnItemClickListener itemLongClickListener = null;

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
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position) {
        holder.title.setText(noteList.get(position).getTitle());
        holder.text.setText(noteList.get(position).getText());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemClickListener != null) itemClickListener.onClick(holder.getAdapterPosition());
                if (isSelectMode){
                    if (selectedItems.contains(position)){
                        holder.background.setBackgroundColor(Color.parseColor("#ffffff"));
                        selectedItems.remove(selectedItems.indexOf(position));
                    } else{
                        holder.background.setBackgroundColor(Color.parseColor("#AED581"));
                        selectedItems.add(position);
                    }
                }
                // NoteFragment.position = getAdapterPosition();
                // Toast.makeText(view.getContext(), "Ты жмакнул на заметку" + NoteFragment.position, Toast.LENGTH_SHORT).show();
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (!isSelectMode){
                    isSelectMode = true;
                    holder.background.setBackgroundColor(Color.parseColor("#AED581"));
                    selectedItems.add(position);
                }
                if (itemLongClickListener != null) itemLongClickListener.onClick(holder.getAdapterPosition());
                return true;
            }
        });
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
                this.itemClickListener = itemClickListener;
    }

    public void setItemLongClickListener(OnItemClickListener itemLongClickListener){
                this.itemLongClickListener = itemLongClickListener;
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public TextView text;
        public LinearLayout background;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.titlee);
            text = (TextView) itemView.findViewById(R.id.textt);
            background = (LinearLayout) itemView.findViewById(R.id.item_background);
        }

    }

    public interface OnItemClickListener{
        void onClick(int position);
    }

}