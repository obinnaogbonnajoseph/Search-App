package com.example.android.searchapp;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;

public class MainActivity extends Activity{

    /** Tag  for log messages **/
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private String title, author;

    private final String bookUrl = "https://www.googleapis.com/books/v1/volumes?q=";

    /** Adapter for the list of books**/
    private BookAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // the ListView in the layout
        ListView bookListView = (ListView) findViewById(R.id.list);

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

        title = changeQuery(bookTitle);
        author = changeQuery(authorName);

        // Start the Async task now
        BookAsyncTask task = new BookAsyncTask();
        task.execute(bookUrl+title+"+inauthor:"+author);
        Log.i(LOG_TAG,"URL Information: "+bookUrl+title+"+inauthor:"+author);
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




    private class BookAsyncTask extends AsyncTask<String, Void, ArrayList<Book>>{

        @Override
        protected void onPreExecute() {
            // Clear the adapter of previous books data
            adapter.clear();
        }

        @Override
        protected ArrayList<Book> doInBackground(String... params) {
            // Don't perform the request if there are no URLs, or the first
            // URL is null.
            if(params.length < 1 || params[0] == null) {
                return null;
            }

            // Perform the network request, parse the response and extract a list
            // of books
            return QueryUtils.fetchBookData(params[0]);
        }

        @Override
        protected void onPostExecute(ArrayList<Book> books) {
            // Clear the adapter of previous books data
            adapter.clear();

            // If there is a valid list of Book(s), then add them to the
            // data set. This will trigger the ListView to update
            if(books != null && !books.isEmpty()){
                adapter.addAll(books);
            }
        }
    }
}
