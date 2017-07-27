package com.bbld.yxpt.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bbld.yxpt.R;
import com.bbld.yxpt.base.BaseActivity;
import com.bbld.yxpt.bean.HotSearchList;
import com.bbld.yxpt.bean.ShopList;
import com.bbld.yxpt.bean.ShopListPage;
import com.bbld.yxpt.dbSearch.SearchDBBean;
import com.bbld.yxpt.dbSearch.UserDataBaseOperate;
import com.bbld.yxpt.dbSearch.UserSQLiteOpenHelper;
import com.bbld.yxpt.loadingdialog.WeiboDialogUtils;
import com.bbld.yxpt.network.RetrofitService;
import com.wuxiaolong.androidutils.library.ActivityManagerUtil;

import java.util.List;

import butterknife.BindView;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * 搜索页(新)
 * Created by likey on 2017/7/10.
 */

public class SearchMoreActivity extends BaseActivity {
    @BindView(R.id.etSearch)
    EditText etSearch;
    @BindView(R.id.llTSH)
    LinearLayout llTSH;
    @BindView(R.id.lvInputSearch)
    ListView lvInputSearch;
    @BindView(R.id.gvTag)
    GridView gvTag;
    @BindView(R.id.lvSearchHistory)
    ListView lvSearchHistory;
    @BindView(R.id.tvClearHistory)
    TextView tvClearHistory;
    @BindView(R.id.ibBack)
    ImageButton ibBack;

    private double x;
    private double y;
    private int page=1;
    private int size=10;
    private List<ShopListPage.ShopListPageShopList> shopList;
    private SearchAdapter searchAdapter;
    private TagAdapter tagAdapter;
    private List<HotSearchList.HotSearchListlist> tags;
    private UserSQLiteOpenHelper mUserSQLiteOpenHelper;
    private UserDataBaseOperate mUserDataBaseOperate;
    private List<SearchDBBean> searchDBs;
    private DBAdapter dbAdapter;
    private Dialog mWeiboDialog;

    @Override
    protected void initViewsAndEvents() {
        mWeiboDialog = WeiboDialogUtils.createLoadingDialog(SearchMoreActivity.this, "处理中...");
        mUserSQLiteOpenHelper = UserSQLiteOpenHelper.getInstance(this);
        mUserDataBaseOperate = new UserDataBaseOperate(mUserSQLiteOpenHelper.getWritableDatabase());
        setListFromDB();
        loadTagData();
        setListeners();
    }

    private void setListFromDB() {
        searchDBs = mUserDataBaseOperate.findAll();
        dbAdapter = new DBAdapter();
        lvSearchHistory.setAdapter(dbAdapter);
    }

    class DBAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return searchDBs.size();
        }

        @Override
        public SearchDBBean getItem(int i) {
            return searchDBs.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            DBHolder dbHolder=null;
            if (view==null){
                view=LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_search_db,null);
                dbHolder=new DBHolder();
                dbHolder.tvDBName=(TextView)view.findViewById(R.id.tvDBName);
                view.setTag(dbHolder);
            }
            final SearchDBBean bean = getItem(i);
            dbHolder= (DBHolder) view.getTag();
            dbHolder.tvDBName.setText(bean.getName());
            if (view!=null){
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent();
                        intent.putExtra("key", bean.getName());
                        intent.putExtra("isBack","back");
                        setResult(6065,intent);
                        finish();
                    }
                });
            }
            return view;
        }

        class DBHolder{
            TextView tvDBName;
        }
    }

    private void loadTagData() {
        Call<HotSearchList> call=RetrofitService.getInstance().getHotSearchList();
        call.enqueue(new Callback<HotSearchList>() {
            @Override
            public void onResponse(Response<HotSearchList> response, Retrofit retrofit) {
                if (response==null){
                    showToast(responseFail());
                    return;
                }
                if (response.body().getStatus()==0){
                    tags=response.body().getList();
                    setTagAdapter();
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

    private void setTagAdapter() {
        tagAdapter=new TagAdapter();
        gvTag.setAdapter(tagAdapter);
    }

    class TagAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return tags.size();
        }

        @Override
        public HotSearchList.HotSearchListlist getItem(int i) {
            return tags.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            TagHolder tagHolder=null;
            if (view==null){
                view=LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_search_more_tag,null);
                tagHolder=new TagHolder();
                tagHolder.tvTag=(TextView)view.findViewById(R.id.tvTag);
                view.setTag(tagHolder);
            }
            final HotSearchList.HotSearchListlist tag = getItem(i);
            tagHolder= (TagHolder) view.getTag();
            tagHolder.tvTag.setText(tag.getText());
            tagHolder.tvTag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    List<SearchDBBean> thisShops = mUserDataBaseOperate.findUserByName(tag.getText());
                    if (thisShops.size()==0){
//                        showToast(tag.getText());
                        SearchDBBean searchDBBean=new SearchDBBean();
                        searchDBBean.setName(tag.getText());
                        mUserDataBaseOperate.insertToUser(searchDBBean);
                    }
                    Intent intent=new Intent();
                    intent.putExtra("key", tag.getText());
                    intent.putExtra("isBack","back");
                    setResult(6065,intent);
                    finish();
                }
            });
            return view;
        }

        class TagHolder {
            TextView tvTag;
        }
    }

    private void setListeners() {
        etSearch.addTextChangedListener(watcher);
        tvClearHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mUserDataBaseOperate.deleteAll();
                setListFromDB();
            }
        });
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityManagerUtil.getInstance().finishActivity(SearchMoreActivity.this);
            }
        });
    }

    private TextWatcher watcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub
        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
            String key = etSearch.getText().toString().trim();
            if (key==null || key.equals("")){
                llTSH.setVisibility(View.VISIBLE);
                lvInputSearch.setVisibility(View.GONE);
            }else{
                setSearchData(key);
            }
        }
    };

    private void setSearchData(final String key) {
        Call<ShopListPage> call= RetrofitService.getInstance().getShopListPage(x+"",y+"",page,size,key);
        call.enqueue(new Callback<ShopListPage>() {
            @Override
            public void onResponse(Response<ShopListPage> response, Retrofit retrofit) {
                if (response==null){
                    showToast(responseFail());
                    return;
                }
                if (response.body().getStatus()==0){
                    int count = response.body().getCount();
                    shopList=response.body().getShopList();
                    if (count>0){
                        setSearchAdapter();
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

    private void setSearchAdapter() {
        llTSH.setVisibility(View.GONE);
        lvInputSearch.setVisibility(View.VISIBLE);
        searchAdapter=new SearchAdapter();
        lvInputSearch.setAdapter(searchAdapter);
    }

    class SearchAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return shopList.size();
        }

        @Override
        public ShopListPage.ShopListPageShopList getItem(int i) {
            return shopList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            SearchHolder searchHolder=null;
            if (view==null){
                view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_search_more,null);
                searchHolder=new SearchHolder();
                searchHolder.tvName=(TextView)view.findViewById(R.id.tvName);
                searchHolder.tvAddress=(TextView)view.findViewById(R.id.tvAddress);
                searchHolder.tvDistance=(TextView)view.findViewById(R.id.tvDistance);
                view.setTag(searchHolder);
            }
            searchHolder= (SearchHolder) view.getTag();
            final ShopListPage.ShopListPageShopList shop = getItem(i);
            searchHolder.tvName.setText(shop.getShopName()+"");
            searchHolder.tvAddress.setText(shop.getAddress()+"");
            searchHolder.tvDistance.setText(shop.getDistance()+"");
            if (view!=null){
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        List<SearchDBBean> thisShops = mUserDataBaseOperate.findUserByName(shop.getShopName());
                        if (thisShops.size()==0){
                            SearchDBBean searchDBBean=new SearchDBBean();
                            searchDBBean.setName(shop.getShopName());
                            mUserDataBaseOperate.insertToUser(searchDBBean);
                        }
                        Intent intent=new Intent();
                        intent.putExtra("key",shop.getShopName());
                        intent.putExtra("isBack","back");
                        setResult(6065,intent);
                        finish();
                    }
                });
            }
            return view;
        }

        class SearchHolder{
            TextView tvName,tvAddress,tvDistance;
        }
    }

    @Override
    protected void getBundleExtras(Bundle extras) {
        x=extras.getDouble("x");
        y=extras.getDouble("y");
    }

    @Override
    public int getContentView() {
        return R.layout.activity_search_more;
    }
}
