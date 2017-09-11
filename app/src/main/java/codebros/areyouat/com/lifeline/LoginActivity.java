package codebros.areyouat.com.lifeline;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
    LoginDetails details;
    public Context mContext;

    public HospitalLoginDetails hospitalLoginDetails;
    private JSONObject loginObject;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userEt = (EditText) findViewById(R.id.edit_text_username);
        passEt = (EditText) findViewById(R.id.edit_text_password);
        mContext = this;
    }


    public void openHospitalActivity(View view) {

        FetchLoginDetails fetchlogin = new FetchLoginDetails();
        String user = userEt.getText().toString();
        String pass = passEt.getText().toString();
        fetchlogin.execute(new LoginDetails(user, pass));

//        Handler handler = new Handler();
//
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//                if(!isAuthenticated)
//                Toast.makeText(mContext, "Username or password wrong.", Toast.LENGTH_LONG).show();
//
//            }
//        }, 3000);
    }

    private void openHospitalLoginActivity() {

        Intent intent = new Intent(this, HospitalActivity.class);
        intent.putExtra("HospitalDetails", loginObject.toString());
        startActivity(intent);

    }


    private class FetchLoginDetails extends AsyncTask<LoginDetails, Void, Void> {

        @Override
        protected Void doInBackground(LoginDetails... params) {

            details = params[0];

            try {
                String json = NetworkUtils.getResponseFromHttpUrl(NetworkUtils
                        .buildHttpUrlForLogin(details));

                Log.d("JSONRESPONSE", json);
                JSONObject object = new JSONObject(json);
                Log.d("JSONRESPONSE", "REACHES HERE");
                if (object.has("id")) {
                    loginObject = object;
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
                }

            } catch (JSONException e) {
                isAuthenticated = false;
                cancel(true);
            }
            return null;
        }

    }

    private void refreshDetails() {
        userEt.setText("");
        passEt.setText("");
    }

    public HospitalLoginDetails getHospitalLoginDetails() {
        return hospitalLoginDetails;
    }
}