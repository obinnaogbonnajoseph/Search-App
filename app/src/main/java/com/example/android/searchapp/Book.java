package com.example.android.searchapp;

import java.util.ArrayList;

/**
 * Creates a special class called Book.
 */

public class Book {

    private String mTitle, mUrl, mImageUrl;
    private ArrayList<String> mAuthor;
    //private int mImageResourceID;

    public Book(String title, ArrayList<String> author, String url, String imageUrl){
        mTitle = title;
        mAuthor = author;
        mUrl = url;
        mImageUrl = imageUrl;
    }

    public String getmTitle() {return mTitle;}

    public ArrayList<String> getmAuthor() {return mAuthor;}

    public String getmUrl() {return mUrl;}

    public String getmImageUrl() {return mImageUrl;}
}
