package com.bbld.yxpt.bean;

/**
 * 支付宝
 * Created by dell on 2017/7/28.
 */

public class GetAlipayPayParam {
    /** "status": 0,
     "mes": "成功",
     "orderString": */
    private int status;
    private String mes;
    private String orderString;

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

    public String getOrderString() {
        return orderString;
    }

    public void setOrderString(String orderString) {
        this.orderString = orderString;
    }
}
