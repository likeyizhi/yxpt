package com.bbld.yxpt.activity;

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

/**
 * 消费金额
 * Created by likey on 2017/7/3.
 */

public class PayMoneyActivity extends BaseActivity{
    @BindView(R.id.tvMoney)
    TextView tvMoney;
    @BindView(R.id.lvOrder)
    ListView lvOrder;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;
    @BindView(R.id.ivBack)
    ImageView ivBack;

    private String token;
    private int count;
    private String total;
    private int pageIndex=1;
    private int pageSize=10;
    private List<UserOrderList.UserOrderListlist> list;
    private PayAdapter payAdapter;
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    srl.setRefreshing(false);
                    pageIndex=1;
                    loadData(false);
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
        pageSize=10;
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
                switch (i) {
                    case SCROLL_STATE_FLING:
                        //Log.i("info", "SCROLL_STATE_FLING");
                        break;
                    case SCROLL_STATE_IDLE:
                        if (isBottom) {
                            pageIndex++;
                            loadData(true);
                        }
                        break;
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
                ActivityManagerUtil.getInstance().finishActivity(PayMoneyActivity.this);
            }
        });
    }

    private void loadData(final boolean isLoadMore) {
        Call<UserOrderList> call= RetrofitService.getInstance().getUserOrderList(token,pageIndex);
        call.enqueue(new Callback<UserOrderList>() {
            @Override
            public void onResponse(Response<UserOrderList> response, Retrofit retrofit) {
                if (response==null){
                    showToast(responseFail());
                    return;
                }
                if (response.body().getStatus()==0){
//                    showToast(token+","+pageIndex+","+pageSize+","+response.body().getMes());
                    count=response.body().getCount();
                    total=response.body().getTotal();
                    if (isLoadMore){
                        List<UserOrderList.UserOrderListlist> listAdd = response.body().getList();
                        list.addAll(listAdd);
                        payAdapter.notifyDataSetChanged();
                    }else{
                        list = response.body().getList();
                        setData();
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

    private void setData() {
        tvMoney.setText(total);
        setPayAdapter();
    }

    private void setPayAdapter() {
        payAdapter=new PayAdapter();
        lvOrder.setAdapter(payAdapter);
    }

    class PayAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public UserOrderList.UserOrderListlist getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            PayHolder holder=null;
            if (view==null){
                view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_lv_3money,null);
                holder=new PayHolder();
                holder.tvActivityTitle=(TextView)view.findViewById(R.id.tvActivityTitle);
                holder.tvAddDate=(TextView) view.findViewById(R.id.tvAddDate);
                holder.tvEnterAmount=(TextView)view.findViewById(R.id.tvEnterAmount);
                holder.ivHead=(ImageView) view.findViewById(R.id.ivHead);
                view.setTag(holder);
            }
            UserOrderList.UserOrderListlist item = getItem(i);
            holder= (PayHolder) view.getTag();
            holder.tvActivityTitle.setText(item.getShopName()+"");
            holder.tvAddDate.setText("消费时间："+item.getAddDate()+"");
            holder.tvEnterAmount.setText("￥"+item.getEnterAmount());
            Glide.with(getApplicationContext()).load(item.getShopImg()).into(holder.ivHead);
            return view;
        }

        class PayHolder{
            TextView tvActivityTitle,tvAddDate,tvEnterAmount;
            ImageView ivHead;
        }
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    public int getContentView() {
        return R.layout.activity_paymoney;
    }
}
