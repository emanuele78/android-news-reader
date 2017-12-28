package com.example.android.newsappstage2project;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * Created by Emanuele on 21/12/2017.
 */
public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public ImageView imageView;
    public TextView titleView;
    public TextView sectionView;
    public TextView dateView;
    public TextView authorView;
    private OnEntryClickListener onEntryClickListener;
    /**
     * Create new Recycler View holder
     *
     * @param view                 the view used for the single news
     * @param onEntryClickListener a reference used to inform the adapter for the user click
     */
    public RecyclerViewHolder(View view, OnEntryClickListener onEntryClickListener) {
        super(view);
        imageView = view.findViewById(R.id.main_image);
        titleView = view.findViewById(R.id.title_view);
        sectionView = view.findViewById(R.id.section_view);
        dateView = view.findViewById(R.id.date_view);
        authorView = view.findViewById(R.id.author_view);
        //set listener in the view group
        view.setOnClickListener(this);
        this.onEntryClickListener = onEntryClickListener;
    }
    @Override
    public void onClick(View view) {
        //inform the adapter for the user click
        if (onEntryClickListener != null) {
            onEntryClickListener.onEntryClick(view, getLayoutPosition());
        }
    }
    /**
     * Interface for the command pattern used to handle the user click
     */
    public interface OnEntryClickListener {
        void onEntryClick(View view, int position);
    }
}
