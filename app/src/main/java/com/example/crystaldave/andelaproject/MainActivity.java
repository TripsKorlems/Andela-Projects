package com.example.crystaldave.andelaproject;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Developer>> {

    private static final int DEVELOPER_LOADER_ID = 1;
    private DeveloperAdapter developerAdapter;
    private ListView developerListView;
    private TextView emptyListView;
    private ProgressBar progressBar;
    private int pageNumber =2;
    private static  String REQUEST_URL =
            "https://api.github.com/search/users?q=+language:java+location:lagos";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //initialize all views and the update UI
        developerAdapter = new DeveloperAdapter(this, new ArrayList<Developer>());
        developerListView = (ListView) findViewById(R.id.developer_list);
        developerListView.setAdapter(developerAdapter);
        emptyListView = (TextView) findViewById(R.id.empty_list_view);
        progressBar = (ProgressBar) findViewById(R.id.loading_indicator);
        updateIfConnected();
    }

    private void updateIfConnected() {

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).

            getSupportLoaderManager().initLoader(DEVELOPER_LOADER_ID, null, this);
            developerListView.setVisibility(View.VISIBLE);
            emptyListView.setVisibility(View.GONE);

        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            progressBar.setVisibility(View.GONE);

            // Update empty state with no connection error message
            developerListView.setVisibility(View.GONE);
            emptyListView.setVisibility(View.VISIBLE);

        }
        developerListView.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                loadNextDataFromApi(pageNumber);
                // or loadNextDataFromApi(totalItemsCount);
                pageNumber++;
                return true; // ONLY if more data is actually being loaded; false otherwise.
            }
        });
        developerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Developer developer = developerAdapter.getItem(position);

                Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                intent.putExtra("sampleObject",  developer);
                startActivity(intent);
            }
        });
    }

    public void loadNextDataFromApi(int offset) {

        REQUEST_URL = "https://api.github.com/search/users?q=+language:java+location:lagos&page="+offset;
        getSupportLoaderManager().restartLoader(DEVELOPER_LOADER_ID, null, this);
        developerAdapter.notifyDataSetChanged();

    }



    @Override
    public Loader<List<Developer>> onCreateLoader(int id, Bundle args) {
        developerAdapter.notifyDataSetChanged();
        return new DeveloperLoader(MainActivity.this, REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Developer>> loader, List<Developer> developers) {
        // Hide loading indicator because the data has been loaded
        progressBar.setVisibility(View.GONE);
        // developerAdapter.clear();
        if (developers != null && !developers.isEmpty()) {
            developerAdapter.addAll(developers);
        }
        developerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<List<Developer>> loader) {
        developerAdapter.clear();
    }
}
