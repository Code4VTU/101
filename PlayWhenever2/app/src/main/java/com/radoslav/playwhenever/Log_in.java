package com.radoslav.playwhenever;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class Log_in extends AppCompatActivity {



    private ProgressDialog pDialog;



    EditText textEmail;
    EditText textPassword;
    Button btnLogIn;
    Button btnReset;
    Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);



      /*  mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);*/


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        btnLogIn = (Button) findViewById(R.id.btnLogIn);

      /*  HardcodedListAdapter adapter = new HardcodedListAdapter(this);
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);*/

        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textEmail.setError(null);
                textPassword.setError(null);

                if (!textEmail.getText().toString().equals("") && !textPassword.getText().toString().equals("")) {

                    if (emailNotTooLong()) {


                        if (passwordNotTooLong()) {

                            new GetProductDetails().execute();
                        } else {
                            textPassword.setError(getString(R.string.password_too_long));
                        }
                    } else {
                        textEmail.setError(getString(R.string.email_too_long));
                    }

                } else {
                    if (textEmail.getText().toString().equals("")) {
                        textEmail.setError(getString(R.string.field_required));
                    }

                    if (textPassword.getText().toString().equals("")) {
                        textPassword.setError(getString(R.string.field_required));
                    }

                    Toast.makeText(getApplicationContext(), R.string.email_pass_not_entered, Toast.LENGTH_LONG).show();
                }
            }
        });

        btnReset = (Button) findViewById(R.id.btnReset);
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textEmail.setText("");
                textPassword.setText("");
            }
        });

        textEmail = (EditText) findViewById(R.id.textEmail);
        textPassword = (EditText) findViewById(R.id.textPassword);


        btnSignUp = (Button) findViewById(R.id.sign_up);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), newAccount.class);
                startActivity(intent);
            }
        });



    }

    private boolean emailNotTooLong()
    {
        String tags = textEmail.getText().toString();
        int chars = tags.codePointCount(0, tags.length());

        return chars<255 && chars>6;
    }

    private boolean passwordNotTooLong()
    {
        String tags = textPassword.getText().toString();
        int chars = tags.codePointCount(0, tags.length());

        return chars<128 && chars>6;
    }







    class getUserDetails extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Log_in.this);
            pDialog.setMessage(getString(R.string.attepmpt_to_log_in));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }


        protected String doInBackground(String... params) {

            runOnUiThread(new Runnable() {
                public void run() {
                    int success;

                    textEmail.setError(null);
                    textPassword.setError(null);

                    try {
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("email", textEmail.getText().toString()));
                        System.out.println("email: " + textEmail.getText().toString());


                        JSONObject json = jsonParser.makeHttpRequest(
                                url_product_details, "GET", params);

                        Log.d("Single Product Details", json.toString());

                        success = json.getInt(TAG_SUCCESS);
                        if (success == 1) {

                            JSONArray productObj = json
                                    .getJSONArray(TAG_PRODUCT);

                            JSONObject product = productObj.getJSONObject(0);


                            hash hash = null;
                            String personalHash = product.getString(TAG_SALT);
                            try {
                                hash = new hash((textPassword.getText().toString()) + ")7sk18zETpOOZvjMR#VkSdRs#1l3gqrWbSl6TE@L" + personalHash);
                            } catch (NoSuchAlgorithmException e) {
                                e.printStackTrace();
                            }
                            String enteredPasswor = hash.getHashedValue();
                            if (enteredPasswor.equals(product.getString(TAG_PRICE))) {

                                Toast.makeText(getApplicationContext(), R.string.you_are_logged_in, Toast.LENGTH_LONG).show();

                                toLogIn = true;
                                userDetails.setLoggedUserID(Integer.parseInt(product.getString(TAG_ID)));

                                name = product.getString("name");

                            } else {
                                textPassword.setError(getString(R.string.wrong_pass));
                                textPassword.requestFocus();

                                Toast.makeText(getApplicationContext(), R.string.wrong_pass, Toast.LENGTH_LONG).show();
                            }

                            System.out.println("pass: " + product.getString(TAG_PRICE));


                        } else {
                            textEmail.setError(getString(R.string.incorrect_email));
                            textEmail.requestFocus();

                            Toast.makeText(getApplicationContext(), R.string.incorrect_email, Toast.LENGTH_LONG).show();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            return null;
        }



        protected void onPostExecute(String file_url) {
            pDialog.dismiss();

            if(toLogIn)
            {
                runOnUiThread(new Runnable() {
                    public void run() {
                        new LoadGroups().execute();
                    }
                });

                toLogIn = false;
            }

        }

    }
}
