package com.example.android.searchapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Loads image from URL and updates an imageView
 */

class ImageLoader extends AsyncTask<Void,Void, Bitmap> {

    private String mUrl;
    private ImageView mImageView;
    private static final String LOG_TAG = ImageLoader.class.getSimpleName();


    ImageLoader(String url, ImageView imageView) {
        this.mUrl = url;
        this.mImageView = imageView;
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        try {
            URL urlConnection = new URL(mUrl);
            HttpURLConnection connection = (HttpURLConnection) urlConnection
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            return BitmapFactory.decodeStream(inputStream);
        } catch (Exception e) {
            Log.e(LOG_TAG,"Problem retrieving image online",e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        mImageView.setImageBitmap(bitmap);
    }
}
