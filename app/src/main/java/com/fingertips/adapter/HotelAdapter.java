package com.fingertips.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fingertips.R;
import com.fingertips.helper.TextViewPlus;
import com.fingertips.model.HotelModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by deepanshurustagi on 4/13/18.
 */

public class HotelAdapter  extends RecyclerView.Adapter<HotelAdapter.ViewHolder> {
    private Context context;
    private List<HotelModel> itemList = new ArrayList<>();
    private ItemClickInterface itemClickInterface;
    

    public HotelAdapter(Context context, List<HotelModel> itemList,ItemClickInterface itemClickInterface){
        this.context=context;
        this.itemList = itemList;
        this.itemClickInterface = itemClickInterface;
    }

    @Override
    public HotelAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.layout_item, parent, false);
        return new HotelAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(HotelAdapter.ViewHolder holder, final int position) {
        final HotelModel model = itemList.get(position);
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
        public void onItemClicked(HotelModel model);
    }

}

