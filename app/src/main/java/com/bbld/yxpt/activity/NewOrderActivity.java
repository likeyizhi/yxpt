package com.bbld.yxpt.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.bbld.yxpt.R;
import com.bbld.yxpt.base.BaseActivity;
import com.bbld.yxpt.bean.GetAlipayPayParam;
import com.bbld.yxpt.bean.WeiXinPayParam;
import com.bbld.yxpt.network.RetrofitService;
import com.bbld.yxpt.utils.AuthResult;
import com.bbld.yxpt.utils.MyToken;
import com.bbld.yxpt.utils.OrderInfoUtil2_0;
import com.bbld.yxpt.utils.PayResult;
import com.bumptech.glide.Glide;
import com.switfpass.pay.utils.Constants;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.wuxiaolong.androidutils.library.ActivityManagerUtil;

import java.util.Map;

import butterknife.BindView;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by dell on 2017/7/28.
 */

public class NewOrderActivity extends BaseActivity {
    @BindView(R.id.ivShopImg)
    ImageView ivShopImg;
    @BindView(R.id.tvShopName)
    TextView tvShopName;
    @BindView(R.id.tvHot)
    TextView tvHot;
    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.btnTest)
    Button btnTest;
    @BindView(R.id.tv_amount)
    TextView tvAmount;
    @BindView(R.id.img_weixin_checked)
    ImageView imgWeixin;
    @BindView(R.id.img_zhifubao_checked)
    ImageView imgZhifubao;

    private String orderNo;
    private String token;
    private String orderInfo;
    public static String money;
    public static String shopImg;
    public static  String shopName;
    public static NewOrderActivity newOrderActivity;
    private String hot;
    private String payType="wx";

    /** 微信支付相关*/
    private IWXAPI api;


    /** 支付宝支付业务：入参app_id */
    public static final String APPID = "2017072707921833";

    /** 支付宝账户登录授权业务：入参pid值 */
    public static final String PID = "";
    /** 支付宝账户登录授权业务：入参target_id值 */
    public static final String TARGET_ID = "";

    /** 商户私钥，pkcs8格式 */
    /** 如下私钥，RSA2_PRIVATE 或者 RSA_PRIVATE 只需要填入一个 */
    /** 如果商户两个都设置了，优先使用 RSA2_PRIVATE */
    /** RSA2_PRIVATE 可以保证商户交易在更加安全的环境下进行，建议使用 RSA2_PRIVATE */
    /** 获取 RSA2_PRIVATE，建议使用支付宝提供的公私钥生成工具生成， */
    /** 工具地址：https://doc.open.alipay.com/docs/doc.htm?treeId=291&articleId=106097&docType=1 */
    public static final String RSA2_PRIVATE = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAMgKtN8ecHknIe9v\n" +
            "7tiAjQ+y/jptYnoTjHK6iJWwK0R0BN4CoYw0T6WdULvJU6kvTii+Vk05SLGiWBBh\n" +
            "TwVBhXMeMs8SUHC9FlOj6WDzThbkhastB58uxFFScG/jLdPE4LDgodun9JqExPCe\n" +
            "tKpwFnPtYbd6LFT2gAFwFv3QjtaFAgMBAAECgYAceOmlmD+J/vfljJkCV8m8Ik3t\n" +
            "IwSRO5cR/VrTc2+1Ho5Syy0TN57eg7WqBM3TltgZFH5UH4MpRWIjcrY8mUbVC0jF\n" +
            "NzV/zC0BA2lST0s6OmkJ3gqQLOnsq8skLa7VKr2cCRnlOCUyHD/lI+PntBNlhHa7\n" +
            "yj2aUAp4XmwyceJ7QQJBAO1zb8B0KfKP3ea+m4vddNlFQu9p0HoBieXgYY3eFtXt\n" +
            "t/FJUE+xYXSkweNiuTdTZ3N3lTxZNLipu/9lDvRfOEMCQQDXqyje9Y1xCPPAZXRf\n" +
            "Kh1HUdqnUeQEZr4Lvpoqoqa3Gy8zPq7bRSMRY1Pzt39GEdMtLG6enBzLGTlXGfpU\n" +
            "Xc2XAkBBuHCWyNAPZchaJClzJcFAnY3wK85a5nAfYuI9XNnfLiN6Mft7gzHhEfTk\n" +
            "FaAD8x6v88onYN1ZuwjgoDzqYCx3AkA75iPR2O1RyaS5ePQbQj0jg85wzkPAHTEH\n" +
            "Xga/dLFNBdXUK8Kwz8DvoWC9vecxdN3sG/0VPFKa7gSI9oQaTns9AkAUYTzewLOr\n" +
            "tlTfR9Ga5KHIQ7fByQFUAYyai+ZDI+oRepaDYuwRLm/358hkAAPtRNfVB04d34V9\n" +
            "jv/u7Z46KG+K";
    public static final String RSA_PRIVATE = "";

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(NewOrderActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        Bundle bundle=new Bundle();
                        bundle.putString("ShopImg",shopImg+"");
                        bundle.putString("ShopName",tvShopName.getText()+"");
                        bundle.putString("money", tvAmount.getText()+"");

                        readyGo(PaySuccessActivity.class,bundle);
                        ActivityManagerUtil.getInstance().finishActivity(NewOrderActivity.this);

                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(NewOrderActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                case SDK_AUTH_FLAG: {
                    @SuppressWarnings("unchecked")
                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();

                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        // 传入，则支付账户为该授权账户
                        Toast.makeText(NewOrderActivity.this,
                                "授权成功\n" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        // 其他状态值则为授权失败
                        Toast.makeText(NewOrderActivity.this,
                                "授权失败" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT).show();

                    }
                    break;
                }
                default:
                    break;
            }
        };
    };

    /**
     * 支付宝支付业务
     *
     * @param
     */
    public void payV2() {
        if (TextUtils.isEmpty(APPID) || (TextUtils.isEmpty(RSA2_PRIVATE) && TextUtils.isEmpty(RSA_PRIVATE))) {
            new AlertDialog.Builder(this).setTitle("警告").setMessage("需要配置APPID | RSA_PRIVATE")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            //
                            finish();
                        }
                    }).show();
            return;
        }

        /**
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
         *
         * orderInfo的获取必须来自服务端；
         */
        boolean rsa2 = (RSA2_PRIVATE.length() > 0);
        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID, rsa2);
//        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);
//
//        String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
//        String sign = OrderInfoUtil2_0.getSign(params, privateKey, rsa2);
//        final String orderInfo = orderParam + "&" + sign;

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(NewOrderActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Log.i("msp", result.toString());

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * 微信支付相关
     */
    private void payWX() {
        Call<WeiXinPayParam> call=RetrofitService.getInstance().getWeiXinPayParam(new MyToken(NewOrderActivity.this).getToken(),orderNo);
        call.enqueue(new Callback<WeiXinPayParam>() {
            @Override
            public void onResponse(Response<WeiXinPayParam> response, Retrofit retrofit) {
                if (response==null){
                    showToast(responseFail());
                    return;
                }
                if (response.body().getStatus()==0){
                    WeiXinPayParam.WeiXinPayParamOrderString wxOrder = response.body().getOrderString();
                    api = WXAPIFactory.createWXAPI(NewOrderActivity.this, wxOrder.getAppid(), false);
                    api.registerApp(wxOrder.getAppid());

                    PayReq payReq = new PayReq();
                    payReq.appId = wxOrder.getAppid();// 微信开放平台审核通过的应用APPID
                    payReq.partnerId = wxOrder.getPartnerid();// 微信支付分配的商户号
                    payReq.prepayId = wxOrder.getPrepayid();// 预支付订单号，app服务器调用“统一下单”接口获取
                    payReq.packageValue = wxOrder.getPackage();// 固定值Sign=WXPay，可以直接写死，服务器返回的也是这个固定值
                    payReq.nonceStr = wxOrder.getNoncestr();// 随机字符串，不长于32位，服务器小哥会给咱生成
                    payReq.timeStamp = wxOrder.getTimestamp();// 时间戳，app服务器小哥给出
                    payReq.sign = wxOrder.getSign();// 签名，服务器小哥给出，他会根据：https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=4_3指导得到这个
                    // 调用api接口发送数据到微信
                    api.sendReq(payReq);
                }else{
                    showToast(response.body().getMes());
                }
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    @Override
    protected void initViewsAndEvents() {
        newOrderActivity=this;
        token=new MyToken(this).getToken();
        tvAmount.setText("￥"+money);
        Glide.with(getApplicationContext()).load(shopImg).into(ivShopImg);
        tvShopName.setText(shopName+"");
        tvHot.setText(hot+"");
        loadData();
        setListeners();
    }

    private void loadData(){
        Call<GetAlipayPayParam> call= RetrofitService.getInstance().getAlipayPayParam(token,orderNo);
        call.enqueue(new Callback<GetAlipayPayParam>() {
            @Override
            public void onResponse(Response<GetAlipayPayParam> response, Retrofit retrofit) {
                if (response==null){
                    showToast(responseFail());
                    return;
                }
                if (response.body().getStatus()==0){
                    orderInfo=response.body().getOrderString();
                }else{
                    showToast(response.body().getMes());
                }
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }
    private void setListeners(){
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (payType){
                    case "wx":
                        payWX();
                        break;
                    case "zfb":
                        payV2();
                        break;
                }
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityManagerUtil.getInstance().finishActivity(NewOrderActivity.this);
            }
        });
        imgWeixin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgWeixin.setImageResource(R.drawable.checked);
                imgZhifubao.setImageResource(R.drawable.unchecked);
                payType="wx";
            }
        });
        imgZhifubao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgWeixin.setImageResource(R.drawable.unchecked);
                imgZhifubao.setImageResource(R.drawable.checked);
                payType="zfb";
            }
        });
    }
    @Override
    protected void getBundleExtras(Bundle extras) {
        orderNo=extras.getString("orderNo");
        money=extras.getString("money");
        shopImg=extras.getString("ShopImg");
        shopName=extras.getString("ShopName");
        hot=extras.getString("Hot");
    }

    @Override
    public int getContentView() {
        return R.layout.activity_gopay;
    }
}
