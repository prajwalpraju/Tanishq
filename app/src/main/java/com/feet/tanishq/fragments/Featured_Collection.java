package com.feet.tanishq.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.feet.tanishq.R;
import com.feet.tanishq.Tanishq_Screen;
import com.feet.tanishq.adapter.Category_List_Adapter;
import com.feet.tanishq.adapter.Collection_List_Adapter;
import com.feet.tanishq.interfaces.AdapterCallback;
import com.feet.tanishq.model.CategoryList_Model;
import com.feet.tanishq.model.CollectionList_Model;
import com.feet.tanishq.model.Model_Filter;
import com.feet.tanishq.model.Model_SubColl;
import com.feet.tanishq.utils.AsifUtils;
import com.feet.tanishq.utils.AsyncTaskCompleteListener;
import com.feet.tanishq.utils.Const;
import com.feet.tanishq.utils.SpacesItemDecoration;
import com.feet.tanishq.utils.VolleyHttpRequest;
import com.google.android.gms.analytics.HitBuilders;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

import static android.support.v7.widget.StaggeredGridLayoutManager.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Featured_Collection#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Featured_Collection extends Fragment implements AsyncTaskCompleteListener,Response.ErrorListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
    private static final String CAT_ID = "cat_id";
    private static final String CAT_NAME = "cat_name";
    private static final String CONTENTTYPE = "contenttype";
    private static final String HASFILTER = "hasfilter";
    private static final String FILTERED_ID = "filtered_id";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String cat_id,cat_name,contenttype,hasfilter,filtered_id;

    ArrayList<Model_SubColl> arr_list=new ArrayList<Model_SubColl>();
    RequestQueue requestQueue;
    Gson gson;

    public Featured_Collection() {
        // Required empty public constructor
    }
    ArrayList<CollectionList_Model> arr_list_collection = new ArrayList<CollectionList_Model>();
    ArrayList<CategoryList_Model> arr_list_catagory = new ArrayList<CategoryList_Model>();
    public static Featured_Collection newInstance(String category_id, String contenttype, String category_name, String hasfilter, String filtered_id){
        Featured_Collection fragment=new Featured_Collection();
        Bundle args = new Bundle();
        args.putString(CAT_ID, category_id);
        args.putString(CAT_NAME, category_name);
        args.putString(CONTENTTYPE, contenttype);
        args.putString(HASFILTER, hasfilter);
        args.putString(FILTERED_ID, filtered_id);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Sub_Collection.
     */
    // TODO: Rename and change types and number of parameters
//    public static Sub_Collection newInstance(String param1, String param2) {
//        Sub_Collection fragment = new Sub_Collection();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cat_id=getArguments().getString(CAT_ID);
            cat_name=getArguments().getString(CAT_NAME);
            contenttype=getArguments().getString(CONTENTTYPE);
            hasfilter=getArguments().getString(HASFILTER);
            filtered_id=getArguments().getString(FILTERED_ID);
        }
        gson = new GsonBuilder().create();

        Tanishq_Screen.tracker.setScreenName(cat_name+" screen");
        Tanishq_Screen.tracker.send(new HitBuilders.ScreenViewBuilder().build());
        requestQueue= Volley.newRequestQueue(getContext());



    }

    @Override
    public void onResume() {
        super.onResume();


        switch (contenttype) {
            case "Collectionslist":
                callCollectionslistApi(cat_id);
                break;
            case "Categorieslist":
                callCategorieslistApi(cat_id);
                break;
        }
        if (hasfilter.equals("1")) {
            AdapterCallback adapterCallback = (AdapterCallback)this.getContext();
            adapterCallback.setFilterToVisable();
            callFilterApi(filtered_id);
        }

    }

    private void callFilterApi(String catId) {
        if (!AsifUtils.isNetworkAvailable(getActivity())) {
            Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
            return;
        }
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(Const.URL, Const.FILTER_URL);
        params.put(Const.Params.ITEM_ID, catId);
        requestQueue.add(new VolleyHttpRequest(Request.Method.GET, params, Const.ServiceCode.FILTER, this, this));
    }

    private void callCollectionslistApi(String catId) {
        if (!AsifUtils.isNetworkAvailable(getActivity())) {
            Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
            return;
        }
        AsifUtils.start(this.getContext());
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(Const.URL, Const.COLLECTIONLIST_URL);
        params.put(Const.Params.COLLECTIONID, catId);
        requestQueue.add(new VolleyHttpRequest(Request.Method.GET, params, Const.ServiceCode.COLLECTIONLIST, this, this));
    }

    private void callCategorieslistApi(String catId) {
        if (!AsifUtils.isNetworkAvailable(getActivity())) {
            Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
            return;
        }
        AsifUtils.start(this.getContext());
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(Const.URL, Const.CATEGORYLIST_URL);
        params.put(Const.Params.CATEGORYID, catId);
        requestQueue.add(new VolleyHttpRequest(Request.Method.GET, params, Const.ServiceCode.CATEGORYLIST, this, this));
    }


    RecyclerView rv_collection;
    GridLayoutManager gridLayoutManager;
    TextView tv_header_coll;
    SharedPreferences preferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all__collection, container, false);

        tv_header_coll = (TextView) view.findViewById(R.id.tv_header_coll);
        tv_header_coll.setTypeface(AsifUtils.getRaleWay_Bold(getContext()));
        rv_collection = (RecyclerView) view.findViewById(R.id.rv_collection);
        gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        rv_collection.setHasFixedSize(true);
        rv_collection.setLayoutManager(gridLayoutManager);
        rv_collection.addItemDecoration(new SpacesItemDecoration(30));

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {

        AsifUtils.stop();
        switch (serviceCode) {


            case Const.ServiceCode.CATEGORYLIST:
                if (AsifUtils.validateResponse(getContext(), response)) {
                    new ParseCategoryListResponse(response).execute();

                }


                break;
            case Const.ServiceCode.COLLECTIONLIST:
                if (AsifUtils.validateResponse(getContext(), response)) {
                    new ParseCollectionResponse(response).execute();

                }


                break;
            case Const.ServiceCode.FILTER:
                if (AsifUtils.validateResponse(getContext(), response)) {
                    parseFilter(response);
                }


                break;
        }

    }

    @Override
    public void onErrorResponse(VolleyError error) {
        AsifUtils.stop();
        AsifUtils.validateResponse(getContext(), error.getMessage());
    }

    class ParseCollectionResponse extends AsyncTask<Void, Void, Void> {
        String response;

        ParseCollectionResponse(String response) {
            this.response = response;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            parseCollectionModel(response);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            AsifUtils.stop();
            Collection_List_Adapter adapter = new Collection_List_Adapter(getContext(), arr_list_collection,cat_name,"");
            rv_collection.setAdapter(new ScaleInAnimationAdapter(new AlphaInAnimationAdapter(adapter)));
            tv_header_coll.setText(cat_name);
        }
    }

    public void parseCollectionModel(String response) {

        try {
            arr_list_collection.clear();
            JSONObject jObj = new JSONObject(response);
            JSONObject jsonResponseObject = jObj.getJSONObject("response");
            JSONArray categorylistArray = jsonResponseObject.getJSONArray("collectionlist");
            int size = categorylistArray.length();
            for (int i = 0; i < size; i++) {
                JSONObject jObjArr = categorylistArray.getJSONObject(i);
                String id = jObjArr.getString("id");
                String name = jObjArr.getString("name");
                String image = jObjArr.getString("image");
                String contenttype = jObjArr.getString("contenttype");
                String hasfilter = jObjArr.getString("hasfilter");
                String filterparameter = jObjArr.getString("filterparameter");
                String onlineexclusive = jObjArr.getString("onlineexclusive");
                CollectionList_Model model = new CollectionList_Model(id, name, image,contenttype,hasfilter,filterparameter,onlineexclusive);
                arr_list_collection.add(model);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    class ParseCategoryListResponse extends AsyncTask<Void, Void, Void> {
        String response;

        ParseCategoryListResponse(String response) {
            this.response = response;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            parseCategoryListModel(response);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Category_List_Adapter adapter = new Category_List_Adapter(getContext(), arr_list_catagory,cat_name,"");
            rv_collection.setAdapter(new ScaleInAnimationAdapter(new AlphaInAnimationAdapter(adapter)));
            tv_header_coll.setText(cat_name);
        }
    }

    public void parseCategoryListModel(String response) {

        try {
            arr_list_catagory.clear();
            JSONObject jObj = new JSONObject(response);
            JSONObject jsonResponseObject = jObj.getJSONObject("response");
            JSONArray categorylistArray = jsonResponseObject.getJSONArray("categorylist");
            int size = categorylistArray.length();
            for (int i = 0; i < size; i++) {
                JSONObject jObjArr = categorylistArray.getJSONObject(i);
                String id = jObjArr.getString("id");
                String name = jObjArr.getString("name");
                String image = jObjArr.getString("image");
                String contenttype = jObjArr.getString("contenttype");
                String hasfilter = jObjArr.getString("hasfilter");
                String filterparameter = jObjArr.getString("filterparameter");
                String onlineexclusive = jObjArr.getString("onlineexclusive");
                CategoryList_Model model = new CategoryList_Model(id, name, image,contenttype,hasfilter,filterparameter,onlineexclusive);
                arr_list_catagory.add(model);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void parseFilter(String response){
        //mapping
        Model_Filter model_filter = gson.fromJson(response, Model_Filter.class);
//        Log.d(TAG, "parseFilter: val="+model_filter.getResponseList().size());

        //saving in sharedPreference
        preferences = this.getActivity().getSharedPreferences("filter", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("filter_arr", response);
        editor.commit();

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
