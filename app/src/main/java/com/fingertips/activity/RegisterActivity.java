package com.fingertips.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.fingertips.R;
import com.fingertips.app.Constants;
import com.fingertips.helper.XClass;
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
import butterknife.OnClick;

/**
 * Created by deepanshurustagi on 3/22/18.
 */

public class RegisterActivity extends AppCompatActivity implements IOkHttpNotify {

    @BindView(R.id.et_mail)
    EditText etMail;

    @BindView(R.id.et_password)
    EditText etPassword;

    @BindView(R.id.et_name)
    EditText etName;

    @BindView(R.id.et_number)
    EditText etNumber;

    @BindView(R.id.btn_submit)
    Button btnLogin;

    @BindView(R.id.ll_bottom)
    LinearLayout llBottom;
    @BindView(R.id.ll_progress)
    LinearLayout llProgress;

    private OKHttpAPICalls okHttpAPICalls;
    private XClass obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        initialize();


    }

    private void initialize() {
        obj = new XClass(RegisterActivity.this);
        okHttpAPICalls = new OKHttpAPICalls();
        okHttpAPICalls.setOnOkHttpNotifyListener(this);
    }

    public void showLoader(){
        llProgress.setVisibility(View.VISIBLE);
    }

    public void hideLoader(){
        llProgress.setVisibility(View.GONE);
    }



    @OnClick({R.id.btn_submit,R.id.ll_bottom})
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btn_submit:
                String email =  etMail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String name = etName.getText().toString().trim();
                String number = etNumber.getText().toString().trim();
                if (name.length()>0) {
                    if(Constants.isValidMobileNo(number)) {
                        if (email.length() > 0) {
                            if (password.length() >= 6) {
                                Bundle bundle = new Bundle();
                                bundle.putString(Constants.Extras.email,email);
                                bundle.putString(Constants.Extras.password,password);
                                bundle.putString(Constants.Extras.name,name);
                                bundle.putString(Constants.Extras.number,number);

                                showLoader();
                                okHttpAPICalls.run(Constants.RequestTag.SIGNUP,bundle);

                            } else {
                                etPassword.setError("Please enter password");
                            }
                        } else {
                             etMail.setError("Please enter mail id ");
                        }
                    }else{
                        etNumber.setError("Please enter mobile number");
                    }
                }else{
                    etName.setError("Please enter name");
                }

                break;
            case R.id.ll_bottom:
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                break;
        }
    }

    @Override
    public void onHttpRequestSuccess(String requestType, Response response) throws IOException {
        String jsonResponse = response.body().string();
        switch(requestType){
            case Constants.RequestTag.SIGNUP:
                try {
                    final JSONObject object = new JSONObject(jsonResponse);
                    JSONObject status = object.getJSONObject("status");
                    final boolean isSuccess = status.getBoolean("isSuccess");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(!isSuccess) {
                                try {
                                    hideLoader();
                                    ToastUtils.showShortToast(RegisterActivity.this,object.getString("error"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }else {
                                /*"employee": {
                                    "email": "deepanshurustagi@gmail.com",
                                            "password": "test123"
                                },
                                "token": "ae79e075b0f73e036d34666fa39c3367525e8b6a"*/
                                try {
                                    hideLoader();
                                    obj.saveInSharedPref(R.string.auth_token,"string",object.getString("token"));
                                    ToastUtils.showShortToast(RegisterActivity.this,"Success");
                                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onHttpRequestFailure(String requestType, Request request, String errorMessage) {

    }
}
