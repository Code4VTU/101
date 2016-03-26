package com.radoslav.playwhenever;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.NameValuePair;

import java.util.ArrayList;
import java.util.List;

public class addAppointment extends AppCompatActivity {
    private ProgressDialog pDialog;

    public static EditText PgName;
    public static EditText Lattitude;
    public static EditText Decription;
    public static EditText Longtitude;
    public static EditText Latitude;
    public static Button Location_btn;
    public static Button Create_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_appointment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        float[] times = intent.getFloatArrayExtra("startEnds");

        Latitude = (EditText)findViewById(R.id.Latitude_EditText);
        Longtitude = (EditText)findViewById(R.id.Longtitude_EditText);
        PgName = (EditText)findViewById(R.id.Pgname_EditText);
        Decription = (EditText)findViewById(R.id.Description_EditText);
        Create_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Check availability
                // Add the appointment
            }
        });


    }




    class createNewAppointment extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(addAppointment.this);
            pDialog.setMessage(getString(R.string.creating_user));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }


        protected String doInBackground(String... args) {

                List<NameValuePair> params = new ArrayList<NameValuePair>();
             /*   params.add(new BasicNameValuePair("name", name));
                params.add(new BasicNameValuePair("password", longitude));
                params.add(new BasicNameValuePair("description", description));
                params.add(new BasicNameValuePair("salt", encodedSalt));


                JSONObject json = jsonParser.makeHttpRequest(url_create_product,
                        "POST", params);

                Log.d("Create Response", json.toString());

                try {
                    int success = json.getInt(TAG_SUCCESS);

                    if (success == 1) {


                        Intent intent = new Intent(getApplicationContext(), logIn.class);

                        Looper.prepare();
                        //   Toast.makeText(getApplicationContext(), R.string.choose_your_group, Toast.LENGTH_LONG).show();
                        startActivity(intent);
                        finish();


                    } else {
                        Looper.prepare();
                        Toast.makeText(getApplicationContext(), R.string.account_not_created, Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            */
            return null;
        }


        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
        }

    }

}
