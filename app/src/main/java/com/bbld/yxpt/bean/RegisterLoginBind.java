package com.bbld.yxpt.bean;

/**
 * Created by dell on 2017/8/18.
 */

public class RegisterLoginBind {
    /**    "status": 0,
     "mes": "操作成功",
     "token": "c3e933a39506466ebd9decd34acaa60a",
     "NickName": "阵雨???",
     "HeadPortrait": "http://mapi.lihuike.com/UpFile/User/0-2ace-20170818112210222.png"
     "*/
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
