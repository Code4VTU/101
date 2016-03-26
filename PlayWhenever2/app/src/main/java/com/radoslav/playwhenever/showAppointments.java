package com.radoslav.playwhenever;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class showAppointments extends AppCompatActivity {

    private ProgressDialog dialog;
    private static final String url_get_appointments = "http://radoslavbonev.net/playWhenever/get_appointments.php";
    private static final String TAG_SUCCESS = "success";

    JSONParser jParser = new JSONParser();
int id;

    ArrayList<String> names = new ArrayList<>();
    ArrayList<Float> starts = new ArrayList<>();
    ArrayList<Float> ends = new ArrayList<>();
    ArrayList<Float> startsAndEnds = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_appointments);
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 2);
        System.out.print("id here: " + id);
        new loadAppointments().execute();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), addAppointment.class);
                intent.putExtra("startEnds", startsAndEnds);
                startActivity(intent);
            }
        });
    }







    class loadAppointments extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(showAppointments.this);
            dialog.setMessage(getString(R.string.getting_appointments));
            dialog.setIndeterminate(false);
            dialog.setCancelable(false);
            dialog.show();

        }


        protected String doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair("id", String.valueOf(id)));


            JSONObject json = jParser.makeHttpRequest(url_get_appointments, "GET", params);



            try {
                int success = json.getInt(TAG_SUCCESS);


                System.out.println("success:   " + success);

                if (success == 1) {

                    JSONArray products = json.getJSONArray("product");


                    for (int i = 0; i < products.length(); i++) {
                        JSONObject c = products.getJSONObject(i);


                        String name = c.getString("name");
                        names.add(name);

                        System.out.print(names);
                        String start_time = c.getString("start_time");
                        starts.add(Float.parseFloat(start_time));

                        String end_time = c.getString("end_time");
                        ends.add(Float.parseFloat(end_time));

                        startsAndEnds.add(Float.parseFloat(start_time));
                        startsAndEnds.add(Float.parseFloat(end_time));


                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }


        protected void onPostExecute(String file_url) {
            dialog.dismiss();

                    ListView lv = (ListView) findViewById(R.id.listView);
                    lv.setAdapter(new VersionAdapter(showAppointments.this));


        }


    }























    class VersionAdapter extends BaseAdapter {

        private LayoutInflater layoutInflater;

        public VersionAdapter(showAppointments activity) {
            // TODO Auto-generated constructor stub
            layoutInflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return names.size();
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
                listItem = layoutInflater.inflate(R.layout.list_item_appointments, null);
            }

            // Initialize the views in the layout
            ImageView iv = (ImageView) listItem.findViewById(R.id.thumb);
            TextView tvIDs = (TextView) listItem.findViewById(R.id.namesTx);
            TextView tvStarts = (TextView) listItem.findViewById(R.id.startsTx);
            TextView tvEnds = (TextView) listItem.findViewById(R.id.endsTx);
            String letter = "A";

            letter = String.valueOf(names.get(pos).charAt(0)).toUpperCase();

            ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
// generate random color
            int color = generator.getRandomColor();
            TextDrawable drawable = TextDrawable.builder()
                    .buildRoundRect(letter, color, 20);



            //  iv.setBackgroundResource(thumb[pos]);  // **************
            System.out.print("id:  " + names.get(pos));
            tvIDs.setText(names.get(pos));
            tvStarts.setText(String.valueOf(starts.get(pos)));
            tvEnds.setText(String.valueOf(ends.get(pos)));
            iv.setImageDrawable(drawable);

            return listItem;
        }

    }




}
