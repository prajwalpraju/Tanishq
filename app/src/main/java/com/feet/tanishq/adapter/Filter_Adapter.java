package com.feet.tanishq.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.feet.tanishq.R;
import com.feet.tanishq.model.Model_Filter;

import java.util.ArrayList;

/**
 * Created by asif on 13-04-2016.
 */
public class Filter_Adapter extends RecyclerView.Adapter<Filter_Adapter.MyViewHolder>{
    Context context;
    ArrayList<Model_Filter> arr_list;

    public Filter_Adapter(Context context,ArrayList<Model_Filter> arr_list){
        this.context=context;
        this.arr_list=arr_list;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.filter_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Model_Filter filter=arr_list.get(position);
        holder.tv_filter.setText(filter.getItem_name());

    }

    @Override
    public int getItemCount() {
        return arr_list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tv_filter;
        CardView cv_filter;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_filter=(TextView) itemView.findViewById(R.id.tv_filter);
            cv_filter=(CardView) itemView.findViewById(R.id.cv_filter);
            cv_filter.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
                arr_list.remove(getAdapterPosition());
                notifyItemRemoved(getAdapterPosition());
                notifyDataSetChanged();
//            Intent intent=new Intent("filter");
//            intent.putExtra("type",0);
//            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        }
    }

}
