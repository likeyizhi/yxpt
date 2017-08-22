package com.bbld.yxpt.bean;

/**
 * Created by dell on 2017/8/18.
 */

public class GetUserBindInfo {
    /**
     * "status": 0,
     "mes": "操作成功",
     "WeiXin": 1,
     "Alipay": 1
     */
    private int status;
    private String mes;
    private int WeiXin;
    private int Alipay;

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

    public int getWeiXin() {
        return WeiXin;
    }

    public void setWeiXin(int weiXin) {
        WeiXin = weiXin;
    }

    public int getAlipay() {
        return Alipay;
    }

    public void setAlipay(int alipay) {
        Alipay = alipay;
    }
}
