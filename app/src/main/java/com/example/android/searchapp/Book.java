package com.example.android.searchapp;

/**
 * Creates a special class called Book.
 */

class Book {

    private String mTitle, mUrl, mImageUrl;

    Book(String title, String url, String imageUrl){
        mTitle = title;
        mUrl = url;
        mImageUrl = imageUrl;
    }

    String getmTitle() {return mTitle;}

    String getmUrl() {return mUrl;}

    String getmImageUrl() {return mImageUrl;}
}
