package com.feet.tanishq.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.feet.tanishq.R;
import com.feet.tanishq.Tanishq_Screen;
import com.feet.tanishq.interfaces.AdapterCallback;
import com.feet.tanishq.model.Model_Filter;
import com.feet.tanishq.model.Model_Filter_Gson;
import com.feet.tanishq.utils.Filterlist_array;
import com.google.android.gms.analytics.HitBuilders;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.android.volley.VolleyLog.TAG;

/**
 * Created by Administrator on 4/17/2017.
 */


public class FilterDialog extends DialogFragment implements View.OnClickListener {

    //initializing

    Gson gson = new GsonBuilder().create();
    ArrayAdapter<String> itemsAdapter;
    RecyclerView rv_main_cat;

    LinearLayout ll_manual_price;
    EditText et_min, et_max;


    RecyclerView rv_cat_items;
    RecyclerView rv_selected_filter;
    ImageView close;
    Button bt_done, bt_price_ok;
    String value;
    TextView tv_price;
    String producstype;
    ArrayList<String> items = new ArrayList<>();
    ArrayList<String> selectionType = new ArrayList<>();
    SharedPreferences preferences;
    Model_Filter model_filter;
    Filter_main_adapter filter_main_adapter;
    Filter_cat_adapter filter_cat_adapter;
    Selecteds_item_adapter selecteds_item_adapter;
    List<Model_Filter.FilterInfo> selected_arr = new ArrayList<>();
    List<Model_Filter.FilterInfo> array;

    LinearLayoutManager linearLayoutManager, linearLayoutManager1, linearLayoutManager2;

    boolean isClickFirstItem = true;

    public static FilterDialog newInstance() {
        FilterDialog filterDialog = new FilterDialog();
        return filterDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getActivity().getSharedPreferences("filter", Context.MODE_PRIVATE);
        value = preferences.getString("filter_arr", null);
        Log.e("ggg", "filter----------> " + value);
        try {
            producstype =  new JSONObject(value).getString("producstype");
            Log.e("ttt", "product  type----------------> "+producstype );
        } catch (JSONException e) {
            e.printStackTrace();
        }
        model_filter = gson.fromJson(value, Model_Filter.class);
        for (int i = 0; i < model_filter.getResponseList().size(); i++) {
            items.add(model_filter.getResponseList().get(i).getTitle());
            selectionType.add(model_filter.getResponseList().get(i).getSelectiontype());
        }


        Tanishq_Screen.tracker.setScreenName("Filter screen");
        Log.e("screen", "onCreate:--------------------> Filter screen" );
        Tanishq_Screen.tracker.send(new HitBuilders.ScreenViewBuilder().build());

    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        View v = inflater.inflate(R.layout.dialog_filter, container, false);


        rv_main_cat = (RecyclerView) v.findViewById(R.id.rv_main_cat);
        rv_cat_items = (RecyclerView) v.findViewById(R.id.rv_cat_items);
        rv_selected_filter = (RecyclerView) v.findViewById(R.id.rv_selected_filter);

        ll_manual_price = (LinearLayout) v.findViewById(R.id.ll_manual_price);
        et_min = (EditText) v.findViewById(R.id.et_min);
        et_max = (EditText) v.findViewById(R.id.et_max);
        bt_price_ok = (Button) v.findViewById(R.id.bt_price_ok);
        tv_price = (TextView) v.findViewById(R.id.tv_price);

        bt_price_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(et_min.getText().toString().trim())) {
                    Toast.makeText(getContext(), "Please enter minimum price...!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(et_max.getText().toString().trim())) {
                    Toast.makeText(getContext(), "Please enter maximum price...!", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    if (Integer.parseInt(et_min.getText().toString()) >= Integer.parseInt(et_max.getText().toString())) {
                        Toast.makeText(getContext(), "Please provide proper price range...!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                if(checkPriceValue("pricebar")){
                    String price = et_min.getText().toString()+"-"+et_max.getText().toString();
                    Model_Filter.FilterInfo filterInfo=new Model_Filter.FilterInfo(price,price,"0","0","pricebar","",true);
                    selected_arr.add(filterInfo);
                    selecteds_item_adapter.notifyDataSetChanged();
                }else {
                    String price = et_min.getText().toString()+"-"+et_max.getText().toString();
                    Model_Filter.FilterInfo filterInfo=new Model_Filter.FilterInfo(price,price,"0","0","pricebar","",true);
                    selected_arr.add(filterInfo);
                    selecteds_item_adapter.notifyDataSetChanged();
                }



            }
        });


        rv_main_cat.setHasFixedSize(true);
        rv_cat_items.setHasFixedSize(true);
        rv_selected_filter.setHasFixedSize(true);

        bt_done = (Button) v.findViewById(R.id.bt_done);

        bt_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selected_arr != null && selected_arr.size()>0 ) {
                    callSellectedFilterApi();
                }
                dismiss();
            }
        });

        close = (ImageView) v.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        ArrayList<Filterlist_array> array = new ArrayList<Filterlist_array>();
        selected_arr.clear();
        for (int i = 0; i < items.size(); i++) {

            Filterlist_array filterlist_array = new Filterlist_array(items.get(i), false, selectionType.get(i));
            array.add(filterlist_array);
            List<Model_Filter.FilterInfo> cat_arr = model_filter.getResponseList().get(i).getFilterInfoList();
            for(int j=0;j< cat_arr.size();j++){
                if (cat_arr.get(j).getChecked().equals("1")){
                    cat_arr.get(j).setSelect(true);
                    selected_arr.add( cat_arr.get(j));
                    Log.e("selected_array", "onCreateView:-----------------> "+selected_arr.size() );
                }
            }
        }


        filter_main_adapter = new Filter_main_adapter(getActivity(), array);

        linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        linearLayoutManager1 = new LinearLayoutManager(getActivity().getBaseContext());
        linearLayoutManager2 = new LinearLayoutManager(getActivity().getBaseContext());

        linearLayoutManager.setAutoMeasureEnabled(false);
        linearLayoutManager1.setAutoMeasureEnabled(false);
        linearLayoutManager2.setAutoMeasureEnabled(false);

        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager2.setOrientation(LinearLayoutManager.VERTICAL);

        rv_main_cat.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        rv_cat_items.setLayoutManager(linearLayoutManager1);
        rv_selected_filter.setLayoutManager(linearLayoutManager2);
        rv_main_cat.setAdapter(filter_main_adapter);
        return v;
    }

    private  boolean checkPriceValue(String pricebar){

        for (int i=0;i<selected_arr.size();i++){
            if (selected_arr.get(i).getMainfilter().equals("pricebar"))
            {
                selected_arr.get(i).setSelect(false);
                filter_cat_adapter.notifyDataSetChanged();
                selected_arr.remove(i);
                return true;
            }
        }
        return false;
    }

    private void callSellectedFilterApi() {

        String selectedNames = "";

        for(int i=0;i<selected_arr.size();i++){
            if(i==0){
                selectedNames = selected_arr.get(i).getFiltername();
            }else {
                selectedNames = selectedNames+","+selected_arr.get(i).getFiltername();
            }

        }
//        if (!TextUtils.isEmpty(tv_price.getText().toString()) && !TextUtils.isEmpty(et_max.getText().toString())){
////            Tanishq_Screen.reportEventToGoogle("Filter", "select", selectedNames);
//
//            AdapterCallback adapterCallback=((AdapterCallback)this.getContext());
//
//            adapterCallback.callFilterProduct(selected_arr,producstype,et_min.getText().toString()+"-"+et_max.getText().toString());
//        }
//        else {
        Tanishq_Screen.reportEventToGoogle("Filter", "select", selectedNames);

        AdapterCallback adapterCallback=((AdapterCallback)this.getContext());

        adapterCallback.callFilterProduct(selected_arr,producstype);
//        }




    }

    @Override
    public void onClick(View v) {

    }




    //-------------------FilterMainAdapter---------------------//
    class Filter_main_adapter extends RecyclerView.Adapter<Filter_main_adapter.MyViewHolder> {

        Context context;
        ArrayList<Filterlist_array> arrayMain;

        public Filter_main_adapter(Context context, ArrayList<Filterlist_array> filterlist_array) {
            this.context = context;
            this.arrayMain = filterlist_array;
        }

        @Override
        public Filter_main_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.filter_dialog_main_list_item, parent, false);
            return new Filter_main_adapter.MyViewHolder(view);
        }


        @Override
        public void onBindViewHolder(final Filter_main_adapter.MyViewHolder holder, final int position) {

            Filterlist_array filterlist_array = arrayMain.get(position);
            holder.tv_filter_dailog_main_item.setText(filterlist_array.getValue());


            if (filterlist_array.isSelected()) {
                holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.bt_dark_gold));
            } else {
                holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.black_iconlay));
            }



            if (isClickFirstItem) {
                if (position == 0) {
                    holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.bt_dark_gold));
                    List<Model_Filter.FilterInfo> cat_arr = model_filter.getResponseList().get(position).getFilterInfoList();
                    filter_cat_adapter = new Filter_cat_adapter(getContext(), cat_arr, filterlist_array.getSelectiontype());
                    rv_cat_items.setAdapter(filter_cat_adapter);
                }
            }


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Filterlist_array optionArrPriceCheck = arrayMain.get(position);

                    if (optionArrPriceCheck.getValue().equals("Pricebar")){
                        ll_manual_price.setVisibility(View.VISIBLE);
                    }else {
                        ll_manual_price.setVisibility(View.GONE);
                    }


                    int  lastFirstVisiblePosition = ((LinearLayoutManager)rv_main_cat.getLayoutManager()).findFirstCompletelyVisibleItemPosition();


                    isClickFirstItem = false;
                    try {
                        Filterlist_array optionArr = arrayMain.get(position);
                        if (!optionArr.isSelected()) {
                            for (int i = 0; i < arrayMain.size(); i++) {
                                Filterlist_array optionArr1 = arrayMain.get(i);

                                if (optionArr1.isSelected()) {
                                    optionArr1.setSelected(false);
                                    break;
                                }
                            }
                            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
                            arrayMain.get(holder.getAdapterPosition()).setSelected(true);
                            notifyDataSetChanged();
//                            callrecyclerViewCatagory(position);
                            List<Model_Filter.FilterInfo> cat_arr = model_filter.getResponseList().get(position).getFilterInfoList();

                            filter_cat_adapter = new Filter_cat_adapter(getContext(), cat_arr, optionArr.getSelectiontype());
                            rv_cat_items.setAdapter(filter_cat_adapter);
                        }


                    } catch (Resources.NotFoundException e) {
                        e.printStackTrace();
                    }

                    rv_main_cat.getLayoutManager().scrollToPosition(lastFirstVisiblePosition);
                    filter_main_adapter.notifyDataSetChanged();

                }
            });

        }

        @Override
        public int getItemCount() {
            return arrayMain.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tv_filter_dailog_main_item;


            public MyViewHolder(final View itemView) {
                super(itemView);
                tv_filter_dailog_main_item = (TextView) itemView.findViewById(R.id.tv_filter_dailog_main_item);

            }
        }
    }

    ////////////---------------------------------innercatAdapter-----------//


    public class Filter_cat_adapter extends RecyclerView.Adapter<Filter_cat_adapter.MyViewHolder> {

        Context context;
        String selectionType;


        public Filter_cat_adapter(Context context, List<Model_Filter.FilterInfo> filterlist_array, String selectionType) {
            this.context = context;
            array = filterlist_array;
            this.selectionType = selectionType;

            selecteds_item_adapter=new Selecteds_item_adapter(getContext(),selected_arr,selectionType);
            rv_selected_filter.setAdapter(selecteds_item_adapter);
            selecteds_item_adapter.notifyDataSetChanged();


        }


        @Override
        public Filter_cat_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.filter_dialog_inner_cat_list_item, parent, false);
            return new Filter_cat_adapter.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final Filter_cat_adapter.MyViewHolder holder, final int position) {

            final Model_Filter.FilterInfo filterInfo = array.get(position);

            if (TextUtils.isEmpty(filterInfo.getProdcutsCount())) {
                holder.tv_filter_dailog_main_item.setText(filterInfo.getFiltername());
                holder.tv_product_count.setText("");
            } else {
                holder.tv_filter_dailog_main_item.setText(filterInfo.getFiltername());
                holder.tv_product_count.setText("(" + filterInfo.getProdcutsCount() + ")");
            }

//checking active states
            if (filterInfo.getActivestatus().equals("0")){
                holder.rl_cat_list_item.setAlpha(0.4f);
            }else {
                holder.rl_cat_list_item.setAlpha(1.0f);
            }
//            selecteds_item_adapter=new Selecteds_item_adapter(getContext(),selected_arr,selectionType);
//            rv_selected_filter.setAdapter(selecteds_item_adapter);
//            selecteds_item_adapter.notifyDataSetChanged();
//            Log.e("selected_array", "onBindViewHolder: --------------------->"+selected_arr.size() );

            if(filterInfo.getChecked().equals("1")){
                holder.iv_tick.setVisibility(View.VISIBLE);
                holder.rl_cat_list_item.setBackgroundColor(context.getResources().getColor(R.color.black_iconlay));

//                if (!filterInfo.isSelect()){
//                    filterInfo.setSelect(true);
//                    selected_arr.add(filterInfo);

//                }


            }else {
                holder.iv_tick.setVisibility(View.GONE);
            }

            if (selectionType.equals("multiple")){
                if (selected_arr != null) {

                    for (int i = 0; i < selected_arr.size(); i++) {

                        if (filterInfo.isSelect() && selected_arr.get(i).isSelect()) {
                            holder.iv_tick.setVisibility(View.VISIBLE);
                            holder.rl_cat_list_item.setBackgroundColor(context.getResources().getColor(R.color.black_iconlay));
                        } else {
                            holder.iv_tick.setVisibility(View.GONE);
                            holder.rl_cat_list_item.setBackgroundColor(context.getResources().getColor(R.color.black_recyclelay));
                        }
                    }
                } else {
                    holder.iv_tick.setVisibility(View.GONE);
                    holder.rl_cat_list_item.setBackgroundColor(context.getResources().getColor(R.color.black_recyclelay));
                }
            }else if(selectionType.equals("single")){
                if (filterInfo.isSelect()){
                    holder.iv_tick.setVisibility(View.VISIBLE);
                    holder.rl_cat_list_item.setBackgroundColor(context.getResources().getColor(R.color.black_iconlay));
                }else {
                    holder.iv_tick.setVisibility(View.GONE);
                    holder.rl_cat_list_item.setBackgroundColor(context.getResources().getColor(R.color.black_recyclelay));
                }

            }




            holder.rl_cat_list_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Model_Filter.FilterInfo filterInfo = array.get(position);

                    if(filterInfo.getActivestatus().equals("0")){
                        return;
                    }else if(filterInfo.getActivestatus().equals("1") && filterInfo.getChecked().equals("1") ){
                        return;
                    }

                    Log.e("type", "------------------------------> " + selectionType);

                    if (selectionType.equals("single")) {

//                        Model_Filter.FilterInfo filterInfo = array.get(position);


                        if (!filterInfo.isSelect()){
                            Log.d(TAG, "onClick:1 ");
                            for (int i = 0; i < array.size(); i++) {
                                if (array.get(i).isSelect()) {
                                    array.get(i).setSelect(false);
                                    for (int j=0;j<selected_arr.size();j++){
                                        if (array.get(i).getFiltername().matches(selected_arr.get(j).getFiltername())){
                                            selected_arr.remove(j);
                                            break;
                                        }
                                    }
                                }
                            }

                            filterInfo.setSelect(true);
                            filter_cat_adapter.notifyDataSetChanged();
                            if (checkPriceValue("pricebar")){
                                selected_arr.add(filterInfo);
                            }else {
                                selected_arr.add(filterInfo);
                            }

                            selecteds_item_adapter=new Selecteds_item_adapter(getContext(),selected_arr,selectionType);
                            rv_selected_filter.setAdapter(selecteds_item_adapter);


                        }else {
                            Log.d(TAG, "onClick: 2");
                            filterInfo.setSelect(false);
                            filter_cat_adapter.notifyDataSetChanged();
                            for (int i=0;i<selected_arr.size();i++){
                                if (filterInfo.getFiltername().matches(selected_arr.get(i).getFiltername())){
                                    Log.d(TAG, "onClick: filterInfo.getFiltername()="+filterInfo.getFiltername());
                                    selected_arr.remove(i);
                                    break;
                                }
                            }
//                            if (selecteds_item_adapter!=null) {
                            selecteds_item_adapter.notifyDataSetChanged();
//                            }

                        }

//                        if (filterInfo.isSelect()) {
//                            holder.iv_tick.setVisibility(View.GONE);
//                            holder.rl_cat_list_item.setBackgroundColor(context.getResources().getColor(R.color.black_recyclelay));
//                            filterInfo.setSelect(false);
//
//                            for (int i = 0; i < selected_arr.size(); i++) {
//                                if (filterInfo.getFilterid().matches(selected_arr.get(i).getFilterid())) {
//                                    Log.d(TAG, "onClick: "+selected_arr.get(i).getFiltername());
//                                    selected_arr.remove(i);
//                                    break;
//                                }
//
//                            }
//                            selecteds_item_adapter = new Selecteds_item_adapter(getContext(), selected_arr, selectionType);
//                            rv_selected_filter.setAdapter(selecteds_item_adapter);
////                            rv_cat_items.scrollToPosition(position);
//
//                        } else {
//
//
//
//                            for (int i = 0; i < array.size(); i++) {
//                                if(i!=position){
//                                    array.get(i).setSelect(false);
//                                }
//
//                            }
//
////                            if (!selected_arr.get()contains(filterInfo)) {
////                                Log.d(TAG, "onClick: bbselected_arr="+selected_arr.size());
////                                selected_arr.add(filterInfo);
////                            }
////                            boolean isThr=false;
//                            for (int i=0;i<selected_arr.size();i++){
//                                Model_Filter.FilterInfo info=selected_arr.get(i);
//                                if (info.getFilterid().matches(array.get(position).getFilterid())){
//                                    selected_arr.remove(i);
//                                    Log.d(TAG, "onClick: asd");
////                                    isThr=true;
//                                    break;
//                                }
//                            }
////
////                            if (!isThr){
//                                selected_arr.add(filterInfo);
////                            }
//
//
//                            Log.d(TAG, "onClick: aaselected_arasdr="+selected_arr.size());
//                            selecteds_item_adapter = new Selecteds_item_adapter(getContext(), selected_arr, selectionType);
//                            rv_selected_filter.setAdapter(selecteds_item_adapter);
//
//                            holder.iv_tick.setVisibility(View.VISIBLE);
//                            holder.rl_cat_list_item.setBackgroundColor(context.getResources().getColor(R.color.black_iconlay));
//                            array.get(position).setSelect(true);
//                        }


                    } else {

//                        Model_Filter.FilterInfo filterInfo = array.get(position);
                        if (array.get(holder.getAdapterPosition()).isSelect()) {

                            holder.iv_tick.setVisibility(View.GONE);
                            holder.rl_cat_list_item.setBackgroundColor(context.getResources().getColor(R.color.black_recyclelay));
                            array.get(holder.getAdapterPosition()).setSelect(false);

                            for (int i = 0; i < selected_arr.size(); i++) {
                                if (filterInfo.getFilterid().matches(selected_arr.get(i).getFilterid())) {
                                    selected_arr.remove(i);
                                    break;
                                }

                            }
                            selecteds_item_adapter = new Selecteds_item_adapter(getContext(), selected_arr,selectionType);

                            rv_selected_filter.setAdapter(selecteds_item_adapter);
                            rv_cat_items.scrollToPosition(position);

                        } else {
                            if (!selected_arr.contains(filterInfo)) {
                                selected_arr.add(filterInfo);
                            }

                            selecteds_item_adapter = new Selecteds_item_adapter(getContext(), selected_arr,selectionType);
                            rv_selected_filter.setAdapter(selecteds_item_adapter);

                            holder.iv_tick.setVisibility(View.VISIBLE);
                            holder.rl_cat_list_item.setBackgroundColor(context.getResources().getColor(R.color.black_iconlay));
                            array.get(holder.getAdapterPosition()).setSelect(true);
                        }

                    }
//                    notifyDataSetChanged();
//                    selecteds_item_adapter.notifyDataSetChanged();
//                    Log.e("selected", "true");
//                    rv_cat_items.smoothScrollToPosition(position);

                }
            });

        }

        @Override
        public int getItemCount() {
            return array.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tv_filter_dailog_main_item, tv_product_count;
            ImageView iv_tick;
            RelativeLayout rl_cat_list_item;

            public MyViewHolder(final View itemView) {
                super(itemView);

                tv_filter_dailog_main_item = (TextView) itemView.findViewById(R.id.tv_filter_dailog_main_item);
                tv_product_count = (TextView) itemView.findViewById(R.id.tv_product_count);
                rl_cat_list_item = (RelativeLayout) itemView.findViewById(R.id.rl_cat_list_item);
                iv_tick = (ImageView) itemView.findViewById(R.id.iv_tick);

            }


        }


    }

    //////////-----------------------------------selected---Adapter-----------------------------------------/////

    public class Selecteds_item_adapter extends RecyclerView.Adapter<Selecteds_item_adapter.MyViewHolder> {

        Context context;
        String selectrionType;

        public Selecteds_item_adapter(Context context, List<Model_Filter.FilterInfo> filterlist_array, String selectrionType) {
            this.context = context;
            selected_arr = filterlist_array;
            this.selectrionType = selectrionType;

        }

        @Override
        public Selecteds_item_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.selected_filter_item, parent, false);
            return new Selecteds_item_adapter.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder( Selecteds_item_adapter.MyViewHolder holder, final int position) {

            final Model_Filter.FilterInfo filterInfo = selected_arr.get(holder.getAdapterPosition());


            if (filterInfo.isSelect()) {

                holder.selected_item.setText(filterInfo.getFiltername());

            } else {

                holder.itemView.setVisibility(View.GONE);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                    if (selected_arr.size() > 0 && selected_arr.get(position).getChecked().equals("0")) {
                        selected_arr.get(position).setSelect(false);
                        selected_arr.remove(position);
//                        filter_cat_adapter = new Filter_cat_adapter(getContext(), array, selectrionType);
//                        rv_cat_items.setAdapter(filter_cat_adapter);
//                        filter_cat_adapter.notifyDataSetChanged();

                    }
                    if(selected_arr.size()==0 ){
                        filter_cat_adapter = new Filter_cat_adapter(getContext(), array, selectrionType);
                        rv_cat_items.setAdapter(filter_cat_adapter);
                        filter_main_adapter.notifyDataSetChanged();
                    }
                    notifyDataSetChanged();
                    filter_cat_adapter.notifyDataSetChanged();
//                    filter_main_adapter.notifyDataSetChanged();

                }
            });

        }

        @Override
        public int getItemCount() {
            return selected_arr.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView selected_item;

            public MyViewHolder(final View itemView) {
                super(itemView);

                selected_item = (TextView) itemView.findViewById(R.id.selected_item);

            }


        }


    }

}
