package com.bbld.yxpt.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bbld.yxpt.R;
import com.bbld.yxpt.base.BaseActivity;
import com.bbld.yxpt.bean.UserOrderList;
import com.bbld.yxpt.bean.UserReturnOrderList;
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

import static android.R.id.list;

/**
 * 奖励金额
 * Created by likey on 2017/7/3.
 */

public class ReceiveMoneyActivity extends BaseActivity{
    @BindView(R.id.tvMoney)
    TextView tvMoney;
    @BindView(R.id.lvOrder)
    ListView lvOrder;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;
    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.ivKong)
    ImageView ivKong;

    private String token;
    private int count;
    private String total;
    private int pageIndex=1;
    private ReceiveAdapter receiveAdapter;
    private List<UserReturnOrderList.UserReturnOrderListlist> list;
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    try {
                        pageIndex=1;
                        loadData(false);
                        srl.setRefreshing(false);
                    }catch (Exception e){
                        showToast(someException());
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void initViewsAndEvents() {
        token=new MyToken(this).getToken();
        pageIndex=1;
        loadData(false);
        setListeners();
    }
    private void setListeners() {
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        mHandler.sendEmptyMessage(1);
                    }
                }).start();
            }
        });
        lvOrder.setOnScrollListener(new AbsListView.OnScrollListener() {
            private boolean isBottom;
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                try {
                    switch (i) {
                        case SCROLL_STATE_FLING:
                            //Log.i("info", "SCROLL_STATE_FLING");
                            break;
                        case SCROLL_STATE_IDLE:
                            try {
                                if (isBottom) {
                                    pageIndex++;
                                    loadData(true);
                                }
                            }catch (Exception e){
                                showToast(someException());
                            }
                            break;
                    }
                }catch (Exception e){
                    showToast(someException());
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(firstVisibleItem+visibleItemCount == totalItemCount){
                    //Log.i("info", "到底了....");
                    isBottom = true;
                }else{
                    isBottom = false;
                }
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    ActivityManagerUtil.getInstance().finishActivity(ReceiveMoneyActivity.this);
                }catch (Exception e){
                    showToast(someException());
                }
            }
        });
    }

    private void loadData(final boolean isLoadMore) {
        try {
            Call<UserReturnOrderList> call= RetrofitService.getInstance().getUserReturnOrderList(token,pageIndex);
            call.enqueue(new Callback<UserReturnOrderList>() {
                @Override
                public void onResponse(Response<UserReturnOrderList> response, Retrofit retrofit) {
                    if (response==null){
                        showToast(responseFail());
                        return;
                    }
                    if (response.body().getStatus()==0){
                        try {
                            count=response.body().getCount();
                            total=response.body().getTotal();
                            if (isLoadMore){
                                try {
                                    List<UserReturnOrderList.UserReturnOrderListlist> listAdd = response.body().getList();
                                    list.addAll(listAdd);
                                    receiveAdapter.notifyDataSetChanged();
                                }catch (Exception e){
                                    showToast(someException());
                                }
                            }else{
                                try {
                                    list = response.body().getList();
                                    if (list.size()==0){
                                        ivKong.setVisibility(View.VISIBLE);
                                    }
                                    setData();
                                }catch (Exception e){
                                    showToast(someException());
                                }
                            }
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
        }catch (Exception e){
            showToast(someException());
        }
    }

    private void setData() {
        try {
            tvMoney.setText(total);
        }catch (Exception e){
            showToast(someException());
        }
        setPayAdapter();
    }

    private void setPayAdapter() {
        try {
            receiveAdapter=new ReceiveAdapter();
            lvOrder.setAdapter(receiveAdapter);
        }catch (Exception e){
            showToast(someException());
        }
    }

    class ReceiveAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public UserReturnOrderList.UserReturnOrderListlist getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ReceiveHolder holder=null;
            if (view==null){
                view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_lv_3money,null);
                holder=new ReceiveHolder();
                holder.tvActivityTitle=(TextView)view.findViewById(R.id.tvActivityTitle);
                holder.tvAddDate=(TextView) view.findViewById(R.id.tvAddDate);
                holder.tvEnterAmount=(TextView)view.findViewById(R.id.tvEnterAmount);
                holder.ivHead=(ImageView) view.findViewById(R.id.ivHead);
                view.setTag(holder);
            }
            UserReturnOrderList.UserReturnOrderListlist item = getItem(i);
            holder= (ReceiveHolder) view.getTag();
            try {
                holder.tvActivityTitle.setText(item.getShopName()+"");
                holder.tvAddDate.setText("返现时间："+item.getAddDate()+"");
                holder.tvEnterAmount.setTextColor(Color.rgb(102,204,255));
                holder.tvEnterAmount.setText("￥"+item.getEnterAmount());
                Glide.with(getApplicationContext()).load(item.getShopImg()).into(holder.ivHead);
            }catch (Exception e){
                showToast(someException());
            }
            return view;
        }

        class ReceiveHolder{
            TextView tvActivityTitle,tvAddDate,tvEnterAmount;
            ImageView ivHead;
        }
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    public int getContentView() {
        return R.layout.activity_receivemoney;
    }
}
