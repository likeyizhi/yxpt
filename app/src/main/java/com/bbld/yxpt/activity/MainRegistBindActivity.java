package com.bbld.yxpt.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bbld.yxpt.R;
import com.bbld.yxpt.bean.Register;
import com.bbld.yxpt.bean.RegisterLoginBind;
import com.bbld.yxpt.bean.RegisterMessage;
import com.bbld.yxpt.bean.SendLoginMessage;
import com.bbld.yxpt.loadingdialog.WeiboDialogUtils;
import com.bbld.yxpt.network.RetrofitService;

import java.util.Timer;
import java.util.TimerTask;

import cn.jpush.android.api.JPushInterface;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * 主页蒙层-注册或绑定
 * Created by likey on 2017/8/17.
 */

public class MainRegistBindActivity extends Activity{
    private EditText etAcc;
    private EditText etCode;
    private Button btnLogin;
    private Button  btnBack;
    private TextView tvSend;
    private Dialog mWeiboDialogSend;
    private Timer          timer           = null;
    private int            time            = 60;
    private TimerTask timerTask;
    private boolean isOnTimer;
    private int indentiy=0;
    private int jointype=0;
    private String joinid;
    private String nickname;
    private String sex;
    private String faceurl;
    private String phone;
    private String code;
    private String plat="android";
    private String pushid="";
    private String subjoinid="";
    private String password="";
    private Dialog mWeiboDialogCreate;
    private Call<RegisterLoginBind> registerCall;
    private static final String TOKEN=null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_regist_bind);
        Intent intent=getIntent();
        jointype = intent.getIntExtra("jointype",0);
        joinid=intent.getStringExtra("joinid");
        nickname=intent.getStringExtra("nickname");
        faceurl = intent.getStringExtra("faceurl");
        sex=intent.getStringExtra("sex");
        subjoinid=intent.getStringExtra("subjoinid");
        pushid = JPushInterface.getRegistrationID(getApplicationContext());
        initView();
        setListeners();
    }

    private void setListeners() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new  Intent(MainRegistBindActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        tvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvSend.setClickable(false);
                phone = etAcc.getText().toString().trim();
                if (phone.equals("") || phone == null) {
                    Toast.makeText(MainRegistBindActivity.this, "请输入手机号", Toast.LENGTH_SHORT).show();
                    tvSend.setClickable(true);
                } else if (phone.length() != 11) {
                    Toast.makeText(MainRegistBindActivity.this, "请输入正确手机号", Toast.LENGTH_SHORT).show();
                    tvSend.setClickable(true);
                } else {
                    mWeiboDialogSend = WeiboDialogUtils.createLoadingDialog(MainRegistBindActivity.this, "发送中...");
                    Call<SendLoginMessage> call = RetrofitService.getInstance().sendLoginMessage(phone);
                    call.enqueue(new Callback<SendLoginMessage>() {
                        @Override
                        public void onResponse(Response<SendLoginMessage> response, Retrofit retrofit) {
                            if (response == null) {
                                Toast.makeText(MainRegistBindActivity.this, getResources().getString(R.string.response_fail), Toast.LENGTH_SHORT).show();
                                WeiboDialogUtils.closeDialog(mWeiboDialogSend);
                                tvSend.setClickable(true);
                                return;
                            }
                            if (response.body().getStatus() == 0) {
                                Toast.makeText(MainRegistBindActivity.this, response.body().getMes(), Toast.LENGTH_SHORT).show();
                                indentiy = response.body().getIdentity();
                                timer = new Timer();
                                isOnTimer = true;
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
                                                    isOnTimer = false;
                                                    tvSend.setClickable(true);
                                                    tvSend.setText("发送");
                                                }
                                            }
                                        });
                                    }
                                };
                                timer.schedule(timerTask, 1000, 1000);
                                WeiboDialogUtils.closeDialog(mWeiboDialogSend);
                            } else {
                                tvSend.setClickable(true);
                                Toast.makeText(MainRegistBindActivity.this, response.body().getMes(), Toast.LENGTH_SHORT).show();
                                WeiboDialogUtils.closeDialog(mWeiboDialogSend);
                            }
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            tvSend.setClickable(true);
                            WeiboDialogUtils.closeDialog(mWeiboDialogSend);
                        }
                    });
                }
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phone = etAcc.getText().toString().trim();
                code = etCode.getText().toString().trim();
                if (indentiy == 0) {
                    Toast.makeText(MainRegistBindActivity.this, "请获取验证码", Toast.LENGTH_SHORT).show();
                } else if (code.equals("") || code == null) {
                    Toast.makeText(MainRegistBindActivity.this, "请获取验证码", Toast.LENGTH_SHORT).show();
                } else if (phone.equals("") || phone == null) {
                    Toast.makeText(MainRegistBindActivity.this, "请输入手机号", Toast.LENGTH_SHORT).show();
                } else if (phone.length() != 11) {
                    Toast.makeText(MainRegistBindActivity.this, "请输入正确手机号", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        mWeiboDialogCreate = WeiboDialogUtils.createLoadingDialog(MainRegistBindActivity.this, "注册中...");
                        registerCall = RetrofitService.getInstance().registerLoginBind(phone, indentiy, code, jointype, joinid, subjoinid, nickname, faceurl, sex, plat, pushid);
                        registerCall.enqueue(new Callback<RegisterLoginBind>() {
                            @Override
                            public void onResponse(Response<RegisterLoginBind> response, Retrofit retrofit) {
                                if (response == null) {
                                    Toast.makeText(MainRegistBindActivity.this, responseFail(), Toast.LENGTH_SHORT).show();
                                    WeiboDialogUtils.closeDialog(mWeiboDialogCreate);
                                    return;
                                }
                                if (response.body().getStatus() == 0) {
                                    //保存Token
                                    SharedPreferences shared=getSharedPreferences("YXToken",MODE_PRIVATE);
                                    SharedPreferences.Editor editor=shared.edit();
                                    editor.putString(TOKEN,response.body().getToken());
                                    editor.putString("HeadPortrait",response.body().getHeadPortrait());
                                    editor.commit();
                                    WeiboDialogUtils.closeDialog(mWeiboDialogCreate);
                                    if (isOnTimer){
                                        timerTask.cancel();
                                    }
                                    finish();
                                } else {
                                    Toast.makeText(MainRegistBindActivity.this, response.body().getMes(), Toast.LENGTH_SHORT).show();
                                    WeiboDialogUtils.closeDialog(mWeiboDialogCreate);
                                }
                            }

                            @Override
                            public void onFailure(Throwable throwable) {
                                WeiboDialogUtils.closeDialog(mWeiboDialogCreate);
                            }
                        });
                    } catch (Exception e) {
                        Toast.makeText(MainRegistBindActivity.this,someException(), Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }



    public String responseFail(){
        return getResources().getString(R.string.response_fail);
    }

    public String someException(){
        return getResources().getString(R.string.some_exception);
    }
    private void initView() {
        etAcc= (EditText) findViewById(R.id.etAcc);
        etCode= (EditText) findViewById(R.id.etCode);
        btnLogin= (Button) findViewById(R.id.btnLogin);
        btnBack= (Button) findViewById(R.id.btnBack);
        tvSend= (TextView) findViewById(R.id.tvSend);

    }
}
