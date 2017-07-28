package com.example.android.searchapp;


import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;



/**
 * Helper methods related to requesting and receiving data from Google Books
 */

class QueryUtils {
    /**
     * Tag for the log messaages
     */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Query the Google Books API and return a list of Book(s)
     * @param requestUrl is the URL string
     * @return a list of Book(s)
     */
    static ArrayList<Book> fetchBookData(String requestUrl) {

        Log.i(LOG_TAG,"TEST: fetchBookData() called ...");
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch(IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request", e);
        }

        // Extract relevant fields from the JSON response and  return the earthquakes array
        Log.i(LOG_TAG,"Extracting features from jsonResponse...");
        return extractFeatureFromJson(jsonResponse);
    }

    /**
     * Returns new URL object from the given string URL.
     * @param stringUrl is the input URL
     * @return the URL
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try{
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response
     * @param url is the input to the method
     * @return a string which is the URL
     * @throws IOException is expected, so it needs to be caught.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        // If the URL is null, then return early.
        if(url==null){
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000/*milliseconds*/);
            urlConnection.setConnectTimeout(15000/*milliseconds*/);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode()==200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG,"Error response code: "+urlConnection.getResponseCode());
            }
        } catch (IOException e){
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results",e);
        } finally {
            if (urlConnection != null){
                urlConnection.disconnect();
            }
            if (inputStream != null){
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the InputStream into a String which contains the
     * whole JSON respnse from the server.
     * @param inputStream is the Input stream to be converted
     * @return a String which is the JSON response
     * @throws IOException is expected, so we catch it.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream,
                    Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while(line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();

    }


    /**
     * Return a list of Book objects that has been built up from
     * parsing  JSON response.
     */

    private static ArrayList<Book> extractFeatureFromJson(String bookJson){
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(bookJson)){
            return null;
        }
        // Create an empty ArrayList that we can stard adding books to
        ArrayList<Book> books = new ArrayList<>();

        try {
            JSONObject baseObj = new JSONObject(bookJson);
            JSONArray items = baseObj.getJSONArray("items");

            for (int i = 0; i < items.length(); i++){
                JSONObject currentBook = items.getJSONObject(i);
                JSONObject volumeInfo = currentBook.getJSONObject("volumeInfo");

                String title = volumeInfo.getString("title");
                Log.i(LOG_TAG,"title of book: "+title);

                String url = volumeInfo.getString("infoLink");
                Log.i(LOG_TAG,"url of book: "+url);

                JSONObject getImageLinks = volumeInfo.getJSONObject("imageLinks");
                String imageUrl = getImageLinks.getString("thumbnail");

                Book book = new Book(title,url,imageUrl);
                books.add(book);
            }
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements
            // in the try block, catch the exception here, so the app does not
            // cras. Print a log message
            Log.e("QueryUtils", "Problem parsing the bookJSON result",e);
        }
        return books;
    }
}
