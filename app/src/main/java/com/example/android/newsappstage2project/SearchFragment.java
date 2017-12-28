package com.example.android.newsappstage2project;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.newsappstage2project.data.DataUtils;
import com.example.android.newsappstage2project.data.News;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by Emanuele on 27/12/2017.
 */
public class SearchFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<News>> {
    private RecyclerView recyclerView;
    private GridLayoutManager layoutManager;
    private RecyclerViewAdapter recyclerViewAdapter;
    private View rootView;
    private View emptyView;
    private ProgressBar loadingBar;
    private Button searchButton;
    private EditText searchText;
    private String userSearch;
    private String resultsToShow;
    private String orderBy;
    @DataUtils.LoaderID
    private int loaderID;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.search_list_layout, container, false);
        //read loader ID from bundle
        loaderID = getArguments().getInt("ID");
        //find reference of the views
        recyclerView = rootView.findViewById(R.id.recycler_view);
        emptyView = rootView.findViewById(R.id.empty_view);
        loadingBar = rootView.findViewById(R.id.loading_spinner);
        searchText = rootView.findViewById(R.id.search_text);
        searchButton = rootView.findViewById(R.id.search_button);
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
        //hide loading effects
        emptyView.setVisibility(View.GONE);
        loadingBar.setVisibility(View.GONE);
        //set search button listener
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check empty text
                userSearch = searchText.getText().toString();
                if (userSearch.trim().isEmpty()) {
                    //no text inserted by the user
                    Toast.makeText(getContext(), R.string.empty_edittext_toast, Toast.LENGTH_SHORT).show();
                    return;
                }
                //check internet connection
                if (!DataUtils.isConnected(getContext())) {
                    //no internet connection
                    emptyView.setVisibility(View.VISIBLE);
                    recyclerViewAdapter.setNewData(null);
                    emptyView.setVisibility(View.GONE);
                    DataUtils.showSnackBarMessage(getString(R.string.no_internet_message), rootView, getContext());
                    return;
                }
                //get preferences for the user search
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                resultsToShow = preferences.getString(
                        getString(R.string.settings_results_per_page_key),
                        getString(R.string.settings_results_per_page_default));
                orderBy = preferences.getString(getString(R.string.settings_order_by_key),
                        getString(R.string.settings_order_by_default));
                LoaderManager loaderManager = getLoaderManager();
                loaderManager.restartLoader(loaderID, null, SearchFragment.this);
            }
        });
        //if loader exists call init loader to load old data, needed on configuration change
        if (getLoaderManager().getLoader(loaderID) != null) {
            //show old data when device configuration changes
            getLoaderManager().initLoader(loaderID, null, this);
        }
        return rootView;
    }
    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {
        //hide loading effects and empty view
        emptyView.setVisibility(View.GONE);
        loadingBar.setVisibility(View.VISIBLE);
        //create uri for the loader
        Uri baseUri = Uri.parse(DataUtils.BASE_URI);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("order-by", orderBy);
        uriBuilder.appendQueryParameter("page-size", resultsToShow);
        uriBuilder.appendQueryParameter("api-key", BuildConfig.GUARDIAN_API_KEY);
        uriBuilder.appendQueryParameter("show-fields", DataUtils.FIELDS);
        uriBuilder.appendQueryParameter("show-tags", DataUtils.TAGS);
        uriBuilder.appendQueryParameter("q", userSearch);
        String url = uriBuilder.toString();
        return new NewsLoader(getActivity(), url);
    }
    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> data) {
        //hide loading effect
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
