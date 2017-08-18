package com.bbld.yxpt.activity;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
                try {
                    finish();
                    overridePendingTransition(0,R.anim.zoomout);
                }catch (Exception e){
                    Toast.makeText(ActivityDescActivity.this,getString(R.string.some_exception),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadData() {
        try {
            tvShowDesc.setText(desc);
        }catch (Exception e){
            Toast.makeText(ActivityDescActivity.this,getString(R.string.some_exception),Toast.LENGTH_SHORT).show();
        }
    }

    private void initView() {
        try {
            tvShowDesc=(TextView)findViewById(R.id.tvShowDesc);
        }catch (Exception e){
            Toast.makeText(ActivityDescActivity.this,getString(R.string.some_exception),Toast.LENGTH_SHORT).show();
        }
        try {
            rlActivittyDescClose=(RelativeLayout)findViewById(R.id.rlActivittyDescClose);
        }catch (Exception e){
            Toast.makeText(ActivityDescActivity.this,getString(R.string.some_exception),Toast.LENGTH_SHORT).show();
        }
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
