package com.feet.tanishq.fragments;


import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.feet.tanishq.R;
import com.feet.tanishq.Tanishq_Screen;
import com.feet.tanishq.adapter.FilterTop_Adapter;
import com.feet.tanishq.adapter.Filter_topPager_Adapter;
import com.feet.tanishq.adapter.ViewPagerAdapter;
import com.feet.tanishq.database.DataBaseHandler;
import com.feet.tanishq.model.Model_Product;
import com.feet.tanishq.model.Model_TopFilter;
import com.feet.tanishq.utils.AsifUtils;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PagerFilter_Product#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PagerFilter_Product extends Fragment implements ViewPager.OnPageChangeListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String MODEL = "model";
    private static final String MODEL_PRO = "model_pro";
    private static final String PRO_POSITION = "position";

    // TODO: Rename and change types of parameters
    ArrayList<Model_Product> arr_product;
    ArrayList<Model_TopFilter> arr_filter;
    int current_poistion=0;


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

    public static PagerFilter_Product newInstance(int adapterPosition, ArrayList<Model_Product> arr_list, ArrayList<Model_TopFilter> arr_top){
        PagerFilter_Product fragment = new PagerFilter_Product();
        Bundle args = new Bundle();
        args.putInt(PRO_POSITION,adapterPosition);
        args.putSerializable(MODEL,arr_list);
        args.putSerializable(MODEL_PRO,arr_top);
        fragment.setArguments(args);
        return fragment;
    }

    RecyclerView rv_filter_pager;
    ViewPager vp_product;
    ImageView iv_wish_pro,iv_compare_pro,iv_back,iv_next;
    TextView tv_header_pager,tv_pro_name,tv_pro_price,tv_pro_material,tv_pro_category,tv_pro_collection,tv_pro_weight,tc_pro_kara,tv_pro_descp;
    LinearLayoutManager layoutManager;
    ViewPagerAdapter viewPagerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            arr_product = (ArrayList<Model_Product>) getArguments().getSerializable(MODEL);
            arr_filter = (ArrayList<Model_TopFilter>) getArguments().getSerializable(MODEL_PRO);
            current_poistion=getArguments().getInt(PRO_POSITION);
        }

    }
        ArrayList<String> arr_wish=new ArrayList<String>();
        ArrayList<String> arr_compare=new ArrayList<String>();
    private void checkForWishSelect() {
        //valid from wish table and update it in arr_list
        try {
            db=getActivity().openOrCreateDatabase(DataBaseHandler.DATABASE_NAME,Context.MODE_PRIVATE,null);
            Cursor cs=db.rawQuery("select product_title from wishlist",null);
            if(cs.moveToFirst()){
                do {
                   String product_title=cs.getString(cs.getColumnIndex("product_title"));
                    arr_wish.add(product_title);
                } while (cs.moveToNext());

            }

            for (String value:arr_wish){
                if (arr_product.size()>0){
                    String value_wish=arr_product.get(current_poistion).getProduct_title();
                    if(value.matches(value_wish)){
                        arr_product.get(current_poistion).setInWish(true);
                        getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    iv_wish_pro.setBackgroundResource(R.color.green_fungus);
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

//        for(String value:arr_str){
//            for (int i=0;i<arr_product.size();i++){
//                String valu2=arr_product.get(i).getProduct_title();
//                if(value.matches(valu2)){
//                    arr_product.get(i).setInWish(true);
//                    do{
//                        getActivity().runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                iv_wish_pro.setBackgroundResource(R.color.green_fungus);
//                            }
//                        });
//
//                    }while (i==0);
//                    break;
//                }
//            }
//
//        }

    }

    private void checkInCompareTable(){
        try {
            db=getActivity().openOrCreateDatabase(DataBaseHandler.DATABASE_NAME,Context.MODE_PRIVATE,null);
            Cursor cs=db.rawQuery("select product_title from compare",null);
            if(cs.moveToFirst()){
                do {
                    String product_title=cs.getString(cs.getColumnIndex("product_title"));
                    arr_compare.add(product_title);
                } while (cs.moveToNext());

            }

            for (String value:arr_compare){
                if (arr_product.size()>0){
                    String value_compare=arr_product.get(current_poistion).getProduct_title();
                    if(value.matches(value_compare)){
                        arr_product.get(current_poistion).setInCompare(true);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                iv_compare_pro.setBackgroundResource(R.color.green_fungus);
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

        RecyclerView.Adapter adapter=new Filter_topPager_Adapter(getContext(),arr_filter);
        rv_filter_pager.setAdapter(adapter);

//        for (Model_TopFilter filter:arr_filter){
//            switch (filter.getCat_id()){
//                case "1":
//                    tv_pro_category.setText(filter.getName());
//                    tv_header_pager.setText(filter.getName());
//                    break;
//                case "2":
//                    tv_pro_collection.setText(filter.getName());
//                    break;
//                case "3":
//                    tv_pro_material.setText(filter.getName());
//                    break;
//                case "4":
//                    break;
//            }
//        }

        viewPagerAdapter=new ViewPagerAdapter(getFragmentManager(),arr_product);
        vp_product.setAdapter(viewPagerAdapter);
        vp_product.setCurrentItem(current_poistion);
        vp_product.addOnPageChangeListener(this);
        viewPagerAdapter.notifyDataSetChanged();
        if (arr_product.size()>0){
            setUpText(arr_product.get(current_poistion));
        }
        visibleArrowButtons();
    }

    SQLiteDatabase db;
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_pager_filter__product, container, false);

        layoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        rv_filter_pager=(RecyclerView) view.findViewById(R.id.rv_filter_pager);
        rv_filter_pager.setHasFixedSize(true);
        rv_filter_pager.setLayoutManager(layoutManager);

        vp_product=(ViewPager) view.findViewById(R.id.vp_product);

        iv_wish_pro=(ImageView) view.findViewById(R.id.iv_wish_pro);
        iv_compare_pro=(ImageView) view.findViewById(R.id.iv_compare_pro);
        iv_back=(ImageView) view.findViewById(R.id.iv_back);
        iv_next=(ImageView) view.findViewById(R.id.iv_next);

        tv_header_pager=(TextView) view.findViewById(R.id.tv_header_pager);
        tv_pro_name=(TextView) view.findViewById(R.id.tv_pro_name);
        tv_pro_price=(TextView) view.findViewById(R.id.tv_pro_price);
        tv_pro_material=(TextView) view.findViewById(R.id.tv_pro_material);
        tv_pro_category=(TextView) view.findViewById(R.id.tv_pro_category);
        tv_pro_collection=(TextView) view.findViewById(R.id.tv_pro_collection);

        tv_pro_descp=(TextView) view.findViewById(R.id.tv_pro_descp);
        tc_pro_kara=(TextView) view.findViewById(R.id.tc_pro_kara);
        tv_pro_weight=(TextView) view.findViewById(R.id.tv_pro_weight);

        tv_header_pager.setTypeface(AsifUtils.getRaleWay_Bold(getContext()));
        tv_pro_name.setTypeface(AsifUtils.getRaleWay_Medium(getContext()));
        tv_pro_price.setTypeface(AsifUtils.getRaleWay_Medium(getContext()));
        tv_pro_material.setTypeface(AsifUtils.getRaleWay_Medium(getContext()));
        tv_pro_category.setTypeface(AsifUtils.getRaleWay_Medium(getContext()));
        tv_pro_collection.setTypeface(AsifUtils.getRaleWay_Medium(getContext()));

        tv_pro_descp.setTypeface(AsifUtils.getRaleWay_Medium(getContext()));
        tc_pro_kara.setTypeface(AsifUtils.getRaleWay_Medium(getContext()));
        tv_pro_weight.setTypeface(AsifUtils.getRaleWay_Medium(getContext()));

        new CheckForWish().execute();

        iv_wish_pro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (arr_product.size() > 0) {
                    Model_Product model_product = arr_product.get(current_poistion);
                    Intent intent = new Intent("filter");
                    intent.putExtra("type", 3);
                    intent.putExtra("notify", 1);

                    if (model_product.isInWish()) {
                        model_product.setInWish(false);
                        iv_wish_pro.setBackgroundResource(R.color.tanishq_gold);
                        deleteFromWish(model_product.getProduct_title());
                    } else {
                        model_product.setInWish(true);
                        iv_wish_pro.setBackgroundResource(R.color.green_fungus);
                        insertIntoWishList(model_product);
                    }
                    LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
                }

            }
        });

        iv_compare_pro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (arr_product.size() > 0) {
                    Model_Product model_product = arr_product.get(current_poistion);
                    Intent intent = new Intent("filter");
                    intent.putExtra("type", 3);
                    intent.putExtra("notify", 2);
                    if (model_product.isInCompare()) {
                        model_product.setInCompare(false);
                        iv_compare_pro.setBackgroundResource(R.color.tanishq_gold);
                        deleteFromCompare(model_product.getProduct_title());
                    } else {
                        model_product.setInCompare(true);
                        iv_compare_pro.setBackgroundResource(R.color.green_fungus);
                        insertIntoCompare(model_product);
                    }
                    LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
                }
            }
        });


        return view;
    }

    private void visibleArrowButtons(){
//        Log.d("ttt", "visibleArrowButtons: "+current_poistion);
        if (current_poistion!=0) {
            iv_back.setVisibility(View.VISIBLE);
        }

        if (current_poistion!=arr_product.size()-1) {
            iv_next.setVisibility(View.VISIBLE);
        }
//            Animation fade_in= AnimationUtils.loadAnimation(getContext(),R.anim.fade_in);
//            iv_back.setAnimation(fade_in);
//            iv_next.setAnimation(fade_in);
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
    private void hideArrowButtons(){
        Animation fade_out= AnimationUtils.loadAnimation(getContext(),R.anim.fade_out);
        if (current_poistion!=0) {
            iv_back.setAnimation(fade_out);
        }
        if (current_poistion!=arr_product.size()-1) {
            iv_next.setAnimation(fade_out);
        }
        iv_back.setVisibility(View.GONE);
        iv_next.setVisibility(View.GONE);

    }





    class CheckForWish extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            AsifUtils.start(getContext());
        }

        @Override
        protected Void doInBackground(Void... params) {

            if (getWishCount()>0) {
                checkForWishSelect();
            }
            if (getCompareCount()>0) {
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

    private long getWishCount() {
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

    private long getCompareCount(){
        long count = 0;
        try {
            db = getActivity().openOrCreateDatabase(DataBaseHandler.DATABASE_NAME, Context.MODE_PRIVATE, null);
            count = DatabaseUtils.queryNumEntries(db, DataBaseHandler.TABLE_COMPARE);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return (int) count;
    }

    private synchronized void insertIntoWishList(Model_Product model_product){

        try {
            db=getActivity().openOrCreateDatabase(DataBaseHandler.DATABASE_NAME, Context.MODE_PRIVATE, null);
            ContentValues values=new ContentValues();
            values.put("device_image",model_product.getDevice_image());
            values.put("product_image",model_product.getProduct_image());
            values.put("product_title",model_product.getProduct_title());
            values.put("product_price",model_product.getProduct_price());
            values.put("discount_price",model_product.getDiscount_price());
            values.put("discount_percent",model_product.getDiscount_percent());

            values.put("description",model_product.getDescription());
            values.put("collection",model_product.getCollection());
            values.put("material",model_product.getMaterial());
            values.put("category",model_product.getCategory());

            values.put("product_url",model_product.getProduct_url());
            db.insert(DataBaseHandler.TABLE_WISHLIST, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

    }

    private synchronized void deleteFromWish(String product_title){
        try {
            db=getActivity().openOrCreateDatabase(DataBaseHandler.DATABASE_NAME, Context.MODE_PRIVATE, null);
            db.delete(DataBaseHandler.TABLE_WISHLIST, "product_title = ?", new String[]{product_title});
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    private synchronized void deleteFromCompare(String product_title) {
        try {
            db=getActivity().openOrCreateDatabase(DataBaseHandler.DATABASE_NAME, Context.MODE_PRIVATE, null);
            db.delete(DataBaseHandler.TABLE_COMPARE, "product_title = ?", new String[]{product_title});
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    private synchronized void insertIntoCompare(Model_Product model_product){
        try {
            db=getActivity().openOrCreateDatabase(DataBaseHandler.DATABASE_NAME, Context.MODE_PRIVATE, null);
            ContentValues values=new ContentValues();

            values.put("device_image",model_product.getDevice_image());
            values.put("product_image",model_product.getProduct_image());
            values.put("product_title",model_product.getProduct_title());
            values.put("product_price",model_product.getProduct_price());
            values.put("discount_price",model_product.getDiscount_price());
            values.put("discount_percent",model_product.getDiscount_percent());

            values.put("description",model_product.getDescription());
            values.put("collection",model_product.getCollection());
            values.put("material",model_product.getMaterial());
            values.put("category",model_product.getCategory());

            values.put("product_url",model_product.getProduct_url());
            db.insert(DataBaseHandler.TABLE_COMPARE, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }




    private void setUpText(Model_Product model_product){

        tv_pro_name.setText("SKU - "+model_product.getProduct_title());
        tv_pro_price.setText("Price - "+getResources().getString(R.string.rs)+" "+model_product.getProduct_price());
        tv_pro_material.setText("Material - "+model_product.getMaterial());
        tv_pro_collection.setText("Collection - "+model_product.getCollection());
        tv_pro_category.setText("Category - "+model_product.getCategory());
        tv_header_pager.setText(model_product.getCategory());

        tv_pro_descp.setText("Description - "+model_product.getDescription());
        tc_pro_kara.setText("Gold kartage - "+model_product.getGold_karatage());
        tv_pro_weight.setText("Weight - "+model_product.getWeight());

    }
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        Model_Product model_product=arr_product.get(position);

        current_poistion=position;
        visibleArrowButtons();
        checkForWishArr(model_product);
        checkForCompareArr(model_product);

        if (model_product.isInWish()) {
            iv_wish_pro.setBackgroundResource(R.color.green_fungus);
        } else {
            iv_wish_pro.setBackgroundResource(R.color.tanishq_gold);
        }

        if(model_product.isInCompare()){
            iv_compare_pro.setBackgroundResource(R.color.green_fungus);
        }else {
            iv_compare_pro.setBackgroundResource(R.color.tanishq_gold);
        }
//        viewPagerAdapter.notifyDataSetChanged();
        setUpText(model_product);


    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    private synchronized void checkForWishArr(Model_Product model_product){
        String title=model_product.getProduct_title();
        for (String value:arr_wish){
            if(value.matches(title)){
                model_product.setInWish(true);
                break;
            }
        }
    }

    private synchronized void checkForCompareArr(Model_Product model_product){
        String title=model_product.getProduct_title();
        for (String value:arr_compare){
            if(value.matches(title)){
                model_product.setInWish(true);
                break;
            }
        }
    }

}
