package com.fingertips.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fingertips.R;
import com.fingertips.helper.TextViewPlus;
import com.fingertips.model.CategoryModel;
import com.fingertips.model.OrderModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by deepanshurustagi on 4/7/18.
 */

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    private Context context;
    private List<OrderModel> itemList = new ArrayList<>();
    private ItemClickInterface itemClickInterface;

    public OrderAdapter(Context context, List<OrderModel> itemList,ItemClickInterface itemClickInterface){
        this.context=context;
        this.itemList = itemList;
        this.itemClickInterface = itemClickInterface;
    }

    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.layout_item, parent, false);
        return new OrderAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(OrderAdapter.ViewHolder holder, final int position) {
        final OrderModel model = itemList.get(position);
        holder.tvItem.setText(itemList.get(position).getOrder_id());

        holder.cardItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickInterface.onItemClicked(model);
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.card_item)
        CardView cardItem;

        @BindView(R.id.tv_item)
        TextViewPlus tvItem;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);



        }
    }

    public interface ItemClickInterface{
        public void onItemClicked(OrderModel model);
    }

}



