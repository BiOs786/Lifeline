package codebros.areyouat.com.lifeline.sync;

import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import codebros.areyouat.com.lifeline.BloodDelete;
import codebros.areyouat.com.lifeline.BloodDetails;
import codebros.areyouat.com.lifeline.BloodInsert;
import codebros.areyouat.com.lifeline.FindDetails;
import codebros.areyouat.com.lifeline.LoginDetails;
import retrofit2.http.DELETE;

/**
 * Created by BiOs on 05-09-2017.
 */

public final class NetworkUtils {


    public static String getBaseUrl() {
        return BASE_URL;
    }

    public static final String BASE_URL = "http://192.168.1.105/lifeline/response.php?key=find&&value=";
    public static final String LOGIN_URL = "http://192.168.1.105/lifeline/response.php?key=login&&value=";
    public static final String FIND_URL = "http://192.168.1.105/lifeline/response.php?key=find&&value=";
    public static final String BLOOD_STATUS_URL= "http://192.168.1.105/lifeline/response.php?key=check&&value=";
    public static final String INSERT_BLOOD_URL = "http://192.168.1.105/lifeline/response.php?key=insert&&value=";
    public static final String DELETE_BLOOD_URL = "http://192.168.1.105/lifeline/response.php?key=delete&&value=}";
    private static final String CITY = "city";
    private static final String QUANTITY = "quantity";

    public static String buildHttpUrlForLogin(LoginDetails loginDetails)
    {

        Gson gson = new Gson();
        String json = gson.toJson(loginDetails);

        Uri loginUri = Uri.parse(LOGIN_URL + json)
                .buildUpon().build();

        Log.d("NetworkUtils", loginUri.toString());

        return loginUri.toString();
    }

    public static String buildHttpUrlForFind(FindDetails find)
    {
        Gson gson = new Gson();
        String json = gson.toJson(find);

        Uri findUri = Uri.parse(FIND_URL + json)
                .buildUpon().build();

        Log.d("NetworkUtils", findUri.toString());

        return findUri.toString();
    }

    public static String buildHttpUrlForBlood(BloodDetails blood)
    {
        Gson gson = new Gson();
        String json = gson.toJson(blood);

        Uri bloodUri = Uri.parse(BLOOD_STATUS_URL + json)
                .buildUpon().build();

        Log.d("NetworkUtils", bloodUri.toString());

        return bloodUri.toString();
    }

    public static String buildHttpUrlForBloodInsert(BloodInsert blood)
    {
        Gson gson = new Gson();
        String json = gson.toJson(blood);

        Uri findUri = Uri.parse(INSERT_BLOOD_URL + json)
                .buildUpon().build();

        Log.d("NetworkUtils", findUri.toString());

        return findUri.toString();
    }

    public static String buildHttpUrlForBloodDelete(BloodDelete blood)
    {
        Gson gson = new Gson();
        String json = gson.toJson(blood);

        Uri findUri = Uri.parse(DELETE_BLOOD_URL + json)
                .buildUpon().build();

        Log.d("NetworkUtils", findUri.toString());

        return findUri.toString();
    }

    public static String getResponseFromHttpUrl(String urlString)
    {
        String urlStr = "";
        try {
            URL url = new URL(urlString);
            URLConnection urlConnection = url.openConnection();
            HttpURLConnection connection = null;
            if (urlConnection instanceof HttpURLConnection) {
                connection = (HttpURLConnection) urlConnection;
            } else {
                System.out.println("Please enter an HTTP URL.");
                return null;
            }
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String current;
            while ((current = in.readLine()) != null) {
                urlStr += current;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return urlStr;
    }
}
