package com.example.finalproject.soccer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.finalproject.R;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SoccerDetailActivity extends AppCompatActivity {

    private ProgressBar pbDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soccer_detail);

        pbDetail = findViewById(R.id.pbDSoccer);
        pbDetail.setVisibility(View.VISIBLE);

        Intent fromSoccerMatch = getIntent();
        String title = fromSoccerMatch.getStringExtra("sTitle");
        String team1 = fromSoccerMatch.getStringExtra("sTeam1");
        String team2 = fromSoccerMatch.getStringExtra("sTeam2");
        String videoUrl = fromSoccerMatch.getStringExtra("sVideoUrl");
        String date = fromSoccerMatch.getStringExtra("sDate");
        String competition = fromSoccerMatch.getStringExtra("sCompetition");
        String thumbnailUrl = fromSoccerMatch.getStringExtra("sThumbnailUrl");

        TextView txtTitle = findViewById(R.id.soccerDTitle);
        txtTitle.setText(title);
        TextView txtDate = findViewById(R.id.soccerDDate);
        txtDate.setText(date);
        TextView txtComp = findViewById(R.id.soccerDCompetition);
        txtComp.setText(competition);

        ImageQuery query = new ImageQuery();
        //set Toast
        Toast.makeText(this, getString(R.string.soccer_detail_toast), Toast.LENGTH_LONG).show();
        query.execute(thumbnailUrl, videoUrl);

        CheckBox cbDFavorite = findViewById(R.id.cbSoccerDFavorite);
        cbDFavorite.setOnCheckedChangeListener((cb, b) ->
        {
            if(b){
                //save to database
            }else{
                //remove from database
            }
            Snackbar.make(cbDFavorite, b?R.string.soccer_detail_snackbar_add:R.string.soccer_detail_snackbar_delete, Snackbar.LENGTH_LONG).show();
        });

        Button btnHighlight = findViewById(R.id.btnSoccerDHighlight);
        btnHighlight.setOnClickListener(v -> {
            //show video
            VideoView vv = findViewById(R.id.soccerVideoView);
            // set VideoView.VISIBLE = true
        });

    }

    private class ImageQuery extends AsyncTask<String, Integer, String> {

        private Bitmap image = null;

        @Override
        protected String doInBackground(String... args) {
            try {
                //get the thumbnail
                //create a URL object of what server to contact:
                URL url = new URL(args[0]);
                publishProgress(25);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                publishProgress(50);
                urlConnection.connect();
                publishProgress(75);
                int responseCode = urlConnection.getResponseCode();
                if (responseCode == 200) {
                    image = BitmapFactory.decodeStream(urlConnection.getInputStream());
                }
                publishProgress(100);
            }catch (Exception e){

            }
            return "Done";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            //pb.setVisibility(View.VISIBLE);
            pbDetail.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String fromDoInBackground) {
            //set the TextView
            ImageView iv = findViewById(R.id.soccerDImage);
            iv.setImageBitmap(image);

            //set the ProgressBar
            pbDetail.setVisibility(View.INVISIBLE);
        }
    }
}