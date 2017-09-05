package codebros.areyouat.com.lifeline;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class LoginActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }


    public void openHospitalActivity(View view) {
        Intent intent = new Intent(this, HospitalActivity.class);
        startActivity(intent);
    }
}
