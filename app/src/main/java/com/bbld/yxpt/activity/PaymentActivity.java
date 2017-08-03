package com.bbld.yxpt.activity;

import android.app.AlertDialog;
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

    @Override
    protected void initViewsAndEvents() {
        token=new MyToken(this).getToken();
        loadData();
        setListeners();
    }

    private void setListeners() {
        etMoney.addTextChangedListener(watcher);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityManagerUtil.getInstance().finishActivity(PaymentActivity.this);
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                                Bundle bundle=new Bundle();
                                bundle.putString("ShopImg",shopInfo.getShopImg()+"");
                                bundle.putString("ShopName",shopInfo.getShopName());
                                bundle.putString("money", etMoney.getText()+"");
                                bundle.putString("Hot",shopInfo.getActivityTitle()+"");
                                bundle.putString("orderNo" , response.body().getOrderNo()+"");
                                ActivityManagerUtil.getInstance().finishActivity(PaymentActivity.this);
                                readyGo(NewOrderActivity.class, bundle);
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
        tvGetMoneyList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putString("ActivityCode",codedContent);
                readyGo(ActivityGetMonetActivity.class,bundle);
                overridePendingTransition(R.anim.zoomin,0);
            }
        });
        tvDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putString("desc",shopInfo.getActivityDesc()+"");
                readyGo(ActivityDescActivity.class,bundle);
                overridePendingTransition(R.anim.zoomin,0);
            }
        });
    }

    private void loadData() {
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
                        return;
                    }
                    if (response.body().getStatus()==0){
                        shopInfo=response.body().getShopInfo();
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
        Glide.with(getApplicationContext()).load(shopInfo.getShopImg()).into(ivShopImg);
        tvShopName.setText(shopInfo.getShopName());
        tvHot.setText(shopInfo.getActivityTitle());
        tvAllMoney.setText("￥"+shopInfo.getActivityTotal());
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
            if (s.length()!=0){
                btnSubmit.setBackgroundColor(Color.rgb(21,212,143));
                btnSubmit.setClickable(true);
            }else{
                btnSubmit.setBackgroundColor(Color.rgb(204,204,204));
                btnSubmit.setClickable(false);
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
