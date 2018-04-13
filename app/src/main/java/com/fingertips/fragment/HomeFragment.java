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

import com.fingertips.R;
import com.fingertips.activity.MainActivity;
import com.fingertips.activity.PlacesAutoCompleteActivity;
import com.fingertips.adapter.ItemAdapter;
import com.fingertips.app.Constants;
import com.fingertips.helper.TextViewPlus;
import com.fingertips.interfaces.IOkHttpNotify;
import com.fingertips.model.CategoryModel;
import com.fingertips.okhttp.OKHttpAPICalls;
import com.fingertips.util.ToastUtils;
import com.google.android.gms.common.api.Api;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Deepanshu on 3/6/2018.
 */

public class HomeFragment extends Fragment implements ItemAdapter.ItemClickInterface, IOkHttpNotify {

    @BindView(R.id.tv_location)
    TextViewPlus tvLocation;

    @BindView(R.id.rv_list)
    RecyclerView rvItem;

    @BindView(R.id.ll_location)
    CardView cardLocation;

    private Bundle dataBundle;
    private MainActivity mActivity;

    private OKHttpAPICalls okHttpAPICalls;
    private double current_latitude;
    private double current_longitude;
    private String pickupAddress;
    private ItemAdapter itemAdapter;
    private Double latUserPickup = 0.0;
    private Double lngUserPickup = 0.0;
    private List<CategoryModel> categoryList = new ArrayList<>();


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
        return inflater.inflate(R.layout.fragment_home, container, false);
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
        okHttpAPICalls.run(Constants.RequestTag.CATEGORY_LIST,null);
    }

    private void setUpRecyclerView() {
        rvItem.setLayoutManager(new LinearLayoutManager(mActivity));
        rvItem.hasFixedSize();
        itemAdapter = new ItemAdapter(mActivity,categoryList,this);
        rvItem.setAdapter(itemAdapter);


    }

    private void initializeLayouts() {
        dataBundle = getArguments();
        okHttpAPICalls = new OKHttpAPICalls();
        okHttpAPICalls.setOnOkHttpNotifyListener(this);
        latUserPickup = dataBundle.getDouble(Constants.Extras.LATITUDE);
        lngUserPickup = dataBundle.getDouble(Constants.Extras.LONGITUDE);
        pickupAddress = Constants.getAddress(mActivity,current_latitude,current_longitude);
        tvLocation.setText(pickupAddress);
    }

    @OnClick({R.id.ll_location})
    public void onClick(View view) {
         switch(view.getId()){
             case R.id.ll_location:
                 Intent intent1 = new Intent(mActivity, PlacesAutoCompleteActivity.class);
                 startActivityForResult(intent1, Constants.Extras.PICKUP_PLACE_ACTIVITY_REQUEST_CODE);
                 break;
         }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.Extras.PICKUP_PLACE_ACTIVITY_REQUEST_CODE && resultCode == Constants.Extras.PLACE_ACTIVITY_RESULT && data != null) {
            Bundle bundle = data.getBundleExtra(Constants.Extras.DATA);
            if (bundle != null) {
                latUserPickup = bundle.getDouble(Constants.Extras.LATITUDE);
                lngUserPickup = bundle.getDouble(Constants.Extras.LONGITUDE);
                Log.e("activity result", bundle.getString(Constants.Extras.PLACE_NAME));
                tvLocation.setText(bundle.getString(Constants.Extras.PLACE_NAME));
            }
        }
    }


    @Override
    public void onItemClicked(CategoryModel model) {
        JSONObject data = new JSONObject();
        try {
            data.put("lat",latUserPickup);
            data.put("lng",lngUserPickup);
            data.put("categoryId",model.getId());
            Log.e("datat value", data.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onHttpRequestSuccess(String requestType, Response response) throws IOException {
        String jsonRequest = response.body().string();
        switch(requestType){
            case Constants.RequestTag.CATEGORY_LIST:
                try {
                    JSONObject dataObject = new JSONObject(jsonRequest);
                    JSONObject statusObject = dataObject.getJSONObject("status");
                    final String message  = statusObject.getString("message");
                    boolean isSuccess = statusObject.getBoolean("status");
                    if(isSuccess) {
                        JSONArray array = new JSONArray(jsonRequest);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            CategoryModel model = new CategoryModel();
                            model.setId(object.getString("id"));
                            model.setName(object.getString("category"));
                            categoryList.add(model);
                        }
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mActivity.hideLoader();
                                itemAdapter.notifyDataSetChanged();
                            }
                        });
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
