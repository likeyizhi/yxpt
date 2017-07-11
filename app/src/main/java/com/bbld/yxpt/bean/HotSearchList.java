package com.bbld.yxpt.bean;

import java.util.List;

/**
 * 热搜关键词
 * Created by likey on 2017/7/10.
 */

public class HotSearchList {
    /**"status": 0,
     "mes": "操作成功",
     "list": []*/
    private int status;
    private String mes;
    private List<HotSearchListlist> list;

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

    public List<HotSearchListlist> getList() {
        return list;
    }

    public void setList(List<HotSearchListlist> list) {
        this.list = list;
    }

    public static class HotSearchListlist{
        /** "Text": "餐厅"*/
        private String Text;

        public String getText() {
            return Text;
        }

        public void setText(String text) {
            Text = text;
        }
    }
}
