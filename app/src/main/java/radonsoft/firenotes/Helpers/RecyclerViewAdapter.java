package radonsoft.firenotes.Helpers;

import android.content.res.ColorStateList;
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
        holder.background.setBackgroundColor(getColor(noteList.get(position).color));

        if (noteList.get(position).getTitle().equals("")){
            holder.title.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemClickListener != null) itemClickListener.onClick(holder.getAdapterPosition());
                if (isSelectMode){
                    if (selectedItems.contains(position)){
                        holder.background.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
                        selectedItems.remove(selectedItems.indexOf(position));
                    } else{
                        holder.background.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#BDBDBD")));
                        selectedItems.add(position);
                    }
                }
                // NoteFragment.position = getAdapterPosition();
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (!isSelectMode){
                    isSelectMode = true;
                    holder.background.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#BDBDBD")));
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

    private int getColor(int color_id){
        switch (color_id){
            case (R.id.rb_red): return Color.parseColor("#F77272");
            case (R.id.rb_green): return Color.parseColor("#A8FA9B");
            case (R.id.rb_blue): return Color.parseColor("#7C8FF7");
            default: return Color.parseColor("#ffffff");
        }
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
            title = itemView.findViewById(R.id.titlee);
            text = itemView.findViewById(R.id.textt);
            background = itemView.findViewById(R.id.item_background);
        }

    }

    public interface OnItemClickListener{
        void onClick(int position);
    }

}