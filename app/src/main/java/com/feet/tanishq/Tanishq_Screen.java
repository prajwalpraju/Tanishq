package com.feet.tanishq;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
import com.feet.tanishq.adapter.Category_Adapter;
import com.feet.tanishq.adapter.Filter_Adapter;
import com.feet.tanishq.database.DataBaseHandler;
import com.feet.tanishq.fragments.All_Collection;
import com.feet.tanishq.fragments.Compare_List;
import com.feet.tanishq.fragments.FeedBack;
import com.feet.tanishq.fragments.Filter_Products;
import com.feet.tanishq.fragments.Help_Fragment;
import com.feet.tanishq.fragments.PagerFilter_Product;
import com.feet.tanishq.fragments.Sub_Collection;
import com.feet.tanishq.fragments.User_Manual;
import com.feet.tanishq.fragments.Wish_List;
import com.feet.tanishq.interfaces.AdapterCallback;
import com.feet.tanishq.model.Model_Category;
import com.feet.tanishq.model.Model_Filter;
import com.feet.tanishq.model.Model_Params;
import com.feet.tanishq.model.Model_Product;
import com.feet.tanishq.model.Model_TopFilter;
import com.feet.tanishq.utils.AsifUtils;
import com.feet.tanishq.utils.AsyncTaskCompleteListener;
import com.feet.tanishq.utils.Const;
import com.feet.tanishq.utils.UserDetails;
import com.feet.tanishq.utils.VolleyHttpRequest;

import java.util.ArrayList;
import java.util.HashMap;

public class Tanishq_Screen extends CustomAppCompactActivity implements AsyncTaskCompleteListener,Response.ErrorListener,AdapterCallback{


    LinearLayout ll_display,ll_filter,ll_icon,ll_recycler,ll_selected_filters;
    ImageView iv_toggle,iv_collection,iv_wish,iv_compare,iv_help,iv_toggle_filter,iv_collection_icon,
            iv_category_icon,iv_material_icon,iv_occasion_icon,iv_logo3,iv_price_icon;
    TextView tv_welcome_user,tv_logout,tv_item_name,tv_wish_count,tv_compare_count;
    FrameLayout fl_fragment;
    Button bt_clear,bt_done;

    RecyclerView rv_cat_item,rv_selected_filter;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.LayoutManager filter_layoutManager;

    RequestQueue requestQueue;

    ArrayList<Model_Filter> arr_filter=new ArrayList<Model_Filter>();
    public static Activity activity;
     SQLiteDatabase db;


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

        iv_logo3=(ImageView) findViewById(R.id.iv_logo3);
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
        iv_price_icon=(ImageView) findViewById(R.id.iv_price_icon);

        tv_welcome_user=(TextView) findViewById(R.id.tv_welcome_user);
        tv_logout=(TextView) findViewById(R.id.tv_logout);
        tv_item_name=(TextView) findViewById(R.id.tv_item_name);
        tv_wish_count=(TextView) findViewById(R.id.tv_wish_count);
        tv_compare_count=(TextView) findViewById(R.id.tv_compare_count);

        tv_welcome_user.setTypeface(AsifUtils.getRaleWay_SemiBold(this));
        tv_logout.setTypeface(AsifUtils.getRaleWay_SemiBold(this));
        tv_item_name.setTypeface(AsifUtils.getRaleWay_Bold(this));
        tv_wish_count.setTypeface(AsifUtils.getRaleWay_Medium(this));
        tv_compare_count.setTypeface(AsifUtils.getRaleWay_Medium(this));


        bt_clear=(Button) findViewById(R.id.bt_clear);
        bt_done=(Button) findViewById(R.id.bt_done);

        bt_clear.setTypeface(AsifUtils.getRaleWay_Medium(this));
        bt_done.setTypeface(AsifUtils.getRaleWay_Medium(this));


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

        iv_logo3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoAllCollectionFragment();
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
                AlertDialog diaBox = AskOption();
                diaBox.show();
            }
        });

        bt_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeSlideWithAnim();
               if(arr_filter.size()>0){
                   new Handler().postDelayed(new Runnable() {
                       @Override
                       public void run() {
                           setUpFilterProducts();
                       }
                   },500);

               }

            }
        });

        fl_fragment=(FrameLayout) findViewById(R.id.fl_fragment);


        iv_collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoAllCollectionFragment();
            }
        });
        iv_wish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoWishListFragment();
            }
        });
        iv_compare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoCompareFragment();
            }
        });
        iv_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoHelpFragment();
            }
        });

        UserDetails user=new UserDetails(getApplicationContext());
        tv_welcome_user.setText("Welcome " + user.getUserName());

        gotoAllCollectionFragment();
        setUpFrameUI();
        resetCategory();
       checkForWish();
        checkForCompare();


    }

    private void checkForWish(){
        Intent intent=new Intent("filter");
        intent.putExtra("type", 3);
        intent.putExtra("notify", 1);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

    private void checkForCompare(){
        Intent intent=new Intent("filter");
        intent.putExtra("type", 3);
        intent.putExtra("notify", 2);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

    private void setUpFilterProducts() {
        HashMap<String,String> jewel_map=new HashMap<String,String>();//chains,bangles..
        HashMap<String,String> coll_map=new HashMap<String,String>();//zuhur,iva..
        HashMap<String,String> mat_map=new HashMap<String,String>();//gold,diamond..
        HashMap<String,String> occas_map=new HashMap<String,String>();//anniversary,valetine..
        HashMap<String,String> price_map=new HashMap<String,String>();//price..

        for(Model_Filter model:arr_filter){
            switch (model.getCat_id()){
                case "1":
                    jewel_map.put("cat_id",model.getCat_id());
                    jewel_map.put("id",model.getItem_id());
                    jewel_map.put("name", model.getItem_name());
                    Log.d("ttt", "setUpFilterProducts: "+jewel_map);
                break;
                case "2":
                    coll_map.put("cat_id",model.getCat_id());
                    coll_map.put("id",model.getItem_id());
                    coll_map.put("name",model.getItem_name());
                    Log.d("ttt", "setUpFilterProducts: " + coll_map);
                    break;
                case "3":
                    mat_map.put("cat_id",model.getCat_id());
                    mat_map.put("id",model.getItem_id());
                    mat_map.put("name",model.getItem_name());
                    Log.d("ttt", "setUpFilterProducts: " + mat_map);
                    break;
                case "4":
                    occas_map.put("cat_id",model.getCat_id());
                    occas_map.put("id",model.getItem_id());
                    occas_map.put("name",model.getItem_name());
                    Log.d("ttt", "setUpFilterProducts: " + occas_map);
                    break;
                case "5":
                    price_map.put("cat_id",model.getCat_id());
                    price_map.put("id",model.getItem_id());
                    price_map.put("name",model.getItem_name());
                    Log.d("ttt", "setUpFilterProducts: " + price_map);
                    break;
            }
        }

        Model_Params model_params=new Model_Params(coll_map,jewel_map,occas_map,mat_map,price_map);
        gotoFilterProductFragment(model_params);
    }

    BroadcastReceiver FilterRecyclerBroadcast=new BroadcastReceiver() {
            @Override
             public void onReceive(Context context, Intent intent) {
                int type=intent.getIntExtra("type", 0);
//                int position=intent.getIntExtra("position",0);

                switch (type){
                    case 0:
                        Model_Category model_category= (Model_Category) intent.getSerializableExtra("model");
                        deleteFilterRecycler(model_category);
                        break;
                    case 1:
                        Model_Category model_category2= (Model_Category) intent.getSerializableExtra("model");
                        addFilterRecycler(model_category2);
                        break;
                    case 2:
                        Model_Filter filter= (Model_Filter) intent.getSerializableExtra("model");
                        removeItemfromCategory(filter);
                        break;
                    case 3:
                        //for wish and compare counter
                        int notify=intent.getIntExtra("notify",1);

                        if (notify==1) {
                            //wish counter
                            if(getWishCount()>0){
                                tv_wish_count.setVisibility(View.VISIBLE);
                                tv_wish_count.setText(""+getWishCount());
                            }else{
                                tv_wish_count.setVisibility(View.GONE);
                            }
                        } else if(notify==2){
                            //compare counter
                            if(getCompareCount()>0){
                                tv_compare_count.setVisibility(View.VISIBLE);
                                tv_compare_count.setText(""+getCompareCount());
                            }else{
                                tv_compare_count.setVisibility(View.GONE);
                            }
                        }
                        break;
                }

//                if (type==0) {
//                    Model_Category model_category= (Model_Category) intent.getSerializableExtra("model");
//                    deleteFilterRecycler(model_category);
//                } else if(type==1){
//                    Model_Category model_category= (Model_Category) intent.getSerializableExtra("model");
//                    addFilterRecycler(model_category);
//                }else if (type==2){
//                    Model_Filter filter= (Model_Filter) intent.getSerializableExtra("model");
//                    removeItemfromCategory(filter);
//                }
            }
        };

    private long getWishCount() {
        long count = 0;
        try {
            db = openOrCreateDatabase(DataBaseHandler.DATABASE_NAME, Context.MODE_PRIVATE, null);
            count = DatabaseUtils.queryNumEntries(db, DataBaseHandler.TABLE_WISHLIST);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return (int) count;
    }
    private long getCompareCount() {
        long count = 0;
        try {
            db = openOrCreateDatabase(DataBaseHandler.DATABASE_NAME, Context.MODE_PRIVATE, null);
            count = DatabaseUtils.queryNumEntries(db, DataBaseHandler.TABLE_COMPARE);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return (int) count;
    }
    private synchronized void removeItemfromCategory(Model_Filter filter) {
        String cat_id= filter.getCat_id();
        String item_id=filter.getItem_id();

        if(arr_list.size()>0){
            for (int i=0;i<arr_list.size();i++){
                Model_Category model=arr_list.get(i);
                if(cat_id.matches(model.getCat_id())&&item_id.matches(model.getId())){
                    model.setIsSelected(false);
                    break;
                }
            }
        }
        catAdapter.notifyDataSetChanged();
        new UpdateCategforyAsync(cat_id,item_id,"0").execute();
    }


    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(FilterRecyclerBroadcast);
        super.onDestroy();
    }

    private void resetCategory() {
        Log.d("ttt", "resetCategory: ");
        try {
            db=openOrCreateDatabase(DataBaseHandler.DATABASE_NAME, MODE_PRIVATE, null);
            ContentValues values=new ContentValues();
            values.put("selected","0");
            db.update(DataBaseHandler.TABLE_CATEGORY,values,"",null);
        } finally {
            db.close();
        }
    }


    RecyclerView.Adapter filter_adapter;
    private synchronized void addFilterRecycler(Model_Category model_category){
        Model_Filter filter=new Model_Filter(model_category.getCat_id(),model_category.getId(),model_category.getName());
        arr_filter.add(filter);
        filter_adapter=new Filter_Adapter(this,arr_filter);
        rv_selected_filter.setAdapter(filter_adapter);
        filter_adapter.notifyDataSetChanged();
        new UpdateCategforyAsync(model_category.getCat_id(), model_category.getId(), "1").execute();
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
        new UpdateCategforyAsync(cat_id,item_id,"0").execute();
    }

    class UpdateCategforyAsync extends AsyncTask<Void,Void,Void>{
        String cat_id,item_id,value;

        UpdateCategforyAsync(String cat_id,String item_id,String value){
            this.cat_id=cat_id;
            this.item_id=item_id;
            this.value=value;

        }
        @Override
        protected Void doInBackground(Void... params) {
            updateCategoryTableSelected(cat_id,item_id,value);
            return null;
        }
    }

    private synchronized void updateCategoryTableSelected(String cat_id,String item_id,String value){

        try {
            db=openOrCreateDatabase(DataBaseHandler.DATABASE_NAME, MODE_PRIVATE, null);
            ContentValues values=new ContentValues();
            values.put("selected",value);
            db.update(DataBaseHandler.TABLE_CATEGORY,values,"cat_id = "+cat_id+" and item_id = "+item_id,null);
        } finally {
            db.close();
        }

    }


    private void setUpFrameUI(){

        iv_category_icon.setBackgroundColor(getResources().getColor(R.color.black_recyclelay));
        iv_collection_icon.setBackgroundColor(getResources().getColor(R.color.black_iconlay));
        iv_material_icon.setBackgroundColor(getResources().getColor(R.color.black_iconlay));
        iv_occasion_icon.setBackgroundColor(getResources().getColor(R.color.black_iconlay));
        iv_price_icon.setBackgroundColor(getResources().getColor(R.color.black_iconlay));
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
                    iv_price_icon.setBackgroundColor(getResources().getColor(R.color.black_iconlay));


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
                    iv_price_icon.setBackgroundColor(getResources().getColor(R.color.black_iconlay));

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
                    iv_price_icon.setBackgroundColor(getResources().getColor(R.color.black_iconlay));

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
                    iv_price_icon.setBackgroundColor(getResources().getColor(R.color.black_iconlay));
                }
            });

        iv_price_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new SetUpFrameFilters("5").execute();
                    iv_category_icon.setBackgroundColor(getResources().getColor(R.color.black_iconlay));
                    iv_collection_icon.setBackgroundColor(getResources().getColor(R.color.black_iconlay));
                    iv_material_icon.setBackgroundColor(getResources().getColor(R.color.black_iconlay));
                    iv_occasion_icon.setBackgroundColor(getResources().getColor(R.color.black_iconlay));
                    iv_price_icon.setBackgroundColor(getResources().getColor(R.color.black_recyclelay));
                }
            });

    }

    private void getValuesFromDb(String id){

        arr_list.clear();
        try {
            db=openOrCreateDatabase(DataBaseHandler.DATABASE_NAME,MODE_PRIVATE,null);

            Cursor cs=db.rawQuery("select * from "+DataBaseHandler.TABLE_CATEGORY+" where cat_id="+id,null);

            if (cs.moveToFirst()){
                do {
                    String item_id=cs.getString(cs.getColumnIndex("item_id"));
                    String item_name=cs.getString(cs.getColumnIndex("item_name"));
                    String cat_id=cs.getString(cs.getColumnIndex("cat_id"));
                    String cat_name=cs.getString(cs.getColumnIndex("cat_name"));
                    String selected=cs.getString(cs.getColumnIndex("selected"));
                    boolean isSelected=false;
                    if (selected.matches("1")){
                        isSelected=true;
                    }


                    Model_Category model=new Model_Category(cat_id,cat_name,item_id,item_name,isSelected);
                    arr_list.add(model);
                } while (cs.moveToNext());

            }
        } finally {
            db.close();
        }


    }



    ArrayList<Model_Category> arr_list=new ArrayList<Model_Category>();
    RecyclerView.Adapter catAdapter;

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
            catAdapter=new Category_Adapter(Tanishq_Screen.this,arr_list);
            rv_cat_item.setAdapter(catAdapter);
            catAdapter.notifyDataSetChanged();
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
                case "5":
                    tv_item_name.setText("PRICE");
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

        AsifUtils.start(this);
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
//            FragmentManager fragmentManager=getSupportFragmentManager();
//                for(int entry = 0; entry < fragmentManager.getBackStackEntryCount(); entry++){
//                    Log.i("ttt", "Found fragment: " + fragmentManager.getBackStackEntryAt(entry).getName());
//                }
            if (getSupportFragmentManager().getBackStackEntryCount() == 0||getSupportFragmentManager().getBackStackEntryCount() == 1) {
               this.finish();
                Log.d("ttt", "onBackPressed:11 "+getSupportFragmentManager().getBackStackEntryCount());
            } else {
               getSupportFragmentManager().popBackStack();
                Log.d("ttt", "onBackPressed:22 " + getSupportFragmentManager().getBackStackEntryCount());
            }

        }

    }


    private AlertDialog AskOption()
    {
        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(this)
                //set message, title, and icon
                .setTitle("Logout")
                .setMessage("Are you sure want to Logout?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        //your deleting code
                        callLogoutApi();
                        dialog.dismiss();
                    }

                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                })
                .create();
        return myQuittingDialogBox;

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
//    public boolean checkFragAvail(String tag){
//        boolean hasFrag=true;
//        for(int entry = 0; entry < getSupportFragmentManager().getBackStackEntryCount(); entry++){
//            Log.i("ttt", "Found fragment: " + getSupportFragmentManager().getBackStackEntryAt(entry).getName());
//            if (tag.matches(getSupportFragmentManager().getBackStackEntryAt(entry).getName())) {
//                hasFrag=false;
//                break;
//            }
//        }
//        return hasFrag;
//    }

    public void gotoHelpFragment(){
        Help_Fragment help_fragment=Help_Fragment.newInstance();
//        addFragment(help_fragment,checkFragAvail(Const.FRAG_HELP), Const.FRAG_HELP);
        addFragment(help_fragment,true, Const.FRAG_HELP);

    }
    public void gotoAllCollectionFragment(){
            All_Collection all_collectionFrag=All_Collection.newInstance();
//            addFragment(all_collectionFrag,checkFragAvail(Const.FRAG_All_COLL), Const.FRAG_All_COLL);
        clearBackStack();
        addFragment(all_collectionFrag, true, Const.FRAG_All_COLL);

    }
    public void gotoWishListFragment(){
        Wish_List wish_list=Wish_List.newInstance();
//        addFragment(wish_list,checkFragAvail(Const.FRAG_WISH_LIST), Const.FRAG_WISH_LIST);
        addFragment(wish_list,true, Const.FRAG_WISH_LIST);
    }

    public void gotoSub_CollectionFragment(String cat_id,String cat_name){
        Sub_Collection sub_collection=Sub_Collection.newInstance(cat_id,cat_name);
//        addFragment(sub_collection,checkFragAvail(Const.FRAG_SUB_COLL),Const.FRAG_SUB_COLL);
        addFragment(sub_collection,true,Const.FRAG_SUB_COLL);

    }

    public void gotoFilterProductFragment(Model_Params params){
        Filter_Products filter_products=Filter_Products.newInstance(params);
//        addFragment(filter_products,checkFragAvail(Const.FRAG_FILTER),Const.FRAG_FILTER);
        addFragment(filter_products,true,Const.FRAG_FILTER);
    }
    public void gotoCompareFragment(){

        Compare_List compare_list=Compare_List.newInstance();
//        addFragment(compare_list,checkFragAvail(Const.FRAG_COMPARE_LIST),Const.FRAG_COMPARE_LIST);
        addFragment(compare_list,true,Const.FRAG_COMPARE_LIST);

    }
    public void gotoPagerFilterProductFragment(int adapterPosition, ArrayList<Model_Product> arr_list, ArrayList<Model_TopFilter> arr_top){
        PagerFilter_Product pagerFilter_product=PagerFilter_Product.newInstance(adapterPosition,arr_list,arr_top);
//        addFragment(pagerFilter_product,checkFragAvail(Const.FRAG_PAGERFILTER),Const.FRAG_PAGERFILTER);
        addFragment(pagerFilter_product,true,Const.FRAG_PAGERFILTER);

    }
    public void gotoFeedBackFragment(){
        FeedBack feedBack=FeedBack.newInstance();
//        addFragment(feedBack,checkFragAvail(Const.FRAG_FEEDBACK),Const.FRAG_FEEDBACK);
        addFragment(feedBack,true,Const.FRAG_FEEDBACK);
    }
    public void gotoUserManualFragment(){
        User_Manual user_manual=User_Manual.newInstance();
//        addFragment(user_manual,checkFragAvail(Const.FRAG_USERMAN),Const.FRAG_USERMAN);
        addFragment(user_manual,true,Const.FRAG_USERMAN);
    }


    @Override
    public void onMethodCallback(int type) {
        switch (type) {
            case 1:
                gotoFeedBackFragment();
                break;
            case 2:
                gotoUserManualFragment();
                break;
            case 3:
                gotoAllCollectionFragment();
                break;

        }

    }

    @Override
    public void onMethodCallback(String cat_id,String cat_name) {
            gotoSub_CollectionFragment(cat_id,cat_name);
    }
    @Override
    public void onMethodCallbackArr(int adapterPosition, ArrayList<Model_Product> arr_list, ArrayList<Model_TopFilter> arr_top) {
        gotoPagerFilterProductFragment(adapterPosition,arr_list, arr_top);
    }
    @Override
    public void onMethodCallFilterProduct(Model_Params model_params) {
        gotoFilterProductFragment(model_params);
    }


    @Override
    public void onTaskCompleted(String response, int serviceCode) {
        switch (serviceCode){
            case Const.ServiceCode.LOGOUT:
                AsifUtils.stop();
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
        AsifUtils.stop();
        AsifUtils.validateResponse(this, error.getMessage());

    }
}
