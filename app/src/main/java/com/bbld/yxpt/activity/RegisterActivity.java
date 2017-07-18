package com.bbld.yxpt.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bbld.yxpt.R;
import com.bbld.yxpt.base.BaseActivity;
import com.bbld.yxpt.bean.Register;
import com.bbld.yxpt.bean.RegisterMessage;
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
 * 注册
 * Created by likey on 2017/7/1.
 */

public class RegisterActivity extends BaseActivity{
    @BindView(R.id.etPhone)
    EditText etPhone;
    @BindView(R.id.etCode)
    EditText etCode;
    @BindView(R.id.etPwd)
    EditText etPwd;
    @BindView(R.id.tvSend)
    TextView tvSend;
    @BindView(R.id.btnCreate)
    Button btnCreate;
    @BindView(R.id.tvHave)
    TextView tvHave;

    private String phone;
    private String code;
    private String password;
    private int indentiy=0;
    private Timer          timer           = null;
    private int            time            = 60;
    private TimerTask timerTask;
    private boolean isOnTimer;

    @Override
    protected void initViewsAndEvents() {
        setListeners();
    }

    private void setListeners() {
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phone=etPhone.getText().toString().trim();
                code=etCode.getText().toString().trim();
                password=etPwd.getText().toString().trim();
                if (indentiy==0){
                    showToast("请获取验证码");
                }else if (code.equals("")||code==null){
                    showToast("请输入验证码");
                }else if (phone.equals("")||phone==null){
                    showToast("请输入手机号");
                }else if (phone.length()!=11){
                    showToast("请输入正确手机号");
                }else{
                    Call<Register> call= RetrofitService.getInstance().register(phone,indentiy,code,password);
                    call.enqueue(new Callback<Register>() {
                        @Override
                        public void onResponse(Response<Register> response, Retrofit retrofit) {
                            if (response==null){
                                showToast(responseFail());
                                return;
                            }
                            if (response.body().getStatus()==0){
                                showToast(response.body().getMes());
                                if (isOnTimer){
                                    timerTask.cancel();
                                }
                                ActivityManagerUtil.getInstance().finishActivity(RegisterActivity.class);
                            }else{
                                showToast(response.body().getMes());
                            }
                        }

                        @Override
                        public void onFailure(Throwable throwable) {

                        }
                    });
                }

            }
        });
        tvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phone=etPhone.getText().toString().trim();
                if (phone.equals("")||phone==null){
                    showToast("请输入手机号");
                }else if (phone.length()!=11){
                    showToast("请输入正确手机号");
                }else{
                    Call<RegisterMessage> call=RetrofitService.getInstance().sendRegisterMessage(phone);
                    call.enqueue(new Callback<RegisterMessage>() {
                        @Override
                        public void onResponse(Response<RegisterMessage> response, Retrofit retrofit) {
                            if (response==null){
                                showToast(getResources().getString(R.string.response_fail));
                                return;
                            }
                            if (response.body().getStatus()==0){
                                showToast(response.body().getMes());
                                indentiy=response.body().getIdentity();
                                timer = new Timer();
                                isOnTimer=true;
                                timerTask = new TimerTask() {
                                    @Override
                                    public void run() {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                time--;
                                                tvSend.setClickable(false);
                                                tvSend.setText(time + "s");
                                                if (time < 0) {
                                                    time = 60;
                                                    timer.cancel();
                                                    isOnTimer=false;
                                                    tvSend.setClickable(true);
                                                    tvSend.setText("发送");
                                                }
                                            }
                                        });
                                    }
                                };
                                timer.schedule(timerTask, 1000, 1000);
                            }else{
                                showToast(response.body().getMes());
                            }
                        }

                        @Override
                        public void onFailure(Throwable throwable) {

                        }
                    });
                }
            }
        });
        tvHave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOnTimer){
                    timer.cancel();
                }
                ActivityManagerUtil.getInstance().finishActivity(RegisterActivity.this);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
            if (isOnTimer){
                timer.cancel();
            }
            ActivityManagerUtil.getInstance().finishActivity(this);
        }
        return false;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    public int getContentView() {
        return R.layout.activity_register;
    }
}
