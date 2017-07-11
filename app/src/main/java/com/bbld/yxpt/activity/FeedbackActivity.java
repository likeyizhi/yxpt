package com.bbld.yxpt.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bbld.yxpt.R;
import com.bbld.yxpt.base.BaseActivity;
import com.bbld.yxpt.bean.Feedback;
import com.bbld.yxpt.network.RetrofitService;
import com.bbld.yxpt.utils.MyToken;
import com.wuxiaolong.androidutils.library.ActivityManagerUtil;

import butterknife.BindView;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * 意见反馈
 * Created by likey on 2017/7/6.
 */

public class FeedbackActivity extends BaseActivity{
    @BindView(R.id.et_Describe)
    EditText etDescribe;
    @BindView(R.id.etTel)
    EditText etTel;
    @BindView(R.id.btn_go)
    Button btn_go;

    private String token;
    private String tel;

    @Override
    protected void initViewsAndEvents() {
        token=new MyToken(this).getToken();
        setListeners();
    }

    private void setListeners() {
        btn_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etDescribe.getText().toString().trim().equals("")){
                    showToast("请输入内容");
                }else if (etTel.getText().toString().trim().equals("")){
                    showToast("请输入手机号");
                }else if (etTel.getText().toString().trim().length()!=11){
                    showToast("请输入正确手机号");
                }else{
                    submit();
                }
            }
        });
    }

    private void submit() {
        Call<Feedback> call= RetrofitService.getInstance().feedback(token, etDescribe.getText()+"",etTel.getText()+"");
        call.enqueue(new Callback<Feedback>() {
            @Override
            public void onResponse(Response<Feedback> response, Retrofit retrofit) {
                if (response==null){
                    showToast(responseFail());
                    return;
                }
                if (response.body().getStatus()==0){
                    showToast("反馈成功");
                    ActivityManagerUtil.getInstance().finishActivity(FeedbackActivity.class);
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
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    public int getContentView() {
        return R.layout.activity_feedback;
    }
}
