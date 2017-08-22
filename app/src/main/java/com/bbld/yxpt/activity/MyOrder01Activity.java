package com.bbld.yxpt.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bbld.yxpt.R;
import com.bbld.yxpt.base.BaseActivity;
import com.bbld.yxpt.bean.BuyShopList;
import com.bbld.yxpt.network.RetrofitService;
import com.bbld.yxpt.utils.MyToken;
import com.bumptech.glide.Glide;
import com.wuxiaolong.androidutils.library.ActivityManagerUtil;

import java.util.List;

import butterknife.BindView;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * 我的订单01
 * Created by likey on 2017/7/19.
 */

public class MyOrder01Activity extends BaseActivity{
    @BindView(R.id.lvMyOrder01)
    ListView lvMyOrder01;
    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.ivKong)
    ImageView ivKong;

    private String token;
    private List<BuyShopList.BuyShopListlist> buyShopList;
    private MyOrder01Adapter adapter;

    @Override
    protected void initViewsAndEvents() {
        token=new MyToken(this).getToken();
        loadData();
        setListeners();
    }

    private void setListeners() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    ActivityManagerUtil.getInstance().finishActivity(MyOrder01Activity.this);
                }catch (Exception e){
                    showToast(someException());
                }
            }
        });
    }

    private void loadData() {
        try {
            Call<BuyShopList> call= RetrofitService.getInstance().getBuyShopList(token);
            call.enqueue(new Callback<BuyShopList>() {
                @Override
                public void onResponse(Response<BuyShopList> response, Retrofit retrofit) {
                    if (response==null){
                        showToast(responseFail());
                        return;
                    }
                    if (response.body().getStatus()==0){
                        buyShopList = response.body().getList();
                        if (buyShopList.size()==0){
                            ivKong.setVisibility(View.VISIBLE);
                        }
                        setAdapter();
                    }else{
                        showToast(response.body().getMes());
                    }
                }

                @Override
                public void onFailure(Throwable throwable) {

                }
            });
        }catch (Exception e){
            showToast(someException());
        }
    }

    private void setAdapter() {
        try {
            adapter=new MyOrder01Adapter();
            lvMyOrder01.setAdapter(adapter);
        }catch (Exception e){
            showToast(someException());
        }
    }

    class MyOrder01Adapter extends BaseAdapter{

        @Override
        public int getCount() {
            return buyShopList.size();
        }

        @Override
        public BuyShopList.BuyShopListlist getItem(int i) {
            return buyShopList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            MyOrder01Holder holder01=null;
            if (view==null){
                view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_myorder01,null);
                holder01=new MyOrder01Holder();
                holder01.ivShopImg=(ImageView)view.findViewById(R.id.ivShopImg);
                holder01.tvShopName=(TextView)view.findViewById(R.id.tvShopName);
                holder01.tvShopAddr=(TextView)view.findViewById(R.id.tvShopAddr);
                holder01.tvActivityCount=(TextView)view.findViewById(R.id.tvActivityCount);
                holder01.tvOrderCount=(TextView)view.findViewById(R.id.tvOrderCount);
                view.setTag(holder01);
            }
            holder01= (MyOrder01Holder) view.getTag();
            final BuyShopList.BuyShopListlist buyShop = getItem(i);
            try {
                Glide.with(getApplicationContext()).load(buyShop.getShopImg()).into(holder01.ivShopImg);
                holder01.tvShopName.setText(buyShop.getShopName()+"");
                holder01.tvShopAddr.setText(buyShop.getAddress()+"");
                holder01.tvActivityCount.setText(buyShop.getActivityCount()+"");
                holder01.tvOrderCount.setText(buyShop.getOrderCount()+"");
                if (view!=null){
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Bundle bundle=new Bundle();
                            bundle.putString("shopId", buyShop.getShopID()+"");
                            readyGo(MyOrder02Activity.class,bundle);
                        }
                    });
                }
            }catch (Exception e){
                showToast(someException());
            }
            return view;
        }

        class MyOrder01Holder{
            ImageView ivShopImg;
            TextView tvShopName,tvShopAddr,tvActivityCount,tvOrderCount;
        }
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    public int getContentView() {
        return R.layout.activity_myorder01;
    }
}
