package com.bbld.yxpt.bean;

import java.util.List;

/**
 * 获取店铺活动订单的待返、已返列表
 * Created by likey on 2017/7/26.
 */

public class ShopActivityOrderList {
    /**"status": 0,
     "mes": "操作成功",
     "ReturnList": []*/
    private int status;
    private String mes;
    private List<ShopActivityOrderListReturnList> ReturnList;
    private List<ShopActivityOrderListNextList> NextList;

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

    public List<ShopActivityOrderListReturnList> getReturnList() {
        return ReturnList;
    }

    public void setReturnList(List<ShopActivityOrderListReturnList> returnList) {
        ReturnList = returnList;
    }

    public List<ShopActivityOrderListNextList> getNextList() {
        return NextList;
    }

    public void setNextList(List<ShopActivityOrderListNextList> nextList) {
        NextList = nextList;
    }

    public static class ShopActivityOrderListReturnList{
        /**"NOID": "11",
         "NickName": " Toni",
         "HeadPortrait": "http://wx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTKI4b5WtMiarqAvedbIFldPCEfcaC4kTR0CV6mRo3wxJVa8CWYd09zFJGyRjyeLTKLr4iap3AKyNKCQ/0",
         "ActivityTitle": "就是The ",
         "OrderNo": "00001201706272026160150000100002",
         "ActivityDiscount": "0.80",
         "EnterAmount": "1.00",
         "AddDate": "2017-06-27 20:26:16",
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
        private int IsReturn;
        private String ReturnStatus;

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
    public static class ShopActivityOrderListNextList{
        private String NOID;
        private String NickName;
        private String HeadPortrait;
        private String ActivityTitle;
        private String OrderNo;
        private String ActivityDiscount;
        private String EnterAmount;
        private String AddDate;
        private int IsReturn;
        private String ReturnStatus;

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
