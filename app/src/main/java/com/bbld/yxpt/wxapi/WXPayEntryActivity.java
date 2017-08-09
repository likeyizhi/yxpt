package com.bbld.yxpt.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bbld.yxpt.R;
import com.bbld.yxpt.activity.NewOrderActivity;
import com.bbld.yxpt.activity.PaySuccessActivity;
import com.switfpass.pay.utils.Constants;
import com.tencent.mm.opensdk.utils.Log;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
    private IWXAPI api;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        api.handleIntent(getIntent(), this);
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }
    @Override
    public void onReq(BaseReq req) {
    }
    /**
     *  第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
     *   arg0。errCode  0成功 -1支付失败 -2取消
     */
    @Override
    public void onResp(BaseResp resp) {
        Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);
        if (resp.errCode == 0) {//支付成功
            Intent intent=new Intent(WXPayEntryActivity.this, PaySuccessActivity.class);
            Bundle bundle=new Bundle();
            bundle.putString("money", NewOrderActivity.money);
            bundle.putString("ShopImg", NewOrderActivity.shopImg);
            bundle.putString("ShopName", NewOrderActivity.shopName);
            intent.putExtras(bundle);
            Toast.makeText(getApplicationContext(), "支付成功", Toast.LENGTH_SHORT).show();
            NewOrderActivity.newOrderActivity.finish();
            startActivity(intent);
            finish();
        } else if (resp.errCode == -1) {//支付失败
            Toast.makeText(getApplicationContext(), "支付失败", Toast.LENGTH_SHORT).show();
            finish();
        } else {//取消
            Toast.makeText(getApplicationContext(), "支付取消", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}