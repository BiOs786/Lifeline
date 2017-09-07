package codebros.areyouat.com.lifeline.sync;

import android.net.Uri;
import android.util.Log;

import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by BiOs on 05-09-2017.
 */

public final class NetworkUtils {


    public static String getBaseUrl() {
        return BASE_URL;
    }

    public static final String BASE_URL = "http://192.168.1.105/lifeline/response.php?city=Mumbai&quantity=1";
    public static final String LOGIN_URL = "http://localhost/lifelinefinal/response.php?key=login&&value=";
    private static final String CITY = "city";
    private static final String QUANTITY = "quantity";

    public static URL buildHttpUrlForResponse()
    {
        Uri responseUri = Uri.parse(BASE_URL)
                .buildUpon()
                .appendQueryParameter(CITY, "Mumbai")
                .appendQueryParameter(QUANTITY, "1")
                .build();

        URL url = null;
        try {
            url = new URL(responseUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildHttpUrlForLogin(String json)
    {
        Uri loginUri = Uri.parse(LOGIN_URL + json)
                .buildUpon().build();

        Log.d("NetworkUtils", loginUri.toString());

        try {
            URL url = new URL(loginUri.toString());
            return url;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }


}
