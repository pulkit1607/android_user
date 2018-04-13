package com.fingertips.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fingertips.R;
import com.fingertips.activity.SettingActivity;
import com.fingertips.app.Constants;
import com.fingertips.helper.TextViewPlus;
import com.fingertips.interfaces.IOkHttpNotify;
import com.fingertips.okhttp.OKHttpAPICalls;
import com.fingertips.util.ToastUtils;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by deepanshurustagi on 4/6/18.
 */

public class ProfileFragment  extends Fragment implements  IOkHttpNotify {


    @BindView(R.id.tv_name)
    TextViewPlus tvName;

    @BindView(R.id.tv_email)
    TextViewPlus tvEmail;

    @BindView(R.id.tv_mobile)
    TextViewPlus tvMobile;

    private SettingActivity mActivity;
    private OKHttpAPICalls okHttpAPICalls;




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
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        initializeLayouts();
        getProfileData();


    }

    private void getProfileData() {
        mActivity.showLoader();
        okHttpAPICalls.run(Constants.RequestTag.GET_PROFILE,null);
    }



    private void initializeLayouts() {

        okHttpAPICalls = new OKHttpAPICalls();
        okHttpAPICalls.setOnOkHttpNotifyListener(this);

    }





    @Override
    public void onHttpRequestSuccess(String requestType, Response response) throws IOException {
        String jsonRequest = response.body().string();
        switch(requestType){
            case Constants.RequestTag.GET_PROFILE:
                try {
                    JSONObject object = new JSONObject(jsonRequest);
                    JSONObject statusObject = object.getJSONObject("status");
                    final String message  = statusObject.getString("message");
                    boolean isSuccess = statusObject.getBoolean("status");
                    if(isSuccess){
                        JSONObject detailObject = object.getJSONObject("details");
                        JSONObject user = object.getJSONObject("user");
                        final String name = user.getString("first_name");
                        final String email = user.getString("email");
                        final String mobile = detailObject.getString("contact");
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mActivity.hideLoader();
                                tvName.setText(name);
                                tvEmail.setText(email);
                                tvMobile.setText(mobile);
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
