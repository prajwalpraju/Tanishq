package com.feet.tanishq.fragments;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.feet.tanishq.R;
import com.feet.tanishq.adapter.WishAdapter;
import com.feet.tanishq.database.DataBaseHandler;
import com.feet.tanishq.model.Model_Product;
import com.feet.tanishq.utils.AsifUtils;
import com.feet.tanishq.utils.SpacesItemDecoration;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link Wish_List#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Wish_List extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

//    private OnFragmentInteractionListener mListener;

    public Wish_List() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters

    public static Wish_List newInstance(){
        Wish_List frag=new Wish_List();
        return  frag;
    }

    public static Wish_List newInstance(String param1, String param2) {
        Wish_List fragment = new Wish_List();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        new GetFromWishList().execute();
    }
    SQLiteDatabase db;
    ArrayList<Model_Product> arr_list=new ArrayList<Model_Product>();
    WishAdapter adapter;

    private void getValuesFromWishList() {
        try {
            db=getActivity().openOrCreateDatabase(DataBaseHandler.DATABASE_NAME,Context.MODE_PRIVATE,null);
            Cursor cs=db.rawQuery("select * from wishlist",null);
            if (cs.moveToFirst()){
                do {
                    String product_image=cs.getString(cs.getColumnIndex("product_image"));
                    String product_title=cs.getString(cs.getColumnIndex("product_title"));
                    String product_price=cs.getString(cs.getColumnIndex("product_price"));
                    String discount_price=cs.getString(cs.getColumnIndex("discount_price"));
                    String discount_percent=cs.getString(cs.getColumnIndex("discount_percent"));
                    String product_url=cs.getString(cs.getColumnIndex("product_url"));
                    Model_Product model_product=new Model_Product(product_image,product_title,product_price,discount_price,discount_percent,product_url,false,false);
                    arr_list.add(model_product);

                } while (cs.moveToNext());
            }else {
                //visible other
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        rl_nowish.setVisibility(View.VISIBLE);
                    }
                });

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

    }


    RecyclerView rv_wish;
    RelativeLayout rl_nowish;
    TextView tv_header_wish,tv_nowish;
    GridLayoutManager gridLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_wish__list, container, false);
        rv_wish=(RecyclerView) view.findViewById(R.id.rv_wish);
        rl_nowish=(RelativeLayout) view.findViewById(R.id.rl_nowish);
        tv_header_wish=(TextView) view.findViewById(R.id.tv_header_wish);
        tv_nowish=(TextView) view.findViewById(R.id.tv_nowish);
        tv_header_wish.setTypeface(AsifUtils.getRaleWay_Bold(getContext()));
        tv_nowish.setTypeface(AsifUtils.getRaleWay_Medium(getContext()));
        gridLayoutManager=new GridLayoutManager(getActivity(),3);
        rv_wish.setHasFixedSize(true);
        rv_wish.setLayoutManager(gridLayoutManager);
        rv_wish.addItemDecoration(new SpacesItemDecoration(30));
        return view;
    }


    class GetFromWishList extends AsyncTask<Void,Void,Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            getValuesFromWishList();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter=new WishAdapter(getContext(),arr_list);
            rv_wish.setAdapter(new ScaleInAnimationAdapter(new AlphaInAnimationAdapter(adapter)));
        }
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
}
