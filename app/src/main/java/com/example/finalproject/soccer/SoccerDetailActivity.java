package com.example.finalproject.soccer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.MediaController;
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
    private SQLiteDatabase db;
    MediaController mediaController;
    int position = 0;

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
                insertSoccerToDatabase(title, team1, team2, videoUrl, date, competition, thumbnailUrl);
            }else{
                //remove from database
            }
            Snackbar.make(cbDFavorite, b?R.string.soccer_detail_snackbar_add:R.string.soccer_detail_snackbar_delete, Snackbar.LENGTH_LONG).show();
        });

        Button btnHighlight = findViewById(R.id.btnSoccerDHighlight);
        btnHighlight.setOnClickListener(v -> {

            //show video
            VideoView player = findViewById(R.id.soccerVideoView);
            player.setVisibility(View.VISIBLE);

            mediaController = new MediaController(this);
            mediaController.setAnchorView(player);
            mediaController.setMediaPlayer(player);

            player.setMediaController(mediaController);
            player.setVideoURI(Uri.parse(videoUrl));
            player.setOnPreparedListener( video ->{
                player.seekTo(position);
                if(position == 0)
                    player.start();
            });
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

    private void insertSoccerToDatabase(String title, String team1, String team2, String videoUrl, String date, String competition, String thumbnailUrl) {
        //get a database connection:
        SoccerOpener dbOpener = new SoccerOpener(this);
        db = dbOpener.getWritableDatabase(); //This calls onCreate() if you've never built the table before, or onUpgrade if the version here is newer

        //add to the database and get the new ID
        ContentValues newRowValues = new ContentValues();
        //how to check the duplication???

        //Now provide a value for every database column defined in MyOpener.java:
        newRowValues.put(SoccerOpener.SOCCER_COL_TITLE, title);
        newRowValues.put(SoccerOpener.SOCCER_COL_TEAM1, team1);
        newRowValues.put(SoccerOpener.SOCCER_COL_TEAM2, team2);
        newRowValues.put(SoccerOpener.SOCCER_COL_VIDEOURL, videoUrl);
        newRowValues.put(SoccerOpener.SOCCER_COL_DATE, date);
        newRowValues.put(SoccerOpener.SOCCER_COL_COMPETITION, competition);
        newRowValues.put(SoccerOpener.SOCCER_COL_THUMBNAILURL, thumbnailUrl);

        //Now insert in the database:
        long newId = db.insert(SoccerOpener.TABLE_NAME, null, newRowValues);
    }

    private void deleteSoccerFromDatabase(long id) {
        db.delete(SoccerOpener.TABLE_NAME, SoccerOpener.SOCCER_COL_ID + "= ?", new String[] {Long.toString(id)});
    }
}