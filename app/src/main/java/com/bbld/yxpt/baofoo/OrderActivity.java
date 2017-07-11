package com.bbld.yxpt.baofoo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baofoo.juhepaysdk.BaofooJuHeAliPayTask;
import com.baofoo.juhepaysdk.BaofooJuHeWeiXinPayTask;
import com.baofoo.juhepaysdk.Constants;
import com.bbld.yxpt.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 下单界面
 */
public class OrderActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mSubmitBtn;
    private LinearLayout llWeixinPay;
    private LinearLayout llZhifubaoPay;
    private boolean isCheckedWeixin = true;
    private boolean isCheckedZhifubao = false;
    private String testTokenId;
    private ImageView imgWeixinChecked;
    private ImageView imgZhifubaoChecked;
    private ImageView iv_logo;
    private String amount;
    private TextView tvAmount;
    private RequestDialog dialogRequestWeiXin;
    private int cj = 2;//默认生产环境
    private int amg = 0;//默认0分
    private boolean isAlipay = false;

    private BaofooJuHeAliPayTask baofooJuHeAliPayTask;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();

            //微信请求
            String weiXinValue = data.getString("weiXinValue");
            if (weiXinValue != null) {
                if (!weiXinValue.equals("")) {
                    dialogRequestWeiXin.isProgressShow(false);
                    dialogRequestWeiXin.setMessage("订单号返回成功!");

                    callWeiXin(weiXinValue);
                } else if (weiXinValue.equals("")) {
                    dialogRequestWeiXin.isProgressShow(false);
                    dialogRequestWeiXin.setMessage("订单号返回失败!");
                }
            }


            //支付宝请求
            String aliPayValue = data.getString("aliPayValue");
            if (aliPayValue != null) {
                if (!aliPayValue.equals("")) {
                    dialogRequestWeiXin.isProgressShow(false);
                    dialogRequestWeiXin.setMessage("订单号返回成功!");

                    calllAliPay(aliPayValue);
                    Log.d("aliPayValue值", aliPayValue);
                } else if (aliPayValue.equals("")) {
                    dialogRequestWeiXin.isProgressShow(false);
                    dialogRequestWeiXin.setMessage("订单号返回失败!");
                }
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        initView();
        initData();
    }

    private void initView() {
        tvAmount = (TextView) findViewById(R.id.tv_amount);
        mSubmitBtn = (Button) findViewById(R.id.btn_sure_pay);
        llWeixinPay = (LinearLayout) findViewById(R.id.ll_weixin_pay);
        llZhifubaoPay = (LinearLayout) findViewById(R.id.ll_zhifubao_pay);
        imgWeixinChecked = (ImageView) findViewById(R.id.img_weixin_checked);
        imgZhifubaoChecked = (ImageView) findViewById(R.id.img_zhifubao_checked);
        iv_logo = (ImageView) findViewById(R.id.iv_logo);
        llWeixinPay.setOnClickListener(this);
        llZhifubaoPay.setOnClickListener(this);
        mSubmitBtn.setOnClickListener(this);
        iv_logo.setOnClickListener(this);
    }

    private void initData() {
        amg = getIntent().getIntExtra("INPUTAMOUNT", 1);
        tvAmount.setText(amg + "");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sure_pay:

                //微信
                if (isCheckedWeixin) {
                    //弹框
                    dialogRequestWeiXin = new RequestDialog(OrderActivity.this) {
                        @Override
                        protected void payConfirm() {
                            this.dismiss();
                        }

                        @Override
                        protected void payCancel() {
                            this.dismiss();
                        }
                    };
                    dialogRequestWeiXin.show();
                    dialogRequestWeiXin.setMessage("请求微信订单号...");
                    dialogRequestWeiXin.isProgressShow(true);
                    //调用微信
                    new Thread(weiXinTokenIdNetworkTask).start();
                }
                //支付宝
                if (isCheckedZhifubao) {
                    //弹框
                    dialogRequestWeiXin = new RequestDialog(OrderActivity.this) {
                        @Override
                        protected void payConfirm() {
                            this.dismiss();
                        }

                        @Override
                        protected void payCancel() {
                            this.dismiss();
                        }
                    };
                    dialogRequestWeiXin.show();
                    dialogRequestWeiXin.setMessage("请求支付宝订单号...");
                    dialogRequestWeiXin.isProgressShow(true);
                    //调用支付宝
                    new Thread(aliPayTokenIdNetworkTask).start();

                    //startActivity(new Intent(PayMainActivity.this, ZhiFuBaoActivity.class));

//                    Intent intent;
//                    try {
//                        intent = Intent.parseUri("https://qr.alipay.com/bax00068b3l3qn32ylpw604b", Intent.URI_INTENT_SCHEME);
//                        intent.addCategory(Intent.CATEGORY_BROWSABLE);
//                        intent.setComponent(null);
//                        startActivity(intent);
//                        finish();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
                }

                break;
            case R.id.ll_weixin_pay:
                if (!isCheckedWeixin) {
                    isCheckedWeixin = !isCheckedWeixin;
                    isCheckedZhifubao = !isCheckedZhifubao;
                    imgWeixinChecked.setImageDrawable(getResources().getDrawable(R.drawable.checked));
                    imgZhifubaoChecked.setImageDrawable(getResources().getDrawable(R.drawable.unchecked));
                }
                break;
            case R.id.ll_zhifubao_pay:
                if (!isCheckedZhifubao) {
                    isCheckedWeixin = !isCheckedWeixin;
                    isCheckedZhifubao = !isCheckedZhifubao;
                    imgWeixinChecked.setImageDrawable(getResources().getDrawable(R.drawable.unchecked));
                    imgZhifubaoChecked.setImageDrawable(getResources().getDrawable(R.drawable.checked));
                }
                break;
            case R.id.iv_logo:
                new EnvironmnetDialog(OrderActivity.this) {
                    @Override
                    protected void setEnvironment(int cjE) {
                        cj = cjE;
                        this.dismiss();
                    }
                }.show();
                break;
            default:
                break;
        }
    }

    /**
     * 微信相关操作
     *
     * @param testTokenId
     */
    //微信:1、请求TokenID
    Runnable weiXinTokenIdNetworkTask = new Runnable() {
        @Override
        public void run() {
            //测试:请求订单号(TokenId)
            String url = "http://dev-gw.baofoo.com/fi-test/appWeChat?cj=" + cj + "&amg=" + amg + "&appId=wx5f490891bf38366c";//amg:金额,cj:环境(1测试2正式3准生产)
            Log.d("微信请求TokenId地址", url);

            String testTokenId = "26c51b328d86485bca29b017f3e72533d";
            Log.d("Token返回的TokenId", testTokenId);

            Message msg = new Message();
            Bundle data = new Bundle();
            data.putString("weiXinValue", testTokenId);
            msg.setData(data);
            handler.sendMessage(msg);
            //测试:Get请求
//            HttpGet httpRequst = new HttpGet(url);
//            try {
//                HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequst);
//                if (httpResponse.getStatusLine().getStatusCode() == 200) {
//                    HttpEntity httpEntity = httpResponse.getEntity();
//                    String entityString = EntityUtils.toString(httpEntity);
//                    Log.d("Token请求返回", entityString);
//                    String testTokenId = entityString.substring(entityString.indexOf("=") + 1, entityString.length());
//                    Log.d("Token返回的TokenId", testTokenId);
//
//                    Message msg = new Message();
//                    Bundle data = new Bundle();
//                    data.putString("weiXinValue", testTokenId);
//                    msg.setData(data);
//                    handler.sendMessage(msg);
//                } else {
//
//                    Message msg = new Message();
//                    Bundle data = new Bundle();
//                    data.putString("weiXinValue", "");
//                    msg.setData(data);
//                    handler.sendMessage(msg);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//
//                Message msg = new Message();
//                Bundle data = new Bundle();
//                data.putString("weiXinValue", "");
//                msg.setData(data);
//                handler.sendMessage(msg);
//            }
        }
    };

    //微信:2、启动微信客户端
    private void callWeiXin(String testTokenId) {
        dialogRequestWeiXin.setMessage("调用宝付，启动微信支付···");
        dialogRequestWeiXin.dismiss();

        //调用微信支付
        Map<String, String> map = new HashMap<String, String>();
        map.put(Constants.TOKEN_ID, testTokenId);//商户后台请求到的TokenId
        map.put(Constants.APPID, "wx5f490891bf38366c");//商户的APPID
        BaofooJuHeWeiXinPayTask task = new BaofooJuHeWeiXinPayTask(OrderActivity.this, map);
        task.execute();//执行
    }

    /**
     * 支付宝相关操作
     */
    //支付宝:请求aliPayTokenId
    Runnable aliPayTokenIdNetworkTask = new Runnable() {
        @Override
        public void run() {
            //商户请求token_id
            String url = "http://dev-gw.baofoo.com/fi-test/appAliPay?cj=" + cj + "&amg=" + amg;

            testTokenId = "H4sIAAAAAAAAAC3MSw7CIBRA0d04hAdIWk0ax50Zd/BoqcXwk0+UxsXbGOf33LWUmM+UPhNBayI2MgVHFb5B9Ce5wYb+sTbhWXeEGT7rP49VWTMRhWEJ4UeixbKEtNuajdc50zsW/cK2r6/YbjpXWy5OO6XTOA+MQy/6Q0nos5kHDqyDDiQDIbjkIL4zSzb7mAAAAA==";
            Log.d("Token:AliPay返回的TokenId", testTokenId);

            Message msg = new Message();
            Bundle data = new Bundle();
            data.putString("aliPayValue", testTokenId);
            msg.setData(data);
            handler.sendMessage(msg);

//            HttpGet httpRequst = new HttpGet(url);
//            try {
//                HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequst);
//                if (httpResponse.getStatusLine().getStatusCode() == 200) {
//                    HttpEntity httpEntity = httpResponse.getEntity();
//                    String entityString = EntityUtils.toString(httpEntity);
//                    Log.d("Token:AliPay请求返回", entityString);
//                    testTokenId = entityString.substring(entityString.indexOf("=") + 1, entityString.length());
//                    Log.d("Token:AliPay返回的TokenId", testTokenId);
//
//                    Message msg = new Message();
//                    Bundle data = new Bundle();
//                    data.putString("aliPayValue", testTokenId);
//                    msg.setData(data);
//                    handler.sendMessage(msg);
//                } else {
//                    //处理错误
//                    Message msg = new Message();
//                    Bundle data = new Bundle();
//                    data.putString("aliPayValue", "");
//                    msg.setData(data);
//                    handler.sendMessage(msg);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//
//                Message msg = new Message();
//                Bundle data = new Bundle();
//                data.putString("aliPayValue", "");
//                msg.setData(data);
//                handler.sendMessage(msg);
//            }
        }
    };

    //调用支付宝
    private void calllAliPay(String testTokenId) {
        dialogRequestWeiXin.setMessage("正在支付...");//请自行处理
        isAlipay = true;

        baofooJuHeAliPayTask = new BaofooJuHeAliPayTask(OrderActivity.this, testTokenId) {
            @Override
            protected void ret(String result) {//支付结果回调
                try {
                    Log.d("Token:AliPay请求结果", result);

                    JSONObject resultObject = new JSONObject(result);
                    String retMsg = resultObject.getString("retMsg");
                    dialogRequestWeiXin.setMessage(retMsg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        baofooJuHeAliPayTask.execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isAlipay) {
            isAlipay = false;

            //手动获取支付宝支付结果,结果会在BaofooJuHeAliPayTask的回调中立即返回(想使用这个功能,开启这段代码即可)
            //baofooJuHeAliPayTask.toGetAliPayResult();
        }
    }
}
