package com.fingertips.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.fingertips.R;
import com.fingertips.interfaces.PlacesInterface;
import com.fingertips.model.PlacesModel;

import java.util.List;

/**
 * Created by sahil on 2/9/16.
 */
public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.ViewHolder> {

    private List<PlacesModel.PlaceAutoComplete> placesModelList;
    private Context context;
    private PlacesInterface placesInterface;

    public PlacesAdapter(Context context, List<PlacesModel.PlaceAutoComplete> placesModelList) {
        this.context = context;
        this.placesModelList = placesModelList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.layout_item_places, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PlacesModel.PlaceAutoComplete placesModel = placesModelList.get(position);
        holder.tv_place.setText(placesModel.getPlaceDesc());
    }

    @Override
    public int getItemCount() {
        return placesModelList != null ? placesModelList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_place;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_place = (TextView) itemView.findViewById(R.id.tv_place);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    placesInterface.getPlaceDetails(placesModelList.get(pos));
                }
            });
        }
    }

    public void setPlacesInterface(PlacesInterface placesInterface) {
        this.placesInterface = placesInterface;
    }
}
