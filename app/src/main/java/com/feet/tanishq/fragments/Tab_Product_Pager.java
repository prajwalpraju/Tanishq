package com.feet.tanishq.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.feet.tanishq.R;
import com.feet.tanishq.model.Model_Product;
import com.feet.tanishq.utils.NetworkImageViews;
import com.feet.tanishq.utils.Singleton_volley;

/**
 * Created by asif on 19-04-2016.
 */

public class Tab_Product_Pager extends Fragment {

    int position;
    Model_Product model_product;
    ImageLoader imageLoader;

    public Tab_Product_Pager(){

    }

    public Tab_Product_Pager(int position,Model_Product model_product){
        this.position=position;
        this.model_product=model_product;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageLoader= Singleton_volley.getInstance().getImageLoader();
    }
    NetworkImageViews nv_pro_pager;
    ProgressBar pg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.product_image,container,false);
        nv_pro_pager=(NetworkImageViews) view.findViewById(R.id.nv_pro_pager);
        pg=(ProgressBar) view.findViewById(R.id.pg);
        try {
            imageLoader.get(model_product.getDevice_image(), new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    pg.setVisibility(View.GONE);
                }
                @Override
                public void onErrorResponse(VolleyError error) {
                    pg.setVisibility(View.GONE);
                }
            });
            nv_pro_pager.setImageUrl(model_product.getDevice_image(), imageLoader);
            pg.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
            pg.setVisibility(View.GONE);
        }

//        nv_pro_pager.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("ttt", "onClick: ");
////                nv_pro_pager.bringToFront();
//            }
//        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
//        try {
//            imageLoader.get(model_product.getDevice_image(), new ImageLoader.ImageListener() {
//                @Override
//                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
//                    pg.setVisibility(View.GONE);
//                }
//
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    pg.setVisibility(View.GONE);
//                }
//            });
//            nv_pro_pager.setImageUrl(model_product.getDevice_image(), imageLoader);
//            pg.setVisibility(View.VISIBLE);
//        } catch (Exception e) {
//            e.printStackTrace();
//            pg.setVisibility(View.GONE);
//        }
    }
}
