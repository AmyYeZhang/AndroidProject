package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get fields from the screen
        ImageButton geo_data = findViewById(R.id.geo_data);
        ImageButton soccer_match = findViewById(R.id.soccer_match);
        ImageButton lyrics_search = findViewById(R.id.lyrics_search);
        ImageButton deezer_song = findViewById(R.id.deezer_song);

        // Create intent for each page
        Intent go_to_geo = new Intent(MainActivity.this, GeoDataActivity.class);
        Intent go_to_soccer = new Intent(MainActivity.this, SoccerMathActivity.class);
        Intent go_to_lyrics = new Intent(MainActivity.this, LyricsSearchActivity.class);
        Intent go_to_deezer = new Intent(MainActivity.this, DeezerSongActivity.class);

        // Click image and go to the correct page
        geo_data.setOnClickListener(bt -> startActivity(go_to_geo));
        soccer_match.setOnClickListener(bt -> startActivity(go_to_soccer));
        lyrics_search.setOnClickListener(bt -> startActivity(go_to_lyrics));
        deezer_song.setOnClickListener(bt -> startActivity(go_to_deezer));
    }
}