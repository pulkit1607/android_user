package com.fingertips.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.fingertips.R;
import com.fingertips.activity.MainActivity;
import com.fingertips.activity.PlacesAutoCompleteActivity;
import com.fingertips.adapter.HotelAdapter;
import com.fingertips.adapter.ItemAdapter;
import com.fingertips.app.Constants;
import com.fingertips.helper.TextViewPlus;
import com.fingertips.helper.XClass;
import com.fingertips.interfaces.IOkHttpNotify;
import com.fingertips.model.CategoryModel;
import com.fingertips.model.HotelModel;
import com.fingertips.model.OrderModel;
import com.fingertips.okhttp.OKHttpAPICalls;
import com.fingertips.util.ToastUtils;
import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by deepanshurustagi on 4/13/18.
 */

public class RestaurantFragment  extends Fragment implements  IOkHttpNotify, HotelAdapter.ItemClickInterface {



    @BindView(R.id.rv_orders)
    RecyclerView rvHotel;

    @BindView(R.id.ll_no_order)
    LinearLayout llNoHotels;

    @BindView(R.id.tv_text)
    TextViewPlus tvText;

    private Bundle dataBundle;
    private MainActivity mActivity;

    private OKHttpAPICalls okHttpAPICalls;
    private String jsonValue;
    private HotelAdapter hotelAdapter;
    private List<HotelModel> hotelList = new ArrayList<>();


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (MainActivity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        initializeLayouts();
        getCategory();
        setUpRecyclerView();

    }

    private void getCategory() {
        mActivity.showLoader();
        Bundle bundle = new Bundle();
        try {
            JSONObject object = new JSONObject(jsonValue);
            bundle.putDouble("lat",object.getDouble("lat"));
            bundle.putDouble("lng",object.getDouble("lng"));
            bundle.putString("categoryId",object.getString("categoryId"));

            okHttpAPICalls.run(Constants.RequestTag.GET_HOTELS,bundle);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void setUpRecyclerView() {
        rvHotel.setLayoutManager(new LinearLayoutManager(mActivity));
        rvHotel.hasFixedSize();
        hotelAdapter = new HotelAdapter(mActivity,hotelList,this);



    }

    private void initializeLayouts() {
        XClass obj = new XClass(mActivity);

        jsonValue = obj.getFromSharedPref(R.string.auth_json,"string","").toString();
        Log.e("JsonValue",jsonValue);
        okHttpAPICalls = new OKHttpAPICalls();
        okHttpAPICalls.setOnOkHttpNotifyListener(this);

    }

    @Override
    public void onItemClicked(HotelModel model) {
            XClass obj = new XClass(mActivity);
            obj.saveInSharedPref(R.string.auth_hotel_id,"string",String.valueOf(model.getId()));
            mActivity.setFragment(Constants.FragmentTag.restaurantFragment,null);
            Log.e("datat value", String.valueOf(model.getId()));


    }


    @Override
    public void onHttpRequestSuccess(String requestType, Response response) throws IOException {
        String jsonRequest = response.body().string();
        Log.e("Response Json",jsonRequest);
        switch(requestType){
            case Constants.RequestTag.GET_HOTELS:
                try {
                    Gson gson = new Gson();
                    JSONArray array = new JSONArray(jsonRequest);
                    if(array.length()>0) {
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            HotelModel model = gson.fromJson(object.toString(), HotelModel.class);
                            hotelList.add(model);
                        }
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mActivity.hideLoader();
                                hotelAdapter.notifyDataSetChanged();

                            }
                        });
                    }else{
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mActivity.hideLoader();
                                tvText.setText("No restaurants found in this area!");
                                llNoHotels.setVisibility(View.VISIBLE);

                            }
                        });
                    }
                } catch (JSONException e) {
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mActivity.hideLoader();
                            ToastUtils.showShortToast(mActivity,"Internal Server Error");
                        }
                    });
                }

                break;
            default:
                break;
        }
    }

    @Override
    public void onHttpRequestFailure(String requestType, Request request, String errorMessage) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mActivity.hideLoader();
                ToastUtils.showShortToast(mActivity,getString(R.string.network_error));
            }
        });
    }


}

