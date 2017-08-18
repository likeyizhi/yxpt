package com.bbld.yxpt.activity;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bbld.yxpt.R;
import com.bbld.yxpt.base.BaseActivity;
import com.bbld.yxpt.bean.Login;
import com.bbld.yxpt.bean.Register;
import com.bbld.yxpt.bean.RegisterMessage;
import com.bbld.yxpt.loadingdialog.WeiboDialogUtils;
import com.bbld.yxpt.network.RetrofitService;
import com.wuxiaolong.androidutils.library.ActivityManagerUtil;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import cn.jpush.android.api.JPushInterface;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * 注册
 * Created by likey on 2017/7/
 */

public class RegisterActivity extends BaseActivity{
    @BindView(R.id.ivBack)
    ImageView ivBack;
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
    private static final String TOKEN=null;
    private Call<Register> registerCall;
    private int jointype;
    private String joinid;
    private String nickname;
    private String faceurl;
    private String sex;
    private Dialog mWeiboDialogSend;
    private Dialog mWeiboDialogCreate;
    private Dialog mWeiboDialogLogin;

    @Override
    protected void initViewsAndEvents() {
        etCode.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        tvSend.setClickable(true);
        setListeners();
    }

    private void setListeners() {
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phone=etPhone.getText().toString().trim();
                code=etCode.getText().toString().trim();
                password="111111";
                if (indentiy==0){
                    showToast("请获取验证码");
                }else if (code.equals("")||code==null){
                    showToast("请输入验证码");
                }else if (phone.equals("")||phone==null){
                    showToast("请输入手机号");
                }else if (phone.length()!=11){
                    showToast("请输入正确手机号");
                }else{
                    try {
                        mWeiboDialogCreate=WeiboDialogUtils.createLoadingDialog(RegisterActivity.this,"注册中...");
                        if (jointype!=0){
                            registerCall= RetrofitService.getInstance().otherRegister(phone,indentiy,code,password,jointype,joinid,nickname,faceurl,sex);
                        }else{
                            registerCall= RetrofitService.getInstance().register(phone,indentiy,code,password);
                        }
                        registerCall.enqueue(new Callback<Register>() {
                            @Override
                            public void onResponse(Response<Register> response, Retrofit retrofit) {
                                if (response==null){
                                    showToast(responseFail());
                                    WeiboDialogUtils.closeDialog(mWeiboDialogCreate);
                                    return;
                                }
                                if (response.body().getStatus()==0){
                                    WeiboDialogUtils.closeDialog(mWeiboDialogCreate);
                                    toLogin();
//                                showToast(response.body().getMes());
//                                if (isOnTimer){
//                                    timerTask.cancel();
//                                }
//                                finish();
                                }else{
                                    showToast(response.body().getMes());
                                    WeiboDialogUtils.closeDialog(mWeiboDialogCreate);
                                }
                            }

                            @Override
                            public void onFailure(Throwable throwable) {
                                WeiboDialogUtils.closeDialog(mWeiboDialogCreate);
                            }
                        });
                    }catch (Exception e){
                        showToast(someException());
                    }
                }

            }
        });
        tvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvSend.setClickable(false);
                phone=etPhone.getText().toString().trim();
                if (phone.equals("")||phone==null){
                    showToast("请输入手机号");
                    tvSend.setClickable(true);
                }else if (phone.length()!=11){
                    showToast("请输入正确手机号");
                    tvSend.setClickable(true);
                }else{
                    mWeiboDialogSend=WeiboDialogUtils.createLoadingDialog(RegisterActivity.this,"发送中...");
                    Call<RegisterMessage> call=RetrofitService.getInstance().sendRegisterMessage(phone);
                    call.enqueue(new Callback<RegisterMessage>() {
                        @Override
                        public void onResponse(Response<RegisterMessage> response, Retrofit retrofit) {
                            if (response==null){
                                showToast(getResources().getString(R.string.response_fail));
                                WeiboDialogUtils.closeDialog(mWeiboDialogSend);
                                tvSend.setClickable(true);
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
                                WeiboDialogUtils.closeDialog(mWeiboDialogSend);
                            }else{
                                tvSend.setClickable(true);
                                showToast(response.body().getMes());
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
        tvHave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (isOnTimer){
                        timer.cancel();
                    }
                    tvSend.setClickable(true);
                    ActivityManagerUtil.getInstance().finishActivity(RegisterActivity.this);
                }catch (Exception e){
                    showToast(someException());
                }
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (isOnTimer){
                        timer.cancel();
                    }
                    tvSend.setClickable(true);
                    ActivityManagerUtil.getInstance().finishActivity(RegisterActivity.this);
                }catch (Exception e){
                    showToast(someException());
                }
            }
        });
    }

    private void toLogin() {
        try {
            mWeiboDialogLogin=WeiboDialogUtils.createLoadingDialog(RegisterActivity.this,"登录中...");
            String rid = JPushInterface.getRegistrationID(getApplicationContext());
            Call<Login> call= RetrofitService.getInstance().login(phone,password, "android", rid);
            call.enqueue(new Callback<Login>() {
                @Override
                public void onResponse(Response<Login> response, Retrofit retrofit) {
                    if (response==null){
                        showToast(responseFail());
                        WeiboDialogUtils.closeDialog(mWeiboDialogLogin);
                        return;
                    }
                    if (response.body().getStatus()==0){
                        try {
                            //保存Token
                            SharedPreferences shared=getSharedPreferences("YXToken",MODE_PRIVATE);
                            SharedPreferences.Editor editor=shared.edit();
                            editor.putString(TOKEN,response.body().getToken());
                            editor.putString("HeadPortrait",response.body().getHeadPortrait());
                            editor.commit();
                            showToast("登录成功");
                            WeiboDialogUtils.closeDialog(mWeiboDialogLogin);
                            LoginActivity.loginActivity.finish();
                            if (isOnTimer){
                                timer.cancel();
                            }
                            finish();
                        }catch (Exception e){
                            showToast(someException());
                            WeiboDialogUtils.closeDialog(mWeiboDialogLogin);
                        }
                    }else{
                        showToast(response.body().getMes());
                        WeiboDialogUtils.closeDialog(mWeiboDialogLogin);
                    }
                }

                @Override
                public void onFailure(Throwable throwable) {
                    WeiboDialogUtils.closeDialog(mWeiboDialogLogin);
                }
            });
        }catch (Exception e){
            showToast(someException());
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
            if (isOnTimer){
                timer.cancel();
            }
            tvSend.setClickable(true);
            finish();
        }
        return false;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {
        jointype=extras.getInt("jointype", 0);
        joinid=extras.getString("joinid");
        nickname=extras.getString("nickname");
        faceurl=extras.getString("faceurl");
        sex=extras.getString("sex");
//        showToast(jointype+","+joinid+","+nickname+","+faceurl+","+sex);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_register;
    }
}
