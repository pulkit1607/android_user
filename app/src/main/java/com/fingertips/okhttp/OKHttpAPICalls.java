package com.fingertips.okhttp;

import android.os.Bundle;
import android.util.Log;


import com.fingertips.R;
import com.fingertips.app.Constants;
import com.fingertips.app.MyApplication;
import com.fingertips.helper.XClass;
import com.fingertips.interfaces.IOkHttpNotify;
import com.fingertips.util.LogUtils;
import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import javax.xml.datatype.XMLGregorianCalendar;


public class OKHttpAPICalls {

    private final String TAG = getClass().getName();
    private OkHttpClient mClient;
    private IOkHttpNotify mIOkHttpNotify;
    private String mUrl;
    private FormEncodingBuilder mFormEncodingBuilder;
    private RequestBody mRequestBody;
    private XClass obj;

    public OKHttpAPICalls() {

        setOkHttpClient();
    }

    /**
     * Set Ok http client
     */
    private void setOkHttpClient() {
        mClient = new OkHttpClient();
        obj = MyApplication.getInstance().getSession();
        mClient.setConnectTimeout(Constants.OKHTTP.CONNECTION_TIMEOUT, TimeUnit.SECONDS);
        mClient.setReadTimeout(Constants.OKHTTP.READ_TIMEOUT, TimeUnit.SECONDS);
    }

    /**
     * Set Ok http notify click listener
     *
     * @param iOkHttpNotify OkHttpNotify Listsner
     */
    public void setOnOkHttpNotifyListener(IOkHttpNotify iOkHttpNotify) {
        this.mIOkHttpNotify = iOkHttpNotify;
    }


    public void run(String requestType, Bundle bundle) {
        switch (requestType) {
            case Constants.RequestTags.PLACES_AUTOCOMPLETE:
                String place_name = bundle.getString(Constants.Extras.PLACE_NAME);
                try {
                    mUrl = "https://maps.googleapis.com/maps/api/place/autocomplete/json?key=" + Constants.Extras.GOOGLE_API_KEY + "&input=" + URLEncoder.encode(place_name, "utf8") + "&components=country:in&region=in";
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                doOkHttpGet(requestType, mUrl);
                break;
            case Constants.RequestTags.PLACE_LAT_LNG:
                String place_id = bundle.getString(Constants.Extras.PLACE_ID);
                mUrl = "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + place_id + "&key=" + Constants.Extras.GOOGLE_API_KEY;
                doOkHttpGet(requestType, mUrl);
                break;
            case Constants.RequestTag.LOGIN:
                mUrl = Constants.Url.LOGIN_URL;
                mFormEncodingBuilder = new FormEncodingBuilder();
                mFormEncodingBuilder.add("email", bundle.getString(Constants.Extras.email));
                mFormEncodingBuilder.add("password", bundle.getString(Constants.Extras.password));
                mRequestBody = mFormEncodingBuilder.build();
                doOkHttpPost(requestType, mUrl, mRequestBody);
                break;
            case Constants.RequestTag.SIGNUP:
                mUrl = Constants.Url.SIGNUP_URL;
                mFormEncodingBuilder = new FormEncodingBuilder();
                mFormEncodingBuilder.add("email", bundle.getString(Constants.Extras.email));
                mFormEncodingBuilder.add("password", bundle.getString(Constants.Extras.password));
                mFormEncodingBuilder.add("name", bundle.getString(Constants.Extras.name));
                mFormEncodingBuilder.add("phone", bundle.getString(Constants.Extras.number));
                mRequestBody = mFormEncodingBuilder.build();
                doOkHttpPost(requestType, mUrl, mRequestBody);
                break;
            case Constants.RequestTag.CATEGORY_LIST:
                mUrl = Constants.Url.CATEGORY_LIST_URL;
                doOkHttpGetWithToken(requestType, mUrl);
                break;
            case Constants.RequestTag.GET_PROFILE:
                mUrl = Constants.Url.GET_PROFILE_URL;
                doOkHttpGetWithToken(requestType, mUrl);
                break;
            case Constants.RequestTag.GET_ORDERS:
                mUrl = Constants.Url.GET_ORDERS_URL;
                doOkHttpGetWithToken(requestType, mUrl);
                break;


        }
    }

    /**
     * Do Http post call
     *
     *
     * @param requestType request type of
     * @param url         url, where request needs to be send
     * @param requestBody request body
     */
    private void doOkHttpPost(final String requestType, String url, RequestBody requestBody) {
        //> http request
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                LogUtils.e(TAG, requestType + " Error : " + e.getMessage());
                if (mIOkHttpNotify != null) {
                    mIOkHttpNotify.onHttpRequestFailure(requestType, request, e.getMessage());
                }
            }

            @Override
            public void onResponse(Response response) throws IOException {
                LogUtils.e(TAG, requestType + " Response : " + response);
                //LogUtils.d(TAG, requestType + " Response : " + response.body().string());
                if (mIOkHttpNotify != null) {
                    mIOkHttpNotify.onHttpRequestSuccess(requestType, response);
                }
            }
        });
    }

    private void doOkHttpPostWithToken(final String requestType, String url, RequestBody requestBody) {
        //> http request
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "token "  + obj.getFromSharedPref(R.string.auth_token,"string","") )
                .post(requestBody)
                .build();
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                LogUtils.e(TAG, requestType + " Error : " + e.getMessage());
                if (mIOkHttpNotify != null) {
                    mIOkHttpNotify.onHttpRequestFailure(requestType, request, e.getMessage());
                }
            }

            @Override
            public void onResponse(Response response) throws IOException {
                LogUtils.e(TAG, requestType + " Response : " + response);
                if (mIOkHttpNotify != null) {
                    mIOkHttpNotify.onHttpRequestSuccess(requestType, response);
                }
            }
        });
    }

    /**
     * do http delete with token
     *
     * @param requestType request type
     * @param url         url
     * @param requestBody request body
     */
    private void doOkHttpDeleteWithToken(final String requestType, String url, RequestBody requestBody) {
        //> http request
        Request request = new Request.Builder()
                .url(url)
                //.addHeader("Authorization", mPrefs.getAuthorizationToken())
                .delete(requestBody)
                .build();
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                LogUtils.e(TAG, requestType + " Error : " + e.getMessage());
                if (mIOkHttpNotify != null) {
                    mIOkHttpNotify.onHttpRequestFailure(requestType, request, e.getMessage());
                }
            }

            @Override
            public void onResponse(Response response) throws IOException {
                LogUtils.e(TAG, requestType + " Response : " + response);
                if (mIOkHttpNotify != null) {
                    mIOkHttpNotify.onHttpRequestSuccess(requestType, response);
                }
            }
        });
    }


    /**
     * Do Http get call
     *
     * @param requestType request type of
     * @param url         url, where request needs to be send
     *                    body
     */
    private void doOkHttpGet(final String requestType, String url) {
        //> http request
        Request request = new Request.Builder()
                .url(url)
                .build();
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                LogUtils.e(TAG, requestType + " Error : " + e.getMessage());
                if (mIOkHttpNotify != null) {
                    mIOkHttpNotify.onHttpRequestFailure(requestType, request, e.getMessage());
                }
            }

            @Override
            public void onResponse(Response response) throws IOException {
                LogUtils.e(TAG, requestType + " Response : " + response);
                if (mIOkHttpNotify != null) {
                    mIOkHttpNotify.onHttpRequestSuccess(requestType, response);
                }
            }
        });
    }

    /**
     * Do Http get call
     *
     * @param requestType request type of
     * @param url         url, where request needs to be send
     *                    body
     */
    private void doOkHttpGetWithToken(final String requestType, String url) {
        //> http request
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "token "  + obj.getFromSharedPref(R.string.auth_token,"string",""))
                .build();
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                LogUtils.e(TAG, requestType + " Error : " + e.getMessage());
                if (mIOkHttpNotify != null) {
                    mIOkHttpNotify.onHttpRequestFailure(requestType, request, e.getMessage());
                }
            }

            @Override
            public void onResponse(Response response) throws IOException {
                LogUtils.e(TAG, requestType + " Response : " + response);
                if (mIOkHttpNotify != null) {
                    mIOkHttpNotify.onHttpRequestSuccess(requestType, response);
                }
            }
        });
    }


    /**
     * Do Http get call
     *
     * @param requestType request type of
     * @param url         url, where request needs to be send
     *                    body
     */
    private void doOkHttpPutWithToken(final String requestType, String url, RequestBody requestBody) {
        //> http PUT request
        Request request = new Request.Builder()
                .url(url)
                .put(requestBody)
                //.addHeader("Authorization", mPrefs.getAuthorizationToken())
                .build();
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                LogUtils.e(TAG, requestType + " Error : " + e.getMessage());
                if (mIOkHttpNotify != null) {
                    mIOkHttpNotify.onHttpRequestFailure(requestType, request, e.getMessage());
                }
            }

            @Override
            public void onResponse(Response response) throws IOException {
                LogUtils.e(TAG, requestType + " Response : " + response);
                if (mIOkHttpNotify != null) {
                    mIOkHttpNotify.onHttpRequestSuccess(requestType, response);
                }
            }
        });
    }



}
