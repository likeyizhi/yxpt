package com.bbld.yxpt.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bbld.yxpt.R;
import com.bbld.yxpt.base.BaseActivity;
import com.bbld.yxpt.bean.BuyShopInfo;
import com.bbld.yxpt.bean.MyOrderReturnInfo;
import com.bbld.yxpt.bean.ShopActivityOrderList;
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
 * 我的订单02
 * Created by likey on 2017/7/19.
 */

public class MyOrder02Activity extends BaseActivity{
    @BindView(R.id.ivShopImg)
    ImageView ivShopImg;
    @BindView(R.id.tvShopName)
    TextView tvShopName;
    @BindView(R.id.rvShopActivity)
    RecyclerView rvShopActivity;
    @BindView(R.id.lvMyOrder02)
    ListView lvMyOrder02;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;
    @BindView(R.id.rlBottom)
    RelativeLayout rlBottom;
    @BindView(R.id.tvBottom)
    TextView tvBottom;
    @BindView(R.id.ivBack)
    ImageView ivBack;


    private String shopId;
    private String token;
    private List<BuyShopInfo.BuyShopInfolist> aList;
    private int checkPosition=0;
    private ActivityAdapter aAdapter;
    private String checkActivityID;
    private List<ShopActivityOrderList.ShopActivityOrderListReturnList> returnList;
    private List<ShopActivityOrderList.ShopActivityOrderListNextList> nextList;
    private List<MyOrderReturnInfo.MyOrderReturnInfoList> infoList;
    private String lvState="next";
    private MyOrder02NextAdapter mo2NAdapter;
    private MyOrder02ReturnAdapter mo2RAdapter;
    private ReternInfoAdapter reternInfoAdapter;
    private float downX ;    //按下时 的X坐标
    private float downY ;    //按下时 的Y坐标
    private String action="";
    private String OrderID;
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    if (lvState.equals("next")){
                        lvState="return";
                        setMyOrder02Adapter();
                    }
                    if (lvState.equals("info")){
                        lvState="next";
                        setMyOrder02Adapter();
                    }
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
        loadBuyShopInfoData();
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
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        mHandler.sendEmptyMessage(1);
                    }
                }).start();
            }
        });
        lvMyOrder02.setOnScrollListener(new AbsListView.OnScrollListener() {
            private boolean isBottom;
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                switch (i) {
                    case SCROLL_STATE_FLING:
                        //Log.i("info", "SCROLL_STATE_FLING");
                        break;
                    case SCROLL_STATE_IDLE:
                        if (isBottom) {
                            //到最底部
                            if (lvState.equals("return")){
                                lvState="next";
                                setMyOrder02Adapter();
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
        rlBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putString("shopActivityID",checkActivityID);
                bundle.putString("activityTitle",aList.get(checkPosition).getTitle()+"");
                readyGoForResult(MyOrder03Activity.class,606,bundle);
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityManagerUtil.getInstance().finishActivity(MyOrder02Activity.this);
            }
        });
    }

    private void loadBuyShopInfoData() {
        Call<BuyShopInfo> call= RetrofitService.getInstance().getBuyShopInfo(token, shopId);
        call.enqueue(new Callback<BuyShopInfo>() {
            @Override
            public void onResponse(Response<BuyShopInfo> response, Retrofit retrofit) {
                if (response==null){
                    showToast(responseFail());
                    return;
                }
                if (response.body().getStatus()==0){
                    BuyShopInfo shopInfo = response.body();
                    setBuyShopInfo(shopInfo);
                }else{
                    showToast(response.body().getMes());
                }
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    private void setBuyShopInfo(BuyShopInfo shopInfo) {
        Glide.with(getApplicationContext()).load(shopInfo.getShopBigImg()).into(ivShopImg);
        tvShopName.setText(shopInfo.getShopName()+"");
        aList=shopInfo.getList();
        if (checkPosition==0){
            checkActivityID=aList.get(checkPosition).getShopActivityID();
        }
        setActivityAdapter();
        loadMyOrder02Data();
    }

    private void loadMyOrder02Data() {
        Call<ShopActivityOrderList> call=RetrofitService.getInstance().getShopActivityOrderList(token, checkActivityID);
        call.enqueue(new Callback<ShopActivityOrderList>() {
            @Override
            public void onResponse(Response<ShopActivityOrderList> response, Retrofit retrofit) {
                if (response.body()==null){
                    showToast(responseFail());
                    return;
                }
                if (response.body().getStatus()==0){
                    returnList=response.body().getReturnList();
                    nextList=response.body().getNextList();
                    lvState="next";
                    setMyOrder02Adapter();
                }else{
                    showToast(response.body().getMes());
                }
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    private void setMyOrder02Adapter() {
        if (lvState.equals("next")){
            mo2NAdapter=new MyOrder02NextAdapter();
            lvMyOrder02.setAdapter(mo2NAdapter);
        } else {
            mo2RAdapter=new MyOrder02ReturnAdapter();
            lvMyOrder02.setAdapter(mo2RAdapter);
        }
    }

    class MyOrder02NextAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return nextList.size();
        }

        @Override
        public ShopActivityOrderList.ShopActivityOrderListNextList getItem(int i) {
            return nextList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            MyOrder02NextHolder nextHolder=null;
            if (view==null){
                view=LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_myorder02,null);
                nextHolder=new MyOrder02NextHolder();
                nextHolder.ivHeadPortrait=(ImageView)view.findViewById(R.id.ivHeadPortrait);
                nextHolder.tvNickName=(TextView)view.findViewById(R.id.tvNickName);
                nextHolder.tvEnterAmount=(TextView)view.findViewById(R.id.tvEnterAmount);
                nextHolder.tvAddDate=(TextView)view.findViewById(R.id.tvAddDate);
                nextHolder.tvReturnStatus=(TextView)view.findViewById(R.id.tvReturnStatus);
                view.setTag(nextHolder);
            }
            nextHolder= (MyOrder02NextHolder) view.getTag();
            ShopActivityOrderList.ShopActivityOrderListNextList next = getItem(i);
            Glide.with(getApplicationContext()).load(next.getHeadPortrait()).into(nextHolder.ivHeadPortrait);
            nextHolder.tvNickName.setText(next.getNickName()+"");
            nextHolder.tvEnterAmount.setText("￥"+next.getEnterAmount());
            nextHolder.tvAddDate.setText("订单时间："+next.getAddDate()+"");
            nextHolder.tvReturnStatus.setText(next.getReturnStatus()+"");
            return view;
        }

        class MyOrder02NextHolder {
            ImageView ivHeadPortrait;
            TextView tvNickName,tvEnterAmount,tvAddDate,tvReturnStatus;

        }
    }
    class MyOrder02ReturnAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return returnList.size();
        }

        @Override
        public ShopActivityOrderList.ShopActivityOrderListReturnList getItem(int i) {
            return returnList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            MyOrder02ReturnHolder returnHolder=null;
            if (view==null){
                view=LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_myorder02,null);
                returnHolder=new MyOrder02ReturnHolder();
                returnHolder.ivHeadPortrait=(ImageView)view.findViewById(R.id.ivHeadPortrait);
                returnHolder.tvNickName=(TextView)view.findViewById(R.id.tvNickName);
                returnHolder.tvEnterAmount=(TextView)view.findViewById(R.id.tvEnterAmount);
                returnHolder.tvAddDate=(TextView)view.findViewById(R.id.tvAddDate);
                returnHolder.tvReturnStatus=(TextView)view.findViewById(R.id.tvReturnStatus);
                view.setTag(returnHolder);
            }
            returnHolder= (MyOrder02ReturnHolder) view.getTag();
            ShopActivityOrderList.ShopActivityOrderListReturnList next = getItem(i);
            Glide.with(getApplicationContext()).load(next.getHeadPortrait()).into(returnHolder.ivHeadPortrait);
            returnHolder.tvNickName.setText(next.getNickName()+"");
            returnHolder.tvEnterAmount.setText("￥"+next.getEnterAmount());
            returnHolder.tvAddDate.setText("订单时间："+next.getAddDate()+"");
            returnHolder.tvReturnStatus.setText(next.getReturnStatus()+"");
            return view;
        }

        class MyOrder02ReturnHolder {
            ImageView ivHeadPortrait;
            TextView tvNickName,tvEnterAmount,tvAddDate,tvReturnStatus;

        }
    }

    private void setActivityAdapter() {
        //新建一个线性布局管理器
        LinearLayoutManager manager=new LinearLayoutManager(this);
        //设置recyclerview的布局管理器
        rvShopActivity.setLayoutManager(manager);
        //设置水平方向
        manager.setOrientation(OrientationHelper.HORIZONTAL);
        //新建适配器
        aAdapter=new ActivityAdapter();
        //设置recyclerview的适配器
        rvShopActivity.setAdapter(aAdapter);
    }

    class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ActivityHolder>{
        @Override
        public ActivityHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ActivityHolder aHolder=new ActivityHolder(LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_myorder02_activity,null));
            return aHolder;
        }

        @Override
        public void onBindViewHolder(ActivityHolder holder, final int position) {
            holder.tvActivityName.setText(aList.get(position).getTitle()+"");
            holder.tvActivityName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkPosition=position;
                    checkActivityID=aList.get(checkPosition).getShopActivityID();
                    aAdapter.notifyDataSetChanged();
                    loadMyOrder02Data();
                }
            });
            if (checkPosition==position){
                holder.tvLine.setVisibility(View.VISIBLE);
            }else{
                holder.tvLine.setVisibility(View.INVISIBLE);
            }
            tvBottom.setText(Html.fromHtml("<font color=\"#0BD28A\">"+"\""+aList.get(checkPosition).getTitle()+"\""+"</font>"+"活动我的订单排行(共"+aList.get(checkPosition).getOrderCount()+"单)"));
        }

        @Override
        public int getItemCount() {
            return aList.size();
        }

        class ActivityHolder extends RecyclerView.ViewHolder{
            private TextView tvActivityName,tvLine;
            public ActivityHolder (View itemView){
                super(itemView);
                tvActivityName=(TextView)itemView.findViewById(R.id.tvActivityName);
                tvLine=(TextView)itemView.findViewById(R.id.tvLine);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case 606:
                OrderID=data.getExtras().getString("OrderID");
                showToast(OrderID+"");
                loadMyOrderReturnInfo();
                break;
            default:
                break;
        }
    }

    private void loadMyOrderReturnInfo() {
        Call<MyOrderReturnInfo> call=RetrofitService.getInstance().getMyOrderReturnInfo(token,OrderID);
        call.enqueue(new Callback<MyOrderReturnInfo>() {
            @Override
            public void onResponse(Response<MyOrderReturnInfo> response, Retrofit retrofit) {
                if (response==null){
                    showToast(responseFail());
                    return;
                }
                if (response.body().getStatus()==0){
                    infoList = response.body().getList();
                    setReternInfoAdapter();
                }else{
                    showToast(response.body().getMes());
                }
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    private void setReternInfoAdapter() {
        reternInfoAdapter=new ReternInfoAdapter();
        lvMyOrder02.setAdapter(reternInfoAdapter);
        lvState="info";
    }

    class ReternInfoAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return infoList.size();
        }

        @Override
        public MyOrderReturnInfo.MyOrderReturnInfoList getItem(int i) {
            return infoList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ReternInfoHolder returnHolder=null;
            if (view==null){
                view=LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_myorder02,null);
                returnHolder=new ReternInfoHolder();
                returnHolder.ivHeadPortrait=(ImageView)view.findViewById(R.id.ivHeadPortrait);
                returnHolder.tvNickName=(TextView)view.findViewById(R.id.tvNickName);
                returnHolder.tvEnterAmount=(TextView)view.findViewById(R.id.tvEnterAmount);
                returnHolder.tvAddDate=(TextView)view.findViewById(R.id.tvAddDate);
                returnHolder.tvReturnStatus=(TextView)view.findViewById(R.id.tvReturnStatus);
                view.setTag(returnHolder);
            }
            returnHolder= (ReternInfoHolder) view.getTag();
            MyOrderReturnInfo.MyOrderReturnInfoList next = getItem(i);
            Glide.with(getApplicationContext()).load(next.getHeadPortrait()).into(returnHolder.ivHeadPortrait);
            returnHolder.tvNickName.setText(next.getNickName()+"");
            returnHolder.tvEnterAmount.setText("￥"+next.getEnterAmount());
            returnHolder.tvAddDate.setText("订单时间："+next.getAddDate()+"");
            returnHolder.tvReturnStatus.setText(next.getReturnStatus()+"");
            return view;
        }

        class ReternInfoHolder {
            ImageView ivHeadPortrait;
            TextView tvNickName,tvEnterAmount,tvAddDate,tvReturnStatus;

        }
    }

    @Override
    protected void getBundleExtras(Bundle extras) {
        shopId=extras.getString("shopId");
    }

    @Override
    public int getContentView() {
        return R.layout.activity_myorder02;
    }
}
