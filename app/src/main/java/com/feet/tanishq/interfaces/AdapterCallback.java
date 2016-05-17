package com.feet.tanishq.interfaces;

import com.feet.tanishq.model.Model_Params;
import com.feet.tanishq.model.Model_Product;
import com.feet.tanishq.model.Model_TopFilter;

import java.util.ArrayList;

/**
 * Created by asif on 19-04-2016.
 */
public interface AdapterCallback {
    void onMethodCallback(int type);
    void onMethodCallback(String cat_id,String cat_name);
    void onMethodCallbackArr(int adapterPosition, ArrayList<Model_Product> arr_list, ArrayList<Model_TopFilter> arr_top);
    void onMethodCallFilterProduct(Model_Params model_params);
}
