package com.bbld.yxpt.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bbld.yxpt.R;
import com.bbld.yxpt.base.BaseActivity;
import com.bbld.yxpt.bean.Login;
import com.bbld.yxpt.bean.Register;
import com.bbld.yxpt.network.RetrofitService;
import com.wuxiaolong.androidutils.library.ActivityManagerUtil;

import butterknife.BindView;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * 登录
 * Created by likey on 2017/7/1.
 */

public class LoginActivity extends BaseActivity{
    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.etAcc)
    EditText etAcc;
    @BindView(R.id.etPwd)
    EditText etPwd;
    @BindView(R.id.tvForgetPwd)
    TextView tvForgetPwd;
    @BindView(R.id.btnLogin)
    Button btnLogin;
    @BindView(R.id.ivQQ)
    ImageView ivQQ;
    @BindView(R.id.ivWX)
    ImageView ivWX;
    @BindView(R.id.tvCreate)
    TextView tvCreate;

    private String acc;
    private String pwd;
    private static final String TOKEN=null;
    private SharedPreferences.Editor editorAP;

    @Override
    protected void initViewsAndEvents() {
        SharedPreferences sharedAP=getSharedPreferences("YXAP",MODE_PRIVATE);
        editorAP=sharedAP.edit();
        setListeners();
    }

    private void setListeners() {
        //登录
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acc=etAcc.getText().toString().trim();
                pwd=etPwd.getText().toString().trim();
                Call<Login> call= RetrofitService.getInstance().login(acc,pwd);
                call.enqueue(new Callback<Login>() {
                    @Override
                    public void onResponse(Response<Login> response, Retrofit retrofit) {
                        if (response==null){
                            showToast(responseFail());
                            return;
                        }
                        if (response.body().getStatus()==0){
                            //保存Token
                            SharedPreferences shared=getSharedPreferences("YXToken",MODE_PRIVATE);
                            SharedPreferences.Editor editor=shared.edit();
                            editor.putString(TOKEN,response.body().getToken());
                            editor.commit();
                            //保存帐号密码
                            editorAP.putString("YXACC",acc);
                            editorAP.putString("YXPWD",pwd);
                            editorAP.commit();

                            showToast("登录成功");
                            ActivityManagerUtil.getInstance().finishActivity(LoginActivity.this);
//                            finish();
                        }else{
                            showToast(response.body().getMes());
                        }
                    }

                    @Override
                    public void onFailure(Throwable throwable) {

                    }
                });
            }
        });
        //创建帐号
        tvCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readyGo(RegisterActivity.class);
            }
        });
        //忘记密码
        tvForgetPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putString("isForget","Forget");
                readyGo(UpdatePasswordActivity.class, bundle);
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityManagerUtil.getInstance().finishActivity(LoginActivity.this);
            }
        });
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
            finish();
        }
        return false;
    }
    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    public int getContentView() {
        return R.layout.activity_login;
    }
}
