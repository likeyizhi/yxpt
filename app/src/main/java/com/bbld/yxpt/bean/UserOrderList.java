package com.bbld.yxpt.bean;

import java.util.List;

/**
 * 获取用户全部订单列表
 * Created by likey on 2017/7/3.
 */

public class UserOrderList {
    /**"status": 0,
     "mes": "操作成功",
     "count": 1,
     "total": "2.00",
     "list": []*/
    private int status;
    private String mes;
    private int count;
    private String total;
    private List<UserOrderListlist> list;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<UserOrderListlist> getList() {
        return list;
    }

    public void setList(List<UserOrderListlist> list) {
        this.list = list;
    }

    public static class UserOrderListlist{
        /**"NOID": "10",
         "NickName": " 张立武",
         "HeadPortrait": "http://wx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTIkqyiaicz1CIugP37VSJgZythN5wtrrFvR7NDkPicDER9frJAZialdn4NFcFnBVhiaNaop7lgLH5xutyg/0",
         "ActivityTitle": "The ",
         "OrderNo": "00001201706272015555710000100001",
         "ActivityDiscount": "0.80",
         "EnterAmount": "2.00",
         "AddDate": "2017-06-27 20:15:55",
         "IsReturn": 1,
         "ReturnStatus": "已返还"*/
        private String NOID;
        private String NickName;
        private String HeadPortrait;
        private String ActivityTitle;
        private String OrderNo;
        private String ActivityDiscount;
        private String EnterAmount;
        private String AddDate;
        private String ShopName;
        private String ShopImg;
        private int IsReturn;
        private String ReturnStatus;

        public String getShopName() {
            return ShopName;
        }

        public void setShopName(String shopName) {
            ShopName = shopName;
        }

        public String getShopImg() {
            return ShopImg;
        }

        public void setShopImg(String shopImg) {
            ShopImg = shopImg;
        }

        public String getNOID() {
            return NOID;
        }

        public void setNOID(String NOID) {
            this.NOID = NOID;
        }

        public String getNickName() {
            return NickName;
        }

        public void setNickName(String nickName) {
            NickName = nickName;
        }

        public String getHeadPortrait() {
            return HeadPortrait;
        }

        public void setHeadPortrait(String headPortrait) {
            HeadPortrait = headPortrait;
        }

        public String getActivityTitle() {
            return ActivityTitle;
        }

        public void setActivityTitle(String activityTitle) {
            ActivityTitle = activityTitle;
        }

        public String getOrderNo() {
            return OrderNo;
        }

        public void setOrderNo(String orderNo) {
            OrderNo = orderNo;
        }

        public String getActivityDiscount() {
            return ActivityDiscount;
        }

        public void setActivityDiscount(String activityDiscount) {
            ActivityDiscount = activityDiscount;
        }

        public String getEnterAmount() {
            return EnterAmount;
        }

        public void setEnterAmount(String enterAmount) {
            EnterAmount = enterAmount;
        }

        public String getAddDate() {
            return AddDate;
        }

        public void setAddDate(String addDate) {
            AddDate = addDate;
        }

        public int getIsReturn() {
            return IsReturn;
        }

        public void setIsReturn(int isReturn) {
            IsReturn = isReturn;
        }

        public String getReturnStatus() {
            return ReturnStatus;
        }

        public void setReturnStatus(String returnStatus) {
            ReturnStatus = returnStatus;
        }
    }
}
