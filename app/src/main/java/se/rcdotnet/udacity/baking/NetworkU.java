package se.rcdotnet.udacity.baking;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Laszlo_HP_Notebook on 2018-03-20.
 * Network utilities to get data from the datasource on the net.
 * This routines runs on worker thread as they are called from a loader after passed Internet availability test.
 */

public final class NetworkU {
    static final String RECEIPEURL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    static String mStatus = "OK"; // indicates thet the response was ok.

    public static String getReservations(){
        // Build Uri for popular endpoint and get data
        URL popularURL;
        Uri returnuri = Uri.parse(RECEIPEURL).buildUpon().appendQueryParameter("apikey","edes").build();
        try {
            popularURL = new URL(returnuri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return "";
        }
        try {
            return getResponse(popularURL);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    private static String getResponse(URL url) throws IOException {
        // Get response from the selected endpoint
        // Inherited from Sunshine project.
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream error = urlConnection.getErrorStream();
            if (error != null) {
                Scanner scanner = new Scanner(error);
                scanner.useDelimiter("\\A");
                boolean hasInput = scanner.hasNext();
                String errorString = null;
                if (hasInput) {
                    errorString = scanner.next();
                }
                scanner.close();
                mStatus = errorString;
                return null;
            } else {
                InputStream in = urlConnection.getInputStream();
                Scanner scanner = new Scanner(in);
                scanner.useDelimiter("\\A");
                Boolean hasInput = scanner.hasNext();
                String response = null;
                if (hasInput) {
                    response = scanner.next();
                }
                scanner.close();
                mStatus="OK";
                return response;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}

