package com.example.crystaldave.andelaproject;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
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
import java.util.List;


/**
 * Created by CRYSTALDAVE on 9/15/2017.
 */

public class DeveloperLoader extends AsyncTaskLoader<List<Developer>> {
    private static final String LOG_TAG = "errors";
    private String REQUEST_URL;

    public DeveloperLoader(Context context, String url) {
        super(context);
        REQUEST_URL = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Developer> loadInBackground() {
        URL url = createUrl(REQUEST_URL);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = "";
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to the server", e);
        }
        // Extract relevant fields from the JSON response and create an {@link Developer} object
        return extractFeatureFromJson(jsonResponse);
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private URL createUrl(String stringUrl) {
        URL url = null;
        try {
            Log.d(LOG_TAG, "FetchWeatherTask Complete.  Inserted");
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            Log.e(LOG_TAG, "Error with creating URL", exception);
            return null;
        }
        return url;
    }

    private String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        //if url is null, then return early
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();
            //if the request was successful (response code 200);
            //then read the input stream and parse the response
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);

            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem receiving JSON results", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // function must handle java.io.IOException here
                inputStream.close();
            }
        }
        return jsonResponse;
    }
    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
    /**
     * Return an {@link Developer} object by parsing out information
     * about the first news from the input developersJSON string.
     */
    private List<Developer> extractFeatureFromJson(String developersJSON) {

        //if json string is empty or null return early
        if (TextUtils.isEmpty(developersJSON)) {
            return null;
        }
        List developersList = new ArrayList();
        try {
            JSONObject baseJsonResponse = new JSONObject(developersJSON);

            JSONArray developersArray = baseJsonResponse.getJSONArray("items");

            // If there are results in the features array
            for (int i = 0; i < developersArray.length(); i++) {
                // Extract out the first feature
                JSONObject firstObjectFeature = developersArray.getJSONObject(i);

                // Extract out the  userName, profileImage   and profileUrl of Developer
                String userName = firstObjectFeature.getString("login");
                String profileImage = firstObjectFeature.getString("avatar_url");
                String profileUrl = firstObjectFeature.getString("html_url");

                Log.d(LOG_TAG, "FetchWeatherTask Complete. " + userName + " Inserted");
                // Create a new {@link News} object
                developersList.add(new Developer(userName, profileImage, profileUrl));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the news JSON results", e);
        }
        return developersList;
    }

}
