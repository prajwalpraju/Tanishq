package com.feet.tanishq.fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.feet.tanishq.R;
import com.feet.tanishq.adapter.All_Collection_Adapter;
import com.feet.tanishq.model.Model_AllCollection;
import com.feet.tanishq.utils.AsifUtils;
import com.feet.tanishq.utils.AsyncTaskCompleteListener;
import com.feet.tanishq.utils.Const;
import com.feet.tanishq.utils.SpacesItemDecoration;
import com.feet.tanishq.utils.UserDetails;
import com.feet.tanishq.utils.VolleyHttpRequest;

import org.json.JSONArray;
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
 * {@link All_Collection.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link All_Collection#newInstance} factory method to
 * create an instance of this fragment.
 */
public class All_Collection extends Fragment implements AsyncTaskCompleteListener,Response.ErrorListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static All_Collection newInstance(){
        All_Collection fragment=new All_Collection();
        return fragment;
    }

    ArrayList<Model_AllCollection> arr_list=new ArrayList<Model_AllCollection>();
    RequestQueue requestQueue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        requestQueue= Volley.newRequestQueue(getContext());
        callProductListApi();
    }

    private void callProductListApi() {
            if (!AsifUtils.isNetworkAvailable(getActivity())) {
                Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                return;
            }

            UserDetails user=new UserDetails(getContext());

            HashMap<String,String> params=new HashMap<String,String>();
            params.put(Const.URL,Const.ALL_COLLECTIONS);
            params.put(Const.Params.ID,user.getUserId());
            requestQueue.add(new VolleyHttpRequest(Request.Method.GET,params,Const.ServiceCode.ALL_COLLECTIONS,this,this));
    }


    RecyclerView rv_collection;
    GridLayoutManager gridLayoutManager;
    TextView tv_header_coll;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_all__collection, container, false);

        tv_header_coll=(TextView) view.findViewById(R.id.tv_header_coll);
        tv_header_coll.setTypeface(AsifUtils.getRaleWay_Bold(getContext()));
        rv_collection=(RecyclerView) view.findViewById(R.id.rv_collection);
        gridLayoutManager=new GridLayoutManager(getActivity(),3);
        rv_collection.setHasFixedSize(true);
        rv_collection.setLayoutManager(gridLayoutManager);
        rv_collection.setItemAnimator(new SlideInUpAnimator(new OvershootInterpolator(1f)));
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
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {

        switch (serviceCode){
            case Const.ServiceCode.ALL_COLLECTIONS:
                if (AsifUtils.validateResponse(getContext(),response)){
                    new ParseCollectionResponse(response).execute();
                }

                break;
        }

    }

    @Override
    public void onErrorResponse(VolleyError error) {
        AsifUtils.validateResponse(getContext(), error.getMessage());
    }

    class ParseCollectionResponse extends AsyncTask<Void,Void,Void>{
        String response;

        ParseCollectionResponse(String response){
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

            All_Collection_Adapter adapter=new All_Collection_Adapter(getContext(),arr_list);
            rv_collection.setAdapter(new ScaleInAnimationAdapter(new AlphaInAnimationAdapter(adapter)));
        }
    }

    private void parseResponse(String response){
        try {
            JSONObject jObj=new JSONObject(response);
            JSONArray jArr=jObj.getJSONArray("response");
            int size=jArr.length();
            for (int i=0;i<size;i++){
                JSONObject jObjArr=jArr.getJSONObject(i);
                String id=jObjArr.getString("id");
                String name=jObjArr.getString("name");
                String image=jObjArr.getString("image");
                Model_AllCollection model=new Model_AllCollection(id,name,image);
                arr_list.add(model);
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
