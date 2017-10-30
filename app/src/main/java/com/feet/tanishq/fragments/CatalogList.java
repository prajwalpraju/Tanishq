package com.feet.tanishq.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.feet.tanishq.adapter.Product_Adapter;
import com.feet.tanishq.interfaces.AdapterCallback;
import com.feet.tanishq.model.ModelTopFilterNew;
import com.feet.tanishq.model.Model_Filter;
import com.feet.tanishq.model.Model_Product;
import com.feet.tanishq.utils.AsifUtils;
import com.feet.tanishq.utils.AsyncTaskCompleteListener;
import com.feet.tanishq.utils.Const;
import com.feet.tanishq.utils.Singleton_volley;
import com.feet.tanishq.utils.UserDetails;
import com.feet.tanishq.utils.VolleyHttpRequest;
import com.google.android.gms.analytics.HitBuilders;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

/**
 * Created by Administrator on 5/1/2017.
 */

public class CatalogList extends Fragment implements AsyncTaskCompleteListener, Response.ErrorListener {

    private static final String NAME = "name";
    private static final String ID = "id";
    private static final String PARENTNAME = "parentname";

    public String name;
    public String id;
    public String parentname;

    ImageLoader imageLoader;
    RequestQueue requestQueue;

    public CatalogList() {
    }

    public static CatalogList newInstance(String name, String id,String parentname) {
        CatalogList catalogList = new CatalogList();
        Bundle args = new Bundle();
        args.putString(NAME, name);
        args.putString(ID, id);
        args.putString(PARENTNAME, parentname);
        catalogList.setArguments(args);
        return catalogList;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            name = getArguments().getString(NAME);
            id = getArguments().getString(ID);
            parentname = getArguments().getString(PARENTNAME);


        }

        Tanishq_Screen.tracker.setScreenName(parentname+" "+name+" screen");
        Tanishq_Screen.tracker.send(new HitBuilders.ScreenViewBuilder().build());
        requestQueue = Volley.newRequestQueue(getContext());


    }

    @Override
    public void onResume() {
        super.onResume();


        userDetails = new UserDetails(getContext());
        device_resolution = userDetails.getUserDevice();
    }

    private void callCatalogueApi(String id) {


        if (!AsifUtils.isNetworkAvailable(getActivity())) {
            Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
            return;
        }
        AsifUtils.start(getContext());
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(Const.URL, Const.CATALOGLIST_URL);
        map.put(Const.Params.CATALOGUEID, id);
        requestQueue.add(new VolleyHttpRequest(Request.Method.GET, map, Const.ServiceCode.COATALOGUE, this, this));

    }




    RecyclerView recyclerView;
    TextView name_of_catalogue, tv_nowish;
    RelativeLayout rl_nowish;
    ArrayList<Model_Product> arr_list_product = new ArrayList<Model_Product>();
    ArrayList<ModelTopFilterNew> arrTopFilternew = new ArrayList<>();
    String device_resolution;
    UserDetails userDetails;
    StaggeredGridLayoutManager staggeredGridLayoutManager;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.catalog_layout, container, false);


        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        name_of_catalogue = (TextView) view.findViewById(R.id.name_of_catalogue);

        rl_nowish = (RelativeLayout) view.findViewById(R.id.rl_nowish);
        tv_nowish = (TextView) view.findViewById(R.id.tv_nowish);
        tv_nowish.setTypeface(AsifUtils.getRaleWay_Medium(getContext()));

        name_of_catalogue.setText(name);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);

        callCatalogueApi(id);



        return view;
    }



    public class StaggeredAdapter extends RecyclerView.Adapter<StaggeredAdapter.ViewHolder> {


        Context context;
        ArrayList<Model_Product> arr_list;
        ArrayList<ModelTopFilterNew> arr_top;
        ImageLoader imageLoader;
        AdapterCallback adapterCallback;

        public StaggeredAdapter(Context context, ArrayList<Model_Product> arr_list, ArrayList<ModelTopFilterNew> arr_top) {
            this.context = context;
            this.arr_list = arr_list;
            this.arr_top = arr_top;
            this.adapterCallback = ((AdapterCallback) context);
            this.imageLoader = Singleton_volley.getInstance().getImageLoader();
            adapterCallback.setFilterToGone();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.catalog_layout_item, parent, false);
            ViewHolder holder = new ViewHolder(view);
            return holder;

        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {




            try {
                Model_Product model = arr_list.get(position);
                imageLoader.get(model.getCatalogue_display_image(), new ImageLoader.ImageListener() {
                    @Override
                    public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                        holder.pg.setVisibility(View.GONE);
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        holder.pg.setVisibility(View.GONE);
                    }
                });
                holder.grid_image.setImageUrl(model.getCatalogue_display_image(), imageLoader);
                holder.pg.setVisibility(View.VISIBLE);

            } catch (Exception e) {
                e.printStackTrace();
                holder.pg.setVisibility(View.GONE);
            }
        }

        @Override
        public int getItemCount() {
            return arr_list.size();
        }

        public final class ViewHolder extends RecyclerView.ViewHolder {
            NetworkImageView grid_image;
            ProgressBar pg;

            public ViewHolder(View itemView) {
                super(itemView);
                grid_image = (NetworkImageView) itemView.findViewById(R.id.grid_image);
                pg = (ProgressBar) itemView.findViewById(R.id.pg);

                grid_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Model_Product model_product=arr_list.get(getAdapterPosition());
                        if(!TextUtils.isEmpty(model_product.getProduct_title())){
                            Tanishq_Screen.reportEventToGoogle(parentname+"-"+name, "Clicks", model_product.getProduct_title());
                            adapterCallback.onMethodCallbackArr(getAdapterPosition(), arr_list, arr_top);
                        }else{
                            CallDialogue();
                        }

                    }
                });
            }

        }
    }

    AlertDialog b;

    private void CallDialogue() {


        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialogue, null);
        dialogBuilder.setView(dialogView);

        ImageView iv_close_dialogue = (ImageView) dialogView.findViewById(R.id.iv_close_dialogue);
        iv_close_dialogue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.dismiss();
            }
        });
        b = dialogBuilder.create();

        b.show();
    }


    @Override
    public void onErrorResponse(VolleyError error) {
        AsifUtils.stop();
        AsifUtils.validateResponse(getContext(), error.getMessage());
    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {
        AsifUtils.stop();
        switch (serviceCode) {
            case Const.ServiceCode.COATALOGUE:
                if (AsifUtils.validateResponse(getContext(), response)) {
                    rl_nowish.setVisibility(View.GONE);

                    new ParseCatalogueListResponse(response).execute();
                } else {
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


    class ParseCatalogueListResponse extends AsyncTask<Void, Void, Void> {
        String response;

        public ParseCatalogueListResponse(String response) {
            this.response = response;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            arr_list_product.clear();
        }

        @Override
        protected Void doInBackground(Void... params) {
            parseResponse(response);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            arrTopFilternew.clear();
            ModelTopFilterNew modelTopFilterNew = new ModelTopFilterNew(name);
            arrTopFilternew.add(modelTopFilterNew);

            Log.e("zzz", "onPostExecute: "+arr_list_product.size() );

            try {
                StaggeredAdapter adapter = new StaggeredAdapter(getContext(), arr_list_product, arrTopFilternew);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (arr_list_product.size() > 0) {
                rl_nowish.setVisibility(View.GONE);
            } else {
                rl_nowish.setVisibility(View.VISIBLE);
                tv_nowish.setText(getResources().getString(R.string.no_list));
            }

        }
    }


    private void parseResponse(String response) {


        try {
            Log.e("ggg", "parseResponse: ->" + response);
            JSONObject jObj = new JSONObject(response);
            JSONObject jResObj = jObj.getJSONObject("response");

            JSONArray jArrPro = jResObj.getJSONArray("cataloguelist");
            int size = jArrPro.length();


            for (int i = 0; i < size; i++) {

                ArrayList<String> stringArrayUrlList = new ArrayList<>();
                JSONObject obj = jArrPro.getJSONObject(i);
                String device_image = getDeviceImage(obj);
                JSONArray jsonArrayMultiAngleImages = obj.getJSONArray("product_images_list");
                for (int j = 0; j < jsonArrayMultiAngleImages.length(); j++) {
                    JSONObject objMultiAngle = jsonArrayMultiAngleImages.getJSONObject(j);
                    String url = getDeviceMultiAngleImageImage(objMultiAngle);
                    stringArrayUrlList.add(url);

                }

                String catalogue_display_image = obj.getString("catalogue_display_image");

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

                Log.e("zzz", "parseResponse: "+catalogue_display_image);


                Model_Product model_product = new Model_Product(device_image, product_image, product_title, product_price, discount_price, discount_percent,
                        description, Collection, Material, category, product_url, false, false, gold_karatage, weight, onlineexclusive,catalogue_display_image, community, occasion, stringArrayUrlList, disclaimer);
                arr_list_product.add(model_product);
            }
//            next_page = jResObj.getInt("next_page");
//            total_pages = jResObj.getInt("total_pages");
//            userDetails.savePagePosition(next_page, total_pages);
//            Log.e("qqq", "nextpage : " + next_page + "  total page : " + total_pages);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private String getDeviceMultiAngleImageImage(JSONObject obj) {
        String image = "";
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


}
