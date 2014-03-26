package com.codepath.googleimagesearch;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class SearchActivity extends ActionBarActivity {

    private static class ImageAdapter extends ArrayAdapter<String> {
        public ImageAdapter(Context c, ArrayList<String> images) {
            super(c, R.layout.image_cell, images);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            String url = getItem(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.image_cell, parent, false);
            }

            AsyncImageView aiv = (AsyncImageView) convertView;
            aiv.setUrl(url);
            return aiv;
        }
    }

    private static final int SETTINGS_REQ_CODE = 25;
    private static final String SEARCH_URL = "http://ajax.googleapis.com/ajax/services/search/images";

    public static final AsyncHttpClient CLIENT = new AsyncHttpClient();

    private static final String IMAGES_KEY = "IMAGES_KEY";
    private static final String FULL_SIZE_KEY = "FULL_SIZE_KEY";
    private static final String INDEX_KEY = "INDEX_KEY";
    private static final String LOADING_INDEX_KEY = "LOADING_INDEX_KEY";
    private static final String MAX_ITEM_COUNT_SEEN_KEY = "MAX_ITEM_COUNT_SEEN_KEY";
    private static final String STARTS_KEY = "STARTS_KEY";
    private static final String LABELS_KEY = "LABELS_KEY";
    private static final String QUERY_KEY = "QUERY_KEY";
    private static final String SETTINGS_KEY = "SETTINGS_KEY";

    private ArrayList<String> mImages;
    private ArrayList<String> mFullSizeUrls;
    private ArrayList<String> mStarts;
    private ArrayList<String> mLabels;

    private GridView mGridView;
    private ImageAdapter mAdapter;

    private SettingsActivity.Settings mSettings;

    private String mQuery;
    private int mIndex = 0;
    private int mLoadingIndex = 0;

    private TextView mErrorText;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putStringArrayList(IMAGES_KEY, mImages);
        outState.putInt(INDEX_KEY, mIndex);
        outState.putInt(LOADING_INDEX_KEY, mLoadingIndex);
        outState.putStringArrayList(STARTS_KEY, mStarts);
        outState.putStringArrayList(LABELS_KEY, mLabels);
        outState.putString(QUERY_KEY, mQuery);
        outState.putStringArrayList(FULL_SIZE_KEY, mFullSizeUrls);
        outState.putSerializable(SETTINGS_KEY, mSettings);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayList<String> images;
        setContentView(R.layout.activity_search);
        mErrorText = (TextView) findViewById(R.id.tvError);
        if (savedInstanceState != null) {
            images = savedInstanceState.getStringArrayList(IMAGES_KEY);
            mStarts = savedInstanceState.getStringArrayList(STARTS_KEY);
            mLabels = savedInstanceState.getStringArrayList(LABELS_KEY);
            mIndex = savedInstanceState.getInt(INDEX_KEY);
            mQuery = savedInstanceState.getString(QUERY_KEY);
            mFullSizeUrls = savedInstanceState.getStringArrayList(FULL_SIZE_KEY);
            if (mQuery != null) {
                ((EditText) findViewById(R.id.etSearchField)).setText(mQuery);
            }
            mSettings = (SettingsActivity.Settings) savedInstanceState.getSerializable(SETTINGS_KEY);
        } else {
            DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().cacheInMemory(true)
                    .cacheOnDisc(true).build();
            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).defaultDisplayImageOptions(
                    defaultOptions).build();
            ImageLoader.getInstance().init(config);

            images = new ArrayList<String>();
            mStarts = new ArrayList<String>();
            mLabels = new ArrayList<String>();
            mFullSizeUrls = new ArrayList<String>();
            mSettings = new SettingsActivity.Settings();
        }
        mGridView = (GridView) findViewById(R.id.gvSearchResults);
        mAdapter = new ImageAdapter(this, images);
        mGridView.setAdapter(mAdapter);
        mImages = images;
        final Context c = this;
        mGridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                String fullUrl = mFullSizeUrls.get(position);
                Intent detailIntent = new Intent(c, ImageActivity.class);
                detailIntent.putExtra(ImageActivity.IMAGE_URL, fullUrl);
                startActivity(detailIntent);
            }
        });
        mGridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                // TODO Auto-generated method stub
                if (mLoadingIndex != mIndex && mIndex < 8 && totalItemCount > 0) {
                    int scrolledThroughItems = firstVisibleItem + visibleItemCount;
                    if (scrolledThroughItems == mImages.size()) {
                        if (mIndex < 8) {
                            loadData(mStarts.get(mIndex));
                        }
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.miSettings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            settingsIntent.putExtra(SettingsActivity.SETTINGS_KEY, mSettings);
            startActivityForResult(settingsIntent, SETTINGS_REQ_CODE);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SETTINGS_REQ_CODE && resultCode == RESULT_OK && data != null) {
            SettingsActivity.Settings newSettings = (SettingsActivity.Settings) data
                    .getSerializableExtra(SettingsActivity.SETTINGS_KEY);
            if (newSettings.size != mSettings.size || newSettings.color != mSettings.color
                    || newSettings.type != mSettings.type || !newSettings.site.equalsIgnoreCase(mSettings.site)) {
                // re-trigger the search
                mSettings = newSettings;
                onSearchButtonClicked(null);
            }
        }
    }

    public void loadData(final String start) {
        RequestParams params = new RequestParams();
        params.add("v", "1.0");
        params.add("q", mQuery);
        if (start != null) {
            params.add("start", start);
        }
        if (mSettings.size > 0) {
            String sizeQuery = getResources().getStringArray(R.array.sizes)[mSettings.size];
            params.add("imgsz", sizeQuery);
        }
        if (mSettings.color > 0) {
            String colorQuery = getResources().getStringArray(R.array.color)[mSettings.color];
            params.add("imgcolor", colorQuery);
        }
        if (mSettings.type > 0) {
            String typeQuery = getResources().getStringArray(R.array.type)[mSettings.type];
            params.add("imgtype", typeQuery);
        }
        if (mSettings.site != null && mSettings.site.length() > 0) {
            params.add("as_sitesearch", mSettings.site);
        }
        mLoadingIndex = mIndex;
        mErrorText.setVisibility(View.GONE);

        CLIENT.get(SEARCH_URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONObject results) {
                try {
                    mIndex++;

                    JSONArray images = results.getJSONObject("responseData").getJSONArray("results");
                    JSONArray pages = results.getJSONObject("responseData").getJSONObject("cursor")
                            .getJSONArray("pages");
                    mStarts.clear();
                    mLabels.clear();
                    for (int i = 0; i < pages.length(); i++) {
                        JSONObject obj = pages.getJSONObject(i);
                        mStarts.add(obj.getString("start"));
                        mLabels.add(obj.getString("label"));
                    }

                    for (int i = 0; i < images.length(); i++) {
                        JSONObject obj = images.getJSONObject(i);
                        mImages.add(obj.getString("tbUrl"));
                        mFullSizeUrls.add(obj.getString("url"));
                    }
                    mAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    System.out.print(e);
                    showError();
                    mLoadingIndex -= 1;
                    mIndex -= 1;
                    if (mIndex == 0) {
                        mErrorText.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(java.lang.Throwable e, org.json.JSONObject errorResponse) {
                mLoadingIndex -= 1;
                if (mIndex == 0) {
                    mErrorText.setVisibility(View.VISIBLE);
                }
            }

        });

    }

    public void onSearchButtonClicked(View v) {
        EditText etSearch = (EditText) findViewById(R.id.etSearchField);
        mQuery = Uri.encode(etSearch.getText().toString());
        mImages.clear();
        mFullSizeUrls.clear();
        mIndex = 0;
        loadData(null);
    }

    public void showError() {
        Toast.makeText(this, "Error during search", Toast.LENGTH_SHORT).show();
    }
}
