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
import com.bbld.yxpt.bean.Login;
import com.bbld.yxpt.bean.UserInfo;
import com.bbld.yxpt.bean.UserOrderList;
import com.bbld.yxpt.loadingdialog.WeiboDialogUtils;
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
//        token=new MyToken(PersonalNewActivity.this).getToken();
//        if (token==null || token.equals("")){
//            WeiboDialogUtils.closeDialog(mWeiboDialog);
//        }
        //读取帐号密码
        SharedPreferences sharedGetAP=getSharedPreferences("YXAP",MODE_PRIVATE);
        sacc = sharedGetAP.getString("YXACC", "");
        spwd = sharedGetAP.getString("YXPWD", "");
        if (sacc.equals("")||spwd.equals("")){
        }else{
            loadToken();
        }
        loadData();
        loadBottomData(false);
        setListeners();
    }

    private void loadBottomData(final boolean isLoadMore) {
        if (new MyToken(PersonalNewActivity.this).getToken()==null || new MyToken(PersonalNewActivity.this).getToken().equals("")){
            //未登录状态
            lvOrder.setVisibility(View.GONE);
            WeiboDialogUtils.closeDialog(mWeiboDialog);
        }else{
            lvOrder.setVisibility(View.VISIBLE);
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
                        count=response.body().getCount();
                        total=response.body().getTotal();
                        if (isLoadMore){
                            List<UserOrderList.UserOrderListlist> ordersAdd = response.body().getList();
                            orders.addAll(ordersAdd);
                            orderAdapter.notifyDataSetChanged();
                        }else{
                            orders = response.body().getList();
                            setBottonAdapter();
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
        }
    }

    private void setBottonAdapter() {
        orderAdapter=new OrderAdapter();
        lvOrder.setAdapter(orderAdapter);
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
            orderHolder.tvShopName.setText(order.getActivityTitle()+"");
            orderHolder.tvDate.setText("消费时间："+order.getAddDate()+"");
            orderHolder.tvEnterAmount.setText("￥"+order.getEnterAmount()+"");
            if (order.getReturnStatus().equals("已返还")){
                orderHolder.ivReturnStatus.setVisibility(View.VISIBLE);
                orderHolder.tvEnterAmount.setTextColor(Color.rgb(188,188,188));
            }else{
                orderHolder.ivReturnStatus.setVisibility(View.GONE);
                orderHolder.tvEnterAmount.setTextColor(Color.rgb(11,210,138));
            }
            Glide.with(getApplicationContext()).load(order.getHeadPortrait()).into(orderHolder.ivHead);
            return view;
        }

        class OrderHolder{
            TextView tvShopName,tvDate,tvEnterAmount;
            ImageView ivReturnStatus,ivHead;
        }
    }

    private void setListeners() {
        ivShopImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (new MyToken(PersonalNewActivity.this).getToken()==null){
                    readyGo(LoginActivity.class);
                }else{
                    readyGo(PersonalDataActivity.class);
                }
            }
        });
        tvShopName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (new MyToken(PersonalNewActivity.this).getToken()==null){
                    readyGo(LoginActivity.class);
                }else{
                    readyGo(PersonalDataActivity.class);
                }
            }
        });
        tvTiXian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (new MyToken(PersonalNewActivity.this).getToken()==null || new MyToken(PersonalNewActivity.this).getToken().equals("")){
                    showToast("暂未登录");
                    readyGo(LoginActivity.class);
                }else{
                    readyGo(WithdrawscashActivity.class);
                }
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                srl.setRefreshing(false);
                ActivityManagerUtil.getInstance().finishActivity(PersonalNewActivity.this);
            }
        });
        ivRing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (new MyToken(PersonalNewActivity.this).getToken()==null || new MyToken(PersonalNewActivity.this).getToken().equals("")){
                    showToast("暂未登录");
                    readyGo(LoginActivity.class);
                }else{
                    readyGo(MessageCenterActivity.class);
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
        llPerson01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (new MyToken(PersonalNewActivity.this).getToken()==null || new MyToken(PersonalNewActivity.this).getToken().equals("")){
                    showToast("暂未登录");
                }else{
                    readyGo(PayMoneyActivity.class);
                }
            }
        });
        llPerson02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (new MyToken(PersonalNewActivity.this).getToken()==null || new MyToken(PersonalNewActivity.this).getToken().equals("")){
                    showToast("暂未登录");
                }else{
                    readyGo(ReceiveMoneyActivity.class);
                }
            }
        });
        llPerson03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (new MyToken(PersonalNewActivity.this).getToken()==null || new MyToken(PersonalNewActivity.this).getToken().equals("")){
                    showToast("暂未登录");
                }else{
                    readyGo(HaveToMoneyActivity.class);
                }
            }
        });
    }

    private void loadToken() {
        Call<Login> call= RetrofitService.getInstance().login(sacc, spwd);
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
        mWeiboDialog = WeiboDialogUtils.createLoadingDialog(PersonalNewActivity.this, "加载中...");
        if (new MyToken(PersonalNewActivity.this).getToken()==null || new MyToken(PersonalNewActivity.this).getToken().equals("")){
            showToast("暂未登录");
            Glide.with(getApplicationContext()).load(R.mipmap.head).into(ivShopImg);
            tvShopName.setText("您还未登录");
            tvXF.setText("0.00");
            tvJL.setText("￥0.00");
            tvMyOrder.setText("0");
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
    }

    private void setData() {
        Glide.with(getApplicationContext()).load(userInfo.getHeadPortrait()).error(R.mipmap.head).into(ivShopImg);
        tvShopName.setText(userInfo.getMobile()+"");
        tvXF.setText(""+userInfo.getTotialSale());
        tvJL.setText(""+userInfo.getReturnTotialSale());
        tvMyOrder.setText(userInfo.getRewardOrderCount()+"");
        tvUseCount.setText("已有"+userInfo.getPlatformUserCount()+"人使用利惠客购物平台");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //读取帐号密码
        SharedPreferences sharedGetAP=getSharedPreferences("YXAP",MODE_PRIVATE);
        sacc = sharedGetAP.getString("YXACC", "");
        spwd = sharedGetAP.getString("YXPWD", "");
        if (sacc.equals("")||spwd.equals("")){
        }else{
            loadToken();
        }
        loadData();
        pageIndex=1;
        loadBottomData(false);
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    public int getContentView() {
        return R.layout.activity_personal_new;
    }
}
