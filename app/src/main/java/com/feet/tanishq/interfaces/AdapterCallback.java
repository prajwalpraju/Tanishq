package com.feet.tanishq.interfaces;

import android.view.View;

import com.feet.tanishq.model.CollectionList_Model;
import com.feet.tanishq.model.ModelTopFilterNew;
import com.feet.tanishq.model.Model_Filter;
import com.feet.tanishq.model.Model_Params;
import com.feet.tanishq.model.Model_Product;
import com.feet.tanishq.model.Model_TopFilter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asif on 19-04-2016.
 */
public interface AdapterCallback {
    void onMethodCallback(int type);
    void setFilterToVisable();
    void setFilterToGone();
    void setBreadcrumb(String name);
    void seachIten(String name,boolean fromsearch);

    void callFilterProduct(List<Model_Filter.FilterInfo> selected_arr, String producstype);
    void callFilterProductByItemClick(String name, String filterparameter,boolean FromProductClick,String itemid,String parentname,String dir);
    void callGetCatalogueslist(String name, String id,String parent);
    //    void callGetCatalogueslist(ArrayList<CollectionList_Model> arrayList);
    void onMethodCallback(String cat_id,String content_type,String hasfilter,String name,String filterparameter,String dir);
    void onFeaturedMethodCallback(String category_id,String contenttype,String category_name,String hasfilter,String filtered_id);
    void onMethodCallbackArr(int adapterPosition, ArrayList<Model_Product> arr_list, ArrayList<ModelTopFilterNew> arr_top);
    void onMethodCallFilterProduct(Model_Params model_params);
    void onItemClick(View view, int position);
}
