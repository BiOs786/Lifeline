package codebros.areyouat.com.lifeline;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import codebros.areyouat.com.lifeline.sync.NetworkUtils;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText userEt;
    private EditText passEt;
    private Boolean isAuthenticated = false;

    public static HospitalLoginDetails hospitalLoginDetails;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userEt = (EditText) findViewById(R.id.edit_text_username);
        passEt = (EditText) findViewById(R.id.edit_text_password);
    }


    public void openHospitalActivity(View view) {

        String username = userEt.getText().toString();
        String password = passEt.getText().toString();

        if (username.equals("") || password.equals("")) {
            Toast.makeText(this, "Enter credetials", Toast.LENGTH_LONG).show();
            return;
        }

        LoginDetails ld = new LoginDetails(username, password);

        LoginAuthentication authentication = new LoginAuthentication();
        authentication.execute(ld);

    }

    private void openHospitalLoginActivity() {

        Intent intent = new Intent(this, HospitalActivity.class);
        startActivity(intent);

    }

    public class LoginAuthentication extends AsyncTask<LoginDetails, Void, Void> {

        @Override
        protected Void doInBackground(LoginDetails... params) {

            LoginDetails details = params[0];
            Gson gson = new Gson();
            String json = gson.toJson(details);

            URL url = NetworkUtils.buildHttpUrlForLogin(json);

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(NetworkUtils.BASE_URL)
                    .build();
            try {

                Response response = client.newCall(request).execute();
                JSONObject object = new JSONObject(response.body().toString());

                if (object.has("id")) {
                    String id = object.getString("id");
                    String username = object.getString("user_name");
                    String email = object.getString("email");
                    String mobile = object.getString("mobile");
                    String hospital = object.getString("hospital");

                    String regno = object.getString("reg_no");
                    String city = object.getString("city");
                    String password = object.getString("password");

                    String address = object.getString("address");
                    String latitude = object.getString("latitude");
                    String longitude = object.getString("longitude");

                    hospitalLoginDetails = new HospitalLoginDetails(id, username, email, mobile, hospital, regno, city, password, address, latitude, longitude
                    );

                    isAuthenticated = true;

                    openHospitalLoginActivity();

                } else {
                    isAuthenticated = false;
                    showLoginFailedToast();
                    return null;
                }

            } catch (IOException e) {

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    private void showLoginFailedToast() {
        Toast.makeText(this, "Login failed", Toast.LENGTH_LONG).show();
    }
}