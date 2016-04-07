package com.feet.tanishq;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.feet.tanishq.utils.AsyncTaskCompleteListener;
import com.feet.tanishq.utils.UserDetails;


public class Splash_Screen extends AppCompatActivity implements AsyncTaskCompleteListener,Response.ErrorListener{

    ImageView iv_logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash__screen);

        iv_logo=(ImageView) findViewById(R.id.iv_logo);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
                Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), android.support.design.R.anim.abc_fade_in);
                iv_logo.setAnimation(anim);
//            }
//        }, 2000);
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
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            UserDetails user=new UserDetails(getApplicationContext());
            Log.d("ddd", "user_id pref: "+user.getUserId());
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
}
