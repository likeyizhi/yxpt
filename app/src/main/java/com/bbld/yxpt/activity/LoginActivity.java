package com.bbld.yxpt.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.AuthTask;
import com.bbld.yxpt.R;
import com.bbld.yxpt.base.BaseActivity;
import com.bbld.yxpt.bean.AlipayLoginParam;
import com.bbld.yxpt.bean.AlipayUserInfo;
import com.bbld.yxpt.bean.JoinLogin;
import com.bbld.yxpt.bean.Login;
import com.bbld.yxpt.loadingdialog.WeiboDialogUtils;
import com.bbld.yxpt.network.RetrofitService;
import com.bbld.yxpt.utils.AuthResult;
import com.bbld.yxpt.utils.OrderInfoUtil2_0;
import com.bbld.yxpt.utils.PayResult;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.wuxiaolong.androidutils.library.ActivityManagerUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import butterknife.BindView;
import cn.jpush.android.api.JPushInterface;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * 登录
 * Created by likey on 2017/7/1.
 */

public class LoginActivity extends BaseActivity{
    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.etAcc)
    EditText etAcc;
    @BindView(R.id.etPwd)
    EditText etPwd;
    @BindView(R.id.tvForgetPwd)
    TextView tvForgetPwd;
    @BindView(R.id.btnLogin)
    Button btnLogin;
    @BindView(R.id.ivQQ)
    ImageView ivQQ;
    @BindView(R.id.ivWX)
    ImageView ivWX;
    @BindView(R.id.tvCreate)
    TextView tvCreate;
    @BindView(R.id.ivZFB)
    ImageView ivZFB;

    // 微信登录
    private static IWXAPI WXapi;
    private String WX_APP_ID = "wxb32b83a9fe661456";
    private String jointype;
    private String joinid;
    private String acc;
    private String pwd;
    private static final String TOKEN=null;
    private SharedPreferences.Editor editorAP;
    private Tencent tencent;
    public static LoginActivity loginActivity;
    private UserInfo userInfo;
    private IUiListener userInfoListener;
    private UserInfo mInfo;
    private String openId;
    private Dialog loginDialog;
    public static Dialog openWXDialog;
    private Dialog openQQDialog;
    private Dialog openZFBDialog;
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

    private AlipayUserInfo.AlipayUserInfoUserInfo userInfoZFB;
    private String zfbGender;
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
                        Toast.makeText(LoginActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(LoginActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(LoginActivity.this,
                                "授权成功\n"/* + String.format("authCode:%s", authResult.getAuthCode())*/, Toast.LENGTH_SHORT)
                                .show();
                        getZFDMsg(authResult.getAuthCode());
                    } else {
                        // 其他状态值则为授权失败
                        Toast.makeText(LoginActivity.this,
                                "授权失败" /*+ String.format("authCode:%s", authResult.getAuthCode())*/, Toast.LENGTH_SHORT).show();

                    }
                    break;
                }
                default:
                    break;
            }
        }
    };

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
                    if (userInfoZFB.getGender().equals("m")){
                        zfbGender = "男";
                    }else{
                        zfbGender = "女";
                    }
                    Verification(userInfoZFB.getUser_id(),
                            userInfoZFB.getNick_name(),
                            zfbGender,userInfoZFB.getAvatar(),2);
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
        loginActivity=this;
        if (tencent != null) {
            //注销登录
            tencent.logout(LoginActivity.this);
        }
        SharedPreferences sharedAP=getSharedPreferences("YXAP",MODE_PRIVATE);
        editorAP=sharedAP.edit();
        setListeners();
    }

    private void setListeners() {
        ivWX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    WXLogin();
                }catch (Exception e){
                    showToast(someException());
                }
            }
        });
        //登录
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    loginDialog=WeiboDialogUtils.createLoadingDialog(LoginActivity.this,"登录中...");
                    String rid = JPushInterface.getRegistrationID(getApplicationContext());
                    acc=etAcc.getText().toString().trim();
                    pwd=etPwd.getText().toString().trim();
                    Call<Login> call= RetrofitService.getInstance().login(acc,pwd, "android", rid);
                    call.enqueue(new Callback<Login>() {
                        @Override
                        public void onResponse(Response<Login> response, Retrofit retrofit) {
                            if (response==null){
                                showToast(responseFail());
                                WeiboDialogUtils.closeDialog(loginDialog);
                                return;
                            }
                            if (response.body().getStatus()==0){
                                //保存Token
                                SharedPreferences shared=getSharedPreferences("YXToken",MODE_PRIVATE);
                                SharedPreferences.Editor editor=shared.edit();
                                editor.putString(TOKEN,response.body().getToken());
                                editor.putString("HeadPortrait",response.body().getHeadPortrait());
                                editor.commit();
                                //保存帐号密码
                                editorAP.putString("YXACC",acc);
                                editorAP.putString("YXPWD",pwd);
                                editorAP.commit();

                                showToast("登录成功");
                                ActivityManagerUtil.getInstance().finishActivity(LoginActivity.this);
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
                    showToast(someException());
                }
            }
        });
        //创建帐号
        tvCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    readyGo(RegisterActivity.class);
                }catch (Exception e){
                    showToast(someException());
                }
            }
        });
        //忘记密码
        tvForgetPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Bundle bundle=new Bundle();
                    bundle.putString("isForget","Forget");
                    readyGo(UpdatePasswordActivity.class, bundle);
                }catch (Exception e){
                    showToast(someException());
                }
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (tencent != null) {
                        //注销登录
                        tencent.logout(LoginActivity.this);
                    }
                    ActivityManagerUtil.getInstance().finishActivity(LoginActivity.this);
                }catch (Exception e){
                    showToast(someException());
                }
            }
        });
        ivQQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    openQQDialog=WeiboDialogUtils.createLoadingDialog(LoginActivity.this,"打开QQ...");
                    tencent = Tencent.createInstance("1106314650", getApplicationContext());
                    if (!tencent.isSessionValid()){
                        tencent.login(LoginActivity.this, "all", loginListener);
                    }
                }catch (Exception e){
                    showToast(someException());
                }
            }
        });
        ivZFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getOrderString();
            }
        });
    }

    private void getOrderString() {
        openZFBDialog=WeiboDialogUtils.createLoadingDialog(LoginActivity.this,"打开支付宝...");
        Call<AlipayLoginParam> call=RetrofitService.getInstance().getAlipayLoginParam();
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

    IUiListener loginListener=new BaseUIListener(){
        @Override
        protected void doComplete(JSONObject o) {
            super.doComplete(o);
            try {
                initOpenidAndToken(o);
                updateUserInfo();
            }catch (Exception e){
                showToast(someException());
            }
        }
    };

    private void initOpenidAndToken(JSONObject jsonObject) {
        try {
            String token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
            String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
            openId = jsonObject.getString(Constants.PARAM_OPEN_ID);
            if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires)
                    && !TextUtils.isEmpty(openId)) {
                tencent.setAccessToken(token, expires);
                tencent.setOpenId(openId);
            }
        } catch(Exception e) {
            showToast(someException());
        }
    }

    private void updateUserInfo() {
        try {
            if (tencent != null && tencent.isSessionValid()) {
                IUiListener listener = new IUiListener() {
                    @Override
                    public void onError(UiError e) {
                        showToast(e.errorMessage+e.errorCode);
                    }
                    @Override
                    public void onComplete(Object response) {
                        Message msg = new Message();
                        msg.obj = response;
                        msg.what = 0;
                        mHandler.sendMessage(msg);
                    }
                    @Override
                    public void onCancel() {
                        if (tencent != null) {
                            //注销登录
                            tencent.logout(LoginActivity.this);
                        }
                        showToast("操作取消");
                    }
                };
                mInfo = new UserInfo(this, tencent.getQQToken());
                mInfo.getUserInfo(listener);
            } else {
                //未获取
                showToast(someException());
            }
        }catch (Exception e){
            showToast(someException());
        }
    }

    private String nickname;
    private String faceurl;
    private String gender;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                if (msg.what == 0) {
                    JSONObject response = (JSONObject) msg.obj;
                    if (response.has("nickname")) {
                        try {
                            nickname = response.getString("nickname");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    if (response.has("figureurl")) {
                        try {
                            faceurl = response.getString("figureurl_qq_2");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    if (response.has("gender")) {
                        try {
                            gender = response.getString("gender");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
//            showToast(nickname+","+faceurl+","+gender);
                Verification(openId,nickname,gender,faceurl,2);
            }catch (Exception e){
                showToast(someException());
            }
        }

    };

    private void Verification(final String openid, final String nickName, final String gender, final String faceurl, final int jointype) {
        try {
            Call<JoinLogin> call= RetrofitService.getInstance().joinLogin(jointype, openid);
            call.enqueue(new Callback<JoinLogin>() {
                @Override
                public void onResponse(Response<JoinLogin> response, Retrofit retrofit) {
                    if (response==null){
                        return;
                    }
                    if (response.body().getStatus()==0){
                        try {
                            //保存Token
                            SharedPreferences shared=getSharedPreferences("YXToken",MODE_PRIVATE);
                            SharedPreferences.Editor editor=shared.edit();
                            editor.putString(TOKEN,response.body().getToken());
                            editor.putString("HeadPortrait",response.body().getHeadPortrait());
                            editor.commit();
                            finish();
                        }catch (Exception e){
                            showToast(someException());
                        }
                    }else if(response.body().getStatus()==10){
                        try {
                            Toast.makeText(LoginActivity.this,"暂未注册，请先注册",Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(LoginActivity.this, RegisterActivity.class);
                            Bundle bundle=new Bundle();
                            bundle.putInt("jointype", jointype);
                            bundle.putString("joinid", openid);
                            bundle.putString("nickname", nickName);
                            bundle.putString("faceurl", faceurl);
                            bundle.putString("sex", gender);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            finish();
                        }catch (Exception e){
                            showToast(someException());
                        }
                    }else{//response.body().getStatus()==1
                        Toast.makeText(LoginActivity.this, "数据获取失败，请重试", Toast.LENGTH_LONG).show();
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

    private class BaseUIListener implements IUiListener{
        /**
         * 返回json数据样例
         *
         * {"ret":0,"pay_token":"D3D678728DC580FBCDE15722B72E7365",
         * "pf":"desktop_m_qq-10000144-android-2002-",
         * "query_authority_cost":448,
         * "authority_cost":-136792089,
         * "openid":"015A22DED93BD15E0E6B0DDB3E59DE2D",
         * "expires_in":7776000,
         * "pfkey":"6068ea1c4a716d4141bca0ddb3df1bb9",
         * "msg":"",
         * "access_token":"A2455F491478233529D0106D2CE6EB45",
         * "login_cost":499}
         */
        @Override
        public void onComplete(Object o) {
            if (null == o){
                showToast("登录失败，请重试");
                return;
            }
            JSONObject jsonObject=(JSONObject)o;
            if (null!=jsonObject && jsonObject.length()==0){
                showToast("登录失败，请重试");
                return;
            }
            try {
                int ret = jsonObject.getInt("ret");
                System.out.println("json=" + String.valueOf(jsonObject));
                if (ret == 0) {
//                    Toast.makeText(LoginActivity.this, "登录成功",
//                            Toast.LENGTH_LONG).show();
                    String openID = jsonObject.getString("openid");
                    String accessToken = jsonObject.getString("access_token");
                    String expires = jsonObject.getString("expires_in");
                    tencent.setOpenId(openID);
                    tencent.setAccessToken(accessToken, expires);
                }
                WeiboDialogUtils.closeDialog(openQQDialog);
            } catch (Exception e) {
                // TODO: handle exception
            }
            doComplete((JSONObject)o);
        }

        protected void doComplete(JSONObject o) {
//            showToast("doComplete"+o);
        }

        @Override
        public void onError(UiError uiError) {
            WeiboDialogUtils.closeDialog(openQQDialog);
            showToast(uiError.errorMessage+","+uiError.errorCode);
        }

        @Override
        public void onCancel() {
            WeiboDialogUtils.closeDialog(openQQDialog);
            showToast("取消登录");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            tencent.onActivityResultData(requestCode, resultCode, data, loginListener);
        }catch (Exception e){
            showToast(someException());
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
            if (tencent != null) {
                //注销登录
                tencent.logout(LoginActivity.this);
            }
            finish();
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        if (tencent != null) {
            //注销登录
            tencent.logout(LoginActivity.this);
        }
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (tencent != null) {
            //注销登录
            tencent.logout(LoginActivity.this);
        }
    }

    /**
     * 登录微信
     */
    private void WXLogin() {
        try {
            WXapi = WXAPIFactory.createWXAPI(this, WX_APP_ID, true);
            openWXDialog=WeiboDialogUtils.createLoadingDialog(LoginActivity.this,"打开微信...");
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
                AuthTask authTask = new AuthTask(LoginActivity.this);
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

    @Override
    protected void getBundleExtras(Bundle extras) {

    }
    @Override
    public int getContentView() {
        return R.layout.activity_login;
    }
}
