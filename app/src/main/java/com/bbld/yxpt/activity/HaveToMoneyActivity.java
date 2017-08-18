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
import com.bbld.yxpt.bean.UserReturnOrderList;
import com.bbld.yxpt.bean.WithdrawalList;
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
 * 获取用户已到帐订单列表
 * Created by likey on 2017/7/3.
 */

public class HaveToMoneyActivity extends BaseActivity{
    @BindView(R.id.tvMoney)
    TextView tvMoney;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;
    @BindView(R.id.lvOrder)
    ListView lvOrder;
    @BindView(R.id.ivBack)
    ImageView ivBack;

    private String token;
    private int pageIndex=1;
    private int pageSize=10;
    private int count;
    private String total;
    private List<WithdrawalList.WithdrawalListlist> list;
    private HaveToAdapter haveToAdapter;
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
        try {
            token=new MyToken(this).getToken();
        }catch (Exception e){
            showToast(someException());
        }
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
                            Thread.sleep(1000);
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
                                loadData(true);
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
                    ActivityManagerUtil.getInstance().finishActivity(HaveToMoneyActivity.this);
                }catch (Exception e){
                    showToast(someException());
                }
            }
        });
    }

    private void loadData(final boolean isLoadMore) {
        try {
            Call<WithdrawalList> call= RetrofitService.getInstance().getWithdrawalList(token, pageIndex);
            call.enqueue(new Callback<WithdrawalList>() {
                @Override
                public void onResponse(Response<WithdrawalList> response, Retrofit retrofit) {
                    if (response==null){
                        showToast(responseFail());
                        return;
                    }
                    if (response.body().getStatus()==0){
                        total=response.body().getTotal();
                        if (isLoadMore){
                            List<WithdrawalList.WithdrawalListlist> listAdd = response.body().getList();
                            list.addAll(listAdd);
                            haveToAdapter.notifyDataSetChanged();
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
        }catch (Exception e){
            showToast(someException());
        }
    }

    private void setData() {
        try {
            tvMoney.setText(total);
            setHaveToAdapter();
        }catch (Exception e){
            showToast(someException());
        }
    }

    private void setHaveToAdapter() {
        try {
            haveToAdapter=new HaveToAdapter();
            lvOrder.setAdapter(haveToAdapter);
        }catch (Exception e){
            showToast(someException());
        }
    }

    class HaveToAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public WithdrawalList.WithdrawalListlist getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            HaveToHolder holder=null;
            if (view==null){
                view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_lv_3money,null);
                holder=new HaveToHolder();
                holder.tvActivityTitle=(TextView)view.findViewById(R.id.tvActivityTitle);
                holder.tvAddDate=(TextView) view.findViewById(R.id.tvAddDate);
                holder.tvEnterAmount=(TextView)view.findViewById(R.id.tvEnterAmount);
                holder.ivHead=(ImageView) view.findViewById(R.id.ivHead);
                view.setTag(holder);
            }
            WithdrawalList.WithdrawalListlist item = getItem(i);
            holder= (HaveToHolder) view.getTag();
            try {
                holder.tvActivityTitle.setText("尾号"+item.getCardNo().substring(item.getCardNo().length()-4,item.getCardNo().length())+"("+item.getBankName()+")");
                holder.tvAddDate.setText("提现时间："+item.getAddDate()+"");
                holder.tvEnterAmount.setText("￥"+item.getWithdrawalMoney());
                Glide.with(getApplicationContext()).load(item.getBankLogo()).into(holder.ivHead);
            }catch (Exception e){
                showToast(someException());
            }
            return view;
        }

        class HaveToHolder{
            TextView tvActivityTitle,tvAddDate,tvEnterAmount;
            ImageView ivHead;
        }
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    public int getContentView() {
        return R.layout.activity_havetomoney;
    }
}
