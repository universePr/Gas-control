package com.cent.testchart.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Helper extends SQLiteOpenHelper {

    private Context context;
    public static final String DATABASE_NAME = "safe_home.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_STATISTICS = "statistics";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_COUNT = "count";
    public static final String COLUMN_TAG = "TAG";
    private static final String DATABASE_CREATE = "create table "
            + TABLE_STATISTICS + "( " + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_TIME
            + " text not null, " + COLUMN_COUNT + " integer not null, "
            + COLUMN_TAG + " text not null );";
    private static final int LIMITED = 30;
    public static final String LATEST_RECORD =
            "SELECT * FROM "+TABLE_STATISTICS+" ORDER BY "+COLUMN_ID+" DESC LIMIT "+LIMITED+" ";

    public Helper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
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
