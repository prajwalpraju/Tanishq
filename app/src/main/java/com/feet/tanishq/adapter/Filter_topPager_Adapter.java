package com.feet.tanishq.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.feet.tanishq.R;
import com.feet.tanishq.model.ModelTopFilterNew;
import com.feet.tanishq.model.Model_TopFilter;

import java.util.ArrayList;

/**
 * Created by asif on 20-05-2016.
 */
public class Filter_topPager_Adapter extends RecyclerView.Adapter<Filter_topPager_Adapter.MyViewHolder>{

    Context context;
    ArrayList<ModelTopFilterNew> arr_list;
    public Filter_topPager_Adapter(Context context,ArrayList<ModelTopFilterNew> arr_list){
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

        ModelTopFilterNew model=arr_list.get(position);
        holder.tv_filter.setAlpha(0.5f);
        holder.tv_filter.setText(model.getTitle());

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
