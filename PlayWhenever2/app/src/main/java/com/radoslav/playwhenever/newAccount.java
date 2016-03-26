package com.radoslav.playwhenever;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class newAccount extends AppCompatActivity {

    private ProgressDialog pDialog, dialog;

    private static final String TAG_SUCCESS = "success";
    Boolean emailNotUsed = false;


    private static String url_create_product = "http://radoslavbonev.net/playWhenever/create_user.php";
    private static final String url_get_details = "http://radoslavbonev.net/playWhenever/get_user_pass.php";



    JSONParser jsonParser = new JSONParser();
    EditText inputEmail;
    EditText inputPass;
    EditText inputDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        inputEmail = (EditText) findViewById(R.id.inputName);
        inputPass = (EditText) findViewById(R.id.inputPrice);
        inputDesc = (EditText) findViewById(R.id.inputDesc);

        Button btnCreateProduct = (Button) findViewById(R.id.btnCreateProduct);

        btnCreateProduct.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                inputEmail.setError(null);
                inputPass.setError(null);
                inputDesc.setError(null);
                if ((!inputEmail.getText().toString().equals("")) && (!inputPass.getText().toString().equals("")) && (!inputDesc.getText().toString().equals("")))
                {
                    if(emailNotTooLong())
                    {
                        if(passwordNotTooLong()) {


                            if(nameNotTooLong()) {
                                new checkEmailAvailability().execute();
                            }
                            else
                            {
                                inputDesc.setError(getString(R.string.name_too_long));
                            }


                        }
                        else
                        {
                            inputPass.setError(getString(R.string.password_too_long));
                        }

                    }
                    else
                    {
                        inputEmail.setError(getString(R.string.email_too_long));
                    }
                }
                else
                {
                    if(inputEmail.getText().toString().equals(""))
                    {
                        inputEmail.setError(getString(R.string.field_required));
                    }

                    if(inputPass.getText().toString().equals(""))
                    {
                        inputPass.setError(getString(R.string.field_required));
                    }

                    if(inputDesc.getText().toString().equals(""))
                    {
                        inputDesc.setError(getString(R.string.field_required));
                    }
                }
            }
        });
    }



    private boolean emailNotTooLong()
    {
        String tags = inputEmail.getText().toString();
        int chars = tags.codePointCount(0, tags.length());

        return chars<255 && chars>6;
    }

    private boolean passwordNotTooLong()
    {
        String tags = inputPass.getText().toString();
        int chars = tags.codePointCount(0, tags.length());

        return chars<128 && chars>6;
    }

    private boolean nameNotTooLong()
    {
        String tags = inputDesc.getText().toString();
        int chars = tags.codePointCount(0, tags.length());

        return chars<128 && chars>2;
    }

















    class checkEmailAvailability extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(newAccount.this);
            dialog.setMessage(getString(R.string.check_email_availability));
            dialog.setIndeterminate(false);
            dialog.setCancelable(true);
            dialog.show();

            inputEmail.setError(null);
        }


        protected String doInBackground(String... params) {

            runOnUiThread(new Runnable() {
                public void run() {
                    int success;


                    try {
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("email", inputEmail.getText().toString()));

System.out.print("Email " + inputEmail.getText().toString());
                        JSONObject json = jsonParser.makeHttpRequest(
                                url_get_details, "GET", params);

                        Log.d("Single Product Details", json.toString());

                        success = json.getInt(TAG_SUCCESS);
                        if (success == 1) {

                            emailNotUsed = false;
                            System.out.println("Email not used:   " + emailNotUsed);


                        } else {
                            emailNotUsed = true;
                            System.out.println("Email not used:   " + emailNotUsed);


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            return null;
        }

        protected void onPostExecute(String file_url) {
            dialog.dismiss();
            if(emailNotUsed)
            {
                emailNotUsed = false;

                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                new CreateNewUser().execute();
            }
            else
            {
                inputEmail.setError(getString(R.string.email_used));
                Toast.makeText(getApplicationContext(), getString(R.string.email_used), Toast.LENGTH_LONG).show();
                emailNotUsed = false;

            }
        }
    }
    class CreateNewUser extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(newAccount.this);
            pDialog.setMessage(getString(R.string.creating_user));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }


        protected String doInBackground(String... args) {
            hash hash = null;

            final Random r = new SecureRandom();
            byte[] salt = new byte[32];
            r.nextBytes(salt);
            String encodedSalt = Base64.encodeToString(salt, 32);

            System.out.println("double salt:   " + encodedSalt);

            try {
                hash = new hash((inputPass.getText().toString()) + ")7sk18zETpOOZvjMR#VkSdRs#1l3gqrWbSl6TE@L" + encodedSalt);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            String name = inputEmail.getText().toString();
            String price = hash.getHashedValue();
            String description = inputDesc.getText().toString();

            if(isEmailValid(inputEmail.getText().toString()))
            {

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("name", name));
                params.add(new BasicNameValuePair("password", price));
                params.add(new BasicNameValuePair("description", description));
                params.add(new BasicNameValuePair("salt", encodedSalt));
                System.out.println("double salt:   " + encodedSalt);


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
            }
            else
            {
                Looper.prepare();
                runOnUiThread(new Runnable() {
                    public void run() {
                        inputEmail.setError(getString(R.string.email_not_valid));
                    }
                });
                Toast.makeText(getApplicationContext(), R.string.email_not_valid, Toast.LENGTH_LONG).show();

            }

            return null;
        }


        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
        }

    }

    private boolean isEmailValid(String email) {
        if((email.contains("@")) && (email.contains(".")))
        {
            return true;
        }
        return false;
    }
}


