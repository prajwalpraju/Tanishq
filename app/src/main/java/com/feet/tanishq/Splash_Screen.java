package com.feet.tanishq;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.feet.tanishq.utils.AsyncTaskCompleteListener;
import com.feet.tanishq.utils.Const;
import com.feet.tanishq.utils.Singleton_volley;
import com.feet.tanishq.utils.UserDetails;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;


public class Splash_Screen extends AppCompatActivity implements AsyncTaskCompleteListener,Response.ErrorListener{

    ImageView iv_logo;


    Tracker tracker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash__screen);


        Singleton_volley analyticsApplication= (Singleton_volley) getApplication();
        tracker=analyticsApplication.getDefaultTracker();
        tracker.setScreenName("Splash Screen");
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
        iv_logo=(ImageView) findViewById(R.id.iv_logo);
        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        iv_logo.setAnimation(anim);
        new SplashAsync().execute();
    }


    @Override
    public void onTaskCompleted(String response, int serviceCode) {

    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }


    class SplashAsync extends AsyncTask<Void,Void,Void>{


        @Override
        protected void onPreExecute() {
            super.onPreExecute();



        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            String res=getDeviceResolution();

            UserDetails user=new UserDetails(getApplicationContext());
            user.setUserDevice(res);
            Log.d("ddd", "user_id pref: " + user.getUserId());
            if (user.getUserId().isEmpty()||user.getUserId()==null){
                Intent intent=new Intent(getApplicationContext(),Login_Screen.class);
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
    }

    private String getDeviceResolution() {
        String res_str="hdpi";
        double res=getResources().getDisplayMetrics().density;

        if(res==Const.Resolution.MDPI){
            res_str=Const.Resolution.MDPI_TXT;
        }else if(res==Const.Resolution.HDPI){
            res_str=Const.Resolution.HDPI_TXT;
        }else if(res==Const.Resolution.XHDPI){
            res_str=Const.Resolution.XHDPI_TXT;
        }else if(res==Const.Resolution.XXHPDI){
            res_str=Const.Resolution.XXHPDI_TXT;
        }else if(res==Const.Resolution.XXXHDPI){
            res_str=Const.Resolution.XXXHDPI_TXT;
        }
        Log.d("ttt", "getDeviceResolution: "+res_str+"  res="+res);

        return res_str;
    }
}
