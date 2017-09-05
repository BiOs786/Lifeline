package codebros.areyouat.com.lifeline;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import codebros.areyouat.com.lifeline.sync.Hospital;
import codebros.areyouat.com.lifeline.sync.NetworkUtils;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {


    private GoogleMap mMap;
    public static List<Hospital> hospitals = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        hospitals = new ArrayList<Hospital>();

        FetchHospitalsDetails fetch = new FetchHospitalsDetails();
        fetch.execute();

    }

    public static void addHospital(Hospital hospital)
    {
        hospitals.add(hospital);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    public class FetchHospitalsDetails extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(NetworkUtils.BASE_URL)
                    .build();
            try {

                Response response = client.newCall(request).execute();
                JSONArray array = new JSONArray(response.body().string());

                for (int i = 0; i < array.length(); i++) {

                    JSONObject object = array.getJSONObject(i);
                    String id = object.getString("id");
                    String latitude = object.getString("latitude");
                    String longitude = object.getString("longitude");
                    String address = object.getString("address");
                    String h = object.getString("hospital");

                    Hospital hospital = new Hospital(id, latitude, longitude, address, h);

                    MapsActivity.addHospital(hospital);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d("Hospital", "Size:"+String.valueOf(hospitals.size()));
            for(Hospital hospital: hospitals)
            {
                String latitude = hospital.getLatitude();
                String longitude = hospital.getLongitude();

                LatLng latlng = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                mMap.addMarker(new MarkerOptions().position(latlng).title(hospital.getHospital()));
            }
            LatLng firstLatLng = new LatLng(Double.parseDouble(hospitals.get(0).getLatitude()),
                    Double.parseDouble(hospitals.get(0).getLongitude()));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(firstLatLng));
            CameraUpdate  zoom = CameraUpdateFactory.zoomTo(11);
            mMap.animateCamera(zoom);
        }
    }


}
