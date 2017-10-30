package com.feet.tanishq.fragments;


import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.feet.tanishq.R;
import com.feet.tanishq.Tanishq_Screen;
import com.feet.tanishq.adapter.Filter_topPager_Adapter;
import com.feet.tanishq.adapter.ViewPagerAdapter;
import com.feet.tanishq.database.DataBaseHandler;
import com.feet.tanishq.interfaces.AdapterCallback;
import com.feet.tanishq.model.ModelStoreList;
import com.feet.tanishq.model.ModelTopFilterNew;
import com.feet.tanishq.model.Model_Product;
import com.feet.tanishq.utils.AsifUtils;
import com.feet.tanishq.utils.AsyncTaskCompleteListener;
import com.feet.tanishq.utils.Const;
import com.feet.tanishq.utils.Singleton_volley;
import com.feet.tanishq.utils.VolleyHttpRequest;
import com.google.android.gms.analytics.HitBuilders;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PagerFilter_Product#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PagerFilter_Product extends Fragment implements ViewPager.OnPageChangeListener, AsyncTaskCompleteListener, Response.ErrorListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String MODEL = "model";
    private static final String MODEL_PRO = "model_pro";
    private static final String PRO_POSITION = "position";

    // TODO: Rename and change types of parameters
    ArrayList<Model_Product> arr_product;
    ArrayList<ModelTopFilterNew> arr_filter;
    int current_poistion = 0;


    public PagerFilter_Product() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PagerFilter_Product.
     */
    // TODO: Rename and change types and number of parameters
//    public static PagerFilter_Product newInstance(String param1, String param2) {
//        PagerFilter_Product fragment = new PagerFilter_Product();
//        Bundle args = new Bundle();
//        args.putString(MODEL, param1);
//        args.putString(MODEL_PRO, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
    @Override
    public void onResume() {
        super.onResume();

        model_product=arr_product.get(current_poistion);

        if(model_product.getOnlineexclusive().equals("1")){
            iv_online_exclusive.setVisibility(View.VISIBLE);
        }else if(model_product.getOnlineexclusive().equals("0")){
            iv_online_exclusive.setVisibility(View.GONE);
        }


        try {
            checkForWishArr(model_product);
            setUpPagerView();
            checkForCompareArr(model_product);
            setUpPagerView();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static PagerFilter_Product newInstance(int adapterPosition, ArrayList<Model_Product> arr_list, ArrayList<ModelTopFilterNew> arr_top) {
        PagerFilter_Product fragment = new PagerFilter_Product();
        Bundle args = new Bundle();
        args.putInt(PRO_POSITION, adapterPosition);
        args.putSerializable(MODEL, arr_list);
        args.putSerializable(MODEL_PRO, arr_top);
        fragment.setArguments(args);
        return fragment;
    }

    RecyclerView rv_filter_pager;
    ViewPager vp_product;
    RelativeLayout rl_nowish;
    TextView tv_nowish;
    Button bt_check_availability;
    ImageView iv_wish_pro, iv_compare_pro, iv_back, iv_next, iv_online_exclusive;
    TextView tv_header_pager, tv_iv_count, tv_pro_name, tv_pro_price, tv_pro_price_name, tv_pro_material, tv_pro_category, tv_pro_collection, tv_pro_weight, tc_pro_kara, tv_community, tv_occasion, tv_pro_descp,tv_disclaimer;
    LinearLayoutManager layoutManager;
    ViewPagerAdapter viewPagerAdapter;
    Model_Product model_product;
    NetworkImageView iv_one, iv_two, iv_three, iv_four;
    ImageLoader imageLoader;
    RequestQueue requestQueue;
    AdapterCallback adapterCallback;

    ArrayList<ModelStoreList> arr_list_product = new ArrayList<ModelStoreList>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            arr_product = (ArrayList<Model_Product>) getArguments().getSerializable(MODEL);
            arr_filter = (ArrayList<ModelTopFilterNew>) getArguments().getSerializable(MODEL_PRO);
            current_poistion = getArguments().getInt(PRO_POSITION);
        }
        this.adapterCallback = ((AdapterCallback) getContext());
        imageLoader = Singleton_volley.getInstance().getImageLoader();
        requestQueue = Volley.newRequestQueue(getContext());
        Tanishq_Screen.tracker.setScreenName("Product Detail Screen");
        Tanishq_Screen.tracker.send(new HitBuilders.ScreenViewBuilder().build());
        adapterCallback.setFilterToGone();

    }

    ArrayList<String> arr_wish = new ArrayList<String>();
    ArrayList<String> arr_compare = new ArrayList<String>();

    private void checkForWishSelect() {
        //valid from wish table and update it in arr_list
        try {
            db = getActivity().openOrCreateDatabase(DataBaseHandler.DATABASE_NAME, Context.MODE_PRIVATE, null);
            Cursor cs = db.rawQuery("select product_title from wishlist", null);
            if (cs.moveToFirst()) {
                do {
                    String product_title = cs.getString(cs.getColumnIndex("product_title"));
                    arr_wish.add(product_title);
                } while (cs.moveToNext());

            }

            if (arr_product.size() > 0) {
                String value_wish = arr_product.get(current_poistion).getProduct_title();
                if (arr_wish.contains(value_wish)) {
                    arr_product.get(current_poistion).setInWish(true);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            iv_wish_pro.setBackgroundResource(R.color.green_fungus);
                        }
                    });
                } else {

                    arr_product.get(current_poistion).setInWish(false);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            iv_wish_pro.setBackgroundResource(R.color.tanishq_gold);
                        }
                    });
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

    }

    private void checkInCompareTable() {
        try {
            db = getActivity().openOrCreateDatabase(DataBaseHandler.DATABASE_NAME, Context.MODE_PRIVATE, null);
            Cursor cs = db.rawQuery("select product_title from compare", null);
            if(cs!=null){
                if (cs.moveToFirst()) {
                    do {
                        String product_title = cs.getString(cs.getColumnIndex("product_title"));
                        arr_compare.add(product_title);
                    } while (cs.moveToNext());

                }
            }


            for (String value : arr_compare) {
                if (arr_product.size() > 0) {
                    String value_compare = arr_product.get(current_poistion).getProduct_title();
                    if (value.matches(value_compare)) {
                        arr_product.get(current_poistion).setInCompare(true);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                iv_compare_pro.setBackgroundResource(R.color.green_fungus);
                            }
                        });
                    } else {

                        arr_product.get(current_poistion).setInWish(false);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                iv_wish_pro.setBackgroundResource(R.color.tanishq_gold);
                            }
                        });
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    private void setUpPagerView() {

        RecyclerView.Adapter adapter = new Filter_topPager_Adapter(getContext(), arr_filter);
        rv_filter_pager.setAdapter(adapter);

        viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager(), arr_product);
        vp_product.setAdapter(viewPagerAdapter);
        vp_product.setCurrentItem(current_poistion);
        vp_product.addOnPageChangeListener(this);
        viewPagerAdapter.notifyDataSetChanged();
        if (arr_product.size() > 0) {
            setUpText(arr_product.get(current_poistion));
        }
        visibleArrowButtons();
    }

    SQLiteDatabase db;
    List<String> arr_image = new ArrayList<>();
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    LinearLayout ll_images;
    FrameLayout fl_last;
    ImageView img_extra;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pager_filter__product, container, false);

        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rv_filter_pager = (RecyclerView) view.findViewById(R.id.rv_filter_pager);
        rv_filter_pager.setHasFixedSize(true);
        rv_filter_pager.setLayoutManager(layoutManager);

        vp_product = (ViewPager) view.findViewById(R.id.vp_product);

        iv_wish_pro = (ImageView) view.findViewById(R.id.iv_wish_pro);
        iv_online_exclusive = (ImageView) view.findViewById(R.id.iv_online_exclusive);
        iv_compare_pro = (ImageView) view.findViewById(R.id.iv_compare_pro);
        iv_back = (ImageView) view.findViewById(R.id.iv_back);
        iv_next = (ImageView) view.findViewById(R.id.iv_next);

        ll_images = (LinearLayout) view.findViewById(R.id.ll_images);
        fl_last = (FrameLayout) view.findViewById(R.id.fl_last);
        tv_iv_count = (TextView) view.findViewById(R.id.tv_iv_count);
        img_extra = (ImageView) view.findViewById(R.id.img_extra);

        iv_one = (NetworkImageView) view.findViewById(R.id.iv_one);
        iv_two = (NetworkImageView) view.findViewById(R.id.iv_two);
        iv_three = (NetworkImageView) view.findViewById(R.id.iv_three);
//        iv_four = (NetworkImageView) view.findViewById(R.id.iv_four);

        tv_header_pager = (TextView) view.findViewById(R.id.tv_header_pager);
        tv_pro_name = (TextView) view.findViewById(R.id.tv_pro_name);
        tv_pro_price = (TextView) view.findViewById(R.id.tv_pro_price);
        tv_pro_price_name = (TextView) view.findViewById(R.id.tv_pro_price_name);
        tv_pro_material = (TextView) view.findViewById(R.id.tv_pro_material);
        tv_pro_category = (TextView) view.findViewById(R.id.tv_pro_category);
        tv_pro_collection = (TextView) view.findViewById(R.id.tv_pro_collection);
        tv_disclaimer = (TextView) view.findViewById(R.id.tv_disclaimer);


        rl_nowish = (RelativeLayout) view.findViewById(R.id.rl_nowish);
        tv_nowish = (TextView) view.findViewById(R.id.tv_nowish);
        tv_nowish.setTypeface(AsifUtils.getRaleWay_Medium(getContext()));

        tv_pro_descp = (TextView) view.findViewById(R.id.tv_pro_descp);
        tc_pro_kara = (TextView) view.findViewById(R.id.tc_pro_kara);
        tv_pro_weight = (TextView) view.findViewById(R.id.tv_pro_weight);

        tv_occasion = (TextView) view.findViewById(R.id.tv_occasion);
        tv_community = (TextView) view.findViewById(R.id.tv_community);


        bt_check_availability = (Button) view.findViewById(R.id.bt_check_availability);

        tv_header_pager.setTypeface(AsifUtils.getRaleWay_Bold(getContext()));
        tv_pro_name.setTypeface(AsifUtils.getRaleWay_Medium(getContext()));


        tv_occasion.setTypeface(AsifUtils.getRaleWay_Medium(getContext()));
        tv_community.setTypeface(AsifUtils.getRaleWay_Medium(getContext()));
        tv_pro_price_name.setTypeface(AsifUtils.getRaleWay_Medium(getContext()));
        tv_pro_material.setTypeface(AsifUtils.getRaleWay_Medium(getContext()));
        tv_pro_category.setTypeface(AsifUtils.getRaleWay_Medium(getContext()));
        tv_pro_collection.setTypeface(AsifUtils.getRaleWay_Medium(getContext()));

        tv_pro_descp.setTypeface(AsifUtils.getRaleWay_Medium(getContext()));
        tc_pro_kara.setTypeface(AsifUtils.getRaleWay_Medium(getContext()));
        tv_pro_weight.setTypeface(AsifUtils.getRaleWay_Medium(getContext()));

        new CheckForWish().execute();


        bt_check_availability.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callStroreListApi(arr_product.get(current_poistion).getProduct_title());
            }
        });

        iv_wish_pro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (arr_product.size() > 0) {
                    model_product = arr_product.get(current_poistion);
                    Intent intent = new Intent("filter");
                    intent.putExtra("type", 3);
                    intent.putExtra("notify", 1);

                    if (model_product.isInWish()) {
                        model_product.setInWish(false);
                        iv_wish_pro.setBackgroundResource(R.color.tanishq_gold);
                        deleteFromWish(model_product.getProduct_title());
                        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
                    } else {
                        if (getWishCount() < 10) {
                            model_product.setInWish(true);
                            iv_wish_pro.setBackgroundResource(R.color.green_fungus);
                            insertIntoWishList(model_product);
                            LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
                        } else {
                            Toast.makeText(getContext(), "You have exceeded the maximum limit of 10 products in your wishlist", Toast.LENGTH_SHORT).show();
                        }
                    }


                }


            }
        });

        iv_compare_pro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (arr_product.size() > 0) {
                    model_product = arr_product.get(current_poistion);
                    Intent intent = new Intent("filter");
                    intent.putExtra("type", 3);
                    intent.putExtra("notify", 2);
                    if (model_product.isInCompare()) {
                        model_product.setInCompare(false);
                        iv_compare_pro.setBackgroundResource(R.color.tanishq_gold);
                        deleteFromCompare(model_product.getProduct_title());
                        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
                    } else {
                        if (getCompareCount() < 4) {
                            model_product.setInCompare(true);
                            iv_compare_pro.setBackgroundResource(R.color.green_fungus);
                            insertIntoCompare(model_product);
                            LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
                        } else {
                            Toast.makeText(getContext(), "You have exceeded the maximum limit of 4 products in the compare list", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
                viewPagerAdapter.notifyDataSetChanged();

            }
        });


        iv_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callImageDialog(model_product.getStringArrayUrlList(), 0);
            }
        });

        iv_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callImageDialog(model_product.getStringArrayUrlList(), 1);
            }
        });
        iv_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callImageDialog(model_product.getStringArrayUrlList(), 2);
            }
        });

        return view;
    }

    private void callStroreListApi(String product_title) {

        if (!AsifUtils.isNetworkAvailable(getActivity())) {
            Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
            return;
        }
//        AsifUtils.start(getContext());
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(Const.URL, Const.STORELIST_API_URL);
        params.put(Const.Params.SKU, product_title);
//        params.put(Const.Params.SKU, "123");
        Log.e("zzz", "callFilterProductApi: request----> " + params);
        requestQueue.add(new VolleyHttpRequest(Request.Method.GET, params, Const.ServiceCode.STORELIST, this, this));

    }


    private void callImageDialog(List<String> arr_image, int position) {


        DialogFragmentImage dialogFragmentImage = DialogFragmentImage.newInstance(arr_image, position, getResources().getString(R.string.all_collection));
        dialogFragmentImage.show(getFragmentManager(), "dialog");
    }

    private void visibleArrowButtons() {
        if (current_poistion != 0) {
            iv_back.setVisibility(View.VISIBLE);
        }

        if (current_poistion != arr_product.size() - 1) {
            iv_next.setVisibility(View.VISIBLE);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    hideArrowButtons();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 1000);

    }

    private void hideArrowButtons() {
        if(getContext()!=null){
            Animation fade_out = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out);
            if (current_poistion != 0) {
                iv_back.setAnimation(fade_out);
            }
            if (current_poistion != arr_product.size() - 1) {
                iv_next.setAnimation(fade_out);
            }
            iv_back.setVisibility(View.GONE);
            iv_next.setVisibility(View.GONE);
        }


    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {
        AsifUtils.stop();
        switch (serviceCode) {
            case Const.ServiceCode.STORELIST:
                AsifUtils.stop();
                if (AsifUtils.validateResponse(getContext(), response)) {
                    new ParseStoresList(response).execute();
                } else {
                    try {
                        callNoproductDialogue(new JSONObject(response).getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    private void callNoproductDialogue(String message) {
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(getContext())
                //set message, title, and icon
                .setMessage(message)
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }

                })
                .create();
        myQuittingDialogBox.show();
    }


    class CheckForWish extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            AsifUtils.stop();
            AsifUtils.start(getContext());
        }

        @Override
        protected Void doInBackground(Void... params) {

            if (getWishCount() > 0) {
                checkForWishSelect();
            }
            if (getCompareCount() > 0) {
                checkInCompareTable();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            setUpPagerView();
            AsifUtils.stop();
        }
    }

    public long getWishCount() {
        long count = 0;
        try {
            db = getActivity().openOrCreateDatabase(DataBaseHandler.DATABASE_NAME, Context.MODE_PRIVATE, null);
            count = DatabaseUtils.queryNumEntries(db, DataBaseHandler.TABLE_WISHLIST);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return (int) count;
    }

    public long getCompareCount() {
        long count = 0;
        try {
            db = getContext().openOrCreateDatabase(DataBaseHandler.DATABASE_NAME, Context.MODE_PRIVATE, null);
            count = DatabaseUtils.queryNumEntries(db, DataBaseHandler.TABLE_COMPARE);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return (int) count;
    }

    private synchronized void insertIntoWishList(Model_Product model_product) {

        try {
            Tanishq_Screen.reportEventToGoogle("Wishlist", "Add", model_product.getProduct_title());

            db = getActivity().openOrCreateDatabase(DataBaseHandler.DATABASE_NAME, Context.MODE_PRIVATE, null);
            ContentValues values = new ContentValues();
            values.put("device_image", model_product.getDevice_image());
            values.put("product_image", model_product.getProduct_image());
            values.put("product_title", model_product.getProduct_title());
            values.put("product_price", model_product.getProduct_price());
            values.put("discount_price", model_product.getDiscount_price());
            values.put("discount_percent", model_product.getDiscount_percent());

            values.put("description", model_product.getDescription());
            values.put("collection", model_product.getCollection());
            values.put("material", model_product.getMaterial());
            values.put("category", model_product.getCategory());

            values.put("product_url", model_product.getProduct_url());
            db.insert(DataBaseHandler.TABLE_WISHLIST, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

    }

    private synchronized void deleteFromWish(String product_title) {
        try {
            db = getActivity().openOrCreateDatabase(DataBaseHandler.DATABASE_NAME, Context.MODE_PRIVATE, null);
            db.delete(DataBaseHandler.TABLE_WISHLIST, "product_title = ?", new String[]{product_title});
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    private synchronized void deleteFromCompare(String product_title) {
        try {
            db = getActivity().openOrCreateDatabase(DataBaseHandler.DATABASE_NAME, Context.MODE_PRIVATE, null);
            db.delete(DataBaseHandler.TABLE_COMPARE, "product_title = ?", new String[]{product_title});
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    private synchronized void insertIntoCompare(Model_Product model_product) {
        try {

            Tanishq_Screen.reportEventToGoogle("Compare", "Add", model_product.getProduct_title());

            db = getActivity().openOrCreateDatabase(DataBaseHandler.DATABASE_NAME, Context.MODE_PRIVATE, null);
            ContentValues values = new ContentValues();

            values.put("device_image", model_product.getDevice_image());
            values.put("product_image", model_product.getProduct_image());
            values.put("product_title", model_product.getProduct_title());
            values.put("product_price", model_product.getProduct_price());
            values.put("discount_price", model_product.getDiscount_price());
            values.put("discount_percent", model_product.getDiscount_percent());

            values.put("description", model_product.getDescription());
            values.put("collection", model_product.getCollection());
            values.put("material", model_product.getMaterial());
            values.put("category", model_product.getCategory());

            values.put("product_url", model_product.getProduct_url());
            db.insert(DataBaseHandler.TABLE_COMPARE, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }


    private void setUpText(Model_Product model_product) {

        if(model_product.getProduct_title().equals(" ") | model_product.getProduct_title().equals("")){
            tv_pro_name.setVisibility(View.GONE);

        }else{
            tv_pro_name.setVisibility(View.VISIBLE);
            tv_pro_name.setText("SKU - " + model_product.getProduct_title());
        }

        if(model_product.getProduct_price().equals(" ") | model_product.getProduct_price().equals("")){
            tv_pro_price_name.setVisibility(View.GONE);
            tv_pro_price.setVisibility(View.GONE);

        }else{
            tv_pro_price_name.setVisibility(View.VISIBLE);
            tv_pro_price.setVisibility(View.VISIBLE);
            tv_pro_price_name.setText("Price - ");
//            tv_pro_price.setText(getResources().getString(R.string.rs) + " " + model_product.getProduct_price());
            tv_pro_price.setText(model_product.getProduct_price());
        }


        if(model_product.getMaterial().equals(" ") | model_product.getMaterial().equals("")){
            tv_pro_material.setVisibility(View.GONE);

        }else{
            tv_pro_material.setVisibility(View.VISIBLE);
            tv_pro_material.setText("Material - " + model_product.getMaterial());
        }


        if(model_product.getCollection().equals(" ") | model_product.getCollection().equals("")){
            tv_pro_collection.setVisibility(View.GONE);

        }else{
            tv_pro_collection.setVisibility(View.VISIBLE);
            tv_pro_collection.setText("Collection - " + model_product.getCollection());
        }


        if(model_product.getCategory().equals(" ") | model_product.getCategory().equals("")){
            tv_pro_category.setVisibility(View.GONE);

        }else{
            tv_pro_category.setVisibility(View.VISIBLE);
            tv_pro_category.setText("Category - " + model_product.getCategory());
        }

        if(model_product.getDisclaimer().equals(" ") | model_product.getDisclaimer().equals("")){
            tv_disclaimer.setVisibility(View.GONE);
            tv_header_pager.setVisibility(View.GONE);

        }else{
            tv_disclaimer.setVisibility(View.VISIBLE);
            tv_header_pager.setVisibility(View.VISIBLE);
            tv_disclaimer.setText("Disclaimer - " + model_product.getDisclaimer());
            tv_header_pager.setText(model_product.getCategory());
        }

        if(model_product.getDescription().equals(" ") | model_product.getDescription().equals("")){
            tv_pro_descp.setVisibility(View.GONE);

        }else{
            tv_pro_descp.setVisibility(View.VISIBLE);
//            tv_pro_descp.setText("Description - " + model_product.getDescription()+" "+model_product.getDescription());
            tv_pro_descp.setText("Description - " + model_product.getDescription());
        }


        if(model_product.getGold_karatage().equals(" ") | model_product.getGold_karatage().equals("")){
            tc_pro_kara.setVisibility(View.GONE);

        }else{
            tc_pro_kara.setVisibility(View.VISIBLE);
            tc_pro_kara.setText("Gold kartage - " + model_product.getGold_karatage());
        }


        if(model_product.getWeight().equals(" ") | model_product.getWeight().equals("")){
            tv_pro_weight.setVisibility(View.GONE);

        }else{
            tv_pro_weight.setVisibility(View.VISIBLE);
            tv_pro_weight.setText("Weight - " + model_product.getWeight());
        }

        if(model_product.getCommunity().equals(" ") | model_product.getCommunity().equals("")){
            tv_community.setVisibility(View.GONE);

        }else{
            tv_community.setVisibility(View.VISIBLE);
            tv_community.setText("Community - " + model_product.getCommunity());
        }

        if(model_product.getOccasion().equals(" ") | model_product.getOccasion().equals("")){
            tv_occasion.setVisibility(View.GONE);

        }else{
            tv_occasion.setVisibility(View.VISIBLE);
            tv_occasion.setText("Occasion - " + model_product.getOccasion());
        }
        if (model_product.getStringArrayUrlList() != null) {

            int size = model_product.getStringArrayUrlList().size();
            if (size > 0) {
                ll_images.setVisibility(View.VISIBLE);
                if (size>3){
                    tv_iv_count.setText(""+size);
                }

            }
            if (size == 1) {
                iv_one.setImageUrl(model_product.getStringArrayUrlList().get(0), imageLoader);
                iv_one.setVisibility(View.VISIBLE);
                iv_two.setVisibility(View.INVISIBLE);
                iv_three.setVisibility(View.INVISIBLE);
                fl_last.setVisibility(View.INVISIBLE);
                tv_iv_count.setVisibility(View.VISIBLE);
                img_extra.setVisibility(View.VISIBLE);
            } else if (size == 2) {
                iv_one.setVisibility(View.VISIBLE);
                iv_two.setVisibility(View.VISIBLE);
                iv_three.setVisibility(View.INVISIBLE);
                fl_last.setVisibility(View.INVISIBLE);
                tv_iv_count.setVisibility(View.VISIBLE);
                img_extra.setVisibility(View.VISIBLE);
                iv_one.setImageUrl(model_product.getStringArrayUrlList().get(0), imageLoader);
                iv_two.setImageUrl(model_product.getStringArrayUrlList().get(1), imageLoader);

            }
            else {
                iv_one.setVisibility(View.VISIBLE);
                iv_two.setVisibility(View.VISIBLE);
                iv_three.setVisibility(View.VISIBLE);
                fl_last.setVisibility(View.VISIBLE);
                if (size == 3) {
                    tv_iv_count.setVisibility(View.INVISIBLE);
                    img_extra.setVisibility(View.INVISIBLE);
                } else {
                    tv_iv_count.setVisibility(View.VISIBLE);
                    img_extra.setVisibility(View.VISIBLE);
                }
                iv_one.setImageUrl(model_product.getStringArrayUrlList().get(0), imageLoader);
                iv_two.setImageUrl(model_product.getStringArrayUrlList().get(1), imageLoader);
                iv_three.setImageUrl(model_product.getStringArrayUrlList().get(2), imageLoader);

//               iv_four.setImageUrl(data.getDishes_image_url() + Const.res + data.getDishes_images().get(3), imageLoader);
            }

        } else {
            ll_images.setVisibility(View.GONE);
        }

    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

        current_poistion = position;

        model_product=arr_product.get(current_poistion);


        iv_one.setImageUrl(model_product.getProduct_url(), imageLoader);
        iv_two.setImageUrl(model_product.getProduct_url(), imageLoader);
        iv_three.setImageUrl(model_product.getProduct_url(), imageLoader);


        if(model_product.getOnlineexclusive().equals("1")){
            iv_online_exclusive.setVisibility(View.VISIBLE);
        }else{
            iv_online_exclusive.setVisibility(View.GONE);
        }

        visibleArrowButtons();

        if (getWishCount() > 0) {
            checkForWishArr(model_product);
        }


        if (getCompareCount() > 0) {
            checkForCompareArr(model_product);

        }



        viewPagerAdapter.notifyDataSetChanged();
        setUpText(model_product);


    }

    @Override
    public void onPageScrollStateChanged(int state) {


    }


    private void checkForWishArr(Model_Product model_product) {

        arr_wish.clear();

        try {
            db = getContext().openOrCreateDatabase(DataBaseHandler.DATABASE_NAME, Context.MODE_PRIVATE, null);
            Cursor cs = db.rawQuery("select product_title from wishlist", null);
            if (cs.moveToFirst()) {
                do {
                    String product_title = cs.getString(cs.getColumnIndex("product_title"));
                    arr_wish.add(product_title);
                } while (cs.moveToNext());

            }

            if (arr_product.size() > 0) {
                String value_wish = model_product.getProduct_title();
                if (arr_wish.contains(value_wish)) {
                    model_product.setInWish(true);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            iv_wish_pro.setBackgroundResource(R.color.green_fungus);
                        }
                    });
                } else {
                    model_product.setInWish(false);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            iv_wish_pro.setBackgroundResource(R.color.tanishq_gold);
                        }
                    });
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

    }

    private void checkForCompareArr(Model_Product model_product) {

        arr_compare.clear();

        try {
            db = getContext().openOrCreateDatabase(DataBaseHandler.DATABASE_NAME, Context.MODE_PRIVATE, null);
            Cursor cs = db.rawQuery("select product_title from compare", null);
            if (cs.moveToFirst()) {
                do {
                    String product_title = cs.getString(cs.getColumnIndex("product_title"));
                    arr_compare.add(product_title);
                } while (cs.moveToNext());

            }

            if (arr_product.size() > 0) {
                String value_wish = model_product.getProduct_title();
                if (arr_compare.contains(value_wish)) {
                    model_product.setInCompare(true);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            iv_compare_pro.setBackgroundResource(R.color.green_fungus);
                        }
                    });
                } else {
                    model_product.setInCompare(false);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            iv_compare_pro.setBackgroundResource(R.color.tanishq_gold);
                        }
                    });
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }


    }

    private class ParseStoresList extends AsyncTask<Void, Void, Void> {

        String response;

        public ParseStoresList(String response) {
            this.response = response;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            AsifUtils.stop();
            AsifUtils.start(getContext());
            arr_list_product.clear();
            store_product_name = "";
        }

        @Override
        protected Void doInBackground(Void... params) {
            parseStore(response);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            StoreListDialog storeListDialog = StoreListDialog.newInstance(arr_list_product, store_product_name);
            storeListDialog.show(getFragmentManager(), "StoreListDialgue");
            Log.e("zzz", "onPostExecute: " + arr_list_product.get(0).getBoutique());
            AsifUtils.stop();
        }
    }

    String store_product_name;

    private void parseStore(String response) {
        try {
            JSONObject jObj = new JSONObject(response);
            JSONObject jResObj = jObj.getJSONObject("response");

            JSONObject jResObjStores = jResObj.getJSONObject("stores");
            store_product_name = jResObjStores.getString("productname");

            JSONArray jArrStoresList = jResObjStores.getJSONArray("storelist");
            int size = jArrStoresList.length();

            for (int i = 0; i < size; i++) {

                JSONObject obj = jArrStoresList.getJSONObject(i);
                String state = obj.getString("state");
                String unitwt = obj.getString("unitwt");
                String qty = obj.getString("qty");
                String lotnumber = obj.getString("lotnumber");
                String boutique = obj.getString("boutique");

                ModelStoreList modelStoreList = new ModelStoreList(boutique, lotnumber, qty, state, unitwt);

                arr_list_product.add(modelStoreList);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

