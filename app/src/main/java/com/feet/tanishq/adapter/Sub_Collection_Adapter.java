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
import com.feet.tanishq.R;
import com.feet.tanishq.model.Model_SubColl;
import com.feet.tanishq.utils.AsifUtils;
import com.feet.tanishq.utils.Singleton_volley;

import java.util.ArrayList;

/**
 * Created by asif on 14-04-2016.
 */
public class Sub_Collection_Adapter extends RecyclerView.Adapter<Sub_Collection_Adapter.MyHolder> {

    Context context;
    ArrayList<Model_SubColl> arr_list;


    ImageLoader imageLoader;
    public Sub_Collection_Adapter(Context context, ArrayList<Model_SubColl> arr_list){
        this.context=context;
        this.arr_list=arr_list;
        imageLoader= Singleton_volley.getInstance().getImageLoader();
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.product_item, null);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {

        Model_SubColl model=arr_list.get(holder.getAdapterPosition());
        holder.tv_product_name.setText(model.getItem_name());
        try {
            holder.nv_product.setImageUrl(model.getItem_image(),imageLoader);
        } catch (Exception e) {
            e.printStackTrace();
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

        public MyHolder(View view) {
            super(view);
            card=(CardView) view.findViewById(R.id.card);
            nv_product=(NetworkImageView) view.findViewById(R.id.nv_product);
            tv_product_name=(TextView) view.findViewById(R.id.tv_product_name);
            tv_product_name.setTypeface(AsifUtils.getRaleWay_SemiBold(context));
            card.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            Toast.makeText(context, "sub= " + getAdapterPosition(), Toast.LENGTH_SHORT).show();

        }
    }
}