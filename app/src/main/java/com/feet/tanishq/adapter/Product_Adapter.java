package com.feet.tanishq.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.feet.tanishq.R;
import com.feet.tanishq.interfaces.AdapterCallback;
import com.feet.tanishq.interfaces.OnItemClickListener;
import com.feet.tanishq.model.Model_Product;
import com.feet.tanishq.model.Model_TopFilter;
import com.feet.tanishq.utils.AsifUtils;
import com.feet.tanishq.utils.Singleton_volley;

import java.util.ArrayList;

/**
 * Created by asif on 15-04-2016.
 */
public class Product_Adapter extends RecyclerView.Adapter<Product_Adapter.MyViewHolder>{
    Context context;
    ArrayList<Model_Product> arr_list;
    ArrayList<Model_TopFilter> arr_top;
    ImageLoader imageLoader;
//    OnItemClickListener onItemClickListener;
    AdapterCallback adapterCallback;

    public Product_Adapter(Context context,ArrayList<Model_Product> arr_list,ArrayList<Model_TopFilter> arr_top){
        this.context=context;
        this.arr_list=arr_list;
        this.arr_top=arr_top;
        this.adapterCallback=((AdapterCallback)context);
        this.imageLoader= Singleton_volley.getInstance().getImageLoader();

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.product_item,null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Model_Product model=arr_list.get(position);
        holder.tv_product_name.setText(model.getProduct_title());

        try {
            holder.nv_product.setImageUrl(model.getProduct_image(), imageLoader);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

//    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
//        this.onItemClickListener=onItemClickListener;
//
//    }

    @Override
    public int getItemCount() {
        return arr_list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        CardView card;
        NetworkImageView nv_product;
        TextView tv_product_name;

        public MyViewHolder(View view) {
            super(view);

            card=(CardView) view.findViewById(R.id.card);
            nv_product=(NetworkImageView) view.findViewById(R.id.nv_product);
            tv_product_name=(TextView) view.findViewById(R.id.tv_product_name);
            tv_product_name.setTypeface(AsifUtils.getRaleWay_SemiBold(context));
            card.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
//            onItemClickListener.onItemClick(v,getAdapterPosition());
            adapterCallback.onMethodCallbackArr(arr_list,arr_top);
        }
    }
}
