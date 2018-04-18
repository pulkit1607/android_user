package com.fingertips.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.fingertips.R;
import com.fingertips.activity.MainActivity;
import com.fingertips.adapter.HotelAdapter;
import com.fingertips.adapter.MenuAdapter;
import com.fingertips.app.Constants;
import com.fingertips.helper.TextViewPlus;
import com.fingertips.helper.XClass;
import com.fingertips.interfaces.IOkHttpNotify;
import com.fingertips.model.HotelModel;
import com.fingertips.model.MenuModel;
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

/**
 * Created by deepanshurustagi on 4/13/18.
 */

public class MenuFragment extends Fragment implements IOkHttpNotify, MenuAdapter.ItemClickInterface {



    @BindView(R.id.rv_orders)
    RecyclerView rvHotel;

    @BindView(R.id.ll_no_order)
    LinearLayout llNoHotels;

    @BindView(R.id.tv_text)
    TextViewPlus tvText;


    private MainActivity mActivity;

    private OKHttpAPICalls okHttpAPICalls;
    private String jsonValue;

    private List<MenuModel> menuList = new ArrayList<>();


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
            bundle.putString("id",object.getString("id"));

            okHttpAPICalls.run(Constants.RequestTag.GET_MENU,bundle);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void setUpRecyclerView() {
        rvHotel.setLayoutManager(new LinearLayoutManager(mActivity));
        rvHotel.hasFixedSize();




    }

    private void initializeLayouts() {
        XClass obj = new XClass(mActivity);

        jsonValue = obj.getFromSharedPref(R.string.auth_json,"string","").toString();
        Log.e("JsonValue",jsonValue);
        okHttpAPICalls = new OKHttpAPICalls();
        okHttpAPICalls.setOnOkHttpNotifyListener(this);

    }

    @Override
    public void onItemClicked(MenuModel model) {

    }


    @Override
    public void onHttpRequestSuccess(String requestType, Response response) throws IOException {
        String jsonRequest = response.body().string();
        Log.e("Response Json",jsonRequest);
        switch(requestType){
            case Constants.RequestTag.GET_MENU:
                try {
                    Gson gson = new Gson();
                    JSONArray array = new JSONArray(jsonRequest);
                    if(array.length()>0) {
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            MenuModel model = gson.fromJson(object.toString(), MenuModel.class);
                            menuList.add(model);
                        }
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mActivity.hideLoader();


                            }
                        });
                    }else{
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mActivity.hideLoader();
                                tvText.setText("No menu found for this restaurant!");
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

