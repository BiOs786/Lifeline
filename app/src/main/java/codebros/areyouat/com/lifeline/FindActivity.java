package codebros.areyouat.com.lifeline;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import codebros.areyouat.com.lifeline.sync.Hospital;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static codebros.areyouat.com.lifeline.MapsActivity.hospitals;

public class FindActivity extends AppCompatActivity {


    public static final String FIND_ACTIVITY_TAG = "FIND_ACTIVITY_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);

    }

    public void openMapActivity(View view) {

        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);

    }
}
