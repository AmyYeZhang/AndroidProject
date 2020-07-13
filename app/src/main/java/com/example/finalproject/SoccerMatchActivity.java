package com.example.finalproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class SoccerMatchActivity extends AppCompatActivity {

    private ArrayList<String> soccerTitleList = new ArrayList<>();
    private ProgressBar pbSoccer;
    private SoccerTitleAdapter myAdapter = new SoccerTitleAdapter();
    private ListView titleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soccer_match);

        Button btnMyFavorite = findViewById(R.id.btnFList);
        btnMyFavorite.setOnClickListener(v -> {
            Intent goToFavorite = new Intent(SoccerMatchActivity.this, SoccerFavoriteActivity.class);
            startActivity(goToFavorite);
        });

        pbSoccer = findViewById(R.id.pbSoccer);
        pbSoccer.setVisibility(View.VISIBLE);

        titleList = findViewById(R.id.lvSoccer);
        //titleList.setAdapter(myAdapter);

        SoccerMatchQuery query = new SoccerMatchQuery();
        query.execute("https://www.scorebat.com/video-api/v1/");

        titleList.setOnItemClickListener((parent, view, position, id) -> {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
            alertBuilder.setTitle("Do you want to see details of this match: ")
                    .setMessage(myAdapter.getItem(position))
                    .setPositiveButton("GO", (click, arg)->{
                        Intent goToDetail = new Intent(SoccerMatchActivity.this, SoccerDetailActivity.class);
                        startActivity(goToDetail);
                    })
                    .setNegativeButton("No", (click, agr)->{})
                    //.setView(getLayoutInflater().inflate(R.layout.send_layout, null))
                    .create().show();

        });

    }

    private class SoccerMatchQuery extends AsyncTask<String, Integer, String> {

        private String title, competition, date;
        private Bitmap image = null;

        @Override
        protected String doInBackground(String... args) {
            try {

                //get the soccer math information
                //create a URL object of what server to contact:
                URL url = new URL(args[0]);
                //open the connection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                publishProgress(25);
                //wait for data:
                InputStream response = urlConnection.getInputStream();
                publishProgress(50);

                //JSON reading
                //Build the entire string response:
                BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);
                publishProgress(75);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }

                String result = sb.toString(); //result is the whole string
                // convert string to JSON Array
                JSONArray soccerArray = new JSONArray(result);
                for(int i=0; i<soccerArray.length(); i++){
                    JSONObject soccer = soccerArray.getJSONObject(i);
                    soccerTitleList.add(soccer.getString("title"));
                }
                publishProgress(100);

            }
            catch (Exception e)
            {

            }
            return "Done";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            pbSoccer.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String fromDoInBackground) {
            //set the ListView
            titleList.setAdapter(myAdapter);

            //set the ProgressBar
            pbSoccer.setVisibility(View.INVISIBLE);
        }
    }

    private class SoccerTitleAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return soccerTitleList.size();
        }

        @Override
        public String getItem(int position) {
            return soccerTitleList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return (long)position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //get a layout for this row at position
            LayoutInflater inflater = getLayoutInflater();
            View newView = inflater.inflate(R.layout.soccer_match_layout, parent,false);
            TextView txtTitle = newView.findViewById(R.id.soccerTitle);
            txtTitle.setText(getItem(position));
            return newView;
        }
    }
}