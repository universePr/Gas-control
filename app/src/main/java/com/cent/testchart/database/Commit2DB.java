package com.cent.testchart.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cent.testchart.data.Data;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

public class Commit2DB {
    private SQLiteDatabase database;
    private Helper dbHelper;
    private String[] allColumns = {

            Helper.COLUMN_ID,
            Helper.COLUMN_TIME,
            Helper.COLUMN_COUNT ,
            Helper.COLUMN_TAG   };



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
        cv.put(allColumns[3], data.getTag());
        database.insert(Helper.TABLE_STATISTICS, null, cv);
        dbHelper.close();
    }

    public ArrayList<com.cent.testchart.data.Data> getLatest30Days(){
        database = dbHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery(Helper.LATEST_RECORD, null);
        ArrayList<Data> list_of_30_days = new ArrayList<>();
        while (cursor.moveToNext()){
            String time = cursor.getString(1);
            int count = cursor.getInt(2);
            String tag = cursor.getString(3);
            Data data = new Data(count, time, tag);
            list_of_30_days.add(data);
        }

        return list_of_30_days;
    }
}
