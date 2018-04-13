package com.fingertips.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fingertips.R;
import com.fingertips.adapter.PlacesAdapter;
import com.fingertips.app.Constants;
import com.fingertips.helper.TextViewPlus;
import com.fingertips.interfaces.IOkHttpNotify;
import com.fingertips.interfaces.PlacesInterface;
import com.fingertips.model.PlaceLatLngModel;
import com.fingertips.model.PlacesModel;
import com.fingertips.okhttp.OKHttpAPICalls;
import com.google.gson.Gson;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlacesAutoCompleteActivity extends AppCompatActivity implements IOkHttpNotify, View.OnClickListener {

    private RecyclerView place_recycler;
    private ImageView iv_cancel;
    private ImageView iv_back;
    private TextViewPlus tv_title;
    private ProgressBar progress_bar;
    private ImageView iv_search;
    private EditText et_search;
    private PlacesAdapter placesAdapter;
    private List<PlacesModel.PlaceAutoComplete> list = new ArrayList<>();
    private Bundle mBundle;
    private OKHttpAPICalls okHttpAPICalls;
    private LinearLayout ll_progress;
    private PlacesModel.PlaceAutoComplete placeAutoComplete;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
           setContentView(R.layout.activity_places_auto_complete);

        initializeViews();

        et_search.setHint("Search Location..");

        setSupportActionBar(toolbar);
        tv_title.setText(getString(R.string.app_name));

        setUpOkHttp();
        setUpRecyclerView();

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 2) {
                    progress_bar.setVisibility(View.VISIBLE);
                    iv_search.setVisibility(View.GONE);
                    iv_cancel.setVisibility(View.INVISIBLE);
                    mBundle.putString(Constants.Extras.PLACE_NAME, s.toString());
                    okHttpAPICalls.run(Constants.RequestTags.PLACES_AUTOCOMPLETE, mBundle);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        placesAdapter.setPlacesInterface(new PlacesInterface() {
            @Override
            public void getPlaceDetails(PlacesModel.PlaceAutoComplete placesModel) {
                placeAutoComplete = placesModel;
                ll_progress.setVisibility(View.VISIBLE);
                mBundle.putString(Constants.Extras.PLACE_ID, placesModel.getPlaceID());
                okHttpAPICalls.run(Constants.RequestTags.PLACE_LAT_LNG, mBundle);
            }
        });

        iv_cancel.setOnClickListener(this);
        ll_progress.setOnClickListener(this);
        iv_back.setOnClickListener(this);
    }

    private void setUpOkHttp() {
        okHttpAPICalls = new OKHttpAPICalls();
        okHttpAPICalls.setOnOkHttpNotifyListener(this);
    }

    private void initializeViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_title = (TextViewPlus) findViewById(R.id.tv_title);

        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);
        iv_search = (ImageView) findViewById(R.id.iv_search);
        et_search = (EditText) findViewById(R.id.et_search);
        iv_cancel = (ImageView) findViewById(R.id.iv_cancel);

        place_recycler = (RecyclerView) findViewById(R.id.place_recycler);

        ll_progress = (LinearLayout) findViewById(R.id.ll_progress);

        mBundle = new Bundle();
    }

    private void setUpRecyclerView() {
        place_recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        place_recycler.setHasFixedSize(true);
        placesAdapter = new PlacesAdapter(this, list);
        place_recycler.setAdapter(placesAdapter);
    }

    @Override
    public void onHttpRequestSuccess(String requestType, Response response) throws IOException {
        final String responseJson = response.body().string();
        Gson gson = new Gson();
        switch (requestType) {
            case Constants.RequestTags.PLACES_AUTOCOMPLETE:
                PlacesModel data = gson.fromJson(responseJson, PlacesModel.class);
                if (data != null) {
                    if (data.getPlaces() != null) {
                        if (data.getPlaces().size() != 0) {
                            list.clear();
                            list.addAll(data.getPlaces());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    placesAdapter.notifyDataSetChanged();
                                }
                            });
                        }
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progress_bar.setVisibility(View.GONE);
                        iv_search.setVisibility(View.VISIBLE);
                        iv_cancel.setVisibility(View.VISIBLE);
                    }
                });
                break;

            case Constants.RequestTags.PLACE_LAT_LNG:
                final PlaceLatLngModel placeLatLng = gson.fromJson(responseJson, PlaceLatLngModel.class);
                if (placeLatLng != null) {
                    if (placeLatLng.getResult() != null) {
                        if (placeLatLng.getResult().getGeometry() != null) {
                            if (placeLatLng.getResult().getGeometry().getLocation() != null) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ll_progress.setVisibility(View.GONE);
                                        Intent intent = new Intent();
                                        Bundle bundle = new Bundle();
                                        bundle.putString(Constants.Extras.PLACE_NAME, placeAutoComplete.getPlaceDesc());
                                        bundle.putDouble(Constants.Extras.LATITUDE, placeLatLng.getResult().getGeometry().getLocation().getLat());
                                        bundle.putDouble(Constants.Extras.LONGITUDE, placeLatLng.getResult().getGeometry().getLocation().getLng());
                                        intent.putExtra(Constants.Extras.DATA, bundle);
                                        setResult(Constants.Extras.PLACE_ACTIVITY_RESULT, intent);
                                        finish();
                                    }
                                });
                            }
                        }
                    }
                }
        }
    }

    @Override
    public void onHttpRequestFailure(String requestType, Request request, String errorMessage) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(PlacesAutoCompleteActivity.this, "Unable to Reach our server...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_cancel:
                et_search.setText("");
                list.clear();
                placesAdapter.notifyDataSetChanged();
                break;
            case R.id.ll_progress:
                break;
            case R.id.iv_back:
                onBackPressed();
        }
    }
}
