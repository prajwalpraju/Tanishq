package com.feet.tanishq.adapter;

import android.content.Context;
import android.content.DialogInterface;
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
import com.feet.tanishq.model.Model_Product;
import com.feet.tanishq.utils.AsifUtils;
import com.feet.tanishq.utils.Singleton_volley;

import java.util.ArrayList;

/**
 * Created by asif on 29-04-2016.
 */
public class CompareAdapter extends RecyclerView.Adapter<CompareAdapter.MyHolderView>{

    Context context;
    ArrayList<Model_Product> arr_list;
    ImageLoader imageLoader;

    public CompareAdapter(Context context,ArrayList<Model_Product> arr_list){
        this.context=context;
        this.arr_list=arr_list;
        this.imageLoader= Singleton_volley.getInstance().getImageLoader();

    }

    public CompareAdapter(Context context) {
        this.context = context;

    }

    @Override
    public MyHolderView onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.compare_item, parent,false);
        return new MyHolderView(view);
    }


    @Override
    public void onBindViewHolder(MyHolderView holder, int position) {
        Model_Product model=arr_list.get(holder.getAdapterPosition());
        holder.tv_com_title.setText(model.getProduct_title());
//        holder.tv_com_price.setText(context.getResources().getString(R.string.rs)+" "+model.getProduct_price());
        holder.tv_com_price.setText(model.getProduct_price());
        holder.tv_com_name.setText(model.getDescription());
        holder.tv_com_material.setText(model.getMaterial());
        holder.tv_com_category.setText(model.getCategory());
        holder.tv_com_collection.setText(model.getCollection());
        try {
            holder.nv_compare.setImageUrl(model.getDevice_image(),imageLoader);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return arr_list.size();
    }

    SQLiteDatabase db;
    private synchronized void deleteFromCompare(String product_title){
        try {
            db=context.openOrCreateDatabase(DataBaseHandler.DATABASE_NAME, Context.MODE_PRIVATE, null);
            db.delete(DataBaseHandler.TABLE_COMPARE,"product_title = ?",new String[]{product_title});
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    class MyHolderView extends RecyclerView.ViewHolder implements View.OnLongClickListener{

        CardView cv_compare;
        NetworkImageView nv_compare;
        TextView tv_com_name,tv_com_title,tv_com_price,tv_com_material,tv_com_category,tv_com_collection;

        public MyHolderView(View view) {
            super(view);

            cv_compare=(CardView) view.findViewById(R.id.cv_compare);
            nv_compare=(NetworkImageView) view.findViewById(R.id.nv_compare);
            tv_com_name=(TextView) view.findViewById(R.id.tv_com_name);
            tv_com_title=(TextView) view.findViewById(R.id.tv_com_title);
            tv_com_price=(TextView) view.findViewById(R.id.tv_com_price);
            tv_com_material=(TextView) view.findViewById(R.id.tv_com_material);
            tv_com_category=(TextView) view.findViewById(R.id.tv_com_category);
            tv_com_collection=(TextView) view.findViewById(R.id.tv_com_collection);

            tv_com_name.setTypeface(AsifUtils.getRaleWay_Bold(context));
            tv_com_title.setTypeface(AsifUtils.getRaleWay_Medium(context));
            tv_com_price.setTypeface(AsifUtils.getRaleWay_Medium(context));
            tv_com_material.setTypeface(AsifUtils.getRaleWay_Medium(context));
            tv_com_category.setTypeface(AsifUtils.getRaleWay_Medium(context));
            tv_com_collection.setTypeface(AsifUtils.getRaleWay_Medium(context));
            cv_compare.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            Model_Product model_product=arr_list.get(getAdapterPosition());
            Intent intent=new Intent("filter");
            intent.putExtra("type",3);
            intent.putExtra("notify", 2);
            deleteFromCompare(model_product.getProduct_title());
            arr_list.remove(getAdapterPosition());
            notifyDataSetChanged();
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            return false;
        }
    }
}