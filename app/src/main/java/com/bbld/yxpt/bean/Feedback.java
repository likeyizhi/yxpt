package com.bbld.yxpt.bean;

/**
 * 意见反馈
 * Created by likey on 2017/7/6.
 */

public class Feedback {
    /**  {
     "status": 0,
     "mes": "操作成功"
     }*/
    private int status;
    private String mes;

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
}
