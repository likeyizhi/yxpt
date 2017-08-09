package com.bbld.yxpt.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bbld.yxpt.R;
import com.bbld.yxpt.base.BaseActivity;
import com.bbld.yxpt.bean.ShopInfo;
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
 * 店铺信息
 * Created by likey on 2017/7/3.
 */

public class ShopDetailsActivity extends BaseActivity{
    @BindView(R.id.ivBack)
    ImageView ivBack;
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
    @BindView(R.id.wvDetail)
    WebView wvDetail;


    private String shopId;
    private double shopX;
    private double shopY;
    private String token;
    private Call<ShopInfo> call;
    private ShopInfo.ShopInfoShopInfo shopInfo;
    private double mCurrentLat;
    private double mCurrentLon;
    private String mCurrentCity;

    @Override
    protected void initViewsAndEvents() {
        token=new MyToken(ShopDetailsActivity.this).getToken();
        loadData();
        setListeners();
    }
    private void setListeners() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityManagerUtil.getInstance().finishActivity(ShopDetailsActivity.this);
            }
        });
        ivHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putDouble("shopX",shopX);
                bundle.putDouble("shopY",shopY);
                bundle.putDouble("mCurrentLat",mCurrentLat);
                bundle.putDouble("mCurrentLon",mCurrentLon);
                bundle.putString("mCurrentCity",mCurrentCity);
                readyGo(RoutePlanActivity.class,bundle);
            }
        });
    }
    private void loadData() {
        call= RetrofitService.getInstance().getShopInfo("",shopId+"",mCurrentLon+"",mCurrentLat+"");
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

    private void setData() {
        Glide.with(getApplicationContext()).load(shopInfo.getShopBigImg()).into(ivBackground);
//        Glide.with(getApplicationContext()).load(shopInfo.getShopImg()).into(ivHead);
        tvShopName.setText(shopInfo.getShopName());
        tvTag.setText(shopInfo.getTag());
        tvDistance.setText(shopInfo.getDistance());
        tvAddress.setText(shopInfo.getAddress());
        tvPhoneNumber.setText(/*shopInfo.getContact()+"，"+*/shopInfo.getLinkPhone());
        wvDetail.loadUrl(shopInfo.getDetailsUrl());
    }

    @Override
    protected void getBundleExtras(Bundle extras) {
        shopId=extras.getString("shopId");
        shopX=extras.getDouble("shopX");
        shopY=extras.getDouble("shopY");
        mCurrentLat=extras.getDouble("mCurrentLat");
        mCurrentLon=extras.getDouble("mCurrentLon");
        mCurrentCity=extras.getString("mCurrentCity");
    }

    @Override
    public int getContentView() {
        return R.layout.activity_shopdetails;
    }
}
