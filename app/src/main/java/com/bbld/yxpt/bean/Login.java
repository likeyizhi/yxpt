package com.bbld.yxpt.bean;

/**
 * 登录
 * Created by likey on 2017/7/1.
 */

public class Login {
    /**"status": 0,
     "mes": "登录成功",
     "token": "f1408484c2bf410aa8db01cd503dcbbf",
     "NickName": "返客1001706031",
     "HeadPortrait": ""*/
    private int status;
    private String mes;
    private String token;
    private String NickName;
    private String HeadPortrait;

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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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
}
