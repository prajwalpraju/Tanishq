package com.feet.tanishq;

import android.content.ContentValues;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.nfc.Tag;
import android.opengl.ETC1;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.feet.tanishq.utils.UserDetails;
import com.feet.tanishq.utils.VolleyHttpRequest;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Order;

import org.json.JSONArray;
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
    Button bt_submit,bt_submit_otp;
    String TAG="Login_Screen";
    LinearLayout ll_login,ll_otp;

    RequestQueue requestQueue;
    Validator validator;

    EditText et_otp;

    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login__screen);

        requestQueue= Volley.newRequestQueue(this);
        validator=new Validator(this);
        validator.setValidationListener(this);

        ll_login=(LinearLayout)findViewById(R.id.ll_login);
        ll_otp=(LinearLayout)findViewById(R.id.ll_otp);
        tv_welcome=(TextView)findViewById(R.id.tv_welcome);
        et_username=(EditText) findViewById(R.id.et_username);
        et_mobile=(EditText) findViewById(R.id.et_mobile);
        et_otp=(EditText) findViewById(R.id.et_otp);
        bt_submit=(Button)findViewById(R.id.bt_submit);
        bt_submit_otp=(Button)findViewById(R.id.bt_submit_otp);

        tv_welcome.setTypeface(AsifUtils.getRaleWay_Thin(this));
        et_username.setTypeface(AsifUtils.getRaleWay_Thin(this));
        et_mobile.setTypeface(AsifUtils.getRaleWay_Thin(this));
        et_otp.setTypeface(AsifUtils.getRaleWay_Thin(this));
        bt_submit.setTypeface(AsifUtils.getRaleWay_Thin(this));
        bt_submit_otp.setTypeface(AsifUtils.getRaleWay_Thin(this));

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
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertIntoCatTable(String cat_id,String cat_name,String item_id,String item_name){
//        Log.d(TAG, "insertIntoCatTable: "+item_name);
        ContentValues values=new ContentValues();
        values.put("cat_id",cat_id);
        values.put("cat_name",cat_name);
        values.put("item_id",item_id);
        values.put("item_name",item_name);
        db.insert(DataBaseHandler.TABLE_CATEGORY,null,values);

    }

    private void callVerfiyOtp(String otp_code){
        if (!AsifUtils.isNetworkAvailable(Login_Screen.this)) {
            Toast.makeText(getApplicationContext(),getResources().getString(R.string.no_internet),Toast.LENGTH_SHORT).show();
            return;
        }
        UserDetails user=new UserDetails(getApplicationContext());
        HashMap<String,String> params=new HashMap<String,String>();
        params.put(Const.URL,Const.OTP_VERIFY);
//        Log.d(TAG, "callVerfiyOtp: "+user.getUserName());
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
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

            parseUserAndCategoryInsertDb(response);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(getApplicationContext(),"otp verify success",Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(getApplicationContext(),ThankYou_Screen.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

        }
    }

    private void parseUserAndCategoryInsertDb(String response){

        try {
            JSONObject jobj=new JSONObject(response);
            JSONObject jobj_response=jobj.getJSONObject("response");
            JSONArray jArrUser=jobj_response.getJSONArray("userinfo");
            Log.d(TAG, "parseUserAndCategoryInsertDb: jArrUser="+jArrUser.length());
            for (int i=0;i<jArrUser.length();i++){
                JSONObject jArrObj=jArrUser.getJSONObject(i);
                UserDetails user=new UserDetails(this);
                user.setUserId(jArrObj.getString("id"));
                user.setUserName(jArrObj.getString("username"));
                user.setMobileNumber(jArrObj.getString("mobile"));
                Log.d(TAG, "parseUserAndCategoryInsertDb: UserId-"+jArrObj.getString("id"));
            }

            JSONObject jobj_filter=jobj_response.getJSONObject("filterconfiginfo");
            JSONArray jArrCategory=jobj_filter.getJSONArray("Category");
            int cat_size=jArrCategory.length();
            Log.d(TAG, "parseUserAndCategoryInsertDb: cat_size="+cat_size);
            for (int i=0;i<cat_size;i++){
                JSONObject cat_obj=jArrCategory.getJSONObject(i);
                String id=cat_obj.getString("id");
                String name=cat_obj.getString("name");
                insertIntoCatTable("1","Category",id,name);
            }
            JSONArray jArrCollection=jobj_filter.getJSONArray("Collection");
            int col_size=jArrCollection.length();
            Log.d(TAG, "parseUserAndCategoryInsertDb: col_size="+col_size);
            for (int i=0;i<col_size;i++){
                JSONObject col_obj=jArrCollection.getJSONObject(i);
                String id=col_obj.getString("id");
                String name=col_obj.getString("name");
                insertIntoCatTable("2","Collection",id,name);
            }
            JSONArray jArrMaterial=jobj_filter.getJSONArray("Material");
            int mat_size=jArrMaterial.length();
            Log.d(TAG, "parseUserAndCategoryInsertDb: mat_size="+mat_size);
            for (int i=0;i<mat_size;i++){
                JSONObject mat_obj=jArrMaterial.getJSONObject(i);
                String id=mat_obj.getString("id");
                String name=mat_obj.getString("name");
                insertIntoCatTable("3","Material",id,name);
            }
            JSONArray jArrOccasion=jobj_filter.getJSONArray("Occasion");
            int occ_size=jArrOccasion.length();
            Log.d(TAG, "parseUserAndCategoryInsertDb: occ_size="+occ_size);
            for (int i=0;i<occ_size;i++){
                JSONObject occ_obj=jArrOccasion.getJSONObject(i);
                String id=occ_obj.getString("id");
                String name=occ_obj.getString("name");
                insertIntoCatTable("4","Occasion",id,name);
            }

        } catch (Exception e) {
            Log.e(TAG, "exception: "+Log.getStackTraceString(e) );
        }

    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {
        switch (serviceCode){
            case Const.ServiceCode.USERLOGIN:
                if (AsifUtils.validateResponse(getApplicationContext(),response)) {
//                    Log.d(TAG, "onTaskCompleted: response success");
                    Toast.makeText(getApplicationContext(),"success",Toast.LENGTH_SHORT).show();
                    UserDetails user=new UserDetails(getApplicationContext());
                    user.setUserName(et_username.getText().toString().trim());
                    user.setMobileNumber(et_mobile.getText().toString().trim());
                    ll_otp.setVisibility(View.VISIBLE);
                    ll_login.setVisibility(View.GONE);
                }
                break;

            case Const.ServiceCode.OTP_VERIFY:
                if (AsifUtils.validateResponse(getApplicationContext(),response)){
                    Log.d(TAG, "onTaskCompleted: otp verify success");
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
        AsifUtils.validateResponse(this, error.getMessage());

    }

    @Override
    public void onValidationSucceeded() {
    //intent to the next activity
        Log.d(TAG, "onValidationSucceeded: ");
        callLoginApi(et_username.getText().toString().trim(), et_mobile.getText().toString().trim());
    }

    @Override
    public void onValidationFailed(List<ValidationError> listErrors) {
        Log.d(TAG, "onValidationFailed: ");
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
