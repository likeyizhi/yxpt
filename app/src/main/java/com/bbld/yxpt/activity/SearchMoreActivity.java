package com.bbld.yxpt.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bbld.yxpt.R;
import com.bbld.yxpt.base.BaseActivity;
import com.bbld.yxpt.bean.HotSearchList;
import com.bbld.yxpt.bean.ShopListPage;
import com.bbld.yxpt.dbSearch.SearchDBBean;
import com.bbld.yxpt.dbSearch.UserDataBaseOperate;
import com.bbld.yxpt.dbSearch.UserSQLiteOpenHelper;
import com.bbld.yxpt.loadingdialog.WeiboDialogUtils;
import com.bbld.yxpt.network.RetrofitService;
import com.bumptech.glide.Glide;
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
    @BindView(R.id.tvInput)
    TextView tvInput;
    @BindView(R.id.llInput)
    LinearLayout llInput;

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
    private static final int IS_POINT=1;
    private static final int IS_NOT_POINT=2;


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
        try {
            searchDBs = mUserDataBaseOperate.findAll();
            dbAdapter = new DBAdapter();
            lvSearchHistory.setAdapter(dbAdapter);
        }catch (Exception e){
            showToast(someException());
        }
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
            DB01Holder dbHolder01=null;
            if (view==null){
                view=LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_search_db_01,null);
                dbHolder01=new DB01Holder();
                dbHolder01.tvPointName=(TextView)view.findViewById(R.id.tvPointName);
                dbHolder01.tvPointAddr=(TextView)view.findViewById(R.id.tvPointAddr);
                dbHolder01.ivType=(ImageView) view.findViewById(R.id.ivType);
                view.setTag(dbHolder01);
            }
            dbHolder01= (DB01Holder) view.getTag();
            final SearchDBBean bean = getItem(i);
            try {
                if (bean.getType()==IS_NOT_POINT){
                    dbHolder01.tvPointName.setText(bean.getName());
                    dbHolder01.tvPointAddr.setVisibility(View.GONE);
                    Glide.with(getApplicationContext()).load(R.mipmap.ss).into(dbHolder01.ivType);
                    if (view!=null){
                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                try {
                                    Intent intent=new Intent();
                                    intent.putExtra("key", bean.getName());
                                    intent.putExtra("isBack","back");
                                    intent.putExtra("isPoint","noPoint");
                                    intent.putExtra("pointX","");
                                    intent.putExtra("pointY","");
                                    setResult(6065,intent);
                                    finish();
                                }catch (Exception e){
                                    showToast(someException());
                                }
                            }
                        });
                    }
                }else{
                    dbHolder01.tvPointName.setText(bean.getName());
                    dbHolder01.tvPointAddr.setVisibility(View.VISIBLE);
                    dbHolder01.tvPointAddr.setText(bean.getAddr());
                    Glide.with(getApplicationContext()).load(R.mipmap.dw_s).into(dbHolder01.ivType);
                    if (view!=null){
                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                try {
                                    Intent intent=new Intent();
                                    intent.putExtra("key", bean.getName());
                                    intent.putExtra("isBack","back");
                                    intent.putExtra("isPoint","isPoint");
                                    intent.putExtra("pointX",bean.getPointX());
                                    intent.putExtra("pointY",bean.getPointY());
                                    setResult(6065,intent);
                                    finish();
                                }catch (Exception e){
                                    showToast(someException());
                                }
                            }
                        });
                    }
                }
            }catch (Exception e){
                showToast(someException());
            }
            return view;
        }

        class DB01Holder{
            TextView tvPointName,tvPointAddr;
            ImageView ivType;
        }
    }

    private void loadTagData() {
        try {
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
        }catch (Exception e){
            showToast(someException());
        }
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
            try {
                tagHolder.tvTag.setText(tag.getText());
                tagHolder.tvTag.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            List<SearchDBBean> thisShops = mUserDataBaseOperate.findUserByName(tag.getText());
                            if (thisShops.size()==0){
                                SearchDBBean searchDBBean=new SearchDBBean();
                                searchDBBean.setName(tag.getText());
                                searchDBBean.setAddr("");
                                searchDBBean.setType(IS_NOT_POINT);
                                searchDBBean.setPointX("");
                                searchDBBean.setPointY("");
                                mUserDataBaseOperate.insertToUser(searchDBBean);
                            }
                            Intent intent=new Intent();
                            intent.putExtra("key", tag.getText());
                            intent.putExtra("isPoint", "noPoint");
                            intent.putExtra("isBack","back");
                            intent.putExtra("pointX","");
                            intent.putExtra("pointY","");
                            setResult(6065,intent);
                            finish();
                        }catch (Exception e){
                            showToast(someException());
                        }
                    }
                });
            }catch (Exception e){
                showToast(someException());
            }
            return view;
        }

        class TagHolder {
            TextView tvTag;
        }
    }

    private void setListeners() {
        try {
            etSearch.addTextChangedListener(watcher);
        }catch (Exception e){
            showToast(someException());
        }
        tvClearHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    mUserDataBaseOperate.deleteAll();
                    setListFromDB();
                }catch (Exception e){
                    showToast(someException());
                }
            }
        });
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    ActivityManagerUtil.getInstance().finishActivity(SearchMoreActivity.this);
                }catch (Exception e){
                    showToast(someException());
                }
            }
        });
        llInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent=new Intent();
                    intent.putExtra("key",etSearch.getText().toString().trim());
                    intent.putExtra("isPoint", "noPoint");
                    intent.putExtra("isBack","back");
                    intent.putExtra("pointX","");
                    intent.putExtra("pointY","");
                    setResult(6065,intent);
                    List<SearchDBBean> thisShops = mUserDataBaseOperate.findUserByName(etSearch.getText().toString().trim());
                    if (thisShops.size()==0){
                        SearchDBBean searchDBBean=new SearchDBBean();
                        searchDBBean.setName(etSearch.getText().toString().trim());
                        searchDBBean.setAddr("");
                        searchDBBean.setType(IS_NOT_POINT);
                        searchDBBean.setPointX("");
                        searchDBBean.setPointY("");
                        mUserDataBaseOperate.insertToUser(searchDBBean);
                    }
                    finish();
                }catch (Exception e){
                    showToast(someException());
                }
            }
        });
        lvInputSearch.setOnScrollListener(new AbsListView.OnScrollListener() {
            private boolean isBottom;
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                try {
                    switch (i) {
                        case SCROLL_STATE_FLING:
                            //Log.i("info", "SCROLL_STATE_FLING");
                            break;
                        case SCROLL_STATE_IDLE:
                            try {
                                if (isBottom) {
                                    page++;
                                    setSearchData(etSearch.getText()+"",true);
                                }
                            }catch (Exception e){
                                showToast(someException());
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
            try {
                String key = etSearch.getText().toString().trim();
                if (key==null || key.equals("")){
                    llTSH.setVisibility(View.VISIBLE);
                    lvInputSearch.setVisibility(View.GONE);
                    llInput.setVisibility(View.GONE);
                }else{
                    page=1;
                    setSearchData(key,false);
                    llTSH.setVisibility(View.GONE);
                    llInput.setVisibility(View.VISIBLE);
                    tvInput.setText(key);
                }
            }catch (Exception e){
                showToast(someException());
            }
        }
    };

    private void setSearchData(final String key, final boolean isLoadMore) {
        try {
            Call<ShopListPage> call= RetrofitService.getInstance().getShopListPage(x+"",y+"",page,5,key);
            call.enqueue(new Callback<ShopListPage>() {
                @Override
                public void onResponse(Response<ShopListPage> response, Retrofit retrofit) {
                    if (response==null){
                        showToast(responseFail());
                        return;
                    }
                    if (response.body().getStatus()==0){
                        try {
                            if (isLoadMore){
                                if (response.body().getShopList().size()!=0){
                                    List<ShopListPage.ShopListPageShopList> shopListAdd = response.body().getShopList();
                                    shopList.addAll(shopListAdd);
                                    searchAdapter.notifyDataSetChanged();
                                }
                            }else{
                                int count = response.body().getCount();
                                shopList=response.body().getShopList();
                                if (count>0){
                                    setSearchAdapter();
                                }
                            }
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

    private void setSearchAdapter() {
        try {
            llTSH.setVisibility(View.GONE);
            lvInputSearch.setVisibility(View.VISIBLE);
            searchAdapter=new SearchAdapter();
            lvInputSearch.setAdapter(searchAdapter);
        }catch (Exception e){
            showToast(someException());
        }
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
            try {
                searchHolder.tvName.setText(shop.getShopName()+"");
                searchHolder.tvAddress.setText(shop.getAddress()+"");
                searchHolder.tvDistance.setText(shop.getDistance()+"");
                if (view!=null){
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            List<SearchDBBean> thisShops = mUserDataBaseOperate.findUserByName(shop.getShopName());
                            List<SearchDBBean> thisAddrs = mUserDataBaseOperate.findUserByAddr(shop.getAddress());
                            if (thisShops.size()==0 || thisAddrs.size()==0){
                                SearchDBBean searchDBBean=new SearchDBBean();
                                searchDBBean.setName(shop.getShopName());
                                searchDBBean.setAddr(shop.getAddress());
                                searchDBBean.setType(IS_POINT);
                                searchDBBean.setPointX(shop.getLatitude());
                                searchDBBean.setPointY(shop.getLongitude());
                                mUserDataBaseOperate.insertToUser(searchDBBean);
                            }
                            Intent intent=new Intent();
                            intent.putExtra("key",shop.getShopName());
                            intent.putExtra("isPoint", "isPoint");
                            intent.putExtra("isBack","back");
                            intent.putExtra("pointX",shop.getLatitude());
                            intent.putExtra("pointY",shop.getLongitude());
                            setResult(6065,intent);
                            finish();
                        }
                    });
                }
            }catch (Exception e){
                showToast(someException());
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
