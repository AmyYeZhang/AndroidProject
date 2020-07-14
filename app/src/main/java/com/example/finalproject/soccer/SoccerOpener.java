package com.example.finalproject.soccer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SoccerOpener extends SQLiteOpenHelper {

    protected final static String DATABASE_NAME = "SoccerDB";
    protected final static int VERSION_NUM = 1;
    public final static String TABLE_NAME = "SoccerTable";
    public final static String SOCCER_COL_ID = "_id";
    public final static String SOCCER_COL_TITLE = "title";
    public final static String SOCCER_COL_TEAM1 = "team1";
    public final static String SOCCER_COL_TEAM2 = "team2";
    public final static String SOCCER_COL_VIDEOURL = "videoUrl";
    public final static String SOCCER_COL_DATE = "date";
    public final static String SOCCER_COL_COMPETITION = "competition";
    public final static String SOCCER_COL_THUMBNAILURL = "thumbnailUrl";

    public SoccerOpener(Context context) {
        super(context, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + SOCCER_COL_TITLE + " text,"
                + SOCCER_COL_TEAM1 + " text,"
                + SOCCER_COL_TEAM2 + " text,"
                + SOCCER_COL_VIDEOURL + " text,"
                + SOCCER_COL_DATE + " text,"
                + SOCCER_COL_COMPETITION + " text,"
                + SOCCER_COL_THUMBNAILURL  + " text);");  // add or remove columns
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Drop the old table:
        db.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create the new table:
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Drop the old table:
        db.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create the new table:
        onCreate(db);
    }
}
