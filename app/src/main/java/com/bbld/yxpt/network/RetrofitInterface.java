package com.bbld.yxpt.network;

import com.bbld.yxpt.bean.AddBankCard;
import com.bbld.yxpt.bean.AddPayOrder;
import com.bbld.yxpt.bean.AddWithdrawa;
import com.bbld.yxpt.bean.BankCardRecognition;
import com.bbld.yxpt.bean.BankList;
import com.bbld.yxpt.bean.BuyShopInfo;
import com.bbld.yxpt.bean.BuyShopList;
import com.bbld.yxpt.bean.CityList;
import com.bbld.yxpt.bean.Feedback;
import com.bbld.yxpt.bean.GetAlipayPayParam;
import com.bbld.yxpt.bean.GetBankCardList;
import com.bbld.yxpt.bean.HelpList;
import com.bbld.yxpt.bean.HotSearchList;
import com.bbld.yxpt.bean.Login;
import com.bbld.yxpt.bean.MessageList;
import com.bbld.yxpt.bean.MyOrderReturnInfo;
import com.bbld.yxpt.bean.NickName;
import com.bbld.yxpt.bean.OrderReturnInfo;
import com.bbld.yxpt.bean.Register;
import com.bbld.yxpt.bean.RegisterMessage;
import com.bbld.yxpt.bean.Retrieve;
import com.bbld.yxpt.bean.RetrieveMessage;
import com.bbld.yxpt.bean.ScanShop;
import com.bbld.yxpt.bean.ShopActivityMyOrderList;
import com.bbld.yxpt.bean.ShopActivityOrderList;
import com.bbld.yxpt.bean.ShopInfo;
import com.bbld.yxpt.bean.ShopList;
import com.bbld.yxpt.bean.ShopListPage;
import com.bbld.yxpt.bean.UserInfo;
import com.bbld.yxpt.bean.UserOrderList;
import com.bbld.yxpt.bean.UserReturnOrderList;
import com.bbld.yxpt.bean.VersionAndroid;
import com.bbld.yxpt.bean.WithdrawaAccountInfo;
import com.bbld.yxpt.bean.WithdrawalList;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by young on 2016/11/6.
 */

public interface RetrofitInterface {
    /**
     * 自动更新
     */
    @GET("api/User/GetVersionAndroid")
    Call<VersionAndroid> getVersionAndroid();
    /**
     * 登录
     */
    @GET("api/User/Login")
    Call<Login> login(@Query("acc") String acc, @Query("pwd") String pwd, @Query("plat") String plat, @Query("pushid") String pushid);
    /**
     * 发送短信验证码
     */
    @GET("api/User/SendRegisterMessage")
    Call<RegisterMessage> sendRegisterMessage(@Query("mobile") String mobile);
    /**
     * 注册
     */
    @GET("api/User/Register")
    Call<Register> register(@Query("mobile") String mobile, @Query("indentiy") int indentiy, @Query("vcode") String vcode, @Query("password") String password);
    /**
     * 个人中心
     */
    @GET("api/User/GetUserInfo")
    Call<UserInfo> getUserInfo(@Query("token") String token);
    /**
     * 获取附近店铺列表（不分页）
     */
    @GET("api/User/GetShopList")
    Call<ShopList> getShopList(@Query("x") String x, @Query("y") String y, @Query("key") String key);
    /**
     * 获取附近店铺列表（分页）
     */
    @GET("api/User/GetShopListPage")
    Call<ShopListPage> getShopListPage(@Query("x") String x, @Query("y") String y, @Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize, @Query("key") String key);
    /**
     * 获取店铺详情
     */
    @GET("api/User/GetShopInfo")
    Call<ShopInfo> getShopInfo(@Query("token") String token, @Query("ShopID") String shopId, @Query("x") String x, @Query("y") String y);
    /**
     * 扫码获取店铺信息
     */
    @GET("api/User/ScanShop")
    Call<ScanShop> scanShop(@Query("token") String token, @Query("ActivityCode") String ActivityCode);
    /**
     * 获取用户订单的返还信息
     */
    @GET("api/User/GetOrderReturnInfo")
    Call<OrderReturnInfo> getOrderReturnInfo(@Query("token") String token);
    /**
     * .创建订单信息
     */
    @GET("api/User/AddPayOrder")
    Call<AddPayOrder> addPayOrder(@Query("token") String token, @Query("ActivityCode") String ActivityCode, @Query("EnterAmount") String EnterAmount);
    /**
     * 获取用户已到帐订单列表
     */
    @GET("api/User/GetUserReturnOrderList")
    Call<UserReturnOrderList> getUserReturnOrderList(@Query("token") String token, @Query("pageIndex") int pageIndex);
    /**
     * 获取用户全部订单列表
     */
    @GET("api/User/GetUserOrderList")
    Call<UserOrderList> getUserOrderList(@Query("token") String token , @Query("pageIndex") int pageIndex);
    /**
     * 我的消息
     */
    @GET("api/User/GetMessageList")
    Call<MessageList> getMessageList(@Query("token") String token);
    /**
     * 使用指南
     */
    @GET("api/User/GetHelpList")
    Call<HelpList> getHelpList();
    /**
     * 找回修改密码
     */
    @GET("api/User/Retrieve")
    Call<Retrieve> retrieve(@Query("mobile") String mobile, @Query("indentiy") int indentiy, @Query("vcode") String vcode, @Query("password") String password);
    /**
     * 找回修改密码的发送验证码
     */
    @GET("api/User/SendRetrieveMessage")
    Call<RetrieveMessage> sendRetrieveMessage(@Query("mobile") String mobile);
    /**
     * 意见反馈
     */
    @GET("api/User/Feedback")
    Call<Feedback> feedback(@Query("token") String token, @Query("content") String content, @Query("tel") String tel);
    /**
     * 城市列表
     */
    @GET("api/User/GetCityList")
    Call<CityList> getCityList();
    /**
     * 热搜关键词
     */
    @GET("api/User/GetHotSearchList")
    Call<HotSearchList> getHotSearchList();
    /**
     * 银行卡列表
     */
    @GET("api/User/GetBankCardList")
    Call<GetBankCardList> getBankCardList(@Query("token") String token);
    /**
     * 添加银行卡
     */
    @GET("api/User/AddBankCard")
    Call<AddBankCard> addBankCard(@Query("token") String token, @Query("bankId") int bankId, @Query("name") String name, @Query("cardno") String cardno, @Query("openbank") String openbank);
    /**
     * 卡号识别银行
     */
    @GET("api/User/BankCardRecognition")
    Call<BankCardRecognition> BankCardRecognition(@Query("cardno") String cardno);
    /**
     * 获取银行字典
     */
    @GET("api/User/GetBankList")
    Call<BankList> getBankList();
    /**
     * 默认提现银行卡
     */
    @GET("api/User/GetWithdrawaAccountInfo")
    Call<WithdrawaAccountInfo> getWithdrawaAccountInfo(@Query("token") String token);
    /**
     * 提现
     */
    @GET("api/User/AddWithdrawa")
    Call<AddWithdrawa> addWithdrawa(@Query("token") String token, @Query("BankCardId") int BankCardId, @Query("price") String price);
    /**
     * 支付测试
     */
    @GET("api/User/PayNotify")
    Call<AddWithdrawa> payNotify(@Query("orderNo") String orderNo);
    /**
     * 修改昵称
     */
    @GET("api/User/UpdateUserNickName")
    Call<NickName> updateUserNickName(@Query("token") String token, @Query("NickName") String NickName);
    /**
     * 修改性别
     */
    @GET("api/User/UpdateUserSex")
    Call<NickName> updateUserSex(@Query("token") String token, @Query("sex") String sex);
    /**
     * 获取购买过的店铺
     */
    @GET("api/User/GetBuyShopList")
    Call<BuyShopList> getBuyShopList(@Query("token") String token);
    /**
     * 获取店铺订单信息
     */
    @GET("api/User/GetBuyShopInfo")
    Call<BuyShopInfo> getBuyShopInfo(@Query("token") String token, @Query("shopId") String shopId);
    /**
     * 获取店铺活动订单的待返、已返列表
     */
    @GET("api/User/GetShopActivityOrderList")
    Call<ShopActivityOrderList> getShopActivityOrderList(@Query("token") String token, @Query("shopActivityID") String shopActivityID);
    /**
     * 获取活动中我的订单列表
     */
    @GET("api/User/GetShopActivityMyOrderList")
    Call<ShopActivityMyOrderList> getShopActivityMyOrderList(@Query("token") String token, @Query("shopActivityID") String shopActivityID);
    /**
     * 获取我的订单相关排队订单
     */
    @GET("api/User/GetMyOrderReturnInfo")
    Call<MyOrderReturnInfo> getMyOrderReturnInfo(@Query("token") String token, @Query("OrderID") String OrderID);
    /**
     * 支付宝
     */
    @GET("api/User/GetAlipayPayParam")
    Call<GetAlipayPayParam> getAlipayPayParam(@Query("token") String token, @Query("orderNo") String orderNo);
    /**
     * 已提现金额
     */
    @GET("api/User/GetWithdrawalList")
    Call<WithdrawalList> getWithdrawalList(@Query("token") String token, @Query("pageIndex") int pageIndex);
}
