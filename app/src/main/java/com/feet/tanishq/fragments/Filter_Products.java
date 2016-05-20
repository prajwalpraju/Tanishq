package com.feet.tanishq.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ProgressBar;
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
import com.feet.tanishq.adapter.FilterTop_Adapter;
import com.feet.tanishq.adapter.Product_Adapter;
import com.feet.tanishq.interfaces.AdapterCallback;
import com.feet.tanishq.model.Model_Filter;
import com.feet.tanishq.model.Model_Params;
import com.feet.tanishq.model.Model_Product;
import com.feet.tanishq.model.Model_TopFilter;
import com.feet.tanishq.utils.AsifUtils;
import com.feet.tanishq.utils.AsyncTaskCompleteListener;
import com.feet.tanishq.utils.Const;
import com.feet.tanishq.utils.OnAddMoreListener;
import com.feet.tanishq.utils.Singleton_volley;
import com.feet.tanishq.utils.SpacesItemDecoration;
import com.feet.tanishq.utils.UserDetails;
import com.feet.tanishq.utils.VolleyHttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.SlideInRightAnimator;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Filter_Products.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Filter_Products#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Filter_Products extends Fragment implements AsyncTaskCompleteListener,Response.ErrorListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String MODEL = "model";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Model_Params model;

    RequestQueue requestQueue;
//    String cat_id="",jewellery="",occasion="",material="";


    public Filter_Products() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Filter_Products.
     */
    // TODO: Rename and change types and number of parameters
    public static Filter_Products newInstance(String param1, String param2) {
        Filter_Products fragment = new Filter_Products();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static Filter_Products newInstance(Model_Params model){
        Filter_Products fragment = new Filter_Products();
        Bundle args = new Bundle();
        args.putSerializable(MODEL, model);
        fragment.setArguments(args);
        return fragment;

    }
    UserDetails userDetails;
    String device_resolution;
    AdapterCallback adapterCallback;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);

            model= (Model_Params) getArguments().getSerializable("model");
        }
        adapterCallback= (AdapterCallback) getContext();

        requestQueue= Volley.newRequestQueue(getContext());



    }

    @Override
    public void onResume() {
        super.onResume();
        userDetails=new UserDetails(getContext());
        device_resolution=userDetails.getUserDevice();
        if (model!=null){
            setUpTopFilter();
            count=0;
            arr_list.clear();
            next_page=1;
            total_pages=2;
            callFilterApi();
        }
    }
    RecyclerView.Adapter filterTop_adapter;

    ArrayList<Model_TopFilter> arr_TopFilter=new ArrayList<Model_TopFilter>();
    private void setUpTopFilter() {
        arr_TopFilter.clear();
        if (model.getColl_map()!=null&&model.getColl_map().get("cat_id")!=null){
            Model_TopFilter coll=new Model_TopFilter(model.getColl_map().get("cat_id"),model.getColl_map().get("id"),model.getColl_map().get("name"));
            arr_TopFilter.add(coll);
        }
        if(model.getJewel_map() !=null&&model.getJewel_map().get("cat_id")!=null){
            Model_TopFilter jewel=new Model_TopFilter(model.getJewel_map().get("cat_id"),model.getJewel_map().get("id"),model.getJewel_map().get("name"));
            arr_TopFilter.add(jewel);
        }
        if (model.getOccas_map()!=null&&model.getOccas_map().get("cat_id")!=null){
            Model_TopFilter occas=new Model_TopFilter(model.getOccas_map().get("cat_id"),model.getOccas_map().get("id"),model.getOccas_map().get("name"));
            arr_TopFilter.add(occas);
        }
        if (model.getMat_map()!=null&&model.getMat_map().get("cat_id")!=null){
            Model_TopFilter mat=new Model_TopFilter(model.getMat_map().get("cat_id"),model.getMat_map().get("id"),model.getMat_map().get("name"));
            arr_TopFilter.add(mat);
        }
        if (model.getPrice_map()!=null&&model.getPrice_map().get("cat_id")!=null){
            Model_TopFilter price=new Model_TopFilter(model.getPrice_map().get("cat_id"),model.getPrice_map().get("id"),model.getPrice_map().get("name"));
            arr_TopFilter.add(price);
        }

        filterTop_adapter=new FilterTop_Adapter(getContext(),arr_TopFilter,Filter_Products.this);
        rv_filter.setAdapter(filterTop_adapter);
    }

    private void callFilterApi() {
        Log.d("ttt", "callFilterApi:er ");

        String cat_id=model.getColl_map()!=null&&model.getColl_map().get("id")!=null?model.getColl_map().get("id"):"";
        String jewellery=model.getJewel_map()!=null&&model.getJewel_map().get("id")!=null?model.getJewel_map().get("id"):"";
        String occasion=model.getOccas_map()!=null&&model.getOccas_map().get("id")!=null?model.getOccas_map().get("id"):"";
        String material=model.getMat_map()!=null&&model.getMat_map().get("id")!=null?model.getMat_map().get("id"):"";
        String pricebar=model.getPrice_map()!=null&&model.getPrice_map().get("id")!=null?model.getPrice_map().get("id"):"";

        if (!AsifUtils.isNetworkAvailable(getActivity())) {
            Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
            return;
        }

        UserDetails user=new UserDetails(getContext());
        HashMap<String,String> map=new HashMap<String,String>();
        map.put(Const.URL,Const.PRODUCT_LIST);
        map.put(Const.Params.ID, user.getUserId());
        map.put(Const.Params.COLLECTIONID, cat_id);
        map.put(Const.Params.JEWELLERY, jewellery);
        map.put(Const.Params.OCCASSION, occasion);
        map.put(Const.Params.MATERIAL, material);
        map.put(Const.Params.PRICEBAR, pricebar);
        map.put(Const.Params.PAGENO, ""+next_page);
        requestQueue.add(new VolleyHttpRequest(Request.Method.GET,map,Const.ServiceCode.PRODUCT_LIST,this,this));
    }


    RecyclerView rv_filter,rv_filter_product;
    LinearLayoutManager gridLayoutManager;
    LinearLayoutManager layoutManager;
    RelativeLayout rl_nowish;
    TextView tv_nowish;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_filter__products, container, false);

        rv_filter=(RecyclerView)view.findViewById(R.id.rv_filter);
        layoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        rv_filter.setHasFixedSize(true);
        rv_filter.setLayoutManager(layoutManager);
        rv_filter.setItemAnimator(new SlideInRightAnimator(new OvershootInterpolator(1f)));

        rl_nowish=(RelativeLayout) view.findViewById(R.id.rl_nowish);
        tv_nowish=(TextView) view.findViewById(R.id.tv_nowish);
        tv_nowish.setTypeface(AsifUtils.getRaleWay_Medium(getContext()));

        rv_filter_product=(RecyclerView)view.findViewById(R.id.rv_filter_product);
        gridLayoutManager=new GridLayoutManager(getActivity(),3);
        rv_filter_product.setHasFixedSize(true);
        rv_filter_product.setLayoutManager(gridLayoutManager);
        rv_filter_product.addItemDecoration(new SpacesItemDecoration(30));

        rv_filter_product.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) //check for scroll down
                {
                    visibleItemCount = gridLayoutManager.getChildCount();
                    totalItemCount = gridLayoutManager.getItemCount();
                    pastVisiblesItems = gridLayoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = false;
                            //Do pagination.. i.e. fetch new data
                            if(next_page<=total_pages){
                                callFilterApi();
                            }

                        }
                    }
                }
            }
        });






        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    Product_Adapter madapter;
    int count=0;


    public void onItemClick(View view, int position) {
        int size=arr_TopFilter.size();
        if (size>1) {
            Model_TopFilter model_topFilter = arr_TopFilter.get(position);
            if (model_topFilter.getId().matches(model.getColl_map().get("id"))){
                model.removeCollmap();
            }else if(model_topFilter.getId().matches(model.getJewel_map().get("id"))){
                model.removeJewelmap();
            }else if(model_topFilter.getId().matches(model.getMat_map().get("id"))){
                model.removeMat_map();
            }else if(model_topFilter.getId().matches(model.getOccas_map().get("id"))){
                model.removeOccas_map();
            }else if(model_topFilter.getId().matches(model.getPrice_map().get("id"))){
                model.removePrice_map();
            }
            arr_TopFilter.remove(position);
            filterTop_adapter.notifyDataSetChanged();
            next_page=1;
            total_pages=2;
            callFilterApiNotify();
        }else{
            adapterCallback.onMethodCallback(3);//all collections
        }

    }

    private void callFilterApiNotify() {
            String cat_id=model.getColl_map()!=null&&model.getColl_map().get("id")!=null?model.getColl_map().get("id"):"";
            String jewellery=model.getJewel_map()!=null&&model.getJewel_map().get("id")!=null?model.getJewel_map().get("id"):"";
            String occasion=model.getOccas_map()!=null&&model.getOccas_map().get("id")!=null?model.getOccas_map().get("id"):"";
            String material=model.getMat_map()!=null&&model.getMat_map().get("id")!=null?model.getMat_map().get("id"):"";
            String pricebar=model.getPrice_map()!=null&&model.getPrice_map().get("id")!=null?model.getPrice_map().get("id"):"";

            if (!AsifUtils.isNetworkAvailable(getActivity())) {
                Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                return;
            }

            UserDetails user=new UserDetails(getContext());
            HashMap<String,String> map=new HashMap<String,String>();
            map.put(Const.URL,Const.PRODUCT_LIST);
            map.put(Const.Params.ID, user.getUserId());
            map.put(Const.Params.COLLECTIONID, cat_id);
            map.put(Const.Params.JEWELLERY, jewellery);
            map.put(Const.Params.OCCASSION, occasion);
            map.put(Const.Params.MATERIAL, material);
            map.put(Const.Params.PRICEBAR, pricebar);
            map.put(Const.Params.PAGENO, ""+next_page);
            requestQueue.add(new VolleyHttpRequest(Request.Method.GET,map,Const.ServiceCode.PRODUCT_LIST_NOTIFY,this,this));
    }

    class ParseProductListResponse extends AsyncTask<Void,Void,Void>{
        String response;
        public ParseProductListResponse(String response) {
            this.response=response;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            parseResponse(response);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (count==0){
                count++;
                madapter=new Product_Adapter(getContext(),arr_list,arr_TopFilter);
                rv_filter_product.setAdapter(new ScaleInAnimationAdapter(new AlphaInAnimationAdapter(madapter)));
                if (arr_list.size()>0) {
                    rl_nowish.setVisibility(View.GONE);
                } else {
                    rl_nowish.setVisibility(View.VISIBLE);
                    tv_nowish.setText(getResources().getString(R.string.no_list));
                }
            }

            madapter.notifyDataSetChanged();
            loading=true;
        }
    }


    ArrayList<Model_Product> arr_list=new ArrayList<Model_Product>();
    int next_page=1,total_pages=2;
    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;

    private void parseResponse(String response) {

        try {
            JSONObject jObj=new JSONObject(response);
            JSONObject jResObj=jObj.getJSONObject("response");
            JSONArray jArrPro=jResObj.getJSONArray("products");
            int size=jArrPro.length();
            for (int i=0;i<size;i++){
                JSONObject obj=jArrPro.getJSONObject(i);
                String device_image=getDeviceImage(obj);
                String product_image=obj.getString("product_image");
                String product_title= obj.getString("product_title");
                String product_price= obj.getString("product_price");
                String discount_price= obj.getString("discount_price");
                String discount_percent=obj.getString("discount_percent");
                String product_url=obj.getString("product_url");

                String description= obj.getString("description");
                String Collection= obj.getString("Collection");
                String Material=obj.getString("Material");
                String category=obj.getString("category");
                Model_Product model_product=new Model_Product(device_image,product_image,product_title,product_price,discount_price,discount_percent,
                        description,Collection,Material,category,product_url,false,false);
                arr_list.add(model_product);
            }
             next_page=jResObj.getInt("next_page");
             total_pages=jResObj.getInt("total_pages");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getDeviceImage(JSONObject obj) {
        String image="";
        try {
            JSONObject jobj=obj.getJSONObject("0");
            switch (device_resolution){
                case Const.Resolution.MDPI_TXT:
                    image=jobj.getString(Const.Resolution.MDPI_TXT);
                    break;
                case Const.Resolution.HDPI_TXT:
                    image=jobj.getString(Const.Resolution.HDPI_TXT);
                    break;
                case Const.Resolution.XHDPI_TXT:
                    image=jobj.getString(Const.Resolution.XHDPI_TXT);
                    break;
                case Const.Resolution.XXHPDI_TXT:
                    image=jobj.getString(Const.Resolution.XXHPDI_TXT);
                    break;
                case Const.Resolution.XXXHDPI_TXT:
                    image=jobj.getString(Const.Resolution.XXXHDPI_TXT);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return image;
    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {
        switch (serviceCode){
            case Const.ServiceCode.PRODUCT_LIST:
                if (AsifUtils.validateResponse(getContext(),response)){
                    rl_nowish.setVisibility(View.GONE);
                    new ParseProductListResponse(response).execute();
                }else {
                    rl_nowish.setVisibility(View.VISIBLE);
                    try {
                        tv_nowish.setText(new JSONObject(response).getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                break;

            case Const.ServiceCode.PRODUCT_LIST_NOTIFY:
                if (AsifUtils.validateResponse(getContext(),response)) {
                    rl_nowish.setVisibility(View.GONE);
                    arr_list.clear();
                    madapter.notifyDataSetChanged();
                    new ParseProductListResponse(response).execute();
                }else {
                    rl_nowish.setVisibility(View.VISIBLE);
                    try {
                        tv_nowish.setText(new JSONObject(response).getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        AsifUtils.validateResponse(getContext(), error.getMessage());
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
