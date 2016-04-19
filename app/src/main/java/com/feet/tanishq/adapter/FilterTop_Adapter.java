package com.feet.tanishq.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.feet.tanishq.R;
import com.feet.tanishq.model.Model_Params;
import com.feet.tanishq.model.Model_TopFilter;

import java.util.ArrayList;

/**
 * Created by asif on 15-04-2016.
 */
public class FilterTop_Adapter extends RecyclerView.Adapter<FilterTop_Adapter.MyViewHolder>{

    Context context;
    ArrayList<Model_TopFilter> arr_list;
    public FilterTop_Adapter(Context context,ArrayList<Model_TopFilter> arr_list){
        this.context=context;
        this.arr_list=arr_list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.filter_item_trans,null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Model_TopFilter model=arr_list.get(position);
        holder.tv_filter.setText(model.getName());

    }

    @Override
    public int getItemCount() {
        return arr_list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tv_filter;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv_filter=(TextView) itemView.findViewById(R.id.tv_filter);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
