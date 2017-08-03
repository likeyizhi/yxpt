package com.bbld.yxpt.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bbld.yxpt.R;
import com.bbld.yxpt.base.BaseActivity;
import com.bbld.yxpt.bean.ShopActivityMyOrderList;
import com.bbld.yxpt.network.RetrofitService;
import com.bbld.yxpt.utils.MyToken;
import com.wuxiaolong.androidutils.library.ActivityManagerUtil;

import java.util.List;

import butterknife.BindView;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * 我的订单03
 * Created by likey on 2017/7/19.
 */

public class MyOrder03Activity extends BaseActivity{
    @BindView(R.id.lvMyOrder03)
    ListView lvMyOrder03;
    @BindView(R.id.tvActivityTitle)
    TextView tvActivityTitle;
    @BindView(R.id.ivClose)
    ImageView ivClose;

    private String shopActivityID;
    private String token;
    private List<ShopActivityMyOrderList.ShopActivityMyOrderListList> myOrderList;
    private String activityTitle;

    @Override
    protected void initViewsAndEvents() {
        token=new MyToken(this).getToken();
        tvActivityTitle.setText("\""+activityTitle+"\"");
        loadData();
        setListeners();
    }

    private void setListeners() {
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityManagerUtil.getInstance().finishActivity(MyOrder03Activity.this);
                overridePendingTransition(0,R.anim.top_to_bottoom);
            }
        });
    }

    private void loadData() {
        Call<ShopActivityMyOrderList> call= RetrofitService.getInstance().getShopActivityMyOrderList(token,shopActivityID);
        call.enqueue(new Callback<ShopActivityMyOrderList>() {
            @Override
            public void onResponse(Response<ShopActivityMyOrderList> response, Retrofit retrofit) {
                if (response==null){
                    showToast(responseFail());
                    return;
                }
                if (response.body().getStatus()==0){
                    myOrderList = response.body().getList();
                    setAdapter();
                }else{
                    showToast(response.body().getMes());
                }
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    private void setAdapter() {
        MyOrder03Adapter adapter=new MyOrder03Adapter();
        lvMyOrder03.setAdapter(adapter);
    }

    class MyOrder03Adapter extends BaseAdapter{

        @Override
        public int getCount() {
            return myOrderList.size();
        }

        @Override
        public ShopActivityMyOrderList.ShopActivityMyOrderListList getItem(int i) {
            return myOrderList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            MyOrder03Holder holder=null;
            if (view==null){
                view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_myorder03,null);
                holder=new MyOrder03Holder();
                holder.tvDate=(TextView)view.findViewById(R.id.tvDate);
                holder.tvEnterAmount=(TextView)view.findViewById(R.id.tvEnterAmount);
                holder.tvSequence=(TextView)view.findViewById(R.id.tvSequence);
                holder.ivThisOrder=(ImageView) view.findViewById(R.id.ivThisOrder);
                view.setTag(holder);
            }
            holder= (MyOrder03Holder) view.getTag();
            final ShopActivityMyOrderList.ShopActivityMyOrderListList order = getItem(i);
            holder.tvDate.setText(order.getAddDate()+"");
            holder.tvEnterAmount.setText("消费"+order.getEnterAmount()+"元");
            holder.tvSequence.setText(order.getSequence()+"");
            holder.ivThisOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent();
                    intent.putExtra("OrderID",order.getNOID()+"");
                    setResult(606,intent);
                    finish();
                    overridePendingTransition(0,R.anim.top_to_bottoom);
                }
            });
            return view;
        }

        class MyOrder03Holder{
            TextView tvDate,tvEnterAmount,tvSequence;
            ImageView ivThisOrder;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
            ActivityManagerUtil.getInstance().finishActivity(this);
            overridePendingTransition(0,R.anim.top_to_bottoom);
        }
        return false;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {
        shopActivityID=extras.getString("shopActivityID");
        activityTitle=extras.getString("activityTitle");
    }

    @Override
    public int getContentView() {
        return R.layout.activity_myorder03;
    }
}
