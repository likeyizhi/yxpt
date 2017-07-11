package com.bbld.yxpt.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.bbld.yxpt.bean.Address;

import java.util.ArrayList;

/**
 * 地址搜索adapter
 * Created by likey on 2017/7/6.
 */

public class SearchAddress extends BaseAdapter{
    private LayoutInflater inflater;
    private Context context;
    private ArrayList<Address> addressList;

    public SearchAddress(LayoutInflater inflater, Context context, ArrayList<Address> addressList) {
        super();
        this.inflater = inflater;
        this.context = context;
        this.addressList = addressList;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }
}
