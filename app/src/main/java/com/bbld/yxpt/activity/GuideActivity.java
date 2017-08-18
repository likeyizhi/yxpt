package com.bbld.yxpt.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.bbld.yxpt.R;
import com.bbld.yxpt.base.BaseActivity;
import com.bbld.yxpt.bean.HelpList;
import com.bbld.yxpt.network.RetrofitService;

import java.util.List;

import butterknife.BindView;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * 使用指南
 * Created by likey on 2017/7/3.
 */

public class GuideActivity extends BaseActivity{
    @BindView(R.id.lvHelp)
    ListView lvHelp;

    private int count;
    private List<HelpList.HelpListlist> list;
    private GuideAdapter adapter;

    @Override
    protected void initViewsAndEvents() {
        loadData();
    }

    private void loadData() {
        try {
            Call<HelpList> call= RetrofitService.getInstance().getHelpList();
            call.enqueue(new Callback<HelpList>() {
                @Override
                public void onResponse(Response<HelpList> response, Retrofit retrofit) {
                    if (response==null){
                        showToast(responseFail());
                        return;
                    }
                    if (response.body().getStatus()==0){
                        count=response.body().getCount();
                        list=response.body().getList();
                        setAdapter();
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

    private void setAdapter() {
        try {
            adapter=new GuideAdapter();
            lvHelp.setAdapter(adapter);
        }catch (Exception e){
            showToast(someException());
        }
    }

    class GuideAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public HelpList.HelpListlist getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            GuideHolder holder=null;
            if (view==null){
                view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_guide,null);
                holder=new GuideHolder();
                holder.tvGuide=(TextView)view.findViewById(R.id.tvGuide);
                view.setTag(holder);
            }
            holder= (GuideHolder) view.getTag();
            HelpList.HelpListlist item = getItem(i);
            try {
                holder.tvGuide.setText(item.getTitle());
            }catch (Exception e){
                showToast(someException());
            }
            return view;
        }

        class GuideHolder {
            TextView tvGuide;
        }
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    public int getContentView() {
        return R.layout.activity_guide;
    }
}
