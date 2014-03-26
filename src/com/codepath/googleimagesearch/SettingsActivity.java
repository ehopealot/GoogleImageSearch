package com.codepath.googleimagesearch;

import java.io.Serializable;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class SettingsActivity extends ActionBarActivity {

    private Settings mSettings;

    public static String SETTINGS_KEY = "SETTINGS_KEY";

    private Spinner mSizeSpinner;
    private Spinner mColorSpinner;
    private Spinner mTypeSpinner;

    public static class Settings implements Serializable {
        public int size;
        public int color;
        public int type;
        public String site = "";
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(SETTINGS_KEY, mSettings);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mSizeSpinner = (Spinner) findViewById(R.id.spSize);
        ArrayAdapter<CharSequence> sizeAdapter = ArrayAdapter.createFromResource(this, R.array.sizes,
                android.R.layout.simple_spinner_item);
        sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSizeSpinner.setAdapter(sizeAdapter);
        mColorSpinner = (Spinner) findViewById(R.id.spColor);
        ArrayAdapter<CharSequence> colorAdapter = ArrayAdapter.createFromResource(this, R.array.color,
                android.R.layout.simple_spinner_item);
        colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mColorSpinner.setAdapter(colorAdapter);
        mTypeSpinner = (Spinner) findViewById(R.id.spType);
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(this, R.array.type,
                android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTypeSpinner.setAdapter(typeAdapter);

        if (savedInstanceState == null) {
            mSettings = (Settings) getIntent().getSerializableExtra(SETTINGS_KEY);
        } else {
            mSettings = (Settings) savedInstanceState.getSerializable(SETTINGS_KEY);
        }

        mSizeSpinner.setSelection(mSettings.size);
        mColorSpinner.setSelection(mSettings.color);
        mTypeSpinner.setSelection(mSettings.type);

        EditText etSite = (EditText) findViewById(R.id.etSite);
        etSite.setText(mSettings.site);

        Button saveButton = (Button) findViewById(R.id.btnSave);
        saveButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                save();
            }

        });
    }

    private void save() {
        // TODO Auto-generated method stub
        mSettings.size = mSizeSpinner.getSelectedItemPosition();
        mSettings.color = mColorSpinner.getSelectedItemPosition();
        mSettings.type = mTypeSpinner.getSelectedItemPosition();
        EditText etSite = (EditText) findViewById(R.id.etSite);

        mSettings.site = etSite.getText().toString();
        Intent resultData = new Intent();
        resultData.putExtra(SETTINGS_KEY, mSettings);
        setResult(RESULT_OK, resultData);

        finish();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

}
