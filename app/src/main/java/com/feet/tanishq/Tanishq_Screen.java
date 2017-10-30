package com.feet.tanishq;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.feet.tanishq.adapter.NavigationListAdapter;
import com.feet.tanishq.database.DataBaseHandler;
import com.feet.tanishq.fragments.All_Collection;
import com.feet.tanishq.fragments.CatalogList;
import com.feet.tanishq.fragments.Compare_List;
import com.feet.tanishq.fragments.FeedBack;
import com.feet.tanishq.fragments.FilterDialog;
import com.feet.tanishq.fragments.Filter_Products;
import com.feet.tanishq.fragments.Help_Fragment;
import com.feet.tanishq.fragments.Mail_WishList;
import com.feet.tanishq.fragments.Main_Collection;
import com.feet.tanishq.fragments.PagerFilter_Product;
import com.feet.tanishq.fragments.Featured_Collection;
import com.feet.tanishq.fragments.SearchDialog;
import com.feet.tanishq.fragments.User_Manual;
import com.feet.tanishq.fragments.Wish_List;
import com.feet.tanishq.interfaces.AdapterCallback;
import com.feet.tanishq.model.ModelTopFilterNew;
import com.feet.tanishq.model.Model_Filter;
import com.feet.tanishq.model.Model_FilterOld;
import com.feet.tanishq.model.Model_Params;
import com.feet.tanishq.model.Model_Product;
import com.feet.tanishq.model.Model_TopFilter;
import com.feet.tanishq.utils.AsifUtils;
import com.feet.tanishq.utils.AsyncTaskCompleteListener;
import com.feet.tanishq.utils.Const;
import com.feet.tanishq.utils.Singleton_volley;
import com.feet.tanishq.utils.UserDetails;
import com.feet.tanishq.utils.VolleyHttpRequest;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Tanishq_Screen extends CustomAppCompactActivity implements AsyncTaskCompleteListener, Response.ErrorListener, AdapterCallback, AdapterView.OnItemClickListener {


    LinearLayout ll_filter, ll_filter_button,ll_search_button, ll_back_button;
    ImageView iv_toggle,
            iv_logo3;
    TextView tv_welcome_user, tv_top_dir_text, tv_back,tv_seach,tv_filter;
    FrameLayout fl_fragment;

    RequestQueue requestQueue;
    ImageLoader imageLoader;
    ListView nav_list;
    ArrayList<Model_FilterOld> arr_filter = new ArrayList<Model_FilterOld>();
    public static Activity activity;
    SQLiteDatabase db;

    static boolean active = false;

    public static Tracker tracker;
    public static Singleton_volley analyticsApplication;

    DrawerLayout drawer;
    UserDetails user;

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent=new Intent(this, RegisterToken.class);
        startService(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        activity = this;
        active = true;

        analyticsApplication = (Singleton_volley) getApplication();
        tracker = analyticsApplication.getDefaultTracker();
        tracker.setScreenName("Dashboard Screen");
//        Log.e("screen", "onCreate:-------------------> Dashboard Screen");
        tracker.send(new HitBuilders.ScreenViewBuilder().build());

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        nav_list = (ListView) findViewById(R.id.navigation_list);

        ll_filter_button = (LinearLayout) findViewById(R.id.ll_filter_button);
        ll_search_button = (LinearLayout) findViewById(R.id.ll_search_button);
        ll_back_button = (LinearLayout) findViewById(R.id.ll_back_button);

        tv_top_dir_text = (TextView) findViewById(R.id.tv_top_dir_text);
        tv_top_dir_text.setTypeface(AsifUtils.getRaleWay_SemiBold(this));



        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_back.setTypeface(AsifUtils.getRaleWay_SemiBold(this));
        tv_filter = (TextView) findViewById(R.id.tv_filter);
        tv_seach = (TextView) findViewById(R.id.tv_seach);
        tv_filter.setTypeface(AsifUtils.getRaleWay_SemiBold(this));
        tv_seach.setTypeface(AsifUtils.getRaleWay_SemiBold(this));
        ll_back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        View headerView = LayoutInflater.from(this).inflate(R.layout.main_nav_header, null);
        tv_welcome_user = (TextView) headerView.findViewById(R.id.tv_nv_welcome_user);

        tv_welcome_user.setTypeface(AsifUtils.getRaleWay_SemiBold(this));

        nav_list.addHeaderView(headerView);
        nav_list.setOnItemClickListener(this);


        requestQueue = Volley.newRequestQueue(this);
        imageLoader = Singleton_volley.getInstance().getImageLoader();
        LocalBroadcastManager.getInstance(this).registerReceiver(FilterRecyclerBroadcast, new IntentFilter("filter"));


        iv_logo3 = (ImageView) findViewById(R.id.iv_logo3);
        iv_toggle = (ImageView) findViewById(R.id.iv_toggle);


        iv_toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
            }
        });

        iv_logo3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ll_filter_button.setVisibility(View.GONE);
                gotoMainCollectionFragment();
            }
        });
        fl_fragment = (FrameLayout) findViewById(R.id.fl_fragment);

        user = new UserDetails(getApplicationContext());
        tv_welcome_user.setText("Welcome " + user.getUserName());

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String category_id = bundle.getString("featured_category_id");
            String contenttype = bundle.getString("content_type");
            String category_name = bundle.getString("cata_name");
            String hasfilter = bundle.getString("hasfilter");
            String filtered_id = bundle.getString("filtered_id");


            onFeaturedMethodCallback(category_id, contenttype, category_name, hasfilter, filtered_id);

        } else {
            ll_filter_button.setVisibility(View.GONE);
            gotoMainCollectionFragment();
        }


        updateNavigationList(1, String.valueOf(getWishCount()), 2, String.valueOf(getCompareCount()));
        ll_filter_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callFilterDialog();
            }

        });

        ll_search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callSearchDialog();
            }

        });


//        CallAutoLogOut();

//        user = new UserDetails(this);
//        String response = user.getMainDashboadResponse();

//        CheckForUpdate(response);

//        openUpdateDialogue();

        UserDetails userDetails = new UserDetails(getApplicationContext());
        Log.d("AutoLogout", "AutoLogout push value: "+userDetails.getPush() );
        if (userDetails.getPush()){

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    callLogoutApi();
                    Toast.makeText(getApplicationContext(),"Session Expired. Please log in again. . .!", Toast.LENGTH_LONG).show();
                }
            },1000);
            user.setPush(false);
        }


    }




//    private void CallAutoLogOut() {
//
//        boolean alarmUp = (PendingIntent.getBroadcast(this, 0,
//                new Intent("myactionalarm"),PendingIntent.FLAG_NO_CREATE) != null);
//
//        Intent alaramIntent = new Intent("myactionalarm");
//        alaramIntent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alaramIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(System.currentTimeMillis());
//        calendar.set(Calendar.HOUR, 3);
//        calendar.set(Calendar.MINUTE, 0);
//        calendar.set(Calendar.SECOND, 0);
//        calendar.set(Calendar.AM_PM, Calendar.PM);
//
//        AlarmManager alarmManager = (AlarmManager) this.getSystemService(ALARM_SERVICE);
////        if (alarmUp)
////        {
////            Log.e("alaram", "Alarm is already active");
////        }else {
//            Log.e("alaram", "Alarm is set..!"+calendar.getTime());
////            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
////            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 2*60*1000, pendingIntent);
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),pendingIntent);
//            }
//
////        }
//
//    }



    private void CheckForUpdate(String response) {
        try {
            JSONObject jObj=new JSONObject(response);
            JSONObject jsonObject = jObj.getJSONObject("response");
            String app_versionName = jsonObject.getString("app_versionName");
            int app_versionCode = jsonObject.getInt("app_versionCode");
            int current = user.getAppVertion();
            if (app_versionCode>current){
                createAlertNotification();
                user.setAppVertion(app_versionCode);
            }
//            int current2 = user.getAppVertion();
//            Log.e("ttt","current code = "+current+"app vertion code = "+app_versionCode+" currnt after = "+current2);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    AlertDialog b;
    NotificationManager notificationManager;
    NotificationCompat.Builder mbBuilder;


    public void gotoPlayStore() {
        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    public void createAlertNotification() {
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mbBuilder = new NotificationCompat.Builder(this);
        Intent intent = new Intent(this, UpdateActivity.class);
        intent.putExtra("notification_tag", true);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, 0);
        long[] pattern = {0, 300, 1000};
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mbBuilder.setSmallIcon(R.drawable.app_icon)
                .setOnlyAlertOnce(true)
                .setSmallIcon(R.drawable.app_icon)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.app_icon))
                .setColor(getResources().getColor(R.color.tanishq_gold))
                .setContentTitle("Tanishq galleria update")
                .setAutoCancel(true)
                .setSound(uri)
                .setContentText("Touch here to update the app to new version").setVibrate(pattern)
                .setContentIntent(contentIntent);
        notificationManager.notify(1, mbBuilder.build());
    }

    public void openUpdateDialogue() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = Tanishq_Screen.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.update_dialog, null);
        dialogBuilder.setView(dialogView);

        Button bt_update = (Button) dialogView.findViewById(R.id.bt_update);
        Button bt_cancel = (Button) dialogView.findViewById(R.id.bt_cancel);
        ImageView iv_close_dialogue = (ImageView) dialogView.findViewById(R.id.iv_close_dialogue);
        b = dialogBuilder.create();

        bt_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoPlayStore();
                b.dismiss();
            }
        });
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.dismiss();
            }
        });
        iv_close_dialogue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.dismiss();
            }
        });
        b.show();
    }


    public void callSearchDialog() {
        SearchDialog searchDialog = SearchDialog.newInstance();
        searchDialog.show(getSupportFragmentManager(), "SearchDialg");
    }

    public void callFilterDialog() {
        FilterDialog filterDialog = FilterDialog.newInstance();
        filterDialog.show(getSupportFragmentManager(), "FilterDialg");
    }


    public void updateNavigationList(int wishlistpostion, String wishlisttext, int comparepostion, String comparetext) {
        int[] navigationListIcons = {

                R.drawable.material,
                R.drawable.wishlist,
                R.drawable.compare,
                R.drawable.help,
                R.drawable.logout_icon,
        };
        String[] navigationListItems = getResources().getStringArray(R.array.navigation_list_items);
        NavigationListAdapter adapter = new NavigationListAdapter(this, navigationListIcons, navigationListItems, wishlistpostion, wishlisttext, comparepostion, comparetext);
        nav_list.setAdapter(adapter);
        adapter.notifyDataSetChanged();


    }

    public static void reportEventToGoogle(String category, String action, String label) {
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory(category)
                .setAction(action)
                .setLabel(label)
                .build());
    }


    BroadcastReceiver FilterRecyclerBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int type = intent.getIntExtra("type", 0);

            switch (type) {
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    Model_FilterOld filter = (Model_FilterOld) intent.getSerializableExtra("model");
                    break;
                case 3:
                    updateNavigationList(1, String.valueOf(getWishCount()), 2, String.valueOf(getCompareCount()));
                    break;
            }
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

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(FilterRecyclerBroadcast);
        active = false;
        super.onDestroy();
    }

    private void resetCategory() {
//        Log.d("ttt", "resetCategory: ");
        try {
            db = openOrCreateDatabase(DataBaseHandler.DATABASE_NAME, MODE_PRIVATE, null);
            ContentValues values = new ContentValues();
            values.put("selected", "0");
            db.update(DataBaseHandler.TABLE_CATEGORY, values, "", null);
        } finally {
            db.close();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {

            case 1:
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                ll_filter_button.setVisibility(View.GONE);
                gotoMainCollectionFragment();
                break;
            case 2:
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                ll_filter_button.setVisibility(View.GONE);
                gotoWishListFragment();
                break;
            case 3:
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                ll_filter_button.setVisibility(View.GONE);
                gotoCompareFragment();
                break;
            case 4:
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                ll_filter_button.setVisibility(View.GONE);
                gotoHelpFragment();
                break;
            case 5:
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                AlertDialog diaBox = AskOption();
                diaBox.show();
                break;
        }
    }

    private synchronized void updateCategoryTableSelected(String cat_id, String item_id, String value) {

        try {
            db = openOrCreateDatabase(DataBaseHandler.DATABASE_NAME, MODE_PRIVATE, null);
            ContentValues values = new ContentValues();
            values.put("selected", value);
            db.update(DataBaseHandler.TABLE_CATEGORY, values, "cat_id = " + cat_id + " and item_id = " + item_id, null);
        } finally {
            db.close();
        }
    }


    private void callLogoutApi() {
        if (!AsifUtils.isNetworkAvailable(this)) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
            return;
        }
        AsifUtils.start(this);
        UserDetails user = new UserDetails(this);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(Const.URL, Const.LOGOUT);
        params.put(Const.Params.USERNAME, user.getUserName());
        params.put(Const.Params.MOBILE, user.getMobileNumber());
        requestQueue.add(new VolleyHttpRequest(Request.Method.POST, params, Const.ServiceCode.LOGOUT, this, this));
    }

    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;


    @Override
    public void onBackPressed() {


        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (getSupportFragmentManager().getBackStackEntryCount() == 2) {
            ll_filter_button.setVisibility(View.GONE);
            ll_back_button.setVisibility(View.GONE);
            ll_search_button.setVisibility(View.GONE);
            tv_top_dir_text.setVisibility(View.GONE);
            getSupportFragmentManager().popBackStack();
        } else {
            ll_back_button.setVisibility(View.VISIBLE);
            ll_search_button.setVisibility(View.VISIBLE);
            if (getSupportFragmentManager().getBackStackEntryCount() == 0 || getSupportFragmentManager().getBackStackEntryCount() == 1) {
                ll_back_button.setVisibility(View.GONE);
                ll_search_button.setVisibility(View.GONE);

                if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {

                    super.onBackPressed();
                    this.finish();

                } else {
                    Toast.makeText(getBaseContext(), "Press again to exit", Toast.LENGTH_SHORT).show();
                }

                mBackPressed = System.currentTimeMillis();
//                Log.e("kkk", "onBackPressed:11 " + getSupportFragmentManager().getBackStackEntryCount());

            } else {

                getSupportFragmentManager().popBackStack();
//                Log.e("kkk", "onBackPressed:22 " + getSupportFragmentManager().getBackStackEntryCount());
            }

        }
    }


    private AlertDialog AskOption() {
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(this)
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

    public void gotoHelpFragment() {
        Help_Fragment help_fragment = Help_Fragment.newInstance();
        tv_top_dir_text.setVisibility(View.GONE);
        ll_back_button.setVisibility(View.VISIBLE);
        ll_search_button.setVisibility(View.VISIBLE);
        addFragment(help_fragment, true, Const.FRAG_HELP);
    }

    public void gotoAllCollectionFragment(String catId, String content_type, String hasfilter, String name, String filterparameter,String dir) {
        tv_top_dir_text.setVisibility(View.VISIBLE);
        All_Collection all_collectionFrag = All_Collection.newInstance(catId, content_type, hasfilter, name, filterparameter,dir);
        addFragment(all_collectionFrag, true, name);

    }

    public void gotoMainCollectionFragment() {
        ll_back_button.setVisibility(View.GONE);
        ll_search_button.setVisibility(View.GONE);
        tv_top_dir_text.setVisibility(View.GONE);
        Main_Collection main_collectionFrag = Main_Collection.newInstance();
        clearBackStack();
        user.ClearPreservedArrayAndPosition();
        addFragment(main_collectionFrag, true, Const.FRAG_MAIN_COLL);

    }

    public void gotoWishListFragment() {
        tv_top_dir_text.setVisibility(View.GONE);
        ll_back_button.setVisibility(View.VISIBLE);
        ll_search_button.setVisibility(View.VISIBLE);
        Wish_List wish_list = Wish_List.newInstance();
        addFragment(wish_list, false, Const.FRAG_WISH_LIST);
    }

    public void gotoFeatured_CollectionFragment(String category_id, String contenttype, String category_name, String hasfilter, String filtered_id) {
        tv_top_dir_text.setVisibility(View.GONE);
        ll_back_button.setVisibility(View.GONE);
        ll_search_button.setVisibility(View.GONE);
        Featured_Collection featured_collection = Featured_Collection.newInstance(category_id, contenttype, category_name, hasfilter, filtered_id);
        addFragment(featured_collection, true, Const.FRAG_SUB_COLL);

    }

    public void gotoFilterProductFragment(List<Model_Filter.FilterInfo> selected_arr, String producstype) {
        tv_top_dir_text.setVisibility(View.VISIBLE);
        Filter_Products filter_products = Filter_Products.newInstance(selected_arr, producstype);
        addFragment(filter_products, true, Const.FRAG_FILTER);
    }

    public void gotoSearchFragment(String name,boolean fromsearch) {
        tv_top_dir_text.setVisibility(View.VISIBLE);
        Filter_Products filter_products = Filter_Products.newInstance(name,fromsearch);
//        addFragment(filter_products, true, Const.FRAG_SEARCH);
        addFragment(filter_products, true, Const.FRAG_FILTER);
    }

    public void gotoFilterProductFragmentByClick(String name, String filterparameter, boolean FromProductClick,String itemId, String parentname,String dir) {
        tv_top_dir_text.setVisibility(View.VISIBLE);
        Filter_Products filter_products = Filter_Products.newInstance(name, filterparameter, FromProductClick,itemId,parentname,dir);
//        addFragment(filter_products, true, Const.FRAG_FILTER_FROM_CLICK);
        addFragment(filter_products, true, Const.FRAG_FILTER);
    }


    public void gotoCatalogueslist(String name, String id, String parentname) {
        tv_top_dir_text.setVisibility(View.VISIBLE);
        CatalogList catalogList = CatalogList.newInstance(name, id, parentname);
        addFragment(catalogList, true, Const.FRAG_FILTER_FROM_CLICK);
    }


    public void gotoCompareFragment() {
        tv_top_dir_text.setVisibility(View.GONE);
        ll_back_button.setVisibility(View.VISIBLE);
        ll_search_button.setVisibility(View.VISIBLE);
        Compare_List compare_list = Compare_List.newInstance();
        addFragment(compare_list, true, Const.FRAG_COMPARE_LIST);

    }

    public void gotoPagerFilterProductFragment(int adapterPosition, ArrayList<Model_Product> arr_list, ArrayList<ModelTopFilterNew> arr_top) {
        tv_top_dir_text.setVisibility(View.VISIBLE);
        PagerFilter_Product pagerFilter_product = PagerFilter_Product.newInstance(adapterPosition, arr_list, arr_top);
        addFragment(pagerFilter_product, true, Const.FRAG_PAGERFILTER);

    }

    public void gotoFeedBackFragment() {
        tv_top_dir_text.setVisibility(View.GONE);
        FeedBack feedBack = FeedBack.newInstance();
        addFragment(feedBack, true, Const.FRAG_FEEDBACK);
    }

    public void gotoMailWishFragment() {
        tv_top_dir_text.setVisibility(View.GONE);
        Mail_WishList mail_wishList = Mail_WishList.newInstance();
        addFragment(mail_wishList, true, Const.FRAG_MAIL);
    }

    public void gotoUserManualFragment() {
        tv_top_dir_text.setVisibility(View.GONE);
        User_Manual user_manual = User_Manual.newInstance();
        addFragment(user_manual, true, Const.FRAG_USERMAN);
    }


    @Override
    public void onMethodCallback(int type) {
        switch (type) {
            case 1:
                ll_filter_button.setVisibility(View.GONE);
                gotoFeedBackFragment();
                break;
            case 2:
                ll_filter_button.setVisibility(View.GONE);
                gotoUserManualFragment();
                break;
            case 3:
                ll_filter_button.setVisibility(View.GONE);
                gotoMainCollectionFragment();
                break;
            case 4:
                ll_filter_button.setVisibility(View.GONE);
                gotoMailWishFragment();
                break;

        }

    }

    @Override
    public void setFilterToVisable() {
        ll_back_button.setVisibility(View.VISIBLE);
        ll_search_button.setVisibility(View.VISIBLE);
        ll_filter_button.setVisibility(View.VISIBLE);
    }

    @Override
    public void setFilterToGone() {
        ll_filter_button.setVisibility(View.GONE);
        ll_search_button.setVisibility(View.GONE);
    }

    @Override
    public void setBreadcrumb(String name) {
        tv_top_dir_text.setVisibility(View.VISIBLE);
        tv_top_dir_text.setText(name);
    }

    @Override
    public void seachIten(String name,boolean fromsearch) {
        gotoSearchFragment(name,fromsearch);

    }

    @Override
    public void callFilterProduct(List<Model_Filter.FilterInfo> selected_arr, String producstype) {
        gotoFilterProductFragment(selected_arr, producstype);
    }

    @Override
    public void callFilterProductByItemClick(String name, String filterparameter, boolean FromProductClick,String itemId,String parentname,String dir) {
        gotoFilterProductFragmentByClick(name, filterparameter, FromProductClick,itemId,parentname,dir);
    }

    @Override
    public void callGetCatalogueslist(String name, String id,String parentname) {
        gotoCatalogueslist(name, id, parentname);
    }


    @Override
    public void onMethodCallback(String cat_id, String content_type, String hasfilter, String name, String filterparameter,String dir) {
        gotoAllCollectionFragment(cat_id, content_type, hasfilter, name, filterparameter,dir);
    }

    @Override
    public void onFeaturedMethodCallback(String category_id, String contenttype, String category_name, String hasfilter, String filtered_id) {
        gotoFeatured_CollectionFragment(category_id, contenttype, category_name, hasfilter, filtered_id);

    }


    @Override
    public void onMethodCallbackArr(int adapterPosition, ArrayList<Model_Product> arr_list, ArrayList<ModelTopFilterNew> arr_top) {
        Model_Product model_product = arr_list.get(adapterPosition);

        int size = arr_list.size();
        ArrayList<Model_Product> dataList = new ArrayList<>();
        for(int i = 0; i < size ; i++)
        {
            Model_Product model_product2 = arr_list.get(i);
            if(model_product2.getProduct_title().equals(""))
            {
                continue;
            }
            else
            {
                dataList.add(model_product2);
            }
        }


        for (int i = 0; i < dataList.size(); i++){
            Model_Product model_product2 = dataList.get(i);

            if (model_product.getProduct_title().matches(model_product2.getProduct_title()))
            {
                adapterPosition = i;
                break;
            }
        }

//        Log.e("ttt", "adapter position "+adapterPosition );

        gotoPagerFilterProductFragment(adapterPosition, dataList, arr_top);
    }

    @Override
    public void onMethodCallFilterProduct(Model_Params model_params) {
    }

    @Override
    public void onItemClick(View view, int position) {

    }


    @Override
    public void onTaskCompleted(String response, int serviceCode) {
        AsifUtils.stop();
        switch (serviceCode) {
            case Const.ServiceCode.LOGOUT:
                if (AsifUtils.validateResponse(getApplicationContext(), response)) {
                    user.clearUserPreference();
                    Intent intent = new Intent(getApplicationContext(), Login_Screen.class);
                    startActivity(intent);
                    finish();
                }


        }
    }


    @Override
    public void onErrorResponse(VolleyError error) {
        AsifUtils.stop();
        AsifUtils.validateResponse(this, error.getMessage());

    }
}
