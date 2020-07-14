package com.example.finalproject.soccer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.finalproject.R;

import java.util.ArrayList;

public class SoccerFavoriteActivity extends AppCompatActivity {

    private ArrayList<SoccerDetail> soccerFavoriteList = new ArrayList<>();
    private SoccerFavoriteAdapter myAdapter = new SoccerFavoriteAdapter();
    private ProgressBar pbFSoccer;
    private ListView favoriteList;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soccer_favorite);

        favoriteList = findViewById(R.id.lvFSoccer);
        loadSoccerFromDatabase();
        favoriteList.setAdapter(myAdapter);

    }

    private class SoccerFavoriteAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return soccerFavoriteList.size();
        }

        @Override
        public SoccerDetail getItem(int position) {
            return soccerFavoriteList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return getItem(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //get a layout for this row at position
            LayoutInflater inflater = getLayoutInflater();
            View newView = inflater.inflate(R.layout.soccer_favorite_layout, parent,false);
            TextView txtTitle = newView.findViewById(R.id.soccerFTitle);
            txtTitle.setText(getItem(position).getTitle());
            TextView txtDate = newView.findViewById(R.id.soccerFDate);
            txtDate.setText(getItem(position).getDate());
            TextView txtComp = newView.findViewById(R.id.soccerFCompetition);
            txtComp.setText((getItem(position).getCompetition()));
            CheckBox cbFavorite = newView.findViewById(R.id.cbSoccerFavorite);
            cbFavorite.setChecked(true);
            return newView;
        }
    }

    private void loadSoccerFromDatabase() {
        //get a database connection:
        SoccerOpener dbOpener = new SoccerOpener(this);
        db = dbOpener.getWritableDatabase(); //This calls onCreate() if you've never built the table before, or onUpgrade if the version here is newer

        // We want to get all of the columns. Look at MyOpener.java for the definitions:
        String [] columns = {SoccerOpener.SOCCER_COL_ID, SoccerOpener.SOCCER_COL_TITLE, SoccerOpener.SOCCER_COL_TEAM1,
                             SoccerOpener.SOCCER_COL_TEAM2,SoccerOpener.SOCCER_COL_VIDEOURL, SoccerOpener.SOCCER_COL_DATE,
                             SoccerOpener.SOCCER_COL_COMPETITION, SoccerOpener.SOCCER_COL_THUMBNAILURL };
        //query all the results from the database:
        Cursor results = db.query(false, SoccerOpener.TABLE_NAME, columns, null, null, null, null, null, null);
        //Cursor results2 = db.rawQuery("Select * from ?", new String[]{MyOpener.TABLE_NAME});

        //Now the results object has rows of results that match the query.
        //find the column indices:
        int idColIndex = results.getColumnIndex(SoccerOpener.SOCCER_COL_ID);
        int titleColIndex = results.getColumnIndex(SoccerOpener.SOCCER_COL_TITLE);
        int team1ColIndex = results.getColumnIndex(SoccerOpener.SOCCER_COL_TEAM1);
        int team2ColIndex = results.getColumnIndex(SoccerOpener.SOCCER_COL_TEAM2);
        int videoColIndex = results.getColumnIndex(SoccerOpener.SOCCER_COL_VIDEOURL);
        int dateColIndex = results.getColumnIndex(SoccerOpener.SOCCER_COL_DATE);
        int compColIndex = results.getColumnIndex(SoccerOpener.SOCCER_COL_COMPETITION);
        int imageColIndex = results.getColumnIndex(SoccerOpener.SOCCER_COL_THUMBNAILURL);

        //iterate over the results, return true if there is a next item:
        while(results.moveToNext())
        {
            long id = results.getLong(idColIndex);
            String title = results.getString(titleColIndex);
            String team1 = results.getString(team1ColIndex);
            String team2 = results.getString(team2ColIndex);
            String videoUrl = results.getString(videoColIndex);
            String date = results.getString(dateColIndex);
            String competition = results.getString(compColIndex);
            String imageUrl = results.getString(imageColIndex);

            //add the new Contact to the array list:
            soccerFavoriteList.add(new SoccerDetail(id, title, team1, team2, videoUrl, date, competition, imageUrl));
        }//At this point, the contactsList array has loaded every row from the cursor.
    }

    private void insertSoccerToDatabase(String title, String team1, String team2, String videoUrl, String date, String competition, String thumbnailUrl) {

        //add to the database and get the new ID
        ContentValues newRowValues = new ContentValues();

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

        //add the new contact to the list:
        soccerFavoriteList.add(new SoccerDetail(newId, title, team1, team2, videoUrl, date, competition, thumbnailUrl));
        //update the listView:
        myAdapter.notifyDataSetChanged();
    }

    private void deleteSoccerFromDatabase(long id) {
        db.delete(SoccerOpener.TABLE_NAME, SoccerOpener.SOCCER_COL_ID + "= ?", new String[] {Long.toString(id)});
    }
}