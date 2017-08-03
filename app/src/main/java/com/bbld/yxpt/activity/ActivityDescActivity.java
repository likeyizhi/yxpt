package com.bbld.yxpt.activity;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bbld.yxpt.R;
import com.wuxiaolong.androidutils.library.ActivityManagerUtil;

/**
 * 活动规则
 * Created by likey on 2017/8/2.
 */

public class ActivityDescActivity extends Activity{
    private TextView tvShowDesc;
    private String desc;
    private RelativeLayout rlActivittyDescClose;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_desc);
        desc=getIntent().getExtras().getString("desc");
        initView();
        loadData();
        setListeners();
    }

    private void setListeners() {
        rlActivittyDescClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(0,R.anim.zoomout);
            }
        });
    }

    private void loadData() {
        tvShowDesc.setText(desc);
    }

    private void initView() {
        tvShowDesc=(TextView)findViewById(R.id.tvShowDesc);
        rlActivittyDescClose=(RelativeLayout)findViewById(R.id.rlActivittyDescClose);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
            finish();
            overridePendingTransition(0,R.anim.zoomout);
        }
        return false;
    }
}
