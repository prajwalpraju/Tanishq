package com.feet.tanishq.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.feet.tanishq.R;
import com.feet.tanishq.model.Model_Category;
import com.feet.tanishq.utils.AsifUtils;

import java.util.ArrayList;

/**
 * Created by asif on 06-04-2016.
 */
public class Category_Adapter  extends RecyclerView.Adapter<Category_Adapter.MyViewHolder>{
    Context context;
    ArrayList<Model_Category> arr_list;

    public Category_Adapter(Context context,ArrayList<Model_Category> arr_list){
        this.context=context;
        this.arr_list=arr_list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.category_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Model_Category model=arr_list.get(position);
        holder.tv_cat_name.setText(model.getName());
        if(model.isSelected()){
            holder.iv_tick.setVisibility(View.VISIBLE);
            holder.cv_card.setCardBackgroundColor(Color.parseColor("#e0000000"));
        }else {
            holder.iv_tick.setVisibility(View.INVISIBLE);
            holder.cv_card.setCardBackgroundColor(Color.parseColor("#191818"));
        }


    }

    @Override
    public int getItemCount() {
        return arr_list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        CardView cv_card;
        TextView tv_cat_name;
        ImageView iv_tick;

        public MyViewHolder(View view){
            super(view);

            cv_card=(CardView) view.findViewById(R.id.cv_card);
            tv_cat_name=(TextView) view.findViewById(R.id.tv_cat_name);
            iv_tick=(ImageView) view.findViewById(R.id.iv_tick);
            tv_cat_name.setTypeface(AsifUtils.getRaleWay_Medium(context));
            cv_card.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            Model_Category model=arr_list.get(getAdapterPosition());
            Intent intent=new Intent("filter");
            if (model.isSelected())
            {
                intent.putExtra("type",0);
                model.setIsSelected(false);
                iv_tick.setVisibility(View.INVISIBLE);
                cv_card.setCardBackgroundColor(Color.parseColor("#191818"));
            }else{
                intent.putExtra("type",1);
                model.setIsSelected(true);
                iv_tick.setVisibility(View.VISIBLE);
                cv_card.setCardBackgroundColor(Color.parseColor("#e0000000"));
            }

            intent.putExtra("model",model);
//            intent.putExtra("position",getAdapterPosition());
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

        }
    }
}
