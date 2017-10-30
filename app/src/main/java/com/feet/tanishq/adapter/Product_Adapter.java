package com.feet.tanishq.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.feet.tanishq.R;
import com.feet.tanishq.Tanishq_Screen;
import com.feet.tanishq.interfaces.AdapterCallback;
import com.feet.tanishq.model.ModelTopFilterNew;
import com.feet.tanishq.model.Model_Product;
import com.feet.tanishq.utils.AsifUtils;
import com.feet.tanishq.utils.Singleton_volley;
import com.feet.tanishq.utils.UserDetails;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by asif on 15-04-2016.
 */
public class Product_Adapter extends RecyclerView.Adapter<Product_Adapter.MyViewHolder>{
    Context context;
    ArrayList<Model_Product> arr_list;
    ArrayList<ModelTopFilterNew> arr_top;
    ImageLoader imageLoader;
    String directory;
    int next_page;
    int total_pages;
    AdapterCallback adapterCallback;

    public Product_Adapter(Context context, ArrayList<Model_Product> arr_list, ArrayList<ModelTopFilterNew> arr_top, String directory, int next_page, int total_pages){
        this.context=context;
        this.arr_list=arr_list;
        this.arr_top=arr_top;
        this.adapterCallback=((AdapterCallback)context);
        this.imageLoader= Singleton_volley.getInstance().getImageLoader();
        this.directory= directory;
        this.next_page = next_page;
        this.total_pages = total_pages;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.product_item,null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Model_Product model=arr_list.get(position);
        holder.tv_product_name.setText(model.getProduct_title());

        try {
            imageLoader.get(model.getDevice_image(), new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    holder.pg.setVisibility(View.GONE);
                }

                @Override
                public void onErrorResponse(VolleyError error) {
                    holder.pg.setVisibility(View.GONE);
                }
            });
            holder.nv_product.setImageUrl(model.getDevice_image(), imageLoader);
//            Log.e("url", "product Url "+model.getDevice_image() );
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

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        CardView card;
        NetworkImageView nv_product;
        TextView tv_product_name;
        ProgressBar pg;

        public MyViewHolder(View view) {
            super(view);

            card=(CardView) view.findViewById(R.id.card);
            pg=(ProgressBar) view.findViewById(R.id.pg);
            nv_product=(NetworkImageView) view.findViewById(R.id.nv_product);
            tv_product_name=(TextView) view.findViewById(R.id.tv_product_name);
            tv_product_name.setTypeface(AsifUtils.getRaleWay_SemiBold(context));
            card.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            Model_Product model_product=arr_list.get(getAdapterPosition());
            Tanishq_Screen.reportEventToGoogle(directory, "Clicks", model_product.getProduct_title());
//            Log.e("ttt", "onClick: "+directory+",Clicks,"+model_product.getProduct_title() );
            adapterCallback.onMethodCallbackArr(getAdapterPosition(), arr_list, arr_top);


            Gson gson = new Gson();
            String json = gson.toJson(arr_list);
            UserDetails user = new UserDetails(context);
//            user.setAndPreservePosition(json,next_page,total_pages);
            user.setAndPreservePosition(json);

        }
    }
}
