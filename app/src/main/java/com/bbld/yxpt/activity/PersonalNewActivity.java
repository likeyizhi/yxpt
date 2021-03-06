package com.bbld.yxpt.activity;

import android.app.Dialog;
import android.content.SharedPreferences;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bbld.yxpt.R;
import com.bbld.yxpt.base.BaseActivity;
import com.bbld.yxpt.bean.GetMessageCount;
import com.bbld.yxpt.bean.Login;
import com.bbld.yxpt.bean.SetMessageRead;
import com.bbld.yxpt.bean.UserInfo;
import com.bbld.yxpt.bean.UserOrderList;
import com.bbld.yxpt.loadingdialog.WeiboDialogUtils;
import com.bbld.yxpt.network.RetrofitService;
import com.bbld.yxpt.utils.MyToken;
import com.bumptech.glide.Glide;
import com.wuxiaolong.androidutils.library.ActivityManagerUtil;

import java.util.List;

import butterknife.BindView;
import cn.jpush.android.api.JPushInterface;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * 个人中心(新)
 * Created by likey on 2017/7/10.
 */

public class PersonalNewActivity extends BaseActivity{
    @BindView(R.id.ivShopImg)
    ImageView ivShopImg;
    @BindView(R.id.ivRing)
    ImageView ivRing;
    @BindView(R.id.tvShopName)
    TextView tvShopName;
    @BindView(R.id.tvTiXian)
    TextView tvTiXian;
    @BindView(R.id.tvXF)
    TextView tvXF;
    @BindView(R.id.tvJL)
    TextView tvJL;
    @BindView(R.id.tvMyOrder)
    TextView tvMyOrder;
    @BindView(R.id.tvUseCount)
    TextView tvUseCount;
    @BindView(R.id.lvOrder)
    ListView lvOrder;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;
    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.llPerson01)
    LinearLayout llPerson01;
    @BindView(R.id.llPerson02)
    LinearLayout llPerson02;
    @BindView(R.id.llPerson03)
    LinearLayout llPerson03;
    @BindView(R.id.ivKong)
    ImageView ivKong;


    private String sacc;
    private String spwd;
    private static final String TOKEN=null;
    private UserInfo.UserInfoUserInfo userInfo;
    private int pageIndex=1;
    private int count;
    private String total;
    private List<UserOrderList.UserOrderListlist> orders;
    private OrderAdapter orderAdapter;
    private Dialog mWeiboDialog;
    private String token;
    public static PersonalNewActivity personalNewActivity=null;
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    try {
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
        personalNewActivity=this;
        loadData();
        loadBottomData(false);
        loadMessage();
        setListeners();
    }
    private  void loadMessage(){
        try {
            Call<GetMessageCount> call= RetrofitService.getInstance().getMessageCount(new MyToken(PersonalNewActivity.this).getToken()+"");
            call.enqueue(new Callback<GetMessageCount>() {
                @Override
                public void onResponse(Response<GetMessageCount> response, Retrofit retrofit) {
                    if (response==null){
                        showToast("获取数据失败");
                        return;
                    }
                    if (response.body().getStatus()==0){
                        if(response.body().getCount()==0){
                            ivRing.setImageResource(R.mipmap.lingdg);
                        }else{
                            ivRing.setImageResource(R.mipmap.lingdgdian);
                        }
                    }else{

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

    private void loadBottomData(final boolean isLoadMore) {
        if (new MyToken(PersonalNewActivity.this).getToken()==null || new MyToken(PersonalNewActivity.this).getToken().equals("")){
            try {
                //未登录状态
                lvOrder.setVisibility(View.GONE);
                ivKong.setVisibility(View.VISIBLE);
                WeiboDialogUtils.closeDialog(mWeiboDialog);
            }catch (Exception e){
                showToast(someException());
            }
        }else{
            try {
                lvOrder.setVisibility(View.VISIBLE);
                ivKong.setVisibility(View.GONE);
                Call<UserOrderList> call= RetrofitService.getInstance().getUserOrderList(new MyToken(PersonalNewActivity.this).getToken(),pageIndex);
                call.enqueue(new Callback<UserOrderList>() {
                    @Override
                    public void onResponse(Response<UserOrderList> response, Retrofit retrofit) {
                        if (response==null){
                            showToast(responseFail());
                            return;
                        }
                        if (response.body().getStatus()==0){
//                    showToast(token+","+pageIndex+","+pageSize+","+response.body().getMes());
                            try {
                                count=response.body().getCount();
                                total=response.body().getTotal();
                                if (isLoadMore){
                                    List<UserOrderList.UserOrderListlist> ordersAdd = response.body().getList();
                                    orders.addAll(ordersAdd);
                                    orderAdapter.notifyDataSetChanged();
                                }else{
                                    orders = response.body().getList();
                                    if (orders.size()==0){
                                        ivKong.setVisibility(View.VISIBLE);
                                    }
                                    setBottonAdapter();
                                }
                            }catch (Exception e){
                                showToast(someException());
                            }
                        }else{
                            showToast(response.body().getMes());
                        }
                        WeiboDialogUtils.closeDialog(mWeiboDialog);
                    }
                    @Override
                    public void onFailure(Throwable throwable) {

                    }
                });
            }catch (Exception e){
                showToast(someException());
            }
        }
    }

    private void setBottonAdapter() {
        try{
            orderAdapter=new OrderAdapter();
            lvOrder.setAdapter(orderAdapter);
        }catch (Exception e){
            showToast(someException());
        }
    }

    class OrderAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return orders.size();
        }

        @Override
        public UserOrderList.UserOrderListlist getItem(int i) {
            return orders.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            OrderHolder orderHolder=null;
            if (view==null){
                view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_personal_new,null);
                orderHolder=new OrderHolder();
                orderHolder.tvShopName=(TextView) view.findViewById(R.id.tvShopName);
                orderHolder.tvDate=(TextView) view.findViewById(R.id.tvDate);
                orderHolder.tvEnterAmount=(TextView) view.findViewById(R.id.tvEnterAmount);
                orderHolder.ivReturnStatus=(ImageView) view.findViewById(R.id.ivReturnStatus);
                orderHolder.ivHead=(ImageView) view.findViewById(R.id.ivHead);
                view.setTag(orderHolder);
            }
            UserOrderList.UserOrderListlist order = getItem(i);
            orderHolder= (OrderHolder) view.getTag();
            try{
                orderHolder.tvShopName.setText(order.getShopName()+"");
                orderHolder.tvDate.setText("消费时间："+order.getAddDate()+"");
                orderHolder.tvEnterAmount.setText("￥"+order.getEnterAmount()+"");
                if (order.getReturnStatus().equals("已返还")){
                    orderHolder.ivReturnStatus.setVisibility(View.VISIBLE);
                    orderHolder.tvEnterAmount.setTextColor(Color.rgb(188,188,188));
                }else{
                    orderHolder.ivReturnStatus.setVisibility(View.GONE);
                    orderHolder.tvEnterAmount.setTextColor(Color.rgb(11,210,138));
                }
                Glide.with(getApplicationContext()).load(order.getShopImg()).into(orderHolder.ivHead);
            }catch (Exception e){
                showToast(someException());
            }
            return view;
        }

        class OrderHolder{
            TextView tvShopName,tvDate,tvEnterAmount;
            ImageView ivReturnStatus,ivHead;
        }
    }

    private void setListeners(){
        ivShopImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (new MyToken(PersonalNewActivity.this).getToken()==null){
                        readyGo(LoginActivity.class);
                    }else{
                        readyGo(PersonalDataActivity.class);
                    }
                }catch (Exception e){
                    showToast(someException());
                }
            }
        });
        tvShopName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (new MyToken(PersonalNewActivity.this).getToken()==null){
                        readyGo(LoginActivity.class);
                    }else{
                        readyGo(PersonalDataActivity.class);
                    }
                }catch (Exception e){
                    showToast(someException());
                }
            }
        });
        tvTiXian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (new MyToken(PersonalNewActivity.this).getToken()==null || new MyToken(PersonalNewActivity.this).getToken().equals("")){
                        readyGo(LoginActivity.class);
                    }else{
                        readyGo(WithdrawscashActivity.class);
                    }
                }catch (Exception e){
                    showToast(someException());
                }
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    srl.setRefreshing(false);
                    ActivityManagerUtil.getInstance().finishActivity(PersonalNewActivity.this);
                }catch (Exception e){
                    showToast(someException());
                }
            }
        });
        ivRing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (new MyToken(PersonalNewActivity.this).getToken()==null || new MyToken(PersonalNewActivity.this).getToken().equals("")){
                        readyGo(LoginActivity.class);
                    }else{
                        setMessageRead();
                        readyGo(MessageCenterActivity.class);
                    }
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
                            pageIndex=1;
                            loadBottomData(false);
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
                            if (isBottom) {
                                pageIndex++;
                                loadBottomData(true);
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
        //消费金额
        llPerson01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (new MyToken(PersonalNewActivity.this).getToken()==null || new MyToken(PersonalNewActivity.this).getToken().equals("")){
                        readyGo(LoginActivity.class);
                    }else{
                        readyGo(PayMoneyActivity.class);
                    }
                }catch (Exception e){
                    showToast(someException());
                }
            }
        });
        //已返现金额
        llPerson02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (new MyToken(PersonalNewActivity.this).getToken()==null || new MyToken(PersonalNewActivity.this).getToken().equals("")){
                        readyGo(LoginActivity.class);
                    }else{
                        readyGo(ReceiveMoneyActivity.class);
                    }
                }catch (Exception e){
                    showToast(someException());
                }
            }
        });
        //已提现金额
        llPerson03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (new MyToken(PersonalNewActivity.this).getToken()==null || new MyToken(PersonalNewActivity.this).getToken().equals("")){
                        readyGo(LoginActivity.class);
                    }else{
                        readyGo(HaveToMoneyActivity.class);
                    }
                }catch (Exception e){
                    showToast(someException());
                }
            }
        });
    }
    private  void  setMessageRead(){
        try {
            Call<SetMessageRead> call= RetrofitService.getInstance().setMessageRead(new MyToken(PersonalNewActivity.this).getToken()+"");
            call.enqueue(new Callback<SetMessageRead>() {
                @Override
                public void onResponse(Response<SetMessageRead> response, Retrofit retrofit) {
                    if (response==null){
                        showToast("获取数据失败");
                        return;
                    }
                    if (response.body().getStatus()==0){

                    }else{

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

    private void loadToken() {
        String rid = JPushInterface.getRegistrationID(getApplicationContext());
        Call<Login> call= RetrofitService.getInstance().login(sacc, spwd,"android", rid);
        call.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Response<Login> response, Retrofit retrofit) {
                //保存Token
                SharedPreferences shared=getSharedPreferences("YXToken",MODE_PRIVATE);
                SharedPreferences.Editor editor=shared.edit();
                editor.putString(TOKEN,response.body().getToken());
                editor.commit();
            }
            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    private void loadData() {
        try {
            mWeiboDialog = WeiboDialogUtils.createLoadingDialog(PersonalNewActivity.this, "加载中...");
            Glide.with(getApplicationContext()).load(new MyToken(PersonalNewActivity.this).getSPHeadPortrait()).error(R.mipmap.head).into(ivShopImg);
            if (new MyToken(PersonalNewActivity.this).getToken()==null || new MyToken(PersonalNewActivity.this).getToken().equals("")){
                Glide.with(getApplicationContext()).load(R.mipmap.head).into(ivShopImg);
                tvShopName.setText("您还未登录");
                tvXF.setText("￥0.00");
                tvJL.setText("￥0.00");
                tvMyOrder.setText("￥0.00");
                tvUseCount.setText("");
            }else{
                Call<UserInfo> call=RetrofitService.getInstance().getUserInfo(new MyToken(PersonalNewActivity.this).getToken());
                call.enqueue(new Callback<UserInfo>() {
                    @Override
                    public void onResponse(Response<UserInfo> response, Retrofit retrofit) {
                        if (response==null){
                            showToast(responseFail());
                            return;
                        }
                        if (response.body().getStatus()==0){
                            userInfo=response.body().getUserInfo();
                            try {
                                setData();
                            }catch (Exception e){
                                showToast(getString(R.string.some_exception));
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
        }catch (Exception e){
            showToast(someException());
        }
    }

    private void setData() {
        try {
            Glide.with(getApplicationContext()).load(userInfo.getHeadPortrait()).error(R.mipmap.head).into(ivShopImg);
            tvShopName.setText(userInfo.getMobile()+"");
            tvXF.setText("￥"+userInfo.getTotialSale());
            tvJL.setText("￥"+userInfo.getRewardTotial());
            tvMyOrder.setText("￥"+userInfo.getWithdrawalPrice());
            tvUseCount.setText("已有"+userInfo.getPlatformUserCount()+"人使用");
        }catch (Exception e){
            showToast(someException());
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadData();
        pageIndex=1;
        loadBottomData(false);
        loadMessage();
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    public int getContentView() {
        return R.layout.activity_personal_new;
    }
}
