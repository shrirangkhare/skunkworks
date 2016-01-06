package com.example.shirang.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class AddItem extends Activity implements View.OnClickListener {

    Button   mSave;
    Button   mCancel;
    EditText mName;
    EditText mCarb;
    EditText mFat;
    EditText mProtien;
    EditText mCal;
    EditText mUnit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        mName    = (EditText) findViewById(R.id.editTextAddItemName);
        mCarb    = (EditText) findViewById(R.id.editTextAddItemCarbs);
        mFat     = (EditText) findViewById(R.id.editTextAddItemFat);
        mProtien = (EditText) findViewById(R.id.editTextAddItemProtien);
        mCal     = (EditText) findViewById(R.id.editTextAddItemCalories);
        mUnit    = (EditText) findViewById(R.id.editTextAddRecUnit);

        mSave   = (Button) findViewById(R.id.buttonAddItemSave);
        mCancel = (Button) findViewById(R.id.buttonAddItemCancel);

        mSave.setOnClickListener(this);
        mCancel.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_item, menu);
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

    private void addRecord() {
        FoodInfo f = new FoodInfo();
        f.mProtien  = Float.parseFloat(mProtien.getText().toString());
        f.mFat = Float.parseFloat(mFat.getText().toString());
        f.mCarb = Float.parseFloat(mCarb.getText().toString());
        f.mCals = Float.parseFloat(mCal.getText().toString());
        f.mUnit = mUnit.getText().toString();
        f.mName = mName.getText().toString();

        MyAppDatabaseHandler temp = new MyAppDatabaseHandler(this);
        temp.addRecord(f);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonAddItemSave:
                addRecord();
                Intent intent = new Intent(AddItem.this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.buttonAddItemCancel:
                break;
        }
    }
}
