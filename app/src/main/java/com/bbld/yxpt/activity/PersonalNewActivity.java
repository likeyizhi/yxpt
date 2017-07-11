package com.bbld.yxpt.activity;

import android.os.Bundle;

import com.bbld.yxpt.R;
import com.bbld.yxpt.base.BaseActivity;

/**
 * 个人中心(新)
 * Created by likey on 2017/7/10.
 */

public class PersonalNewActivity extends BaseActivity{


    @Override
    protected void initViewsAndEvents() {
        loadData();
    }

    private void loadData() {
        
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    public int getContentView() {
        return R.layout.activity_personal_new;
    }
}
