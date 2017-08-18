package com.bbld.yxpt.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bbld.yxpt.R;
import com.bbld.yxpt.baofoo.OrderActivity;
import com.bbld.yxpt.base.BaseActivity;
import com.bbld.yxpt.bean.AddPayOrder;
import com.bbld.yxpt.bean.ScanShop;
import com.bbld.yxpt.loadingdialog.WeiboDialogUtils;
import com.bbld.yxpt.network.RetrofitService;
import com.bbld.yxpt.utils.MyToken;
import com.bumptech.glide.Glide;
import com.wuxiaolong.androidutils.library.ActivityManagerUtil;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * 扫码获取店铺信息
 * Created by likey on 2017/7/3.
 */

public class PaymentActivity extends BaseActivity{
    @BindView(R.id.ivShopImg)
    ImageView ivShopImg;
    @BindView(R.id.tvShopName)
    TextView tvShopName;
    @BindView(R.id.tvHot)
    TextView tvHot;
    @BindView(R.id.btnSubmit)
    Button btnSubmit;
    @BindView(R.id.tvAllMoney)
    TextView tvAllMoney;
    @BindView(R.id.etMoney)
    EditText etMoney;
    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.tvGetMoneyList)
    TextView tvGetMoneyList;
    @BindView(R.id.tvDesc)
    TextView tvDesc;

    private String codedContent;
    Call<ScanShop> call;
    private String token;
    private ScanShop.ScanShopShopInfo shopInfo;
    private Dialog loading;

    @Override
    protected void initViewsAndEvents() {
        token=new MyToken(this).getToken();
        loadData();
        setListeners();
    }

    private void setListeners() {
        try {
            etMoney.addTextChangedListener(watcher);
        }catch (Exception e){
            showToast(someException());
        }
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    ActivityManagerUtil.getInstance().finishActivity(PaymentActivity.this);
                }catch (Exception e){
                    showToast(someException());
                }
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (etMoney.getText().toString().trim().equals("")){
                        showToast("请输入金额");
                    }else{
                        Call<AddPayOrder> payOrderCall=RetrofitService.getInstance().addPayOrder(token,codedContent,etMoney.getText()+"");
                        payOrderCall.enqueue(new Callback<AddPayOrder>() {
                            @Override
                            public void onResponse(Response<AddPayOrder> response, Retrofit retrofit) {
                                if (response==null){
                                    showToast(responseFail());
                                    return;
                                }
                                if (response.body().getStatus()==0){
                                    try {
                                        Bundle bundle=new Bundle();
                                        bundle.putString("ShopImg",shopInfo.getShopImg()+"");
                                        bundle.putString("ShopName",shopInfo.getShopName());
                                        bundle.putString("money", etMoney.getText()+"");
                                        bundle.putString("Hot",shopInfo.getActivityTitle()+"");
                                        bundle.putString("orderNo" , response.body().getOrderNo()+"");
                                        ActivityManagerUtil.getInstance().finishActivity(PaymentActivity.this);
                                        readyGo(NewOrderActivity.class, bundle);
                                    }catch (Exception e){
                                        showToast(someException());
                                    }
                                }else{
                                    showToast(response.body().getMes());
                                }
                            }

                            @Override
                            public void onFailure(Throwable throwable) {

                            }
                        });
                    }
                }catch (Exception e){
                    showToast(someException());
                }
            }
        });
        tvGetMoneyList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Bundle bundle=new Bundle();
                    bundle.putString("ActivityCode",codedContent);
                    readyGo(ActivityGetMonetActivity.class,bundle);
                    overridePendingTransition(R.anim.zoomin,0);
                }catch (Exception e){
                    showToast(someException());
                }
            }
        });
        tvDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Bundle bundle=new Bundle();
                    bundle.putString("desc",shopInfo.getActivityDesc()+"");
                    readyGo(ActivityDescActivity.class,bundle);
                    overridePendingTransition(R.anim.zoomin,0);
                }catch (Exception e){
                    showToast(someException());
                }
            }
        });
    }

    private void loadData() {
        try {
            loading=WeiboDialogUtils.createLoadingDialog(PaymentActivity.this,"加载中...");
            if (token==null || token.equals("")){
                call= RetrofitService.getInstance().scanShop("", codedContent);
                showToast("未登录");
            }else {
                call= RetrofitService.getInstance().scanShop(token, codedContent);
                call.enqueue(new Callback<ScanShop>() {
                    @Override
                    public void onResponse(Response<ScanShop> response, Retrofit retrofit) {
                        if (response==null){
                            showToast(responseFail());
                            WeiboDialogUtils.closeDialog(loading);
                            return;
                        }
                        if (response.body().getStatus()==0){
                            try {
                                shopInfo=response.body().getShopInfo();
                                setData();
                                WeiboDialogUtils.closeDialog(loading);
                            }catch (Exception e){
                                showToast(someException());
                            }
                        }else{
                            try {
                                showToast(response.body().getMes());
                                WeiboDialogUtils.closeDialog(loading);
                                ActivityManagerUtil.getInstance().finishActivity(PaymentActivity.this);
                            }catch (Exception e){
                                showToast(someException());
                            }
                        }
                    }
                    @Override
                    public void onFailure(Throwable throwable) {
                        WeiboDialogUtils.closeDialog(loading);
                    }
                });
            }
        }catch (Exception e){
            showToast(someException());
            WeiboDialogUtils.closeDialog(loading);
        }
    }

    private void setData() {
        try {
            Glide.with(getApplicationContext()).load(shopInfo.getShopImg()).into(ivShopImg);
            tvShopName.setText(shopInfo.getShopName());
            tvHot.setText(shopInfo.getActivityTitle());
            tvAllMoney.setText("￥"+shopInfo.getActivityTotal());
        }catch (Exception e){
            showToast(someException());
        }
    }
    private TextWatcher watcher = new TextWatcher(){
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            try {
                if (s.length()!=0){
                    btnSubmit.setBackgroundColor(Color.rgb(21,212,143));
                    btnSubmit.setClickable(true);
                }else{
                    btnSubmit.setBackgroundColor(Color.rgb(204,204,204));
                    btnSubmit.setClickable(false);
                }
            }catch (Exception e){
                showToast(someException());
            }
        }
    };

    @Override
    protected void getBundleExtras(Bundle extras) {
        codedContent=extras.getString("codedContent");
    }

    @Override
    public int getContentView() {
        return R.layout.activity_payment;
    }
}
