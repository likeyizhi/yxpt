package com.bbld.yxpt.bean;

/**
 * Created by dell on 2017/8/18.
 */

public class SendLoginMessage {
/**  "status": 0,
             "mes": "发送成功",
             "identity": 153,
             "isLogin": 1*/
private int status;
    private String mes;
    private int identity;
    private int isLogin;

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

    public int getIdentity() {
        return identity;
    }

    public void setIdentity(int identity) {
        this.identity = identity;
    }

    public int getIsLogin() {
        return isLogin;
    }

    public void setIsLogin(int isLogin) {
        this.isLogin = isLogin;
    }
}
