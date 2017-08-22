package com.bbld.yxpt.bean;

/**
 * 服务费
 * Created by likey on 2017/8/18.
 */

public class ServiceMoney {
    /**"status": 0,
     "mes": "操作成功",
     "serviceMoney": "1.00"*/
    private int status;
    private String mes;
    private String serviceMoney;

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

    public String getServiceMoney() {
        return serviceMoney;
    }

    public void setServiceMoney(String serviceMoney) {
        this.serviceMoney = serviceMoney;
    }
}
