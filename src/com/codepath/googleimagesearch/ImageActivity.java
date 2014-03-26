package com.codepath.googleimagesearch;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

public class ImageActivity extends ActionBarActivity {

    public static final String IMAGE_URL = "IMAGE_URL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        String url = getIntent().getStringExtra(IMAGE_URL);
        final ImageView iv = (ImageView) findViewById(R.id.ivImageDetail);
        final ProgressBar pb = (ProgressBar) findViewById(R.id.pbLoader);
        final TextView tvError = (TextView) findViewById(R.id.tvError);
        ImageLoader.getInstance().loadImage(url, new ImageLoadingListener() {

            @Override
            public void onLoadingCancelled(String arg0, View arg1) {
                // TODO Auto-generated method stub
                pb.setVisibility(View.GONE);
                tvError.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingComplete(String url, View arg1, Bitmap bitmap) {
                // TODO Auto-generated method stub
                iv.setImageBitmap(bitmap);
                pb.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
                // TODO Auto-generated method stub
                pb.setVisibility(View.GONE);
                tvError.setVisibility(View.VISIBLE);

            }

            @Override
            public void onLoadingStarted(String arg0, View arg1) {
                // TODO Auto-generated method stub

            }

        });
        iv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.image, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.miSettings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
