package com.feet.tanishq.fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.feet.tanishq.R;
import com.feet.tanishq.adapter.CompareAdapter;
import com.feet.tanishq.database.DataBaseHandler;
import com.feet.tanishq.model.Model_Product;
import com.feet.tanishq.utils.AsifUtils;
import com.feet.tanishq.utils.SpacesItemDecoration;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Compare_List#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Compare_List extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public Compare_List() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Compare_List.
     */
    // TODO: Rename and change types and number of parameters
    public static Compare_List newInstance(String param1, String param2) {
        Compare_List fragment = new Compare_List();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static Compare_List newInstance(){
        Compare_List frag=new Compare_List();
        return  frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        new GetFromCompareList().execute();
    }

    RecyclerView rv_compare;
    RelativeLayout rl_nocompare;
    TextView tv_header_compare,tv_nocompare;
//    GridLayoutManager gridLayoutManager;
    LinearLayoutManager linearLayoutManager;
    ImageView iv_delete;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_compare__list, container, false);
        rv_compare=(RecyclerView) view.findViewById(R.id.rv_compare);
        rl_nocompare=(RelativeLayout) view.findViewById(R.id.rl_nocompare);
        tv_header_compare=(TextView) view.findViewById(R.id.tv_header_compare);
        tv_nocompare=(TextView) view.findViewById(R.id.tv_nocompare);
        iv_delete=(ImageView) view.findViewById(R.id.iv_delete);
        tv_header_compare.setTypeface(AsifUtils.getRaleWay_Bold(getContext()));
        tv_nocompare.setTypeface(AsifUtils.getRaleWay_Medium(getContext()));
        linearLayoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
//        rv_compare.setHasFixedSize(true);
        rv_compare.setLayoutManager(linearLayoutManager);
        rv_compare.addItemDecoration(new SpacesItemDecoration(30));
        iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getCountCompareList()>0) {
                    AlertDialog diaBox = AskOption();
                    diaBox.show();
                }

            }
        });

        return view;

    }

    private AlertDialog AskOption()
    {
        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(getContext())
                //set message, title, and icon
                .setTitle("Delete")
                .setMessage("Do you want to Remove All?")
                .setIcon(R.drawable.delete)
                .setPositiveButton("Remove", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        //your deleting code
                            new DeleteAllCompareList().execute();
                        dialog.dismiss();
                    }

                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                })
                .create();
        return myQuittingDialogBox;

    }

    SQLiteDatabase db;
    ArrayList<Model_Product> arr_list=new ArrayList<Model_Product>();
    CompareAdapter adapter;

    private void getValuesFromCompareList() {
        try {
            db=getActivity().openOrCreateDatabase(DataBaseHandler.DATABASE_NAME, Context.MODE_PRIVATE,null);
            Cursor cs=db.rawQuery("select * from compare",null);
            if (cs.moveToFirst()){
                do {
                    String device_image=cs.getString(cs.getColumnIndex("device_image"));
                    String product_image=cs.getString(cs.getColumnIndex("product_image"));
                    String product_title=cs.getString(cs.getColumnIndex("product_title"));
                    String product_price=cs.getString(cs.getColumnIndex("product_price"));
                    String discount_price=cs.getString(cs.getColumnIndex("discount_price"));
                    String discount_percent=cs.getString(cs.getColumnIndex("discount_percent"));

                    String description=cs.getString(cs.getColumnIndex("description"));
                    String collection=cs.getString(cs.getColumnIndex("collection"));
                    String material=cs.getString(cs.getColumnIndex("material"));
                    String category=cs.getString(cs.getColumnIndex("category"));
                    String product_url=cs.getString(cs.getColumnIndex("product_url"));

                    Model_Product model_product=new Model_Product(device_image,product_image,product_title,product_price,discount_price,discount_percent,
                            description,collection,material,category,product_url,false,false,"","");
                    arr_list.add(model_product);

                } while (cs.moveToNext());
            }else {
                //visible other
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        rl_nocompare.setVisibility(View.VISIBLE);
                    }
                });

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

    }

    public long getCountCompareList(){
        long count =0;

        try {
            db = getActivity().openOrCreateDatabase(DataBaseHandler.DATABASE_NAME, Context.MODE_PRIVATE, null);
            count= DatabaseUtils.queryNumEntries(db, DataBaseHandler.TABLE_COMPARE);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return count;
    }
    public void deleteAllCompareList(){
        try {
            db = getActivity().openOrCreateDatabase(DataBaseHandler.DATABASE_NAME, Context.MODE_PRIVATE, null);
            db.delete(DataBaseHandler.TABLE_COMPARE,null,null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

    }

    class DeleteAllCompareList extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            deleteAllCompareList();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            arr_list.clear();
            adapter.notifyDataSetChanged();
            Intent intent=new Intent("filter");
            intent.putExtra("type", 3);
            intent.putExtra("notify", 2);
            LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
            rl_nocompare.setVisibility(View.VISIBLE);
        }
    }

    class GetFromCompareList extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            getValuesFromCompareList();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter=new CompareAdapter(getContext(),arr_list);
            rv_compare.setAdapter(new ScaleInAnimationAdapter(new AlphaInAnimationAdapter(adapter)));
        }
    }

}
