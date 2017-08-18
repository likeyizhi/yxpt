package com.bbld.yxpt.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.bbld.yxpt.R;
import com.bbld.yxpt.adapter.LoactionCityAdapter;
import com.bbld.yxpt.base.BaseActivity;
import com.bbld.yxpt.bean.Address;
import com.bbld.yxpt.bean.CityList;
import com.bbld.yxpt.network.RetrofitService;
import com.bbld.yxpt.utils.CharacterParser;
import com.bbld.yxpt.utils.PinyinComparator;
import com.bbld.yxpt.widget.SideBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;


/**
 * 地址搜索
 * Created by likey on 2017/7/5.
 */

public class SearchLocationActivity extends BaseActivity {
    @BindView(R.id.et_search_location)
    EditText etSearchLocation;
    @BindView(R.id.lvChangeCity)
    ListView lvChangeCity;
    @BindView(R.id.lvSearchLocation)
    ListView lvSearchLocation;
    @BindView(R.id.tv_city)
    TextView tv_city;
    @BindView(R.id.sidrbar)
    SideBar sidrbar;
    @BindView(R.id.dialog)
    TextView dialog;
    @BindView(R.id.rlCity)
    RelativeLayout rlCity;
    @BindView(R.id.ll_city)
    LinearLayout ll_city;


    private PoiSearch poiSearch;
    private PoiCitySearchOption poiCitySearchOption;
    private List<CityList.CityListlist> cityList;
    private ArrayList<CityList.CityListlist.CityListCityList> brands;
    private LoactionCityAdapter locationAdapter;
    private CharacterParser characterParser;
    private PinyinComparator pinyinComparator;
    private String city;

    @Override
    protected void initViewsAndEvents() {
        poiSearch= PoiSearch.newInstance();
        poiSearch.setOnGetPoiSearchResultListener(onGetPoiSearchResultListener);
        // 实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
        sidrbar.setTextView(dialog);
        tv_city.setText(city);
        loadCity();
        setListeners();
    }

    private void loadCity() {
        try {
            Call<CityList> call= RetrofitService.getInstance().getCityList();
            call.enqueue(new Callback<CityList>() {
                @Override
                public void onResponse(Response<CityList> response, Retrofit retrofit) {
                    if (response==null){
                        showToast(responseFail());
                        return;
                    }
                    if (response.body().getStatus()==0){
                        try {
                            cityList=response.body().getList();
                            brands=new ArrayList<CityList.CityListlist.CityListCityList>();
                            for (int i=0;i<cityList.size();i++){
                                List<CityList.CityListlist.CityListCityList> citise = cityList.get(i).getCityList();
                                for (int j=0;j<citise.size();j++){
                                    CityList.CityListlist.CityListCityList brand = cityList.get(i).getCityList().get(j);
                                    brands.add(brand);
                                }
                            }
                            setLocationAdapter(brands);
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
    private void setLocationAdapter(ArrayList<CityList.CityListlist.CityListCityList> brands) {
        locationAdapter=new LoactionCityAdapter(SearchLocationActivity.this, brands);
        lvChangeCity.setAdapter(locationAdapter);

        locationAdapter.setOnItemClickSetChangeCity(new LoactionCityAdapter.OnItemClickSetChangeCity() {
            @Override
            public void OnItemClickSetChangeCity(int id, String name, String x, String y) {
                try {
                    tv_city.setText(name+"");
                    city=name;
                    rlCity.setVisibility(View.GONE);
                    lvSearchLocation.setVisibility(View.VISIBLE);
                }catch (Exception e){
                    showToast(someException());
                }
            }
        });
    }
    private void setListeners() {
        try {
            etSearchLocation.addTextChangedListener(watcher);
        }catch (Exception e){
            showToast(someException());
        }
        ll_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (rlCity.getVisibility()==View.VISIBLE){
                        rlCity.setVisibility(View.GONE);
                        lvSearchLocation.setVisibility(View.VISIBLE);
                    }else{
                        rlCity.setVisibility(View.VISIBLE);
                        lvSearchLocation.setVisibility(View.GONE);
                    }
                }catch (Exception e){
                    showToast(someException());
                }

            }
        });
        sidrbar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                try {
                    // 该字母首次出现的位置
                    int position = locationAdapter.getPositionForSection(s.charAt(0));
                    if (position != -1) {
                        lvChangeCity.setSelection(position);
                    }
                }catch (Exception e){
                    showToast(someException());
                }
            }
        });
    }
    OnGetPoiSearchResultListener onGetPoiSearchResultListener=new OnGetPoiSearchResultListener(){
        @Override
        public void onGetPoiResult(PoiResult poiResult){
            try {
                if(poiResult.getAllPoi()!=null){
                    try {
                        showToast(poiResult.getAllPoi().get(0).address+","+poiResult.getAllPoi().get(0).location+","
                                +poiResult.getAllPoi().get(0).name);
                        List<PoiInfo> searchResult = poiResult.getAllPoi();
                        setSearchAdapter(searchResult);
                    }catch (Exception e){
                        showToast(someException());
                    }
                }
            }catch (Exception e){
                showToast(someException());
            }
        }

        @Override
        public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

        }

        @Override
        public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

        }
    };

    private void setSearchAdapter(List<PoiInfo> searchResult) {
        try {
            List<Address> addressList=new ArrayList<Address>();
            for (int i=0;i<searchResult.size();i++){
                Address addressItem = new Address();
                addressItem.setX(searchResult.get(i).location.latitude);
                addressItem.setY(searchResult.get(i).location.longitude);
                addressItem.setName(searchResult.get(i).name);
                addressItem.setAddress(searchResult.get(i).address);
                addressList.add(addressItem);
            }
        }catch (Exception e){
            showToast(someException());
        }
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
                if (!s.toString().trim().equals("")){
                    poiCitySearchOption= new PoiCitySearchOption().city(city).keyword(etSearchLocation.getText()+"");
                    poiSearch.searchInCity(poiCitySearchOption);
                }
            }catch (Exception e){
                showToast(someException());
            }
        }
    };

    @Override
    protected void getBundleExtras(Bundle extras) {
        city=extras.getString("city");
    }

    @Override
    public int getContentView() {
        return R.layout.activity_search_location;
    }
}
