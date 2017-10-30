package com.feet.tanishq;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.feet.tanishq.utils.AsifUtils;
import com.feet.tanishq.utils.AsyncTaskCompleteListener;
import com.feet.tanishq.utils.Const;
import com.feet.tanishq.utils.Singleton_volley;
import com.feet.tanishq.utils.UserDetails;
import com.feet.tanishq.utils.VolleyHttpRequest;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Featured extends AppCompatActivity implements AsyncTaskCompleteListener, Response.ErrorListener {


    ImageView iv_close;
    Button bt_view;
    NetworkImageView nv_feature;
    ProgressBar pg_bar;

    RequestQueue requestQueue;
    ImageLoader imageLoader;

    Tracker tracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null) {
//        if (getIntent() != null) {
            // Do your onclick code.
//            Log.e("ttt", "onCreate:------> " + getIntent().getBooleanExtra("notification_tag", false));
            if (getIntent().getBooleanExtra("notification_tag", false)) {
                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        }


        setContentView(R.layout.activity_featured);
        Singleton_volley analyticsApplication = (Singleton_volley) getApplication();
        tracker = analyticsApplication.getDefaultTracker();
        tracker.setScreenName("Featured Screen");
        tracker.send(new HitBuilders.ScreenViewBuilder().build());

        iv_close = (ImageView) findViewById(R.id.iv_close);
        bt_view = (Button) findViewById(R.id.bt_view);
        nv_feature = (NetworkImageView) findViewById(R.id.nv_feature);
        pg_bar = (ProgressBar) findViewById(R.id.pg_bar);
        requestQueue = Volley.newRequestQueue(this);
        imageLoader = Singleton_volley.getInstance().getImageLoader();
        bt_view.setTypeface(AsifUtils.getRaleWay_Medium(this));

        bt_view.setVisibility(View.GONE);
        iv_close.setVisibility(View.GONE);

        callFeatureApi();
//        openUpdateDialogue();
//        createAlertNotification();

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Tanishq_Screen.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        });

        bt_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!featured_category_id.isEmpty() || featured_category_id != null) {
                    reportEventToGoogle("Featured", "Clicks", featured_cat_title);
                    Intent intent = new Intent(getApplicationContext(), Tanishq_Screen.class);
                    intent.putExtra("featured_category_id", featured_category_id);
                    intent.putExtra("content_type", contenttype);
                    intent.putExtra("cata_name", featured_cat_title);
                    intent.putExtra("hasfilter", hasfilter);
                    intent.putExtra("filtered_id", filterid);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                } else {
                    Intent intent = new Intent(getApplicationContext(), Tanishq_Screen.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                }
            }
        });
    }

    public void reportEventToGoogle(String category, String action, String label) {
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory(category)
                .setAction(action)
                .setLabel(label)
                .build());
    }


    private void callFeatureApi() {
        if (!AsifUtils.isNetworkAvailable(this)) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
            return;
        }

        UserDetails user = new UserDetails(this);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(Const.URL, Const.FEATURED);
        params.put(Const.Params.USERNAME, user.getUserName());
        params.put(Const.Params.MOBILE, user.getMobileNumber());

        requestQueue.add(new VolleyHttpRequest(Request.Method.GET, params, Const.ServiceCode.FEATURED, this, this));
    }

    String image_url = "", featured_type = "", featured_category_id = "", featured_cat_title = "", contenttype = "", filterid = "", hasfilter = "";

    class FeatureAsync extends AsyncTask<Void, Void, Void> {

        String response;

        public FeatureAsync(String response) {
            this.response = response;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            parseFeature(response);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            try {
                imageLoader.get(image_url, new ImageLoader.ImageListener() {
                    @Override
                    public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                        pg_bar.setVisibility(View.GONE);
                        bt_view.setVisibility(View.VISIBLE);
                        iv_close.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pg_bar.setVisibility(View.GONE);
                        bt_view.setVisibility(View.VISIBLE);
                        iv_close.setVisibility(View.VISIBLE);
                    }
                });
                nv_feature.setImageUrl(image_url, imageLoader);
                pg_bar.setVisibility(View.GONE);

            } catch (Exception e) {
                e.printStackTrace();
                pg_bar.setVisibility(View.GONE);
                bt_view.setVisibility(View.VISIBLE);
                iv_close.setVisibility(View.VISIBLE);
            }
        }
    }

    private void parseFeature(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
//            Log.e("ttt", "Feature: " + response);
            JSONObject re_obj = jsonObject.getJSONObject("result");
            image_url = re_obj.getString("image_url");
            featured_type = re_obj.getString("featured_type");
            featured_category_id = re_obj.getString("featured_id");
            contenttype = re_obj.getString("contenttype");
            filterid = re_obj.getString("filterid");
            hasfilter = re_obj.getString("hasfilter");
            featured_cat_title = re_obj.getString("featured_title");

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {

        switch (serviceCode) {
            case Const.ServiceCode.FEATURED:
                if (AsifUtils.validateResponse(getApplicationContext(), response)) {
                    new FeatureAsync(response).execute();
                }

                break;
        }

    }

    @Override
    public void onErrorResponse(VolleyError error) {
        AsifUtils.validateResponse(this, error.getMessage());
        Intent intent = new Intent(getApplicationContext(), Tanishq_Screen.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }
}
