package com.example.shirang.myapplication;

import android.app.Application;
import android.content.res.Configuration;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * Created by shirang on 23/12/15.
 */
public class FInfoApp extends Application {
    private ArrayAdapter arrayAdapter = null;
    public float mCarbTotal;
    public float mFatTotal;
    public float mProtienTotal;
    public ArrayList<FoodInfo> mListData = null;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }


    ArrayAdapter getFoodListAdapter() {
        return arrayAdapter;
    }

    void setFoodListAdapter(ArrayAdapter pList) {
        arrayAdapter = pList;
    }
}
