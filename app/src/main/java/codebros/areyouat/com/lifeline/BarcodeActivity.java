package codebros.areyouat.com.lifeline;/*
 * Copyright (C) The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import codebros.areyouat.com.lifeline.BarcodeCaptureActivity;
import codebros.areyouat.com.lifeline.R;
import codebros.areyouat.com.lifeline.sync.NetworkUtils;

/**
 * Main activity demonstrating how to pass extra parameters to an activity that
 * reads barcodes.
 */
public class BarcodeActivity extends Activity implements View.OnClickListener {

    public static final String STATUSTAG = "STATUSTAG";

    // use a compound button so either checkbox or switch widgets work.
    private CompoundButton autoFocus;
    private CompoundButton useFlash;
    private TextView statusMessage;
    private TextView barcodeValue;

    private static final int RC_BARCODE_CAPTURE = 9001;
    private static final String TAG = "BarcodeMain";

    private String bloodId;
    private String hospitalId;
    public String bloodType;
    public String quantity;
    public String expiry;

    public static HashMap<String, Integer> bloodStrToInt;

    public Context mContext;
    public FetchBloodInsertStatus fetch;
    public int dataFound;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);

        mContext = BarcodeActivity.this;

        bloodStrToInt = new HashMap<>();
        bloodStrToInt.put("A+", 1);
        bloodStrToInt.put("A-", 2);
        bloodStrToInt.put("B+", 3);
        bloodStrToInt.put("B-", 4);
        bloodStrToInt.put("AB+", 5);
        bloodStrToInt.put("AB-", 6);
        bloodStrToInt.put("O+", 7);
        bloodStrToInt.put("O-", 8);


        if(getIntent() != null)
        {
            hospitalId = getIntent().getStringExtra("id");
        }

        statusMessage = (TextView)findViewById(R.id.status_message);
        barcodeValue = (TextView)findViewById(R.id.barcode_value);

        autoFocus = (CompoundButton) findViewById(R.id.auto_focus);
        useFlash = (CompoundButton) findViewById(R.id.use_flash);

        findViewById(R.id.read_barcode).setOnClickListener(this);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.read_barcode) {
            // launch barcode activity.
            Intent intent = new Intent(this, BarcodeCaptureActivity.class);
            intent.putExtra(BarcodeCaptureActivity.AutoFocus, autoFocus.isChecked());
            intent.putExtra(BarcodeCaptureActivity.UseFlash, useFlash.isChecked());

            startActivityForResult(intent, RC_BARCODE_CAPTURE);
        }

    }

    /**
     * Called when an activity you launched exits, giving you the requestCode
     * you started it with, the resultCode it returned, and any additional
     * data from it.  The <var>resultCode</var> will be
     * {@link #RESULT_CANCELED} if the activity explicitly returned that,
     * didn't return any result, or crashed during its operation.
     * <p/>
     * <p>You will receive this call immediately before onResume() when your
     * activity is re-starting.
     * <p/>
     *
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode  The integer result code returned by the child activity
     *                    through its setResult().
     * @param data        An Intent, which can return result data to the caller
     *                    (various data can be attached to Intent "extras").
     * @see #startActivityForResult
     * @see #createPendingResult
     * @see #setResult(int)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_BARCODE_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    statusMessage.setText(R.string.barcode_success);
                    barcodeValue.setText(barcode.displayValue);

                    bloodId = barcode.displayValue;

                    performFetching();

                    Log.d(TAG, "Barcode read: " + barcode.displayValue);
                } else {
                    statusMessage.setText(R.string.barcode_failure);
                    Log.d(TAG, "No barcode captured, intent data is null");
                }
            } else {
                statusMessage.setText(String.format(getString(R.string.barcode_error),
                        CommonStatusCodes.getStatusCodeString(resultCode)));
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void performFetching() {

        BloodDetails bd = new BloodDetails(hospitalId, bloodId);
        FetchBloodData fetchBloodData = new FetchBloodData();
        fetchBloodData.execute(NetworkUtils.buildHttpUrlForBlood(bd));
    }

    private class FetchBloodData extends AsyncTask<String, Void, Void>
    {

        @Override
        protected Void doInBackground(String... params) {

            Log.d("Fetching Blood Data", "Fetching Blood Data");

            String url = params[0];
            String jsonString = NetworkUtils.getResponseFromHttpUrl(url);

            try {
                JSONObject bloodJson = new JSONObject(jsonString);

                Log.d("FetchingBloodData", bloodJson.toString());
                dataFound = ((Long) bloodJson.getLong("data_found")).intValue();
//                String hr_id = (String) bloodJson.get("hr_id");
//                String hr_uid = (String) bloodJson.get("hr_uid");
//                String hr_bloodtype = (String) bloodJson.get("hr_bloodtype");
//                String hr_dateadded = (String) bloodJson.get("hr_dateadded");
//                String h_id = (String) bloodJson.get("h_id");

            } catch (JSONException e) {
                e.printStackTrace();
                cancel(true);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            performPostBloodData();
        }
    }

    private void performPostBloodData() {

        if(dataFound == 0)
        {
            //                  perform insert
            showDialogBox();
            BloodInsert bi = new BloodInsert(hospitalId, bloodType, quantity, expiry, bloodId);
            performInsert(bi);
            Log.d("dataFound", String.valueOf(dataFound));
        }
        else
        {
            Log.d("dataFound", String.valueOf(dataFound));
            showDialogBoxForBloodType();
            //                    perform delete
            BloodDelete bd = new BloodDelete(hospitalId, bloodType, bloodId);
            performDelete(bd);
        }

    }

    public void showDialogBox()
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(mContext, R.style.myDialog));
        final EditText text = new EditText(mContext);

        builder.setTitle("Add blood details").setMessage("Bloodtype").setView(text);
        builder.setPositiveButton("Go", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface di, int i) {
                bloodType = text.getText().toString();
                //do something with it
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface di, int i) {
            }
        });
        builder.create().show();

        //For Quantity

        AlertDialog.Builder qBuilder = new AlertDialog.Builder(new ContextThemeWrapper(mContext, R.style.myDialog));
        final EditText qText = new EditText(mContext);

        qBuilder.setTitle("Add blood details").setMessage("Quantity").setView(qText);
        qBuilder.setPositiveButton("Go", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface di, int i) {
                quantity = qText.getText().toString();
            }
        });
        qBuilder.create().show();

        //For Expiry

        AlertDialog.Builder yBuilder = new AlertDialog.Builder(new ContextThemeWrapper(mContext, R.style.myDialog));
        final EditText yText = new EditText(mContext);

        yBuilder.setTitle("Add blood details").setMessage("Expiry in YYYY-MM-DD format").setView(yText);
        yBuilder.setPositiveButton("Go", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface di, int i) {
                expiry = yText.getText().toString();
                //do something with it
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface di, int i) {
            }
        });
        yBuilder.create().show();
    }

    public void showDialogBoxForBloodType()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(mContext, R.style.myDialog));
        final EditText text = new EditText(mContext);

        builder.setTitle("Add blood details").setMessage("Bloodtype").setView(text);
        builder.setPositiveButton("Go", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface di, int i) {
                bloodType = text.getText().toString();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface di, int i) {
            }
        });
        builder.create().show();
    }

    private void performDelete(BloodDelete blood) {
        FetchBloodDeleteStatus fetch = new FetchBloodDeleteStatus();
        fetch.execute(NetworkUtils.buildHttpUrlForBloodDelete(blood));
    }

    private void performInsert(BloodInsert blood) {
        FetchBloodInsertStatus fetch = new FetchBloodInsertStatus();
        fetch.execute(NetworkUtils.buildHttpUrlForBloodInsert(blood));
    }

    class FetchBloodDeleteStatus extends AsyncTask<String, Void, Void>
    {

        @Override
        protected Void doInBackground(String... params) {

            String url = params[0];

            String jsonReponse = NetworkUtils.getResponseFromHttpUrl(url);

            int status;
            try {
                JSONObject jsonObject = new JSONObject(jsonReponse);
                status = (int) jsonObject.getLong("status");

                if(status == 1)
                {
//                    Snackbar.make(findViewById(R.id.ad_container), "Deleted Successfully", Snackbar.LENGTH_LONG)
//                            .show();
                    Log.d(STATUSTAG, "Deleted successfull");
                    cancel(true);
                }
                else
                {
//                    Snackbar.make(findViewById(R.id.ad_container), "Deletion Unsuccesfull", Snackbar.LENGTH_LONG)
//                            .show();
                    Log.d(STATUSTAG, "Deleted unsuccessfull");
                    cancel(true);
                }
            } catch (JSONException e) {
                e.printStackTrace();
//                    Snackbar.make(findViewById(R.id.ad_container), "Deletion Unsuccesfull", Snackbar.LENGTH_LONG)
//                        .show();
                Log.d(STATUSTAG, "Deleted unsuccessfull");
                cancel(true);
            }

            return null;
        }
    }

    class FetchBloodInsertStatus extends AsyncTask<String, Void, Void>
    {

        @Override
        protected Void doInBackground(String... params) {
            String url = params[0];

            String jsonReponse = NetworkUtils.getResponseFromHttpUrl(url);

            int status;
            try {
                JSONObject jsonObject = new JSONObject(jsonReponse);
                status = (int) jsonObject.get("status");

                if(status == 1)
                {
//                    Snackbar.make(findViewById(R.id.ad_container), "Inserted Successfully", Snackbar.LENGTH_LONG)
//                            .show();
                    Log.d(STATUSTAG, "Inserted successfully");

                    cancel(true);
                }
                else
                {
//                    Snackbar.make(findViewById(R.id.ad_container), "Insertion Unsuccesfull", Snackbar.LENGTH_LONG)
//                            .show();
                    Log.d(STATUSTAG, "Insertion unsuccessful");
                    cancel(true);
                }
            } catch (JSONException e) {
                e.printStackTrace();
//                    Snackbar.make(findViewById(R.id.ad_container), "Insertion Unsuccesfull", Snackbar.LENGTH_LONG)
//                        .show();
                Log.d(STATUSTAG, "Insertion unsuccessful");
            }
            return null;
        }
    }

}
