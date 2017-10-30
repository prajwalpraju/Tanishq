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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.feet.tanishq.R;
import com.feet.tanishq.Tanishq_Screen;
import com.feet.tanishq.adapter.Main_Collection_Adapter;
import com.feet.tanishq.interfaces.AdapterCallback;
import com.feet.tanishq.model.Model_MainCollection;
import com.feet.tanishq.utils.AsifUtils;
import com.feet.tanishq.utils.AsyncTaskCompleteListener;
import com.feet.tanishq.utils.Const;
import com.feet.tanishq.utils.SpacesItemDecoration;
import com.feet.tanishq.utils.UserDetails;
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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Main_Collection.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Main_Collection#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Main_Collection extends Fragment implements AsyncTaskCompleteListener,Response.ErrorListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

//    private OnFragmentInteractionListener mListener;

    public Main_Collection() {
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
    public static Main_Collection newInstance(String param1, String param2) {
        Main_Collection fragment = new Main_Collection();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static Main_Collection newInstance(){
        Main_Collection fragment=new Main_Collection();
        return fragment;
    }

    ArrayList<Model_MainCollection> arr_list=new ArrayList<Model_MainCollection>();
    RequestQueue requestQueue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        Tanishq_Screen.tracker.setScreenName("Main Collection Screen");
        Tanishq_Screen.tracker.send(new HitBuilders.ScreenViewBuilder().build());
        Tanishq_Screen.tracker.send(new HitBuilders.TimingBuilder().setCategory("Main Collection").setVariable("Load Time").setLabel("Screen").build());

        requestQueue= Volley.newRequestQueue(getContext());

    }

    @Override
    public void onResume() {
        super.onResume();



    }


    RecyclerView rv_collection;
    GridLayoutManager gridLayoutManager;
    TextView tv_header_coll;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_main__collection, container, false);

        tv_header_coll=(TextView) view.findViewById(R.id.tv_header_coll);
        tv_header_coll.setTypeface(AsifUtils.getRaleWay_Bold(getContext()));
        rv_collection=(RecyclerView) view.findViewById(R.id.rv_collection);


        tv_header_coll.setText("MAIN COLLECTIONS");

        callMainCollectionApi();

        return view;
    }

    private void callMainCollectionApi() {
        if (!AsifUtils.isNetworkAvailable(getActivity())) {
            Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
            return;
        }
        AsifUtils.start(getContext());
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(Const.URL, Const.MAINCOLLECTIONLIST_URL);
        requestQueue.add(new VolleyHttpRequest(Request.Method.GET, params, Const.ServiceCode.MAIN_COLLECTIONS, this, this));
    }

    // TODO: Rename method, update argument and hook method into UI event

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

        switch (serviceCode){
            case Const.ServiceCode.MAIN_COLLECTIONS:
                AsifUtils.stop();
                if (AsifUtils.validateResponse(getContext(),response)){
                    new ParseMainCollectionResponse(response).execute();
                }
//

                break;
        }

    }

    @Override
    public void onErrorResponse(VolleyError error) {
        AsifUtils.stop();
        AsifUtils.validateResponse(getContext(), error.getMessage());
    }

    class ParseMainCollectionResponse extends AsyncTask<Void,Void,Void>{
        String response;

        ParseMainCollectionResponse(String response){
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
            AsifUtils.stop();
            super.onPostExecute(aVoid);
            if(arr_list.size() <= 4)
            {

                gridLayoutManager=new GridLayoutManager(getActivity(),2);
                rv_collection.setHasFixedSize(true);
                rv_collection.setLayoutManager(gridLayoutManager);
                rv_collection.addItemDecoration(new SpacesItemDecoration(30));
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0, 0, 0, 0);
                rv_collection.setLayoutParams(layoutParams);

            }
            else
            {
                gridLayoutManager=new GridLayoutManager(getActivity(),3);
                rv_collection.setHasFixedSize(true);
                rv_collection.setLayoutManager(gridLayoutManager);
                rv_collection.addItemDecoration(new SpacesItemDecoration(30));
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                layoutParams.setMargins(0, 0, 0, 0);
                rv_collection.setLayoutParams(layoutParams);

            }

            Main_Collection_Adapter adapter=new Main_Collection_Adapter(getContext(),arr_list);
            rv_collection.setAdapter(new ScaleInAnimationAdapter(new AlphaInAnimationAdapter(adapter)));
            adapter.notifyDataSetChanged();
        }
    }

    private void parseResponse(String response){
        try {
            arr_list.clear();
            JSONObject jObj=new JSONObject(response);
            JSONObject jObj2=jObj.getJSONObject("response");
            JSONArray jArr=jObj2.getJSONArray("dashboardconfiginfo");

            int size=jArr.length();

            for (int i=0;i<size;i++){
                JSONObject jObjArr=jArr.getJSONObject(i);
                String id=jObjArr.getString("id");
                String name=jObjArr.getString("name");
                String image=jObjArr.getString("image");
                String contenttype=jObjArr.getString("contenttype");
                String hasfilter=jObjArr.getString("hasfilter");
                String filterparameter=jObjArr.getString("filterparameter");
                String onlineexclusive=jObjArr.getString("onlineexclusive");
//                Log.e("ppp", "online exclusive ----->"+onlineexclusive);
                Model_MainCollection model=new Model_MainCollection(id,name,image,contenttype,hasfilter,filterparameter,onlineexclusive);
                arr_list.add(model);



            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
