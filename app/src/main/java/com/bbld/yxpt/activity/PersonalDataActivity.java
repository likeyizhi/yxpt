package com.bbld.yxpt.activity;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bbld.yxpt.R;
import com.bbld.yxpt.base.BaseActivity;
import com.bbld.yxpt.bean.UserInfo;
import com.bbld.yxpt.loadingdialog.WeiboDialogUtils;
import com.bbld.yxpt.network.RetrofitService;
import com.bbld.yxpt.utils.MyToken;
import com.bumptech.glide.Glide;
import com.wuxiaolong.androidutils.library.ActivityManagerUtil;

import butterknife.BindView;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * 个人信息
 * Created by likey on 2017/7/1.
 */

public class PersonalDataActivity extends BaseActivity{
    @BindView(R.id.ivHead)
    ImageView ivHead;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvSex)
    TextView tvSex;
    @BindView(R.id.tvPhone)
    TextView tvPhone;
    @BindView(R.id.llChangePwd)
    LinearLayout llChangePwd;
    @BindView(R.id.btnOut)
    Button btnOut;
    @BindView(R.id.ivBack)
    ImageView ivBack;

    private UserInfo.UserInfoUserInfo userInfo;
    private Dialog loadDialog;

    @Override
    protected void initViewsAndEvents() {
        loadData();
        setListeners();
    }

    private void setListeners() {
        btnOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MyToken(PersonalDataActivity.this).delToken();
                SharedPreferences sharedAP=getSharedPreferences("YXAP",MODE_PRIVATE);
                SharedPreferences.Editor editorAP = sharedAP.edit();
                editorAP.putString("YXACC","");
                editorAP.putString("YXPWD","");
                editorAP.commit();
                ActivityManagerUtil.getInstance().finishActivity(PersonalDataActivity.this);
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityManagerUtil.getInstance().finishActivity(PersonalDataActivity.this);
            }
        });
        llChangePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putString("bandPhone", userInfo.getMobile());
                readyGo(UpdatePasswordActivity.class,bundle);
            }
        });
    }

    private void loadData() {
        loadDialog= WeiboDialogUtils.createLoadingDialog(PersonalDataActivity.this, "加载中...");
        Call<UserInfo> call= RetrofitService.getInstance().getUserInfo(new MyToken(PersonalDataActivity.this).getToken());
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

    private void setData() {
        Glide.with(getApplicationContext()).load(userInfo.getHeadPortrait()).error(R.mipmap.head).into(ivHead);
        tvName.setText(userInfo.getNickName()+"");
        tvPhone.setText(userInfo.getMobile()+"");
        tvSex.setText(userInfo.getSex()+"");
        WeiboDialogUtils.closeDialog(loadDialog);
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    public int getContentView() {
        return R.layout.activity_personaldata;
    }
}
