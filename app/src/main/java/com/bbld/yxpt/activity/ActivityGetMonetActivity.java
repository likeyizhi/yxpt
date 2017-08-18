package com.bbld.yxpt.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bbld.yxpt.R;
import com.bbld.yxpt.bean.ScanShop;
import com.bbld.yxpt.network.RetrofitService;
import com.bbld.yxpt.utils.MyToken;
import com.bumptech.glide.Glide;

import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * 获奖名单
 * Created by likey on 2017/8/2.
 */

public class ActivityGetMonetActivity extends Activity{
    private ListView lvGetMoney;
    private RelativeLayout rlActivittyDescClose;
    private String activityCode;
    private String token;
    private Call<ScanShop> call;
    private List<ScanShop.ScanShopList> gmList;
    private GMAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_get_money);
        token=new MyToken(this).getToken();
        activityCode=getIntent().getExtras().getString("ActivityCode");
        initView();
        loadData();
        setListeners();
    }

    private void setListeners() {
        rlActivittyDescClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    finish();
                    overridePendingTransition(0,R.anim.zoomout);
                }catch (Exception e){
                    Toast.makeText(ActivityGetMonetActivity.this,getString(R.string.some_exception),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadData() {
        try {
            if (token==null || token.equals("")){
                call= RetrofitService.getInstance().scanShop("", activityCode);
                Toast.makeText(ActivityGetMonetActivity.this,"未登录",Toast.LENGTH_SHORT).show();
            }else {
                call= RetrofitService.getInstance().scanShop(token, activityCode);
                call.enqueue(new Callback<ScanShop>() {
                    @Override
                    public void onResponse(Response<ScanShop> response, Retrofit retrofit) {
                        if (response==null){
                            Toast.makeText(ActivityGetMonetActivity.this,"数据获取失败，请重试",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (response.body().getStatus()==0){
                            try {
                                gmList = response.body().getList();
                                setData();
                            }catch (Exception e){
                                Toast.makeText(ActivityGetMonetActivity.this,getString(R.string.some_exception),Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(ActivityGetMonetActivity.this,""+response.body().getMes(),Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Throwable throwable) {

                    }
                });
            }
        }catch (Exception e){
            Toast.makeText(ActivityGetMonetActivity.this,getString(R.string.some_exception),Toast.LENGTH_SHORT).show();
        }
    }

    private void setData() {
        try {
            adapter=new GMAdapter();
            lvGetMoney.setAdapter(adapter);
        }catch (Exception e){
            Toast.makeText(ActivityGetMonetActivity.this,getString(R.string.some_exception),Toast.LENGTH_SHORT).show();
        }
    }

    class GMAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return gmList.size();
        }

        @Override
        public ScanShop.ScanShopList getItem(int i) {
            return gmList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            GMHolder holder=null;
            if (view==null){
                view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_activity_gm,null);
                holder=new GMHolder();
                holder.img=(ImageView)view.findViewById(R.id.img);
                holder.phone=(TextView)view.findViewById(R.id.phone);
                holder.money=(TextView)view.findViewById(R.id.money);
                view.setTag(holder);
            }
            ScanShop.ScanShopList gm = getItem(i);
            holder= (GMHolder) view.getTag();
            try {
                Glide.with(getApplicationContext()).load(gm.getHeadPortrait()).into(holder.img);
                holder.phone.setText(gm.getNickName()+"");
                holder.money.setText("￥"+gm.getReturnPrice());
            }catch (Exception e){
                Toast.makeText(ActivityGetMonetActivity.this,getString(R.string.some_exception),Toast.LENGTH_SHORT).show();
            }
            return view;
        }

        class GMHolder{
            ImageView img;
            TextView phone,money;
        }
    }

    private void initView() {
        try {
            lvGetMoney=(ListView)findViewById(R.id.lvGetMoney);
            rlActivittyDescClose=(RelativeLayout)findViewById(R.id.rlActivittyDescClose);
        }catch (Exception e){
            Toast.makeText(ActivityGetMonetActivity.this,getString(R.string.some_exception),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
            finish();
            overridePendingTransition(0,R.anim.zoomout);
        }
        return false;
    }
}
