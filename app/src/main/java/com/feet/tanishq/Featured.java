package com.feet.tanishq;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Featured extends AppCompatActivity implements AsyncTaskCompleteListener,Response.ErrorListener{


    ImageView iv_close;
    Button bt_view;
    NetworkImageView nv_feature;
    ProgressBar pg_bar;

    RequestQueue requestQueue;
    ImageLoader imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_featured);

        iv_close=(ImageView) findViewById(R.id.iv_close);
        bt_view=(Button) findViewById(R.id.bt_view);
        nv_feature=(NetworkImageView) findViewById(R.id.nv_feature);
        pg_bar=(ProgressBar) findViewById(R.id.pg_bar);
        requestQueue= Volley.newRequestQueue(this);
        imageLoader= Singleton_volley.getInstance().getImageLoader();

        bt_view.setVisibility(View.GONE);
        iv_close.setVisibility(View.GONE);

        callFeatureApi();

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),Tanishq_Screen.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        });

        bt_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!featured_id.isEmpty() || featured_id != null) {
                    Intent intent=new Intent(getApplicationContext(),Tanishq_Screen.class);
                    intent.putExtra("featured_id",featured_id);
                    intent.putExtra("featured_name",featured_title);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                }else{
                    Intent intent=new Intent(getApplicationContext(),Tanishq_Screen.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                }
            }
        });
    }

    private void callFeatureApi(){
        if(!AsifUtils.isNetworkAvailable(this)){
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
            return;
        }

        UserDetails user=new UserDetails(this);
        HashMap<String,String> params=new HashMap<String,String>();
        params.put(Const.URL,Const.FEATURED);
        params.put(Const.Params.USERNAME, user.getUserName());
        params.put(Const.Params.MOBILE, user.getMobileNumber());

        requestQueue.add(new VolleyHttpRequest(Request.Method.GET, params, Const.ServiceCode.FEATURED, this, this));
    }

    String image_url="",featured_type="",featured_id="",featured_title="";

    class FeatureAsync extends AsyncTask<Void,Void,Void> {

        String response;

        public FeatureAsync(String response){
            this.response=response;
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
                pg_bar.setVisibility(View.VISIBLE);

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
            JSONObject jsonObject= new JSONObject(response);
            JSONObject re_obj=jsonObject.getJSONObject("result");
            image_url=re_obj.getString("image_url");
            featured_type=re_obj.getString("featured_type");
            featured_id=re_obj.getString("featured_id");
            featured_title=re_obj.getString("featured_title");

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {

        switch (serviceCode){
            case Const.ServiceCode.FEATURED:
//                Log.d("response", "validateResponse:ss=== " + response);
                if (AsifUtils.validateResponse(getApplicationContext(),response)){
                    new FeatureAsync(response).execute();
                }

                break;
        }

    }

    @Override
    public void onErrorResponse(VolleyError error) {
        AsifUtils.validateResponse(this, error.getMessage());
    }
}
