package com.feet.tanishq;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.feet.tanishq.adapter.Category_Adapter;
import com.feet.tanishq.fragments.All_Collection;
import com.feet.tanishq.fragments.Wish_List;
import com.feet.tanishq.model.Model_Category;
import com.feet.tanishq.utils.AsifUtils;
import com.feet.tanishq.utils.AsyncTaskCompleteListener;
import com.feet.tanishq.utils.Const;
import com.feet.tanishq.utils.DividerItemDecoration;
import com.feet.tanishq.utils.UserDetails;
import com.feet.tanishq.utils.VolleyHttpRequest;

import java.util.ArrayList;
import java.util.HashMap;

public class Tanishq_Screen extends CustomAppCompactActivity implements AsyncTaskCompleteListener,Response.ErrorListener {


    LinearLayout ll_display,ll_filter;
    ImageView iv_toggle,iv_collection,iv_wish,iv_compare,iv_help,iv_toggle_filter,iv_collection_icon,
            iv_category_icon,iv_material_icon,iv_occasion_icon;
    TextView tv_welcome_user,tv_logout,tv_item_name;
    FrameLayout fl_fragment;
    Button bt_clear,bt_done;

    RecyclerView rv_cat_item,rv_selected_filter;
    RecyclerView.LayoutManager layoutManager;

    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tanishq__screen);

        requestQueue= Volley.newRequestQueue(this);

        ll_display = (LinearLayout) findViewById(R.id.ll_display);
        ll_filter=(LinearLayout) findViewById(R.id.ll_filter);

        iv_toggle=(ImageView) findViewById(R.id.iv_toggle);
        iv_collection=(ImageView) findViewById(R.id.iv_collection);
        iv_wish=(ImageView) findViewById(R.id.iv_wish);
        iv_compare=(ImageView) findViewById(R.id.iv_compare);
        iv_help=(ImageView) findViewById(R.id.iv_help);

        iv_toggle_filter=(ImageView) findViewById(R.id.iv_toggle_filter);
        iv_collection_icon=(ImageView) findViewById(R.id.iv_collection_icon);
        iv_category_icon=(ImageView) findViewById(R.id.iv_category_icon);
        iv_material_icon=(ImageView) findViewById(R.id.iv_material_icon);
        iv_occasion_icon=(ImageView) findViewById(R.id.iv_occasion_icon);

        tv_welcome_user=(TextView) findViewById(R.id.tv_welcome_user);
        tv_logout=(TextView) findViewById(R.id.tv_logout);
        tv_item_name=(TextView) findViewById(R.id.tv_item_name);

        bt_clear=(Button) findViewById(R.id.bt_clear);
        bt_done=(Button) findViewById(R.id.bt_done);

        rv_cat_item=(RecyclerView) findViewById(R.id.rv_cat_item);
        rv_selected_filter=(RecyclerView) findViewById(R.id.rv_selected_filter);

        rv_cat_item.setHasFixedSize(true);
        rv_selected_filter.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);

        rv_cat_item.setLayoutManager(layoutManager);
//        rv_selected_filter.setLayoutManager(layoutManager);
        rv_cat_item.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.line_divider)));
//        rv_selected_filter.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.line_divider)));

        iv_toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ddd", "onClick: iv click");
                if (ll_filter.getVisibility() == View.GONE) {
                    ll_filter.setVisibility(View.VISIBLE);
                }
            }
        });

        iv_toggle_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ddd", "onClick: iv click22");
                if (ll_filter.getVisibility() == View.VISIBLE) {
                    ll_filter.setVisibility(View.GONE);
                }
            }
        });

        tv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callLogoutApi();
            }
        });


        ArrayList<Model_Category> arr_list=new ArrayList<Model_Category>();
        arr_list.clear();
        for (int i = 0; i <5; i++) {
            Model_Category model=new Model_Category(""+i,"bangles "+i,false);
            arr_list.add(model);
        }

        RecyclerView.Adapter adapter=new Category_Adapter(this,arr_list);

        fl_fragment=(FrameLayout) findViewById(R.id.fl_fragment);
        gotoAllCollectionFragment();

        iv_wish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoWishListFragment();
            }
        });

    }

    private  void callLogoutApi(){
       if(!AsifUtils.isNetworkAvailable(this)){
           Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
           return;
       }
        UserDetails user=new UserDetails(this);
        HashMap<String,String> params=new HashMap<String,String>();
        params.put(Const.URL,Const.LOGOUT);
        params.put(Const.Params.USERNAME,user.getUserName());
        params.put(Const.Params.MOBILE,user.getMobileNumber());

        requestQueue.add(new VolleyHttpRequest(Request.Method.POST, params, Const.ServiceCode.LOGOUT, this, this));
    }



    @Override
    public void onBackPressed() {
        gotoAllCollectionFragment();
    }

    @Override
    protected boolean isValidate() {
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment != null) {
                fragment.onActivityResult(requestCode, resultCode, data);
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    public void gotoTrendingFragment(){

    }
    public void gotoAllCollectionFragment(){
        All_Collection all_collectionFrag=All_Collection.newInstance();
        addFragment(all_collectionFrag,false, Const.FRAG_All_COLL);
    }
    public void gotoWishListFragment(){
        Wish_List wish_list=Wish_List.newInstance();
        addFragment(wish_list,false, Const.FRAG_All_COLL);
    }
    public void gotoCompareFragment(){

    }
    public void gotoDeatailsFragment(){

    }
    public void gotoFeedBackFragment(){

    }
    public void gotoUserFragment(){

    }


    @Override
    public void onTaskCompleted(String response, int serviceCode) {
        switch (serviceCode){
            case Const.ServiceCode.LOGOUT:
                if (AsifUtils.validateResponse(getApplicationContext(),response)){
                    new UserDetails(this).clearUserPreference();
                    Intent intent=new Intent(getApplicationContext(),Login_Screen.class);
                    startActivity(intent);
                    finish();
                }

                break;
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }
}
