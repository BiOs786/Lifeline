package codebros.areyouat.com.lifeline;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class HospitalActivity extends AppCompatActivity {

    String hospitalDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital);

        hospitalDetails = getIntent().getStringExtra("HospitalDetails");
    }

    public void loginAsHospital(View view) {



        Intent intent = new Intent(getApplicationContext(), FindActivity.class);
        intent.putExtra("HospitalDetails", hospitalDetails);
        startActivity(intent);

    }

    public void openBarcodeActivity(View view) {

    }
}
