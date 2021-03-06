package com.feet.tanishq;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.feet.tanishq.utils.AsifUtils;
import com.feet.tanishq.utils.Singleton_volley;
import com.feet.tanishq.utils.UserDetails;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class ThankYou_Screen extends AppCompatActivity {

    TextView tv_thankyou,tv_forllog,tv_username;
    Tracker tracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thank_you__screen);

        Singleton_volley analyticsApplication= (Singleton_volley) getApplication();
        tracker=analyticsApplication.getDefaultTracker();
        tracker.setScreenName("Thankyou Screen");
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
        tv_thankyou=(TextView) findViewById(R.id.tv_thankyou);
        tv_forllog=(TextView) findViewById(R.id.tv_for_log);
        tv_username=(TextView) findViewById(R.id.tv_username);

        tv_thankyou.setTypeface(AsifUtils.getRaleWay_Medium(this));
        tv_forllog.setTypeface(AsifUtils.getRaleWay_Medium(this));
        tv_username.setTypeface(AsifUtils.getRaleWay_Medium(this));

        UserDetails user=new UserDetails(getApplicationContext());
        tv_username.setText(user.getUserTitle()+". "+user.getUserName());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(getApplicationContext(),Featured.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        },2000);
    }
}
