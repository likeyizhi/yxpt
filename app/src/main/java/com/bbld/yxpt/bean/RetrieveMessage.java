package com.bbld.yxpt.bean;

/**
 * 忘记和修改密码
 * Created by likey on 2017/7/4.
 */

public class RetrieveMessage {
    /**"status": 0,
     "mes": "发送成功",
     "identity": 2*/
    private int status;
    private String mes;
    private int identity;

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
}
