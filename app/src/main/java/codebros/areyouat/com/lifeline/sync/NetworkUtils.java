package codebros.areyouat.com.lifeline.sync;

import android.net.Uri;
import android.util.Log;

import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by BiOs on 05-09-2017.
 */

public final class NetworkUtils {


    public static String getBaseUrl() {
        return BASE_URL;
    }

    public static final String BASE_URL = "http://192.168.1.105/lifeline/response.php?city=Mumbai&quantity=1";
    public static final String LOGIN_URL = "http://192.168.1.105/lifeline/response.php?key=login&&value={\"email\":\"nair@gmail.com\",\"password\":\"12345\"}";
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

    public static String buildHttpUrlForLogin(String email, String pass)
    {

        String json = String.format("{\"email\":\"%s\",\"password\":\"%s\"}", email, pass);

        Uri loginUri = Uri.parse(LOGIN_URL + json)
                .buildUpon().build();

        Log.d("NetworkUtils", loginUri.toString());

        return loginUri.toString();
    }

    public static String getJSON(String url, int timeout) {
        HttpURLConnection c = null;
        try {
            URL u = new URL(url);
            c = (HttpURLConnection) u.openConnection();
            c.setRequestMethod("GET");
            c.setRequestProperty("Content-length", "0");
            c.setUseCaches(false);
            c.setAllowUserInteraction(false);
            c.setConnectTimeout(timeout);
            c.setReadTimeout(timeout);
            c.connect();
            int status = c.getResponseCode();

            switch (status) {
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line+"\n");
                    }
                    br.close();
                    return sb.toString();
            }

        } catch (MalformedURLException ex) {

        } catch (IOException ex) {

        }
        return null;
    }
}
