package com.bbld.yxpt.bean;

/**
 * 支付宝登录授权所需信息
 * Created by likey on 2017/8/17.
 */

public class AlipayLoginParam {
    /**"status": 0,
     "mes": "成功",
     "orderString": "apiname=com.alipay.account.auth&method=alipay.open.auth.sdk.code.get&app_id=2017072707920346&app_name=mc&biz_type=openservice&pid=2088721453488684&product_id=APP_FAST_LOGIN&scope=kuaijie&target_id=20170817143646960&auth_type=AUTHACCOUNT&sign_type=RSA&sign=E2OpwuFmSfRSNPSI3zDHkEObryhH5L7CCjPdR3du11lbDtVHf9Qj6FrZIDXD4pL50rgT0cihmChNTaM0R8r43N0cEEei5ku%2bHgA1G7m9RRN04OP%2fzX7UcWh%2fTjrg%2f732GQQ71R5DG1Lwb0psYBxavaNETSJGO0rGw99B5ACtPmw%3d"*/
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
