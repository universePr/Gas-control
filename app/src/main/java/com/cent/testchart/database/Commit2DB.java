package com.cent.testchart.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cent.testchart.data.Data;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

import static com.cent.testchart.constants.Constants.desc_order;
import static com.cent.testchart.constants.Constants.limit_like_data;
import static com.cent.testchart.constants.Constants.limit_on_data;

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

    public ArrayList<com.cent.testchart.data.Data> getLatest7Days(String tag){
        String query_find_tag = Helper.LATEST_RECORD + " '" + tag + "' "+ desc_order + limit_on_data;
        database = dbHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery(query_find_tag, null);
        ArrayList<String> times = new ArrayList<>();
        String pref = " ";
        // Find the date associated with each tag
        while (cursor.moveToNext()){
            String time = cursor.getString(1);
            String[] d = time.split(" ");
            time = d[0] + " " + d[1] + " " + d[2] + "%" + d[5];
            if(!time.equals(pref)){
                pref = time;
                times.add(pref);
            }
        }
        database = dbHelper.getReadableDatabase();
        String query_find_max_in_day = "";
        ArrayList<Data> list_of_max_days = new ArrayList<>();
        for (int i = times.size() - 1; i >= 0 ; i--) {
            query_find_max_in_day = Helper.LATEST_RECORD_FILTER + " '" + tag + "' " + limit_like_data + " '" + times.get(i) + "';";
            cursor = database.rawQuery(query_find_max_in_day, null);
            while (cursor.moveToNext()) {
                list_of_max_days.add(new Data(cursor.getInt(2), cursor.getString(1), tag));
            }
        }
        return list_of_max_days;
    }
}
