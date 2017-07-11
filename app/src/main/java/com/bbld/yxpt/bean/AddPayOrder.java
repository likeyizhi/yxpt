package com.bbld.yxpt.bean;

/**
 * 创建订单信息
 * Created by likey on 2017/7/3.
 */

public class AddPayOrder {
    /** "status": 0,
     "mes": "操作成功",
     "OrderNo": "00001201707031106454500000800004",
     "PayPrice": "999.00"*/
    private int status;
    private String mes;
    private String OrderNo;
    private String PayPrice;

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

    public String getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(String orderNo) {
        OrderNo = orderNo;
    }

    public String getPayPrice() {
        return PayPrice;
    }

    public void setPayPrice(String payPrice) {
        PayPrice = payPrice;
    }
}
