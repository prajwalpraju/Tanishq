package com.feet.tanishq.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.feet.tanishq.R;
import com.feet.tanishq.model.Model_Product;
import com.feet.tanishq.utils.Singleton_volley;

/**
 * Created by asif on 19-04-2016.
 */
public class Tab_Product_Pager extends Fragment {

    int position;
    Model_Product model_product;
    ImageLoader imageLoader;

    public Tab_Product_Pager(int position,Model_Product model_product){
        this.position=position;
        this.model_product=model_product;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageLoader= Singleton_volley.getInstance().getImageLoader();
    }
    NetworkImageView nv_pro_pager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.product_image,container,false);
        nv_pro_pager=(NetworkImageView) view.findViewById(R.id.nv_pro_pager);
        try {
            nv_pro_pager.setImageUrl(model_product.getProduct_image(),imageLoader);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }
}
