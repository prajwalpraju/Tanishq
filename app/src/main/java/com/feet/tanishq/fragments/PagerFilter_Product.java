package com.feet.tanishq.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.feet.tanishq.R;
import com.feet.tanishq.adapter.FilterTop_Adapter;
import com.feet.tanishq.adapter.ViewPagerAdapter;
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

    // TODO: Rename and change types of parameters
    ArrayList<Model_Product> arr_product;
    ArrayList<Model_TopFilter> arr_filter;


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

    public static PagerFilter_Product newInstance(ArrayList<Model_Product> arr_list,ArrayList<Model_TopFilter> arr_top){
        PagerFilter_Product fragment = new PagerFilter_Product();
        Bundle args = new Bundle();
        args.putSerializable(MODEL,arr_list);
        args.putSerializable(MODEL_PRO,arr_top);
        fragment.setArguments(args);
        return fragment;
    }

    RecyclerView rv_filter_pager;
    ViewPager vp_product;
    ImageView iv_wish_pro,iv_compare_pro;
    TextView tv_header_pager,tv_pro_name,tv_pro_price,tv_pro_material,tv_pro_category,tv_pro_collection;
    LinearLayoutManager layoutManager;
    ViewPagerAdapter viewPagerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            arr_product = (ArrayList<Model_Product>) getArguments().getSerializable(MODEL);
            arr_filter = (ArrayList<Model_TopFilter>) getArguments().getSerializable(MODEL_PRO);
        }

    }

    private void setUpPagerView() {

        RecyclerView.Adapter adapter=new FilterTop_Adapter(getContext(),arr_filter);
        rv_filter_pager.setAdapter(adapter);

        for (Model_TopFilter filter:arr_filter){
            switch (filter.getCat_id()){
                case "1":
                    tv_pro_category.setText(filter.getName());
                    tv_header_pager.setText(filter.getName());
                    break;
                case "2":
                    tv_pro_collection.setText(filter.getName());
                    break;
                case "3":
                    tv_pro_material.setText(filter.getName());
                    break;
                case "4":
                    break;
            }
        }

        viewPagerAdapter=new ViewPagerAdapter(getFragmentManager(),arr_product);
        vp_product.setAdapter(viewPagerAdapter);
        vp_product.addOnPageChangeListener(this);
        if (arr_product.size()>0){
            setUpText(arr_product.get(0));
        }
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

        tv_header_pager=(TextView) view.findViewById(R.id.tv_header_pager);
        tv_pro_name=(TextView) view.findViewById(R.id.tv_pro_name);
        tv_pro_price=(TextView) view.findViewById(R.id.tv_pro_price);
        tv_pro_material=(TextView) view.findViewById(R.id.tv_pro_material);
        tv_pro_category=(TextView) view.findViewById(R.id.tv_pro_category);
        tv_pro_collection=(TextView) view.findViewById(R.id.tv_pro_collection);

        tv_header_pager.setTypeface(AsifUtils.getRaleWay_Bold(getContext()));
        tv_pro_name.setTypeface(AsifUtils.getRaleWay_Medium(getContext()));
        tv_pro_price.setTypeface(AsifUtils.getRaleWay_Medium(getContext()));
        tv_pro_material.setTypeface(AsifUtils.getRaleWay_Medium(getContext()));
        tv_pro_category.setTypeface(AsifUtils.getRaleWay_Medium(getContext()));
        tv_pro_collection.setTypeface(AsifUtils.getRaleWay_Medium(getContext()));

        setUpPagerView();

        return view;
    }

    private void setUpText(Model_Product model_product){

        tv_pro_name.setText(model_product.getProduct_title());
        tv_pro_price.setText("Rs. "+model_product.getProduct_price());


    }
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        Model_Product model_product=arr_product.get(position);
        setUpText(model_product);

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
