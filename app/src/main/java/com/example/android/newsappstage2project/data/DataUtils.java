package com.example.android.newsappstage2project.data;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.IntDef;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.example.android.newsappstage2project.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
/**
 * Util class that performs connection to urls, formatting data and parsing json string
 * Created by Emanuele on 18/12/2017.
 */
public class DataUtils {
    //loader id constants
    public static final boolean SHOW_SECTION_DEFAULT = false;
    @IntDef({LoaderID.FRONT_PAGE_LOADER_ID,
            LoaderID.BUSINESS_LOADER_ID,
            LoaderID.TECH_LOADER_ID,
            LoaderID.WORLD_LOADER_ID,
            LoaderID.LIFESTYLE_LOADER_ID,
            LoaderID.CULTURE_LOADER_ID,
            LoaderID.TRAVEL_LOADER_ID,
            LoaderID.SEARCH_LOADER_ID})
    @Retention(RetentionPolicy.SOURCE)
    public @interface LoaderID {
        int FRONT_PAGE_LOADER_ID = 0;
        int BUSINESS_LOADER_ID = 1;
        int TECH_LOADER_ID = 2;
        int WORLD_LOADER_ID = 3;
        int LIFESTYLE_LOADER_ID = 4;
        int CULTURE_LOADER_ID = 5;
        int TRAVEL_LOADER_ID = 6;
        int SEARCH_LOADER_ID = 7;
    }
    //column width for grid layout manager
    public static final int COLUMN_WIDTH = 600;
    //vars for uri builder
    public static final String BASE_URI = "http://content.guardianapis.com/search";
    public static final String ORDER_BY = "newest";
    public static final String FIELDS = "thumbnail";
    public static final String TAGS = "contributor";
    //vars that holds number of news per tab and number of news per row
    public static String rowSize;
    public static String pageSize;
    //constants for the connection
    private static final int READ_TIMEOUT = 10000;
    private static final int CONNECT_TIMEOUT = 15000;
    public static final String GUARDIAN_API_KEY = "test";
    /**
     * Returns the json string response from the server at the specified url api
     *
     * @param stringUrl the url api
     * @return a json string response. It returns null if an error occurs
     */
    public static String getJSONResponse(String stringUrl) {
        StringBuilder output = new StringBuilder();
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader;
        try {
            URL url = new URL(stringUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(READ_TIMEOUT);
            urlConnection.setConnectTimeout(CONNECT_TIMEOUT);
            urlConnection.connect();
            int responseCode = urlConnection.getResponseCode();
            if (responseCode != 200) {
                throw new IOException();
            }
            inputStream = urlConnection.getInputStream();
            output = new StringBuilder();
            inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    return null;
                }
            }
            if (output.length() == 0) {
                return null;
            } else {
                return output.toString();
            }
        }
    }
    /**
     * Returns a bitmap object downloaded at the specified url
     *
     * @param stringUrl the string url where the image is located
     * @return a bitmap object. It returns null if an error occurs
     */
    private static Bitmap getBitmap(String stringUrl) {
        Bitmap image = null;
        try {
            URL url = new URL(stringUrl);
            image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (IOException e) {
        } finally {
            return image;
        }
    }
    /**
     * Converts a string date in the forms of "yyyy-MM-dd'T'HH:mm:ss" in a date object
     *
     * @param stringDate the string date to convert
     * @return a date object. It returns null if an error occurs
     */
    private static Date getFormatDate(String stringDate) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date date = null;
        try {
            date = dateFormat.parse(stringDate);
        } catch (ParseException e) {
        } finally {
            return date;
        }
    }
    /**
     * Parses the json response string and returns an arraylist of news
     *
     * @param json the json string to parse
     * @return an arraylist whose type is news. It returns null if an error occurs
     */
    public static ArrayList<News> parseJSON(String json) {
        final int FIRST_ELEMENT = 0;
        ArrayList<News> newsList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONObject("response").getJSONArray("results");
            for (int cont = 0; cont < jsonArray.length(); cont++) {
                String section = jsonArray.getJSONObject(cont).optString("sectionName");
                String title = jsonArray.getJSONObject(cont).optString("webTitle");
                Date date = getFormatDate(jsonArray.getJSONObject(cont).optString("webPublicationDate"));
                String webUrl = jsonArray.getJSONObject(cont).optString("webUrl");
                //check if news image exist and eventually download it
                Bitmap bitmap = null;
                if (jsonArray.getJSONObject(cont).has("fields")) {
                    String imageUrl = jsonArray.getJSONObject(cont).getJSONObject("fields").optString("thumbnail");
                    if (!imageUrl.isEmpty()) {
                        bitmap = getBitmap(imageUrl);
                    }
                }
                //check if contributor exists
                JSONArray tagsArray = jsonArray.getJSONObject(cont).getJSONArray("tags");
                String contributor = "";
                if (tagsArray.length() > 0) {
                    contributor = tagsArray.getJSONObject(FIRST_ELEMENT).optString("webTitle");
                }
                //create news object and add it to the list
                News news = new News(title, section, contributor, date, bitmap, webUrl);
                newsList.add(news);
            }
        } catch (JSONException e) {
            newsList = null;
        } finally {
            return newsList;
        }
    }
    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
    public static void showSnackBarMessage(String message, View rootView, Context context) {
        Snackbar snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_LONG);
        View view = snackbar.getView();
        int color = ContextCompat.getColor(context, R.color.colorAccent);
        TextView textView = view.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();
    }
}