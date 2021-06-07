package com.cent.testchart.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import static com.cent.testchart.constants.Constants.tag_co;

public class Helper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "safe_home.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_STATISTICS = "statistics";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_COUNT = "count";
    public static final String COLUMN_TAG = "tag";
    private static final String DATABASE_CREATE = "create table "
            + TABLE_STATISTICS + "( " + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_TIME
            + " text not null, " + COLUMN_COUNT + " integer not null, "
            + COLUMN_TAG + " text not null );";
    public static  String LATEST_RECORD =
            "SELECT * FROM "+TABLE_STATISTICS+" where "+COLUMN_TAG+" = ";
    public static  String LATEST_RECORD_FILTER =
            "SELECT "+COLUMN_ID+","+COLUMN_TIME+", max("+COLUMN_COUNT+") FROM "+TABLE_STATISTICS+" where "+COLUMN_TAG+" = ";


    public Helper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STATISTICS);
        onCreate(db);
    }
}
