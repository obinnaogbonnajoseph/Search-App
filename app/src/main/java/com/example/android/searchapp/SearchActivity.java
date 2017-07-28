package com.example.android.searchapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;

public class SearchActivity extends Activity {

    public static final String EXTRA_BOOK = "com.example.searchapp.BOOK";

    public static final String EXTRA_AUTHOR = "com.example.searchapp.AUTHOR";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_design);
    }

    public void submitSearch(View view) {
        // Find the Edit Text Views
        EditText bookTitleView = (EditText) findViewById(R.id.search_book);
        EditText authorNameView = (EditText) findViewById(R.id.search_author);

        Editable bookTitle = bookTitleView.getText();
        Editable authorName = authorNameView.getText();

        String titleQuery = bookTitle.toString();
        String authorQuery = authorName.toString();

        // Use an intent to send the query to the appropriate activity.
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(EXTRA_BOOK,titleQuery);
        intent.putExtra(EXTRA_AUTHOR,authorQuery);

        // Start the activity
        startActivity(intent);
    }
}
