package com.bbld.yxpt.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bbld.yxpt.R;
import com.bbld.yxpt.base.BaseActivity;
import com.bbld.yxpt.bean.Retrieve;
import com.bbld.yxpt.bean.RetrieveMessage;
import com.bbld.yxpt.network.RetrofitService;
import com.wuxiaolong.androidutils.library.ActivityManagerUtil;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * 找回密码、修改密码
 * Created by likey on 2017/7/4.
 */

public class UpdatePasswordActivity extends BaseActivity{
    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.ll_1)
    LinearLayout ll_1;
    @BindView(R.id.ll_2)
    RelativeLayout ll_2;
    @BindView(R.id.ll_3)
    LinearLayout ll_3;
    @BindView(R.id.etPhone)
    EditText etPhone;
    @BindView(R.id.etCode)
    EditText etCode;
    @BindView(R.id.tvSendCode)
    TextView tvSendCode;
    @BindView(R.id.etNewPwd1)
    EditText etNewPwd1;
    @BindView(R.id.etNewPwd2)
    EditText etNewPwd2;
    @BindView(R.id.btnOK)
    Button btnOK;
    @BindView(R.id.iv123)
    ImageView iv123;

    private String isForget;
    private String sacc;
    private String spwd;
    private int identity;
    private String newPwd1;
    private String newPwd2;
    private Timer          timer           = null;
    private int            time            = 60;
    private TimerTask timerTask;
    private boolean isOnTimer;
    private String bandPhone;

    @Override
    protected void initViewsAndEvents() {
        //读取帐号密码
        SharedPreferences sharedGetAP=getSharedPreferences("YXAP",MODE_PRIVATE);
        sacc = sharedGetAP.getString("YXACC", "");
        spwd = sharedGetAP.getString("YXPWD", "");

        if (sacc.equals("")||spwd.equals("")){
        }else{
            etPhone.setText(bandPhone);
            etPhone.setFocusable(false);
        }
        setListeners();
    }

    private void setListeners() {
        tvSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendCode();
            }
        });
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ll_1.getVisibility()==View.VISIBLE){
                    if (etPhone.getText().toString().trim().equals("")){
                        showToast("请输入手机号");
                    }else{
                        if (etPhone.getText().toString().trim().length()==11){
                            ll_1.setVisibility(View.GONE);
                            ll_2.setVisibility(View.VISIBLE);
                            ll_3.setVisibility(View.GONE);
                            iv123.setImageResource(R.mipmap.pwd02);
                        }else{
                            showToast("请输入正确手机号");
                        }
                    }
                }else if (ll_2.getVisibility()==View.VISIBLE){
                    if (etCode.getText().toString().trim().equals("")){
                        showToast("请输入验证码");
                    }else{
                        ll_1.setVisibility(View.GONE);
                        ll_2.setVisibility(View.GONE);
                        ll_3.setVisibility(View.VISIBLE);
                        iv123.setImageResource(R.mipmap.pwd03);
                        btnOK.setText("完成");
                    }
                }else {
                    newPwd1 = etNewPwd1.getText().toString().trim();
                    newPwd2 = etNewPwd2.getText().toString().trim();
                    if (newPwd1.equals("") || newPwd2.equals("")){
                        showToast("请输入密码");
                    }else{
                        if (newPwd1.length()<6 || newPwd2.length()<6){
                            showToast("请确认密码长度大于6位");
                        }else{
                            if (newPwd1.equals(newPwd2)){
                                UpdatePwd();
                            }else{
                                showToast("两次输入不相同");
                            }
                        }
                    }
                }
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityManagerUtil.getInstance().finishActivity(UpdatePasswordActivity.this);
            }
        });
    }

    private void sendCode() {
        Call<RetrieveMessage> call= RetrofitService.getInstance().sendRetrieveMessage(etPhone.getText().toString().trim());
        call.enqueue(new Callback<RetrieveMessage>() {
            @Override
            public void onResponse(Response<RetrieveMessage> response, Retrofit retrofit) {
                if (response==null){
                    showToast(responseFail());
                    return;
                }
                if (response.body().getStatus()==0){
                    identity = response.body().getIdentity();
                    timer = new Timer();
                    isOnTimer=true;
                    timerTask = new TimerTask() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    time--;
                                    tvSendCode.setClickable(false);
                                    tvSendCode.setText(time + "s");
                                    if (time < 0) {
                                        time = 60;
                                        timer.cancel();
                                        isOnTimer=false;
                                        tvSendCode.setClickable(true);
                                        tvSendCode.setText("发送");
                                    }
                                }
                            });
                        }
                    };
                    timer.schedule(timerTask, 1000, 1000);
                    showToast("发送成功");
                } else {
                    showToast(response.body().getMes());
                }
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    private void UpdatePwd() {
        Call<Retrieve> call=RetrofitService.getInstance().retrieve(etPhone.getText().toString().trim(), identity,etCode.getText().toString().trim(),newPwd1);
        call.enqueue(new Callback<Retrieve>() {
            @Override
            public void onResponse(Response<Retrieve> response, Retrofit retrofit) {
                if (response==null){
                    showToast(responseFail());
                    return;
                }
                if (response.body().getStatus()==0){
                    showToast("修改成功");
                    if (isOnTimer){
                        timer.cancel();
                    }
                    ActivityManagerUtil.getInstance().finishActivity(UpdatePasswordActivity.this);
                }else{
                    showToast(response.body().getMes());
                }
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
            if (ll_1.getVisibility()==View.VISIBLE){
                if (isOnTimer){
                    timer.cancel();
                }
                ActivityManagerUtil.getInstance().finishActivity(UpdatePasswordActivity.this);
            }else if (ll_2.getVisibility()==View.VISIBLE){
                ll_2.setVisibility(View.GONE);
                ll_1.setVisibility(View.VISIBLE);
                iv123.setImageResource(R.mipmap.pwd01);
                btnOK.setText("下一步");
            }else{
                ll_2.setVisibility(View.VISIBLE);
                ll_3.setVisibility(View.GONE);
                iv123.setImageResource(R.mipmap.pwd02);
                btnOK.setText("下一步");
            }
        }
        return false;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {
        bandPhone=extras.getString("bandPhone");
    }

    @Override
    public int getContentView() {
        return R.layout.activity_updatepassword;
    }
}
