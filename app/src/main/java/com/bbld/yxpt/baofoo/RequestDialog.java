package com.bbld.yxpt.baofoo;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bbld.yxpt.R;

/**
 * 请求数据自定义Dialog
 * Created by zst on 2017/3/28.
 */

public abstract class RequestDialog extends Dialog {
    private View rootview;
    private Context mContext;
    private TextView tv_pay_confirm;
    private TextView tv_pay_cancel;
    private TextView tv_message;
    private ProgressBar pb_progress;

    public RequestDialog(Context context) {
        super(context, R.style.RequestDialogStyle);

        this.mContext = context;
        rootview = LayoutInflater.from(mContext).inflate(R.layout.dialog_request, null, false);

        tv_pay_confirm = (TextView) rootview.findViewById(R.id.tv_pay_confirm);
        tv_pay_cancel = (TextView) rootview.findViewById(R.id.tv_pay_cancel);
        tv_message = (TextView) rootview.findViewById(R.id.tv_message);
        pb_progress = (ProgressBar) rootview.findViewById(R.id.pb_progress);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(rootview);
        //this.getWindow().setBackgroundDrawable(new PaintDrawable(Color.TRANSPARENT));

        setCancelable(true);
        setCanceledOnTouchOutside(false);

        tv_pay_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payConfirm();
            }
        });

        tv_pay_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payCancel();
            }
        });
    }

    public void setMessage(String message) {
        tv_message.setText(message);
    }

    public void isProgressShow(boolean b) {
        if (b) {
            pb_progress.setVisibility(View.VISIBLE);
        }else {
            pb_progress.setVisibility(View.GONE);
        }
    }

    protected abstract void payConfirm();
    protected abstract void payCancel();

}
