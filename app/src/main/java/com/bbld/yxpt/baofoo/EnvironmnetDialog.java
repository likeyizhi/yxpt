package com.bbld.yxpt.baofoo;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.bbld.yxpt.R;

/**
 * 选择环境自定义Dialog
 * Created by zst on 2017/3/28.
 */

public abstract class EnvironmnetDialog extends Dialog {
    private View rootview;
    private Context mContext;
    private TextView tv_test;
    private TextView tv_zsc;
    private TextView tv_pro;

    public EnvironmnetDialog(Context context) {
        super(context, R.style.RequestDialogStyle);

        this.mContext = context;
        rootview = LayoutInflater.from(mContext).inflate(R.layout.dialog_environment, null, false);

        tv_test = (TextView) rootview.findViewById(R.id.tv_test);
        tv_zsc = (TextView) rootview.findViewById(R.id.tv_zsc);
        tv_pro = (TextView) rootview.findViewById(R.id.tv_pro);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(rootview);
        //this.getWindow().setBackgroundDrawable(new PaintDrawable(Color.TRANSPARENT));

        //setCancelable(true);
        //setCanceledOnTouchOutside(false);

        tv_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEnvironment(1);
            }
        });

        tv_zsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEnvironment(3);
            }
        });

        tv_pro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEnvironment(2);
            }
        });
    }

    protected abstract void setEnvironment(int cj);

}
