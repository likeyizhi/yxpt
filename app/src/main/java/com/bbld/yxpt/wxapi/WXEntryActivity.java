package com.bbld.yxpt.wxapi;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;


import com.bbld.yxpt.activity.LoginActivity;
import com.bbld.yxpt.activity.RegisterActivity;
import com.bbld.yxpt.bean.JoinLogin;
import com.bbld.yxpt.bean.WXLoginBackMsg;
import com.bbld.yxpt.loadingdialog.WeiboDialogUtils;
import com.bbld.yxpt.network.RetrofitService;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONObject;

import java.net.URLEncoder;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by dell on 2017/8/4.
 */

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private IWXAPI api;
    private BaseResp resp = null;
    private String WX_APP_ID = "wxb32b83a9fe661456";
    // 获取第一步的code后，请求以下链接获取access_token
    private String GetCodeRequest = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
    // 获取用户个人信息
    private String GetUserInfo = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID";
    private String WX_APP_SECRET = "d357f8a2841b33d70878ffe0ae22e4a6";/*"创建应用后得到的APP_SECRET"*/
    private String otherLogin;
    private static final String TOKEN=null;
    private Dialog openWXDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, WX_APP_ID, false);
//        openWXDialog= WeiboDialogUtils.createLoadingDialog(WXEntryActivity.this,"打开微信...");
        api.handleIntent(getIntent(), this);
    }

    // 微信发送请求到第三方应用时，会回调到该方法
    @Override
    public void onReq(BaseReq req) {
        finish();
    }

    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    @Override
    public void onResp(BaseResp resp) {
        WeiboDialogUtils.closeDialog(LoginActivity.openWXDialog);
        String result = "";
        if (resp != null) {
            resp = resp;
        }
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                result = "发送成功";
//                Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
                String code = ((SendAuth.Resp) resp).code;

            /*
             * 将你前面得到的AppID、AppSecret、code，拼接成URL 获取access_token等等的信息(微信)
             */
                String get_access_token = getCodeRequest(code);
                AsyncHttpClient client = new AsyncHttpClient();
                client.post(get_access_token, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
//                        Toast.makeText(WXEntryActivity.this, response+"", Toast.LENGTH_LONG).show();
                        // TODO Auto-generated method stub
                        super.onSuccess(statusCode, response);
                        try {
                            if (!response.equals("")) {
                                String access_token = response
                                        .getString("access_token");
                                String openid = response.getString("openid");
                                String get_user_info_url = getUserInfo(
                                        access_token, openid);
                                getUserInfo(get_user_info_url);
                            }

                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                });

                finish();
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = "操作取消";
                Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
                finish();
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result = "操作被拒绝";
                Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
                finish();
                break;
            default:
                result = "操作失败，请重试";
                Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }

    /**
     * 通过拼接的用户信息url获取用户信息
     *
     * @param user_info_url
     */
    private void getUserInfo(String user_info_url) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(user_info_url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                // TODO Auto-generated method stub
                super.onSuccess(statusCode, response);
                try {
                    System.out.println("获取用户信息:" + response);
                    if (!response.equals("")) {
                        Gson gson=new Gson();
                        final WXLoginBackMsg wxMsg=gson.fromJson(response+"",WXLoginBackMsg.class);
                        Call<JoinLogin> call= RetrofitService.getInstance().joinLogin(1, wxMsg.getUnionid());
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
//                                    Toast.makeText(WXEntryActivity.this,"0"+response.body().getMes(),Toast.LENGTH_SHORT).show();
                                    LoginActivity.loginActivity.finish();
                                    finish();
                                }else if(response.body().getStatus()==10){
                                    Toast.makeText(WXEntryActivity.this,"该微信号暂未注册，请先注册",Toast.LENGTH_SHORT).show();
                                    Intent intent=new Intent(WXEntryActivity.this, RegisterActivity.class);
                                    Bundle bundle=new Bundle();
                                    bundle.putInt("jointype", 1);
                                    bundle.putString("joinid", wxMsg.getUnionid());
                                    bundle.putString("nickname", wxMsg.getNickname());
                                    bundle.putString("faceurl", wxMsg.getHeadimgurl());
                                    bundle.putString("sex", wxMsg.getSex()==1?"男":"女");
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                    finish();
                                }else{//response.body().getStatus()==1
                                    Toast.makeText(WXEntryActivity.this, "数据获取失败，请重试", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Throwable throwable) {

                            }
                        });
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
        finish();
    }

    /**
     * 获取access_token的URL（微信）
     *
     * @param code
     *            授权时，微信回调给的
     * @return URL
     */
    private String getCodeRequest(String code) {
        String result = null;
        GetCodeRequest = GetCodeRequest.replace("APPID",
                urlEnodeUTF8(WX_APP_ID));
        GetCodeRequest = GetCodeRequest.replace("SECRET",
                urlEnodeUTF8(WX_APP_SECRET));
        GetCodeRequest = GetCodeRequest.replace("CODE", urlEnodeUTF8(code));
        result = GetCodeRequest;
        return result;
    }

    /**
     * 获取用户个人信息的URL（微信）
     *
     * @param access_token
     *            获取access_token时给的
     * @param openid
     *            获取access_token时给的
     * @return URL
     */
    private String getUserInfo(String access_token, String openid) {
        String result = null;
        GetUserInfo = GetUserInfo.replace("ACCESS_TOKEN",
                urlEnodeUTF8(access_token));
        GetUserInfo = GetUserInfo.replace("OPENID", urlEnodeUTF8(openid));
        result = GetUserInfo;
        return result;
    }

    private String urlEnodeUTF8(String str) {
        String result = str;
        try {
            result = URLEncoder.encode(str, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}