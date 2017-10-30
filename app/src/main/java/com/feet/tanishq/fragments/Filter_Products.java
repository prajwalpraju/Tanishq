package com.feet.tanishq.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.feet.tanishq.R;
import com.feet.tanishq.Tanishq_Screen;
import com.feet.tanishq.adapter.Product_Adapter;
import com.feet.tanishq.interfaces.AdapterCallback;
import com.feet.tanishq.model.ModelTopFilterNew;
import com.feet.tanishq.model.Model_Filter;
import com.feet.tanishq.model.Model_Product;
import com.feet.tanishq.utils.AsifUtils;
import com.feet.tanishq.utils.AsyncTaskCompleteListener;
import com.feet.tanishq.utils.Const;
import com.feet.tanishq.utils.SpacesItemDecoration;
import com.feet.tanishq.utils.UserDetails;
import com.feet.tanishq.utils.VolleyHttpRequest;
import com.google.android.gms.analytics.HitBuilders;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.SlideInRightAnimator;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Filter_Products.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Filter_Products#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Filter_Products extends Fragment implements AsyncTaskCompleteListener, Response.ErrorListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String PRODUCSTYPE = "producstype";
    private static final String SELLECTED_ARRAY = "sellected_array";
    private static final String NAME = "name";
    private static final String FROMSEARCH = "fromsearch";
    private static final String SEARCHNAME = "searchname";
    private static final String FILTERPARAMETER = "filterparameter";
    private static final String FROMPRODUCTCLICK = "fromProductClick";
    private static final String ITEM_ID = "itemid";
    private static final String DIR = "itemid";
    private static final String PARENTNAME = "parentname";

    // TODO: Rename and change types of parameters
//    private String mParam1;
    public List<Model_Filter.FilterInfo> sellected_array;
    private String producstype;
    private String name;
    private String searchname;
    private String filterparameter;
    private String itemId;
    private String dir = "";
    private String parentname;
    private boolean fromProductClick;
    private boolean fromsearch;


    RequestQueue requestQueue;


    public Filter_Products() {
        // Required empty public constructor
    }

    Filter_Products fragment;

    // TODO: Rename and change types and number of parameters

    public static Filter_Products newInstance(String name, boolean fromsearch) {
        Filter_Products fragment = new Filter_Products();
        Log.e("bbb", "newInstance: "+name );
        Bundle args = new Bundle();
        args.putString(SEARCHNAME, name);
        args.putBoolean(FROMSEARCH, fromsearch);
        fragment.setArguments(args);
        return fragment;
    }

    public static Filter_Products newInstance(String name, String filterparameter, boolean fromProductClick, String itemId, String parentname, String dir) {
        Filter_Products fragment = new Filter_Products();
        Bundle args = new Bundle();
        args.putString(NAME, name);
        args.putString(FILTERPARAMETER, filterparameter);
        args.putBoolean(FROMPRODUCTCLICK, fromProductClick);
        args.putString(ITEM_ID, itemId);
        args.putString(DIR, dir);
        args.putString(PARENTNAME, parentname);
        fragment.setArguments(args);
        return fragment;


    }


    public static Filter_Products newInstance(List<Model_Filter.FilterInfo> selected_arr, String producstype) {
        Filter_Products fragment = new Filter_Products();
        Bundle args = new Bundle();
        args.putString(PRODUCSTYPE, producstype);
        args.putSerializable(SELLECTED_ARRAY, (Serializable) selected_arr);
        fragment.setArguments(args);
        return fragment;

    }

    UserDetails userDetails;
    String device_resolution;
    String directory;
    AdapterCallback adapterCallback;
    ArrayList<ModelTopFilterNew> arrTopFilternew = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            AsifUtils.stop();

            fromsearch = getArguments().getBoolean(FROMSEARCH);
            Log.e("bbb", "onCreate: "+fromsearch );

            producstype = getArguments().getString(PRODUCSTYPE);
            sellected_array = (List<Model_Filter.FilterInfo>) getArguments().getSerializable("sellected_array");

            name = getArguments().getString(NAME);
            searchname = getArguments().getString(SEARCHNAME);

            parentname = getArguments().getString(PARENTNAME);
            filterparameter = getArguments().getString(FILTERPARAMETER);
            itemId = getArguments().getString(ITEM_ID);
            dir = getArguments().getString(DIR);
            fromProductClick = getArguments().getBoolean(FROMPRODUCTCLICK);


            if (!fromProductClick&&!fromsearch) {
                updateTopArray();
            }else if (fromsearch){
                arrTopFilternew.clear();
                ModelTopFilterNew modelTopFilterNew = new ModelTopFilterNew(searchname);
                arrTopFilternew.add(modelTopFilterNew);
            }
            else {
                arrTopFilternew.clear();
                ModelTopFilterNew modelTopFilterNew = new ModelTopFilterNew(name);
                arrTopFilternew.add(modelTopFilterNew);

            }

//            callFilterApi(itemId);
        }

        if (parentname != null && name != null) {
            Tanishq_Screen.tracker.setScreenName(parentname + " " + name + " screen");
            directory = parentname + "-" + name;
            Tanishq_Screen.tracker.send(new HitBuilders.ScreenViewBuilder().build());

        } else {
            directory = "Filter";
            Tanishq_Screen.tracker.send(new HitBuilders.ScreenViewBuilder().build());
        }

        adapterCallback = (AdapterCallback) getContext();
        requestQueue = Volley.newRequestQueue(getContext());




    }

    public void updateTopArray() {
        arrTopFilternew.clear();
        for (int i = 0; i < sellected_array.size(); i++) {
            ModelTopFilterNew modelTopFilterNew = new ModelTopFilterNew(sellected_array.get(i).getFiltername());
            arrTopFilternew.add(modelTopFilterNew);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        AdapterCallback adapterCallback1 = (AdapterCallback) this.getContext();
        adapterCallback1.setFilterToVisable();

        try {
            if (dir != null) {
                AdapterCallback adapterCallback = (AdapterCallback) this.getContext();
                adapterCallback.setBreadcrumb(dir);
            }

            if (!fromsearch){
                callFilterApi(itemId);
            }

//}


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void callSearchApi(String searchname) {
        if (!AsifUtils.isNetworkAvailable(getActivity())) {
            Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
            return;
        }
        AsifUtils.start(getContext());
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(Const.URL, Const.SEARCH_URL);
        params.put(Const.Params.NEXT_PAGE, "" + next_page);
        params.put(Const.Params.SEARCHTERM, searchname);
        Log.e("ggg", "callSearchApi: -->"+params );
        requestQueue.add(new VolleyHttpRequest(Request.Method.GET, params, Const.ServiceCode.PRODUCT_LIST, this, this));
    }

    private void callFilterApi(String itemId) {
        if (!AsifUtils.isNetworkAvailable(getActivity())) {
            Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
            return;
        }
//        AsifUtils.start(getContext());
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(Const.URL, Const.FILTER_URL);
        params.put(Const.Params.ITEM_ID, itemId + "&" + filterparameter);
        Log.e("ggg", "callFilterProductApi: request----> " + params);
        requestQueue.add(new VolleyHttpRequest(Request.Method.GET, params, Const.ServiceCode.FILTER, this, this));
    }


    private void callFilterProductApifromProduct(String filterparameter) {

        if (!AsifUtils.isNetworkAvailable(getActivity())) {
            Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
            return;
        }
        AsifUtils.start(this.getContext());
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(Const.URL, Const.PRODUCT_LIST + "?" + filterparameter + "&" + Const.Params.NEXT_PAGE + "=0" + next_page);
        Log.e("ggg", "callFilterProductApifromProduct requiest " + map);
        requestQueue.add(new VolleyHttpRequest(Request.Method.GET, map, Const.ServiceCode.PRODUCT_LIST_FROM_CLICK, this, this));
    }


    private void callFilterProductApi() {
        if (!AsifUtils.isNetworkAvailable(getActivity())) {
            Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
            return;
        }

        AsifUtils.start(this.getContext());
        HashMap<String, String> map = new HashMap<String, String>();
        String newkey, oldValue;

        for (int i = 0; i < sellected_array.size(); i++) {
            String key = sellected_array.get(i).getMainfilter();
            String newvalue = sellected_array.get(i).getFilterid();
            if (map.containsKey(key)) {
                newkey = key;
                oldValue = map.get(key);
                map.remove(key);
                map.put(newkey, oldValue + "," + newvalue);
            } else {
                map.put(key, newvalue);
            }
        }
        map.put(Const.URL, Const.PRODUCT_LIST);
        map.put(Const.Params.PRODUCTSTYPE, producstype);
        map.put(Const.Params.NEXT_PAGE, "" + next_page);
        Log.e("ggg", "callFilterProductApi: " + map);
        requestQueue.add(new VolleyHttpRequest(Request.Method.GET, map, Const.ServiceCode.PRODUCT_LIST, this, this));

    }


    RecyclerView rv_filter, rv_filter_product;
    LinearLayoutManager gridLayoutManager;
    LinearLayoutManager layoutManager;
    RelativeLayout rl_nowish;
    TextView tv_nowish;
    RecyclerView.Adapter topfilteradapter, itemClick_Adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_filter__products, container, false);

        rv_filter = (RecyclerView) view.findViewById(R.id.rv_filter);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rv_filter.setHasFixedSize(true);
        rv_filter.setLayoutManager(layoutManager);
        rv_filter.setItemAnimator(new SlideInRightAnimator(new OvershootInterpolator(1f)));

        rl_nowish = (RelativeLayout) view.findViewById(R.id.rl_nowish);
        tv_nowish = (TextView) view.findViewById(R.id.tv_nowish);
        tv_nowish.setTypeface(AsifUtils.getRaleWay_Medium(getContext()));

        rv_filter_product = (RecyclerView) view.findViewById(R.id.rv_filter_product);
        gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        rv_filter_product.setHasFixedSize(true);
        rv_filter_product.setLayoutManager(gridLayoutManager);
        rv_filter_product.addItemDecoration(new SpacesItemDecoration(30));


        userDetails = new UserDetails(getContext());
        device_resolution = userDetails.getUserDevice();
        count = 0;
        arr_list_product.clear();
        next_page = 1;
        total_pages = 2;

        ArrayList<Model_Product> preservedArray = userDetails.getPreservedArray();
//        int preservedPosition = userDetails.getPreservedPosition();

        if (fromsearch&&preservedArray!=null){

            next_page = userDetails.getNextPosition();
            total_pages = userDetails.getTotalPosition();
            Log.e("zzz", "onCreateView: " + next_page);
            arr_list_product.clear();
            arr_list_product = preservedArray;
            if (count == 0) {
                count++;
            }

            madapter = new Product_Adapter(getContext(), preservedArray, arrTopFilternew, directory, next_page, total_pages);
            topfilteradapter = new Filter_MaintopPager_Adapter(getContext(), arrTopFilternew);
            rv_filter.setAdapter(topfilteradapter);

            rv_filter_product.setAdapter(new ScaleInAnimationAdapter(new AlphaInAnimationAdapter(madapter)));
            if (preservedArray.size() > 0) {
                rl_nowish.setVisibility(View.GONE);
            } else {
                rl_nowish.setVisibility(View.VISIBLE);
                tv_nowish.setText(getResources().getString(R.string.no_list));
            }
            userDetails.ClearPreservedArrayAndPosition();
            madapter.notifyDataSetChanged();
            loading = true;

        }else if (fromsearch) {
            callSearchApi(searchname);
        } else if (fromProductClick && preservedArray != null) {

            next_page = userDetails.getNextPosition();
            total_pages = userDetails.getTotalPosition();
            Log.e("zzz", "onCreateView: " + next_page);
            arr_list_product.clear();
            arr_list_product = preservedArray;

            if (count == 0) {
                count++;
            }

            madapter = new Product_Adapter(getContext(), preservedArray, arrTopFilternew, directory, next_page, total_pages);
            itemClick_Adapter = new ItemClick_Adapter(getContext());
            rv_filter.setAdapter(itemClick_Adapter);
            rv_filter_product.setAdapter(new ScaleInAnimationAdapter(new AlphaInAnimationAdapter(madapter)));
            if (preservedArray.size() > 0) {
                rl_nowish.setVisibility(View.GONE);
            } else {
                rl_nowish.setVisibility(View.VISIBLE);
                tv_nowish.setText(getResources().getString(R.string.no_list));
            }
            userDetails.ClearPreservedArrayAndPosition();
            madapter.notifyDataSetChanged();
            loading = true;

        } else if (fromProductClick) {
            callFilterProductApifromProduct(filterparameter);
        } else if (!fromProductClick && preservedArray != null) {

            next_page = userDetails.getNextPosition();
            total_pages = userDetails.getTotalPosition();
            Log.e("zzz", "onCreateView: " + next_page);
            arr_list_product.clear();
            arr_list_product = preservedArray;
            if (count == 0) {
                count++;
            }

            madapter = new Product_Adapter(getContext(), preservedArray, arrTopFilternew, directory, next_page, total_pages);
            topfilteradapter = new Filter_MaintopPager_Adapter(getContext(), arrTopFilternew);
            rv_filter.setAdapter(topfilteradapter);

            rv_filter_product.setAdapter(new ScaleInAnimationAdapter(new AlphaInAnimationAdapter(madapter)));
            if (preservedArray.size() > 0) {
                rl_nowish.setVisibility(View.GONE);
            } else {
                rl_nowish.setVisibility(View.VISIBLE);
                tv_nowish.setText(getResources().getString(R.string.no_list));
            }
            userDetails.ClearPreservedArrayAndPosition();
            madapter.notifyDataSetChanged();
            loading = true;

        } else if (!fromProductClick && !fromsearch) {
            callFilterProductApi();
        }


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
                            if (next_page <= total_pages) {
                                if (fromsearch) {
                                    callSearchApi(searchname);
                                } else if (!fromProductClick &&!fromsearch) {
                                    callFilterProductApi();
                                } else {
                                    callFilterProductApifromProduct(filterparameter);
                                }

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
    int count = 0;


    class ParseProductListResponse extends AsyncTask<Void, Void, Void> {
        String response;

        public ParseProductListResponse(String response) {
            this.response = response;
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
            if (!fromProductClick) {
                if (count == 0) {
                    count++;
                    madapter = new Product_Adapter(getContext(), arr_list_product, arrTopFilternew, directory, next_page, total_pages);
                    topfilteradapter = new Filter_MaintopPager_Adapter(getContext(), arrTopFilternew);
                    rv_filter.setAdapter(topfilteradapter);
                    rv_filter_product.setAdapter(new ScaleInAnimationAdapter(new AlphaInAnimationAdapter(madapter)));
                    madapter.notifyItemRangeChanged(arr_list_product.size() - 1, arr_list_product.size());
                    if (arr_list_product.size() > 0) {
                        rl_nowish.setVisibility(View.GONE);
                    } else {
                        rl_nowish.setVisibility(View.VISIBLE);
                        tv_nowish.setText(getResources().getString(R.string.no_list));
                    }
                }

                madapter.notifyDataSetChanged();
                loading = true;
            } else {
                if (count == 0) {
                    count++;
                    madapter = new Product_Adapter(getContext(), arr_list_product, arrTopFilternew, directory, next_page, total_pages);
                    itemClick_Adapter = new ItemClick_Adapter(getContext());
                    rv_filter.setAdapter(itemClick_Adapter);
                    rv_filter_product.setAdapter(new ScaleInAnimationAdapter(new AlphaInAnimationAdapter(madapter)));
                    madapter.notifyItemRangeChanged(arr_list_product.size() - 1, arr_list_product.size());
                    if (arr_list_product.size() > 0) {
                        rl_nowish.setVisibility(View.GONE);
                    } else {
                        rl_nowish.setVisibility(View.VISIBLE);
                        tv_nowish.setText(getResources().getString(R.string.no_list));
                    }
                }

                madapter.notifyDataSetChanged();
                loading = true;
            }

            AsifUtils.stop();

        }
    }


    //---------------------------------------------itemClickAdapter--------------------------------------------------//

    public class ItemClick_Adapter extends RecyclerView.Adapter<ItemClick_Adapter.ViewHolder> {

        Context context;

        public ItemClick_Adapter(Context context) {
            this.context = context;

        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.filter_item_trans, null);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {


            holder.tv_filter.setText(name);


        }

        @Override
        public int getItemCount() {
            return 1;
        }

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView tv_filter;

            public ViewHolder(View itemView) {
                super(itemView);
                tv_filter = (TextView) itemView.findViewById(R.id.tv_filter);
                tv_filter.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        }
    }


    //---------------------------------------------filterAdapter-----------------------------------------------------//


    public class Filter_MaintopPager_Adapter extends RecyclerView.Adapter<Filter_MaintopPager_Adapter.ViewHolder> {

        Context context;
        ArrayList<ModelTopFilterNew> arr_list;

        public Filter_MaintopPager_Adapter(Context context, ArrayList<ModelTopFilterNew> arr_list) {
            this.context = context;
            this.arr_list = arr_list;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.filter_item_trans, null);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {

            ModelTopFilterNew model = arr_list.get(position);
            holder.tv_filter.setText(model.getTitle());


        }

        @Override
        public int getItemCount() {
            return arr_list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView tv_filter;

            public ViewHolder(View itemView) {
                super(itemView);
                tv_filter = (TextView) itemView.findViewById(R.id.tv_filter);

                tv_filter.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                int size = arrTopFilternew.size();
                if (size > 1) {

                    ModelTopFilterNew modelTopFilterNew = arrTopFilternew.get(getAdapterPosition());

                    Model_Filter.FilterInfo filterInfo = sellected_array.get(getAdapterPosition());

                    if (modelTopFilterNew.getTitle().equals(filterInfo.getFiltername())) {
                        sellected_array.remove(getAdapterPosition());
                        arrTopFilternew.remove(getAdapterPosition());

                        if (!fromProductClick) {
                            updateTopArray();
                        } else {
                            arrTopFilternew.clear();
                            ModelTopFilterNew modelTopFilter = new ModelTopFilterNew(name);
                            arrTopFilternew.add(modelTopFilter);

                        }

                        next_page = 1;
                        total_pages = 2;
                        if (fromsearch) {
                            callSearchApi(searchname);
                        } else if (fromProductClick) {
                            callFilterProductApifromProduct(filterparameter);
                        } else {
                            callFilterProductApi();
                        }
                        arr_list_product.clear();
                        madapter.notifyDataSetChanged();
                        topfilteradapter.notifyDataSetChanged();
                    }

                } else {
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            }
        }
    }


    ArrayList<Model_Product> arr_list_product = new ArrayList<Model_Product>();
    int next_page = 1, total_pages = 2;
    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;

    private void parseResponse(String response) {


        try {
            Log.e("ggg", "parseResponse: ->" + response);
            JSONObject jObj = new JSONObject(response);
            JSONObject jResObj = jObj.getJSONObject("response");

            JSONArray jArrPro = jResObj.getJSONArray("products");
            int size = jArrPro.length();


            for (int i = 0; i < size; i++) {

                ArrayList<String> stringArrayUrlList = new ArrayList<>();
                JSONObject obj = jArrPro.getJSONObject(i);
                String device_image = getDeviceImage(obj);
                JSONArray jsonArrayMultiAngleImages = obj.getJSONArray("product_images_list");
                for (int j = 0; j < jsonArrayMultiAngleImages.length(); j++) {
                    JSONObject objMultiAngle = jsonArrayMultiAngleImages.getJSONObject(j);
                    String url = getDeviceMultiAngleImageImage(objMultiAngle);
                    Log.e("zzz", "parseResponse: " + url);
                    stringArrayUrlList.add(url);

                }

                String product_image = obj.getString("product_image");
                String product_title = obj.getString("product_title");
                String product_price = obj.getString("product_price");
                String discount_price = obj.getString("discount_price");
                String discount_percent = obj.getString("discount_percent");
                String product_url = obj.getString("product_url");

                String description = obj.getString("description");
                String Collection = obj.getString("collection");
                String Material = obj.getString("material");
                String category = obj.getString("category");

                String gold_karatage = obj.getString("gold_karatage");
                String weight = obj.getString("weight");
                String onlineexclusive = obj.getString("onlineexclusive");

                String community = obj.getString("community");
                String occasion = obj.getString("occasion");
                String disclaimer = obj.getString("disclaimer");


                Model_Product model_product = new Model_Product(device_image, product_image, product_title, product_price, discount_price, discount_percent,
                        description, Collection, Material, category, product_url, false, false, gold_karatage, weight, onlineexclusive, "", community, occasion, stringArrayUrlList, disclaimer);
                arr_list_product.add(model_product);
            }
            next_page = jResObj.getInt("next_page");
            total_pages = jResObj.getInt("total_pages");
            userDetails.savePagePosition(next_page, total_pages);
            Log.e("qqq", "nextpage : " + next_page + "  total page : " + total_pages);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getDeviceImage(JSONObject obj) {
        String image = "";
        try {
            JSONObject jobj = obj.getJSONObject("0");
            switch (device_resolution) {
                case Const.Resolution.MDPI_TXT:
                    image = jobj.getString(Const.Resolution.MDPI_TXT);
                    break;
                case Const.Resolution.HDPI_TXT:
                    image = jobj.getString(Const.Resolution.HDPI_TXT);
                    break;
                case Const.Resolution.XHDPI_TXT:
                    image = jobj.getString(Const.Resolution.XHDPI_TXT);
                    break;
                case Const.Resolution.XXHPDI_TXT:
                    image = jobj.getString(Const.Resolution.XXHPDI_TXT);
                    break;
                case Const.Resolution.XXXHDPI_TXT:
                    image = jobj.getString(Const.Resolution.XXXHDPI_TXT);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return image;
    }

    private String getDeviceMultiAngleImageImage(JSONObject obj) {
        String image = "";
        Log.e("zzz", "getDeviceMultiAngleImageImage: " + obj.toString());
        try {

            switch (device_resolution) {
                case Const.Resolution.MDPI_TXT:
                    image = obj.getString(Const.Resolution.MDPI_TXT);
                    break;
                case Const.Resolution.HDPI_TXT:
                    image = obj.getString(Const.Resolution.HDPI_TXT);
                    break;
                case Const.Resolution.XHDPI_TXT:
                    image = obj.getString(Const.Resolution.XHDPI_TXT);
                    break;
                case Const.Resolution.XXHPDI_TXT:
                    image = obj.getString(Const.Resolution.XXHPDI_TXT);
                    break;
                case Const.Resolution.XXXHDPI_TXT:
                    image = obj.getString(Const.Resolution.XXXHDPI_TXT);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return image;
    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {
        AsifUtils.stop();
        switch (serviceCode) {
            case Const.ServiceCode.PRODUCT_LIST:
                Log.e("ggg", "PRODUCT_LIST response: "+response );
                AsifUtils.stop();
                if (AsifUtils.validateResponse(getContext(), response)) {
                    rl_nowish.setVisibility(View.GONE);

                    new ParseProductListResponse(response).execute();
                } else {
                    rl_nowish.setVisibility(View.VISIBLE);
                    try {
                        tv_nowish.setText(new JSONObject(response).getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                break;
            case Const.ServiceCode.PRODUCT_LIST_FROM_CLICK:
                AsifUtils.stop();
                if (AsifUtils.validateResponse(getContext(), response)) {
                    rl_nowish.setVisibility(View.GONE);

                    new ParseProductListResponse(response).execute();
                } else {
                    rl_nowish.setVisibility(View.VISIBLE);
                    try {
                        tv_nowish.setText(new JSONObject(response).getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;

            case Const.ServiceCode.FILTER:
                AsifUtils.stop();
                if (AsifUtils.validateResponse(getContext(), response)) {

                    parseFilter(response);

                }
                break;
        }
    }

    public void parseFilter(String response) {

        try {
            //mapping

            Gson gson = new GsonBuilder().create();
            Model_Filter model_filter = gson.fromJson(response, Model_Filter.class);
            Log.e("ggg", "parseFilter: val=------------------------>" + response);

            //saving in sharedPreference
            if (getActivity() != null) {
                SharedPreferences preferences = getActivity().getSharedPreferences("filter", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("filter_arr", response);
                editor.commit();
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
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
