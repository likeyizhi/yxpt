package com.bbld.yxpt.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bbld.yxpt.R;
import com.bbld.yxpt.base.BaseActivity;
import com.bbld.yxpt.bean.ShopInfo;
import com.bbld.yxpt.network.RetrofitService;
import com.bbld.yxpt.utils.MyToken;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * 店铺信息
 * Created by likey on 2017/7/3.
 */

public class ShopDetailsActivity extends BaseActivity{
    @BindView(R.id.ivBackground)
    ImageView ivBackground;
    @BindView(R.id.ivHead)
    ImageView ivHead;
    @BindView(R.id.tvFeedback)
    TextView tvFeedback;
    @BindView(R.id.tvShopName)
    TextView tvShopName;
    @BindView(R.id.tvTag)
    TextView tvTag;
    @BindView(R.id.tvDistance)
    TextView tvDistance;
    @BindView(R.id.tvAddress)
    TextView tvAddress;
    @BindView(R.id.tvPhoneNumber)
    TextView tvPhoneNumber;


    private String shopId;
    private String shopX;
    private String shopY;
    private String token;
    private Call<ShopInfo> call;
    private ShopInfo.ShopInfoShopInfo shopInfo;

    @Override
    protected void initViewsAndEvents() {
        token=new MyToken(ShopDetailsActivity.this).getToken();
        loadData();
    }

    private void loadData() {
        if (token==null || token.equals("")){
            call= RetrofitService.getInstance().getShopInfo("",shopId,shopX,shopY);
            showToast("请先登录");
        }else{
            call= RetrofitService.getInstance().getShopInfo(token,shopId,shopX,shopY);
            call.enqueue(new Callback<ShopInfo>() {
                @Override
                public void onResponse(Response<ShopInfo> response, Retrofit retrofit) {
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
        Glide.with(getApplicationContext()).load(shopInfo.getShopImg()).into(ivBackground);
        Glide.with(getApplicationContext()).load(shopInfo.getShopImg()).into(ivHead);
        tvShopName.setText(shopInfo.getShopName());
        tvTag.setText(shopInfo.getTag());
        tvDistance.setText(shopInfo.getDistance());
        tvAddress.setText(shopInfo.getAddress());
        tvPhoneNumber.setText(shopInfo.getContact()+"，"+shopInfo.getLinkPhone());
    }

    @Override
    protected void getBundleExtras(Bundle extras) {
        shopId=extras.getString("shopId");
        shopX=extras.getString("shopX");
        shopY=extras.getString("shopY");
    }

    @Override
    public int getContentView() {
        return R.layout.activity_shopdetails;
    }
}
