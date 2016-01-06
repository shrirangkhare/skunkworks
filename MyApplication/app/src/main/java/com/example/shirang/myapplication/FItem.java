package com.example.shirang.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;


public class FItem extends Activity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    ArrayList<FoodInfo> mBase;
    int currentSelected;
    CheckBox mEditCheckBox;
    EditText mCarbsEdit;
    EditText mFatEdit;
    EditText mProtienEdit;
    EditText mCaloriesEdit;

    Button mUpdateButton;
    Button mSaveButton;
    MyAppDatabaseHandler myAppDatabaseHandler;
    EditText mQty;
    Spinner mSpinner;
    TextView mUnit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitem);

        mUnit = (TextView)findViewById(R.id.textViewUnit);

        mQty = (EditText) findViewById(R.id.editTextQtyVal);

        mCarbsEdit = (EditText) findViewById(R.id.editTextCarbsVal);
        mFatEdit = (EditText) findViewById(R.id.editTextFatVal);
        mProtienEdit = (EditText) findViewById(R.id.editTextProtienVal);

        mCaloriesEdit = (EditText) findViewById(R.id.editTextFItemCaloriesVal);

        mEditCheckBox = (CheckBox) findViewById(R.id.checkBoxEdit);
        mEditCheckBox.setChecked(false);

        mEditCheckBox.setOnClickListener(this);


        mSaveButton = (Button) findViewById(R.id.buttonFItemSave);
        mSaveButton.setOnClickListener(this);

        mUpdateButton = (Button) findViewById(R.id.idbuttonUpdate);
        mUpdateButton.setOnClickListener(this);

        mSpinner = (Spinner) findViewById(R.id.spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.planets_array, android.R.layout.simple_spinner_item);


        myAppDatabaseHandler = new MyAppDatabaseHandler(this);

        mBase = myAppDatabaseHandler.getFoodLabels();

        ArrayList<String> lables = new ArrayList<String>();

        Iterator<FoodInfo> iter =  mBase.iterator();
        while(iter.hasNext()) {
            lables.add(iter.next().mName);
        }

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, lables);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        mSpinner.setOnItemSelectedListener(this);

        mSpinner.setAdapter(dataAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_fitem, menu);
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


        currentSelected = position;

        TextView unit = (TextView) findViewById(R.id.textViewUnit);
        unit.setText(mBase.get(position).mUnit);

        mEditCheckBox.setChecked(false);


        mCarbsEdit.setText(Float.toString(mBase.get(position).mCarb));
        mCarbsEdit.setFocusable(false);


        mFatEdit.setText(Float.toString(mBase.get(position).mFat));
        mFatEdit.setFocusable(false);


        mProtienEdit.setText(Float.toString(mBase.get(position).mProtien));
        mProtienEdit.setFocusable(false);

        mCaloriesEdit.setText(Float.toString(mBase.get(position).mCals));
        mCaloriesEdit.setFocusable(false);

        updateValues();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    FoodInfo getFoodInfoFromName(String Name) {
        Iterator<FoodInfo> iter =  mBase.iterator();
        FoodInfo result = null;
        while(iter.hasNext()) {
            result =  iter.next();
           if(result.mName.equals(Name)) {
                return result;
            }
        }
        return result;
    }

    private void updateValues() {
        String name = (String)mSpinner.getSelectedItem();
        FoodInfo finfo = getFoodInfoFromName(name);
        float mul = Float.parseFloat(mQty.getText().toString());
        mCarbsEdit.setText(Float.toString(finfo.mCarb*mul));
        mFatEdit.setText(Float.toString(finfo.mFat*mul));
        mProtienEdit.setText(Float.toString(finfo.mProtien*mul));
        mCaloriesEdit.setText(Float.toString(finfo.mCals*mul));
    }

    private void enableEditing() {
        mCarbsEdit.setFocusableInTouchMode(true);
        mFatEdit.setFocusableInTouchMode(true);
        mProtienEdit.setFocusableInTouchMode(true);
        mCaloriesEdit.setFocusableInTouchMode(true);
        mUpdateButton.setText("Update");
    }

    private void disableEditing() {
        mCarbsEdit.setFocusableInTouchMode(false);
        mFatEdit.setFocusableInTouchMode(false);
        mProtienEdit.setFocusableInTouchMode(false);
        mCaloriesEdit.setFocusableInTouchMode(false);
        mEditCheckBox.setChecked(false);
        mUpdateButton.setText("Refresh");
        ((FInfoApp)getApplication()).mListData.clear();
    }

    private void updateFromDb() {
        mBase = myAppDatabaseHandler.getFoodLabels();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.checkBoxEdit:
                if(mEditCheckBox.isChecked()) {
                    enableEditing();
                } else {
                    disableEditing();
                }
                break;

            case R.id.idbuttonUpdate:
                //
                if(mEditCheckBox.isChecked()) {
                    myAppDatabaseHandler.updateRecord(mCarbsEdit.getText().toString(), mFatEdit.getText().toString(),
                    mProtienEdit.getText().toString(), mCaloriesEdit.getText().toString(), (String) mSpinner.getSelectedItem(), Float.parseFloat(mQty.getText().toString()));
                    disableEditing();
                    updateFromDb();
                } else {
                    updateValues();
                }
                break;

            case R.id.buttonFItemSave:
                Intent intent = new Intent(FItem.this, MainActivity.class);
                String name = (String)mSpinner.getSelectedItem();
                FoodInfo finfo = getFoodInfoFromName(name);
                //intent.putExtra("Qty", Float.parseFloat(mQty.getText().toString()));
                //intent.putExtra("FoodInfo", finfo);
                float mul  = Float.parseFloat(mQty.getText().toString());

                finfo.mProtien *= mul;
                finfo.mCarb    *= mul;
                finfo.mFat     *= mul;

                String toBeAdded = Utils.truncate(finfo.mName, 5) + "  " + Utils.truncate(Float.toString(finfo.mCarb), 5)
                        + "    " + Utils.truncate(Float.toString(finfo.mFat), 5) + "   " + Utils.truncate(Float.toString(finfo.mProtien), 5)
                        + "  " + Utils.truncate(mQty.getText().toString(), 5) + " " + mUnit.getText();
                FInfoApp app = (FInfoApp)getApplication();
                ArrayAdapter tempAdapter = app.getFoodListAdapter();
                tempAdapter.add(toBeAdded);
                app.mCarbTotal    += finfo.mCarb;
                app.mFatTotal     += finfo.mFat;
                app.mProtienTotal += finfo.mProtien;
                app.mListData.add(finfo);
                startActivity(intent);
                break;
        }
    }
}
