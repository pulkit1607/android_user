package com.fingertips.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fingertips.R;
import com.fingertips.helper.TextViewPlus;
import com.fingertips.model.CategoryModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Deepanshu on 3/7/2018.
 */

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    private Context context;
    private List<CategoryModel> itemList = new ArrayList<>();
    private ItemClickInterface itemClickInterface;

    public ItemAdapter(Context context, List<CategoryModel> itemList,ItemClickInterface itemClickInterface){
        this.context=context;
        this.itemList = itemList;
        this.itemClickInterface = itemClickInterface;
    }

    @Override
    public ItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.layout_item, parent, false);
        return new ItemAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ItemAdapter.ViewHolder holder, final int position) {
        final CategoryModel model = itemList.get(position);
        holder.tvItem.setText(itemList.get(position).getName());

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
        public void onItemClicked(CategoryModel model);
    }

}


