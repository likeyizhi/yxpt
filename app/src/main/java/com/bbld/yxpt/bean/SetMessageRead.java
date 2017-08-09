package com.bbld.yxpt.bean;

/**
 * 清空已读
 * Created by dell on 2017/8/9.
 */

public class SetMessageRead {
    /**    "status": 0,
     "mes": "操作成功",
     "*/
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
