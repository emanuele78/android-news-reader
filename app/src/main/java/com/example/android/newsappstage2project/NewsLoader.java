package com.example.android.newsappstage2project;
import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.example.android.newsappstage2project.data.DataUtils;
import com.example.android.newsappstage2project.data.News;

import java.util.List;
/**
 * Custom loader
 * Created by Emanuele on 18/12/2017.
 */
public class NewsLoader extends AsyncTaskLoader<List<News>> {
    private final String urlString;
    /**
     * Construct a custom loader that gets data from url and parses
     * it to produce an ArrayList of news
     *
     * @param context   the context reference
     * @param urlString the url api
     */
    public NewsLoader(Context context, String urlString) {
        super(context);
        this.urlString = urlString;
    }
    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }
    @Override
    public List<News> loadInBackground() {
        //getting the json response from api url
        String jsonResponse = DataUtils.getJSONResponse(urlString);
        if (jsonResponse == null) {
            //error occurs
            return null;
        } else {
            //parsing json string and returning an ArrayList of News
            return DataUtils.parseJSON(jsonResponse);
        }
    }
}
