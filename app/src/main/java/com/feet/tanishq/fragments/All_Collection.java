package com.feet.tanishq.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.SyncStateContract;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.feet.tanishq.adapter.Category_List_Adapter;
import com.feet.tanishq.adapter.Collection_List_Adapter;
import com.feet.tanishq.interfaces.AdapterCallback;
import com.feet.tanishq.model.CategoryList_Model;
import com.feet.tanishq.model.CollectionList_Model;
import com.feet.tanishq.model.Model_Filter;
import com.feet.tanishq.utils.AsifUtils;
import com.feet.tanishq.utils.AsyncTaskCompleteListener;
import com.feet.tanishq.utils.Const;
import com.feet.tanishq.utils.SpacesItemDecoration;
import com.feet.tanishq.utils.VolleyHttpRequest;
import com.google.android.gms.analytics.HitBuilders;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

import static android.support.v7.widget.StaggeredGridLayoutManager.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link All_Collection.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link All_Collection#newInstance} factory method to
 * create an instance of this fragment.
 */
public class All_Collection extends Fragment implements AsyncTaskCompleteListener, Response.ErrorListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String CATID = "catid";
    private static final String CONTENT_TYPE = "content_type";
    private static final String HASFILTER = "hasfilter";
    private static final String NAME = "name";
    private static final String FILTERPARAMETER = "filterparameter";
    private static final String DIR = "dir";

    // TODO: Rename and change types of parameters
    private String catId;
    private String content_type;
    private String hasfilter;
    private String name;
    private String filterparameter;
    private String dir=null;
    Gson gson;

//    private OnFragmentInteractionListener mListener;

    public All_Collection() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment All_Collection_Adapter.
     */
    // TODO: Rename and change types and number of parameters
    public static All_Collection newInstance(String param1, String param2) {
        All_Collection fragment = new All_Collection();
        Bundle args = new Bundle();
        args.putString(CATID, param1);
        args.putString(CONTENT_TYPE, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static All_Collection newInstance(String catId, String content_type, String hasfilter, String name, String filterparameter,String dir) {

        All_Collection fragment = new All_Collection();
        Bundle args = new Bundle();
        args.putString(CATID, catId);
        args.putString(CONTENT_TYPE, content_type);
        args.putString(HASFILTER, hasfilter);
        args.putString(NAME, name);
        args.putString(FILTERPARAMETER, filterparameter);
        args.putString(DIR, dir);
        fragment.setArguments(args);
        return fragment;
    }

    ArrayList<CollectionList_Model> arr_list_collection = new ArrayList<CollectionList_Model>();
    ArrayList<CategoryList_Model> arr_list_catagory = new ArrayList<CategoryList_Model>();
    RequestQueue requestQueue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            catId = getArguments().getString(CATID);
            content_type = getArguments().getString(CONTENT_TYPE);
            hasfilter = getArguments().getString(HASFILTER);
            name = getArguments().getString(NAME);
            filterparameter = getArguments().getString(FILTERPARAMETER);
            dir = getArguments().getString(DIR);
            if(!dir.equals("") | !dir.isEmpty()){
            }else {
                dir = "Main collections > "+name;
            }

        }
        Tanishq_Screen.tracker.setScreenName(name+" screen");
        Tanishq_Screen.tracker.send(new HitBuilders.ScreenViewBuilder().build());
        requestQueue = Volley.newRequestQueue(getContext());
        gson = new GsonBuilder().create();

    }

    @Override
    public void onResume() {
        super.onResume();

        AdapterCallback adapterCallback = (AdapterCallback) this.getActivity();
        adapterCallback.setFilterToVisable();
        adapterCallback.setBreadcrumb(dir);

        switch (content_type) {
            case "Collectionslist":
                callCollectionslistApi(catId);
                break;
            case "Categorieslist":
                callCategorieslistApi(catId);
                break;
        }
        if (hasfilter.equals("1")) {
            callFilterApi(catId);
        }
    }

    private void callFilterApi(String catId) {
        if (!AsifUtils.isNetworkAvailable(getActivity())) {
            Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
            return;
        }
//        AsifUtils.start(this.getContext());
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(Const.URL, Const.FILTER_URL);
        params.put(Const.Params.ITEM_ID, catId+"&"+filterparameter);
        Log.e("ggg", "callFilterApi: request -------------->"+params );
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
        AsifUtils.start(getContext());
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(Const.URL, Const.CATEGORYLIST_URL);
        params.put(Const.Params.CATEGORYID, catId);
        requestQueue.add(new VolleyHttpRequest(Request.Method.GET, params, Const.ServiceCode.CATEGORYLIST, this, this));
    }

    RecyclerView rv_collection;
    GridLayoutManager gridLayoutManager;
    TextView tv_header_coll;
    SharedPreferences preferences;
    Collection_List_Adapter adapter;
    Category_List_Adapter adapter2;
    RelativeLayout rl_nowish;
    TextView tv_nowish;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all__collection, container, false);

        tv_header_coll = (TextView) view.findViewById(R.id.tv_header_coll);
        tv_header_coll.setTypeface(AsifUtils.getRaleWay_Bold(getContext()));
        rv_collection = (RecyclerView) view.findViewById(R.id.rv_collection);

        rl_nowish = (RelativeLayout) view.findViewById(R.id.rl_nowish);
        tv_nowish = (TextView) view.findViewById(R.id.tv_nowish);
        tv_nowish.setTypeface(AsifUtils.getRaleWay_Medium(getContext()));

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
    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {
        AsifUtils.stop();
        switch (serviceCode) {

            case Const.ServiceCode.CATEGORYLIST:
                if (AsifUtils.validateResponse(getContext(), response)) {
//                    AsifUtils.stop();
                    new ParseCategoryListResponse(response).execute();

                }
                else{
                    rl_nowish.setVisibility(View.VISIBLE);
                    try {
                        tv_nowish.setText(new JSONObject(response).getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


                break;
            case Const.ServiceCode.COLLECTIONLIST:
                if (AsifUtils.validateResponse(getContext(), response)) {
                    new ParseCollectionResponse(response).execute();

                }
                else {
                    rl_nowish.setVisibility(View.VISIBLE);
                    try {
                        tv_nowish.setText(new JSONObject(response).getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
//                AsifUtils.stop();

                break;
            case Const.ServiceCode.FILTER:
//                Log.e("filter", "onTaskCompleted: " + response);
                AsifUtils.stop();
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

            adapter = new Collection_List_Adapter(getContext(), arr_list_collection,name,dir);
            rv_collection.setAdapter(new ScaleInAnimationAdapter(new AlphaInAnimationAdapter(adapter)));
            tv_header_coll.setText(name);
        }
    }

    public void parseCollectionModel(String response) {

        try {
            arr_list_collection.clear();
//            Log.e("ttt", "parseResponse: " + response);
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
//                Log.e("ppp", "online exclusive ----->"+onlineexclusive);
                CollectionList_Model model = new CollectionList_Model(id, name, image, contenttype, hasfilter, filterparameter,onlineexclusive);
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
           adapter2 = new Category_List_Adapter(getContext(), arr_list_catagory,name,dir);
            rv_collection.setAdapter(new ScaleInAnimationAdapter(new AlphaInAnimationAdapter(adapter2)));
            tv_header_coll.setText(name);
        }
    }

    public void parseCategoryListModel(String response) {

        try {
            arr_list_catagory.clear();
//            Log.e("ttt", "parseResponse: " + response);
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
//                Log.e("ppp", "online exclusive ----->"+onlineexclusive);
                CategoryList_Model model = new CategoryList_Model(id, name, image, contenttype, hasfilter, filterparameter,onlineexclusive);
                arr_list_catagory.add(model);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void parseFilter(String response) {

        try {
            //mapping
            Model_Filter model_filter = gson.fromJson(response, Model_Filter.class);


            //saving in sharedPreference

            if (getActivity()!=null) {
                SharedPreferences preferences = getActivity().getSharedPreferences("filter", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("filter_arr", response);
                editor.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

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
