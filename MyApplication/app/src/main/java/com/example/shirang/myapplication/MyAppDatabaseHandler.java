package com.example.shirang.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by shirang on 21/12/15.
 */
public class MyAppDatabaseHandler extends SQLiteOpenHelper {

    MyAppDatabaseHandler(Context context) {
        super(context, "dtest", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.e("SHRIRANG", "onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e("SHRIRANG", "onUpgrade");
    }

    public void updateRecord(String carbs, String fat, String protien, String cal, String name, float mul){
        // first multiply
        float fcarbs = Float.parseFloat(carbs)/mul;
        float ffat = Float.parseFloat(fat)/mul;
        float fprotien = Float.parseFloat(protien)/mul;
        float fcal = Float.parseFloat(cal)/mul;


        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("carbs", fcarbs);
        values.put("fat", ffat);
        values.put("protien", fprotien);
        values.put("cal", fcal);

        // updating row
        int i = db.update("macro_values", values, "name" + " = ?", new String[] { String.valueOf(name) });
        Log.i("SHRIRANG", Integer.toString(i));
    }

    public void addRecord(FoodInfo rec) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("carbs", rec.mCarb);
        values.put("fat", rec.mFat);
        values.put("protien", rec.mProtien);
        values.put("units", rec.mUnit);
        values.put("name", rec.mName);
        db.insert("macro_values", null, values);
    }

    public ArrayList<FoodInfo> getFoodLabels(){
        ArrayList<FoodInfo> l = new ArrayList<FoodInfo>();


        // Select All Query
        String selectQuery = "SELECT  * FROM " + "macro_values;";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                FoodInfo f = new FoodInfo();
                f.mKey     = cursor.getString(0);
                f.mName    = cursor.getString(1);
                f.mUnit    = cursor.getString(3);
                f.mCarb    = cursor.getFloat(4);
                f.mFat     = cursor.getFloat(5);
                f.mProtien = cursor.getFloat(6);
                f.mCals    = cursor.getFloat(7);
                l.add(f);
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();
        return l;
    }
}
