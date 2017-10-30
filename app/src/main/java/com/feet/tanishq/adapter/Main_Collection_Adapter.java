package com.feet.tanishq.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.feet.tanishq.R;
import com.feet.tanishq.interfaces.AdapterCallback;
import com.feet.tanishq.model.Model_MainCollection;
import com.feet.tanishq.utils.AsifUtils;
import com.feet.tanishq.utils.Singleton_volley;

import java.util.ArrayList;

/**
 * Created by asif on 11-04-2016.
 */
public class Main_Collection_Adapter extends RecyclerView.Adapter<Main_Collection_Adapter.MyHolder>{

    Context context;
    ArrayList<Model_MainCollection> arr_list;
    AdapterCallback adapterCallback;

    ImageLoader imageLoader;
    public Main_Collection_Adapter(Context context, ArrayList<Model_MainCollection> arr_list){
        this.context=context;
        this.arr_list=arr_list;
        this.adapterCallback=((AdapterCallback)context);
        imageLoader= Singleton_volley.getInstance().getImageLoader();
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.main_collection_item, null);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, int position) {

        Model_MainCollection model=arr_list.get(holder.getAdapterPosition());



        holder.tv_product_name.setText(model.getName());
        try {

            holder.nv_product.setImageUrl(model.getImage(),imageLoader);


        } catch (Exception e) {
            e.printStackTrace();

        }

        if(model.getOnlineexclusive().equals("1")){
            holder.iv_online_exclusive.setVisibility(View.VISIBLE);
        }
        else {
            holder.iv_online_exclusive.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return arr_list.size();
    }

     class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

         CardView card;
         NetworkImageView nv_product;
         TextView tv_product_name;
         ImageView iv_online_exclusive;
         ProgressBar pg;

        public MyHolder(View view) {
            super(view);
            card=(CardView) view.findViewById(R.id.card);
            nv_product=(NetworkImageView) view.findViewById(R.id.nv_product);
            pg=(ProgressBar) view.findViewById(R.id.pg);
            iv_online_exclusive=(ImageView) view.findViewById(R.id.iv_online_exclusive);
            tv_product_name=(TextView) view.findViewById(R.id.tv_product_name);
            tv_product_name.setTypeface(AsifUtils.getRaleWay_SemiBold(context));
            card.setOnClickListener(this);
            if(arr_list.size() > 4)
            {
                nv_product.setScaleType(ImageView.ScaleType.FIT_XY);
            }
        }

        @Override
        public void onClick(View v) {

            adapterCallback.onMethodCallback(arr_list.get(getAdapterPosition()).getId(),arr_list.get(getAdapterPosition()).getContenttype(),arr_list.get(getAdapterPosition()).getHasfilter(),arr_list.get(getAdapterPosition()).getName(),arr_list.get(getAdapterPosition()).getFilterparameter(),"");

        }
    }
}
