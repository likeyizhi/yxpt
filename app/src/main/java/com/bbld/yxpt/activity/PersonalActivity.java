package com.bbld.yxpt.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bbld.yxpt.R;
import com.bbld.yxpt.base.BaseActivity;
import com.bbld.yxpt.bean.Feedback;
import com.bbld.yxpt.bean.Login;
import com.bbld.yxpt.bean.UserInfo;
import com.bbld.yxpt.network.RetrofitService;
import com.bbld.yxpt.utils.MyToken;
import com.bumptech.glide.Glide;
import com.wuxiaolong.androidutils.library.ActivityManagerUtil;

import butterknife.BindView;
import cn.jpush.android.api.JPushInterface;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * 个人中心
 * Created by likey on 2017/6/28.
 */

public class PersonalActivity extends BaseActivity{
    @BindView(R.id.ivHead)
    ImageView ivHead;
    @BindView(R.id.tvMobile)
    TextView tvMobile;
    @BindView(R.id.tv01)
    TextView tv01;
    @BindView(R.id.tv02)
    TextView tv02;
    @BindView(R.id.tv03)
    TextView tv03;
    @BindView(R.id.tv04)
    TextView tv04;
    @BindView(R.id.tvUseCount)
    TextView tvUseCount;
    @BindView(R.id.ivClose)
    ImageView ivClose;
    @BindView(R.id.llPayMoney)
    LinearLayout llPayMoney;
    @BindView(R.id.llHaveToMoney)
    LinearLayout llHaveToMoney;
    @BindView(R.id.llReceive)
    LinearLayout llReceive;
    @BindView(R.id.llMessage)
    LinearLayout llMessage;
    @BindView(R.id.llHelp)
    LinearLayout llHelp;
    @BindView(R.id.tvFeedback)
    TextView tvFeedback;

    private String sacc;
    private String spwd;
    private static final String TOKEN=null;
    private SharedPreferences.Editor editorAP;
    private UserInfo.UserInfoUserInfo userInfo;

    @Override
    protected void initViewsAndEvents() {
        //读取帐号密码
        SharedPreferences sharedGetAP=getSharedPreferences("YXAP",MODE_PRIVATE);
        sacc = sharedGetAP.getString("YXACC", "");
        spwd = sharedGetAP.getString("YXPWD", "");

        if (sacc.equals("")||spwd.equals("")){
        }else{
            loadToken();
        }
        loadData();
        setListeners();
    }

    private void loadData() {
        if (new MyToken(PersonalActivity.this).getToken()==null || new MyToken(PersonalActivity.this).getToken().equals("")){
            showToast("暂未登录");
            Glide.with(getApplicationContext()).load(R.mipmap.head).into(ivHead);
            tvMobile.setText("-----------");
            tv01.setText("￥0.00");
            tv02.setText("￥0.00");
            tv03.setText("￥0.00");
            tv04.setText("0单");
            tvUseCount.setText("");
        }else{
            Call<UserInfo> call=RetrofitService.getInstance().getUserInfo(new MyToken(PersonalActivity.this).getToken());
            call.enqueue(new Callback<UserInfo>() {
                @Override
                public void onResponse(Response<UserInfo> response, Retrofit retrofit) {
                    if (response==null){
                        showToast(responseFail());
                        return;
                    }
                    if (response.body().getStatus()==0){
                        userInfo=response.body().getUserInfo();
                        setData();
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

    private void setData() {
        Glide.with(getApplicationContext()).load(userInfo.getHeadPortrait()).error(R.mipmap.head).into(ivHead);
        tvMobile.setText(userInfo.getMobile()+"");
        tv01.setText("￥"+userInfo.getTotialSale());
        tv02.setText("￥"+userInfo.getReturnTotialSale());
        tv03.setText("￥"+userInfo.getRewardTotial());
        tv04.setText(userInfo.getRewardOrderCount()+"单");
        tvUseCount.setText("已有"+userInfo.getPlatformUserCount()+"人使用言闻享购物平台");
    }

    private void loadToken() {
        String rid = JPushInterface.getRegistrationID(getApplicationContext());
        Call<Login> call= RetrofitService.getInstance().login(sacc, spwd,"android", rid);
        call.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Response<Login> response, Retrofit retrofit) {
                //保存Token
                SharedPreferences shared=getSharedPreferences("YXToken",MODE_PRIVATE);
                SharedPreferences.Editor editor=shared.edit();
                editor.putString(TOKEN,response.body().getToken());
                editor.commit();
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    private void setListeners() {
        ivHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (new MyToken(PersonalActivity.this).getToken()==null){
                    readyGo(LoginActivity.class);
                }else{
                    readyGo(PersonalDataActivity.class);
                }
            }
        });
        tvMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (new MyToken(PersonalActivity.this).getToken()==null){
                    readyGo(LoginActivity.class);
                }else{
                    readyGo(PersonalDataActivity.class);
                }
            }
        });
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityManagerUtil.getInstance().finishActivity(PersonalActivity.this);
            }
        });
        llPayMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (new MyToken(PersonalActivity.this).getToken()==null || new MyToken(PersonalActivity.this).getToken().equals("")){
                    showToast("暂未登录");
                }else{
                    readyGo(PayMoneyActivity.class);
                }
            }
        });
        llHaveToMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (new MyToken(PersonalActivity.this).getToken()==null || new MyToken(PersonalActivity.this).getToken().equals("")){
                    showToast("暂未登录");
                }else{
                    readyGo(HaveToMoneyActivity.class);
                }
            }
        });
        llReceive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (new MyToken(PersonalActivity.this).getToken()==null || new MyToken(PersonalActivity.this).getToken().equals("")){
                    showToast("暂未登录");
                }else{
                    readyGo(ReceiveMoneyActivity.class);
                }
            }
        });
        llMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (new MyToken(PersonalActivity.this).getToken()==null || new MyToken(PersonalActivity.this).getToken().equals("")){
                    showToast("暂未登录");
                }else{
                    readyGo(MessageCenterActivity.class);
                }
            }
        });
        llHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (new MyToken(PersonalActivity.this).getToken()==null || new MyToken(PersonalActivity.this).getToken().equals("")){
                    showToast("暂未登录");
                }else{
                    readyGo(GuideActivity.class);
                }
            }
        });
        tvFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (new MyToken(PersonalActivity.this).getToken()==null || new MyToken(PersonalActivity.this).getToken().equals("")){
                    showToast("暂未登录");
                }else{
                    readyGo(FeedbackActivity.class);
                }
            }
        });
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //读取帐号密码
        SharedPreferences sharedGetAP=getSharedPreferences("YXAP",MODE_PRIVATE);
        sacc = sharedGetAP.getString("YXACC", "");
        spwd = sharedGetAP.getString("YXPWD", "");

        if (sacc.equals("")||spwd.equals("")){
        }else{
            loadToken();
        }
        loadData();
        setListeners();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_personal;
    }
}
