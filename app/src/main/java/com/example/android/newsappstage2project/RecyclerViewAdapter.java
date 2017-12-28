package com.example.android.newsappstage2project;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.android.newsappstage2project.data.DataUtils;
import com.example.android.newsappstage2project.data.News;

import java.text.DateFormat;
import java.util.List;
import java.util.Locale;
/**
 * Created by Emanuele on 21/12/2017.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {
    private List<News> newsList;
    private final Context context;
    /**
     * Create new Recycler View Adapter
     *
     * @param context   context reference
     * @param itemsList list of news object
     */
    public RecyclerViewAdapter(Context context, List<News> itemsList) {
        this.context = context;
        this.newsList = itemsList;
    }
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate the layout for the single news
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, parent,
                false);
        //setting listener for user click using command pattern
        RecyclerViewHolder.OnEntryClickListener onEntryClickListener =
                new RecyclerViewHolder.OnEntryClickListener() {
                    @Override
                    public void onEntryClick(View view, int position) {
                        //get news object from the list
                        News news = newsList.get(position);
                        if (news.getWebUrl() == null || news.getWebUrl().isEmpty()) {
                            //the news doesn't hold a link to open with the browser
                            Toast.makeText(context, context.getString(R.string.unable_to_open_news),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            if (!DataUtils.isConnected(context)) {
                                //the device is not connected
                                DataUtils.showSnackBarMessage(
                                        context.getString(R.string.no_internet_message),
                                        view.findViewById(R.id.card_view), context);
                            } else {
                                //open the link in the browser
                                Uri webPage = Uri.parse(news.getWebUrl());
                                Intent intent = new Intent(Intent.ACTION_VIEW, webPage);
                                if (intent.resolveActivity(context.getPackageManager()) != null) {
                                    context.startActivity(intent);
                                } else {
                                    Toast.makeText(context,
                                            context.getString(R.string.unable_to_open_news),
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                };
        //create and return recycle view holder
        RecyclerViewHolder recycleViewHolder = new RecyclerViewHolder(view, onEntryClickListener);
        return recycleViewHolder;
    }
    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        //setting values
        News news = newsList.get(position);
        if (news.getImage() == null) {
            holder.imageView.setImageResource(R.drawable.no_image);
        } else {
            holder.imageView.setImageBitmap(news.getImage());
        }
        holder.imageView.setContentDescription(news.getTitle());
        holder.titleView.setText(news.getTitle());
        holder.sectionView.setText(news.getSection());
        holder.authorView.setText(news.getAuthor());
        DateFormat df = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault());
        String formattedDate = df.format(news.getDate());
        holder.dateView.setText(formattedDate);
    }
    @Override
    public int getItemCount() {
        if (newsList != null) {
            return newsList.size();
        } else {
            return 0;
        }
    }
    /**
     * Set new data into the adapter
     *
     * @param itemsList a list of news object
     */
    public void setNewData(List<News> itemsList) {
        this.newsList = itemsList;
        notifyDataSetChanged();
    }
}
