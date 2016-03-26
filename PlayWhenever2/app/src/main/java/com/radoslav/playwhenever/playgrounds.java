package com.radoslav.playwhenever;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class playgrounds extends AppCompatActivity {
    ListView lv;

    private ProgressDialog pDialog;

    ArrayList<Integer> ids = new ArrayList<>();
    ArrayList<String> names = new ArrayList<>();
    ArrayList<Float> latitudes = new ArrayList<>();
    ArrayList<Float> longitudes = new ArrayList<>();

    JSONParser jsonParser = new JSONParser();
    private static final String url_get_playgrounds = "http://radoslavbonev.net/playWhenever/get_all_playgrounds.php";
    private JSONArray products = null;
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_USERS = "users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playgrounds);
        new getPlaygrounds().execute();

    }
















    class getPlaygrounds extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(playgrounds.this);
            pDialog.setMessage(getString(R.string.get_playgrounds));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }


        protected String doInBackground(String... params) {

            runOnUiThread(new Runnable() {
                public void run() {
                    int success;


                    try {
                        List<NameValuePair> params = new ArrayList<NameValuePair>();

                        JSONObject json = jsonParser.makeHttpRequest(
                                url_get_playgrounds, "GET", params);

                        Log.d("Single Product Details", json.toString());

                        success = json.getInt(TAG_SUCCESS);
                        if (success == 1) {

                            products = json.getJSONArray(TAG_USERS);


                            for (int i = 0; i < products.length(); i++) {

                            JSONObject c = products.getJSONObject(i);

                            int id = Integer.parseInt(c.getString("id"));
                            ids.add(id);
                                System.out.println("DB says:   " + id);


                            String name = c.getString("name");
                            names.add(name);

                                System.out.println("DB says:   " + name);

                                Float latitude = Float.parseFloat(c.getString("latitude"));
                                latitudes.add(latitude);

                                Float longitude = Float.parseFloat(c.getString("longitude"));
                                longitudes.add(longitude);


                        }


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



            lv = (ListView) findViewById(R.id.listView);
            lv.setAdapter(new VersionAdapter(playgrounds.this));
           // lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);


           lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                        long arg3) {
                    // TODO Auto-generated method stub



                    LayoutInflater layoutInflator = getLayoutInflater();

                    View layout = layoutInflator.inflate(R.layout.custom_toast,
                            (ViewGroup) findViewById(R.id.toast_layout_root));


                    // ImageView iv = (ImageView) layout.findViewById(R.id.toast_iv);

                    // iv.setBackgroundResource(thumb[pos]); //****************************
                    int idClicked = ids.get(arg2);


                    Intent intent = new Intent(getApplicationContext(), showAppointments.class);
                    intent.putExtra("id", idClicked);
                    startActivity(intent);

                    System.out.println("currentID:   " + idClicked);

                }
            });




           /* if(emailNotUsed)
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

            }*/
        }




    }














































    class VersionAdapter extends BaseAdapter {

        private LayoutInflater layoutInflater;

        public VersionAdapter(playgrounds activity) {
            // TODO Auto-generated constructor stub
            layoutInflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return ids.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub

            View listItem = convertView;
            int pos = position;
            if (listItem == null) {
                listItem = layoutInflater.inflate(R.layout.list_item, null);
            }

            // Initialize the views in the layout
            ImageView iv = (ImageView) listItem.findViewById(R.id.thumb);
            TextView tvIDs = (TextView) listItem.findViewById(R.id.id_playground);
            TextView tvNames = (TextView) listItem.findViewById(R.id.name_playground);
            TextView tvLat = (TextView) listItem.findViewById(R.id.lat);
            TextView tvLong = (TextView) listItem.findViewById(R.id.longit);
            String letter = "A";

                letter = String.valueOf(names.get(pos).charAt(0)).toUpperCase();

            ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
// generate random color
            int color = generator.getRandomColor();
            TextDrawable drawable = TextDrawable.builder()
                    .buildRoundRect(letter, color, 20);



            //  iv.setBackgroundResource(thumb[pos]);  // **************
            System.out.print("id:  " + ids.get(pos));
            tvNames.setText("" + ids.get(pos));
            tvIDs.setText(names.get(pos));
            tvLat.setText(String.valueOf(latitudes.get(pos)));
           tvLong.setText(String.valueOf(longitudes.get(pos)));
            iv.setImageDrawable(drawable);

            return listItem;
        }

    }

}
