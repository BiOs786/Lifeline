package codebros.areyouat.com.lifeline;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import codebros.areyouat.com.lifeline.sync.Hospital;
import codebros.areyouat.com.lifeline.sync.NetworkUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static codebros.areyouat.com.lifeline.MapsActivity.hospitals;
import static codebros.areyouat.com.lifeline.R.id.default_activity_button;

public class FindActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    public static final String FIND_ACTIVITY_TAG = "FIND_ACTIVITY_TAG";

    private JSONObject hospitalDetails;
    private JSONObject findDetails;
    private FindDetails details;

    Spinner spinnerGroup;
    Spinner spinnerQuantity;

    String bloodGroup;
    String bloodQuantity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);

        Intent callingIntent = getIntent();
        String jsonExtra = callingIntent.getStringExtra("HospitalDetails");

        try {
            hospitalDetails = new JSONObject(jsonExtra);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //FOR BLOOD GROUPS
        spinnerGroup = (Spinner) findViewById(R.id.spinner_bloodgroup);
        ArrayAdapter<CharSequence> gAdapter = ArrayAdapter.createFromResource(this,
                R.array.blood_groups_array, android.R.layout.simple_spinner_item);
        gAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGroup.setAdapter(gAdapter);

        //FOR QUANTITY
        spinnerQuantity = (Spinner) findViewById(R.id.spinner_quantity);
        ArrayAdapter<CharSequence> qAdapter = ArrayAdapter.createFromResource(this,
                R.array.blood_quantity_array, android.R.layout.simple_spinner_item);
        qAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerQuantity.setAdapter(qAdapter);

        //Adding Click Listeners
        spinnerGroup.setOnItemSelectedListener(this);
        spinnerQuantity.setOnItemSelectedListener(this);
    }

    public void openMapActivity(View view) {

        FindDetails fd=null;

        try {
            String id = hospitalDetails.getString("id");
            String latitude = hospitalDetails.getString("latitude");
            String longitude = hospitalDetails.getString("longitude");
            String bloodtype = getValueOfBloodGroup((String) spinnerGroup.getSelectedItem());
            String city = hospitalDetails.getString("city");
            String quantity = (String) spinnerQuantity.getSelectedItem();
            fd = new FindDetails(id, latitude, longitude, bloodtype, city, quantity);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        String find = gson.toJson(fd);
        Intent intent = new Intent(this, MapsActivity.class);
        Log.d("FindDetailsURL", NetworkUtils.buildHttpUrlForFind(fd));
        intent.putExtra("FindDetailsURL", NetworkUtils.buildHttpUrlForFind(fd));
        startActivity(intent);

    }

    private String getValueOfBloodGroup(String bloodGroup) {

        String bloodtype = null;
        switch(bloodGroup)
        {
            case "A+":
                bloodtype = String.valueOf(1);
                break;
            case "A-":
                bloodtype = String.valueOf(2);
                break;
            case "B+":
                bloodtype = String.valueOf(3);
                break;
            case "B-":
                bloodtype = String.valueOf(4);
                break;
            case "AB+":
                bloodtype = String.valueOf(5);
                break;
            case "AB-":
                bloodtype = String.valueOf(6);
                break;
            case "O+":
                bloodtype = String.valueOf(7);
                break;
            case "O-":
                bloodtype = String.valueOf(8);
                break;
            default:
                break;
        }
        return bloodtype;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        int spinnerId = view.getId();
        if (spinnerId == R.id.spinner_bloodgroup) {
            bloodGroup = (String) parent.getItemAtPosition(position);
        }
        if (spinnerId == R.id.spinner_quantity) {
            bloodQuantity = (String) parent.getItemAtPosition(position);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
