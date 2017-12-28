package com.example.android.newsappstage2project;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.android.newsappstage2project.data.DataUtils;
import com.example.android.newsappstage2project.data.News;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by Emanuele on 27/12/2017.
 */
public class NewsFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<News>> {
    private RecyclerView recyclerView;
    private GridLayoutManager layoutManager;
    private RecyclerViewAdapter recyclerViewAdapter;
    private View rootView;
    private View emptyView;
    private ProgressBar loadingBar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String urlSection;
    @DataUtils.LoaderID
    private int loaderID;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.news_list_layout, container, false);
        //read section parameter and loader ID from bundle
        urlSection = getArguments().getString("section");
        loaderID = getArguments().getInt("ID");
        //find reference of the views
        emptyView = rootView.findViewById(R.id.empty_view);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        swipeRefreshLayout = rootView.findViewById(R.id.swipe_refresh);
        loadingBar = rootView.findViewById(R.id.loading_spinner);
        //create and set adapter to the recycler view
        recyclerViewAdapter = new RecyclerViewAdapter(getActivity(), new ArrayList<News>());
        recyclerView.setAdapter(recyclerViewAdapter);
        //get column counts from preference settings and create appropriate layout manager
        switch (DataUtils.rowSize) {
            case "1":
            case "2":
                layoutManager = new GridLayoutManager(getActivity(), Integer.parseInt(DataUtils.rowSize));
                break;
            default:
                layoutManager = new CustomGridLayoutManager(getContext(), DataUtils.COLUMN_WIDTH);
        }
        //set the layout manager
        recyclerView.setLayoutManager(layoutManager);
        //set refresh listener for the swipe refresh layout
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (DataUtils.isConnected(getContext())) {
                    //refreshing data on connected device
                    emptyView.setVisibility(View.GONE);
                    getLoaderManager().restartLoader(loaderID, null, NewsFragment.this);
                } else {
                    //refreshing data on NOT connected device
                    swipeRefreshLayout.setRefreshing(false);
                    //show snackBar message
                    String message = getString(R.string.no_internet_message);
                    DataUtils.showSnackBarMessage(message, rootView, getContext());
                }
            }
        });
        //hide loading effects
        emptyView.setVisibility(View.GONE);
        loadingBar.setVisibility(View.GONE);
        //start loader if device is connected, otherwise check if the loader exists
        if (DataUtils.isConnected(getContext())) {
            //start new loader
            getLoaderManager().initLoader(loaderID, null, this);
        } else {
            if (getLoaderManager().getLoader(loaderID) != null) {
                //device not connected but loader exists, show old data
                getLoaderManager().initLoader(loaderID, null, this);
            } else {
                //device not connected and loader doesn't exist, show empty view
                emptyView.setVisibility(View.VISIBLE);
            }
        }
        return rootView;
    }
    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {
        if (!swipeRefreshLayout.isRefreshing()) {
            //hide progress bar if swipe refresh layout is refreshing
            loadingBar.setVisibility(View.VISIBLE);
        }
        //create uri for the loader
        Uri baseUri = Uri.parse(DataUtils.BASE_URI);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("order-by", DataUtils.ORDER_BY);
        uriBuilder.appendQueryParameter("page-size", DataUtils.pageSize);
        uriBuilder.appendQueryParameter("api-key", BuildConfig.GUARDIAN_API_KEY);
        uriBuilder.appendQueryParameter("show-fields", DataUtils.FIELDS);
        uriBuilder.appendQueryParameter("show-tags", DataUtils.TAGS);
        if (urlSection != null) {
            //front page section loader doesn't have a section url parameter
            uriBuilder.appendQueryParameter("section", urlSection);
        }
        return new NewsLoader(getContext(), uriBuilder.toString());
    }
    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> data) {
        //hide loading effects
        swipeRefreshLayout.setRefreshing(false);
        loadingBar.setVisibility(View.GONE);
        //set new data into the adapter
        recyclerViewAdapter.setNewData(data);
        if (data.size() == 0) {
            //show empty view if list is empty
            emptyView.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        //clear list in the adapter
        recyclerViewAdapter.setNewData(null);
    }
}
