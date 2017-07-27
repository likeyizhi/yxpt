package com.bbld.yxpt.bean;

import java.util.List;

/**
 * 获取活动中我的订单列表
 * Created by likey on 2017/7/26.
 */

public class ShopActivityMyOrderList {
    /**"status": 0,
     "mes": "操作成功",
     "List": []*/
    private int status;
    private String mes;
    private java.util.List<ShopActivityMyOrderListList> List;

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

    public java.util.List<ShopActivityMyOrderListList> getList() {
        return List;
    }

    public void setList(java.util.List<ShopActivityMyOrderListList> list) {
        List = list;
    }

    public static class ShopActivityMyOrderListList{
        /**"NOID": "17",
         "OrderNo": "00001201707051956278310000100008",
         "EnterAmount": "100.00",
         "AddDate": "2017-07-05 19:56:27",
         "IsReturn": 1,
         "ReturnStatus": "已返还",
         "Sequence": "已返还"*/
        private String NOID;
        private String OrderNo;
        private String EnterAmount;
        private String AddDate;
        private int IsReturn;
        private String ReturnStatus;
        private String Sequence;

        public String getNOID() {
            return NOID;
        }

        public void setNOID(String NOID) {
            this.NOID = NOID;
        }

        public String getOrderNo() {
            return OrderNo;
        }

        public void setOrderNo(String orderNo) {
            OrderNo = orderNo;
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

        public String getSequence() {
            return Sequence;
        }

        public void setSequence(String sequence) {
            Sequence = sequence;
        }
    }
}
