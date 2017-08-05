package com.example.android.searchapp;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Loader;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;


/**
 * Create a special adapter for Book Class.
 */

class BookAdapter extends ArrayAdapter<Book>{

    BookAdapter(Activity context, ArrayList<Book> books) {
        super(context, 0, books);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        Book currentBook = getItem(position);

        TextView title = (TextView) convertView.findViewById(R.id.title);
        assert currentBook != null;
        title.setText(currentBook.getmTitle());

        ImageView imageView = (ImageView) convertView.findViewById(R.id.image);
        String imageResourceString = currentBook.getmImageUrl();
        ImageLoader imageLoader = new ImageLoader(imageResourceString, imageView);
        imageLoader.execute();

        return convertView;
    }
}
