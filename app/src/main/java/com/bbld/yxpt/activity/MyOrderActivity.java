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
import com.bbld.yxpt.bean.OrderReturnInfo;
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
 * 获取用户订单的返还信息
 * Created by likey on 2017/7/3.
 */

public class MyOrderActivity extends BaseActivity{
    @BindView(R.id.ivBackground)
    ImageView ivBackground;
    @BindView(R.id.tvShopName)
    TextView tvShopName;
    @BindView(R.id.tvChangeShop)
    TextView tvChangeShop;
    @BindView(R.id.tvPeopleCount)
    TextView tvPeopleCount;
    @BindView(R.id.tvToMe)
    TextView tvToMe;
    @BindView(R.id.lvMyorder)
    ListView lvMyorder;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;

    private int whichList;
    private String token;
    private String QueueInfo;
    private OrderReturnInfo.OrderReturnInfoShopInfo ShopInfo;
    private List<OrderReturnInfo.OrderReturnInfoReturnList> ReturnList=null;
    private List<OrderReturnInfo.OrderReturnInfoNextList> NextList=null;
    private List<OrderReturnInfo.OrderReturnInfoMyList> MyList=null;
    private OrderAdapter adapter;
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    srl.setRefreshing(false);
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    protected void initViewsAndEvents() {
        token=new MyToken(this).getToken();
        whichList=2;
        loadData();
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
                            switch (whichList){
                                case 1:
                                    break;
                                case 2:
                                    whichList=1;
                                    setAdapter();
                                    break;
                                case 3:
                                    whichList=2;
                                    setAdapter();
                                    break;
                            }
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        mHandler.sendEmptyMessage(1);
                    }
                }).start();
            }
        });
        lvMyorder.setOnScrollListener(new AbsListView.OnScrollListener() {
            private boolean isBottom;
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                switch (i) {
                    case SCROLL_STATE_FLING:
                        //Log.i("info", "SCROLL_STATE_FLING");
                        break;
                    case SCROLL_STATE_IDLE:
                        if (isBottom) {
                            switch (whichList){
                                case 1:
                                    whichList=2;
                                    setAdapter();
                                    break;
                                case 2:
                                    break;
                                case 3:
                                    break;
                            }
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
        tvToMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                whichList=3;
                setAdapter();
            }
        });
    }

    private void loadData() {
        Call<OrderReturnInfo> call= RetrofitService.getInstance().getOrderReturnInfo(token);
        call.enqueue(new Callback<OrderReturnInfo>() {
            @Override
            public void onResponse(Response<OrderReturnInfo> response, Retrofit retrofit) {
                if (response==null){
                    showToast(responseFail());
                    return;
                }
                if (response.body().getStatus()==0){
                    QueueInfo=response.body().getQueueInfo();
                    ShopInfo=response.body().getShopInfo();
                    ReturnList=response.body().getReturnList();
                    NextList=response.body().getNextList();
                    MyList=response.body().getMyList();
                    setData();
                }else{
                    showToast(response.body().getMes());
                    ActivityManagerUtil.getInstance().finishActivity(MyOrderActivity.class);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    private void setData() {
        Glide.with(getApplicationContext()).load(ShopInfo.getShopImg()).into(ivBackground);
        tvShopName.setText(ShopInfo.getShopName());
        tvPeopleCount.setText(QueueInfo);
        setAdapter();
    }

    private void setAdapter() {
        if (whichList==1){
            if (ReturnList==null){
                return;
            }
        }else if (whichList==2){
            if (NextList==null){
                return;
            }
        }else if (whichList==3){
            if (MyList==null){
                return;
            }
        }else{
            adapter=new OrderAdapter();
            lvMyorder.setAdapter(adapter);
        }
    }

    class OrderAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            if (whichList==1){
                return ReturnList.size();
            }else if (whichList==2){
                return NextList.size();
            }else{
                return MyList.size();
            }
        }

        @Override
        public Object getItem(int i) {
            if (whichList==1){
                return ReturnList.get(i);
            }else if (whichList==2){
                return NextList.get(i);
            }else{
                return MyList.get(i);
            }
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            OrderHolder holder=null;
            if (view==null){
                view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_lv_myorder,null);
                holder=new OrderHolder();
                holder.ivHead=(ImageView)view.findViewById(R.id.ivHead);
                holder.tvNickName=(TextView)view.findViewById(R.id.tvNickName);
                holder.tvEnterAmount=(TextView)view.findViewById(R.id.tvEnterAmount);
                holder.tvAddDate=(TextView)view.findViewById(R.id.tvAddDate);
                holder.tvReturnStatus=(TextView)view.findViewById(R.id.tvReturnStatus);
                view.setTag(holder);
            }
            holder= (OrderHolder) view.getTag();
            if (whichList==1){
                OrderReturnInfo.OrderReturnInfoReturnList item1 =(OrderReturnInfo.OrderReturnInfoReturnList)getItem(i);
                Glide.with(getApplicationContext()).load(item1.getHeadPortrait()).into(holder.ivHead);
                holder.tvNickName.setText(item1.getNickName());
                holder.tvEnterAmount.setText("￥"+item1.getEnterAmount());
                holder.tvAddDate.setText(item1.getAddDate());
                holder.tvReturnStatus.setText(item1.getReturnStatus());
            }else if (whichList==2){
                OrderReturnInfo.OrderReturnInfoNextList item2 =(OrderReturnInfo.OrderReturnInfoNextList)getItem(i);
                Glide.with(getApplicationContext()).load(item2.getHeadPortrait()).into(holder.ivHead);
                holder.tvNickName.setText(item2.getNickName());
                holder.tvEnterAmount.setText("￥"+item2.getEnterAmount());
                holder.tvAddDate.setText(item2.getAddDate());
                holder.tvReturnStatus.setText(item2.getReturnStatus());
            }else{
                OrderReturnInfo.OrderReturnInfoMyList item3 =(OrderReturnInfo.OrderReturnInfoMyList)getItem(i);
                Glide.with(getApplicationContext()).load(item3.getHeadPortrait()).into(holder.ivHead);
                holder.tvNickName.setText(item3.getNickName());
                holder.tvEnterAmount.setText("￥"+item3.getEnterAmount());
                holder.tvAddDate.setText(item3.getAddDate());
                holder.tvReturnStatus.setText(item3.getReturnStatus());
            }
            return view;
        }

        class OrderHolder{
            ImageView ivHead;
            TextView tvNickName,tvEnterAmount,tvAddDate,tvReturnStatus;
        }
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    public int getContentView() {
        return R.layout.activity_myorder;
    }
}
