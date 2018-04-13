
package com.fingertips.activity;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.fingertips.R;
import com.fingertips.app.Constants;
import com.fingertips.fragment.OrderFragment;
import com.fingertips.fragment.ProfileFragment;
import com.fingertips.helper.TextViewPlus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends AppCompatActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tv_title)
    TextViewPlus tvText;

    @BindView(R.id.ll_progress)
    LinearLayout llProgress;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        setFragment(getIntent().getIntExtra(Constants.WHICH, 1));
    }

    @OnClick({R.id.iv_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }

    public void showLoader(){
        llProgress.setVisibility(View.VISIBLE);
    }

    public void hideLoader(){
        llProgress.setVisibility(View.GONE);
    }


    private void setFragment(int intExtra) {
        Bundle bundle = new Bundle();
        FragmentTransaction localFragmentTransaction = getSupportFragmentManager().beginTransaction();
        switch (intExtra) {
            case Constants.Navigation.PROFILE:
                ProfileFragment profileFragment = new ProfileFragment();
                tvText.setText("PROFILE");
                localFragmentTransaction.replace(R.id.container, profileFragment, "profile");
                localFragmentTransaction.commit();
               break;
            case Constants.Navigation.ORDER:
                OrderFragment orderFragment = new OrderFragment();
                tvText.setText("ORDERS");
                localFragmentTransaction.replace(R.id.container, orderFragment, "orders");
                localFragmentTransaction.commit();
                break;

        }
    }
}
