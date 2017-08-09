package com.bbld.yxpt.activity;

import android.app.Dialog;
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

import com.bbld.yxpt.R;
import com.bbld.yxpt.base.BaseActivity;
import com.bbld.yxpt.bean.JoinLogin;
import com.bbld.yxpt.bean.Login;
import com.bbld.yxpt.loadingdialog.WeiboDialogUtils;
import com.bbld.yxpt.network.RetrofitService;
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

                WXLogin();
            }
        });
        //登录
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
            }
        });
        //创建帐号
        tvCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readyGo(RegisterActivity.class);
            }
        });
        //忘记密码
        tvForgetPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putString("isForget","Forget");
                readyGo(UpdatePasswordActivity.class, bundle);
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tencent != null) {
                    //注销登录
                    tencent.logout(LoginActivity.this);
                }
                ActivityManagerUtil.getInstance().finishActivity(LoginActivity.this);
            }
        });
        ivQQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openQQDialog=WeiboDialogUtils.createLoadingDialog(LoginActivity.this,"打开QQ...");
                tencent = Tencent.createInstance("1106314650", getApplicationContext());
                if (!tencent.isSessionValid()){
                    tencent.login(LoginActivity.this, "all", loginListener);
                }
            }
        });
    }

    IUiListener loginListener=new BaseUIListener(){
        @Override
        protected void doComplete(JSONObject o) {
            super.doComplete(o);
            initOpenidAndToken(o);
            updateUserInfo();
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
        }
    }

    private void updateUserInfo() {
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
        }
    }

    private String nickname;
    private String faceurl;
    private String gender;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
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
            Verification(openId,nickname,gender,faceurl);
        }

    };

    private void Verification(final String openid, final String nickName, final String gender, final String faceurl) {
        Call<JoinLogin> call= RetrofitService.getInstance().joinLogin(2, openid);
        call.enqueue(new Callback<JoinLogin>() {
            @Override
            public void onResponse(Response<JoinLogin> response, Retrofit retrofit) {
                if (response==null){
                    return;
                }
                if (response.body().getStatus()==0){
                    //保存Token
                    SharedPreferences shared=getSharedPreferences("YXToken",MODE_PRIVATE);
                    SharedPreferences.Editor editor=shared.edit();
                    editor.putString(TOKEN,response.body().getToken());
                    editor.putString("HeadPortrait",response.body().getHeadPortrait());
                    editor.commit();
                    finish();
                }else if(response.body().getStatus()==10){
                    Toast.makeText(LoginActivity.this,"该微QQ暂未注册，请先注册",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(LoginActivity.this, RegisterActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putInt("jointype", 2);
                    bundle.putString("joinid", openid);
                    bundle.putString("nickname", nickName);
                    bundle.putString("faceurl", faceurl);
                    bundle.putString("sex", gender);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                }else{//response.body().getStatus()==1
                    Toast.makeText(LoginActivity.this, "数据获取失败，请重试", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
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
        tencent.onActivityResultData(requestCode, resultCode, data, loginListener);
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
        WXapi = WXAPIFactory.createWXAPI(this, WX_APP_ID, true);
        openWXDialog=WeiboDialogUtils.createLoadingDialog(LoginActivity.this,"打开微信...");
        WXapi.registerApp(WX_APP_ID);
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "wechat_sdk_demo";
        WXapi.sendReq(req);
    }
    @Override
    protected void getBundleExtras(Bundle extras) {

    }
    @Override
    public int getContentView() {
        return R.layout.activity_login;
    }
}
