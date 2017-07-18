package com.bbld.yxpt.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bbld.yxpt.R;
import com.bbld.yxpt.base.BaseActivity;
import com.bbld.yxpt.bean.GetBankCardList;
import com.bbld.yxpt.network.RetrofitService;
import com.bbld.yxpt.utils.MyToken;
import com.bbld.yxpt.widget.CircleImageView;
import com.bumptech.glide.Glide;
import com.wuxiaolong.androidutils.library.ActivityManagerUtil;

import java.util.List;

import butterknife.BindView;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * 银行卡
 * Created by dell on 2017/7/7.
 */

public class BankCardActivity extends BaseActivity {
    @BindView(R.id.lv_list)
    ListView lvList;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.srl_member)
    SwipeRefreshLayout srlMember;
    @BindView(R.id.iv_add)
    ImageView ivAdd;
    private List<GetBankCardList.GetBankCardListRes> bankcardlist;
    private BankCardListAdapter adapter;
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    srlMember.setRefreshing(false);
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    protected void initViewsAndEvents() {
        loadData(false);
        setListeners();
    }
    private void setListeners() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityManagerUtil.getInstance().finishActivity(BankCardActivity.this);
            }
        });
        srlMember.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        loadData(false);
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
        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readyGo(AddBankCardActivity.class);
            }
        });
    }
    private void loadData(final boolean isLoadMore) {
        Call<GetBankCardList> call= RetrofitService.getInstance().getBankCardList(new MyToken(BankCardActivity.this).getToken());
        call.enqueue(new Callback<GetBankCardList>() {
            @Override
            public void onResponse(Response<GetBankCardList> response, Retrofit retrofit) {
                if (response==null){
                    return;
                }
                if (response.body().getStatus()==0){
                    if (isLoadMore){
                        List<GetBankCardList.GetBankCardListRes> productsAdd = response.body().getList();
                        bankcardlist.addAll(productsAdd);
                        adapter.notifyDataSetChanged();
                    }else{
                        bankcardlist = response.body().getList();
                        if( bankcardlist.isEmpty()){
                            setAdapter();
                        }else{
                            setAdapter();
                        }
                    }
                }else{
                    showToast(response.body().getMes()+"");
                }
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    private void setAdapter() {
        adapter=new BankCardListAdapter();
        lvList.setAdapter(adapter);
    }

    class BankCardListAdapter extends BaseAdapter {
        BankCardListHolder holder=null;
        @Override
        public int getCount() {
            return bankcardlist.size();
        }

        @Override
        public GetBankCardList.GetBankCardListRes getItem(int i) {
            return bankcardlist.get(i);
        }

        @Override
        public long getItemId(int i) {
            return Long.parseLong(bankcardlist.get(i).getBankCardID()+"");
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view==null){
                view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_lv_bankcard,null);
                holder=new BankCardListHolder();
                holder.tvCardNo=(TextView)view.findViewById(R.id.tv_CardNo);
                holder.tvBankName=(TextView)view.findViewById(R.id.tv_BankName);
                holder.ivBankLogo=(CircleImageView)view.findViewById(R.id.iv_BankLogo);
                view.setTag(holder);
            }
            holder= (BankCardListHolder) view.getTag();
            final GetBankCardList.GetBankCardListRes list = getItem(i);
            holder.tvCardNo.setText(list.getCardNo()+"");
            holder.tvBankName.setText(list.getBankName()+"");
            Glide.with(getApplicationContext()).load(list.getBankLogo()).error(R.drawable.zwt).into(holder.ivBankLogo);
            if (view!=null){
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent();
                        intent.putExtra("bankName", list.getBankName()+"");
                        intent.putExtra("bankNo", list.getCardNo()+"");
                        intent.putExtra("bankLogo", list.getBankLogo()+"");
                        intent.putExtra("bankCardId", list.getBankCardID());
                        setResult(7788,intent);
                        finish();
                    }
                });
            }
            return view;
        }
        class  BankCardListHolder{
            TextView tvCardNo;
            CircleImageView ivBankLogo;
            TextView tvBankName;

        }
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        loadData(false);
    }
    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    public int getContentView() {
        return R.layout.activity_bankcard;
    }
}
