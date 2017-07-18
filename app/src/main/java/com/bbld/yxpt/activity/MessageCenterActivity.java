package com.bbld.yxpt.activity;

import android.os.Bundle;
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
import com.bbld.yxpt.bean.MessageList;
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
 * 我的消息
 * Created by likey on 2017/7/3.
 */

public class MessageCenterActivity extends BaseActivity{
    @BindView(R.id.lvMessage)
    ListView lvMessage;
    @BindView(R.id.ivBack)
    ImageView ivBack;

    private String token;
    private int count;
    private List<MessageList.MessageListlist> list;
    private MessageAdapter adapter;


    @Override
    protected void initViewsAndEvents() {
        token=new MyToken(this).getToken();
        loadData();
        setListeners();
    }

    private void setListeners() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityManagerUtil.getInstance().finishActivity(MessageCenterActivity.this);
            }
        });
    }

    private void loadData() {
        Call<MessageList> call= RetrofitService.getInstance().getMessageList(token);
        call.enqueue(new Callback<MessageList>() {
            @Override
            public void onResponse(Response<MessageList> response, Retrofit retrofit) {
                if (response==null){
                    showToast(responseFail());
                    return;
                }
                if (response.body().getStatus()==0){
                    count=response.body().getCount();
                    list=response.body().getList();
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

    private void setData() {
        adapter=new MessageAdapter();
        lvMessage.setAdapter(adapter);
    }

    class MessageAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public MessageList.MessageListlist getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            MessageHolder holder=null;
            if (view==null){
                view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_lv_messagecenter,null);
                holder=new MessageHolder();
                holder.ivImg=(ImageView)view.findViewById(R.id.ivImg);
                holder.tvMsg=(TextView)view.findViewById(R.id.tvMsg);
                holder.tvContent=(TextView)view.findViewById(R.id.tvContent);
                holder.tvDate=(TextView)view.findViewById(R.id.tvDate);
                view.setTag(holder);
            }
            holder= (MessageHolder) view.getTag();
            MessageList.MessageListlist item=getItem(i);
            holder.tvMsg.setText(item.getTitle()+"");
            holder.tvContent.setText(item.getContent()+"");
            holder.tvDate.setText(item.getTime());
            return view;
        }

        class MessageHolder{
            ImageView ivImg;
            TextView tvMsg,tvContent,tvDate;
        }
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    public int getContentView() {
        return R.layout.activity_messagecenter;
    }
}
