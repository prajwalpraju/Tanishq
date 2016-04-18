package com.feet.tanishq;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.StringDef;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import com.feet.tanishq.adapter.All_Collection_Adapter;
import com.feet.tanishq.adapter.Category_Adapter;
import com.feet.tanishq.adapter.Filter_Adapter;
import com.feet.tanishq.adapter.Sub_Collection_Adapter;
import com.feet.tanishq.database.DataBaseHandler;
import com.feet.tanishq.fragments.All_Collection;
import com.feet.tanishq.fragments.Filter_Products;
import com.feet.tanishq.fragments.Sub_Collection;
import com.feet.tanishq.fragments.Wish_List;
import com.feet.tanishq.model.Model_Category;
import com.feet.tanishq.model.Model_Filter;
import com.feet.tanishq.model.Model_Params;
import com.feet.tanishq.utils.AsifUtils;
import com.feet.tanishq.utils.AsyncTaskCompleteListener;
import com.feet.tanishq.utils.Const;
import com.feet.tanishq.utils.DividerItemDecoration;
import com.feet.tanishq.utils.SimpleGestureFilter;
import com.feet.tanishq.utils.UserDetails;
import com.feet.tanishq.utils.VolleyHttpRequest;

import java.util.ArrayList;
import java.util.HashMap;

public class Tanishq_Screen extends CustomAppCompactActivity implements AsyncTaskCompleteListener,Response.ErrorListener,All_Collection_Adapter.AdapterCallback{


    LinearLayout ll_display,ll_filter,ll_icon,ll_recycler,ll_selected_filters;
    ImageView iv_toggle,iv_collection,iv_wish,iv_compare,iv_help,iv_toggle_filter,iv_collection_icon,
            iv_category_icon,iv_material_icon,iv_occasion_icon;
    TextView tv_welcome_user,tv_logout,tv_item_name;
    FrameLayout fl_fragment;
    Button bt_clear,bt_done;

    RecyclerView rv_cat_item,rv_selected_filter;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.LayoutManager filter_layoutManager;

    RequestQueue requestQueue;

    ArrayList<Model_Filter> arr_filter=new ArrayList<Model_Filter>();
    public static Activity activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tanishq__screen);
        activity=this;

        requestQueue= Volley.newRequestQueue(this);
        LocalBroadcastManager.getInstance(this).registerReceiver(FilterRecyclerBroadcast,new IntentFilter("filter"));

        ll_display = (LinearLayout) findViewById(R.id.ll_display);
        ll_filter=(LinearLayout) findViewById(R.id.ll_filter);
        ll_icon=(LinearLayout) findViewById(R.id.ll_icon);
        ll_recycler=(LinearLayout) findViewById(R.id.ll_recycler);
        ll_selected_filters=(LinearLayout) findViewById(R.id.ll_selected_filters);

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

        tv_welcome_user.setTypeface(AsifUtils.getRaleWay_SemiBold(this));
        tv_logout.setTypeface(AsifUtils.getRaleWay_SemiBold(this));
        tv_item_name.setTypeface(AsifUtils.getRaleWay_Bold(this));


        bt_clear=(Button) findViewById(R.id.bt_clear);
        bt_done=(Button) findViewById(R.id.bt_done);

        bt_clear.setTypeface(AsifUtils.getRaleWay_Thin(this));
        bt_done.setTypeface(AsifUtils.getRaleWay_Thin(this));


        rv_cat_item=(RecyclerView) findViewById(R.id.rv_cat_item);
        rv_selected_filter=(RecyclerView) findViewById(R.id.rv_selected_filter);

        rv_cat_item.setHasFixedSize(true);
        rv_selected_filter.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        filter_layoutManager=new LinearLayoutManager(this);


        rv_cat_item.setLayoutManager(layoutManager);
        rv_selected_filter.setLayoutManager(filter_layoutManager);
//        rv_selected_filter.setLayoutManager(layoutManager);
//        rv_cat_item.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.line_divider)));
//        rv_selected_filter.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.line_divider)));

        iv_toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ll_filter.getVisibility() == View.GONE) {
                    openSlideWithAnim();
                }
            }
        });

        iv_toggle_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ll_filter.getVisibility() == View.VISIBLE) {
                     closeSlideWithAnim();
                }
            }
        });

        ll_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                return;
            }
        });

        ll_recycler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                return;
            }
        });

        ll_selected_filters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                return;
            }
        });

        tv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callLogoutApi();
            }
        });

        bt_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(arr_filter.size()>0){
                   closeSlideWithAnim();
                   setUpFilterProducts();

               }
            }
        });

        fl_fragment=(FrameLayout) findViewById(R.id.fl_fragment);


        iv_wish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoWishListFragment();
            }
        });

        UserDetails user=new UserDetails(getApplicationContext());
        tv_welcome_user.setText(user.getUserName());

        gotoAllCollectionFragment();
        setUpFrameUI();

    }

    private void setUpFilterProducts() {
        String collection_id="",jewellery_id="",material_id="",occassion_id="",
        collection_name="",jewellery_name="",occassion_name="",material_name="";
        for(Model_Filter model:arr_filter){
            switch (model.getCat_id()){
                case "1":
                    jewellery_id=model.getItem_id();
                    jewellery_name=model.getItem_name();
                    break;
                case "2":
                    collection_id=model.getItem_id();
                    collection_name=model.getItem_name();
                    break;
                case "3":
                    material_id=model.getItem_id();
                    material_name=model.getItem_name();
                    break;
                case "4":
                    occassion_id=model.getItem_id();
                    occassion_name=model.getItem_name();
                    break;
            }
        }

        Model_Params model_params=new Model_Params(collection_id,jewellery_id,occassion_id,material_id,
                collection_name,jewellery_name,occassion_name,material_name);
        gotoFilterProductFragment(model_params);
    }

    BroadcastReceiver FilterRecyclerBroadcast=new BroadcastReceiver() {
            @Override
             public void onReceive(Context context, Intent intent) {
                int type=intent.getIntExtra("type", 0);
//                int position=intent.getIntExtra("position",0);
                Model_Category model_category= (Model_Category) intent.getSerializableExtra("model");
                if (type==0) {
                    deleteFilterRecycler(model_category);
                } else {
                    addFilterRecycler(model_category);
                }
            }
        };
    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(FilterRecyclerBroadcast);
    }
    RecyclerView.Adapter filter_adapter;
    private synchronized void addFilterRecycler(Model_Category model_category){
        Model_Filter filter=new Model_Filter(model_category.getCat_id(),model_category.getId(),model_category.getName());
        arr_filter.add(filter);
        filter_adapter=new Filter_Adapter(this,arr_filter);
        rv_selected_filter.setAdapter(filter_adapter);
        filter_adapter.notifyDataSetChanged();
    }

    private synchronized void deleteFilterRecycler(Model_Category model_category){
       String cat_id= model_category.getCat_id();
        String item_id=model_category.getId();
        if(arr_filter.size()>0){
        for (int i=0;i<arr_filter.size();i++){
            Model_Filter model=arr_filter.get(i);
            if(cat_id.matches(model.getCat_id())&&item_id.matches(model.getItem_id())){
                    arr_filter.remove(i);
                break;
            }
        }
        }

        filter_adapter.notifyDataSetChanged();
    }


    private void setUpFrameUI(){

        iv_category_icon.setBackgroundColor(getResources().getColor(R.color.black_recyclelay));
        iv_collection_icon.setBackgroundColor(getResources().getColor(R.color.black_iconlay));
        iv_material_icon.setBackgroundColor(getResources().getColor(R.color.black_iconlay));
        iv_occasion_icon.setBackgroundColor(getResources().getColor(R.color.black_iconlay));
        tv_item_name.setText("CATEGORY");
        new SetUpFrameFilters("1").execute();

            iv_category_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new SetUpFrameFilters("1").execute();
                    iv_category_icon.setBackgroundColor(getResources().getColor(R.color.black_recyclelay));
                    iv_collection_icon.setBackgroundColor(getResources().getColor(R.color.black_iconlay));
                    iv_material_icon.setBackgroundColor(getResources().getColor(R.color.black_iconlay));
                    iv_occasion_icon.setBackgroundColor(getResources().getColor(R.color.black_iconlay));


                }
            });

            iv_collection_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new SetUpFrameFilters("2").execute();
                    iv_category_icon.setBackgroundColor(getResources().getColor(R.color.black_iconlay));
                    iv_collection_icon.setBackgroundColor(getResources().getColor(R.color.black_recyclelay));
                    iv_material_icon.setBackgroundColor(getResources().getColor(R.color.black_iconlay));
                    iv_occasion_icon.setBackgroundColor(getResources().getColor(R.color.black_iconlay));

                }
            });

            iv_material_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new SetUpFrameFilters("3").execute();
                    iv_category_icon.setBackgroundColor(getResources().getColor(R.color.black_iconlay));
                    iv_collection_icon.setBackgroundColor(getResources().getColor(R.color.black_iconlay));
                    iv_material_icon.setBackgroundColor(getResources().getColor(R.color.black_recyclelay));
                    iv_occasion_icon.setBackgroundColor(getResources().getColor(R.color.black_iconlay));

                }
            });

            iv_occasion_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new SetUpFrameFilters("4").execute();
                    iv_category_icon.setBackgroundColor(getResources().getColor(R.color.black_iconlay));
                    iv_collection_icon.setBackgroundColor(getResources().getColor(R.color.black_iconlay));
                    iv_material_icon.setBackgroundColor(getResources().getColor(R.color.black_iconlay));
                    iv_occasion_icon.setBackgroundColor(getResources().getColor(R.color.black_recyclelay));
                }
            });

    }

    SQLiteDatabase db;
    private void getValuesFromDb(String id){

        arr_list.clear();
        db=openOrCreateDatabase(DataBaseHandler.DATABASE_NAME,MODE_PRIVATE,null);

        Cursor cs=db.rawQuery("select * from "+DataBaseHandler.TABLE_CATEGORY+" where cat_id="+id,null);

        if (cs.moveToFirst()){
            do {
                String item_id=cs.getString(cs.getColumnIndex("item_id"));
                String item_name=cs.getString(cs.getColumnIndex("item_name"));
                String cat_id=cs.getString(cs.getColumnIndex("cat_id"));
                String cat_name=cs.getString(cs.getColumnIndex("cat_name"));

                Model_Category model=new Model_Category(cat_id,cat_name,item_id,item_name,false);
                arr_list.add(model);
            } while (cs.moveToNext());

        }

        db.close();
    }

    ArrayList<Model_Category> arr_list=new ArrayList<Model_Category>();


    class SetUpFrameFilters extends AsyncTask<Void,Void,Void>{

        String cat_id;
        SetUpFrameFilters(String cat_id){
            this.cat_id=cat_id;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            arr_list.clear();

        }

        @Override
        protected Void doInBackground(Void... params) {
            getValuesFromDb(cat_id);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            RecyclerView.Adapter adapter=new Category_Adapter(Tanishq_Screen.this,arr_list);
            rv_cat_item.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            switch (cat_id){
                case "1":
                    tv_item_name.setText("CATEGORY");
                    break;
                case "2":
                    tv_item_name.setText("COLLECTION");
                    break;
                case "3":
                    tv_item_name.setText("MATERIAL");
                    break;
                case "4":
                    tv_item_name.setText("OCCASION");
                    break;
            }
        }
    }



    private void openSlideWithAnim(){
        final Animation anim= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.pull_in_left);
        ll_filter.setVisibility(View.VISIBLE);
        ll_filter.setAnimation(anim);
    }

    private void closeSlideWithAnim(){
        final Animation anim= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.push_out_left);
        ll_filter.setAnimation(anim);
        ll_filter.setVisibility(View.GONE);

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
        params.put(Const.Params.MOBILE, user.getMobileNumber());

        requestQueue.add(new VolleyHttpRequest(Request.Method.POST, params, Const.ServiceCode.LOGOUT, this, this));
    }



    @Override
    public void onBackPressed() {
        if (ll_filter.getVisibility()==View.VISIBLE){
            closeSlideWithAnim();
        }else {
            gotoAllCollectionFragment();
        }

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
        addFragment(wish_list,false, Const.FRAG_WISH_LIST);
    }

    public void gotoSub_CollectionFragment(String cat_id,String cat_name){
        Sub_Collection sub_collection=Sub_Collection.newInstance(cat_id,cat_name);
        addFragment(sub_collection,false,Const.FRAG_SUB_COLL);

    }

    public void gotoFilterProductFragment(Model_Params params){
        Filter_Products filter_products=Filter_Products.newInstance(params);
        addFragment(filter_products,false,Const.FRAG_SUB_COLL);
    }
    public void gotoCompareFragment(){

    }
    public void gotoDetailsFragment(){

    }
    public void gotoFeedBackFragment(){

    }
    public void gotoUserFragment(){

    }


    @Override
    public void onMethodCallback(int frag,String cat_id,String cat_name) {
            gotoSub_CollectionFragment(cat_id,cat_name);
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
        AsifUtils.validateResponse(this, error.getMessage());

    }
}
