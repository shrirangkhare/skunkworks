package com.example.shirang.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class MainActivity extends Activity implements View.OnClickListener, LocationListener, AdapterView.OnItemClickListener {

    Button mButtonPlan = null;
    Button mButtonAddRecord = null;
    Button mButtonClear = null;
    ArrayList<String> list = null;

    TextView mCarbTotal;
    TextView mFatTotal;
    TextView mProtienTotal;

    ListView foodListView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        foodListView = (ListView) findViewById(R.id.listViewMainFoodList);

        //  foodListView.setOnLongClickListener();
        foodListView.setOnItemClickListener(this);

        FInfoApp app = (FInfoApp)getApplication();
        ArrayAdapter tempAdapter = app.getFoodListAdapter();
        //foodListView.setAdapter();
        if(tempAdapter==null) {

            //
            // String [] values = {"Name   Carbs   Fat   Protien"};
            ArrayList<String> s = new ArrayList<String>();
            s.add("Name   Carbs     Fat     Protien     Qty");

            tempAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, s);
            app.setFoodListAdapter(tempAdapter);
        }

        if(app.mListData==null) {
            app.mListData = new ArrayList<FoodInfo>();
        }

        foodListView.setAdapter(app.getFoodListAdapter());

        mButtonPlan = (Button) findViewById(R.id.buttonPlan);
        mButtonPlan.setOnClickListener(this);
        mButtonAddRecord = (Button) findViewById(R.id.buttonAddRecord);
        mButtonAddRecord.setOnClickListener(this);

        mButtonClear = (Button) findViewById(R.id.buttonMainClear);
        mButtonClear.setOnClickListener(this);

        float total = app.mCarbTotal + app.mFatTotal + app.mProtienTotal;

        mCarbTotal     = (TextView)findViewById(R.id.textViewMainCarbs);

        String temp = Float.toString((100 / total)*app.mCarbTotal);

        String upToNCharacters = Utils.truncate(temp,5);

        mCarbTotal.setText("Carbs " + Utils.truncate(Float.toString(app.mCarbTotal),5)+ "\n(" + upToNCharacters + "%)" );

        mFatTotal      = (TextView)findViewById(R.id.textViewMainFat);
        temp = Float.toString((100 / total)*app.mFatTotal);
        upToNCharacters = Utils.truncate(temp,5);
        mFatTotal.setText("Fat " + Utils.truncate(Float.toString(app.mFatTotal),5) + "\n(" + upToNCharacters + "%)");

        mProtienTotal  = (TextView)findViewById(R.id.textViewMainProtien);
        temp = Float.toString((100 / total)*app.mProtienTotal);
        upToNCharacters = Utils.truncate(temp,5);
        mProtienTotal.setText("Protien " + Utils.truncate(Float.toString(app.mProtienTotal), 5) + "\n(" + upToNCharacters + "%)" );

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.buttonPlan:
                intent = new Intent(MainActivity.this, FItem.class);
                startActivity(intent);
                break;

            case R.id.buttonAddRecord:
                intent = new Intent(MainActivity.this, AddItem.class);
                startActivity(intent);
                break;

            case R.id.buttonMainClear:
                clearList();
                break;
        }
    }

    void clearList() {
        FInfoApp app = (FInfoApp)getApplication();
        app.mCarbTotal     = (float) 0.0;
        app.mFatTotal      = (float) 0.0;
        app.mProtienTotal  = (float) 0.0;
        ArrayAdapter temp = app.getFoodListAdapter();
        if(temp!=null) {
            temp.clear();
        }
        mCarbTotal.setText("Carbs 0.0");
        mFatTotal.setText("Fat 0.0");
        mProtienTotal.setText("Protien 0.0");
        temp.add("Name     Carbs     Fat     Protien     Qty");
    }

    @Override
    public void onLocationChanged(Location location) {
        double s = location.getLatitude();
        Log.e("SHRIRANG", "onLocationChanged lat=" + Double.toString(s) + " lon=" + Double.toString(location.getLongitude()));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.e("SHRIRANG", "onStatusChanged provider=" + provider + " status=" + Integer.toString(status));
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.e("SHRIRANG", "onProviderEnabled");
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.e("SHRIRANG", "onProviderDisabled");
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        FInfoApp app = (FInfoApp)getApplicationContext();
        Object o = app.getFoodListAdapter().getItem(i);
        app.getFoodListAdapter().remove(o);

        FoodInfo obj = app.mListData.get(i-1);

        app.mCarbTotal    -= obj.mCarb;
        app.mFatTotal     -= obj.mFat;
        app.mProtienTotal -= obj.mProtien;

        app.mCarbTotal = (app.mCarbTotal<0) ? 0 : app.mCarbTotal;
        app.mFatTotal = (app.mFatTotal<0) ? 0 : app.mFatTotal;
        app.mProtienTotal = (app.mProtienTotal<0) ? 0 : app.mProtienTotal;

        float total = app.mCarbTotal + app.mFatTotal + app.mProtienTotal;

        String temp = Float.toString((100 / total)*app.mCarbTotal);
        String upToNCharacters = temp.substring(0, Math.min(temp.length(), 5));

        mCarbTotal.setText("Carbs " + Utils.truncate(Float.toString(app.mCarbTotal),5) + "(" + upToNCharacters + "%)" );

        temp = Float.toString((100 / total)*app.mFatTotal);
        upToNCharacters = temp.substring(0, Math.min(temp.length(), 5));
        mFatTotal.setText("Fat " + Utils.truncate(Float.toString(app.mFatTotal),5) + "(" + upToNCharacters + "%)" );

        temp = Float.toString((100 / total) * app.mProtienTotal);
        upToNCharacters = temp.substring(0, Math.min(temp.length(), 5));
        mProtienTotal.setText("Protien " + Utils.truncate(Float.toString(app.mProtienTotal),5) + "(" + upToNCharacters + "%)" );
    }
}