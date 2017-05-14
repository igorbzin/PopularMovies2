package org.bozin.igor.popularmovies;

import android.content.res.Resources;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import static android.content.ContentValues.TAG;

/**
 * Created by igorb on 08.05.2017.
 */

public class NetworkUtils {

    static String baseURL = MainActivity.resources.getString(R.string.base_url);
    static String jos_nepoznato = MainActivity.resources.getString(R.string.jos_nepoznato);
    static String API_Key_param = MainActivity.resources.getString(R.string.url_api_key);
    static String API_Key = MainActivity.resources.getString(R.string.api_key);
    static String sortByCategory = MainActivity.resources.getString(R.string.sort_by_category);
    static String imageBaseURL = MainActivity.resources.getString(R.string.image_base_url);



    public static URL buildURL(String sortByCriterium) {
        baseURL  = MainActivity.resources.getString(R.string.base_url);
        Uri builtUri = Uri.parse(baseURL).buildUpon()
                .appendPath(jos_nepoznato)
                .appendPath(sortByCategory)
                .appendPath(sortByCriterium)
                .appendQueryParameter(API_Key_param,API_Key)
                .build();

        URL url = null;

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI:  " + url);

        return url;
    }


    public static URL buildImageURL(String imageLink) {

        Uri builtUri = Uri.parse(imageBaseURL).buildUpon()
                .appendPath("t")
                .appendPath("p")
                .appendPath(MainActivity.resources.getString(R.string.image_url_width))
                .appendEncodedPath(imageLink)
                .build();

        URL url = null;

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v("IMAGE URL", "Built URI:  " + url);

        return url;
    }


    public static String getResponseFromHttpURL(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasResponse = scanner.hasNext();
            if (hasResponse) {
                return scanner.next();
            } else {
                return null;
            }

        } finally {
            urlConnection.disconnect();
        }
    }


}
