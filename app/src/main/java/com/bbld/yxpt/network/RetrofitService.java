package com.bbld.yxpt.network;

import com.baidu.platform.comapi.map.B;
import com.bbld.yxpt.base.Constants;
import com.bbld.yxpt.bean.AddBankCard;
import com.bbld.yxpt.bean.AddPayOrder;
import com.bbld.yxpt.bean.AddWithdrawa;
import com.bbld.yxpt.bean.AlipayLoginParam;
import com.bbld.yxpt.bean.AlipayUserInfo;
import com.bbld.yxpt.bean.BankCardRecognition;
import com.bbld.yxpt.bean.BankList;
import com.bbld.yxpt.bean.BuyShopInfo;
import com.bbld.yxpt.bean.BuyShopList;
import com.bbld.yxpt.bean.CityList;
import com.bbld.yxpt.bean.Feedback;
import com.bbld.yxpt.bean.GetAlipayPayParam;
import com.bbld.yxpt.bean.GetBankCardList;
import com.bbld.yxpt.bean.GetMessageCount;
import com.bbld.yxpt.bean.HelpList;
import com.bbld.yxpt.bean.HotSearchList;
import com.bbld.yxpt.bean.JoinLogin;
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
import com.bbld.yxpt.bean.SetMessageRead;
import com.bbld.yxpt.bean.ShopActivityMyOrderList;
import com.bbld.yxpt.bean.ShopActivityOrderList;
import com.bbld.yxpt.bean.ShopInfo;
import com.bbld.yxpt.bean.ShopList;
import com.bbld.yxpt.bean.ShopListPage;
import com.bbld.yxpt.bean.UserInfo;
import com.bbld.yxpt.bean.UserOrderList;
import com.bbld.yxpt.bean.UserReturnOrderList;
import com.bbld.yxpt.bean.VersionAndroid;
import com.bbld.yxpt.bean.WeiXinPayParam;
import com.bbld.yxpt.bean.WithdrawaAccountInfo;
import com.bbld.yxpt.bean.WithdrawalList;

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
     * 自动更新
     */
    public Call<VersionAndroid> getVersionAndroid(){
        return retrofitInterface.getVersionAndroid();
    }
    /**
     * 登录
     */
    public Call<Login> login(String acc, String pwd, String plat, String pushid){
        return retrofitInterface.login(acc, pwd, plat, pushid);
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
     * 注册
     */
    public Call<Register> otherRegister(String mobile, int indentiy, String vcode, String password,
                                        int jointype, String joinid, String nickname, String faceurl, String sex){
        return retrofitInterface.otherRegister(mobile, indentiy, vcode, password, jointype, joinid, nickname, faceurl, sex);
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
    /**
     * 银行卡列表
     */
    public Call<GetBankCardList> getBankCardList(String token){
        return retrofitInterface.getBankCardList(token);
    }
    /**
     * 添加银行卡
     */
    public Call<AddBankCard> addBankCard(String token, int bankId, String name, String cardno, String openbank){
        return retrofitInterface.addBankCard(token, bankId, name, cardno, openbank);
    }
    /**
     * 卡号识别银行
     */
    public Call<BankCardRecognition> BankCardRecognition(String cardno){
        return retrofitInterface.BankCardRecognition(cardno);
    }
    /**
     * 获取银行字典
     */
    public Call<BankList> getBankList(){
        return retrofitInterface.getBankList();
    }
    /**
     * 默认提现银行卡
     */
    public Call<WithdrawaAccountInfo> getWithdrawaAccountInfo(String token){
        return retrofitInterface.getWithdrawaAccountInfo(token);
    }
    /**
     * 提现
     */
    public Call<AddWithdrawa> addWithdrawa(String token, int BankCardId, String price){
        return retrofitInterface.addWithdrawa(token, BankCardId, price);
    }
    /**
     * 支付测试
     */
    public Call<AddWithdrawa> payNotify(String orderNo){
        return retrofitInterface.payNotify(orderNo);
    }
    /**
     * 修改昵称
     */
    public Call<NickName> updateUserNickName(String token, String NickName){
        return retrofitInterface.updateUserNickName(token, NickName);
    }
    /**
     * 修改昵称
     */
    public Call<NickName> updateUserSex(String token, String sex){
        return retrofitInterface.updateUserSex(token, sex);
    }
    /**
     * 获取购买过的店铺
     */
    public Call<BuyShopList> getBuyShopList(String token){
        return retrofitInterface.getBuyShopList(token);
    }
    /**
     * 获取店铺订单信息
     */
    public Call<BuyShopInfo> getBuyShopInfo(String token, String shopId){
        return retrofitInterface.getBuyShopInfo(token, shopId);
    }
    /**
     * 获取店铺活动订单的待返、已返列表
     */
    public Call<ShopActivityOrderList> getShopActivityOrderList(String token, String shopActivityID){
        return retrofitInterface.getShopActivityOrderList(token, shopActivityID);
    }
    /**
     * 获取活动中我的订单列表
     */
    public Call<ShopActivityMyOrderList> getShopActivityMyOrderList(String token, String shopActivityID){
        return retrofitInterface.getShopActivityMyOrderList(token, shopActivityID);
    }
    /**
     * 获取我的订单相关排队订单
     */
    public Call<MyOrderReturnInfo> getMyOrderReturnInfo(String token, String OrderID){
        return retrofitInterface.getMyOrderReturnInfo(token, OrderID);
    }
    /**
     * 支付宝
     */
    public Call<GetAlipayPayParam> getAlipayPayParam(String token, String orderNo){
        return retrofitInterface.getAlipayPayParam(token, orderNo);
    }
    /**
     * 已提现金额
     */
    public Call<WithdrawalList> getWithdrawalList(String token, int pageIndex){
        return retrofitInterface.getWithdrawalList(token, pageIndex);
    }
    /**
     * san
     */
    public Call<JoinLogin> joinLogin(int jointype, String joinid){
        return retrofitInterface.joinLogin(jointype, joinid);
    }
    /**
     * 消息数量
     */
    public Call<GetMessageCount> getMessageCount(String token) {
        return retrofitInterface.getMessageCount(token);
    }

    /**
     * 消息已读
     */
    public Call<SetMessageRead> setMessageRead(String token) {
        return retrofitInterface.setMessageRead(token);
    }

    /**
     * 微信支付
     */
    public Call<WeiXinPayParam> getWeiXinPayParam(String token, String orderNo) {
        return retrofitInterface.getWeiXinPayParam(token,orderNo);
    }
    /**
     * 支付宝登录授权所需信息
     */
    public Call<AlipayLoginParam> getAlipayLoginParam() {
        return retrofitInterface.getAlipayLoginParam();
    }
    /**
     * 支付宝登录授权所需信息
     */
    public Call<AlipayUserInfo> getAlipayUserInfo(String code) {
        return retrofitInterface.getAlipayUserInfo(code);
    }
}
