package com.cent.testchart.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cent.testchart.data.Data;

public class Commit2DB {
    private SQLiteDatabase database;
    private Helper dbHelper;
    private String[] allColumns = { Helper.COLUMN_ID,
            Helper.COLUMN_TIME, Helper.COLUMN_COUNT };

    public Commit2DB(Context context) {
        dbHelper = new Helper(context);
    }

    public void close(){
        dbHelper.close();
    }

    public void insertData(Data data){
        database = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(allColumns[1], data.getTime());
        cv.put(allColumns[2], data.getCount());
        database.insert(Helper.TABLE_STATISTICS, null, cv);
        dbHelper.close();
    }
}
