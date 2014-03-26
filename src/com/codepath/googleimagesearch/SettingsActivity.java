package com.codepath.googleimagesearch;

import java.io.Serializable;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
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
        public String site;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(SETTINGS_KEY, mSettings);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        if (savedInstanceState == null) {
            mSettings = (Settings) getIntent().getSerializableExtra(SETTINGS_KEY);
        } else {
            mSettings = (Settings) savedInstanceState.getSerializable(SETTINGS_KEY);
        }
        Spinner mSizeSpinner = (Spinner) findViewById(R.id.spSize);
        ArrayAdapter<CharSequence> sizeAdapter = ArrayAdapter.createFromResource(this, R.array.sizes,
                android.R.layout.simple_spinner_item);
        sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSizeSpinner.setAdapter(sizeAdapter);
        Spinner mColorSpinner = (Spinner) findViewById(R.id.spColor);
        ArrayAdapter<CharSequence> colorAdapter = ArrayAdapter.createFromResource(this, R.array.color,
                android.R.layout.simple_spinner_item);
        colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mColorSpinner.setAdapter(colorAdapter);
        Spinner mTypeSpinner = (Spinner) findViewById(R.id.spSize);
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(this, R.array.sizes,
                android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTypeSpinner.setAdapter(typeAdapter);
        mSizeSpinner.setSelection(mSettings.size);
        mColorSpinner.setSelection(mSettings.color);
        mTypeSpinner.setSelection(mSettings.type);

        EditText etSite = (EditText) findViewById(R.id.etSite);
        etSite.setText(mSettings.site);
    }

    public void onSave(View v) {
        mSettings.size = mSizeSpinner.getSelectedItemPosition();
        mSettings.color = mColorSpinner.getSelectedItemPosition();
        mSettings.type = mTypeSpinner.getSelectedItemPosition();
        EditText etSite = (EditText) findViewById(R.id.etSite);

        mSettings.site = etSite.getText().toString();
        setResult(RESULT_OK);
        Intent resultData = new Intent();
        resultData.putExtra(SETTINGS_KEY, mSettings);
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
