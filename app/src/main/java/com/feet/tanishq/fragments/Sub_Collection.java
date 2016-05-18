package com.feet.tanishq.fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.feet.tanishq.adapter.Sub_Collection_Adapter;
import com.feet.tanishq.model.Model_SubColl;
import com.feet.tanishq.utils.AsifUtils;
import com.feet.tanishq.utils.AsyncTaskCompleteListener;
import com.feet.tanishq.utils.Const;
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
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Sub_Collection#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Sub_Collection extends Fragment implements AsyncTaskCompleteListener,Response.ErrorListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String CAT_ID = "cat_id";
    private static final String CAT_NAME = "cat_name";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String cat_id,cat_name;

    ArrayList<Model_SubColl> arr_list=new ArrayList<Model_SubColl>();
    RequestQueue requestQueue;


    public Sub_Collection() {
        // Required empty public constructor
    }

    public static Sub_Collection newInstance(String cat_id,String cat_name){
        Sub_Collection fragment=new Sub_Collection();
        Bundle args = new Bundle();
        args.putString(CAT_ID, cat_id);
        args.putString(CAT_NAME, cat_name);
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
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
            cat_id=getArguments().getString(CAT_ID);
            cat_name=getArguments().getString(CAT_NAME);
        }

        requestQueue= Volley.newRequestQueue(getContext());

        if (!cat_id.isEmpty()&&cat_id!=null) {
            callSubCategoryApi();
        } else {
            Toast.makeText(getContext(),"No category value",Toast.LENGTH_SHORT).show();
        }

    }

    private void callSubCategoryApi(){
        if (!AsifUtils.isNetworkAvailable(getActivity())) {
            Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
            return;
        }
        AsifUtils.start(getContext());

        UserDetails user=new UserDetails(getContext());
        HashMap<String,String> map=new HashMap<String,String>();
        map.put(Const.URL,Const.COLLECTION_CATEGORY);
        map.put(Const.Params.ID, user.getUserId());
        map.put(Const.Params.COLLECTIONID, cat_id);
        requestQueue.add(new VolleyHttpRequest(Request.Method.GET,map,Const.ServiceCode.COLLECTION_CATEGORY,this,this));
    }

    RecyclerView rv_subcollection;
    GridLayoutManager gridLayoutManager;
    TextView tv_header_subcoll;
    RelativeLayout rl_nowish;
    TextView tv_nowish;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_sub__collection, container, false);
        tv_header_subcoll=(TextView) view.findViewById(R.id.tv_header_subcoll);
        rl_nowish=(RelativeLayout) view.findViewById(R.id.rl_nowish);
        tv_nowish=(TextView) view.findViewById(R.id.tv_nowish);

        tv_header_subcoll.setText(cat_name);
        tv_nowish.setTypeface(AsifUtils.getRaleWay_Medium(getContext()));
        tv_header_subcoll.setTypeface(AsifUtils.getRaleWay_Bold(getContext()));

        rv_subcollection=(RecyclerView) view.findViewById(R.id.rv_subcollection);
        gridLayoutManager=new GridLayoutManager(getActivity(),3);
        rv_subcollection.setHasFixedSize(true);
        rv_subcollection.setLayoutManager(gridLayoutManager);
        rv_subcollection.addItemDecoration(new SpacesItemDecoration(30));
        return view;
    }


    class ParseSubCollectionResponse extends AsyncTask<Void,Void,Void>{
        String response;

        ParseSubCollectionResponse(String response){
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

            Sub_Collection_Adapter adapter=new Sub_Collection_Adapter(getContext(),arr_list);
            rv_subcollection.setAdapter(new ScaleInAnimationAdapter(new AlphaInAnimationAdapter(adapter)));
        }
    }


    private void parseResponse(String response){
        try {
            JSONObject jObj=new JSONObject(response);
            JSONArray jArr=jObj.getJSONArray("response");
            int size=jArr.length();
            for (int i=0;i<size;i++){
                JSONObject jObjArr=jArr.getJSONObject(i);
                String id=jObjArr.getString("categoryid");
                String name=jObjArr.getString("categoryname");
                String image=jObjArr.getString("categoryimage");
                Model_SubColl model=new Model_SubColl(cat_id,cat_name,id,name,image);
                arr_list.add(model);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @Override
    public void onTaskCompleted(String response, int serviceCode) {
        switch (serviceCode){
            case Const.ServiceCode.COLLECTION_CATEGORY:
                if (AsifUtils.validateResponse(getContext(),response)){
                    rl_nowish.setVisibility(View.GONE);
                    new ParseSubCollectionResponse(response).execute();
                }else {
                    rl_nowish.setVisibility(View.VISIBLE);
                    try {
                        tv_nowish.setText(new JSONObject(response).getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                AsifUtils.stop();

                break;
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        AsifUtils.stop();
        AsifUtils.validateResponse(getContext(), error.getMessage());



    }
}
