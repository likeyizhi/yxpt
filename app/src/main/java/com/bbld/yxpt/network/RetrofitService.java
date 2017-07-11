package com.bbld.yxpt.network;

import com.bbld.yxpt.base.Constants;
import com.bbld.yxpt.bean.AddPayOrder;
import com.bbld.yxpt.bean.CityList;
import com.bbld.yxpt.bean.Feedback;
import com.bbld.yxpt.bean.HelpList;
import com.bbld.yxpt.bean.HotSearchList;
import com.bbld.yxpt.bean.Login;
import com.bbld.yxpt.bean.MessageList;
import com.bbld.yxpt.bean.OrderReturnInfo;
import com.bbld.yxpt.bean.Register;
import com.bbld.yxpt.bean.RegisterMessage;
import com.bbld.yxpt.bean.Retrieve;
import com.bbld.yxpt.bean.RetrieveMessage;
import com.bbld.yxpt.bean.ScanShop;
import com.bbld.yxpt.bean.ShopInfo;
import com.bbld.yxpt.bean.ShopList;
import com.bbld.yxpt.bean.ShopListPage;
import com.bbld.yxpt.bean.UserInfo;
import com.bbld.yxpt.bean.UserOrderList;
import com.bbld.yxpt.bean.UserReturnOrderList;
import com.bbld.yxpt.bean.VersionAndroid;

import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by young on 2016/11/6.
 */

public class RetrofitService {
    private static RetrofitService retrofitService = new RetrofitService();
    private static RetrofitInterface retrofitInterface;

    private RetrofitService() {
        initRetrofit();
    }

    public static RetrofitService getInstance() {
        if (retrofitService == null) {
            retrofitService = new RetrofitService();
        }
        return retrofitService;
    }

    private void initRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);
    }

    /**
     * 测试
     */
    public Call<VersionAndroid> getVersionAndroid(){
        return retrofitInterface.getVersionAndroid();
    }
    /**
     * 登录
     */
    public Call<Login> login(String acc, String pwd){
        return retrofitInterface.login(acc, pwd);
    }
    /**
     * 发送短信验证码
     */
    public Call<RegisterMessage> sendRegisterMessage(String mobile){
        return retrofitInterface.sendRegisterMessage(mobile);
    }
    /**
     * 注册
     */
    public Call<Register> register(String mobile, int indentiy, String vcode, String password){
        return retrofitInterface.register(mobile, indentiy, vcode, password);
    }
    /**
     * 个人中心
     */
    public Call<UserInfo> getUserInfo(String token){
        return retrofitInterface.getUserInfo(token);
    }
    /**
     * 获取附近店铺列表（不分页）
     */
    public Call<ShopList> getShopList(String x, String y, String key){
        return retrofitInterface.getShopList(x, y, key);
    }
    /**
     * 获取附近店铺列表（分页）
     */
    public Call<ShopListPage> getShopListPage(String x, String y, int pageIndex, int pageSize, String key){
        return retrofitInterface.getShopListPage(x, y, pageIndex, pageSize, key);
    }
    /**
     * 获取店铺详情
     */
    public Call<ShopInfo> getShopInfo(String token,String shopId, String x, String y){
        return retrofitInterface.getShopInfo(token, shopId, x, y);
    }
    /**
     * 扫码获取店铺信息
     */
    public Call<ScanShop> scanShop(String token, String ActivityCode){
        return retrofitInterface.scanShop(token, ActivityCode);
    }
    /**
     * 扫码获取店铺信息
     */
    public Call<OrderReturnInfo> getOrderReturnInfo(String token){
        return retrofitInterface.getOrderReturnInfo(token);
    }
    /**
     * 创建订单信息
     */
    public Call<AddPayOrder> addPayOrder(String token, String ActivityCode, String EnterAmount){
        return retrofitInterface.addPayOrder(token, ActivityCode, EnterAmount);
    }
    /**
     * 获取用户已到帐订单列表
     */
    public Call<UserReturnOrderList> getUserReturnOrderList(String token, int pageIndex){
        return retrofitInterface.getUserReturnOrderList(token, pageIndex);
    }
    /**
     * 获取用户全部订单列表
     */
    public Call<UserOrderList> getUserOrderList(String token, int pageIndex){
        return retrofitInterface.getUserOrderList(token, pageIndex);
    }
    /**
     * 我的消息
     */
    public Call<MessageList> getMessageList(String token){
        return retrofitInterface.getMessageList(token);
    }
    /**
     * 使用指南
     */
    public Call<HelpList> getHelpList(){
        return retrofitInterface.getHelpList();
    }
    /**
     * 找回修改密码
     */
    public Call<Retrieve> retrieve(String mobile, int indentiy, String vcode, String password){
        return retrofitInterface.retrieve(mobile, indentiy, vcode, password);
    }
    /**
     * 找回修改密码的发送验证码
     */
    public Call<RetrieveMessage> sendRetrieveMessage(String mobile){
        return retrofitInterface.sendRetrieveMessage(mobile);
    }
    /**
     * 意见反馈
     */
    public Call<Feedback> feedback(String token, String content, String tel){
        return retrofitInterface.feedback(token, content, tel);
    }
    /**
     * 城市列表
     */
    public Call<CityList> getCityList(){
        return retrofitInterface.getCityList();
    }
    /**
     * 热搜关键词
     */
    public Call<HotSearchList> getHotSearchList(){
        return retrofitInterface.getHotSearchList();
    }
}