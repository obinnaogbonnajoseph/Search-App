package com.example.android.searchapp;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements LoaderManager.LoaderCallbacks<List<Book>> {

    /** Tag  for log messages **/
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    /**
     * Constant value for the earthquake loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int EARTHQUAKE_LOADER_ID = 1;

    /** Adapter for the list of books**/
    private BookAdapter adapter;

    /** TextView that is displayed when the list is empty */
    private TextView mEmptyStateTextView;

    private String bookUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // the ListView in the layout
        ListView bookListView = (ListView) findViewById(R.id.list);

        // Find the emptyView in the layout and setEmptyView to be it's ID
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        bookListView.setEmptyView(mEmptyStateTextView);

        // Create a new ArrayAdapter of books
        adapter = new BookAdapter(this, new ArrayList<Book>());

        // Set the adapter on the ListView so the list can be
        // populated in the user interface
        bookListView.setAdapter(adapter);

        // Set an item click listener on the ListView, which sends an intent to a web
        // to open a website with more information about the selected book.
        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Find the current earthquake that was clicked on
                Book currentBook = adapter.getItem(position);

                // Convert the String URL into a URI object
                assert currentBook != null;
                Uri bookUri = Uri.parse(currentBook.getmUrl());

                // Create a new intent to view the earthquake URI
                Intent intent = new Intent(Intent.ACTION_VIEW, bookUri);

                // Send the intent to launch a new activity
                startActivity(intent);
            }
        });

        Intent intent = getIntent();
        String bookTitle = intent.getStringExtra(SearchActivity.EXTRA_BOOK);
        String authorName = intent.getStringExtra(SearchActivity.EXTRA_AUTHOR);

        String title = changeQuery(bookTitle);
        String author = changeQuery(authorName);
        bookUrl = "https://www.googleapis.com/books/v1/volumes?q="+title+"+inauthor:"+author;

        // Get a reference to the ConnectivityManager to check the state of
        // network connectivity.
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(
                Context.CONNECTIVITY_SERVICE);
        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        // If there is a network connection, fetch data
        if(networkInfo != null && networkInfo.isConnected()){
            // Get a reference to the LoaderManager, in order to interact with loaders
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the ID as 1, null for the bundle and
            // this activity for the LoaderCallbacks parameter which is valid because
            // this activity implements the LoaderCallback's interface
            loaderManager.initLoader(EARTHQUAKE_LOADER_ID,null,this);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }
    }

    @Override
    public Loader<List<Book>> onCreateLoader(int id, Bundle args) {
        return new BookLoader(this, bookUrl);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> data) {
        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        // Set empty state text to display "No books found."
        mEmptyStateTextView.setText(R.string.no_books);

        // Clear the adapter of previous earthquake data
        adapter.clear();

        // If there is a valid list of Book(s), then add them to the
        // data set. This will trigger the ListView to update.
        if (data != null && !data.isEmpty()){
            adapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        // Loader reset, so we can clear out our existing data.
        adapter.clear();
    }

    private String changeQuery(String query){
        String finalString = "";
        if (query.contains(" ")){
            String[] parts = query.split(" ");
            for (String part : parts) {
                finalString = finalString + part + "%20";
            }

        } else return query;
        return finalString;
    }

}
