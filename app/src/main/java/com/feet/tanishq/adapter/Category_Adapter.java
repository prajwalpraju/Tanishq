package com.feet.tanishq.adapter;

import android.content.Context;
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
        View view= LayoutInflater.from(context).inflate(R.layout.category_item,null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Model_Category model=arr_list.get(position);
        holder.tv_cat_name.setText(model.getName());
        if(model.isSelected()){
            holder.iv_tick.setVisibility(View.VISIBLE);
        }else {
            holder.iv_tick.setVisibility(View.GONE);
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
            cv_card.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            Model_Category model=arr_list.get(getAdapterPosition());
            if (model.isSelected())
            {
                model.setIsSelected(false);
            }else{
                model.setIsSelected(true);
            }

            Toast.makeText(context,""+getAdapterPosition(),Toast.LENGTH_SHORT).show();
        }
    }
}
