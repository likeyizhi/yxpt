package com.bbld.yxpt.bean;

/**
 * 消息数量
 * Created by dell on 2017/8/9.
 */

public class GetMessageCount {
/**    "status": 0,
 "mes": "操作成功",
 "count": 0*/
private int status;
    private String mes;
    private  int count;

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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
