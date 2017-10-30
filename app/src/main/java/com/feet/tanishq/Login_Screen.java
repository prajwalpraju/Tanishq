package com.feet.tanishq;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.feet.tanishq.database.DataBaseHandler;
import com.feet.tanishq.utils.AsifUtils;
import com.feet.tanishq.utils.AsyncTaskCompleteListener;
import com.feet.tanishq.utils.Const;
import com.feet.tanishq.utils.Singleton_volley;
import com.feet.tanishq.utils.UserDetails;
import com.feet.tanishq.utils.VolleyHttpRequest;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Order;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class Login_Screen extends AppCompatActivity implements AsyncTaskCompleteListener,Response.ErrorListener,Validator.ValidationListener{


    TextView tv_welcome;
    @NotEmpty(trim = true,sequence = 1,message = "Please Enter UserName")
            @Order(1)
    EditText et_username;
    @NotEmpty(trim = true,message = "Please Enter Mobile Number")
    @Length(min = 10,max = 15,message = "Please Enter at least 10 digits number")
            @Order(2)
    EditText et_mobile;
    Button bt_submit,bt_submit_otp,bt_resend_otp;
    String TAG="Login_Screen";
    LinearLayout ll_login,ll_otp;
    RelativeLayout rl_main;

    RequestQueue requestQueue;
    Validator validator;

    EditText et_otp;

    SQLiteDatabase db;

    Tracker tracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login__screen);

        requestQueue= Volley.newRequestQueue(this);
        validator=new Validator(this);
        validator.setValidationListener(this);


        Singleton_volley analyticsApplication= (Singleton_volley) getApplication();
        tracker=analyticsApplication.getDefaultTracker();
        tracker.setScreenName("Login Screen");
        Log.e("screen", "onCreate:-------------------> Login Screen");
        tracker.send(new HitBuilders.ScreenViewBuilder().build());

        rl_main=(RelativeLayout) findViewById(R.id.rl_main);
        ll_login=(LinearLayout)findViewById(R.id.ll_login);
        ll_otp=(LinearLayout)findViewById(R.id.ll_otp);
        tv_welcome=(TextView)findViewById(R.id.tv_welcome);
        et_username=(EditText) findViewById(R.id.et_username);
        et_mobile=(EditText) findViewById(R.id.et_mobile);
        et_otp=(EditText) findViewById(R.id.et_otp);
        bt_submit=(Button)findViewById(R.id.bt_submit);
        bt_submit_otp=(Button)findViewById(R.id.bt_submit_otp);
        bt_resend_otp=(Button)findViewById(R.id.bt_resend_otp);

        tv_welcome.setTypeface(AsifUtils.getRaleWay_Medium(this));
        et_username.setTypeface(AsifUtils.getRaleWay_Medium(this));
        et_mobile.setTypeface(AsifUtils.getRaleWay_Medium(this));
        et_otp.setTypeface(AsifUtils.getRaleWay_Medium(this));
        bt_submit.setTypeface(AsifUtils.getRaleWay_Medium(this));
        bt_submit_otp.setTypeface(AsifUtils.getRaleWay_Medium(this));
        bt_resend_otp.setTypeface(AsifUtils.getRaleWay_Medium(this));

        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate();
            }


        });
        bt_submit_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_otp.getText().toString().trim().length() > 3) {
                    callVerfiyOtp(et_otp.getText().toString().trim());
                } else {
                    Toast.makeText(getApplicationContext(), "Enter Received OTP!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        bt_resend_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate();
            }
        });

        rl_main.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(rl_main.getWindowToken(),0);
                return false;
            }
        });

        new CreateDataBase().execute();

    }

    class CreateDataBase extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... params) {
            createDataBase();
            return null;
        }
    }

    private void createDataBase(){
        db=openOrCreateDatabase(DataBaseHandler.DATABASE_NAME,MODE_PRIVATE,null);

        try {
            db.execSQL(DataBaseHandler.CREATE_CATEGORY_TABLE);
            db.delete(DataBaseHandler.TABLE_CATEGORY, null, null);
            db.execSQL(DataBaseHandler.CREATE_WISHLIST_TABLE);
            db.delete(DataBaseHandler.TABLE_WISHLIST, null, null);
            db.execSQL(DataBaseHandler.CREATE_COMPARE_TABLE);
            db.delete(DataBaseHandler.TABLE_COMPARE, null, null);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    private void callVerfiyOtp(String otp_code){
        if (!AsifUtils.isNetworkAvailable(Login_Screen.this)) {
            Toast.makeText(getApplicationContext(),getResources().getString(R.string.no_internet),Toast.LENGTH_SHORT).show();
            return;
        }
        AsifUtils.start(this);
        UserDetails user=new UserDetails(getApplicationContext());
        HashMap<String,String> params=new HashMap<String,String>();
        params.put(Const.URL,Const.OTP_VERIFY);

        params.put(Const.Params.USERNAME,user.getUserName());
        params.put(Const.Params.MOBILE,user.getMobileNumber());
        params.put(Const.Params.OTP_CODE, otp_code);
        requestQueue.add(new VolleyHttpRequest(Request.Method.POST, params, Const.ServiceCode.OTP_VERIFY, this, this));
    }


    private void callLoginApi(String username,String mobile){
        if (!AsifUtils.isNetworkAvailable(Login_Screen.this)) {
            Toast.makeText(getApplicationContext(),getResources().getString(R.string.no_internet),Toast.LENGTH_SHORT).show();
            return;
        }
        AsifUtils.start(this);

        HashMap<String,String> params=new HashMap<String,String>();
        params.put(Const.URL,Const.USER_LOGIN);
        params.put(Const.Params.USERNAME,username);
        params.put(Const.Params.MOBILE,mobile);
        requestQueue.add(new VolleyHttpRequest(Request.Method.POST,params,Const.ServiceCode.USERLOGIN,this,this));

    }

    class ParseResponseFromOtp extends AsyncTask<Void,Void,Void>{
        String response;
        ParseResponseFromOtp(String response){
            this.response=response;

        }

        @Override
        protected void onPreExecute() {

//            Log.e("otpresponce", "onPreExecute: "+response );
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
//            Log.e("otpresponce", "onPreExecute: "+response );
            parseUserAndCategoryInsertDb(response);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            AsifUtils.stop();

            String res=getDeviceResolution();

            UserDetails user=new UserDetails(getApplicationContext());
            user.setUserDevice(res);

            Intent intent=new Intent(getApplicationContext(),ThankYou_Screen.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

        }

    }

    private String getDeviceResolution() {
        String res_str = "hdpi";
        double res = getResources().getDisplayMetrics().density;

        if (res == Const.Resolution.MDPI) {
            res_str = Const.Resolution.MDPI_TXT;
        } else if (res == Const.Resolution.HDPI) {
            res_str = Const.Resolution.HDPI_TXT;
        } else if (res == Const.Resolution.XHDPI) {
            res_str = Const.Resolution.XHDPI_TXT;
        } else if (res == Const.Resolution.XXHPDI) {
            res_str = Const.Resolution.XXHPDI_TXT;
        } else if (res == Const.Resolution.XXXHDPI) {
            res_str = Const.Resolution.XXXHDPI_TXT;
        }
        Log.d("ttt", "getDeviceResolution: " + res_str + "  res=" + res);

        return res_str;
    }

    private void parseUserAndCategoryInsertDb(String response) {

        try {
            UserDetails user = new UserDetails(this);

            user.setMainDashboadResponse(response);

            JSONObject jobj = new JSONObject(response);
            JSONObject jobj_response = jobj.getJSONObject("response");
            JSONArray jArrUser = jobj_response.getJSONArray("userinfo");
            Log.e("ttt", "parseUserAndCategoryInsertDb: " + response);
            for (int i = 0; i < jArrUser.length(); i++) {
                JSONObject jArrObj = jArrUser.getJSONObject(i);
                tracker.setClientId(jArrObj.getString("id"));
                user.setUserId(jArrObj.getString("id"));
                user.setUserTitle(jArrObj.getString("title"));
                user.setUserName(jArrObj.getString("username"));
                user.setMobileNumber(jArrObj.getString("mobile"));
            }

            String url = jobj_response.getString("appdemoyoutubelink");
            String video_id = jobj_response.getString("appdemoyoutubeid");
            user.setDemoUrl(url);
            user.setVideo_idUrl(video_id);




        } catch (Exception e) {
//            Log.e(TAG, "exception: " + Log.getStackTraceString(e));
        }
    }

        @Override
    public void onTaskCompleted(String response, int serviceCode) {
        switch (serviceCode){
            case Const.ServiceCode.USERLOGIN:
                if (AsifUtils.validateResponse(getApplicationContext(),response)) {
                    try {
                        Toast.makeText(Login_Screen.this,new JSONObject(response).getString("message"),Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    UserDetails user=new UserDetails(getApplicationContext());
                    user.setUserName(et_username.getText().toString().trim());
                    user.setMobileNumber(et_mobile.getText().toString().trim());
                    ll_otp.setVisibility(View.VISIBLE);
                    ll_login.setVisibility(View.GONE);

                    Log.e("otp", "OTP responce: "+response);
                    Log.e("alara", "OTP responce: "+response);
//                    Apk v2

//                    try {
//                        Log.e("otp", "onTaskCompleted: "+ new JSONObject(response).getString("otpcode"));
//                        et_otp.setText(new JSONObject(response).getString("otpcode"));
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }


                    tracker.setScreenName("OTP Screen");
                    tracker.send(new HitBuilders.ScreenViewBuilder().build());
                }
                AsifUtils.stop();
                break;

            case Const.ServiceCode.OTP_VERIFY:
//                Log.e("otp", "responce: "+response );
                if (AsifUtils.validateResponse(getApplicationContext(),response)){
                    new ParseResponseFromOtp(response).execute();
                }
                break;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (ll_otp.getVisibility()==View.VISIBLE){
            ll_otp.setVisibility(View.GONE);
            ll_login.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        AsifUtils.stop();
        AsifUtils.validateResponse(this, error.getMessage());

    }

    @Override
    public void onValidationSucceeded() {
    //intent to the next activity
//        Log.d(TAG, "onValidationSucceeded: ");
        callLoginApi(et_username.getText().toString().trim(), et_mobile.getText().toString().trim());
    }

    @Override
    public void onValidationFailed(List<ValidationError> listErrors) {
//        Log.d(TAG, "onValidationFailed: ");
        for (ValidationError error:listErrors) {
            View view=error.getView();
            String message=error.getCollatedErrorMessage(this);
         if (view instanceof EditText){
             ((EditText) view).setError(message);
         }else {
             Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
         }


        }

    }
}
