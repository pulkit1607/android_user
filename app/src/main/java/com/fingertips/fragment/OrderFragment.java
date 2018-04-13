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
import com.fingertips.activity.SettingActivity;
import com.fingertips.adapter.ItemAdapter;
import com.fingertips.adapter.OrderAdapter;
import com.fingertips.app.Constants;
import com.fingertips.helper.TextViewPlus;
import com.fingertips.interfaces.IOkHttpNotify;
import com.fingertips.model.CategoryModel;
import com.fingertips.model.OrderModel;
import com.fingertips.okhttp.OKHttpAPICalls;
import com.fingertips.util.ToastUtils;
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
 * Created by deepanshurustagi on 4/6/18.
 */

public class OrderFragment extends Fragment implements OrderAdapter.ItemClickInterface, IOkHttpNotify {

    @BindView(R.id.tv_text)
    TextViewPlus tvText;

    @BindView(R.id.rv_orders)
    RecyclerView rvItem;

    @BindView(R.id.ll_no_order)
    LinearLayout llNoOrder;


    private SettingActivity mActivity;

    private OKHttpAPICalls okHttpAPICalls;
    private OrderAdapter orderAdapter;
    private List<OrderModel> orderList = new ArrayList<>();


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (SettingActivity) context;
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
        okHttpAPICalls.run(Constants.RequestTag.GET_ORDERS,null);
    }

    private void setUpRecyclerView() {
        rvItem.setLayoutManager(new LinearLayoutManager(mActivity));
        rvItem.hasFixedSize();
        orderAdapter = new OrderAdapter(mActivity,orderList,this);
        rvItem.setAdapter(orderAdapter);


    }

    private void initializeLayouts() {

        okHttpAPICalls = new OKHttpAPICalls();
        okHttpAPICalls.setOnOkHttpNotifyListener(this);

    }



    @Override
    public void onItemClicked(OrderModel model) {


    }

    @Override
    public void onHttpRequestSuccess(String requestType, Response response) throws IOException {
        String jsonRequest = response.body().string();
        switch(requestType){
            case Constants.RequestTag.GET_ORDERS:
                try {
                    final JSONObject object = new JSONObject(jsonRequest);
                    JSONObject statusObject = object.getJSONObject("status");
                    final String message  = statusObject.getString("message");
                    boolean isSuccess = statusObject.getBoolean("status");
                    if(isSuccess){
                        JSONArray details = object.getJSONArray("details");
                        if(details.length()>0){
                            for (int i = 0;i<details.length();i++){
                                OrderModel model = new OrderModel();
                                JSONObject data = details.getJSONObject(i);
                                model.setOrder_id(data.getString("order_id"));
                                orderList.add(model);
                            }
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mActivity.hideLoader();
                                    orderAdapter.notifyDataSetChanged();

                                }
                            });
                        }else{
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mActivity.hideLoader();
                                   llNoOrder.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    }else{
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mActivity.hideLoader();
                                ToastUtils.showShortToast(mActivity,message);
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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
