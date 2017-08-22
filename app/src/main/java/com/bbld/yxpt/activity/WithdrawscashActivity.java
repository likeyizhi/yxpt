package com.bbld.yxpt.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.AuthTask;
import com.bbld.yxpt.R;
import com.bbld.yxpt.base.BaseActivity;
import com.bbld.yxpt.bean.AddWithdrawa;
import com.bbld.yxpt.bean.AlipayLoginParam;
import com.bbld.yxpt.bean.AlipayUserInfo;
import com.bbld.yxpt.bean.GetUserBindInfo;
import com.bbld.yxpt.bean.ServiceMoney;
import com.bbld.yxpt.bean.UserBind;
import com.bbld.yxpt.bean.WithdrawaAccountInfo;
import com.bbld.yxpt.loadingdialog.WeiboDialogUtils;
import com.bbld.yxpt.network.RetrofitService;
import com.bbld.yxpt.utils.AuthResult;
import com.bbld.yxpt.utils.MyToken;
import com.bbld.yxpt.utils.OrderInfoUtil2_0;
import com.bbld.yxpt.utils.PayResult;
import com.bbld.yxpt.wxapi.WXEntryActivity;
import com.bumptech.glide.Glide;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wuxiaolong.androidutils.library.ActivityManagerUtil;

import java.math.BigDecimal;
import java.util.Map;

import butterknife.BindView;
import cn.jpush.android.api.JPushInterface;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * 提现
 * Created by likey on 2017/7/11.
 */

public class WithdrawscashActivity extends BaseActivity{
    @BindView(R.id.llBankCard)
    LinearLayout llBankCard;
    @BindView(R.id.ivBankLogo)
    ImageView ivBankLogo;
    @BindView(R.id.tvBankName)
    TextView tvBankName;
    @BindView(R.id.tvBankNo)
    TextView tvBankNo;
    @BindView(R.id.etMoney)
    EditText etMoney;
    @BindView(R.id.tvCanMoney)
    TextView tvCanMoney;
    @BindView(R.id.tvAllMoney)
    TextView tvAllMoney;
    @BindView(R.id.btnOK)
    Button btnOK;
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.img_weixin_checked)
    ImageView img_weixin_checked;
    @BindView(R.id.img_zhifubao_checked)
    ImageView img_zhifubao_checked;
    @BindView(R.id.tvWXToBind)
    TextView tvWXToBind;
    @BindView(R.id.tvZFBToBind)
    TextView tvZFBToBind;

    private String token;
    private String accountMoney;
    private WithdrawaAccountInfo.WithdrawaAccountInfocardinfo cardinfo;
    private int bankCardId;
    private Dialog mWeiboDialog;
    private String serviceMoney;
    private int type=1;
    /** 支付宝支付业务：入参app_id */
    public static final String APPID = "2017072707921833";

    /** 支付宝账户登录授权业务：入参pid值 */
    public static final String PID = "2088721453488684";
    /** 支付宝账户登录授权业务：入参target_id值 */
    public static final String TARGET_ID = "20141225xxxx";
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
    private Dialog openZFBDialog;
    private IWXAPI WXapi;
    private String WX_APP_ID = "wxb32b83a9fe661456";
    private AlipayUserInfo.AlipayUserInfoUserInfo userInfoZFB;
    private Dialog openWXDialog;
    private Dialog loginDialog;
    private int jointype;
    private String joinid;
    public static WithdrawscashActivity withdrawscashActivity=null;
    private BigDecimal omAccount;
    @SuppressLint("HandlerLeak")
    private Handler zHandler = new Handler() {
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
                        Toast.makeText(WithdrawscashActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(WithdrawscashActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(WithdrawscashActivity.this,
                                "授权成功"/* + String.format("authCode:%s", authResult.getAuthCode())*/, Toast.LENGTH_SHORT)
                                .show();
//                        getZFDMsg(authResult.getAuthCode());
                        getZFDMsg(authResult.getAuthCode());

                    } else {
                        // 其他状态值则为授权失败
                        Toast.makeText(WithdrawscashActivity.this,
                                "授权失败" /*+ String.format("authCode:%s", authResult.getAuthCode())*/, Toast.LENGTH_SHORT).show();

                    }
                    break;
                }
                default:
                    break;
            }
        }
    };

    @Override
    protected void initViewsAndEvents() {
        withdrawscashActivity=this;
        token=new MyToken(this).getToken();
        loadData();
        loadBindData();
        setListeners();
    }

    private void loadData() {
        try {
            Call<WithdrawaAccountInfo> call= RetrofitService.getInstance().getWithdrawaAccountInfo(token);
            call.enqueue(new Callback<WithdrawaAccountInfo>() {
                @Override
                public void onResponse(Response<WithdrawaAccountInfo> response, Retrofit retrofit) {
                    if (response==null){
                        showToast(responseFail());
                        return;
                    }
                    if (response.body().getStatus()==0){
                        try {
                            accountMoney=response.body().getAccountMoney();
                            cardinfo=response.body().getCardinfo();
                            Glide.with(getApplicationContext()).load(cardinfo.getBankLogo()).into(ivBankLogo);
                            bankCardId=cardinfo.getBankCardID();
                            if (bankCardId!=0){
                                tvBankName.setText(cardinfo.getBankName()+"");
                                tvBankNo.setText("尾号"+cardinfo.getCardNo().substring(cardinfo.getCardNo().length()-4,cardinfo.getCardNo().length()));
                            }
                            tvCanMoney.setText("可提现金额"+accountMoney+"元");
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

    private void setListeners() {
        llBankCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    readyGoForResult(BankCardActivity.class,7788);
                }catch (Exception e){
                    showToast(someException());
                }
            }
        });
        tvAllMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    getService(accountMoney,true);
//                    etMoney.setText(accountMoney);
                }catch (Exception e){
                    showToast(someException());
                }
            }
        });
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((etMoney.getText()+"").trim().equals("") || (etMoney.getText()+"").trim().length()==0){
                    showToast("请输入提现金额");
                }else{
                    if (Double.parseDouble(serviceMoney)+Double.parseDouble(etMoney.getText()+"")>Double.parseDouble(accountMoney)){
                        showToast("提现金额超出");
                    }else{
                        try {
                            mWeiboDialog = WeiboDialogUtils.createLoadingDialog(WithdrawscashActivity.this, "处理中...");
                            toAddWithdrawa();
                        }catch (Exception e){
                            showToast(someException());
                        }
                    }
                }
            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    finish();
                }catch (Exception e){
                    showToast(someException());
                }
            }
        });
        img_weixin_checked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img_weixin_checked.setImageResource(R.mipmap.right);
                img_zhifubao_checked.setImageResource(R.mipmap.circle);
                type=1;
            }
        });
        img_zhifubao_checked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img_weixin_checked.setImageResource(R.mipmap.circle);
                img_zhifubao_checked.setImageResource(R.mipmap.right);
                type=2;
            }
        });
        etMoney.addTextChangedListener(watcher);
        tvWXToBind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    //保存Token
                    SharedPreferences shared=getSharedPreferences("YXToken",MODE_PRIVATE);
                    SharedPreferences.Editor editor=shared.edit();
                    editor.putString("WXLogin","Withdrawscash");
                    editor.commit();
                    WXLogin();
                }catch (Exception e){
                    showToast(someException());
                }
            }
        });
        tvZFBToBind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getOrderString();
            }
        });
    }

    TextWatcher watcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (etMoney.getText().toString().indexOf(".") >= 0) {
                if (etMoney.getText().toString().indexOf(".", etMoney.getText().toString().indexOf(".") + 1) > 0) {
//                    tv_numOfChar.setText("已经输入\".\"不能重复输入");
                    etMoney.setText(etMoney.getText().toString().substring(0, etMoney.getText().toString().length() - 1));
                    etMoney.setSelection(etMoney.getText().toString().length());
                }else{
                    if (editable.length()!=0){
                        if (Double.parseDouble(accountMoney)<Double.parseDouble((editable+"").toString().trim())){
                            showToast("超出可提现金额");
                        }else{
                            getService(editable+"", false);
                        }
                    }else{
                        tvCanMoney.setText("可提现金额"+accountMoney+"元");
                    }
                }
            }else{
                if (editable.length()!=0){
                    if (Double.parseDouble(accountMoney)<Double.parseDouble((editable+"").toString().trim())){
                        showToast("超出可提现金额");
                    }else{
                        getService(editable+"", false);
                    }
                }else{
                    tvCanMoney.setText("可提现金额"+accountMoney+"元");
                }
            }
        }
    };

    private void loadBindData() {
        try {
            Call<GetUserBindInfo> call= RetrofitService.getInstance().getUserBindInfo(token);
            call.enqueue(new Callback<GetUserBindInfo>() {
                @Override
                public void onResponse(Response<GetUserBindInfo> response, Retrofit retrofit) {
                    if (response==null){
                        showToast(responseFail());
                        return;
                    }
                    if (response.body().getStatus()==0) {
                        if (response.body().getWeiXin() == 1) {
                            tvWXToBind.setVisibility(View.GONE);
                            img_weixin_checked.setVisibility(View.VISIBLE);
                            type=1;
                        } else if (response.body().getWeiXin() == 0) {
                            tvWXToBind.setVisibility(View.VISIBLE);
                            img_weixin_checked.setVisibility(View.GONE);
                        }
                        if (response.body().getAlipay() == 1) {
                            tvZFBToBind.setVisibility(View.GONE);
                            img_zhifubao_checked.setVisibility(View.VISIBLE);
                            if (response.body().getWeiXin() == 0){
                                img_zhifubao_checked.setImageResource(R.mipmap.right);
                                type=2;
                            }
                        } else if (response.body().getAlipay() == 0) {
                            tvZFBToBind.setVisibility(View.VISIBLE);
                            img_zhifubao_checked.setVisibility(View.GONE);
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

    private void getService(final String s, final boolean isAll) {
        Call<ServiceMoney> call=RetrofitService.getInstance().getServiceMoney(s);
        call.enqueue(new Callback<ServiceMoney>() {
            @Override
            public void onResponse(Response<ServiceMoney> response, Retrofit retrofit) {
                if (response==null){
                    showToast(responseFail());
                    return;
                }
                if (response.body().getStatus()==0){
                    serviceMoney=response.body().getServiceMoney();
                    if (isAll){
                        BigDecimal accountMoneyBig=new BigDecimal(accountMoney);
                        BigDecimal serviceMoneyBig=new BigDecimal(serviceMoney);
                        omAccount = accountMoneyBig.subtract(serviceMoneyBig);
                        etMoney.setText(omAccount+"");
                        tvCanMoney.setText("服务费"+response.body().getServiceMoney()+"元");
                    }else{
                        tvCanMoney.setText("服务费"+response.body().getServiceMoney()+"元");
                    }
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case 7788:
                try {
                    String bankName = data.getExtras().getString("bankName");
                    String bankNo = data.getExtras().getString("bankNo");
                    String bankLogo = data.getExtras().getString("bankLogo");
                    bankCardId=data.getExtras().getInt("bankCardId");
                    tvBankName.setText(bankName);
                    tvBankNo.setText("尾号"+bankNo.substring(bankNo.length()-4,bankNo.length())+"("+bankName+")");
                    Glide.with(getApplicationContext()).load(bankLogo).into(ivBankLogo);
                }catch (Exception e){
                    showToast(someException());
                }
                break;
        }
    }

    private void toAddWithdrawa() {
        try {
            Call<AddWithdrawa> call=RetrofitService.getInstance().addWithdrawa(token,type,etMoney.getText()+"");
            call.enqueue(new Callback<AddWithdrawa>() {
                @Override
                public void onResponse(Response<AddWithdrawa> response, Retrofit retrofit) {
                    if (response==null){
                        showToast(responseFail());
                        return;
                    }
                    if (response.body().getStatus()==0){
                        try {
                            showToast("提现成功");
                            ActivityManagerUtil.getInstance().finishActivity(WithdrawscashActivity.this);
                        }catch (Exception e){
                            showToast(someException());
                        }
                    }else{
                        showToast(response.body().getMes());
                    }
                    WeiboDialogUtils.closeDialog(mWeiboDialog);
                }

                @Override
                public void onFailure(Throwable throwable) {

                }
            });
        }catch (Exception e){
            showToast(someException());
        }
    }

    /**
     * 登录微信
     */
    private void WXLogin() {
        try {
            WXapi = WXAPIFactory.createWXAPI(this, WX_APP_ID, true);
            WXapi.registerApp(WX_APP_ID);
            SendAuth.Req req = new SendAuth.Req();
            req.scope = "snsapi_userinfo";
            req.state = "wechat_sdk_demo";
            WXapi.sendReq(req);
        }catch (Exception e){
            showToast(someException());
        }
    }
    /**
     * 支付宝登录
     */
    private void getOrderString() {
        openZFBDialog=WeiboDialogUtils.createLoadingDialog(WithdrawscashActivity.this,"打开支付宝...");
        Call<AlipayLoginParam> call= RetrofitService.getInstance().getAlipayLoginParam();
        call.enqueue(new Callback<AlipayLoginParam>() {
            @Override
            public void onResponse(Response<AlipayLoginParam> response, Retrofit retrofit) {
                if (response==null){
                    showToast(responseFail());
                    WeiboDialogUtils.closeDialog(openZFBDialog);
                    return;
                }
                if (response.body().getStatus()==0){
                    String orderString = response.body().getOrderString();
                    WeiboDialogUtils.closeDialog(openZFBDialog);
                    authV2(orderString);
                }else{
                    showToast(response.body().getMes());
                    WeiboDialogUtils.closeDialog(openZFBDialog);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                WeiboDialogUtils.closeDialog(openZFBDialog);
            }
        });
    }
    /**
     * 支付宝账户授权业务
     * @param orderString
     */
    private void authV2(String orderString) {
        if (TextUtils.isEmpty(PID) || TextUtils.isEmpty(APPID)
                || (TextUtils.isEmpty(RSA2_PRIVATE) && TextUtils.isEmpty(RSA_PRIVATE))
                || TextUtils.isEmpty(TARGET_ID)) {
            new AlertDialog.Builder(this).setTitle("警告").setMessage("需要配置PARTNER |APP_ID| RSA_PRIVATE| TARGET_ID")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                        }
                    }).show();
            return;
        }

        /**
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
         *
         * authInfo的获取必须来自服务端；
         */
        boolean rsa2 = (RSA2_PRIVATE.length() > 0);
        Map<String, String> authInfoMap = OrderInfoUtil2_0.buildAuthInfoMap(PID, APPID, TARGET_ID, rsa2);
        String info = OrderInfoUtil2_0.buildOrderParam(authInfoMap);

        String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
        String sign = OrderInfoUtil2_0.getSign(authInfoMap, privateKey, rsa2);
//        final String authInfo = info + "&" + sign;
        final String authInfo = orderString;
        Runnable authRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造AuthTask 对象
                AuthTask authTask = new AuthTask(WithdrawscashActivity.this);
                // 调用授权接口，获取授权结果
                Map<String, String> result = authTask.authV2(authInfo, true);

                Message msg = new Message();
                msg.what = SDK_AUTH_FLAG;
                msg.obj = result;
                zHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread authThread = new Thread(authRunnable);
        authThread.start();
    }

    private void getZFDMsg(String authCode) {
        Call<AlipayUserInfo> call=RetrofitService.getInstance().getAlipayUserInfo(authCode);
        call.enqueue(new Callback<AlipayUserInfo>() {
            @Override
            public void onResponse(Response<AlipayUserInfo> response, Retrofit retrofit) {
                if (response==null){
                    showToast(responseFail());
                    return;
                }
                if (response.body().getStatus()==0){
                    userInfoZFB=response.body().getUserInfo();
                    userInfoZFB.getUser_id();
                    sendData();
                }else{
                    showToast(response.body().getMes());
                }
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    private void sendData() {
        try {
            loginDialog=WeiboDialogUtils.createLoadingDialog(WithdrawscashActivity.this,"绑定中...");
            String rid = JPushInterface.getRegistrationID(getApplicationContext());
            jointype=2;
            joinid=userInfoZFB.getUser_id();
            Call<UserBind> call= RetrofitService.getInstance().userBind(token,jointype, joinid, WXEntryActivity.subjoinid);

            call.enqueue(new Callback<UserBind>() {
                @Override
                public void onResponse(Response<UserBind> response, Retrofit retrofit) {
                    if (response==null){
                        showToast(responseFail());
                        WeiboDialogUtils.closeDialog(loginDialog);
                        return;
                    }
                    if (response.body().getStatus()==0){
                        loadData();
                        loadBindData();
                        showToast("绑定成功");
//                        ActivityManagerUtil.getInstance().finishActivity(BindActivity.this);
//                            finish();
                        WeiboDialogUtils.closeDialog(loginDialog);
                    }else{
                        showToast(response.body().getMes());
                        WeiboDialogUtils.closeDialog(loginDialog);
                    }
                }

                @Override
                public void onFailure(Throwable throwable) {
                    WeiboDialogUtils.closeDialog(loginDialog);
                }
            });
        }catch (Exception e){
//            showToast(e+"");
            showToast(someException());
        }
    }
    public static void sendData1(String tokens, final Context context,int jointypes,String joinids) {
        try {
            Call<UserBind> call= RetrofitService.getInstance().userBind(tokens,jointypes, joinids, WXEntryActivity.subjoinid);

            call.enqueue(new Callback<UserBind>() {
                @Override
                public void onResponse(Response<UserBind> response, Retrofit retrofit) {
                    if (response==null){
                        return;
                    }
                    if (response.body().getStatus()==0){
                        Toast.makeText(context,"绑定成功",Toast.LENGTH_SHORT).show();
//                        ActivityManagerUtil.getInstance().finishActivity(BindActivity.this);
//                            finish();
                    }else{
                        Toast.makeText(context,""+response.body().getMes(),Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Throwable throwable) {
                }
            });
        }catch (Exception e){
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadData();
        loadBindData();
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    public int getContentView() {
        return R.layout.activity_withdrawscash;
    }
}
