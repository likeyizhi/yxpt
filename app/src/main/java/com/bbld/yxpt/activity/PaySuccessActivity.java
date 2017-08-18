package com.bbld.yxpt.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bbld.yxpt.R;
import com.bbld.yxpt.base.BaseActivity;
import com.bumptech.glide.Glide;
import com.wuxiaolong.androidutils.library.ActivityManagerUtil;

import butterknife.BindView;

/**
 * 支付成功
 * Created by dell on 2017/7/28.
 */

public class PaySuccessActivity extends BaseActivity{
    @BindView(R.id.ivShopImg)
    ImageView ivShopImg;
    @BindView(R.id.tvShopName)
    TextView tvShopName;
    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.tv_go)
    TextView tvGo;
    @BindView(R.id.tv_amount)
    TextView tvAmount;
    private String money;
    private String shopImg;
    private String shopName;
    @Override
    protected void initViewsAndEvents() {
        try {
            tvAmount.setText(money+"");
            tvShopName.setText(shopName+"");
            Glide.with(getApplicationContext()).load(shopImg).into(ivShopImg);
        }catch (Exception e){
            showToast(someException());
        }
        setListeners();
    }
    private void setListeners(){
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    ActivityManagerUtil.getInstance().finishActivity(PaySuccessActivity.this);
                }catch (Exception e){
                    showToast(someException());
                }
            }
        });
        tvGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    ActivityManagerUtil.getInstance().finishActivity(PaySuccessActivity.this);
                }catch (Exception e){
                    showToast(someException());
                }
            }
        });
    }
    @Override
    protected void getBundleExtras(Bundle extras) {
        money=extras.getString("money");
        shopImg=extras.getString("ShopImg");
        shopName=extras.getString("ShopName");
    }

    @Override
    public int getContentView() {
        return R.layout.activity_paysuccess;
    }
}
