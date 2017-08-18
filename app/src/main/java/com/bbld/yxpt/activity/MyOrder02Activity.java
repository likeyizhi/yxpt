package com.bbld.yxpt.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bbld.yxpt.R;
import com.bbld.yxpt.base.BaseActivity;
import com.bbld.yxpt.bean.BuyShopInfo;
import com.bbld.yxpt.bean.MyOrder02;
import com.bbld.yxpt.bean.MyOrderReturnInfo;
import com.bbld.yxpt.bean.ShopActivityOrderList;
import com.bbld.yxpt.network.RetrofitService;
import com.bbld.yxpt.utils.MyToken;
import com.bumptech.glide.Glide;
import com.wuxiaolong.androidutils.library.ActivityManagerUtil;

import java.util.ArrayList;
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
    @BindView(R.id.rlBottom)
    RelativeLayout rlBottom;
    @BindView(R.id.tvBottom)
    TextView tvBottom;
    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;


    private String shopId;
    private String token;
    private List<BuyShopInfo.BuyShopInfolist> aList;
    private int checkPosition=0;
    private ActivityAdapter aAdapter;
    private String checkActivityID;
    private List<ShopActivityOrderList.ShopActivityOrderListReturnList> returnList;
    private List<ShopActivityOrderList.ShopActivityOrderListNextList> nextList;
    private List<MyOrderReturnInfo.MyOrderReturnInfoList> infoList;
    private MyOrder02NextAdapter mo2NAdapter;
    private String OrderID;
    private ArrayList<MyOrder02> myOrders;
    private MyOrderBackAdapter backAdapter;
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    try {
                        loadMyOrder02Data();
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
        loadBuyShopInfoData();
        setListeners();
    }

    private void setListeners() {
        rlBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Bundle bundle=new Bundle();
                    bundle.putString("shopActivityID",checkActivityID);
                    bundle.putString("activityTitle",aList.get(checkPosition).getTitle()+"");
                    readyGoForResult(MyOrder03Activity.class,606,bundle);
                    overridePendingTransition(R.anim.bottom_to_top,0);
                }catch (Exception e){
                    showToast(someException());
                }
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    ActivityManagerUtil.getInstance().finishActivity(MyOrder02Activity.this);
                }catch (Exception e){
                    showToast(someException());
                }
            }
        });
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
    }

    private void loadBuyShopInfoData() {
        try {
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
        }catch (Exception e){
            showToast(someException());
        }
    }

    private void setBuyShopInfo(BuyShopInfo shopInfo) {
        try {
            Glide.with(getApplicationContext()).load(shopInfo.getShopBigImg()).into(ivShopImg);
            tvShopName.setText(shopInfo.getShopName()+"");
            aList=shopInfo.getList();
            if (checkPosition==0){
                checkActivityID=aList.get(checkPosition).getShopActivityID();
            }
        }catch (Exception e){
            showToast(someException());
        }
        try {
            setActivityAdapter();
            loadMyOrder02Data();
        }catch (Exception e){
            showToast(someException());
        }
    }

    private void loadMyOrder02Data() {
        try {
            Call<ShopActivityOrderList> call=RetrofitService.getInstance().getShopActivityOrderList(token, checkActivityID);
            call.enqueue(new Callback<ShopActivityOrderList>() {
                @Override
                public void onResponse(Response<ShopActivityOrderList> response, Retrofit retrofit) {
                    if (response.body()==null){
                        showToast(responseFail());
                        return;
                    }
                    if (response.body().getStatus()==0){
                        try {
                            returnList=response.body().getReturnList();
                            nextList=response.body().getNextList();
                            myOrders=new ArrayList<MyOrder02>();
                            for (int r=0;r<returnList.size();r++){
                                MyOrder02 myOrder=new MyOrder02();
                                myOrder.setNOID(returnList.get(r).getNOID());
                                myOrder.setNickName(returnList.get(r).getNickName());
                                myOrder.setHeadPortrait(returnList.get(r).getHeadPortrait());
                                myOrder.setActivityTitle(returnList.get(r).getActivityTitle());
                                myOrder.setOrderNo(returnList.get(r).getOrderNo());
                                myOrder.setActivityDiscount(returnList.get(r).getActivityDiscount());
                                myOrder.setEnterAmount(returnList.get(r).getEnterAmount());
                                myOrder.setAddDate(returnList.get(r).getAddDate());
                                myOrder.setIsReturn(returnList.get(r).getIsReturn());
                                myOrder.setReturnStatus(returnList.get(r).getReturnStatus());
                                myOrders.add(myOrder);
                            }
                            for (int n=0;n<nextList.size();n++){
                                MyOrder02 myOrder=new MyOrder02();
                                myOrder.setNOID(nextList.get(n).getNOID());
                                myOrder.setNickName(nextList.get(n).getNickName());
                                myOrder.setHeadPortrait(nextList.get(n).getHeadPortrait());
                                myOrder.setActivityTitle(nextList.get(n).getActivityTitle());
                                myOrder.setOrderNo(nextList.get(n).getOrderNo());
                                myOrder.setActivityDiscount(nextList.get(n).getActivityDiscount());
                                myOrder.setEnterAmount(nextList.get(n).getEnterAmount());
                                myOrder.setAddDate(nextList.get(n).getAddDate());
                                myOrder.setIsReturn(nextList.get(n).getIsReturn());
                                myOrder.setReturnStatus(nextList.get(n).getReturnStatus());
                                myOrders.add(myOrder);
                            }
                            setMyOrder02Adapter();
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

    private void setMyOrder02Adapter() {
        try {
            mo2NAdapter=new MyOrder02NextAdapter();
            lvMyOrder02.setAdapter(mo2NAdapter);
            lvMyOrder02.setSelection(returnList.size());
        }catch (Exception e){
            showToast(someException());
        }
    }

    class MyOrder02NextAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return myOrders.size();
        }

        @Override
        public MyOrder02 getItem(int i) {
            return myOrders.get(i);
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
                nextHolder.tvEnterAmountTMD=(TextView)view.findViewById(R.id.tvEnterAmountTMD);
                nextHolder.tvAddDate=(TextView)view.findViewById(R.id.tvAddDate);
                nextHolder.tvReturnStatus=(TextView)view.findViewById(R.id.tvReturnStatus);
                nextHolder.llJuzhong=(LinearLayout) view.findViewById(R.id.llJuzhong);
                nextHolder.llBujuzhong=(LinearLayout) view.findViewById(R.id.llBujuzhong);
                view.setTag(nextHolder);
            }
            nextHolder= (MyOrder02NextHolder) view.getTag();
            MyOrder02 item = getItem(i);
            try {
                Glide.with(getApplicationContext()).load(item.getHeadPortrait()).into(nextHolder.ivHeadPortrait);
                nextHolder.tvNickName.setText(item.getNickName()+"");
                nextHolder.tvEnterAmount.setText("￥"+item.getEnterAmount());
                nextHolder.tvEnterAmountTMD.setText("￥"+item.getEnterAmount());
                nextHolder.tvAddDate.setText("订单时间："+item.getAddDate()+"");
                nextHolder.tvReturnStatus.setText(item.getReturnStatus()+"");
                if(item.getReturnStatus().equals("已返还")){
                    try {
                        nextHolder.llBujuzhong.setVisibility(View.VISIBLE);
                        nextHolder.llJuzhong.setVisibility(View.GONE);
                        nextHolder.tvEnterAmount.setTextColor(Color.rgb(153,153,153));
                    }catch (Exception e){
                        showToast(someException());
                    }
                }else{
                    try {
                        nextHolder.llBujuzhong.setVisibility(View.GONE);
                        nextHolder.llJuzhong.setVisibility(View.VISIBLE);
                        nextHolder.tvEnterAmount.setTextColor(Color.rgb(255,38,38));
                    }catch (Exception e){
                        showToast(someException());
                    }
                }
            }catch (Exception e){
                showToast(someException());
            }
            return view;
        }

        class MyOrder02NextHolder {
            ImageView ivHeadPortrait;
            TextView tvNickName,tvEnterAmount,tvAddDate,tvReturnStatus,tvEnterAmountTMD;
            LinearLayout llJuzhong,llBujuzhong;
        }
    }

    private void setActivityAdapter() {
        try {
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
        }catch (Exception e){
            showToast(someException());
        }
    }

    class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ActivityHolder>{
        @Override
        public ActivityHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ActivityHolder aHolder=new ActivityHolder(LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_myorder02_activity,null));
            return aHolder;
        }

        @Override
        public void onBindViewHolder(ActivityHolder holder, final int position) {
            try {
                holder.tvActivityName.setText(aList.get(position).getTitle()+"");
            }catch (Exception e){
                showToast(someException());
            }
            holder.tvActivityName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        checkPosition=position;
                        checkActivityID=aList.get(checkPosition).getShopActivityID();
                        aAdapter.notifyDataSetChanged();
                        loadMyOrder02Data();
                    }catch (Exception e){
                        showToast(someException());
                    }
                }
            });
            try {
                if (checkPosition==position){
                    holder.tvLine.setVisibility(View.VISIBLE);
                    holder.tvActivityName.setBackgroundResource(R.drawable.bg_shadowy);
                }else{
                    holder.tvLine.setVisibility(View.INVISIBLE);
                    holder.tvActivityName.setBackgroundResource(R.drawable.bg_shadow);
                }
                tvBottom.setText(Html.fromHtml("<font color=\"#0BD28A\">"+"\""+aList.get(checkPosition).getTitle()+"\""+"</font>"+"活动我的订单排行(共"+aList.get(checkPosition).getOrderCount()+"单)"));
            }catch (Exception e){
                showToast(someException());
            }
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
                loadBackData();
                break;
            default:
                break;
        }
    }

    private void loadBackData() {
        try {
            Call<MyOrderReturnInfo> call=RetrofitService.getInstance().getMyOrderReturnInfo(token,OrderID);
            call.enqueue(new Callback<MyOrderReturnInfo>() {
                @Override
                public void onResponse(Response<MyOrderReturnInfo> response, Retrofit retrofit) {
                    if (response==null){
                        showToast(responseFail());
                        return;
                    }
                    if (response.body().getStatus()==0){
                        try {
                            infoList=response.body().getList();
                            setBackAdapter();
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

    private void setBackAdapter() {
        backAdapter=new MyOrderBackAdapter();
        lvMyOrder02.setAdapter(backAdapter);
    }

    class MyOrderBackAdapter extends BaseAdapter {

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
            MyOrderBackHolder nextHolder = null;
            if (view == null) {
                view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_myorder02, null);
                nextHolder = new MyOrderBackHolder();
                nextHolder.ivHeadPortrait = (ImageView) view.findViewById(R.id.ivHeadPortrait);
                nextHolder.tvNickName = (TextView) view.findViewById(R.id.tvNickName);
                nextHolder.tvEnterAmount = (TextView) view.findViewById(R.id.tvEnterAmount);
                nextHolder.tvEnterAmountTMD = (TextView) view.findViewById(R.id.tvEnterAmountTMD);
                nextHolder.tvAddDate = (TextView) view.findViewById(R.id.tvAddDate);
                nextHolder.tvReturnStatus = (TextView) view.findViewById(R.id.tvReturnStatus);
                nextHolder.llJuzhong = (LinearLayout) view.findViewById(R.id.llJuzhong);
                nextHolder.llBujuzhong = (LinearLayout) view.findViewById(R.id.llBujuzhong);
                view.setTag(nextHolder);
            }
            nextHolder = (MyOrderBackHolder) view.getTag();
            MyOrderReturnInfo.MyOrderReturnInfoList item = getItem(i);
            try {
                Glide.with(getApplicationContext()).load(item.getHeadPortrait()).into(nextHolder.ivHeadPortrait);
                nextHolder.tvNickName.setText(item.getNickName() + "");
                nextHolder.tvEnterAmount.setText("￥" + item.getEnterAmount());
                nextHolder.tvEnterAmountTMD.setText("￥" + item.getEnterAmount());
                nextHolder.tvAddDate.setText("订单时间：" + item.getAddDate() + "");
                nextHolder.tvReturnStatus.setText(item.getReturnStatus() + "");
                if (item.getReturnStatus().equals("已返还")) {
                    try {
                        nextHolder.llBujuzhong.setVisibility(View.VISIBLE);
                        nextHolder.llJuzhong.setVisibility(View.GONE);
                        nextHolder.tvEnterAmount.setTextColor(Color.rgb(153, 153, 153));
                    }catch (Exception e){
                        showToast(someException());
                    }
                } else {
                    try {
                        nextHolder.llBujuzhong.setVisibility(View.GONE);
                        nextHolder.llJuzhong.setVisibility(View.VISIBLE);
                        nextHolder.tvEnterAmount.setTextColor(Color.rgb(255, 38, 38));
                    }catch (Exception e){
                        showToast(someException());
                    }
                }
            }catch (Exception e){
                showToast(someException());
            }
            return view;
        }

        class MyOrderBackHolder {
            ImageView ivHeadPortrait;
            TextView tvNickName, tvEnterAmount, tvAddDate, tvReturnStatus,tvEnterAmountTMD;
            LinearLayout llJuzhong, llBujuzhong;
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
