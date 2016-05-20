package com.feet.tanishq.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.feet.tanishq.R;
import com.feet.tanishq.database.DataBaseHandler;
import com.feet.tanishq.model.Model_AllCollection;
import com.feet.tanishq.model.Model_Product;
import com.feet.tanishq.utils.AsifUtils;
import com.feet.tanishq.utils.Singleton_volley;

import java.util.ArrayList;

/**
 * Created by asif on 27-04-2016.
 */
public class WishAdapter extends RecyclerView.Adapter<WishAdapter.MyHolder>{

    Context context;
    ArrayList<Model_Product> arr_list;
    ImageLoader imageLoader;

    public WishAdapter(Context context,ArrayList<Model_Product> arr_list){
        this.context=context;
        this.arr_list=arr_list;
        this.imageLoader= Singleton_volley.getInstance().getImageLoader();

    }
    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.product_item, null);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        Model_Product model=arr_list.get(holder.getAdapterPosition());
        holder.tv_product_name.setText(model.getProduct_title());
        try {
            holder.nv_product.setImageUrl(model.getDevice_image(),imageLoader);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return arr_list.size();
    }

    SQLiteDatabase db;
    private synchronized void deleteFromWish(String product_title){
        try {
            db=context.openOrCreateDatabase(DataBaseHandler.DATABASE_NAME, Context.MODE_PRIVATE, null);
            db.delete(DataBaseHandler.TABLE_WISHLIST,"product_title = ?",new String[]{product_title});
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    class MyHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener{

        CardView card;
        NetworkImageView nv_product;
        TextView tv_product_name;

        public MyHolder(View view) {
            super(view);

            card=(CardView) view.findViewById(R.id.card);
            nv_product=(NetworkImageView) view.findViewById(R.id.nv_product);
            tv_product_name=(TextView) view.findViewById(R.id.tv_product_name);
            tv_product_name.setTypeface(AsifUtils.getRaleWay_SemiBold(context));
            card.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            Model_Product model_product=arr_list.get(getAdapterPosition());
            Intent intent=new Intent("filter");
            intent.putExtra("type",3);
            intent.putExtra("notify", 1);
            deleteFromWish(model_product.getProduct_title());
            arr_list.remove(getAdapterPosition());
            notifyDataSetChanged();
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            return false;
        }
    }
}
