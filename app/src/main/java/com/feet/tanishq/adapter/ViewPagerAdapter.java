package com.feet.tanishq.adapter;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.feet.tanishq.fragments.Tab_Product_Pager;
import com.feet.tanishq.model.Model_Product;

import java.util.ArrayList;

/**
 * Created by asif on 19-04-2016.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    ArrayList<Model_Product> arr_product;

    public ViewPagerAdapter(FragmentManager fm,ArrayList<Model_Product> arr_product) {
        super(fm);
        this.arr_product=arr_product;
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public Fragment getItem(int position) {
        Model_Product model_product=arr_product.get(position);
        Tab_Product_Pager tab_product_pager=new Tab_Product_Pager(position,model_product);
        return tab_product_pager;
    }

    @Override
    public int getCount() {
        return arr_product.size();
    }
}
