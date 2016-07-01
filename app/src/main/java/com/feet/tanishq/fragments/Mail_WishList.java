package com.feet.tanishq.fragments;


import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.feet.tanishq.R;
import com.feet.tanishq.database.DataBaseHandler;
import com.feet.tanishq.interfaces.AdapterCallback;
import com.feet.tanishq.utils.AsifUtils;
import com.feet.tanishq.utils.AsyncTaskCompleteListener;
import com.feet.tanishq.utils.Const;
import com.feet.tanishq.utils.UserDetails;
import com.feet.tanishq.utils.VolleyHttpRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Mail_WishList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Mail_WishList extends Fragment implements AsyncTaskCompleteListener,Response.ErrorListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    public Mail_WishList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    // TODO: Rename and change types and number of parameters
    public static Mail_WishList newInstance() {
        Mail_WishList fragment = new Mail_WishList();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    TextView tv_mail,tv_title,tv_name,tv_mobile,tv_email;
    EditText et_name,et_mobile,et_email;
    Button bt_send;
    RequestQueue requestQueue;
    AdapterCallback adapterCallback;
    String wish_id;
    SQLiteDatabase db;
    AppCompatSpinner sp_title;

    String[] titleArr={"Mr","Mrs","Ms"};
    ArrayAdapter<String> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_mail_wish_list, container, false);

        this.adapterCallback= (AdapterCallback) getContext();
        requestQueue= Volley.newRequestQueue(getContext());

        tv_mail=(TextView) v.findViewById(R.id.tv_mail);
        tv_title=(TextView) v.findViewById(R.id.tv_title);
        tv_name=(TextView) v.findViewById(R.id.tv_name);
        tv_mobile=(TextView) v.findViewById(R.id.tv_mobile);
        tv_email=(TextView) v.findViewById(R.id.tv_email);

        et_name=(EditText) v.findViewById(R.id.et_name);
        et_mobile=(EditText) v.findViewById(R.id.et_mobile);
        et_email=(EditText) v.findViewById(R.id.et_email);
        sp_title=(AppCompatSpinner) v.findViewById(R.id.sp_title);

        tv_mail.setTypeface(AsifUtils.getRaleWay_Bold(getContext()));

        tv_title.setTypeface(AsifUtils.getRaleWay_Medium(getContext()));
        tv_name.setTypeface(AsifUtils.getRaleWay_Medium(getContext()));
        tv_mobile.setTypeface(AsifUtils.getRaleWay_Medium(getContext()));
        tv_email.setTypeface(AsifUtils.getRaleWay_Medium(getContext()));
        et_mobile.setTypeface(AsifUtils.getRaleWay_Medium(getContext()));
        et_email.setTypeface(AsifUtils.getRaleWay_Medium(getContext()));

        bt_send=(Button) v.findViewById(R.id.bt_send);
        bt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInfo()) {
                    callMailInfoApi();
                }
            }
        });

        adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1, titleArr);
        sp_title.setAdapter(adapter);

        addToString();

        return v;
    }

    private boolean validateInfo(){
        String name=et_name.getText().toString().trim();
        String mobile=et_mobile.getText().toString().trim();
        String email=et_email.getText().toString().trim();
        String title=sp_title.getSelectedItem().toString();

        if(title==null||title.isEmpty()){
            Toast.makeText(getActivity(), "select tile", Toast.LENGTH_SHORT).show();
            return false;
        }else if(name.isEmpty()||name.length()==0){
            Toast.makeText(getActivity(), "please enter name", Toast.LENGTH_SHORT).show();
            return false;
        }else if(mobile.isEmpty()||mobile.length()<10){
            Toast.makeText(getActivity(), "please enter valid mobile number", Toast.LENGTH_SHORT).show();
            return false;
        }else if(!AsifUtils.isValidEmail(email)){
            Toast.makeText(getActivity(), "please enter valid email id", Toast.LENGTH_SHORT).show();

            return false;
        }


        return true;
    }


    List<String> arr=new ArrayList<String>();

    private void addToString(){
        arr.clear();
        try {
            db = getActivity().openOrCreateDatabase(DataBaseHandler.DATABASE_NAME, Context.MODE_PRIVATE, null);
            Cursor cs=db.rawQuery("select product_title from "+DataBaseHandler.TABLE_WISHLIST,null);
            if(cs.moveToFirst()){
                do {
                   String id=cs.getString(cs.getColumnIndex("product_title"));
                    arr.add(id);
                }while (cs.moveToNext());

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        if(arr.size()>0){
            for (int i=0;i<arr.size();i++){
                if(i==0){
                    wish_id=arr.get(i);
                }else{

                    wish_id=wish_id+","+ arr.get(i);
                }
            }
        }

        Log.d("ttt", "addToString: "+wish_id);
        Log.d("ttt", "arr: "+arr);
    }

    public long getCountWishList(){
        long count =0;
        try {
            db = getActivity().openOrCreateDatabase(DataBaseHandler.DATABASE_NAME, Context.MODE_PRIVATE, null);
            count= DatabaseUtils.queryNumEntries(db, DataBaseHandler.TABLE_WISHLIST);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return count;


    }

    public void callMailInfoApi(){
        if (!AsifUtils.isNetworkAvailable(getActivity())) {
            Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
            return;
        }
        AsifUtils.start(getContext());

        UserDetails user=new UserDetails(getContext());
        HashMap<String,String> map=new HashMap<String,String>();
        map.put(Const.URL,Const.MAIL_INFO);
        map.put(Const.Params.ID, user.getUserId());
        map.put(Const.Params.MOBILE, et_mobile.getText().toString().trim());
        map.put(Const.Params.WISHLIST, wish_id);
        map.put(Const.Params.EMAILID, et_email.getText().toString().trim());
        map.put(Const.Params.USERNAME, et_name.getText().toString().trim());
        requestQueue.add(new VolleyHttpRequest(Request.Method.POST, map, Const.ServiceCode.MAIL_INFO, this, this));
    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {
        switch (serviceCode){
            case Const.ServiceCode.MAIL_INFO:
                if (AsifUtils.validateResponse(getContext(),response)){
                    adapterCallback.onMethodCallback(3);
                }

                break;
        }
        AsifUtils.stop();
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        AsifUtils.stop();
        AsifUtils.validateResponse(getContext(), error.getMessage());
    }
}
